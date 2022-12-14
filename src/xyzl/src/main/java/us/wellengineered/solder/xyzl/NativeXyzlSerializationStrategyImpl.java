/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.NotImplementedException;
import us.wellengineered.solder.primitives.StringUtils;
import us.wellengineered.solder.serialization.AbstractCommonSerializationStrategy;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public final class NativeXyzlSerializationStrategyImpl extends AbstractCommonSerializationStrategy<Void, Void>
{
	public NativeXyzlSerializationStrategyImpl(XyzlSerializer xyzlSerializer)
	{
		super(StringUtils.EMPTY_STRING);

		if (xyzlSerializer == null)
			throw new ArgumentNullException("xyzlSerializer");

		this.xyzlSerializer = xyzlSerializer;
	}

	private final XyzlSerializer xyzlSerializer;

	public XyzlSerializer getXyzlSerializer()
	{
		return this.xyzlSerializer;
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromByteStream(Class<? extends TObject> objectClass, InputStream inputStream) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromCharStream(Class<? extends TObject> objectClass, Reader inputReader) throws Exception
	{
		return this.getXyzlSerializer()._deserialize(objectClass, inputReader);
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromNative(Class<? extends TObject> objectClass, Void unused) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	protected <TObject> void coreSerializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}

	@Override
	protected <TObject> void coreSerializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		this.getXyzlSerializer()._serialize(objectClass, obj, outputWriter);
	}

	@Override
	protected <TObject> void coreSerializeObjectToNative(Void unused, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{
		throw new NotImplementedException();
	}
}
