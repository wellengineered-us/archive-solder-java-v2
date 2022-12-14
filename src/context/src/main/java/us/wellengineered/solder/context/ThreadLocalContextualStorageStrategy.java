/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.polyfills.exceptions.NotImplementedException;

public final class ThreadLocalContextualStorageStrategy extends AbstractContextualStorageStrategy
{
	public ThreadLocalContextualStorageStrategy()
	{
	}

	@Override
	protected <TValue> TValue coreGetValue(Class<? extends TValue> valueClass, String key) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	protected boolean coreHasValue(String key) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	protected void coreRemoveValue(String key) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	protected <TValue> void coreSetValue(String key, Class<? extends TValue> valueClass, TValue value) throws Exception
	{
		throw new NotImplementedException();
	}
}
