/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.injection.DependencyException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.primitives.AbstractLifecycle;
import us.wellengineered.solder.primitives.ReflectionUtils;
import us.wellengineered.solder.primitives.StringUtils;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.stream.events.XMLEvent;
import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static us.wellengineered.solder.primitives.StringUtils.equalsIgnoreCaseByValue;

public final class XyzlSerializerImpl extends AbstractLifecycle<Exception, Exception> implements XyzlSerializer
{
	public XyzlSerializerImpl()
	{
		this(new HashMap<>());
	}

	public XyzlSerializerImpl(Map<XmlName, Class<?>> knownObjectRegistrations)
	{
		if (knownObjectRegistrations == null)
			throw new ArgumentNullException("knownObjectRegistrations");

		this.knownObjectRegistrations = knownObjectRegistrations;
	}

	private final Map<XmlName, Class<?>> knownObjectRegistrations;
	private Class<?> knownValueObjectRegistration;

	private Map<XmlName, Class<?>> getKnownObjectRegistrations()
	{
		return this.knownObjectRegistrations;
	}

	private Class<?> getKnownValueObjectRegistration()
	{
		return this.knownValueObjectRegistration;
	}

	private void setKnownValueObjectRegistration(Class<?> knownValueObjectRegistration)
	{
		this.knownValueObjectRegistration = knownValueObjectRegistration;
	}

	@Override
	public <TObject> TObject _deserialize(Class<? extends TObject> knownObjectClass, Reader inputReader) throws XyzlException
	{
		return null;
	}

