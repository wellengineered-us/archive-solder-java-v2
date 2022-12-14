/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.utilities.appsettings;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.StringUtils;

public final class AppSettingsFacadeImpl implements AppSettingsFacade
{
	public AppSettingsFacadeImpl()
	{
	}

	@Override
	public <TValue> TValue getAppSetting(Class<? extends TValue> valueClass, String key) throws AppSettingsException
	{
		String svalue;
		TValue ovalue;

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		if (key == null)
			throw new ArgumentNullException("key");

		svalue = System.getProperty(key);

		if (svalue == null)
			throw new AppSettingsException(String.format("Key '%s' was not found in property container.", key));

		final MethodParameterModifier.Out<TValue> outValue = new MethodParameterModifier.Out<TValue>(valueClass);

		if (!StringUtils.tryParse(valueClass, svalue, outValue))
			throw new AppSettingsException(String.format("Property key '%s' value '%s' is not a valid '%s'.", key, svalue, valueClass.getName()));

		ovalue = outValue.getValue();

		return ovalue;
	}

	@Override
	public boolean hasAppSetting(String key)
	{
		String value;

		if (key == null)
			throw new ArgumentNullException("key");

		value = System.getProperty(key);

		return value != null;
	}
}
