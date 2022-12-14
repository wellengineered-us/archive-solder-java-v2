/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.polyfills.exceptions.NotImplementedException;

import java.io.*;
import java.nio.Buffer;

public abstract class AbstractCommonSerializationStrategy<TNativeInput, TNativeOutput> implements CommonSerializationStrategy, NativeSerializationStrategy<TNativeInput, TNativeOutput>
{
	protected AbstractCommonSerializationStrategy(String semanticVersionUrlString)
	{
		this.semanticVersionUrlString = semanticVersionUrlString;
	}

	private final String semanticVersionUrlString;

	@Override
	public final String getSemanticVersionUrlString() throws SerializationException
	{
		return this.semanticVersionUrlString;
	}

	protected abstract <TObject> TObject coreDeserializeObjectFromByteStream(Class<? extends TObject> objectClass, InputStream inputStream) throws Exception;

	protected abstract <TObject> TObject coreDeserializeObjectFromCharStream(Class<? extends TObject> objectClass, Reader inputReader) throws Exception;

	protected abstract <TObject> TObject coreDeserializeObjectFromNative(Class<? extends TObject> objectClass, TNativeInput nativeInput) throws Exception;

	protected abstract <TObject> void coreSerializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> objectClass, TObject obj) throws Exception;

	protected abstract <TObject> void coreSerializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> objectClass, TObject obj) throws Exception;

	protected abstract <TObject> void coreSerializeObjectToNative(TNativeOutput nativeOutput, Class<? extends TObject> objectClass, TObject obj) throws Exception;

	@Override
	public final <TObject> TObject deserializeObjectFromBuffer(Class<? extends TObject> objectClass, Buffer inputBuffer) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputBuffer == null)
			throw new ArgumentNullException("inputBuffer");

		throw new NotImplementedException();
	}

	@Override
	public final <TObject> TObject deserializeObjectFromByteArray(Class<? extends TObject> objectClass, byte[] inputBytes) throws SerializationException
	{
		TObject obj;

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputBytes == null)
			throw new ArgumentNullException("inputBytes");

		if (inputBytes.length == 0)
			throw new ArgumentOutOfRangeException("inputBytes");

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputBytes))
		{
			obj = this.deserializeObjectFromByteStream(objectClass, byteArrayInputStream);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}

		return obj;
	}

	@Override
	public final <TObject> TObject deserializeObjectFromByteStream(Class<? extends TObject> objectClass, InputStream inputStream) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputStream == null)
			throw new ArgumentNullException("obj");

		try
		{
			return this.coreDeserializeObjectFromByteStream(objectClass, inputStream);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> TObject deserializeObjectFromCharArray(Class<? extends TObject> objectClass, char[] inputChars) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputChars == null)
			throw new ArgumentNullException("inputChars");

		throw new NotImplementedException();
	}

	@Override
	public final <TObject> TObject deserializeObjectFromCharStream(Class<? extends TObject> objectClass, Reader inputReader) throws Exception
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputReader == null)
			throw new ArgumentNullException("inputReader");

		try
		{
			return this.coreDeserializeObjectFromCharStream(objectClass, inputReader);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> TObject deserializeObjectFromFile(Class<? extends TObject> objectClass, String inputFilePath) throws SerializationException
	{
		TObject obj;

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputFilePath == null)
			throw new ArgumentNullException("inputFilePath");

		if (inputFilePath.isEmpty())
			throw new ArgumentOutOfRangeException("inputFilePath");

		try (FileInputStream fileInputStream = new FileInputStream(inputFilePath))
		{
			obj = this.deserializeObjectFromByteStream(objectClass, fileInputStream);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}

		return obj;
	}

	@Override
	public final <TObject> TObject deserializeObjectFromNative(Class<? extends TObject> objectClass, TNativeInput nativeInput) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (nativeInput == null)
			throw new ArgumentNullException("nativeInput");

		try
		{
			return this.coreDeserializeObjectFromNative(objectClass, nativeInput);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> TObject deserializeObjectFromString(Class<? extends TObject> objectClass, String inputString) throws SerializationException
	{
		TObject obj;

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (inputString == null)
			throw new ArgumentNullException("inputString");

		if (inputString.isEmpty())
			throw new ArgumentOutOfRangeException("inputString");

		try (ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(inputString.getBytes()))
		{
			obj = this.deserializeObjectFromByteStream(objectClass, byteArrayInputStream);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}

		return obj;
	}

	@Override
	public final <TObject> void serializeObjectToBuffer(Buffer outputBuffer, Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (outputBuffer == null)
			throw new ArgumentNullException("outputBuffer");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		throw new NotImplementedException();
	}

	@Override
	public final <TObject> byte[] serializeObjectToByteArray(Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
		{
			this.serializeObjectToByteStream(byteArrayOutputStream, objectClass, obj);

			final byte[] result = byteArrayOutputStream.toByteArray();

			return result;
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> void serializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try
		{
			this.coreSerializeObjectToByteStream(outputStream, objectClass, obj);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> byte[] serializeObjectToCharArray(Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		throw new NotImplementedException();
	}

	@Override
	public final <TObject> void serializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		if (outputWriter == null)
			throw new ArgumentNullException("outputWriter");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try
		{
			this.coreSerializeObjectToCharStream(outputWriter, objectClass, obj);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> void serializeObjectToFile(String outputFilePath, Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (outputFilePath == null)
			throw new ArgumentNullException("outputFilePath");

		if (outputFilePath.isEmpty())
			throw new ArgumentOutOfRangeException("outputFilePath");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (FileOutputStream fileOutputStream = new FileOutputStream(outputFilePath))
		{
			this.serializeObjectToByteStream(fileOutputStream, objectClass, obj);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> void serializeObjectToNative(TNativeOutput nativeOutput, Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (nativeOutput == null)
			throw new ArgumentNullException("nativeOutput");

		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try
		{
			this.coreSerializeObjectToNative(nativeOutput, objectClass, obj);
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}

	@Override
	public final <TObject> String serializeObjectToString(Class<? extends TObject> objectClass, TObject obj) throws SerializationException
	{
		if (objectClass == null)
			throw new ArgumentNullException("objectClass");

		if (obj == null)
			throw new ArgumentNullException("obj");

		try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
		{
			this.serializeObjectToByteStream(byteArrayOutputStream, objectClass, obj);

			final String result = byteArrayOutputStream.toString();

			return result;
		}
		catch (Exception ex)
		{
			throw new SerializationException(String.format("The serialization strategy failed (see inner exception)."), ex);
		}
	}
}