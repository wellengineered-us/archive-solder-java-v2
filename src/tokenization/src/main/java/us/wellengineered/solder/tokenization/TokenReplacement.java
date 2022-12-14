/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization;

public interface TokenReplacement
{
	Object evaluate(String[] parameters) throws TokenizationException;

	Object evaluate(String token, Object[] parameters) throws TokenizationException;
}
