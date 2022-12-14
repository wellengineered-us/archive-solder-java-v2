/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.replacements;

public class StaticValueTokenReplacementImpl extends AbstractTokenReplacement
{
	public StaticValueTokenReplacementImpl(Object value)
	{
		this.value = value;
	}

	private final Object value;

	public Object getValue()
	{
		return this.value;
	}

	@Override
	protected Object coreEvaluate(String[] parameters) throws Exception
	{
		return this.getValue();
	}

	@Override
	protected Object coreEvaluate(String token, Object[] parameters) throws Exception
	{
		return this.getValue();
	}

}
