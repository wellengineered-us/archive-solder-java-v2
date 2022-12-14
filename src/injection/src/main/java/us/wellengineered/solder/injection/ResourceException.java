/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

public class ResourceException extends InjectionException
{
	public ResourceException()
	{
		super();
	}

	public ResourceException(String message)
	{
		super(message);
	}

	public ResourceException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ResourceException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
