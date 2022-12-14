/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

public interface ContextualStorageStrategy
{
	<TValue> TValue getValue(Class<? extends TValue> valueClass, String key) throws ContextException;

	boolean hasValue(String key) throws ContextException;

	void removeValue(String key) throws ContextException;

	<TValue> void setValue(String key, Class<? extends TValue> valueClass, TValue value) throws ContextException;
}
