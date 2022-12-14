/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import java.io.Closeable;

public final class LifecycleUtils
{
	public static boolean maybeClose(Object obj) throws Exception
	{
		if (obj != null)
		{
			if (obj instanceof Closeable)
			{
				((Closeable) obj).close();
				return true;
			}
			else if (obj instanceof AutoCloseable)
			{
				((AutoCloseable) obj).close();
				return true;
			}
		}

		return false;
	}

	public static boolean maybeCreate(Object obj) throws Exception
	{
		if (obj != null)
		{
			if (obj instanceof Creatable)
			{
				final Creatable creatable = (Creatable) obj;

				if (!creatable.isCreated())
					creatable.create();

				return true;
			}
		}

		return false;
	}

	public static boolean maybeDispose(Object obj) throws Exception
	{
		if (obj != null)
		{
			if (obj instanceof Disposable)
			{
				final Disposable disposable = (Disposable) obj;

				if (!disposable.isDisposed())
					disposable.dispose();

				return true;
			}
		}

		return false;
	}
}
