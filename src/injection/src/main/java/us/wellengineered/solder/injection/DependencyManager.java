/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.primitives.Lifecycle;

/**
 * Provides dependency registration and resolution services.
 */
public interface DependencyManager extends Lifecycle
{
	<TResolution> void addResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes, DependencyResolution<? extends TResolution> dependencyResolution) throws DependencyException;

	boolean clearAllResolutions() throws DependencyException;

	<TResolution> boolean clearResolutions(Class<? extends TResolution> resolutionClass, boolean includeAssignableTypes) throws DependencyException;

	<TResolution> boolean hasResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws DependencyException;

	<TResolution> void removeResolution(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws DependencyException;

	<TResolution> TResolution resolveDependency(Class<? extends TResolution> resolutionClass, String selectorKey, boolean includeAssignableTypes) throws DependencyException;
}
