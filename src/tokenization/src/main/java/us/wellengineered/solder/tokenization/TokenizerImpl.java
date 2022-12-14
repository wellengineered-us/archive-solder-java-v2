/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.MapUtils;
import us.wellengineered.solder.primitives.ReflectionUtils;
import us.wellengineered.solder.primitives.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TokenizerImpl implements Tokenizer
{
	public TokenizerImpl(Map<String, TokenReplacement> tokenReplacementStrategies, boolean strictMatching)
	{
		this(tokenReplacementStrategies, strictMatching, new ArrayList<>());
	}
	public TokenizerImpl(Map<String, TokenReplacement> tokenReplacementStrategies, boolean strictMatching, List<String> previousExpansionTokens)
	{
		if (tokenReplacementStrategies == null)
			throw new ArgumentNullException("tokenReplacementStrategies");

		if (previousExpansionTokens == null)
			throw new ArgumentNullException("previousExpansionTokens");

		this.tokenReplacementStrategies = tokenReplacementStrategies;
		this.strictMatching = strictMatching;
		this.previousExpansionTokens = previousExpansionTokens;
	}

	private static final String TOKEN_ID_REGEX_UNBOUNDED = "[a-zA-Z_\\.][a-zA-Z_\\.0-9]";
	private static final String TOKEN_ID_REGEX = TOKEN_ID_REGEX_UNBOUNDED + "{0,1023}";

	private static final String TOKENIZER_LOGICAL_PROPERTY_PATH_CHAR_REGEX = "\\.";

	private static final String TOKENIZER_REGEX =
	"\\$ \\{" +
	"(?: [ ]* ( " + TOKEN_ID_REGEX + " ){1,1} )" +
	"(?: [ ]* \\( ( [ ]* (?: ` [^`]* ` [ ]* (?: , [ ]* ` [^`]* ` [ ]* )* ){0,1} ){0,1} \\) ){0,1}" +
	"[ ]* \\}";

	private final boolean strictMatching;

	private final Map<String, TokenReplacement> tokenReplacementStrategies;

	private final List<String> previousExpansionTokens;

	public static String getTokenIdRegEx()
	{
		return TOKEN_ID_REGEX;
	}

	public static String getTokenizerRegEx()
	{
		return TOKENIZER_REGEX;
	}

	public List<String> getPreviousExpansionTokens()
	{
		return this.previousExpansionTokens;
	}

	@Override
	public String[] getOrderedPreviousExpansionTokens() throws TokenizationException
	{
		String[] value;
		value = new String[this.getPreviousExpansionTokens().size()];
		this.getPreviousExpansionTokens().toArray(value);
		return value;
	}

	@Override
	public Map<String, TokenReplacement> getTokenReplacementStrategies()
	{
		return this.tokenReplacementStrategies;
	}

	@Override
	public boolean isStrictMatching()
	{
		return this.strictMatching;
	}

	private static String getOriginalValueOrThrowException(boolean strictMatching, String originalValue, String matchPoint) throws TokenizationException
	{
		if (strictMatching)
			throw new TokenizationException(String.format("Failed to recognize '%s' due to '%s' match error; strict matching enabled.", originalValue, matchPoint));
		else
			return originalValue;
	}

	public static boolean isValidTokenId(String token)
	{
		return Pattern.matches(getTokenIdRegEx(), token);
	}

	@Override
	public String expandTokens(String tokenizedValue) throws TokenizationException
	{
		return this.expandTokens(tokenizedValue, null);
	}


	/**
	 * https://github.com/eugenp/tutorials/blob/master/core-java-modules/core-java-regex-2/src/main/java/com/baeldung/replacetokens/ReplacingTokens.java
	 * @param original
	 * @param tokenPattern
	 * @param converter
	 * @return
	 */
	public static String replaceTokens(String original, Pattern tokenPattern,
									   Function<Matcher, String> converter) {
		int lastIndex = 0;
		StringBuilder output = new StringBuilder();
		Matcher matcher = tokenPattern.matcher(original);
		while (matcher.find()) {
			output.append(original, lastIndex, matcher.start())
					.append(converter.apply(matcher));

			lastIndex = matcher.end();
		}
		if (lastIndex < original.length()) {
			output.append(original, lastIndex, original.length());
		}
		return output.toString();
	}

	@Override
	public String expandTokens(String tokenizedValue, TokenReplacement optionalWildcardTokenReplacement) throws TokenizationException
	{
		if (StringUtils.isNullOrEmptyString(tokenizedValue))
			return tokenizedValue;

		// clean token collection
		this.getPreviousExpansionTokens().clear();

		final Pattern pattern = Pattern.compile(getTokenizerRegEx());
		tokenizedValue = replaceTokens(tokenizedValue, pattern, m -> this.replacementMatcherEx(m, optionalWildcardTokenReplacement));

		return tokenizedValue;
	}

	private String[] GetArgs(String call)
	{
		String[] args;

		if (StringUtils.isNullOrWhiteSpace(call))
			return new String[] { };

		// fixup argument list
		call = call.replaceAll("^ [ ]* `", StringUtils.EMPTY_STRING);
		call = call.replaceAll("` [ ]* $", StringUtils.EMPTY_STRING);
		call = call.replaceAll("` [ ]* , [ ]* `", "`");

		args = call.split("`");

		// fix-up escaped backtick
		for (int i = 0; i < args.length; i++)
			args[i] = args[i].replace("\\'", "`");

		return args;
	}

	private String replacementMatcherEx(Matcher matcher, TokenReplacement wildcardTokenReplacement) throws FailFastException
	{
		try
		{
			return replacementMatcherInt(matcher, wildcardTokenReplacement);
		}
		catch(Exception ex)
		{
			throw new FailFastException(ex);
		}
	}
	private String replacementMatcherInt(Matcher matcher, TokenReplacement wildcardTokenReplacement) throws Exception
	{
		// ${ .token.token.token_end (`arg0`, ..) }

		String firstToken, rawToken = null;
		String[] tokens;
		List<String> tokenList;
		String[] argumentList = null;
		Object tokenLogicalValue, tokenReplacementValue = null;
		MethodParameterModifier.Out<TokenReplacement> outTokenReplacement;
		MethodParameterModifier.Out<Object> outTokenLogicalValue;
		boolean keyNotFound, tryWildcard;
		String matcherValue;

		if (matcher == null)
			throw new ArgumentNullException("matcher");

		matcherValue = matcher.toString();
		rawToken = matcher.group(1);
		argumentList = matcher.group(2) != null ? this.GetArgs(matcher.group(2)) : null;

		if (StringUtils.isNullOrWhiteSpace(rawToken))
			return getOriginalValueOrThrowException(this.isStrictMatching(), matcherValue, "token missing");

		// break any token paths into token list
		tokens = rawToken.split(TOKENIZER_LOGICAL_PROPERTY_PATH_CHAR_REGEX);

		if (tokens == null ||
				tokens.length <= 0)
			return null;

		firstToken = tokens[0];
		tokenList = Arrays.stream(tokens).skip(1).toList();

		// add to token collection
		this.getPreviousExpansionTokens().add(firstToken);

		outTokenReplacement = new MethodParameterModifier.Out<>(TokenReplacement.class);
		keyNotFound = !MapUtils.tryMapGetValue(this.getTokenReplacementStrategies(), firstToken, outTokenReplacement);
		tryWildcard = keyNotFound && wildcardTokenReplacement != null;

		if (keyNotFound && !tryWildcard)
			return getOriginalValueOrThrowException(this.isStrictMatching(), matcherValue, "token unknown");

		try
		{
			final TokenReplacement tokenReplacement = outTokenReplacement.getValue();

			if (!tryWildcard)
				tokenReplacementValue = tokenReplacement.evaluate(argumentList);
			else
				tokenReplacementValue = wildcardTokenReplacement.evaluate(firstToken, argumentList);
		}
		catch (Exception ex)
		{
			return getOriginalValueOrThrowException(this.isStrictMatching(), matcherValue, String.format("function exception {{" + System.lineSeparator() + "%s" + System.lineSeparator() + "}}", ReflectionUtils.getErrors(ex, 0)));
		}

		if (tokenList == null ||
				tokenList.size() <= 0)
			return tokenReplacementValue != null ? tokenReplacementValue.toString() : StringUtils.EMPTY_STRING;

		tokenLogicalValue = tokenReplacementValue;
		outTokenLogicalValue = new MethodParameterModifier.Out<>(Object.class);
		for (String token : tokenList)
		{
			// only do logical lookup here
			if (!ReflectionUtils.tryGetLogicalPropertyValue(tokenLogicalValue, token, outTokenLogicalValue))
				return getOriginalValueOrThrowException(this.isStrictMatching(), matcherValue, String.format("logical property expansion failed {{%s}}", token));

			tokenLogicalValue = outTokenLogicalValue.isSet() ? outTokenLogicalValue.getValue() : null;
		}

		return tokenLogicalValue != null ? tokenLogicalValue.toString() : StringUtils.EMPTY_STRING;
	}
}
