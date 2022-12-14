/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

/**
 * Specifies the configuration child object mode (applicable only to those which are not well-known via properties with implicit or explicit mapping attributes).
 */
public enum XyzlChildMode
{

	/**
	 * This configuration object is not allowed to have any child objects. This is the default.
	 */
	NONE,

	/**
	 * This configuration object can have ONE non-well-known child object. Use the Content property to access the possibly null value..
	 */
	CONTENT,

	/**
	 * This configuration object can have MANY non-well-known child objects. Use the Items property to access the possibly empty list of values.
	 */
	ITEMS,

	/**
	 *
	 */
	VALUE
}
