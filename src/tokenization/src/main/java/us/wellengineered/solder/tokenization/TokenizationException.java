/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization;

import us.wellengineered.solder.polyfills.exceptions.ApplicationException;
import us.wellengineered.solder.primitives.SolderException;

public class TokenizationException extends SolderException
{
	public TokenizationException()
	{
		super();
	}

	public TokenizationException(String message)
	{
		super(message);
	}

	public TokenizationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public TokenizationException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
