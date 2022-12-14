/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.NotSupportedException;
import us.wellengineered.solder.polyfills.exceptions.ObjectCreatedException;
import us.wellengineered.solder.polyfills.exceptions.ObjectDisposedException;
import us.wellengineered.solder.polyfills.language.Tuple;
import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class DependencyManagerImpl extends AbstractLifecycle<Exception, Exception> implements DependencyManager
{
	public DependencyManagerImpl()
	{
		this(new LinkedHashMap<>(), new ReentrantReadWriteLock());
	}

	protected DependencyManagerImpl(Map<Tuple.Arity2<Class<?>, String>, DependencyResolution> dependencyResolutionRegistrations, ReadWriteLock readerWriterLock)
	{
		if (dependencyResolutionRegistrations == null)
			throw new ArgumentNullException("dependencyResolutionRegistrations");

		if (readerWriterLock == null)
			throw new ArgumentNullException("readerWriterLock");

		this.dependencyResolutionRegistrations = dependencyResolutionRegistrations;
		this.readerWriterLock = readerWriterLock;
	}

	private final Map<Tuple.Arity2<Class<?>, String>, DependencyResolution> dependencyResolutionRegistrations;
	private final ReadWriteLock readerWriterLock;

	public Map<Tuple.Arity2<Class<?>, String>, DependencyResolution> getDependencyResolutionRegistrations()
	{
		return this.dependencyResolutionRegistrations;
	}

	private ReadWriteLock getReaderWriterLock()
	{
		return this.readerWriterLock;
	}

	@Override
	public final <TResolution> void addResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes, DependencyResolution<? extends TResolution> dependencyResolution) throws DependencyException
	{
		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		if (dependencyResolution == null)
			throw new ArgumentNullException("dependencyResolution");

		try
		{
			this.coreAddResolution(resolutionClass, selectorKey, includeAssignableTypes, dependencyResolution);
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency manager failed (see inner exception)."), ex);
		}
	}

	@Override
	public final boolean clearAllResolutions() throws DependencyException
	{
		try
		{
			return this.coreClearAllResolutions();
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency manager failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TResolution> boolean clearResolutions(Class<? extends TResolution> resolutionClass, boolean includeAssignableTypes) throws DependencyException
	{
		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		try
		{
			return this.coreClearResolutions(resolutionClass, includeAssignableTypes);
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency manager failed (see inner exception)."), ex);
		}
	}

	private final <TResolution> void coreAddResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes, DependencyResolution<? extends TResolution> dependencyResolution) throws Exception
	{
		Tuple.Arity2<Class<?>, String> trait;
		Set<Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution<TResolution>>> candidateResolutions;

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		// cop a writer lock
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (!this.isCreated())
				throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				trait = new Tuple.Arity2<Class<?>, String>(resolutionClass, selectorKey);
				candidateResolutions = this.getCandidateResolutionsMustReadLock(resolutionClass, selectorKey, includeAssignableTypes);

				if (candidateResolutions.size() > 0)
				{
					// need to downgrade: cop a reader lock (again)
					this.getReaderWriterLock().readLock().lock();

					throw new DependencyException(String.format("Dependency resolution already exists in the dependency manager for resolution type '%s' and selector key '%s' (include assignable types: '%s').", resolutionClass.getName(), selectorKey, includeAssignableTypes));
				}

				this.getDependencyResolutionRegistrations().put(trait, dependencyResolution);

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private boolean coreClearAllResolutions() throws Exception
	{
		boolean result;

		// cop a writer lock
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (!this.isCreated())
				throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				result = this.freeDependencyResolutionsMustReadLock();

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();

				return result;
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private <TResolution> boolean coreClearResolutions(Class<? extends TResolution> resolutionClass, boolean includeAssignableTypes) throws Exception
	{
		int count = 0;
		Tuple.Arity2<Class<?>, String> trait;
		Set<Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution<TResolution>>> candidateResolutions;
		final String selectorKey = StringUtils.EMPTY_STRING;

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		// cop a writer lock
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (!this.isCreated())
				throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				trait = new Tuple.Arity2<Class<?>, String>(resolutionClass, selectorKey);
				candidateResolutions = this.getCandidateResolutionsMustReadLock(resolutionClass, selectorKey, includeAssignableTypes);

				if (candidateResolutions.size() > 0)
				{
					// need to downgrade: cop a reader lock (again)
					this.getReaderWriterLock().readLock().lock();

					throw new DependencyException(String.format("Dependency resolution already exists in the dependency manager for resolution type '%s' and selector key '%s' (include assignable types: '%s').", resolutionClass.getName(), selectorKey, includeAssignableTypes));
				}

				for (var dependencyResolutionRegistration : candidateResolutions)
				{
					final var key = dependencyResolutionRegistration.getKey();
					final var value = dependencyResolutionRegistration.getValue();

					if (dependencyResolutionRegistration != null &&
							value != null)
						value.dispose();

					this.getDependencyResolutionRegistrations().remove(key);

					count++;
				}

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();

				return count > 0;
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			// cop a writer lock
			this.getReaderWriterLock().readLock().lock();

			try
			{
				if (!this.isCreated())
					throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

				if (this.isDisposed())
					throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

				//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
				this.getReaderWriterLock().readLock().unlock();

				// !!! DEADLOCK ZONE !!!

				// upgrade: cop a writer lock
				this.getReaderWriterLock().writeLock().lock();

				try
				{
					this.freeDependencyResolutionsMustReadLock();

					// need to downgrade: cop a reader lock (again)
					this.getReaderWriterLock().readLock().lock();
				}
				finally
				{
					// downgrade: release writer lock
					this.getReaderWriterLock().writeLock().unlock();
				}
			}
			finally
			{
				//  exit: releasing read lock last
				this.getReaderWriterLock().readLock().unlock();
			}
		}
	}

	private <TResolution> boolean coreHasResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws Exception
	{
		DependencyResolution dependencyResolution;

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		// selector key can be null in this context
		//if (selectorKey == null)
		//throw new ArgumentNullException("selectorKey");

		// cop a writer lock
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (!this.isCreated())
				throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				dependencyResolution = this.getDependencyResolutionMustReadLock(resolutionClass, selectorKey, includeAssignableTypes);

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();

				return dependencyResolution != null;
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private <TResolution> void coreRemoveResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws Exception
	{
		Tuple.Arity2<Class<? extends TResolution>, String> trait;
		DependencyResolution dependencyResolution;

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		// cop a writer lock
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (!this.isCreated())
				throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				trait = new Tuple.Arity2<>(resolutionClass, selectorKey);
				dependencyResolution = this.getDependencyResolutionMustReadLock(resolutionClass, selectorKey, includeAssignableTypes);

				if (dependencyResolution == null) // nothing to offer up
				{
					// need to downgrade: cop a reader lock (again)
					this.getReaderWriterLock().readLock().lock();

					throw new DependencyException(String.format("Dependency resolution in the in-effect dependency manager failed to match for resolution type '%s' and selector key '%s' (include assignable types: '%s').", resolutionClass.getName(), selectorKey, includeAssignableTypes));
				}

				dependencyResolution.dispose();
				this.getDependencyResolutionRegistrations().remove(trait);

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private <TResolution> TResolution coreResolveDependency(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws Exception
	{
		TResolution value;
		DependencyResolution<TResolution> dependencyResolution;
		Class<?> classOfValue;

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		// cop a writer lock
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (!this.isCreated())
				throw new ObjectCreatedException(DependencyManagerImpl.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyManagerImpl.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				dependencyResolution = this.getDependencyResolutionMustReadLock(resolutionClass, selectorKey, includeAssignableTypes);

				if (dependencyResolution == null) // nothing to offer up
				{
					// need to downgrade: cop a reader lock (again)
					this.getReaderWriterLock().readLock().lock();

					throw new DependencyException(String.format("Dependency resolution in the in-effect dependency manager failed to match for resolution type '%s' and selector key '%s' (include assignable types: '%s').", resolutionClass.getName(), selectorKey, includeAssignableTypes));
				}

				value = dependencyResolution.resolve(resolutionClass, this, selectorKey);

				if (value != null)
				{
					classOfValue = value.getClass();

					if (!resolutionClass.isAssignableFrom(classOfValue))
					{
						// need to downgrade: cop a reader lock (again)
						this.getReaderWriterLock().readLock().lock();

						throw new DependencyException(String.format("Dependency resolution in the dependency manager matched for resolution type '%s' and selector key '%s' but the resolved value type '%s' is not assignable the resolution type '%s'.", resolutionClass.getName(), selectorKey, classOfValue.getName(), resolutionClass.getName()));
					}
				}

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();

				return value;
			}
			finally
			{
				// downgrade: release writer lock
				this.getReaderWriterLock().writeLock().unlock();
			}
		}
		finally
		{
			//  exit: releasing read lock last
			this.getReaderWriterLock().readLock().unlock();
		}
	}

	private boolean freeDependencyResolutionsMustReadLock() throws Exception
	{
		boolean result;

		result = this.getDependencyResolutionRegistrations().size() > 0;

		for (Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution> dependencyResolutionRegistration : this.getDependencyResolutionRegistrations().entrySet())
		{
			if (dependencyResolutionRegistration != null &&
					dependencyResolutionRegistration.getValue() != null)
				dependencyResolutionRegistration.getValue().dispose();
		}

		this.getDependencyResolutionRegistrations().clear();

		return result;
	}

	private <TResolution> Set<Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution<TResolution>>> getCandidateResolutionsMustReadLock(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes)
	{
		Set<Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution<TResolution>>> candidateResolutions;

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		// selector key can be null in this context
		//if (selectorKey == null)
		//throw new ArgumentNullException("selectorKey");

		candidateResolutions = new HashSet<>();

		for (var dependencyResolutionRegistration : this.getDependencyResolutionRegistrations().entrySet())
		{
			final var key = dependencyResolutionRegistration.getKey();
			final var value = dependencyResolutionRegistration.getValue();

			if (key == null || value == null)
				continue;

			if (key.getValue1().equals(resolutionClass) ||
					(includeAssignableTypes && resolutionClass.isAssignableFrom(key.getValue1())) &&
							(selectorKey == null || key.getValue2().equalsIgnoreCase(selectorKey)))
			{
				candidateResolutions.add(new Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution<TResolution>>()
				{
					@Override
					public Tuple.Arity2<Class<?>, String> getKey()
					{
						return key;
					}

					@Override
					public DependencyResolution<TResolution> getValue()
					{
						return value;
					}

					@Override
					public DependencyResolution<TResolution> setValue(DependencyResolution<TResolution> value)
					{
						throw new NotSupportedException();
					}
				});
			}
		}

		return candidateResolutions;
	}

	private <TResolution> DependencyResolution<TResolution> getDependencyResolutionMustReadLock(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes)
	{
		Set<Map.Entry<Tuple.Arity2<Class<?>, String>, DependencyResolution<TResolution>>> candidateResolutions;
		DependencyResolution<TResolution> dependencyResolution = null;
		Tuple.Arity2<Class<? extends TResolution>, String> trait;

		// selector key can be null in this context
		//if (selectorKey == null)
		//throw new ArgumentNullException("selectorKey");

		trait = new Tuple.Arity2<>(resolutionClass, selectorKey);
		candidateResolutions = this.getCandidateResolutionsMustReadLock(resolutionClass, selectorKey, includeAssignableTypes);

		// first attempt direct resolution: exact type and selector key
		for (var candidateResolution : candidateResolutions)
		{
			final var key = candidateResolution.getKey();
			final var value = candidateResolution.getValue();

			if (key == null || value == null)
				continue;

			if ((key.getValue1().equals(resolutionClass) ||
					(includeAssignableTypes && resolutionClass.isAssignableFrom(key.getValue1()))) &&
							(selectorKey == null || key.getValue2().equalsIgnoreCase(selectorKey)))
			{
				dependencyResolution = candidateResolution.getValue();
				break;
			}
		}

		return dependencyResolution;
	}

	@Override
	public final <TResolution> boolean hasResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws DependencyException
	{
		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		try
		{
			return this.coreHasResolution(resolutionClass, selectorKey, includeAssignableTypes);
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency manager failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TResolution> void removeResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws DependencyException
	{
		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		try
		{
			this.coreRemoveResolution(resolutionClass, selectorKey, includeAssignableTypes);
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency manager failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TResolution> TResolution resolveDependency(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws DependencyException
	{
		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionClass");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		try
		{
			return this.coreResolveDependency(resolutionClass, selectorKey, includeAssignableTypes);
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency manager failed (see inner exception)."), ex);
		}
	}
}
