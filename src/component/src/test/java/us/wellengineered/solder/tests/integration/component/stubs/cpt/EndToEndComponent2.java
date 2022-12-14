/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs.cpt;

import us.wellengineered.solder.component.SolderComponent2;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndSpecification;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownTypedConfiguration;

public interface EndToEndComponent2<TEndToEndConfiguration extends EndToEndUnknownTypedConfiguration<TEndToEndSpecification>, TEndToEndSpecification extends EndToEndSpecification> extends SolderComponent2<TEndToEndConfiguration, TEndToEndSpecification>, EndToEndComponent1<TEndToEndConfiguration>
{
}
