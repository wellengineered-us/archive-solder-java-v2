/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.resolutions;

import us.wellengineered.solder.context.ContextScope;
import us.wellengineered.solder.context.ContextualStorageStrategy;
import us.wellengineered.solder.context.DefaultContextualStorageFactory;
import us.wellengineered.solder.injection.DependencyLifetime;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.injection.DependencyResolution;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.LifecycleUtils;

import java.util.ArrayList;
import java.util.List;

public final class SingletonContextWrapperDependencyResolutionImpl<TResolution> extends AbstractDependencyResolution<TResolution>
{
	public SingletonContextWrapperDependencyResolutionImpl(ContextScope contextScope, DependencyResolution<TResolution> innerDependencyResolution) throws Exception
	{
		this(new ArrayList<>(), new DefaultContextualStorageFactory().getContextualStorage(contextScope), innerDependencyResolution);
	}

	public SingletonContextWrapperDependencyResolutionImpl(List<String> trackedContextKeys, ContextualStorageStrategy contextualStorageStrategy, DependencyResolution<TResolution> innerDependencyResolution)
	{
		super(DependencyLifetime.SINGLETON);

		if (trackedContextKeys == null)
			throw new ArgumentNullException("trackedContextKeys");

		if (contextualStorageStrategy == null)
			throw new ArgumentNullException("contextualStorageStrategy");

		if (innerDependencyResolution == null)
			throw new ArgumentNullException("innerDependencyResolution");

		this.trackedContextKeys = trackedContextKeys;
		this.contextualStorageStrategy = contextualStorageStrategy;
		this.innerDependencyResolution = innerDependencyResolution;
	}
	private final ContextualStorageStrategy contextualStorageStrategy;
	private final DependencyResolution<TResolution> innerDependencyResolution;

	private final List<String> trackedContextKeys;

	public ContextualStorageStrategy getContextualStorageStrategy()
	{
		return this.contextualStorageStrategy;
	}

	private DependencyResolution<TResolution> getInnerDependencyResolution()
	{
		return this.innerDependencyResolution;
	}

	public List<String> getTrackedContextKeys()
	{
		return this.trackedContextKeys;
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
			for (String trackedContextKey : this.getTrackedContextKeys())
			{
				if (this.getContextualStorageStrategy().hasValue(trackedContextKey))
				{
					final Object value = this.getContextualStorageStrategy().getValue(Object.class, trackedContextKey);
					LifecycleUtils.maybeClose(value);
					this.getContextualStorageStrategy().removeValue(trackedContextKey);
				}
			}

			if (this.getInnerDependencyResolution() != null)
				this.getInnerDependencyResolution().dispose();
		}
	}

	@Override
	protected TResolution coreResolve(DependencyManager dependencyManager, Class<? extends TResolution> resolutionClass, String selectorKey) throws Exception
	{
		String contextKey;
		TResolution value;

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (resolutionClass == null)
			throw new ArgumentNullException("resolutionType");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		contextKey = String.format("%s_%s", resolutionClass.getName(), selectorKey);

		if (!this.getContextualStorageStrategy().hasValue(contextKey))
		{
			// NOTE: assuming dependency manager owns locking
			// NOTE: inner dependency resolution owns the instance
			value = this.getInnerDependencyResolution()
					.resolve(resolutionClass, dependencyManager, selectorKey);

			this.getContextualStorageStrategy().setValue(contextKey, resolutionClass, value);

			this.getTrackedContextKeys().add(contextKey);
		}
		else
			value = this.getContextualStorageStrategy().getValue(resolutionClass, contextKey);

		return value;
	}
}
