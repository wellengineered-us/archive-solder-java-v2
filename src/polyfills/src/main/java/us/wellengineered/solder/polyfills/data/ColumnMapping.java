/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

public interface ColumnMapping
{
	String getDataSetColumn();

	void setDataSetColumn(String dataSetColumn);

	String getSourceColumn();

	void setSourceColumn(String sourceColumn);
}
