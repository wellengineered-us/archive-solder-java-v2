/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.executive;

public interface ExecutableArgument<TValue>
{
	String getName();

	boolean isBounded();

	boolean isRequired();

	Class<? extends TValue> getValueClass();

	TValue getDefaultValue();
}
