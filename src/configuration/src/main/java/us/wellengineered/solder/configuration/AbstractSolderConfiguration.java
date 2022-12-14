/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

import us.wellengineered.solder.primitives.Message;

import java.util.Collections;
import java.util.List;

/**
 * Provides a base for all configuration objects.
 */
public abstract class AbstractSolderConfiguration implements SolderConfiguration
{
	protected AbstractSolderConfiguration()
	{
		this.items = new SolderConfigurationCollectionImpl<SolderConfiguration>(this); // cannot pass in "this"
	}

	private final SolderConfigurationCollectionImpl<SolderConfiguration> items;
	private SolderConfiguration content;
	private SolderConfiguration parent;
	private SolderConfigurationCollectionImpl<?> surround;

	@Override
	public Iterable<Class<? extends SolderConfiguration>> getAllowedChildClasses()
	{
		List<Class<? extends SolderConfiguration>> objectClasses = Collections.emptyList();
		return objectClasses;
	}

	@Override
	public SolderConfiguration getContent()
	{
		return this.content;
	}

	@Override
	public void setContent(SolderConfiguration content)
	{
		this.ensureParentOnSetter(this.content, content);
		this.content = content;
	}

	@Override
	public SolderConfigurationCollectionImpl<SolderConfiguration> getItems()
	{
		return this.items;
	}

	@Override
	public SolderConfiguration getParent()
	{
		return this.parent;
	}

	@Override
	public void setParent(SolderConfiguration parent)
	{
		this.parent = parent;
	}

	@Override
	public SolderConfigurationCollectionImpl<?> getSurround()
	{
		return this.surround;
	}

	@Override
	public void setSurround(SolderConfigurationCollectionImpl<?> surround)
	{
		this.surround = surround;
	}

	protected abstract Iterable<Message> coreValidate(Object context);

	/**
	 * Ensures that for any configuration object property, the correct parent instance is set/unset.
	 * Should be called in the setter for all configuration object properties, before assigning the value.
	 * Example:
	 * setThingy(Thingy thingy) { this.ensureParentOnSetter(this.thingy, thingy); this.thingy = thingy; }
	 *
	 * @param oldValueObj The old configuration object value (the backing field).
	 * @param newValueObj The new configuration object value (the local parameter in setter method).
	 */
	protected void ensureParentOnSetter(SolderConfiguration oldValueObj, SolderConfiguration newValueObj)
	{
		if (oldValueObj != null)
		{
			oldValueObj.setSurround(null);
			oldValueObj.setParent(null);
		}

		if (newValueObj != null)
		{
			newValueObj.setSurround(null);
			newValueObj.setParent(this);
		}
	}

	@Override
	public final Iterable<Message> validate()
	{
		return this.coreValidate(null);
	}

	@Override
	public final Iterable<Message> validate(Object context)
	{
		return this.coreValidate(context);
	}
}
