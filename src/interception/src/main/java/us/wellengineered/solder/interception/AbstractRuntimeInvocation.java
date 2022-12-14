/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.AbstractLifecycle;

import java.lang.reflect.Method;

public abstract class AbstractRuntimeInvocation extends AbstractLifecycle<Exception, Exception> implements RuntimeInvocation
{
	public AbstractRuntimeInvocation(Object proxyInstance, Class<?> targetClass, Method targetMethod, Object[] invocationArguments, Object wrappedInstance)
	{
		if (proxyInstance == null)
			throw new ArgumentNullException("proxyInstance");

		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		if (targetMethod == null)
			throw new ArgumentNullException("targetMethod");

		if (invocationArguments == null)
			throw new ArgumentNullException("invocationArguments");

		//if ((object)wrappedInstance == null)
		//throw new ArgumentNullException(nameof(wrappedInstance));

		this.proxyInstance = proxyInstance;
		this.targetClass = targetClass;
		this.targetMethod = targetMethod;
		this.invocationArguments = invocationArguments;
		this.wrappedInstance = wrappedInstance;
	}

	private final Object[] invocationArguments;
	private final Object proxyInstance;
	private final Class<?> targetClass;
	private final Method targetMethod;
	private final Object wrappedInstance;
	private Object invocationReturnValue;

	@Override
	public Object[] getInvocationArguments()
	{
		return this.invocationArguments;
	}

	@Override
	public Object getInvocationReturnValue()
	{
		return this.invocationReturnValue;
	}

	@Override
	public void setInvocationReturnValue(Object invocationReturnValue)
	{
		this.invocationReturnValue = invocationReturnValue;
	}

	@Override
	public Object getProxyInstance()
	{
		return this.proxyInstance;
	}

	@Override
	public Class<?> getTargetClass()
	{
		return this.targetClass;
	}

	@Override
	public Method getTargetMethod()
	{
		return this.targetMethod;
	}

	@Override
	public Object getWrappedInstance()
	{
		return this.wrappedInstance;
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

	protected void coreInvokeIndirect() throws Exception
	{
		if (this.getWrappedInstance() != null)
			this.setInvocationReturnValue(this.getTargetMethod().invoke(this.getWrappedInstance(), this.getInvocationArguments()));
	}

	@Override
	public final void invokeIndirect() throws InvocationException
	{
		try
		{
			this.coreInvokeIndirect();
		}
		catch (Exception ex)
		{
			throw new InvocationException(String.format("The runtime invocation failed (see inner exception)."), ex);
		}
	}
}
