/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.primitives.Lifecycle;

import java.util.UUID;

public interface SolderComponent0 extends Lifecycle
{
	UUID getComponentId();

	boolean isReusable();
}
