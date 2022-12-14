/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

public class InvocationException extends InterceptionException
{
	public InvocationException()
	{
		super();
	}

	public InvocationException(String message)
	{
		super(message);
	}

	public InvocationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InvocationException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
