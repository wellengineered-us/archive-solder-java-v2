/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

public interface Configurable<TConfiguration extends SolderConfiguration>
{
	TConfiguration getConfiguration();

	void setConfiguration(TConfiguration configuration);
}
