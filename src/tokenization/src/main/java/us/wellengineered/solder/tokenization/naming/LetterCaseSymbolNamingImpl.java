/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.naming;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.StringUtils;
import us.wellengineered.solder.tokenization.TokenizationException;

public class LetterCaseSymbolNamingImpl extends AbstractSymbolNaming
{
	protected LetterCaseSymbolNamingImpl()
	{
		this(false);
	}

	protected LetterCaseSymbolNamingImpl(boolean upperCase)
	{
		this.upperCase = upperCase;
	}

	private final boolean upperCase;

	public boolean isUpperCase()
	{
		return this.upperCase;
	}

	@Override
	protected String coreToCamelCase(String value) throws Exception
	{
		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		return this.isUpperCase() ? value.toUpperCase() : value.toLowerCase();
	}

	@Override
	protected String coreToConstantCase(String value) throws Exception
	{
		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		return this.isUpperCase() ? value.toUpperCase() : value.toLowerCase();
	}

	@Override
	protected String coreToPascalCase(String value) throws Exception
	{
		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		return this.isUpperCase() ? value.toUpperCase() : value.toLowerCase();
	}

	@Override
	protected String coreToPluralForm(String value) throws Exception
	{
		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		return this.isUpperCase() ? value.toUpperCase() : value.toLowerCase();
	}

	@Override
	protected String coreToSingularForm(String value) throws Exception
	{
		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		return this.isUpperCase() ? value.toUpperCase() : value.toLowerCase();
	}

}
