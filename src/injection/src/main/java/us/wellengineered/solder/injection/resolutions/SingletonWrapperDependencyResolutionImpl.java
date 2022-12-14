/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.resolutions;

import us.wellengineered.solder.injection.DependencyLifetime;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.injection.DependencyManagerImpl;
import us.wellengineered.solder.injection.DependencyResolution;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.ObjectCreatedException;
import us.wellengineered.solder.polyfills.exceptions.ObjectDisposedException;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public final class SingletonWrapperDependencyResolutionImpl<TResolution> extends AbstractDependencyResolution<TResolution>
{
	public SingletonWrapperDependencyResolutionImpl(DependencyResolution<TResolution> innerDependencyResolution)
	{
		super(DependencyLifetime.SINGLETON);

		if (innerDependencyResolution == null)
			throw new ArgumentNullException("innerDependencyResolution");

		this.innerDependencyResolution = innerDependencyResolution;
	}

	private final DependencyResolution<TResolution> innerDependencyResolution;

	private TResolution instance;
	private boolean isFrozen;

	private DependencyResolution<TResolution> getInnerDependencyResolution()
	{
		return this.innerDependencyResolution;
	}

	private TResolution getInstance()
	{
		return this.instance;
	}

	private void setInstance(TResolution instance)
	{
		this.instance = instance;
	}

	private boolean isFrozen()
	{
		return this.isFrozen;
	}

	private void setFrozen(boolean frozen)
	{
		isFrozen = frozen;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		if (creating)
		{
			if (this.getInnerDependencyResolution() != null)
				this.getInnerDependencyResolution().create();
		}
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			if (this.getInnerDependencyResolution() != null)
				this.getInnerDependencyResolution().dispose();
		}
	}

	@Override
	protected TResolution coreResolve(DependencyManager dependencyManager, Class<? extends TResolution> resolutionClass, String selectorKey) throws Exception
	{
		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionType");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		if (!this.isFrozen())
		{
			// NOTE: assuming dependency manager owns locking
			// NOTE: inner dependency resolution owns the instance
			final TResolution instance = this.getInnerDependencyResolution()
					.resolve(resolutionClass, dependencyManager, selectorKey);

			this.setInstance(instance);
			this.setFrozen(true);
		}

		return this.getInstance();
	}
}
