/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs.cpt;

import us.wellengineered.solder.component.AbstractSolderComponent2;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndSpecification;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownTypedConfiguration;

public abstract class AbstractEndToEndComponent2<TEndToEndConfiguration extends EndToEndUnknownTypedConfiguration<TEndToEndSpecification>, TEndToEndSpecification extends EndToEndSpecification> extends AbstractSolderComponent2<TEndToEndConfiguration, TEndToEndSpecification> implements EndToEndComponent2<TEndToEndConfiguration, TEndToEndSpecification>
{
	protected AbstractEndToEndComponent2()
	{
	}
}
