/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.Lifecycle;

import java.lang.reflect.Method;

public interface RuntimeInvocation extends Lifecycle
{
	Object[] getInvocationArguments();

	Object getInvocationReturnValue();

	void setInvocationReturnValue(Object invocationReturnValue);

	Object getProxyInstance();

	Class<?> getTargetClass();

	Method getTargetMethod();

	Object getWrappedInstance();

	void invokeIndirect() throws InvocationException;
}
