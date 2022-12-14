/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.polyfills.exceptions.ApplicationException;
import us.wellengineered.solder.primitives.SolderException;

public class InterceptionException extends SolderException
{
	public InterceptionException()
	{
		super();
	}

	public InterceptionException(String message)
	{
		super(message);
	}

	public InterceptionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public InterceptionException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
