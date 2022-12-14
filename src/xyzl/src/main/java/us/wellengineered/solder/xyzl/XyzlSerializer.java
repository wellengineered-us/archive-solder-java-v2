/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.primitives.Lifecycle;

import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

/**
 * Provides custom optimized XML configuration object serializer/deserializer behavior.
 */
public interface XyzlSerializer extends Lifecycle
{
	<TObject> TObject _deserialize(Class<? extends TObject> knownObjectClass, Reader inputReader) throws XyzlException;

	<TObject> void _serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, Writer outputWriter) throws XyzlException;

	void clearAllKnownObjects() throws XyzlException;

	<TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, String fileName) throws XyzlException;

	<TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, InputStream inputStream) throws XyzlException;

	<TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, XMLStreamReader xmlStreamReader) throws XyzlException;

	<TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, Reader inputReader) throws XyzlException;

	<TObject extends XyzlModel> void registerKnownObject(Class<? extends TObject> knownObjectClass) throws XyzlException;

	<TObject extends XyzlModel> void registerKnownObject(Class<? extends TObject> knownObjectClass, XmlName xmlName) throws XyzlException;

	<TObject extends XyzlValue<String>> void registerKnownValueObject(Class<? extends TObject> knownValueObjectClass) throws XyzlException;

	<TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, String fileName) throws XyzlException;

	<TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, OutputStream outputStream) throws XyzlException;

	<TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, XMLStreamWriter xmlStreamWriter) throws XyzlException;

	<TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, Writer outputWriter) throws XyzlException;

	<TObject extends XyzlModel> boolean unregisterKnownObject(Class<? extends TObject> knownObjectClass) throws XyzlException;

	<TObject extends XyzlValue<String>> boolean unregisterKnownValueObject(Class<? extends TObject> knownValueObjectClass) throws XyzlException;
}
