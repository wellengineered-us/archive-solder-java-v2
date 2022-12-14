/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.SolderException;

public class InjectionException extends SolderException
{
	public InjectionException()
	{
		super();
	}

	public InjectionException(String message)
	{
		super(message);
	}

	public InjectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InjectionException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
