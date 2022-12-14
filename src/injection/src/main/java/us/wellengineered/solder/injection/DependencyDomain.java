/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;
import us.wellengineered.solder.polyfills.exceptions.ObjectCreatedException;
import us.wellengineered.solder.polyfills.exceptions.ObjectDisposedException;
import us.wellengineered.solder.primitives.AbstractLifecycle;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Serves as a logical run-time boundary for dependencies.
 */
public final class DependencyDomain extends AbstractLifecycle<Exception, Exception>
{
	public DependencyDomain() throws Exception
	{
		this(new DependencyManagerImpl(), new ResourceManagerImpl(), new ReentrantReadWriteLock());
	}

	protected DependencyDomain(DependencyManager dependencyManager, ResourceManager resourceManager, ReadWriteLock readerWriterLock) throws Exception
	{
		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (resourceManager == null)
			throw new ArgumentNullException("resourceManager");

		if (readerWriterLock == null)
			throw new ArgumentNullException("readerWriterLock");

		this.dependencyManager = dependencyManager;
		this.resourceManager = resourceManager;
		this.readerWriterLock = readerWriterLock;

		this.dependencyManager.create();
		this.resourceManager.create();
	}

	private final DependencyManager dependencyManager;
	private final ReadWriteLock readerWriterLock;
	private final ResourceManager resourceManager;

	public static DependencyDomain getDefault()
	{
		DependencyDomain instance = LazySingleton.get();

		// the only way this is null is if this accessor is invoked AGAIN while the lazy singleton is being constructed
		if (instance == null)
			throw new FailFastException("Not re-entrant; getter is invoked AGAIN while the lazy singleton is being constructed.");

		return instance;
	}

	public DependencyManager getDependencyManager()
	{
		return this.dependencyManager;
	}

	private ReadWriteLock getReaderWriterLock()
	{
		return this.readerWriterLock;
	}

	public ResourceManager getResourceManager()
	{
		return this.resourceManager;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		if (!creating)
			return;

		// cop a reader lock (initial)
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (this.isCreated())
				throw new ObjectCreatedException(DependencyDomain.class.getName());

			if (this.isDisposed())
				throw new ObjectDisposedException(DependencyDomain.class.getName());

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				// special case here since this class will execute under multithreaded scenarios
				this.explicitSetCreated();

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

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (!disposing)
			return;

		// cop a reader lock (initial)
		this.getReaderWriterLock().readLock().lock();

		try
		{
			if (this.isDisposed())
				return;

			//  need to upgrade: releasing read lock first (NOT upgradable in JRE)
			this.getReaderWriterLock().readLock().unlock();

			// !!! DEADLOCK ZONE !!!

			// upgrade: cop a writer lock
			this.getReaderWriterLock().writeLock().lock();

			try
			{
				final DependencyManager dependencyManager = this.getDependencyManager();

				if (dependencyManager != null)
					dependencyManager.dispose();

				final ResourceManager resourceManager = this.getResourceManager();

				if (resourceManager != null)
					resourceManager.dispose();

				// need to downgrade: cop a reader lock (again)
				this.getReaderWriterLock().readLock().lock();
			}
			finally
			{
				// special case here since this class wil execute under multithreaded scenarios
				this.explicitSetDisposed();

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
	protected void maybeSetCreated()
	{
		// do nothing??
	}

	@Override
	protected void maybeSetDisposed()
	{
		// do nothing??
	}

	/**
	 * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
	 */
	private static class LazySingleton
	{
		private static final DependencyDomain __ = newUp();

		public static DependencyDomain get()
		{
			return __;
		}

		private static DependencyDomain newUp()
		{
			DependencyDomain dependencyDomain;

			try
			{
				dependencyDomain = new DependencyDomain();
				dependencyDomain.create();

				return dependencyDomain;
			}
			catch (Exception e)
			{
				throw new FailFastException(e);
			}
		}

		static
		{
		}
	}
}
