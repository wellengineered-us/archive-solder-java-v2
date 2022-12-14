/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.executive;

import us.wellengineered.solder.primitives.SolderException;

public class ExecutableException extends SolderException
{
	public ExecutableException()
	{
		super();
	}

	public ExecutableException(String message)
	{
		super(message);
	}

	public ExecutableException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ExecutableException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
