/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.naming;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.StringUtils;
import us.wellengineered.solder.tokenization.TokenizationException;

public class UsEnglishIdentifierSymbolNamingImpl extends AbstractSymbolNaming
{
	protected UsEnglishIdentifierSymbolNamingImpl()
	{
		this(false);
	}

	protected UsEnglishIdentifierSymbolNamingImpl(boolean nameManglingEnabled)
	{
		this.nameManglingEnabled = nameManglingEnabled;
	}

	private final boolean nameManglingEnabled;

	public boolean isNameManglingEnabled()
	{
		return this.nameManglingEnabled;
	}

	@Override
	protected String coreToCamelCase(String value) throws Exception
	{
		StringBuilder sb;
		char prev;
		int i = 0;
		boolean toupper = false;

		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		if (value.length() < 1)
			return value;

		sb = new StringBuilder();
		prev = value.charAt(0);

		for (char curr : value.toCharArray())
		{
			if (curr == '_')
			{
				toupper = true;

				if (!this.isNameManglingEnabled())
					sb.append(curr);

				continue; // ignore setting prev=curr
			}

			toupper = toupper || Character.isDigit(prev) || (Character.isLowerCase(prev) && Character.isUpperCase(curr));

			if (toupper)
				sb.append(Character.toUpperCase(curr));
			else
				sb.append(Character.toLowerCase(curr));

			prev = curr;
			i++;
			toupper = false;
		}

		return sb.toString();
	}

	@Override
	protected String coreToConstantCase(String value) throws Exception
	{
		StringBuilder sb;
		char prev;

		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		if (value.length() < 1)
			return value;

		sb = new StringBuilder();
		prev = value.charAt(0);

		for (char curr : value.toCharArray())
		{
			if (this.isNameManglingEnabled() && curr == '_')
				continue;

			if (Character.isLowerCase(prev) && Character.isUpperCase(curr))
				sb.append('_');

			sb.append(Character.toUpperCase(curr));
			prev = curr;
		}

		return sb.toString();
	}

	@Override
	protected String coreToPascalCase(String value) throws Exception
	{
		StringBuilder sb;

		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		if (value.length() < 1)
			return value;

		value = this.toCamelCase(value);
		sb = new StringBuilder(value);

		sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));

		return sb.toString();
	}

	@Override
	protected String coreToPluralForm(String value) throws Exception
	{
		StringBuilder sb;

		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		if (!this.isNameManglingEnabled())
			return value;

		if (value.length() < 1)
			return value;

		sb = new StringBuilder(value);

		if (value.endsWith("x") || value.endsWith("X") ||
				value.endsWith("ch") || value.endsWith("CH") ||
				value.endsWith("ss") || value.endsWith("SS") ||
						value.endsWith("sh") || value.endsWith("SH"))
			sb.append("es");
		else if ((value.endsWith("y") || value.endsWith("Y")) && value.length() > 1 &&
				!StringUtils.isUsEnglishVowel(value.charAt(value.length() - 2)))
		{
			sb.replace(value.length() - 1, value.length() - 1, StringUtils.EMPTY_STRING);
			sb.append("ies");
		}
		else if (!value.endsWith("s") || value.endsWith("S"))
			sb.append("s");

		return sb.toString();
	}

	@Override
	protected String coreToSingularForm(String value) throws Exception
	{
		StringBuilder sb;

		if (value == null)
			throw new ArgumentNullException("value");

		value = StringUtils.toStringEx(toValidIdentifier(value), null, StringUtils.EMPTY_STRING);

		if (!this.isNameManglingEnabled())
			return value;

		if (value.length() < 1)
			return value;

		sb = new StringBuilder(value);

		if (String.CASE_INSENSITIVE_ORDER.compare(value, "series") == 0)
			return sb.toString();

		if (String.CASE_INSENSITIVE_ORDER.compare(value, "wines") == 0)
		{
			sb.replace(value.length() - 1, value.length() - 1, StringUtils.EMPTY_STRING);
			return sb.toString();
		}

		if (value.length() > 3 && (value.endsWith("ies") || value.endsWith("IES")))
		{
			if (!StringUtils.isUsEnglishVowel(value.charAt(value.length() - 4)))
			{
				sb.replace(value.length() - 3, value.length() - 1, StringUtils.EMPTY_STRING);
				sb.append("y");
			}
		}
		else if (value.endsWith("ees") || value.endsWith("EES"))
			sb.replace(value.length() - 1, value.length() - 1, StringUtils.EMPTY_STRING);
		else if (value.endsWith("ches") || value.endsWith("xes") || value.endsWith("sses") ||
				value.endsWith("CHES") || value.endsWith("XES") || value.endsWith("SSES"))
			sb.replace(value.length() - 2, value.length() - 1, StringUtils.EMPTY_STRING);
		else
		{
			if (String.CASE_INSENSITIVE_ORDER.compare(value, "gas") == 0 ||
					value.length() <= 1 ||
					(!value.endsWith("s") || value.endsWith("ss")) || value.endsWith("us") ||
					(!value.endsWith("S") || value.endsWith("SS")) || value.endsWith("US"))
				return sb.toString();

			sb.replace(value.length() - 1, value.length() - 1, StringUtils.EMPTY_STRING);
		}

		return sb.toString();
	}
}
