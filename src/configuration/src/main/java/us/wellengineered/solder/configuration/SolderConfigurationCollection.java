/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

import us.wellengineered.solder.primitives.Validatable;

import java.util.Collection;


/**
 * Represents an configuration object collection.
 *
 * @param <TItemSolderConfiguration> The contained configuration object class.
 */
public interface SolderConfigurationCollection<TItemSolderConfiguration extends SolderConfiguration> extends Validatable, Collection<TItemSolderConfiguration>
{
	/**
	 * Gets the site configuration object or null if this instance is unattached.
	 *
	 * @return a site configuration object or null if this instance is unattached.
	 */
	SolderConfiguration getSite();
}
