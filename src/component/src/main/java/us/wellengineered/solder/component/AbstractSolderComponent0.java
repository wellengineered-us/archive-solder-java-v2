/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.*;

import java.util.UUID;

public abstract class AbstractSolderComponent0 extends AbstractLifecycle<Exception, Exception> implements SolderComponent0
{
	protected AbstractSolderComponent0()
	{
		this(UUID.randomUUID());
	}

	protected AbstractSolderComponent0(UUID componentId)
	{
		if (componentId == null)
			throw new ArgumentNullException("componentId");

		this.componentId = componentId;
	}

	private final UUID componentId;

	@Override
	public final UUID getComponentId()
	{
		return this.componentId;
	}

	@Override
	public final boolean isReusable()
	{
		return false;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		if (creating)
		{
			// do nothing
		}
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			// do nothing
		}
	}
}
