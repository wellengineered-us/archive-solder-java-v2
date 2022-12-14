/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

public class DependencyException extends InjectionException
{
	public DependencyException()
	{
		super();
	}

	public DependencyException(String message)
	{
		super(message);
	}

	public DependencyException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DependencyException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
