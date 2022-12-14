/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import us.wellengineered.solder.configuration.SolderConfiguration;

/**
 * Represents a XYZL object and it's "name".
 */
public interface XyzlModel extends SolderConfiguration
{
	XmlName getXmlName();

	void setXmlName(XmlName xmlName);
}
