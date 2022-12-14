/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.context;

import us.wellengineered.solder.polyfills.exceptions.ApplicationException;
import us.wellengineered.solder.primitives.SolderException;

public class ContextException extends SolderException
{
	public ContextException()
	{
		super();
	}

	public ContextException(String message)
	{
		super(message);
	}

	public ContextException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ContextException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
