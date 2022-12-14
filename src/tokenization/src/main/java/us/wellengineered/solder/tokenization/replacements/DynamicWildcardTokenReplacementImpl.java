/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.replacements;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;
import us.wellengineered.solder.polyfills.language.Func;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.ReflectionUtils;
import us.wellengineered.solder.primitives.StringUtils;

import java.util.Arrays;

public class DynamicWildcardTokenReplacementImpl<TContext> extends AbstractTokenReplacement
{
	public DynamicWildcardTokenReplacementImpl(Object[] targets, boolean strict)
	{
		this.targets = targets;
		this.strict = strict;
	}

	private final boolean strict;

	private final Object[] targets;

	public boolean isStrict()
	{
		return this.strict;
	}

	public Object[] getTargets()
	{
		return this.targets;
	}

	@Override
	protected Object coreEvaluate(String[] parameters) throws Exception
	{
		MethodParameterModifier.Out<Object> outValue;

		outValue = new MethodParameterModifier.Out<>(Object.class);
		this.tryGetByToken(null, outValue); // or fail if strict

		final Object value = outValue.getValue();
		return value;
	}

	@Override
	protected Object coreEvaluate(String token, Object[] parameters) throws Exception
	{
		MethodParameterModifier.Out<Object> outValue;

		outValue = new MethodParameterModifier.Out<>(Object.class);
		this.tryGetByToken(token, outValue); // or fail if strict

		final Object value = outValue.getValue();
		return value;
	}

	public boolean tryGetByToken(String _token, MethodParameterModifier.Out<Object> outValue) throws Exception
	{
		String firstToken, rawToken = null;
		String[] tokens;
		MethodParameterModifier.Out<Object> outTokenLogicalValue, outTokenReplacementValue;

		rawToken = _token;

		if (StringUtils.isNullOrWhiteSpace(rawToken))
		{
			if (this.isStrict())
				throw new InvalidOperationException(String.format("Failed to get value for empty token."));
			else
				return false;
		}

		// break any token paths into token list
		tokens = rawToken.split("\\.");

		if (tokens == null ||
				tokens.length <= 0)
		{
			if (this.isStrict())
				throw new InvalidOperationException(String.format("Failed to get value for empty token."));
			else
				return false;
		}

		firstToken = tokens[0];
		final String[] tokenz = new String[tokens.length - 1];
		Arrays.stream(tokens).skip(1).toList().toArray(tokenz);
		tokens = tokenz;

		if (this.getTargets() != null)
		{
			for (Object target : this.getTargets())
			{
				outTokenReplacementValue = new MethodParameterModifier.Out<>(Object.class);
				if (ReflectionUtils.tryGetLogicalPropertyValue(target, firstToken, outTokenReplacementValue))
				{
					if (tokens == null ||
							tokens.length <= 0)
					{
						outValue.setValue(outTokenReplacementValue.getValue());
						return true;
					}

					outTokenLogicalValue = new MethodParameterModifier.Out<>(Object.class);
					outTokenLogicalValue.setValue(outTokenReplacementValue.getValue());

					for (String token : tokens)
					{
						// only do logical lookup here
						if (!ReflectionUtils.tryGetLogicalPropertyValue(target, token, outTokenLogicalValue))
						{
							if (this.isStrict())
								throw new InvalidOperationException(String.format("Failed to get value for token '%s'", _token));
							else
								return false;
						}
					}

					outValue.setValue(outTokenLogicalValue.getValue());
					return true;
				}
			}
		}

		if (this.isStrict())
			throw new InvalidOperationException(String.format("Failed to get value for token '%s'", _token));

		outValue.setValue(null);
		return false;
	}
}
