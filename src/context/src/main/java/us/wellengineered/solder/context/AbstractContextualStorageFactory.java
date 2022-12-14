/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;

public abstract class AbstractContextualStorageFactory implements ContextualStorageFactory
{
	protected AbstractContextualStorageFactory()
	{
	}

	protected abstract ContextualStorageStrategy coreGetContextualStorage(ContextScope contextScope) throws Exception;

	@Override
	public ContextualStorageStrategy getContextualStorage(ContextScope contextScope) throws ContextException
	{
		if (contextScope == null)
			throw new ArgumentNullException("contextScope");

		try
		{
			return this.coreGetContextualStorage(contextScope);
		}
		catch (Exception ex)
		{
			throw new ContextException(String.format("The contextual storage factory failed (see inner exception)."), ex);
		}
	}
}
