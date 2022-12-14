/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.resolutions;

import us.wellengineered.solder.injection.DependencyException;
import us.wellengineered.solder.injection.DependencyLifetime;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.injection.DependencyResolution;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.AbstractLifecycle;

public abstract class AbstractDependencyResolution<TResolution> extends AbstractLifecycle<Exception, Exception> implements DependencyResolution<TResolution>
{
	protected AbstractDependencyResolution(DependencyLifetime dependencyLifetime)
	{
		this.dependencyLifetime = dependencyLifetime;
	}

	private final DependencyLifetime dependencyLifetime;

	@Override
	public final DependencyLifetime getDependencyLifetime()
	{
		return this.dependencyLifetime;
	}

	protected abstract TResolution coreResolve(DependencyManager dependencyManager, Class<? extends TResolution> resolutionClass, String selectorKey) throws Exception;

	@Override
	public final TResolution resolve(Class<? extends TResolution> resolutionClass, DependencyManager dependencyManager, String selectorKey) throws DependencyException
	{
		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionType");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		try
		{
			return this.coreResolve(dependencyManager, resolutionClass, selectorKey);
		}
		catch (Exception ex)
		{
			throw new DependencyException(String.format("The dependency resolution failed (see inner exception)."), ex);
		}
	}
}
