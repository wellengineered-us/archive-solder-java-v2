/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.exceptions;

public class ArgumentOutOfRangeException extends ArgumentException
{
	public ArgumentOutOfRangeException()
	{
		super();
	}

	public ArgumentOutOfRangeException(String parameterName)
	{
		super(parameterName);
	}

	public ArgumentOutOfRangeException(String parameterName, String message)
	{
		super(parameterName, message);
	}

	public ArgumentOutOfRangeException(String message, Throwable cause)
	{
		super(null, message, cause);
	}

	public ArgumentOutOfRangeException(String parameterName, String message, Throwable cause)
	{
		super(parameterName, message, cause);
	}

	public ArgumentOutOfRangeException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
