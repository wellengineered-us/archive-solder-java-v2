/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.primitives.*;

import java.util.Map;

public class DisposableListTests
{
	public DisposableListTests()
	{
	}

	@Test
	public void shouldCreateTest() throws Exception
	{
		DisposableList disposableList;
		Disposable mockDisposable;

		mockDisposable = Mockito.mock(Disposable.class);

		Mockito.doNothing().when(mockDisposable).dispose();

		disposableList = new DisposableList();

		disposableList.add(mockDisposable);

		Assertions.assertNotNull(disposableList);
		Assertions.assertEquals(1, disposableList.size());
		Assertions.assertFalse(disposableList.isDisposed());
		disposableList.dispose();
		Assertions.assertTrue(disposableList.isDisposed());

		Mockito.verify(mockDisposable, Mockito.times(1)).dispose();
		Mockito.verifyNoMoreInteractions(mockDisposable);
	}
}
