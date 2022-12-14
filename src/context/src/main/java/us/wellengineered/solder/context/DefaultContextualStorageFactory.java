/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.polyfills.exceptions.NotSupportedException;

public final class DefaultContextualStorageFactory extends AbstractContextualStorageFactory
{
	public DefaultContextualStorageFactory()
	{
	}

	@Override
	protected ContextualStorageStrategy coreGetContextualStorage(ContextScope contextScope) throws Exception
	{
		if (contextScope == null)
			throw new ArgumentNullException("contextScope");

		switch (contextScope)
		{
			case GLOBAL_STATIC_UNSAFE:
				return new GlobalStaticContextualStorageStrategy();
			case LOCAL_THREAD_SAFE:
				return new ThreadLocalContextualStorageStrategy();
			case LOCAL_ASYNC_SAFE:
				throw new NotSupportedException();
			case LOCAL_REQUEST_SAFE:
				return new RequestLocalContextualStorageStrategy();
			default:
				throw new ArgumentOutOfRangeException();
		}
	}
}
