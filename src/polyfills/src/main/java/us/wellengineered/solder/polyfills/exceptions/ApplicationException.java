/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.exceptions;

public class ApplicationException extends Exception
{
	public ApplicationException()
	{
		super();
	}

	public ApplicationException(String message)
	{
		super(message);
	}

	public ApplicationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ApplicationException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
