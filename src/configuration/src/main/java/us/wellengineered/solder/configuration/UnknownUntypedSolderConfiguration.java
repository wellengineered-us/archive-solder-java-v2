/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

import java.util.Map;

public interface UnknownUntypedSolderConfiguration extends SolderConfiguration
{
	SolderSpecification getBaseSpecification();

	String getComponentClassName();

	void setComponentClassName(String componentClassName);

	Class<? extends SolderSpecification> getBaseSpecificationClass();

	Map<String, Object> getUntypedSpecification();
}
