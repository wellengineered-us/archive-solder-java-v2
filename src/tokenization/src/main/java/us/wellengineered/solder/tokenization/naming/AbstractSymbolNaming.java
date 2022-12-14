/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.naming;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.NotImplementedException;
import us.wellengineered.solder.tokenization.SymbolNaming;
import us.wellengineered.solder.tokenization.TokenizationException;

public abstract class AbstractSymbolNaming implements SymbolNaming
{
	protected AbstractSymbolNaming()
	{
	}

	public static boolean isValidIdentifier(String value) throws TokenizationException
	{
		throw new NotImplementedException();
	}

	public static String toValidIdentifier(String value) throws TokenizationException
	{
		boolean first = true;
		StringBuilder sb;

		if (value == null)
			throw new ArgumentNullException("value");

		sb = new StringBuilder();

		for (char curr : value.toCharArray())
		{
			if (!(first && Character.isDigit(curr)) && (Character.isLetterOrDigit(curr) || curr == '_'))
				sb.append(curr);
			else if ((first && Character.isDigit(curr)) || curr == ' ')
				sb.append('_');
			else
			{
				// skip
			}

			first = false;
		}

		return sb.toString();
	}

	@Override
	public String toCamelCase(String value) throws TokenizationException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		try
		{
			return this.coreToCamelCase(value);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The symbol naming failed (see inner exception)."), ex);
		}
	}

	protected abstract String coreToCamelCase(String value) throws Exception;

	@Override
	public String toConstantCase(String value) throws TokenizationException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		try
		{
			return this.coreToConstantCase(value);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The symbol naming failed (see inner exception)."), ex);
		}
	}

	protected abstract String coreToConstantCase(String value) throws Exception;

	@Override
	public String toPascalCase(String value) throws TokenizationException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		try
		{
			return this.coreToPascalCase(value);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The symbol naming failed (see inner exception)."), ex);
		}
	}

	protected abstract String coreToPascalCase(String value) throws Exception;

	@Override
	public String toPluralForm(String value) throws TokenizationException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		try
		{
			return this.coreToPluralForm(value);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The symbol naming failed (see inner exception)."), ex);
		}
	}

	protected abstract String coreToPluralForm(String value) throws Exception;

	@Override
	public String toSingularForm(String value) throws TokenizationException
	{
		if (value == null)
			throw new ArgumentNullException("value");

		try
		{
			return this.coreToSingularForm(value);
		}
		catch (Exception ex)
		{
			throw new TokenizationException(String.format("The symbol naming failed (see inner exception)."), ex);
		}
	}

	protected abstract String coreToSingularForm(String value) throws Exception;
}
