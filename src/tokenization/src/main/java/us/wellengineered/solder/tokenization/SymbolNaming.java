/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization;

public interface SymbolNaming
{
	String toCamelCase(String value) throws TokenizationException;

	String toConstantCase(String value) throws TokenizationException;

	String toPascalCase(String value) throws TokenizationException;

	String toPluralForm(String value) throws TokenizationException;

	String toSingularForm(String value) throws TokenizationException;
}
