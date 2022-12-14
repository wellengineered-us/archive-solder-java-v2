/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target({ ElementType.TYPE })
public @interface XyzlElementMapping
{
	boolean isKnown();

	XyzlChildMode childElementMode();

	String localName();

	String namespaceUri();
}
