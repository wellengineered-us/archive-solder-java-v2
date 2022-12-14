/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.Disposable;

public interface DisposableDispatch<TDisposable extends Disposable> extends Disposable
{
	TDisposable getTarget();
}
