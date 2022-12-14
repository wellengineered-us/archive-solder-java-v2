/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.configuration.SolderSpecification;
import us.wellengineered.solder.configuration.UnknownTypedSolderConfiguration;
import us.wellengineered.solder.configuration.UnknownUntypedSolderConfiguration;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractUnknownTypedSolderConfiguration<TSolderSpecification extends SolderSpecification> extends AbstractUnknownUntypedSolderConfiguration implements UnknownTypedSolderConfiguration<TSolderSpecification>
{
	public AbstractUnknownTypedSolderConfiguration(UnknownUntypedSolderConfiguration unknownUntypedSolderConfiguration, Class<? extends TSolderSpecification> typedSpecificationClass)
	{
		this(new LinkedHashMap<>(), unknownUntypedSolderConfiguration, typedSpecificationClass);
	}

	public AbstractUnknownTypedSolderConfiguration(Map<String, Object> untypedSpecification, UnknownUntypedSolderConfiguration unknownUntypedSolderConfiguration, Class<? extends TSolderSpecification> typedSpecificationClass)
	{
		super(untypedSpecification, typedSpecificationClass);

		if (untypedSpecification == null)
			throw new ArgumentNullException("untypedSpecification");

		if (unknownUntypedSolderConfiguration == null)
			throw new ArgumentNullException("unknownUntypedSolderConfiguration");

		if (typedSpecificationClass == null)
			throw new ArgumentNullException("typedSpecificationClass");

		if (this.getUntypedSpecification() != null &&
				unknownUntypedSolderConfiguration.getUntypedSpecification() != null)
		{
			final Map<String, Object> otherComponentSpecificConfiguration = unknownUntypedSolderConfiguration.getUntypedSpecification();
			for (Map.Entry<String, Object> entry : otherComponentSpecificConfiguration.entrySet())
			{
				if (entry == null)
					continue;

				this.getUntypedSpecification().put(entry.getKey(), entry.getValue());
			}
		}

		this.setComponentClassName(unknownUntypedSolderConfiguration.getComponentClassName());
		this.setParent(unknownUntypedSolderConfiguration.getParent());
		this.setSurround(unknownUntypedSolderConfiguration.getSurround());

		this.typedSpecificationClass = typedSpecificationClass;
	}

	private final Class<? extends TSolderSpecification> typedSpecificationClass;

	private TSolderSpecification typedSpecification;

	@Override
	public TSolderSpecification getTypedSpecification()
	{
		this.coreApplyTypedSpecification();
		return this.typedSpecification;
	}

	public void setTypedSpecification(TSolderSpecification typedSpecification)
	{
		this.ensureParentOnSetter(this.typedSpecification, typedSpecification);
		this.typedSpecification = typedSpecification;
	}

	@Override
	public Class<? extends TSolderSpecification> getTypedSpecificationClass()
	{
		return this.typedSpecificationClass;
	}

	protected void coreApplyTypedSpecification()
	{
		this.coreApplyTypedSpecification(this.getTypedSpecificationClass());
	}

	private void coreApplyTypedSpecification(Class<? extends TSolderSpecification> typedSpecificationClass)
	{
		if (typedSpecificationClass == null)
			throw new ArgumentNullException("typedSpecificationClass");

		if (this.getBaseSpecification() != null) // MUST BE this.getBaseSpecification() here or SOE throws
		{
			this.setTypedSpecification(this.coreTypedObjectFromJsonMap());
		}
	}

	protected abstract TSolderSpecification coreTypedObjectFromJsonMap();
}
