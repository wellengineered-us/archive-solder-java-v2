/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.exceptions;

public class PlatformNotSupportedException extends NotSupportedException
{
	public PlatformNotSupportedException()
	{
		super();
	}

	public PlatformNotSupportedException(String message)
	{
		super(message);
	}

	public PlatformNotSupportedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public PlatformNotSupportedException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
