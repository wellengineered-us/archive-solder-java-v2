/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;
import us.wellengineered.solder.polyfills.language.Func;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.Disposable;
import us.wellengineered.solder.primitives.DisposableList;
import us.wellengineered.solder.primitives.MapUtils;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static us.wellengineered.solder.primitives.TelemetryUtils.*;

public final class ResourceManagerImpl extends AbstractLifecycle<Exception, Exception> implements ResourceManager
{
	public ResourceManagerImpl()
	{
		this(new ConcurrentHashMap<>());
	}

	public ResourceManagerImpl(ConcurrentMap<UUID, DisposableList<Disposable>> trackedResources)
	{
		this.trackedResources = trackedResources;
	}

	private final ConcurrentMap<UUID, DisposableList<Disposable>> trackedResources;

	private ConcurrentMap<UUID, DisposableList<Disposable>> getTrackedResources()
	{
		return this.trackedResources;
	}

	@Override
	public void check() throws ResourceException
	{
		StringBuilder sb;
		int slots = 0, objs = 0;

		sb = new StringBuilder();
		for (Map.Entry<UUID, DisposableList<Disposable>> trackedResource : getTrackedResources().entrySet())
		{
			if (trackedResource == null)
				continue;

			if (trackedResource.getKey() == null ||
					trackedResource.getValue() == null)
				throw new ResourceException();

			final UUID __ = trackedResource.getKey();
			final DisposableList<Disposable> disposables = trackedResource.getValue();
			final int size = disposables.size();
			int ezis = 0;

			for (Disposable disposable : disposables)
			{
				if (disposable == null || disposable.isDisposed())
				{
					ezis--;
					continue;
				}

				sb.append(System.lineSeparator());
				sb.append(String.format("\t\t%s", formatObjectInfo(disposable)));
			}

			final int offset = size + ezis;

			if (offset > 0)
			{
				sb.append(System.lineSeparator());
				sb.append(String.format("\t%s ~ %s#", formatUUID(__), offset));

				slots++;
				objs += offset;
			}
		}

		if (sb.length() > 0)
			sb.append(System.lineSeparator());

		this.print(formatCallerInfo(), String.format("check!%s", sb));

		if (slots > 0)
		{
			final String message = String.format("Resource manager tracked resource check FAILED with %s slots having %s leaked disposable objects", slots, objs);
			this.print(formatCallerInfo(), message);

			throw new ResourceException(message);
		}
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (this.isDisposed())
			return;

		if (disposing)
		{
			this.reset();
		}
	}

	@Override
	public void dispose(UUID slotId, Disposable disposable) throws ResourceException
	{
		MethodParameterModifier.Out<DisposableList<Disposable>> outDisposables;
		DisposableList disposables;

		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		outDisposables = new MethodParameterModifier.Out<>();

		if (!MapUtils.tryMapGetValue(this.getTrackedResources(), slotId, outDisposables))
			throw new ResourceException(String.format("Resource manager does not contain a record of slot '%s'.", formatUUID(slotId)));

		disposables = outDisposables.getValue();

		if (!disposables.contains(disposable))
			throw new ResourceException(String.format("Leak detector does not contain a record of slot '%s | %s'.", formatUUID(slotId), formatObjectInfo(disposable)));

		disposables.remove(disposable);

		this.print(formatCallerInfo(), formatOperation("dispose", slotId, disposable));
	}

	@Override
	public UUID enter() throws ResourceException
	{
		final UUID slotId = UUID.randomUUID();

		this.print(formatCallerInfo(), formatOperation("enter", slotId));

		return slotId;
	}

	@Override
	public <TValue> TValue leave(UUID slotId, TValue value) throws ResourceException
	{
		if (slotId == null)
			throw new ArgumentNullException("slotId");

		this.leave(formatCallerInfo(), slotId);

		return value;
	}

	@Override
	public void leave(UUID slotId) throws ResourceException
	{
		if (slotId == null)
			throw new ArgumentNullException("slotId");

		this.leave(formatCallerInfo(), slotId);
	}

	private void leave(String caller, UUID slotId)
	{
		if (slotId == null)
			throw new ArgumentNullException("slotId");

		this.print(caller, formatOperation("leave", slotId));
	}

	@Override
	public void print(UUID slotId, String message) throws ResourceException
	{
		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (message == null)
			throw new ArgumentNullException("message");

		this.print(formatCallerInfo(), String.format("%s => `%s`", formatOperation("print", slotId), message));
	}

	private void print(String caller, String message)
	{
		System.out.println(String.format("[%s: %s]", caller, message));
	}

	@Override
	public void reset() throws ResourceException
	{
		this.getTrackedResources().clear();
	}

	private void slot(String caller, UUID slotId, Disposable disposable, String message)
	{
		MethodParameterModifier.Out<DisposableList<Disposable>> outDisposables;
		DisposableList<Disposable> disposables;

		if (caller == null)
			throw new ArgumentNullException("caller");

		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		if (message == null)
			throw new ArgumentNullException("message");

		outDisposables = new MethodParameterModifier.Out<>();

		if (!MapUtils.tryMapGetValue(this.getTrackedResources(), slotId, outDisposables))
		{
			disposables = new DisposableList<>();
			this.getTrackedResources().put(slotId, disposables);
		}
		else
		{
			disposables = outDisposables.getValue();
		}

		if (disposables.contains(disposable))
			throw new InvalidOperationException(String.format("Leak detector already contains a record of slot '%s | %s'.", formatUUID(slotId), formatObjectInfo(disposable)));

		disposables.add(disposable);

		this.print(caller, message);
	}

	@Override
	public <TDisposable extends Disposable> DisposableDispatch<TDisposable> using(UUID slotId, TDisposable disposable, Func.Arity0 onDisposal) throws ResourceException
	{
		DisposableDispatch<TDisposable> disposableDispatch;

		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		disposableDispatch = new DisposableDispatchImpl<>(this, slotId, disposable); // forward call to .dispose()
		//disposableDispatch.create();

		this.slot(formatCallerInfo(), slotId, disposable, formatOperation("using", slotId, disposable));

		return disposableDispatch;
	}

	@Override
	public <TDisposable extends Disposable> TDisposable watching(UUID slotId, TDisposable disposable) throws ResourceException
	{
		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (disposable == null)
			throw new ArgumentNullException("disposable");

		this.slot(formatCallerInfo(), slotId, disposable, formatOperation("watching", slotId, disposable));

		return disposable;
	}
}
