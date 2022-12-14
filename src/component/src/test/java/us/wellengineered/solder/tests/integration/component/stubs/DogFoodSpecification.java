/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.integration.component.stubs;

import us.wellengineered.solder.tests.integration.component.stubs.cfg.AbstractEndToEndSpecification;
import us.wellengineered.solder.primitives.Message;

public class DogFoodSpecification extends AbstractEndToEndSpecification
{
	public DogFoodSpecification()
	{
	}

	public static final String PROP_A = "propA";
	public static final String PROP_B = "propB";
	public static final String PROP_C = "propC";
	public static final String PROP_D = "propD";

	private String propA;
	private Integer propB;
	private Boolean propC;
	private Double propD;

	public String getPropA()
	{
		return this.propA;
	}

	public void setPropA(String propA)
	{
		this.propA = propA;
	}

	public Integer getPropB()
	{
		return this.propB;
	}

	public void setPropB(Integer propB)
	{
		this.propB = propB;
	}

	public Boolean getPropC()
	{
		return this.propC;
	}

	public void setPropC(Boolean propC)
	{
		this.propC = propC;
	}

	public Double getPropD()
	{
		return this.propD;
	}

	public void setPropD(Double propD)
	{
		this.propD = propD;
	}

	@Override
	protected Iterable<Message> coreValidate(Object context)
	{
		return null;
	}
}
