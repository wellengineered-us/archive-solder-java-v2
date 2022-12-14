/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonGenerator.Feature.AUTO_CLOSE_TARGET;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES;
import static com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNRESOLVED_OBJECT_IDS;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES;

public class NativeJsonSerializationStrategyImpl extends AbstractCommonSerializationStrategy<JsonParser, JsonGenerator>
{
	public NativeJsonSerializationStrategyImpl()
	{
		super(StringUtils.EMPTY_STRING);
	}

	private static ObjectMapper getObjectMapper()
	{
		ObjectMapper objectMapper;

		objectMapper = new ObjectMapper();
		objectMapper
				.configure(AUTO_CLOSE_TARGET, false)
				.configure(ALLOW_COMMENTS, true)
				.configure(ACCEPT_CASE_INSENSITIVE_PROPERTIES, true)
				.configure(FAIL_ON_UNKNOWN_PROPERTIES, false)
				.configure(FAIL_ON_UNRESOLVED_OBJECT_IDS, false);

		return objectMapper;
	}

	private static ObjectWriter getObjectWriter()
	{
		ObjectWriter objectWriter;

		final ObjectMapper objectMapper = getObjectMapper();
		objectWriter = objectMapper.writerWithDefaultPrettyPrinter();

		return objectWriter;
	}

	private static <Tin, Tout> Tout convertObject(Tin value, Class<Tout> objectClass)
	{
		Tout obj;

		if (value == null)
			throw new ArgumentNullException("value");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.convertValue(value, objectClass);

		return obj;
	}

	public static <Tin extends Map<?, ?>, Tout> Tout getObjectFromJsonMap(Tin value, Class<Tout> objectClass)
	{
		return convertObject(value, objectClass);
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromByteStream(Class<? extends TObject> objectClass, InputStream inputStream) throws Exception
	{
		TObject obj;

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputStream == null)
			throw new ArgumentNullException("inputStream");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(inputStream, objectClass);

		return obj;
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromCharStream(Class<? extends TObject> objectClass, Reader inputReader) throws Exception
	{
		TObject obj;

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputReader == null)
			throw new ArgumentNullException("inputReader");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(inputReader, objectClass);

		return obj;
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromNative(Class<? extends TObject> objectClass, JsonParser nativeInput) throws Exception
	{
		TObject obj;

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (nativeInput == null)
			throw new ArgumentNullException("nativeInput");

		final ObjectMapper objectMapper = getObjectMapper();

		obj = objectMapper.readValue(nativeInput, objectClass);

		return obj;
	}

	@Override
	protected <TObject> void coreSerializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		objectWriter.writeValue(outputStream, obj);
	}

	@Override
	protected <TObject> void coreSerializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		if (outputWriter == null)
			throw new ArgumentNullException("outputWriter");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		objectWriter.writeValue(outputWriter, obj);
	}

	@Override
	protected <TObject> void coreSerializeObjectToNative(JsonGenerator nativeOutput, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		if (nativeOutput == null)
			throw new ArgumentNullException("nativeOutput");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		final ObjectWriter objectWriter = getObjectWriter();

		objectWriter.writeValue(nativeOutput, obj);
	}
}