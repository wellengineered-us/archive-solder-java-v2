/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs;

import us.wellengineered.solder.configuration.UnknownTypedSolderConfiguration;
import us.wellengineered.solder.configuration.UnknownUntypedSolderConfiguration;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownTypedConfiguration;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownTypedConfigurationImpl;
import us.wellengineered.solder.tests.integration.component.stubs.cpt.AbstractEndToEndComponent2;

public class DogFoodComponent2 extends AbstractEndToEndComponent2<EndToEndUnknownTypedConfiguration<DogFoodSpecification>, DogFoodSpecification>
{
	public DogFoodComponent2()
	{
	}

	@Override
	public Class<? extends DogFoodSpecification> getSpecificationClass()
	{
		return DogFoodSpecification.class;
	}

	@Override
	protected UnknownTypedSolderConfiguration<DogFoodSpecification> coreCreateTypedUnknownConfiguration(UnknownUntypedSolderConfiguration unknownUntypedConfiguration)
	{
		return new EndToEndUnknownTypedConfigurationImpl<DogFoodSpecification>(unknownUntypedConfiguration, DogFoodSpecification.class);
	}
}
