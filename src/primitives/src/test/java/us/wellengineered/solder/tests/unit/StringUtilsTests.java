/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.Message;
import us.wellengineered.solder.primitives.MessageImpl;
import us.wellengineered.solder.primitives.Severity;
import us.wellengineered.solder.primitives.StringUtils;

public class StringUtilsTests
{
	public StringUtilsTests()
	{
	}

	@Test
	public void shouldGetEmptyStringTest() throws Exception
	{
		Assertions.assertEquals("", StringUtils.EMPTY_STRING);
	}

	@Test
	public void shouldCheckStringTest() throws Exception
	{
		Assertions.assertTrue(StringUtils.isNullOrEmpty(null));
		Assertions.assertTrue(StringUtils.isNullOrEmpty(""));
		Assertions.assertFalse(StringUtils.isNullOrEmpty("test"));

		Assertions.assertTrue(StringUtils.isNullOrEmptyString(null));
		Assertions.assertTrue(StringUtils.isNullOrEmptyString(""));
		Assertions.assertFalse(StringUtils.isNullOrEmptyString("test"));

		Assertions.assertFalse(StringUtils.isWhiteSpace(null));
		Assertions.assertTrue(StringUtils.isWhiteSpace(""));
		Assertions.assertTrue(StringUtils.isWhiteSpace("\t")); // failed test found bug in code 2022-07-17#dpb
		Assertions.assertFalse(StringUtils.isWhiteSpace("test"));

		Assertions.assertTrue(StringUtils.isNullOrWhiteSpace(null));
		Assertions.assertTrue(StringUtils.isNullOrWhiteSpace(""));
		Assertions.assertTrue(StringUtils.isNullOrWhiteSpace("\t"));
		Assertions.assertFalse(StringUtils.isNullOrWhiteSpace("test"));
	}
}
