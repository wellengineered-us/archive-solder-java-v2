/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

public interface NativeSerializationStrategy<TNativeInput, TNativeOutput> extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromNative(Class<? extends TObject> objectClass, TNativeInput nativeInput) throws SerializationException;

	<TObject> void serializeObjectToNative(TNativeOutput nativeOutput, Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}