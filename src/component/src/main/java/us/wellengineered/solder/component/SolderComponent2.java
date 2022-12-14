/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.configuration.SolderSpecification;
import us.wellengineered.solder.configuration.Specifiable;
import us.wellengineered.solder.configuration.UnknownTypedSolderConfiguration;

public interface SolderComponent2<TSolderConfiguration extends UnknownTypedSolderConfiguration<TSolderSpecification>, TSolderSpecification extends SolderSpecification> extends SolderComponent1<TSolderConfiguration>, Specifiable<TSolderSpecification>
{
}
