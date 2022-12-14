/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

import java.io.Closeable;

public interface DbConnection extends Closeable
{
	ConnectionState getConnectionState();

	String getConnectionString();

	void setConnectionString(String connectionString);

	int getConnectionTimeout();

	String getDatabase();

	DbTransaction beginTransaction();

	DbTransaction beginTransaction(IsolationLevel il);

	void changeDatabase(String databaseName);

	void close();

	DbCommand createCommand();

	void open();
}
