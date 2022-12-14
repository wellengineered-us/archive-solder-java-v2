/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.language;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;

public final class Lazy<T>
{
	public Lazy(Func.Arity1<T> initializer)
	{
		if (initializer == null)
			throw new ArgumentNullException("initializer");

		this.initializer = initializer;
	}
	private final Func.Arity1<T> initializer;
	private final Object lock = new Object();
	private volatile T value;

	public T getSafeValue()
	{
		try
		{
			return this.getValue();
		}
		catch (Exception ex)
		{
			return null;
		}
	}

	public T getValue() throws Exception
	{
		if (this.value == null)
		{
			synchronized (this.lock)
			{
				if (this.value == null)
				{
					this.value = this.initializer.invoke();
				}
			}
		}

		return this.value;
	}
}
