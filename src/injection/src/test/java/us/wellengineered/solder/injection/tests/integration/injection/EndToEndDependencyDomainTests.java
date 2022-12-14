/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection.tests.integration.injection;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import us.wellengineered.solder.injection.*;
import us.wellengineered.solder.injection.resolutions.AbstractDependencyResolution;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.Disposable;

import java.util.Random;
import java.util.UUID;

public class EndToEndDependencyDomainTests
{
	public EndToEndDependencyDomainTests()
	{
	}

	@Test
	public void shouldCreateTest() throws Exception
	{
		var dependencyDomain = DependencyDomain.getDefault();
		{
			var dependencyManager = dependencyDomain.getDependencyManager();
			var resourceManager = dependencyDomain.getResourceManager();

			Assertions.assertNotNull(dependencyDomain);
			Assertions.assertTrue(dependencyDomain.isCreated());
			Assertions.assertFalse(dependencyDomain.isDisposed());

			Assertions.assertNotNull(dependencyManager);
			Assertions.assertTrue(dependencyManager.isCreated());
			Assertions.assertFalse(dependencyManager.isDisposed());

			Assertions.assertNotNull(resourceManager);
			Assertions.assertTrue(resourceManager.isCreated());
			Assertions.assertFalse(resourceManager.isDisposed());

			dependencyManager.clearAllResolutions();
			Assertions.assertFalse(dependencyManager.hasResolution(Integer.class, "", true));

			DependencyResolution dependencyResolution;

			dependencyResolution = new AbstractDependencyResolution<Integer>(DependencyLifetime.TRANSIENT)
			{
				@Override
				protected void coreCreate(boolean creating) throws Exception
				{

				}

				@Override
				protected void coreDispose(boolean disposing) throws Exception
				{

				}

				@Override
				protected Integer coreResolve(DependencyManager dependencyManager, Class<? extends Integer> resolutionClass, String selectorKey) throws Exception
				{
					return new Random().nextInt();
				}
			};
			dependencyResolution.create();

			dependencyManager.addResolution(Integer.class, "", true, dependencyResolution);

			Assertions.assertTrue(dependencyManager.hasResolution(Integer.class, "", true));
			final Integer value = dependencyManager.resolveDependency(Integer.class, "", true);
			Assertions.assertNotNull(value);

			//System.out.println(dependencyManager.resolveDependency(Integer.class, "jo", true));

			dependencyManager.removeResolution(Integer.class, "", true);
			Assertions.assertFalse(dependencyManager.hasResolution(Integer.class, "", true));


			resourceManager.check();
			UUID slotId = resourceManager.enter();

			resourceManager.print(slotId, "This is a test.");
			resourceManager.reset();
			var watch = resourceManager.watching(slotId, new Disposable()
			{
				@Override
				public boolean isDisposed()
				{
					return false;
				}

				@Override
				public void dispose() throws Exception
				{

				}

				@Override
				public void close() throws Exception
				{

				}
			});

			try(var dd = resourceManager.using(slotId, new Disposable()
			{
				@Override
				public boolean isDisposed()
				{
					return false;
				}

				@Override
				public void dispose() throws Exception
				{

				}

				@Override
				public void close() throws Exception
				{

				}
			}, null))
			{
				dd.getTarget();
			}

			resourceManager.dispose(slotId, watch);
			resourceManager.leave(slotId);
			resourceManager.check();

			dependencyDomain.dispose();

			Assertions.assertNotNull(dependencyDomain);
			Assertions.assertTrue(dependencyDomain.isCreated());
			Assertions.assertTrue(dependencyDomain.isDisposed());

			Assertions.assertNotNull(dependencyManager);
			Assertions.assertTrue(dependencyManager.isCreated());
			Assertions.assertTrue(dependencyManager.isDisposed());

			Assertions.assertNotNull(resourceManager);
			Assertions.assertTrue(resourceManager.isCreated());
			Assertions.assertTrue(resourceManager.isDisposed());
		}
	}
}
