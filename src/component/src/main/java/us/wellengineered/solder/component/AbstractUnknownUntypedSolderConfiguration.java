/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.configuration.*;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractUnknownUntypedSolderConfiguration extends AbstractSolderConfiguration implements UnknownUntypedSolderConfiguration
{
	public AbstractUnknownUntypedSolderConfiguration(Class<? extends SolderSpecification> baseSpecificationClass)
	{
		this(new LinkedHashMap<>(), baseSpecificationClass);
	}

	public AbstractUnknownUntypedSolderConfiguration(Map<String, Object> untypedSpecification, Class<? extends SolderSpecification> baseSpecificationClass)
	{
		if (untypedSpecification == null)
			throw new ArgumentNullException("untypedSpecification");

		if (baseSpecificationClass == null)
			throw new ArgumentNullException("baseSpecificationClass");

		this.untypedSpecification = untypedSpecification;
		this.baseSpecificationClass = baseSpecificationClass;
	}

	private final Class<? extends SolderSpecification> baseSpecificationClass;
	private final Map<String, Object> untypedSpecification;
	private SolderSpecification baseSpecification;
	private String componentClassName;

	public SolderSpecification getBaseSpecification()
	{
		this.coreApplyUntypedSpecification(); // special case
		return this.baseSpecification;
	}

	public void setBaseSpecification(SolderSpecification baseSpecification)
	{
		this.ensureParentOnSetter(this.baseSpecification, baseSpecification);
		this.baseSpecification = baseSpecification;
	}

	public Class<? extends SolderSpecification> getBaseSpecificationClass()
	{
		return this.baseSpecificationClass;
	}

	public <TComponent extends Specifiable<? extends SolderSpecification>> Class<? extends TComponent> getComponentClass()
	{
		Class<? extends TComponent> componentClass;

		componentClass = ReflectionUtils.loadClassByName(this.getComponentClassName());

		return componentClass;
	}

	public String getComponentClassName()
	{
		return this.componentClassName;
	}

	public void setComponentClassName(String componentClassName)
	{
		this.componentClassName = componentClassName;
	}

	public Map<String, Object> getUntypedSpecification()
	{
		return this.untypedSpecification;
	}

	protected void coreApplyUntypedSpecification(Object unused)
	{
		if (this.getUntypedSpecification() != null)
		{
			var baseSpecification = this.coreBaseObjectFromJsonMap();
			this.setBaseSpecification(baseSpecification);
		}
	}

	protected void coreApplyUntypedSpecification()
	{
		this.coreApplyUntypedSpecification(null);
	}

	protected abstract SolderSpecification coreBaseObjectFromJsonMap();

	@Override
	protected Iterable<Message> coreValidate(Object context)
	{
		List<Message> messages;
		Class<? extends SolderComponent2<? extends UnknownUntypedSolderConfiguration, ? extends SolderSpecification>> componentClass;
		SolderComponent2<? extends UnknownUntypedSolderConfiguration, ? extends SolderSpecification> component;

		messages = new ArrayList<>();

		if (StringUtils.isNullOrEmptyString(this.getComponentClassName()))
			messages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("%s component class name is required.", context), Severity.ERROR));
		else
		{
			componentClass = this.getComponentClass();

			if (componentClass == null)
				messages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("%s component failed to load type from class name.", context), Severity.ERROR));
			else if (!SolderComponent2.class.isAssignableFrom(componentClass))
				messages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("%s component loaded an unrecognized type via class name.", context), Severity.ERROR));
			else
			{
				// new-ing up via default public constructor should be low friction
				component = ReflectionUtils.createInstanceAssignableToClass(componentClass);

				if (component == null)
					messages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("%s component failed to instantiate type from class name.", context), Severity.ERROR));
				else
				{
					try (component)
					{
						Class<? extends SolderSpecification> specificationClass;

						// TODO: fix this generics issue
						//component.setConfiguration(this);
						component.create();

						// "Hey Component, tell me what your Specification (Component SpecificConfiguration derived) class is?"
						specificationClass = component.getSpecificationClass();
						this.coreApplyUntypedSpecification(specificationClass);

						MessageImpl.addRange(messages, component.getSpecification().validate(String.format("%s", context)));
					}
					catch (Exception ex)
					{
						messages.add(new MessageImpl(StringUtils.EMPTY_STRING, ex.toString(), Severity.ERROR));
					}
				}
			}
		}

		return messages;
	}

	public void resetUntypedSpecification()
	{
		this.getUntypedSpecification().clear();
		this.setBaseSpecification(null);
	}
}
