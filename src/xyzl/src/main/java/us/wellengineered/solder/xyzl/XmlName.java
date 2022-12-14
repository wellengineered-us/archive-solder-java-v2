/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.polyfills.language.Tuple;

import java.util.Objects;

public final class XmlName
{
	public XmlName()
	{
	}

	public XmlName(String namespaceUri, String localName)
	{
		this.namespaceUri = namespaceUri;
		this.localName = localName;
	}

	private String localName;
	private String namespaceUri;

	public String getLocalName()
	{
		return this.localName;
	}

	public void setLocalName(String localName)
	{
		this.localName = localName;
	}

	public String getNamespaceUri()
	{
		return this.namespaceUri;
	}

	public void setNamespaceUri(String namespaceUri)
	{
		this.namespaceUri = namespaceUri;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;

		if (obj == null || getClass() != obj.getClass())
			return false;

		XmlName xmlName = (XmlName) obj;

		return  Objects.equals(this.getNamespaceUri(), xmlName.getNamespaceUri()) &&
				Objects.equals(this.getLocalName(), xmlName.getLocalName());
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(this.getNamespaceUri(), this.getLocalName());
	}
}
