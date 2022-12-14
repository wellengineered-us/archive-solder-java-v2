/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.primitives.SolderException;

/**
 * The exception thrown when a XYZL error occurs.
 */
public class XyzlException extends SolderException
{
	/**
	 * Initializes a new instance of the XyzlException class.
	 */
	public XyzlException()
	{
		super();
	}

	/**
	 * Initializes a new instance of the XyzlException class.
	 *
	 * @param message The message that describes the error.
	 */
	public XyzlException(String message)
	{
		super(message);
	}

	/**
	 * Initializes a new instance of the XyzlException class.
	 *
	 * @param message The message that describes the error.
	 * @param cause   The inner exception.
	 */
	public XyzlException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Initializes a new instance of the XyzlException class.
	 *
	 * @param cause The inner exception.
	 */
	public XyzlException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
