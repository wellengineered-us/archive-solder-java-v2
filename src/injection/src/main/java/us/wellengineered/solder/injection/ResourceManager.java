/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.polyfills.language.Func;
import us.wellengineered.solder.primitives.Disposable;
import us.wellengineered.solder.primitives.Lifecycle;

import java.util.UUID;

public interface ResourceManager extends Lifecycle
{
	void check() throws ResourceException;

	void dispose(UUID slotId, Disposable disposable) throws ResourceException;

	UUID enter() throws ResourceException;

	<TValue> TValue leave(UUID slotId, TValue value) throws ResourceException;

	void leave(UUID slotId) throws ResourceException;

	void print(UUID slotId, String message) throws ResourceException;

	void reset() throws ResourceException;

	<TDisposable extends Disposable> DisposableDispatch<TDisposable> using(UUID slotId, TDisposable disposable, Func.Arity0 onDisposal) throws ResourceException;

	<TDisposable extends Disposable> TDisposable watching(UUID slotId, TDisposable disposable) throws ResourceException;
}
