/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: http://www.opensource.org/licenses/mit-license.php
*/

package us.wellengineered.solder.serialization;

import us.wellengineered.solder.primitives.StringUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;

public class NativeXmlSerializationStrategyImpl extends AbstractCommonSerializationStrategy<StringBuilder, StringBuilder>
{
	public NativeXmlSerializationStrategyImpl()
	{
		super(StringUtils.EMPTY_STRING);
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromByteStream(Class<? extends TObject> objectClass, InputStream inputStream) throws Exception
	{
		return null;
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromCharStream(Class<? extends TObject> objectClass, Reader inputReader) throws Exception
	{
		return null;
	}

	@Override
	protected <TObject> TObject coreDeserializeObjectFromNative(Class<? extends TObject> objectClass, StringBuilder stringBuilder) throws Exception
	{
		return null;
	}

	@Override
	protected <TObject> void coreSerializeObjectToByteStream(OutputStream outputStream, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{

	}

	@Override
	protected <TObject> void coreSerializeObjectToCharStream(Writer outputWriter, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{

	}

	@Override
	protected <TObject> void coreSerializeObjectToNative(StringBuilder stringBuilder, Class<? extends TObject> objectClass, TObject obj) throws Exception
	{

	}
}