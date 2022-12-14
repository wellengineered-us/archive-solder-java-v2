/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

public interface TableMapping
{
	ColumnMappingCollection getColumnMappings();

	String getDataSetTable();

	void setDataSetTable(String dataSetTable);

	String getSourceTable();

	void setSourceTable(String sourceTable);
}
