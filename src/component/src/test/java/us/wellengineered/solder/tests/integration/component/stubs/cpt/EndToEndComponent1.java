/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs.cpt;

import us.wellengineered.solder.component.SolderComponent1;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndConfiguration;

public interface EndToEndComponent1<TEndToEndConfiguration extends EndToEndConfiguration> extends SolderComponent1<TEndToEndConfiguration>, EndToEndComponent0
{
}
