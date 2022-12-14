/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.NotImplementedException;
import us.wellengineered.solder.polyfills.exceptions.ObjectDisposedException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.Disposable;

public abstract class AbstractRuntimeInterception extends AbstractLifecycle<Exception, Exception> implements RuntimeInterception
{
	protected AbstractRuntimeInterception()
	{
	}

	public static <TTarget> TTarget createProxy(Class<? extends TTarget> targetClass, RuntimeInterception[] runtimeInterceptionChain)
	{
		TTarget proxyInstance;

		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		if (runtimeInterceptionChain == null)
			throw new ArgumentNullException("runtimeInterceptionChain");

		throw new NotImplementedException();
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

	protected void coreOnAfterInvoke(boolean proceedWithInvocation, RuntimeInvocation runtimeInvocation, MethodParameterModifier.Ref<Exception> outThrownException) throws Exception
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outThrownException == null)
			throw new ArgumentNullException("outThrownException");
	}

	protected void coreOnBeforeInvoke(RuntimeInvocation runtimeInvocation, MethodParameterModifier.Out<Boolean> outProceedWithInvocation) throws Exception
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outProceedWithInvocation == null)
			throw new ArgumentNullException("outProceedWithInvocation");

		outProceedWithInvocation.setValue(true);
	}

	protected void coreOnInvoke(RuntimeInvocation runtimeInvocation, RuntimeContext runtimeContext) throws Exception
	{
		final MethodParameterModifier.Out<Boolean> outProceedWithInvocation = new MethodParameterModifier.Out<>(Boolean.class);
		final MethodParameterModifier.Out<Exception> outThrownException = new MethodParameterModifier.Out<>(Exception.class);

		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (runtimeContext == null)
			throw new ArgumentNullException("runtimeContext");

		if (!(runtimeInvocation.getTargetMethod() != null &&
				runtimeInvocation.getTargetMethod().getDeclaringClass() == Disposable.class) &&
				this.isDisposed()) // always forward dispose invocations
			throw new ObjectDisposedException(this.getClass().getName());

		this.coreOnBeforeInvoke(runtimeInvocation, outProceedWithInvocation);

		if (outProceedWithInvocation.getValue())
			this.coreOnProceedInvoke(runtimeInvocation, outThrownException);

		final MethodParameterModifier.Ref<Exception> refThrownException = new MethodParameterModifier.Ref<>(Exception.class, outThrownException.getValue());
		this.coreOnAfterInvoke(outProceedWithInvocation.getValue(), runtimeInvocation, refThrownException);

		final Exception thrownException = refThrownException.getValue();

		if (thrownException != null)
		{
			runtimeContext.abortInterceptionChain();
			throw thrownException;
		}
	}

	protected void coreOnMagicalSpellInvoke(RuntimeInvocation runtimeInvocation) throws Exception
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		runtimeInvocation.invokeIndirect(); // use reflection over wrapped instance
	}

	protected void coreOnProceedInvoke(RuntimeInvocation runtimeInvocation, MethodParameterModifier.Out<Exception> outThrownException) throws Exception
	{
		Exception exception;

		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outThrownException == null)
			throw new ArgumentNullException("outThrownException");

		try
		{
			exception = null;
			this.coreOnMagicalSpellInvoke(runtimeInvocation);
		}
		catch (Exception ex)
		{
			exception = ex;
		}

		outThrownException.setValue(exception);
	}

	@Override
	public final void invoke(RuntimeInvocation runtimeInvocation, RuntimeContext runtimeContext) throws InvocationException
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (runtimeContext == null)
			throw new ArgumentNullException("runtimeContext");

		try
		{
			this.coreOnInvoke(runtimeInvocation, runtimeContext);
		}
		catch (Exception ex)
		{
			throw new InvocationException(String.format("The runtime interception failed (see inner exception)."), ex);
		}
	}
}
