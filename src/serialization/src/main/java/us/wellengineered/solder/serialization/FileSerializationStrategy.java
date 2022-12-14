/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

public interface FileSerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromFile(Class<? extends TObject> objectClass, String inputFilePath) throws SerializationException;

	<TObject> void serializeObjectToFile(String outputFilePath, Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}