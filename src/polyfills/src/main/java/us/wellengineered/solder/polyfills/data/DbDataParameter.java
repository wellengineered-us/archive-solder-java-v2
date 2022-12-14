/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

public interface DbDataParameter extends DataParameter
{
	Byte getPrecision();

	Byte getScale();

	Integer getSize();
}
