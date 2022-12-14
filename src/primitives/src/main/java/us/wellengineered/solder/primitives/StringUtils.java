/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;

import java.lang.module.ModuleDescriptor;
import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.Formattable;
import java.util.UUID;

public final class StringUtils
{
	public static final String EMPTY_STRING = "";

	public static boolean equalsByValue(String objA, String objB)
	{
		if ((objA != null && objB == null) ||
				(objA == null && objB != null))
			return false; // prevent null coalescence

		return (objA != null ? objA.equals(objB) : (objB == null || objB.equals(objA) /* both null */));
	}

	public static boolean equalsIgnoreCaseByValue(String objA, String objB)
	{
		if ((objA != null && objB == null) ||
				(objA == null && objB != null))
			return false; // prevent null coalescence

		return (objA != null ? objA.equalsIgnoreCase(objB) : (objB == null || objB.equalsIgnoreCase(objA) /* both null */));
	}

	public static boolean isUsEnglishVowel(char ch)
	{
		switch (ch)
		{
			case 'A':
			case 'E':
			case 'I':
			case 'O':
			case 'U':
			case 'Y':
				return true;
			case 'a':
			case 'e':
			case 'i':
			case 'o':
			case 'u':
			case 'y':
				return true;
			default:
				return false;
		}
	}

	public static boolean isNullOrEmpty(String value)
	{
		return value == null || value.length() <= 0;
	}

	public static boolean isNullOrEmptyString(String value)
	{
		return value == null || value.isEmpty();
	}

	public static boolean isNullOrWhiteSpace(String value)
	{
		return value == null || isWhiteSpace(value);
	}

	public static boolean isWhiteSpace(String value)
	{
		if (value == null)
			return false;

		for (int i = 0; i < value.length(); i++)
		{
			if (!Character.isWhitespace(value.charAt(i))) // failed test found bug in code 2022-07-17#dpb: was Character.isSpaceChar()
				return false;
		}

		return true;
	}

	public static <TValue> String toStringEx(TValue value, String format, String defaultValue)
	{
		String retval;

		defaultValue = defaultValue == null ? EMPTY_STRING : defaultValue;

		if (value == null)
			retval = defaultValue;
		else if (value instanceof Formattable)
			retval = String.format(format, value);
		else
			retval = value.toString();

		if (isNullOrEmptyString(retval))
			retval = defaultValue;

		return retval;
	}

	public static String toStringEx(Object value)
	{
		return value == null ? EMPTY_STRING : value.toString();
	}

	@SuppressWarnings("unchecked")
	public static <TValue> boolean tryParse(Class<? extends TValue> valueClass, String value, MethodParameterModifier.Out<TValue> outResult)
	{
		boolean retval;
		boolean nonNullable;
		TValue result;

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		if (outResult == null)
			throw new ArgumentNullException("outResult");

		if (valueClass == String.class)
		{
			retval = true;
			result = (TValue) value;
		}
		else if ((nonNullable = valueClass == boolean.class)
				|| valueClass == Boolean.class)
		{
			Boolean zresult;
			zresult = Boolean.parseBoolean(value);
			retval = true; // OK, for now
			/*.booleanValue()*/
			result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
		}
		else if ((nonNullable = valueClass == char.class)
				|| valueClass == Character.class)
		{
			Character zresult;
			zresult = (retval = !isNullOrEmpty(value)) ? value.charAt(0) : null;
			/*.charValue()*/
			result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
		}
		else if ((nonNullable = valueClass == byte.class)
				|| valueClass == Byte.class)
		{
			Byte zresult;

			try
			{
				zresult = Byte.parseByte(value);
				retval = true;
				/*.intValue()*/
				result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == short.class)
				|| valueClass == Short.class)
		{
			Short zresult;

			try
			{
				zresult = Short.parseShort(value);
				retval = true;
				/*.intValue()*/
				result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == int.class)
				|| valueClass == Integer.class)
		{
			Integer zresult;

			try
			{
				zresult = Integer.parseInt(value);
				retval = true;
				/*.intValue()*/
				result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == long.class)
				|| valueClass == Long.class)
		{
			Long zresult;

			try
			{
				zresult = Long.parseLong(value);
				retval = true;
				/*.longValue()*/
				result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == float.class)
				|| valueClass == Float.class)
		{
			Float zresult;

			try
			{
				zresult = Float.parseFloat(value);
				retval = true;
				/*.intValue()*/
				result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if ((nonNullable = valueClass == double.class)
				|| valueClass == Double.class)
		{
			Double zresult;

			try
			{
				zresult = Double.parseDouble(value);
				retval = true;
				/*.intValue()*/
				result = (TValue) (zresult); // cannot return un-boxed value type in Java via generic??
			}
			catch (NumberFormatException nfex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass == UUID.class)
		{
			UUID zresult;

			try
			{
				zresult = UUID.fromString(value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (IllegalArgumentException iaex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass == Instant.class)
		{
			Instant zresult;

			try
			{
				zresult = Instant.parse(value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (DateTimeParseException dtpex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass == ModuleDescriptor.Version.class)
		{
			ModuleDescriptor.Version zresult;

			try
			{
				zresult = ModuleDescriptor.Version.parse(value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (IllegalArgumentException iaex)
			{
				retval = false;
				result = null;
			}
		}
		else if (valueClass.isEnum()) // special case
		{
			Enum<?> zresult;

			try
			{
				zresult = Enum.valueOf((Class<? extends Enum>) valueClass, value);
				retval = true;
				result = (TValue) zresult;
			}
			catch (IllegalArgumentException iaex)
			{
				retval = false;
				result = null;
			}
		}
		else
			throw new ArgumentOutOfRangeException("valueClass", String.format("The value type '%s' is not supported.", valueClass.getName()));

		outResult.setValue(result);
		return retval;
	}
}
