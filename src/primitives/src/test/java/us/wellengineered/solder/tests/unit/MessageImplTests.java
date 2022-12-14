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

public class MessageImplTests
{
	public MessageImplTests()
	{
	}

	@Test
	public void shouldCreateTest() throws Exception
	{
		Message message;
		final String CATEGORY = "myCategory";
		final String DESCRIPTION = "myDescription";
		final Severity SEVERITY = Severity.INFORMATION;

		message = new MessageImpl(CATEGORY, DESCRIPTION, SEVERITY);

		Assertions.assertEquals(CATEGORY, message.getCategory());
		Assertions.assertEquals(DESCRIPTION, message.getDescription());
		Assertions.assertEquals(SEVERITY, message.getSeverity());
	}

	@Test
	public void shouldFailOnNullCategoryCreateTest()
	{
		Assertions.assertThrows(ArgumentNullException.class, () -> { __shouldFailOnNullCategoryCreateTest(); });
	}

	private void __shouldFailOnNullCategoryCreateTest()
	{
		Message message;
		final String CATEGORY = null;
		final String DESCRIPTION = "myDescription";
		final Severity SEVERITY = Severity.INFORMATION;

		message = new MessageImpl(CATEGORY, DESCRIPTION, SEVERITY);
	}

	@Test
	public void shouldFailOnNullDescriptionCreateTest()
	{
		Assertions.assertThrows(ArgumentNullException.class, () -> { __shouldFailOnNullDescriptionCreateTest(); });
	}

	private void __shouldFailOnNullDescriptionCreateTest()
	{
		Message message;
		final String CATEGORY = "myCategory";
		final String DESCRIPTION = null;
		final Severity SEVERITY = Severity.INFORMATION;

		message = new MessageImpl(CATEGORY, DESCRIPTION, SEVERITY);
	}
}
