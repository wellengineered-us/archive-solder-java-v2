/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.resolutions;

import us.wellengineered.solder.injection.DependencyLifetime;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.LifecycleUtils;

public final class InstanceDependencyResolutionImpl<TResolution> extends AbstractDependencyResolution<TResolution>
{
	public InstanceDependencyResolutionImpl(TResolution instance)
	{
		super(DependencyLifetime.INSTANCE);
		this.instance = instance;
	}

	private final TResolution instance;

	public TResolution getInstance()
	{
		return this.instance;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		if (creating)
			LifecycleUtils.maybeCreate(this.getInstance());
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
			LifecycleUtils.maybeDispose(this.getInstance());
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

		return this.getInstance(); // simply return instance
	}
}
