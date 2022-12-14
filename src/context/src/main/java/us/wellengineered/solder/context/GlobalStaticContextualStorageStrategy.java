/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;

import java.util.LinkedHashMap;
import java.util.Map;

public final class GlobalStaticContextualStorageStrategy extends AbstractContextualStorageStrategy
{
	public GlobalStaticContextualStorageStrategy()
	{
		this(LazySingleton.get());
	}

	public GlobalStaticContextualStorageStrategy(Map<String, Object> context)
	{
		if (context == null)
			throw new ArgumentNullException("context");

		this.context = context;
	}

	private final Map<String, Object> context;

	private Map<String, Object> getContext()
	{
		return this.context;
	}

	@Override
	@SuppressWarnings("unchecked")
	protected <TValue> TValue coreGetValue(Class<? extends TValue> valueClass, String key) throws Exception
	{
		Object value;
		value = this.getContext().get(key);
		return (TValue) value;
	}

	@Override
	protected boolean coreHasValue(String key) throws Exception
	{
		return this.getContext().containsKey(key);
	}

	@Override
	protected void coreRemoveValue(String key) throws Exception
	{
		this.getContext().remove(key);
	}

	@Override
	protected <TValue> void coreSetValue(String key, Class<? extends TValue> valueClass, TValue value) throws Exception
	{
		this.removeValue(key);
		this.getContext().put(key, value);
	}

	public void resetValues()
	{
		this.getContext().clear();
	}

	/**
	 * https://en.wikipedia.org/wiki/Initialization-on-demand_holder_idiom
	 */
	private static class LazySingleton
	{
		private static final Map<String, Object> __ = new LinkedHashMap<>();

		public static Map<String, Object> get()
		{
			return __;
		}

		static
		{
		}
	}
}
