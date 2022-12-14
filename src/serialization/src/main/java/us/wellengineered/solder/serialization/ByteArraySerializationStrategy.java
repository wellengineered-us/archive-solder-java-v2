/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

public interface ByteArraySerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromByteArray(Class<? extends TObject> objectClass, byte[] inputBytes) throws SerializationException;

	<TObject> byte[] serializeObjectToByteArray(/*byte[] outputBytes,*/ Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}