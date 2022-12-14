/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.AbstractLifecycle;

public abstract class AbstractRuntimeContext extends AbstractLifecycle<Exception, Exception> implements RuntimeContext
{
	public AbstractRuntimeContext()
	{
	}

	private boolean continueInterception;
	private int interceptionCount;
	private int interceptionIndex;

	@Override
	public int getInterceptionCount()
	{
		return this.interceptionCount;
	}

	private void setInterceptionCount(int interceptionCount)
	{
		this.interceptionCount = interceptionCount;
	}

	@Override
	public int getInterceptionIndex()
	{
		return this.interceptionIndex;
	}

	private void setInterceptionIndex(int interceptionIndex)
	{
		this.interceptionIndex = interceptionIndex;
	}

	private void setContinueInterception(boolean continueInterception)
	{
		this.continueInterception = continueInterception;
	}

	@Override
	public void abortInterceptionChain() throws InterceptionException
	{
		if (!this.shouldContinueInterception())
			throw new InterceptionException(String.format("Cannot abort interception chain; the chain has was either previously aborted or pending graceful completion."));

		this.setContinueInterception(false);
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		// do nothing
	}

	@Override
	public boolean shouldContinueInterception()
	{
		return this.continueInterception;
	}
}
