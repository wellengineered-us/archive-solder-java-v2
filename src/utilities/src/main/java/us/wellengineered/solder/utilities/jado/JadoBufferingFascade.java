/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities.jado;

import us.wellengineered.solder.polyfills.data.*;

import java.sql.Driver;
import java.util.Map;

public interface JadoBufferingFascade
{
	DbParameter createParameter(Class<? extends Driver> driverClass, String sourceColumn, ParameterDirection parameterDirection, DbType parameterType, int parameterSize, byte parameterPrecision, byte parameterScale, boolean parameterIsNullable, String parameterName, Object parameterValue) throws JadoBufferingException;

	Iterable<Map<String, Object>> executeRecords(boolean schemaOnly, Class<? extends Driver> driverClass, String connectionUrl, boolean transactional, IsolationLevel isolationLevel, CommandType commandType, String commandText, Iterable<DbParameter> commandParameters, RecordsAffectedDelegate resultCallback) throws JadoBufferingException;
}
