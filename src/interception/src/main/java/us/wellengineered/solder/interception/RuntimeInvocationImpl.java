/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import java.lang.reflect.Method;

public final class RuntimeInvocationImpl extends AbstractRuntimeInvocation
{
	public RuntimeInvocationImpl(Object proxyInstance, Class<?> targetClass, Method targetMethod, Object[] invocationArguments, Object wrappedInstance)
	{
		super(proxyInstance, targetClass, targetMethod, invocationArguments, wrappedInstance);
	}
}
