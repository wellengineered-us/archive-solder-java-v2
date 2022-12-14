/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities.appsettings;

import us.wellengineered.solder.primitives.SolderException;

/**
 * The exception thrown when a application settings error occurs.
 */
public class AppSettingsException extends SolderException
{
	/**
	 * Initializes a new instance of the AppSettingsException class.
	 */
	public AppSettingsException()
	{
		super();
	}

	/**
	 * Initializes a new instance of the AppSettingsException class.
	 *
	 * @param message The message that describes the error.
	 */
	public AppSettingsException(String message)
	{
		super(message);
	}

	/**
	 * Initializes a new instance of the AppSettingsException class.
	 *
	 * @param message The message that describes the error.
	 * @param cause   The inner exception.
	 */
	public AppSettingsException(String message, Throwable cause)
	{
		super(message, cause);
	}

	/**
	 * Initializes a new instance of the AppSettingsException class.
	 *
	 * @param cause The inner exception.
	 */
	public AppSettingsException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
