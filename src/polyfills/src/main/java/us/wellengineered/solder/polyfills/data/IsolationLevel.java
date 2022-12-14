/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

import java.sql.Connection;

public enum IsolationLevel
{
	NONE(Connection.TRANSACTION_NONE),

	READ_COMMITTED(Connection.TRANSACTION_READ_COMMITTED),

	READ_UNCOMMITTED(Connection.TRANSACTION_READ_UNCOMMITTED),

	REPEATABLE_READ(Connection.TRANSACTION_REPEATABLE_READ),

	SERIALIZABLE(Connection.TRANSACTION_SERIALIZABLE);

	IsolationLevel(final Integer value)
	{
		this.value = value;
	}

	private final Integer value;
}
