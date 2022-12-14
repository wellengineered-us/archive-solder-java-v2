/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tests.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import us.wellengineered.solder.primitives.Creatable;
import us.wellengineered.solder.primitives.Disposable;
import us.wellengineered.solder.primitives.DisposableList;
import us.wellengineered.solder.primitives.LifecycleUtils;

import java.io.Closeable;

public class LifecycleUtilsTests
{
	public LifecycleUtilsTests()
	{
	}

	@Test
	public void shouldMaybeDisposeDisposableTest() throws Exception
	{
		Disposable mockDisposable;

		mockDisposable = Mockito.mock(Disposable.class);

		Mockito.when(mockDisposable.isDisposed()).thenReturn(false);
		Mockito.doNothing().when(mockDisposable).dispose();

		boolean result = LifecycleUtils.maybeDispose(mockDisposable);
		Assertions.assertTrue(result);

		Mockito.verify(mockDisposable, Mockito.times(1)).isDisposed();
		Mockito.verify(mockDisposable, Mockito.times(1)).dispose();
		Mockito.verifyNoMoreInteractions(mockDisposable);
	}

	@Test
	public void shouldMaybeCloseCloseableTest() throws Exception
	{
		Closeable mockCloseable;

		mockCloseable = Mockito.mock(Closeable.class);

		Mockito.doNothing().when(mockCloseable).close();

		boolean result = LifecycleUtils.maybeClose(mockCloseable);
		Assertions.assertTrue(result);

		Mockito.verify(mockCloseable, Mockito.times(1)).close();
		Mockito.verifyNoMoreInteractions(mockCloseable);
	}

	@Test
	public void shouldMaybeCloseAutoCloseableTest() throws Exception
	{
		AutoCloseable mockAutoCloseable;

		mockAutoCloseable = Mockito.mock(AutoCloseable.class);

		Mockito.doNothing().when(mockAutoCloseable).close();

		boolean result = LifecycleUtils.maybeClose(mockAutoCloseable);
		Assertions.assertTrue(result);

		Mockito.verify(mockAutoCloseable, Mockito.times(1)).close();
		Mockito.verifyNoMoreInteractions(mockAutoCloseable);
	}

	@Test
	public void shouldMaybeDisposeOtherTest() throws Exception
	{
		Object mockDisposable;

		mockDisposable = Mockito.mock(Object.class);

		boolean result = LifecycleUtils.maybeDispose(mockDisposable);
		Assertions.assertFalse(result);

		Mockito.verifyNoMoreInteractions(mockDisposable);
	}

	@Test
	public void shouldMaybeCloseOtherTest() throws Exception
	{
		Object mockCloseable;

		mockCloseable = Mockito.mock(Object.class);

		boolean result = LifecycleUtils.maybeClose(mockCloseable);
		Assertions.assertFalse(result);

		Mockito.verifyNoMoreInteractions(mockCloseable);
	}

	@Test
	public void shouldMaybeCreateCreatableTest() throws Exception
	{
		Creatable mockCreatable;

		mockCreatable = Mockito.mock(Creatable.class);

		Mockito.when(mockCreatable.isCreated()).thenReturn(false);
		Mockito.doNothing().when(mockCreatable).create();

		boolean result = LifecycleUtils.maybeCreate(mockCreatable);
		Assertions.assertTrue(result);

		Mockito.verify(mockCreatable, Mockito.times(1)).isCreated();
		Mockito.verify(mockCreatable, Mockito.times(1)).create();
		Mockito.verifyNoMoreInteractions(mockCreatable);
	}

	@Test
	public void shouldMaybeCreateOtherTest() throws Exception
	{
		Object mockCreatable;

		mockCreatable = Mockito.mock(Object.class);

		boolean result = LifecycleUtils.maybeCreate(mockCreatable);
		Assertions.assertFalse(result);

		Mockito.verifyNoMoreInteractions(mockCreatable);
	}
}
