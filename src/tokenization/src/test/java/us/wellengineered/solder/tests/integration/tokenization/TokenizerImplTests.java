/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.tokenization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.wellengineered.solder.primitives.StringUtils;
import us.wellengineered.solder.tokenization.TokenReplacement;
import us.wellengineered.solder.tokenization.Tokenizer;
import us.wellengineered.solder.tokenization.TokenizerImpl;
import us.wellengineered.solder.tokenization.replacements.AbstractTokenReplacement;

import java.util.HashMap;
import java.util.Map;

public class TokenizerImplTests
{
	public TokenizerImplTests()
	{
	}

	@Test
	public void shouldExpandTokensLooseMatchingTest() throws Exception
	{
		Tokenizer tokenizer;
		Map<String, TokenReplacement> tokenReplacementStrategies;

		String tokenizedValue;
		String expandedValue;
		String expectedValue;

		tokenReplacementStrategies = new HashMap<>();
		tokenReplacementStrategies.put("myValueSemanticToken", new AbstractTokenReplacement()
		{
			@Override
			protected Object coreEvaluate(String[] parameters)
			{
				return "testValue";
			}

			@Override
			protected Object coreEvaluate(String token, Object[] parameters)
			{
				return "testValue";
			}
		});
		tokenizer = new TokenizerImpl(tokenReplacementStrategies, false);

		tokenizedValue = StringUtils.EMPTY_STRING;
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = StringUtils.EMPTY_STRING;
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...{myNoSemanticToken}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...{myNoSemanticToken}...";
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...${myValueSemanticToken}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...testValue...";
		Assertions.assertEquals(expectedValue, expandedValue);
	}
}
