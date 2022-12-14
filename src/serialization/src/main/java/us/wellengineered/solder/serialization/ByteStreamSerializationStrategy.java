/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

import java.io.InputStream;
import java.io.OutputStream;

public interface ByteStreamSerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromByteStream(Class<? extends TObject> objectClass, InputStream inputStream) throws SerializationException;

	<TObject> void serializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}