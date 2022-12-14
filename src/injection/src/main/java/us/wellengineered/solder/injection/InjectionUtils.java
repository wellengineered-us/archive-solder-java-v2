/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.injection;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;
import us.wellengineered.solder.polyfills.language.Lazy;
import us.wellengineered.solder.primitives.ReflectionUtils;
import us.wellengineered.solder.primitives.SolderException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;

public final class InjectionUtils
{
	public static <TTarget> TTarget getObjectAssignableToTargetClass(Class<? extends TTarget> instantiateClass, DependencyManager dependencyManager, boolean autoWire, String selectorKey, boolean throwOnError) throws InjectionException
	{
		TTarget obj;

		if (instantiateClass == null)
			throw new ArgumentNullException("instantiateClass");

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (autoWire)
			obj = resolveInstanceAssignableToClass(instantiateClass, dependencyManager, selectorKey, throwOnError);
		else
		{
			try
			{
				obj = ReflectionUtils.createInstanceAssignableToClass(instantiateClass, null, throwOnError);
			}
			catch (SolderException ex)
			{
				throw new InjectionException(String.format("Failed to create object of type: '%s', auto-wire: %s.", instantiateClass.getName(), autoWire), ex);
			}
		}

		if (obj == null)
			throw new InjectionException(String.format("Failed to create object of type: '%s', auto-wire: %s.", instantiateClass.getName(), autoWire));

		return obj;
	}

	public static <TTarget> TTarget resolveInstanceAssignableToClass(Class<? extends TTarget> instantiateClass, DependencyManager dependencyManager, String selectorKey, boolean throwOnError) throws InjectionException
	{
		Object obj;

		if (instantiateClass == null)
			throw new ArgumentNullException("instantiateClass");

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		obj = autoWireResolve(instantiateClass, dependencyManager, selectorKey);

		return (TTarget)obj;
	}

	private static <TTarget> TTarget autoWireResolve(Class<? extends TTarget> instantiateClass, DependencyManager dependencyManager, String selectorKey) throws InjectionException
	{
		Constructor[] constructorInfos;
		Parameter[] parameterInfos;

		DependencyInjectionTarget dependencyInjectionAttribute;
		Lazy<TTarget> lazyConstructorInvokation = null;

		if (instantiateClass == null)
			throw new ArgumentNullException("instantiateClass");

		if (dependencyManager == null)
			throw new ArgumentNullException("dependencyManager");

		if (selectorKey == null)
			throw new ArgumentNullException("selectorKey");

		// get public, instance .ctors for instantiate type
		constructorInfos = instantiateClass.getConstructors();

		if (constructorInfos != null)
		{
			for (int constructorIndex = 0; constructorIndex < constructorInfos.length; constructorIndex++)
			{
				ArrayList<Lazy<Object>> lazyConstructorArguments;

				final Constructor constructorInfo = constructorInfos[constructorIndex];

				if (constructorInfo == null)
					continue;

				// BindingFlags.DeclaredOnly | BindingFlags.Public | BindingFlags.Static
				final int mods = constructorInfo.getModifiers();
				if (!Modifier.isPublic(mods) || !Modifier.isStatic(mods))
					continue;

				// on constructor
				dependencyInjectionAttribute = ReflectionUtils.getOneAnnotation(constructorInfo, DependencyInjectionTarget.class);

				if (dependencyInjectionAttribute == null)
					continue;

				if (dependencyInjectionAttribute.selectorKey() != selectorKey)
					continue;

				if (lazyConstructorInvokation != null)
					throw new DependencyException(String.format("More than one constructor for instantiate class '%s' specified the '%s' with selector key '%s'.", instantiateClass.getName(), DependencyInjectionTarget.class.getSimpleName(), selectorKey));

				parameterInfos = constructorInfo.getParameters();
				lazyConstructorArguments = new ArrayList<>(parameterInfos.length);

				for (int parameterIndex = 0; parameterIndex < parameterInfos.length; parameterIndex++)
				{
					Parameter parameterInfo;
					Class<?> parameterClass;
					Lazy<Object> lazyConstructorArgument;
					DependencyInjectionTarget parameterDependencyInjectionTarget;

					parameterInfo = parameterInfos[parameterIndex];
					parameterClass = parameterInfo.getType();

					// on parameter
					parameterDependencyInjectionTarget = ReflectionUtils.getOneAnnotation(parameterInfo, DependencyInjectionTarget.class);

					if (parameterDependencyInjectionTarget == null)
						throw new DependencyException(String.format("A constructor for instantiate class '%s' specifying the '%s' " +
										"with selector key '%s' had at least one parameter missing the '%s': index='%s';name='%s';type='%s'.",
								instantiateClass.getName(), DependencyInjectionTarget.class.getSimpleName(), selectorKey,
								DependencyInjectionTarget.class.getSimpleName(), parameterIndex, parameterInfo.getName(), parameterClass.getName()));

					lazyConstructorArgument = new Lazy<>(() ->
							{
								// prevent modified closure bug
								DependencyManager _dependencyManager = dependencyManager;
								Class<?> _parameterClass = parameterClass;
								DependencyInjectionTarget _parameterDependencyInjectionTarget = parameterDependencyInjectionTarget;

								try
								{
									return _dependencyManager.resolveDependency(_parameterClass, _parameterDependencyInjectionTarget.selectorKey(), true);
								}
								catch (DependencyException e)
								{
									throw new FailFastException(e);
								}
							});


					lazyConstructorArguments.add(parameterIndex, lazyConstructorArgument);
				}

				lazyConstructorInvokation = new Lazy<>(() ->
						{
							// prevent modified closure bug
							Class<?> _instantiateClass = instantiateClass;
							ArrayList<Lazy<Object>> _lazyConstructorArguments = lazyConstructorArguments;

							return (TTarget) ReflectionUtils.createInstanceAssignableToClass(_instantiateClass,
									_lazyConstructorArguments.stream().map(l -> l.getSafeValue()).toArray(),
									true);
						});
			}
		}

		if (lazyConstructorInvokation == null)
			throw new DependencyException(String.format("Cannot find a dependency injection constructor for instantiate class '%s' with selector key '%s'.", instantiateClass.getName(), selectorKey));

		return lazyConstructorInvokation.getSafeValue(); // .get[Safe]Value() will load a cascading chain of Lazy's...
	}
}
