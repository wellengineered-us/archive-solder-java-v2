/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.serialization;

import us.wellengineered.solder.polyfills.exceptions.ApplicationException;
import us.wellengineered.solder.primitives.SolderException;

public class SerializationException extends SolderException
{
	public SerializationException()
	{
		super();
	}

	public SerializationException(String message)
	{
		super(message);
	}

	public SerializationException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public SerializationException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
