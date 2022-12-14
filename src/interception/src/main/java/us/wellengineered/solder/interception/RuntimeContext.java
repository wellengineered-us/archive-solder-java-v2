/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.interception;

import us.wellengineered.solder.primitives.Lifecycle;

public interface RuntimeContext extends Lifecycle
{
	int getInterceptionCount();

	int getInterceptionIndex();

	void abortInterceptionChain() throws InterceptionException;

	boolean shouldContinueInterception();
}
