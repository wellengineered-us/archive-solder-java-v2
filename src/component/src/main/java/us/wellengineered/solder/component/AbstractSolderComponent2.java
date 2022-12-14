/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.configuration.SolderSpecification;
import us.wellengineered.solder.configuration.UnknownTypedSolderConfiguration;
import us.wellengineered.solder.configuration.UnknownUntypedSolderConfiguration;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;

import java.util.Map;

public abstract class AbstractSolderComponent2
		<TSolderConfiguration extends UnknownTypedSolderConfiguration<TSolderSpecification>, TSolderSpecification extends SolderSpecification>
		extends AbstractSolderComponent1<TSolderConfiguration>
		implements SolderComponent2<TSolderConfiguration, TSolderSpecification>
{
	protected AbstractSolderComponent2()
	{
	}

	private void assertValidSpecification()
	{
		if (this.getSpecification() == null)
			throw new InvalidOperationException(String.format("Component specification missing."));
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		UnknownUntypedSolderConfiguration /*TSolderConfiguration*/ unknownUntypedConfiguration;
		UnknownTypedSolderConfiguration<TSolderSpecification> unknownTypedConfiguration;
		Map<String, Object> untypedSpecification;
		TSolderSpecification typedSpecification;
		Class<? extends TSolderSpecification> specificationClass;

		if (creating)
		{
			super.coreCreate(creating);

			unknownUntypedConfiguration = this.getConfiguration();

			if (unknownUntypedConfiguration == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "unknownUntypedConfiguration"));

			untypedSpecification = unknownUntypedConfiguration.getUntypedSpecification();

			if (untypedSpecification == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "untypedSpecification"));

			specificationClass = this.getSpecificationClass();

			if (specificationClass == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "specificationClass"));

			unknownTypedConfiguration = this.coreCreateTypedUnknownConfiguration(unknownUntypedConfiguration);

			if (unknownTypedConfiguration == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "unknownTypedConfiguration"));

			typedSpecification = unknownTypedConfiguration.getTypedSpecification();

			if (typedSpecification == null)
				throw new InvalidOperationException(String.format("Configuration missing: '%s'.", "typedSpecification"));

			this.setSpecification(typedSpecification);

			this.assertValidSpecification();
		}
	}

	protected abstract UnknownTypedSolderConfiguration<TSolderSpecification> coreCreateTypedUnknownConfiguration(UnknownUntypedSolderConfiguration unknownUntypedConfiguration);

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			this.setSpecification(null);
			this.setConfiguration(null);

			super.coreDispose(disposing);
		}
	}

	private TSolderSpecification specification;

	@Override
	public TSolderSpecification getSpecification()
	{
		return this.specification;
	}

	@Override
	public void setSpecification(TSolderSpecification specification)
	{
		this.specification = specification;
	}
}
