/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.xyzl;

public abstract class AbstractXyzlValue<TValue> extends AbstractXyzlModel implements XyzlValue<TValue>
{
	protected AbstractXyzlValue()
	{
	}

	private TValue value;

	@Override
	public TValue getValue(Class<? extends TValue> valueClass)
	{
		return this.value;
	}

	@Override
	public void setValue(Class<? extends TValue> valueClass, TValue value)
	{
		this.value = value;
	}
}
