/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs.cfg;

import us.wellengineered.solder.component.AbstractUnknownUntypedSolderConfiguration;
import us.wellengineered.solder.configuration.SolderSpecification;
import us.wellengineered.solder.serialization.NativeJsonSerializationStrategyImpl;

import java.util.Map;

public class EndToEndUnknownUntypedConfigurationImpl extends AbstractUnknownUntypedSolderConfiguration implements EndToEndUnknownUntypedConfiguration
{
	public EndToEndUnknownUntypedConfigurationImpl(Class<? extends SolderSpecification> baseSpecificationClass)
	{
		super(baseSpecificationClass);
	}

	public EndToEndUnknownUntypedConfigurationImpl(Map<String, Object> untypedSpecification, Class<? extends SolderSpecification> baseSpecificationClass)
	{
		super(untypedSpecification, baseSpecificationClass);
	}

	@Override
	protected SolderSpecification coreBaseObjectFromJsonMap()
	{
		return NativeJsonSerializationStrategyImpl.getObjectFromJsonMap(this.getUntypedSpecification(), this.getBaseSpecificationClass());
	}
}
