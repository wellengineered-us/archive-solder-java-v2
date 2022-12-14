/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs;

import us.wellengineered.solder.tests.integration.component.stubs.cfg.AbstractEndToEndConfiguration;
import us.wellengineered.solder.primitives.Message;

public class DogFoodConfiguration extends AbstractEndToEndConfiguration
{
	public DogFoodConfiguration()
	{
	}

	private String prop1;
	private Integer prop2;
	private Boolean prop3;
	private Double prop4;

	public String getProp1()
	{
		return this.prop1;
	}

	public void setProp1(String prop1)
	{
		this.prop1 = prop1;
	}

	public Integer getProp2()
	{
		return this.prop2;
	}

	public void setProp2(Integer prop2)
	{
		this.prop2 = prop2;
	}

	public Boolean getProp3()
	{
		return this.prop3;
	}

	public void setProp3(Boolean prop3)
	{
		this.prop3 = prop3;
	}

	public Double getProp4()
	{
		return this.prop4;
	}

	public void setProp4(Double prop4)
	{
		this.prop4 = prop4;
	}

	@Override
	protected Iterable<Message> coreValidate(Object context)
	{
		return null;
	}
}
