/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.Disposable;

import java.util.UUID;

public final class DisposableDispatchImpl<TDisposable extends Disposable> extends AbstractLifecycle<Exception, Exception> implements DisposableDispatch<TDisposable>
{
	public DisposableDispatchImpl(ResourceManager resourceManager, UUID slotId, TDisposable target)
	{
		if (resourceManager == null)
			throw new ArgumentNullException("resourceManager");

		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (target == null)
			throw new ArgumentNullException("target");

		this.resourceManager = resourceManager;
		this.slotId = slotId;
		this.target = target;
	}

	private final ResourceManager resourceManager;
	private final UUID slotId;
	private final TDisposable target;

	public ResourceManager getResourceManager()
	{
		return this.resourceManager;
	}

	public UUID getSlotId()
	{
		return this.slotId;
	}

	@Override
	public TDisposable getTarget()
	{
		return this.target;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			if (this.getTarget() != null)
			{
				this.getTarget().dispose();
				this.getResourceManager().dispose(this.getSlotId(), this.getTarget());
			}
		}
	}
}
