/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.Message;
import us.wellengineered.solder.primitives.MessageImpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

public class SolderConfigurationCollectionImpl<TItemSolderConfiguration extends SolderConfiguration> implements SolderConfigurationCollection<TItemSolderConfiguration>
{
	public SolderConfigurationCollectionImpl(SolderConfiguration site)
	{
		this(site, new ArrayList<>());
	}

	public SolderConfigurationCollectionImpl(SolderConfiguration site, Collection<TItemSolderConfiguration> collection)
	{
		if (site == null)
			throw new ArgumentNullException("site");

		if (collection == null)
			throw new ArgumentNullException("collection");

		this.site = site;
		this.collection = collection;
	}

	private final Collection<TItemSolderConfiguration> collection;
	private final SolderConfiguration site;

	private Collection<TItemSolderConfiguration> getCollection()
	{
		return this.collection;
	}

	@Override
	public SolderConfiguration getSite()
	{
		return this.site;
	}

	@Override
	public boolean isEmpty()
	{
		return this.getCollection().isEmpty();
	}

	@Override
	public boolean add(TItemSolderConfiguration item)
	{
		if (item != null)
		{
			item.setSurround(this);
			item.setParent(this.getSite());
		}

		return this.getCollection().add(item);
	}

	@Override
	public boolean addAll(Collection<? extends TItemSolderConfiguration> col)
	{
		if (col == null)
		{
			for (TItemSolderConfiguration item : col)
			{
				if (item == null)
					continue;

				item.setSurround(null);
				item.setParent(null);
			}
		}

		return this.getCollection().addAll(col);
	}

	@Override
	public void clear()
	{
		for (TItemSolderConfiguration item : this.getCollection())
		{
			if (item == null)
				continue;

			item.setSurround(null);
			item.setParent(null);
		}

		this.getCollection().clear();
	}

	@Override
	public boolean contains(Object obj)
	{
		return this.contains(obj);
	}

	@Override
	public boolean containsAll(Collection<?> col)
	{
		return this.getCollection().containsAll(col);
	}

	@Override
	public Iterator<TItemSolderConfiguration> iterator()
	{
		return this.getCollection().iterator();
	}

	@Override
	public boolean remove(Object obj)
	{
		SolderConfiguration item;

		if (obj instanceof SolderConfiguration)
		{
			item = (SolderConfiguration) obj;
			item.setSurround(null);
			item.setParent(null);
		}

		return this.getCollection().remove(obj);
	}

	@Override
	public boolean removeAll(Collection<?> col)
	{
		if (col == null)
		{
			for (Object obj : col)
			{
				SolderConfiguration item;

				if (obj == null)
					continue;

				if (obj instanceof SolderConfiguration)
				{
					item = (SolderConfiguration) obj;
					item.setSurround(null);
					item.setParent(null);
				}
			}
		}

		return this.getCollection().removeAll(col);
	}

	@Override
	public boolean retainAll(Collection<?> col)
	{
		if (col == null)
		{
			for (Object obj : col)
			{
				SolderConfiguration item;

				if (obj == null)
					continue;

				if (obj instanceof SolderConfiguration)
				{
					item = (SolderConfiguration) obj;
					item.setSurround(this);
					item.setParent(this.getSite());
				}
			}
		}

		return this.getCollection().retainAll(col);
	}

	@Override
	public int size()
	{
		return this.getCollection().size();
	}

	@Override
	public Object[] toArray()
	{
		return this.getCollection().toArray();
	}

	@Override
	public <T1> T1[] toArray(T1[] array)
	{
		return this.getCollection().toArray(array);
	}

	@Override
	public final Iterable<Message> validate(Object context)
	{
		List<Message> messages;
		int index = 0;

		messages = new ArrayList<>();

		for (TItemSolderConfiguration item : this.getCollection())
		{
			final String value = context == null ? String.format("%s", index++) : String.format("%s[%s]", context, index++);
			Iterable<Message> iterable;

			if (item == null)
				continue;

			iterable = item.validate(value);
			MessageImpl.addRange(messages, iterable);
		}

		return messages;
	}

	@Override
	public final Iterable<Message> validate()
	{
		return this.validate(null);
	}
}
