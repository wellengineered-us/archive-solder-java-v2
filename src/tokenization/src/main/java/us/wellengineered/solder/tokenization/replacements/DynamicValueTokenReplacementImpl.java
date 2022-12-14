/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.tokenization.replacements;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;
import us.wellengineered.solder.polyfills.language.Func;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.ReflectionUtils;
import us.wellengineered.solder.primitives.StringUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class DynamicValueTokenReplacementImpl extends AbstractTokenReplacement
{
	public DynamicValueTokenReplacementImpl(Func.Arity2<String[], Object> method)
	{
		if (method == null)
			throw new ArgumentNullException("method");

		this.method = method;
	}

	private final Func.Arity2<String[], Object> method;

	public Func.Arity2<String[], Object> getMethod()
	{
		return this.method;
	}

	@Override
	protected Object coreEvaluate(String[] parameters) throws Exception
	{
		return this.getMethod().invoke(parameters);
	}

	@Override
	protected Object coreEvaluate(String token, Object[] parameters) throws Exception
	{
		String[] parameterz;

		if (parameters != null)
		{
			parameterz = new String[parameters.length];

			for (int i = 0; i < parameterz.length; i++)
				parameterz[i] = parameters[i] instanceof String ? (String)parameters[i] : StringUtils.toStringEx(parameters[i]);
		}
		else
		{
			parameterz = new String[] { };
		}

		return this.getMethod().invoke(parameterz);
	}

	public static Object StaticMethodResolver(String[] parameters) throws Exception
	{
		Class<?> targetType, parameterType = null;
		List<Class<?>> parameterTypes;
		Method methodInfo;
		Parameter[] parameterInfos;
		Object returnValue;
		MethodParameterModifier.Out<Object> outArgumentValue;
		List<Object> argumentValues;

		if (parameters == null)
			throw new ArgumentNullException("parameters");

		if (parameters.length < 2)
			throw new InvalidOperationException(String.format("StaticMethodResolver requires at least two parameters but was invoked with '%s'. USAGE: StaticMethodResolver(`assembly-qualified-type-name`, `static-method-name`, [`type0`, `arg0`...])", parameters.length));

		if ((parameters.length % 2) != 0)
			throw new InvalidOperationException(String.format("StaticMethodResolver was invoked with an odd number '%s' of parameters.", parameters.length));

		parameterTypes = new ArrayList<>();
		argumentValues = new ArrayList<>();

		// assembly-qualified-type-name
		targetType = ReflectionUtils.loadClassByName(parameters[0]);

		if (targetType == null)
			throw new InvalidOperationException(String.format("StaticMethodResolver parameter at index '%s' with value '%s' was not a valid, loadable JVM type.", 0, parameters[0]));

		// skip first two parameters
		for (int i = 2; i < parameters.length; i++)
		{
			if ((i % 2) == 0) // parameter type
			{
				// assembly-qualified-type-name
				parameterType = ReflectionUtils.loadClassByName(parameters[i]);

				if (parameterType == null)
					throw new InvalidOperationException(String.format("StaticMethodResolver parameter at index '%s' with value '%s' was not a valid, loadable JVM type (for method call parameter).", i, parameters[i]));

				parameterTypes.add(parameterType);
			}
			else // argument value
			{
				outArgumentValue = new MethodParameterModifier.Out<>(Object.class);
				if (!StringUtils.tryParse(parameterType, parameters[i], outArgumentValue))
					throw new InvalidOperationException(String.format("StaticMethodResolver parameter at index '%s' with value '%s' was not a valid '%s'.", i, parameters[i], parameterType.getName()));

				final Object argumentValue = outArgumentValue.getValue();
				argumentValues.add(argumentValue);
			}
		}

		// method-name
		final Class<?>[] parameterz = new Class<?>[0];
		parameterTypes.toArray(parameterz);
		methodInfo = targetType.getMethod(parameters[1], parameterz);

		if (methodInfo == null)
			throw new InvalidOperationException(String.format("StaticMethodResolver parameter at index '%s' with value '%s' was not a valid, executable method name of type '%s'.", 1, parameters[1], targetType.getName()));

		// BindingFlags.DeclaredOnly | BindingFlags.Public | BindingFlags.Static
		final int mods = methodInfo.getModifiers();
		if (methodInfo.getDeclaringClass() != targetType || !Modifier.isPublic(mods) || !Modifier.isStatic(mods))
			throw new InvalidOperationException(String.format("StaticMethodResolver parameter at index '%s' with value '%s' was not a valid, executable method name of type '%s'.", 1, parameters[1], targetType.getName()));

		parameterInfos = methodInfo.getParameters();

		if (parameterInfos.length != ((parameters.length - 2) / 2))
			throw new InvalidOperationException(String.format("StaticMethodResolver was invoked with '%s' but the static method '%s' of type '%s' requires '%s' parameters.", ((parameters.length - 2) / 2), methodInfo.getName(), targetType.getName(), parameterInfos.length));

		returnValue = methodInfo.invoke(null, argumentValues.toArray());

		return returnValue;
	}
}
