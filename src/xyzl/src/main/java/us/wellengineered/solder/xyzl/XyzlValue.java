/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

/**
 * Represents a XYZL object and it's "name".
 */
public interface XyzlValue<TValue> extends XyzlModel
{
	TValue getValue(Class<? extends TValue> valueClass);

	void setValue(Class<? extends TValue> valueClass, TValue value);
}
