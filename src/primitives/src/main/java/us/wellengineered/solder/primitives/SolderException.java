/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.polyfills.exceptions.ApplicationException;

public class SolderException extends ApplicationException
{
	public SolderException()
	{
		super();
	}

	public SolderException(String message)
	{
		super(message);
	}

	public SolderException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SolderException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
