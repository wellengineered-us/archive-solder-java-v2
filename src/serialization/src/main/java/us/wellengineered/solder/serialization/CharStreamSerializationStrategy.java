/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

import java.io.Reader;
import java.io.Writer;

public interface CharStreamSerializationStrategy extends SerializationStrategy
{
	<TObject> TObject deserializeObjectFromCharStream(Class<? extends TObject> objectClass, Reader inputReader) throws Exception;

	<TObject> void serializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> objectClass, TObject obj) throws Exception;
}