/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;

public final class LoggingRuntimeInterception extends AbstractRuntimeInterception
{
	public LoggingRuntimeInterception()
	{
	}

	@Override
	protected void coreOnAfterInvoke(boolean proceedWithInvocation, RuntimeInvocation runtimeInvocation, MethodParameterModifier.Ref<Exception> outThrownException)
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outThrownException == null)
			throw new ArgumentNullException("outThrownException");

		System.out.println(String.format("after invoke: %s", runtimeInvocation.getTargetMethod().getName()));
	}

	@Override
	protected void coreOnBeforeInvoke(RuntimeInvocation runtimeInvocation, MethodParameterModifier.Out<Boolean> outProceedWithInvocation)
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		if (outProceedWithInvocation == null)
			throw new ArgumentNullException("outProceedWithInvocation");

		System.out.println(String.format("before invoke: %s", runtimeInvocation.getTargetMethod().getName()));
		outProceedWithInvocation.setValue(true);
	}

	@Override
	protected void coreOnMagicalSpellInvoke(RuntimeInvocation runtimeInvocation) throws Exception
	{
		if (runtimeInvocation == null)
			throw new ArgumentNullException("runtimeInvocation");

		System.out.println(String.format("magic invoke: %s", runtimeInvocation.getTargetMethod().getName()));
		super.coreOnMagicalSpellInvoke(runtimeInvocation);
	}
}
