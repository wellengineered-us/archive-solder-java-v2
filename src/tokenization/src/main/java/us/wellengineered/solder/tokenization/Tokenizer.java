/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization;

import java.util.Map;

public interface Tokenizer
{
	String[] getOrderedPreviousExpansionTokens() throws TokenizationException;

	boolean isStrictMatching() throws TokenizationException;

	Map<String, TokenReplacement> getTokenReplacementStrategies() throws TokenizationException;

	String expandTokens(String tokenizedValue) throws TokenizationException;

	String expandTokens(String tokenizedValue, TokenReplacement optionalWildcardTokenReplacement) throws TokenizationException;
}
