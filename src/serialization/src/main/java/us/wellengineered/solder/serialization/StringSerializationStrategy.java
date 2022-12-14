/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

public interface StringSerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromString(Class<? extends TObject> objectClass, String inputString) throws SerializationException;

	<TObject> String serializeObjectToString(/*String outputString,*/ Class<? extends TObject> objectClass, TObject obj) throws SerializationException;
}