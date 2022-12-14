/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.configuration.AbstractSolderConfiguration;

public abstract class AbstractXyzlModel extends AbstractSolderConfiguration implements XyzlModel
{
	protected AbstractXyzlModel()
	{
	}

	private XmlName xmlName;

	@Override
	public XmlName getXmlName()
	{
		return this.xmlName;
	}

	@Override
	public void setXmlName(XmlName xmlName)
	{
		this.xmlName = xmlName;
	}
}
