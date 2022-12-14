/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.exceptions;

public class IndexOutOfRangeException extends SystemException
{
	public IndexOutOfRangeException()
	{
		super();
	}

	public IndexOutOfRangeException(String message)
	{
		super(message);
	}

	private static final long serialVersionUID = -5365630128856068164L;
}
