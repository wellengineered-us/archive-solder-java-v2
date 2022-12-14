/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.configuration;

public interface Specifiable<TSpecification extends SolderSpecification>
{
	TSpecification getSpecification();

	void setSpecification(TSpecification specification);

	Class<? extends TSpecification> getSpecificationClass();
}
