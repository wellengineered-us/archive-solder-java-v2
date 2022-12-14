/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.resolutions;

import us.wellengineered.solder.injection.DependencyException;
import us.wellengineered.solder.injection.DependencyLifetime;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.LifecycleUtils;
import us.wellengineered.solder.primitives.ReflectionUtils;

public final class TransientDefaultConstructorDependencyResolutionImpl<TResolution> extends AbstractDependencyResolution<TResolution>
{
	public TransientDefaultConstructorDependencyResolutionImpl(Class<? extends TResolution> instanceClass)
	{
		super(DependencyLifetime.TRANSIENT);
		this.instanceClass = instanceClass;
	}

	private final Class<? extends TResolution> instanceClass;

	public Class<? extends TResolution> getInstanceClass()
	{
		return this.instanceClass;
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

		if (!resolutionClass.isAssignableFrom(this.getInstanceClass()))
			throw new DependencyException(String.format("Resolution class '%s' is not assignable from activator class '%s'; selector key '%s'.", resolutionClass.getName(), this.getInstanceClass().getName(), selectorKey));

		instance = ReflectionUtils.createInstanceAssignableToClass(this.getInstanceClass());

		LifecycleUtils.maybeCreate(instance);

		return instance;
	}
}
