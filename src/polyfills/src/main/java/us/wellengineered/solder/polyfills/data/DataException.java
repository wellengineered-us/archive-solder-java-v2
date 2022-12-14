/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

public class DataException extends RuntimeException
{
	public DataException()
	{
		super();
	}

	public DataException(String message)
	{
		super(message);
	}

	public DataException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public DataException(Throwable cause)
	{
		super(cause);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
