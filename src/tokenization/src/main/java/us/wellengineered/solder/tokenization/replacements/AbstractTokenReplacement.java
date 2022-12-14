/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.replacements;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.tokenization.TokenReplacement;
import us.wellengineered.solder.tokenization.TokenizationException;

public abstract class AbstractTokenReplacement implements TokenReplacement
{
	protected AbstractTokenReplacement()
	{
	}

	@Override
	public Object evaluate(String[] parameters) throws TokenizationException
	{
		if (parameters == null)
			throw new ArgumentNullException("parameters");

		try
		{
			return this.coreEvaluate(parameters);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The token replacement failed (see inner exception)."), ex);
		}
	}

	protected abstract Object coreEvaluate(String[] parameters) throws Exception;

	@Override
	public Object evaluate(String token, Object[] parameters) throws TokenizationException
	{
		if (token == null)
			throw new ArgumentNullException("token");

		if (parameters == null)
			throw new ArgumentNullException("parameters");

		try
		{
			return this.coreEvaluate(token, parameters);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The token replacement failed (see inner exception)."), ex);
		}
	}

	protected abstract Object coreEvaluate(String token, Object[] parameters) throws Exception;
}
