/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

@FunctionalInterface
public interface RecordsAffectedDelegate
{
	void invoke(long recordsAffected) throws Exception;
}
