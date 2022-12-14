/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.configuration.Configurable;
import us.wellengineered.solder.configuration.SolderConfiguration;

public interface SolderComponent1<TSolderConfiguration extends SolderConfiguration> extends SolderComponent0, Configurable<TSolderConfiguration>
{
}
