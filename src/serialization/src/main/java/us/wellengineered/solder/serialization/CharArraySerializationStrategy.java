/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

public interface CharArraySerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromCharArray(Class<? extends TObject> objectClass, char[] inputChars) throws SerializationException;

	<TObject> byte[] serializeObjectToCharArray(/*char[] outputChars,*/ Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}