/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

public abstract class AbstractContextualStorageStrategy implements ContextualStorageStrategy
{
	protected AbstractContextualStorageStrategy()
	{
	}

	protected abstract <TValue> TValue coreGetValue(Class<? extends TValue> valueClass, String key) throws Exception;

	protected abstract boolean coreHasValue(String key) throws Exception;

	protected abstract void coreRemoveValue(String key) throws Exception;

	protected abstract <TValue> void coreSetValue(String key, Class<? extends TValue> valueClass, TValue value) throws Exception;

	@Override
	public final <TValue> TValue getValue(Class<? extends TValue> valueClass, String key) throws ContextException
	{
		try
		{
			return this.coreGetValue(valueClass, key);
		}
		catch (Exception ex)
		{
			throw new ContextException(String.format("The contextual storage strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final boolean hasValue(String key) throws ContextException
	{
		try
		{
			return this.coreHasValue(key);
		}
		catch (Exception ex)
		{
			throw new ContextException(String.format("The contextual storage strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final void removeValue(String key) throws ContextException
	{
		try
		{
			this.coreRemoveValue(key);
		}
		catch (Exception ex)
		{
			throw new ContextException(String.format("The contextual storage strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TValue> void setValue(String key, Class<? extends TValue> valueClass, TValue value) throws ContextException
	{
		try
		{
			this.coreSetValue(key, valueClass, value);
		}
		catch (Exception ex)
		{
			throw new ContextException(String.format("The contextual storage strategy failed (see inner exception)."), ex);
		}
	}
}
