/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.replacements;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.language.Func;
import us.wellengineered.solder.primitives.StringUtils;

public class ContextualDynamicValueTokenReplacementImpl<TContext> extends AbstractTokenReplacement
{
	public ContextualDynamicValueTokenReplacementImpl(Func.Arity3<TContext, String[], Object> method, TContext context)
	{
		if (method == null)
			throw new ArgumentNullException("method");

		this.method = method;
		this.context = context;
	}

	private final TContext context;

	private final Func.Arity3<TContext, String[], Object> method;

	public TContext getContext()
	{
		return this.context;
	}

	public Func.Arity3<TContext, String[], Object> getMethod()
	{
		return this.method;
	}

	@Override
	protected Object coreEvaluate(String[] parameters) throws Exception
	{
		return this.getMethod().invoke(this.getContext(), parameters);
	}

	@Override
	protected Object coreEvaluate(String token, Object[] parameters) throws Exception
	{
		String[] parameterz;

		if (parameters != null)
		{
			parameterz = new String[parameters.length];

			for (int i = 0; i < parameterz.length; i++)
				parameterz[i] = parameters[i] instanceof String ? (String)parameters[i] : StringUtils.toStringEx(parameters[i]);
		}
		else
		{
			parameterz = new String[] { };
		}

		return this.getMethod().invoke(this.getContext(), parameterz);
	}

}
