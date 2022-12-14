/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.unit.tokenization;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.wellengineered.solder.primitives.StringUtils;
import us.wellengineered.solder.tokenization.TokenReplacement;
import us.wellengineered.solder.tokenization.Tokenizer;
import us.wellengineered.solder.tokenization.TokenizerImpl;

import java.util.Map;

public class TokenizerImplTests
{
	public TokenizerImplTests()
	{
	}

	@Test
	public void shouldCreateTest() throws Exception
	{
		Tokenizer tokenizer;
		Map<String, TokenReplacement> mockTokenReplacementStrategies;

		mockTokenReplacementStrategies = Mockito.mock(Map.class);

		tokenizer = new TokenizerImpl(mockTokenReplacementStrategies, true);

		Assertions.assertNotNull(tokenizer);
		Assertions.assertNotNull(tokenizer.getTokenReplacementStrategies());
		Assertions.assertTrue(tokenizer.isStrictMatching());

		Mockito.verifyNoMoreInteractions(mockTokenReplacementStrategies);
	}

	@Test
	public void shouldExpandTokensLooseMatchingTest() throws Exception
	{
		Tokenizer tokenizer;
		Map<String, TokenReplacement> mockTokenReplacementStrategies;
		TokenReplacement mockTokenReplacement;

		String tokenizedValue;
		String expandedValue;
		String expectedValue;

		mockTokenReplacementStrategies = Mockito.mock(Map.class);
		mockTokenReplacement = Mockito.mock(TokenReplacement.class);

		Mockito.when(mockTokenReplacementStrategies.containsKey("myValueSemanticToken")).thenReturn(true);
		Mockito.when(mockTokenReplacementStrategies.get("myValueSemanticToken")).thenReturn(mockTokenReplacement);
		Mockito.when(mockTokenReplacement.evaluate(null)).thenReturn("testValue");
/*
		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("myValueSemanticToken"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(null)).WillReturn("testValue");

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("myFunctionSemanticToken0"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(new string[] { })).WillReturn("testValue");

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("myFunctionSemanticToken1"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(new string[] { "a", })).WillReturn("testValue");

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("myFunctionSemanticToken2"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(new string[] { "a", "b" })).WillReturn("testValue");

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("myUnkSemanticToken"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", null), Return.Value(false));

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("myErrSemanticToken"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(null)).Will(Throw.Exception(new Exception()));

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("a"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(null)).WillReturn(string.Empty);

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("b"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(null)).WillReturn(string.Empty);

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("c"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(null)).WillReturn(string.Empty);

		Expect.On(mockTokenReplacementStrategies).One.Method(x => x.TryGetValue(_unusedString, out unusedTokenReplacement)).With(new EqualMatcher("d"), new AndMatcher(new ArgumentsMatcher.OutMatcher(), new AlwaysMatcher(true, string.Empty))).Will(new SetNamedParameterAction("value", mockTokenReplacement), Return.Value(true));
		Expect.On(mockTokenReplacement).One.Method(x => x.Evaluate(_unusedStrings)).With(new EqualMatcher(null)).Will(Throw.Exception(new Exception()));
*/
		tokenizer = new TokenizerImpl(mockTokenReplacementStrategies, false);

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

		tokenizedValue = "...${myFunctionSemanticToken0()}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...testValue...";
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...${myFunctionSemanticToken1(`a`)}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...testValue...";
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...${myFunctionSemanticToken2(`a`,  `b`)}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...testValue...";
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...${myUnkSemanticToken}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...${myUnkSemanticToken}...";
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...${myErrSemanticToken}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "...${myErrSemanticToken}...";
		Assertions.assertEquals(expectedValue, expandedValue);

		tokenizedValue = "...${a}...${c}...${b}...${d}...";
		expandedValue = tokenizer.expandTokens(tokenizedValue);
		expectedValue = "............${d}...";
		Assertions.assertEquals(expectedValue, expandedValue);

		Assertions.assertNotNull(tokenizer.getOrderedPreviousExpansionTokens());
		Assertions.assertEquals("a,b,c,d", String.join(",", tokenizer.getOrderedPreviousExpansionTokens()));

		Mockito.verifyNoMoreInteractions(mockTokenReplacementStrategies, mockTokenReplacement);
	}
}
