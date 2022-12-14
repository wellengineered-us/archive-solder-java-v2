/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.component;

import us.wellengineered.solder.configuration.SolderConfiguration;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;

public abstract class AbstractSolderComponent1<TSolderConfiguration extends SolderConfiguration> extends AbstractSolderComponent0 implements SolderComponent1<TSolderConfiguration>
{
	protected AbstractSolderComponent1()
	{
	}

	private void assertValidConfiguration()
	{
		if (this.getConfiguration() == null)
			throw new InvalidOperationException(String.format("Component configuration missing."));
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		if (creating)
		{
			super.coreCreate(creating);

			this.assertValidConfiguration();
		}
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			this.setConfiguration(null);

			super.coreDispose(disposing);
		}
	}

	private TSolderConfiguration configuration;

	@Override
	public TSolderConfiguration getConfiguration()
	{
		return this.configuration;
	}

	@Override
	public void setConfiguration(TSolderConfiguration configuration)
	{
		this.configuration = configuration;
	}
}
