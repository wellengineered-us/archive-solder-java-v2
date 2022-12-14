/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

public interface UnknownTypedSolderConfiguration<TSolderSpecification extends SolderSpecification> extends UnknownUntypedSolderConfiguration
{
	Class<? extends TSolderSpecification> getTypedSpecificationClass();

	TSolderSpecification getTypedSpecification();
}
