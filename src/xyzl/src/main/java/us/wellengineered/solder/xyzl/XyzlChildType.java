/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

/**
 * Specifies the configuration child object type (applicable only to those which are well-known via properties with implicit or explicit mapping attributes).
 */
public enum XyzlChildType
{

	/**
	 * The child configuration object is will be rendered as a value object using it's FQN. This is the default.
	 * Think: "text mode".
	 */
	VALUE,

	/**
	 * The child configuration object will be rendered as a non-value object using it's FQN.
	 * Think: "property mode".
	 */
	UNQUALIFIED,

	/**
	 * The child configuration object will be rendered as a non-value object using it's UPN dot prefixed with the UPN of it's parent configuration object.
	 * Think: "XAML mode".
	 */
	PARENT_QUALIFIED
}
