/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs.cfg;

import us.wellengineered.solder.component.AbstractUnknownTypedSolderConfiguration;
import us.wellengineered.solder.configuration.SolderSpecification;
import us.wellengineered.solder.configuration.UnknownUntypedSolderConfiguration;
import us.wellengineered.solder.serialization.NativeJsonSerializationStrategyImpl;

import java.util.Map;

public class EndToEndUnknownTypedConfigurationImpl<TEndToEndSpecification extends EndToEndSpecification> extends AbstractUnknownTypedSolderConfiguration<TEndToEndSpecification> implements EndToEndUnknownTypedConfiguration<TEndToEndSpecification>
{
	public EndToEndUnknownTypedConfigurationImpl(UnknownUntypedSolderConfiguration unknownUntypedSolderConfiguration, Class<? extends TEndToEndSpecification> typedSpecificationClass)
	{
		super(unknownUntypedSolderConfiguration, typedSpecificationClass);
	}

	public EndToEndUnknownTypedConfigurationImpl(Map<String, Object> untypedSpecification, UnknownUntypedSolderConfiguration unknownUntypedSolderConfiguration, Class<? extends TEndToEndSpecification> typedSpecificationClass)
	{
		super(untypedSpecification, unknownUntypedSolderConfiguration, typedSpecificationClass);
	}

	@Override
	protected SolderSpecification coreBaseObjectFromJsonMap()
	{
		return NativeJsonSerializationStrategyImpl.getObjectFromJsonMap(this.getUntypedSpecification(), this.getBaseSpecificationClass());
	}

	@Override
	protected TEndToEndSpecification coreTypedObjectFromJsonMap()
	{
		return NativeJsonSerializationStrategyImpl.getObjectFromJsonMap(this.getUntypedSpecification(), this.getTypedSpecificationClass());
	}
}
