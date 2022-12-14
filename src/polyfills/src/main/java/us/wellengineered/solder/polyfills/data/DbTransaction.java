/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

import java.io.Closeable;

public interface DbTransaction extends Closeable
{
	DbConnection getConnection();

	IsolationLevel getIsolationLevel();

	void commit();

	void rollback();
}