	@Override
	public <TObject> void _serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, Writer outputWriter) throws XyzlException
	{

	}

	@Override
	public void clearAllKnownObjects() throws XyzlException
	{
		this.getKnownObjectRegistrations().clear();
		this.setKnownValueObjectRegistration(null);
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		// do nothing
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		// do nothing
	}

	@Override
	public <TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, String fileName) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (fileName == null)
			throw new ArgumentNullException("fileName");

		try
		{
			return this.coreDeserialize(knownObjectClass, fileName);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	protected <TObject extends XyzlModel> TObject coreDeserialize(Class<? extends TObject> knownObjectClass, String fileName) throws Exception
	{
		TObject document;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (fileName == null)
			throw new ArgumentNullException("fileName");

		if (StringUtils.isNullOrWhiteSpace(fileName))
			throw new ArgumentOutOfRangeException("fileName");

		try (FileInputStream fileInputStream = new FileInputStream(fileName))
		{
			document = this.coreDeserialize(knownObjectClass, fileInputStream);
			return document;
		}
	}

	@Override
	public <TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, InputStream inputStream) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (inputStream == null)
			throw new ArgumentNullException("inputStream");

		try
		{
			return this.coreDeserialize(knownObjectClass, inputStream);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> TObject coreDeserialize(Class<? extends TObject> knownObjectClass, InputStream inputStream) throws Exception
	{
		XMLStreamReader xmlStreamReader;
		TObject document;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (inputStream == null)
			throw new ArgumentNullException("inputStream");

		// DO NOT USE A USING BLOCK HERE (CALLER OWNS STREAM) !!!
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputStream);

		document = this.coreDeserialize(knownObjectClass, xmlStreamReader);
		return document;
	}

	@Override
	public <TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, XMLStreamReader xmlStreamReader) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (xmlStreamReader == null)
			throw new ArgumentNullException("xmlReader");

		try
		{
			return this.coreDeserialize(knownObjectClass, xmlStreamReader);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> TObject coreDeserialize(Class<? extends TObject> knownObjectClass, XMLStreamReader xmlStreamReader) throws Exception
	{
		XmlName elementXmlName = null, attributeXmlName, previousElementXmlName;

		TObject documentXyzlModel = null;
		XyzlModel currentXyzlModel;

		Stack<XyzlModel> contextStack;
		boolean isEmptyElement, isTextElement;
		Map<XmlName, String> attributes;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (xmlStreamReader == null)
			throw new ArgumentNullException("xmlStreamReader");

		// setup contextual data
		attributes = new HashMap<>();
		contextStack = new Stack<>();

		// walk the XML document
		while (xmlStreamReader.hasNext())
		{
			// get node type
			final int eventType = xmlStreamReader.next();

			// determine node type
			if (eventType == XMLEvent.CDATA ||
					eventType == XMLEvent.CHARACTERS)
			{
				// clear previous attributes
				attributes.clear();

				// is this a text element?
				isTextElement = this.coreIsTextElement(contextStack, elementXmlName != null ? elementXmlName : new XmlName());

				// get the current XML configuration object as XML text object
				currentXyzlModel = this.coreDeserializeFromXmlText(contextStack, xmlStreamReader.getText(), isTextElement ? elementXmlName : null);

				// is this a text element? if so, deserialize into it in a special maner
				if (isTextElement)
					this.coreDeserializeFromXml(contextStack, null, elementXmlName, attributes, (XyzlValue<String>)currentXyzlModel);
			}
			else if (eventType == XMLEvent.START_ELEMENT)
			{
				// debugWrite(String.format("%s <%s%s>", xmlReader.LocalName, xmlReader.IsEmptyElement ? " /" : string.Empty, xmlReader.IsEmptyElement ? "empty" : "begin"));

				// stash away previous element
				//previousElementXyzlName = elementXyzlName;

				// fixes a bug here
				if (!contextStack.isEmpty())
					previousElementXmlName = this.coreGetConfiguredName(contextStack.peek());
				else
					previousElementXmlName = null;

				// create the element XML name
				elementXmlName = new XmlName();
				elementXmlName.setLocalName(xmlStreamReader.getLocalName());
				elementXmlName.setNamespaceUri(xmlStreamReader.getNamespaceURI());

				// is this an empty element?
				isEmptyElement = xmlStreamReader.isStandalone();

				// is this a text element?
				isTextElement = this.coreIsTextElement(contextStack, elementXmlName);

				// clear previous attributes
				attributes.clear();

				// iterate over attributes of current element
				for (int ai = 0; ai < xmlStreamReader.getAttributeCount(); ai++)
				{
					// traverse to next attribute
					//xmlStreamReader.isAttributeSpecified(ai);

					// create the attribute XML name
					attributeXmlName = new XmlName();
					attributeXmlName.setLocalName(xmlStreamReader.getAttributeLocalName(ai));
					attributeXmlName.setNamespaceUri(xmlStreamReader.getAttributeNamespace(ai));

					// append to attribute collection
					attributes.put(attributeXmlName, xmlStreamReader.getAttributeValue(ai));
				}

				// clear attribute name
				attributeXmlName = null;

				// is this not a text element?
				if (!isTextElement)
				{
					// deserialize current XML configuration object
					currentXyzlModel = this.coreDeserializeFromXml(contextStack, previousElementXmlName, elementXmlName, attributes, null);
				}
				else
				{
					// use 'dummy' current XML configuration object (the parent so depth counts are correct and IsTextElement() works)
					currentXyzlModel = contextStack.peek();
				}

				// check context stack depth for emptiness
				if (contextStack.isEmpty())
				{
					// document element is current element when no context present
					documentXyzlModel = (TObject)currentXyzlModel;
				}

				// push current XML configuration object as parent XML configuration object if there are children possible (no empty element)
				if (!isEmptyElement)
					contextStack.push(currentXyzlModel);
			}
			else if (eventType == XMLEvent.END_ELEMENT)
			{
				// DebugWrite(string.Format("end <{0}>", xmlReader.LocalName));

				// create the element XML name
				elementXmlName = new XmlName();
				elementXmlName.setLocalName(xmlStreamReader.getLocalName());
				elementXmlName.setNamespaceUri(xmlStreamReader.getNamespaceURI());

				// is this a text element?
				isTextElement = this.coreIsTextElement(contextStack, elementXmlName);

				// sanity check
				if (contextStack.isEmpty())
					throw new XyzlException(String.format("TODO: error message"));

				// pop element off stack (unwind)
				contextStack.pop();

				// clear attribute name
				elementXmlName = null;
			}

			// sanity check
			if (!contextStack.isEmpty())
				throw new XyzlException(String.format("TODO: error message"));

			// ...and I'm spent!
			return documentXyzlModel;
		}

		return null;
	}

	private XyzlModel coreDeserializeFromXmlText(Stack<XyzlModel> contextStack, String text, XmlName xmlName)
	{
		return null;
	}

	private XmlName coreGetConfiguredName(XyzlModel peek)
	{
		return null;
	}

	private XyzlModel coreDeserializeFromXml(Stack<XyzlModel> contextStack, XmlName previousElementXmlName, XmlName elementXmlName, Map<XmlName, String> attributes, XyzlValue<String> xyzlValue)
	{
		return null;
	}

	private boolean coreIsTextElement(Stack<XyzlModel> contextStack, XmlName xmlName) throws Exception
	{
		XyzlModel parentSolderConfiguration;
		Class<?> parentType;

		XyzlAttributeMapping xyzlAttributeMapping;
		XyzlChildElementMapping xyzlChildElementMapping;

		int attributeCount;

		if (contextStack == null)
			throw new ArgumentNullException("contextStack");

		if (xmlName == null)
			throw new ArgumentNullException("xmlName");

		// sanity check
		if (contextStack.isEmpty())
			return false;

		// interogate the parent (last pushed value)
		parentSolderConfiguration = contextStack.peek();
		parentType = parentSolderConfiguration.getClass();

		final BeanInfo beanInfo = Introspector.getBeanInfo(parentType);

		if (beanInfo == null)
			return false;

		final PropertyDescriptor[] parentPropertyDescriptors = beanInfo.getPropertyDescriptors();

		if (parentPropertyDescriptors == null)
			return false;

		// examine parent mapping tables for attributes and child elements
		if (parentPropertyDescriptors != null)
		{
			for (PropertyDescriptor parentPropertyDescriptor : parentPropertyDescriptors)
			{
				// get potential attribute mapping metadata
				final Method readMethod = parentPropertyDescriptor.getReadMethod();
				xyzlAttributeMapping = ReflectionUtils.getOneAnnotation(readMethod, XyzlAttributeMapping.class);

				// get the potential child mapping metadata
				xyzlChildElementMapping = ReflectionUtils.getOneAnnotation(readMethod, XyzlChildElementMapping.class);

				// count what we found; there can only be one
				attributeCount = 0;
				attributeCount += xyzlAttributeMapping == null ? 0 : 1;
				attributeCount += xyzlChildElementMapping == null ? 0 : 1;

				// sanity check
				if (attributeCount > 1)
					throw new XyzlException(String.format("TODO: error message"));

				// we only care about child elements
				if (xyzlChildElementMapping == null)
					continue;

				// is this mapped as a text element?
				if (xyzlChildElementMapping.childElementType() == XyzlChildType.VALUE &&
						equalsIgnoreCaseByValue(xyzlChildElementMapping.localName(), xmlName.getLocalName()) &&
						equalsIgnoreCaseByValue(xyzlChildElementMapping.namespaceUri(), xmlName.getNamespaceUri()))
					return true;
			}
		}

		// nope, we exhausted our search
		return false;
	}

	@Override
	public <TObject extends XyzlModel> TObject deserialize(Class<? extends TObject> knownObjectClass, Reader inputReader) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (inputReader == null)
			throw new ArgumentNullException("inputReader");

		try
		{
			return this.coreDeserialize(knownObjectClass, inputReader);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> TObject coreDeserialize(Class<? extends TObject> knownObjectClass, Reader inputReader) throws Exception
	{
		XMLStreamReader xmlStreamReader;
		TObject document;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (inputReader == null)
			throw new ArgumentNullException("inputReader");

		// DO NOT USE A USING BLOCK HERE (CALLER OWNS TEXTWRITER) !!!
		final XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
		xmlStreamReader = xmlInputFactory.createXMLStreamReader(inputReader);

		document = this.coreDeserialize(knownObjectClass, xmlStreamReader);
		return document;
	}

	@Override
	public <TObject extends XyzlModel> void registerKnownObject(Class<? extends TObject> knownObjectClass) throws XyzlException
	{
		XmlName xmlName;
		XyzlElementMapping xyzlElementMapping;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		xyzlElementMapping = ReflectionUtils.getOneAnnotation(knownObjectClass, XyzlElementMapping.class);

		if (xyzlElementMapping == null)
			throw new XyzlException(String.format("Missing XYZL element mapping annotation: '%s' on class '%s'.", XyzlElementMapping.class.getName(), knownObjectClass.getName()));

		xmlName = new XmlName();
		xmlName.setNamespaceUri(xyzlElementMapping.namespaceUri());
		xmlName.setLocalName(xyzlElementMapping.localName());

		this.registerKnownObject(knownObjectClass, xmlName);
	}

	@Override
	public <TObject extends XyzlModel> void registerKnownObject(Class<? extends TObject> knownObjectClass, XmlName xmlName) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (xmlName == null)
			throw new ArgumentNullException("xmlName");

		if (this.getKnownObjectRegistrations().containsKey(xmlName))
			throw new XyzlException(String.format("Duplicate XYZL element mapping annotation with name '%s#%s': on class '%s'.", xmlName.getNamespaceUri(), xmlName.getLocalName(), XyzlElementMapping.class.getName(), knownObjectClass.getName()));

		if (!XyzlModel.class.isAssignableFrom(knownObjectClass))
			throw new XyzlException(String.format("XYZL element mapping annotation with name '%s#%s': on class '%s' is not assignable to '%s'.", xmlName.getNamespaceUri(), xmlName.getLocalName(), XyzlElementMapping.class.getName(), knownObjectClass.getName(), XyzlModel.class.getName()));

		this.getKnownObjectRegistrations().put(xmlName, knownObjectClass);
	}

	@Override
	public <TObject extends XyzlValue<String>> void registerKnownValueObject(Class<? extends TObject> knownValueObjectClass) throws XyzlException
	{
		if (knownValueObjectClass == null)
			throw new ArgumentNullException("knownValueObjectClass");

		if (this.getKnownValueObjectRegistration() != null)
			throw new XyzlException(String.format("XYZL known value object '%' cannot be registered: class '%s' is already registered. Please unregister before registering a new class.", knownValueObjectClass.getName(), this.getKnownValueObjectRegistration().getName()));

		if (!XyzlValue.class.isAssignableFrom(knownValueObjectClass))
			throw new XyzlException(String.format("XYZL known value object '%' cannot be registered: not assignable to '%s'.", knownValueObjectClass.getName(), XyzlValue.class.getName()));

		this.setKnownValueObjectRegistration(knownValueObjectClass);
	}

	@Override
	public <TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, String fileName) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (fileName == null)
			throw new ArgumentNullException("fileName");

		try
		{
			this.coreSerialize(knownObjectClass, knownObject, fileName);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> void coreSerialize(Class<? extends TObject> knownObjectClass, TObject knownObject, String fileName) throws Exception
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (fileName == null)
			throw new ArgumentNullException("fileName");

		if (StringUtils.isNullOrWhiteSpace(fileName))
			throw new ArgumentOutOfRangeException("fileName");

		try (FileOutputStream fileOutputStream = new FileOutputStream(fileName))
		{
			this.coreSerialize(knownObjectClass, knownObject, fileOutputStream);
		}
	}

	@Override
	public <TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, OutputStream outputStream) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		try
		{
			this.coreSerialize(knownObjectClass, knownObject, outputStream);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> void coreSerialize(Class<? extends TObject> knownObjectClass, TObject knownObject, OutputStream outputStream) throws Exception
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (outputStream == null)
			throw new ArgumentNullException("outputStream");

		// DO NOT USE A USING BLOCK HERE (CALLER OWNS TEXTWRITER) !!!
		final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		final XMLStreamWriter xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputStream);

		this.coreSerialize(knownObjectClass, knownObject, xmlStreamWriter);
	}

	@Override
	public <TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, XMLStreamWriter xmlStreamWriter) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (xmlStreamWriter == null)
			throw new ArgumentNullException("xmlStreamWriter");

		try
		{
			this.coreSerialize(knownObjectClass, knownObject, xmlStreamWriter);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> void coreSerialize(Class<? extends TObject> knownObjectClass, TObject knownObject, XMLStreamWriter xmlStreamWriter) throws Exception
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (xmlStreamWriter == null)
			throw new ArgumentNullException("xmlStreamWriter");

		this.coreSerialize(xmlStreamWriter, knownObject, null);
	}

	private <TObject extends XyzlModel> void coreSerialize(XMLStreamWriter xmlStreamWriter, TObject knownObject, XmlName overrideXmlName)
	{
	}

	@Override
	public <TObject extends XyzlModel> void serialize(Class<? extends TObject> knownObjectClass, TObject knownObject, Writer outputWriter) throws XyzlException
	{
		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (outputWriter == null)
			throw new ArgumentNullException("outputWriter");

		try
		{
			this.coreSerialize(knownObjectClass, knownObject, outputWriter);
		}
		catch (Exception ex)
		{
			throw new XyzlException(String.format("The XYZL serializer failed (see inner exception)."), ex);
		}
	}

	private <TObject extends XyzlModel> void coreSerialize(Class<? extends TObject> knownObjectClass, TObject knownObject, Writer outputWriter) throws Exception
	{
		XMLStreamWriter xmlStreamWriter;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		if (knownObject == null)
			throw new ArgumentNullException("knownObject");

		if (outputWriter == null)
			throw new ArgumentNullException("outputWriter");

		// DO NOT USE A USING BLOCK HERE (CALLER OWNS TEXTWRITER) !!!
		final XMLOutputFactory xmlOutputFactory = XMLOutputFactory.newInstance();
		xmlStreamWriter = xmlOutputFactory.createXMLStreamWriter(outputWriter);

		this.coreSerialize(knownObjectClass, knownObject, xmlStreamWriter);
		xmlStreamWriter.flush();
	}

	@Override
	public <TObject extends XyzlModel> boolean unregisterKnownObject(Class<? extends TObject> knownObjectClass) throws XyzlException
	{
		boolean retval;
		XmlName xmlName;
		XyzlElementMapping xyzlElementMapping;

		if (knownObjectClass == null)
			throw new ArgumentNullException("knownObjectClass");

		xyzlElementMapping = ReflectionUtils.getOneAnnotation(knownObjectClass, XyzlElementMapping.class);

		if (xyzlElementMapping == null)
			throw new XyzlException(String.format("Missing XYZL element mapping annotation: '%s' on class '%s'.", XyzlElementMapping.class.getName(), knownObjectClass.getName()));

		xmlName = new XmlName();
		xmlName.setNamespaceUri(xyzlElementMapping.namespaceUri());
		xmlName.setLocalName(xyzlElementMapping.localName());

		if (retval = this.getKnownObjectRegistrations().containsKey(xmlName))
			this.getKnownObjectRegistrations().remove(xmlName);

		return retval;
	}

	@Override
	public <TObject extends XyzlValue<String>> boolean unregisterKnownValueObject(Class<? extends TObject> knownValueObjectClass) throws XyzlException
	{
		boolean retval;

		if (knownValueObjectClass == null)
			throw new ArgumentNullException("knownValueObjectClass");

		if (retval = this.getKnownValueObjectRegistration() != null)
			this.setKnownValueObjectRegistration(null);

		return retval;
	}

}
