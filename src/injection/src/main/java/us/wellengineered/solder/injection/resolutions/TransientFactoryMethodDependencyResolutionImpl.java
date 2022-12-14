/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.resolutions;

import us.wellengineered.solder.injection.DependencyLifetime;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.LifecycleUtils;

import java.util.function.Function;

public final class TransientFactoryMethodDependencyResolutionImpl<TResolution> extends AbstractDependencyResolution<TResolution>
{
	public TransientFactoryMethodDependencyResolutionImpl(Function<DependencyManager, TResolution> factoryMethod)
	{
		super(DependencyLifetime.TRANSIENT);
		this.factoryMethod = factoryMethod;
	}

	private final Function<DependencyManager, TResolution> factoryMethod;

	public Function<DependencyManager, TResolution> getFactoryMethod()
	{
		return this.factoryMethod;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		// do nothing ???
		// if (disposing) LifecycleUtils.maybeDispose(instance);
	}

	@Override
	protected TResolution coreResolve(DependencyManager dependencyManager, Class<? extends TResolution> resolutionClass, String selectorKey) throws Exception
	{
		TResolution instance;

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionType");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		instance = this.getFactoryMethod().apply(dependencyManager);

		LifecycleUtils.maybeCreate(instance);

		return instance;
	}
}
