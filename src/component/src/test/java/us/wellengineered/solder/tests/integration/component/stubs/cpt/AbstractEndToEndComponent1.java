/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs.cpt;

import us.wellengineered.solder.component.AbstractSolderComponent1;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndConfiguration;

public abstract class AbstractEndToEndComponent1<TEndToEndConfiguration extends EndToEndConfiguration> extends AbstractSolderComponent1<TEndToEndConfiguration> implements EndToEndComponent1<TEndToEndConfiguration>
{
	protected AbstractEndToEndComponent1()
	{
	}
}
