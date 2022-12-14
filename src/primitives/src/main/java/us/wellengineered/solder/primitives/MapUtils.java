/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;

import java.util.Map;

public final class MapUtils
{
	public static <K, V> boolean tryMapGetValue(Map<K, V> map, K key, MethodParameterModifier.Out<V> outValue)
	{
		boolean result;
		V value;

		if (map == null)
			throw new ArgumentNullException("map");

		if (outValue == null)
			throw new ArgumentNullException("outValue");

		if (!map.containsKey(key))
		{
			value = null;
			result = false;
		}
		else
		{
			value = map.get(key);
			result = true;
		}

		outValue.setValue(value);
		return result;
	}

	public static <K, V> boolean tryMapGetValueOrPutDefault(Map<K, V> map, K key, MethodParameterModifier.Ref<V> refValue)
	{
		boolean result;
		V value;

		if (map == null)
			throw new ArgumentNullException("map");

		if (refValue == null)
			throw new ArgumentNullException("refValue");

		if (!map.containsKey(key))
		{
			map.put(key, (value = refValue.getValue()));
			result = false;
		}
		else
		{
			value = map.get(key);
			result = true;
		}

		refValue.setValue(value);
		return result;
	}

	/*public static <K, V> boolean tryPutValue(Map<K, V> thisMap, K key, V value)
	{
		if (thisMap == null)
			throw new ArgumentNullException("thisMap");

		if(thisMap.containsKey(key))
			return false;

		thisMap.put(key, value);
		return true;
	}*/
}
