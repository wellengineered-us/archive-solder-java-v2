/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

import java.nio.Buffer;

public interface BufferSerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromBuffer(Class<? extends TObject> objectClass, Buffer inputBuffer) throws SerializationException;

	<TObject> void serializeObjectToBuffer(Buffer outputBuffer, Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}