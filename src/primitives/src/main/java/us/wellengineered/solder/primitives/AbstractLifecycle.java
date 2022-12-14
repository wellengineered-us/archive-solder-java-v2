/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

public abstract class AbstractLifecycle<TCreationException extends Exception, TDisposalException extends Exception> implements Creatable, Disposable
{
	protected AbstractLifecycle()
	{
	}

	private boolean created;
	private boolean disposed;

	@Override
	public final boolean isCreated()
	{
		return this.created;
	}

	private void setCreated(boolean created)
	{
		this.created = created;
	}

	@Override
	public final boolean isDisposed()
	{
		return this.disposed;
	}

	private void setDisposed(boolean disposed)
	{
		this.disposed = disposed;
	}

	@Override
	public final void /* dispose */ close() throws TDisposalException
	{
		this.dispose(); // special
	}

	protected abstract void coreCreate(boolean creating) throws TCreationException;

	protected abstract void coreDispose(boolean disposing) throws TDisposalException;

	@Override
	public final void create() throws TCreationException
	{
		this.initialize();
	}

	@Override
	public final void dispose() throws TDisposalException
	{
		this.terminate();
	}

	private void explicitInitialize() throws TCreationException
	{
		if (this.isCreated())
			return;

		this.coreCreate(true);
		this.maybeSetCreated();
	}

	protected void explicitSetCreated()
	{
		//GC.ReRegisterForFinalize(this);
		this.setCreated(true);
	}

	protected void explicitSetDisposed()
	{
		this.setDisposed(true);
		//GC.SuppressFinalize(this);
	}

	private final void explicitTerminate() throws TDisposalException
	{
		if (this.isDisposed())
			return;

		this.coreDispose(true);
		this.maybeSetDisposed();
	}

	public final void initialize() throws TCreationException
	{
		this.maybeInitialize();
	}

	protected void maybeInitialize() throws TCreationException
	{
		this.explicitInitialize();
	}

	protected void maybeSetCreated()
	{
		this.explicitSetCreated();
	}

	protected void maybeSetDisposed()
	{
		this.explicitSetDisposed();
	}

	protected void maybeTerminate() throws TDisposalException
	{
		this.explicitTerminate();
	}

	public final void terminate() throws TDisposalException
	{
		this.maybeTerminate();
	}
}
