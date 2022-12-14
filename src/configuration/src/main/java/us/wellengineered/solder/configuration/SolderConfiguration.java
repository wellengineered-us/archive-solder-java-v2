/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

import us.wellengineered.solder.primitives.Validatable;

/**
 * Represents an configuration object and it's "schema".
 */
public interface SolderConfiguration extends Validatable
{
	/**
	 * Gets an iterable of allowed child configuration object classes.
	 *
	 * @return
	 */
	Iterable<Class<? extends SolderConfiguration>> getAllowedChildClasses();

	/**
	 * Gets the optional single configuration content object.
	 *
	 * @return
	 */
	SolderConfiguration getContent();

	/**
	 * Sets the optional single configuration content object.
	 *
	 * @param content
	 */
	void setContent(SolderConfiguration content);

	/**
	 * Gets a list of configuration object items.
	 *
	 * @return
	 */
	SolderConfigurationCollectionImpl<SolderConfiguration> getItems();

	/**
	 * Gets the parent configuration object or null if this is the configuration root.
	 *
	 * @return
	 */
	SolderConfiguration getParent();

	/**
	 * Sets the parent configuration object or null if this is the configuration root.
	 *
	 * @param parent
	 */
	void setParent(SolderConfiguration parent);

	/**
	 * Gets the surrounding configuration object or null if this is not surrounded (in a collection).
	 *
	 * @return
	 */
	SolderConfigurationCollectionImpl<?> getSurround();

	/**
	 * Sets the surrounding configuration object or null if this is not surrounded (in a collection).
	 *
	 * @param surround
	 */
	void setSurround(SolderConfigurationCollectionImpl<?> surround);
}
