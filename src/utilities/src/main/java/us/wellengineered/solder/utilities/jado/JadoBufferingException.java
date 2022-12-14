/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities.jado;

import us.wellengineered.solder.primitives.SolderException;

public class JadoBufferingException extends SolderException
{
	/**
	 * Initializes a new instance of the JadoBufferingException class.
	 */
	public JadoBufferingException()
	{
		super();
	}

	/**
	 * Initializes a new instance of the JadoBufferingException class.
	 *
	 * @param message The message that describes the error.
	 */
	public JadoBufferingException(String message)
	{
		super(message);
	}

	/**
	 * Initializes a new instance of the JadoBufferingException class.
	 *
	 * @param message The message that describes the error.
	 * @param cause   The inner exception.
	 */
	public JadoBufferingException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Initializes a new instance of the JadoBufferingException class.
	 *
	 * @param cause The inner exception.
	 */
	public JadoBufferingException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
