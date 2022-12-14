/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.wellengineered.solder.component.SolderComponent0;
import us.wellengineered.solder.component.SolderComponent1;
import us.wellengineered.solder.component.SolderComponent2;
import us.wellengineered.solder.tests.integration.component.stubs.*;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownTypedConfiguration;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownTypedConfigurationImpl;
import us.wellengineered.solder.tests.integration.component.stubs.cfg.EndToEndUnknownUntypedConfigurationImpl;

public class EndToEndTests
{
	public EndToEndTests()
	{
	}

	@Test
	public void shouldCreateTest() throws Exception
	{
		try (SolderComponent0 solderComponent = new DogFoodComponent0())
		{
			solderComponent.create();
		}

		try (SolderComponent1<DogFoodConfiguration> solderComponent = new DogFoodComponent1())
		{
			DogFoodConfiguration configuration;

			configuration = new DogFoodConfiguration();

			solderComponent.setConfiguration(configuration);
			solderComponent.create();

			configuration = solderComponent.getConfiguration();
			
			Assertions.assertNotNull(configuration);
		}

		try (SolderComponent2<EndToEndUnknownTypedConfiguration<DogFoodSpecification>, DogFoodSpecification> solderComponent = new DogFoodComponent2())
		{
			EndToEndUnknownUntypedConfigurationImpl that;

			that = new EndToEndUnknownUntypedConfigurationImpl(DogFoodSpecification.class);

			that.getUntypedSpecification().put((DogFoodSpecification.PROP_A), "test");
			that.getUntypedSpecification().put((DogFoodSpecification.PROP_B), 100);
			that.getUntypedSpecification().put((DogFoodSpecification.PROP_C), true);
			that.getUntypedSpecification().put((DogFoodSpecification.PROP_D), 10.50);

			var baseSpecification = that.getBaseSpecification();

			Assertions.assertNotNull(baseSpecification);
			Assertions.assertInstanceOf(DogFoodSpecification.class, baseSpecification);

			Assertions.assertEquals("test", ((DogFoodSpecification)baseSpecification).getPropA());
			Assertions.assertEquals(100, ((DogFoodSpecification)baseSpecification).getPropB());
			Assertions.assertEquals(true, ((DogFoodSpecification)baseSpecification).getPropC());
			Assertions.assertEquals(10.50, ((DogFoodSpecification)baseSpecification).getPropD());

			EndToEndUnknownTypedConfiguration<DogFoodSpecification> configuration;

			configuration = new EndToEndUnknownTypedConfigurationImpl<DogFoodSpecification>(that, DogFoodSpecification.class);
			solderComponent.setConfiguration(configuration);
			solderComponent.create();

			configuration = solderComponent.getConfiguration();

			Assertions.assertNotNull(configuration);

			Assertions.assertEquals("test", configuration.getTypedSpecification().getPropA());
			Assertions.assertEquals(100, configuration.getTypedSpecification().getPropB());
			Assertions.assertEquals(true, configuration.getTypedSpecification().getPropC());
			Assertions.assertEquals(10.50, configuration.getTypedSpecification().getPropD());

			DogFoodSpecification specification;

			specification = solderComponent.getSpecification();

			Assertions.assertNotNull(specification);

			Assertions.assertEquals("test", specification.getPropA());
			Assertions.assertEquals(100, specification.getPropB());
			Assertions.assertEquals(true, specification.getPropC());
			Assertions.assertEquals(10.50, specification.getPropD());
		}
	}
}
