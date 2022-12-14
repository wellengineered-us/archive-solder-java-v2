/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentOutOfRangeException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;
import us.wellengineered.solder.polyfills.language.CustomAnnotationProvider;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;

public final class ReflectionUtils
{
	public static <TValue> TValue changeClass(Class<? extends TValue> valueClass, Object value)
	{
		return (TValue) value;
	}

	public static <TObject> TObject createInstanceAssignableToClass(Class<? extends TObject> instantiateClass)
	{
		try
		{
			return createInstanceAssignableToClass(instantiateClass, null, false);
		}
		catch (SolderException ex)
		{
			throw new FailFastException("Should never reach here.", ex);
		}
	}

	public static <TObject> TObject createInstanceAssignableToClass(Class<? extends TObject> instantiateClass, Object[] ctorArgs, boolean throwOnError) throws SolderException
	{
		if (instantiateClass == null)
			throw new ArgumentNullException("instantiateClass");

		try
		{
			if (ctorArgs == null || ctorArgs.length == 0)
				return instantiateClass.getConstructor().newInstance();
			else
				return instantiateClass.getConstructor().newInstance(ctorArgs);
		}
		catch (NoSuchMethodException ex)
		{
		}
		catch (IllegalAccessException ex)
		{
		}
		catch (InvocationTargetException ex)
		{
		}
		catch (InstantiationException ex)
		{
		}

		if (throwOnError)
			throw new SolderException(instantiateClass.getName());

		return defaultValue(instantiateClass);
	}

	public static <TValue> TValue defaultValue(Class<? extends TValue> valueClass)
	{
		TValue value;

		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		value = valueClass.isPrimitive() ? createInstanceAssignableToClass(valueClass) : null;

		return value;
	}

	public static boolean equalsByValue(Object objA, Object objB)
	{
		Class classA = null, classB = null;

		if ((objA != null && objB == null) ||
				(objA == null && objB != null))
			return false; // prevent null coalescence

		if (objA != null)
			classA = objA.getClass();

		if (objB != null)
			classB = objB.getClass();

		return (objA != null ? objA.equals(objB) : (objB == null || objB.equals(objA) /* both null */));
	}

	public static <TAnnotation extends Annotation> TAnnotation[] getAllAnnotations(Class<?> target, Class<? extends TAnnotation> annotationClass)
	{
		return getAllAnnotations(new CustomAnnotationProvider()
		{
			@Override
			public <TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass)
			{
				if (target == null)
					return null;
				else
					return target.getAnnotationsByType(annotationClass);
			}
		}, annotationClass);
	}

	public static <TAnnotation extends Annotation> TAnnotation[] getAllAnnotations(Method target, Class<? extends TAnnotation> annotationClass)
	{
		return getAllAnnotations(new CustomAnnotationProvider()
		{
			@Override
			public <TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass)
			{
				if (target == null)
					return null;
				else
					return target.getAnnotationsByType(annotationClass);
			}
		}, annotationClass);
	}

	public static <TAnnotation extends Annotation> TAnnotation[] getAllAnnotations(Parameter target, Class<? extends TAnnotation> annotationClass)
	{
		return getAllAnnotations(new CustomAnnotationProvider()
		{
			@Override
			public <TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass)
			{
				if (target == null)
					return null;
				else
					return target.getAnnotationsByType(annotationClass);
			}
		}, annotationClass);
	}

	public static <TAnnotation extends Annotation> TAnnotation[] getAllAnnotations(CustomAnnotationProvider targetProvider, Class<? extends TAnnotation> annotationClass)
	{
		TAnnotation[] annotations;

		if (targetProvider == null)
			throw new ArgumentNullException("targetProvider");

		annotations = targetProvider.getAnnotations(annotationClass);

		return annotations;
	}

	public static String getErrors(Throwable exception, int indent)
	{
		String message = StringUtils.EMPTY_STRING;
		final String INDENT_CHAR = "\t";

		while (exception != null)
		{
			message += INDENT_CHAR.repeat(indent) + "+++ BEGIN EXECPTION +++" + System.lineSeparator() +
					INDENT_CHAR.repeat(indent) + "Exception class: " + exception.getClass().getName() + System.lineSeparator() +
					INDENT_CHAR.repeat(indent) + "Error message: " + exception.getMessage() + System.lineSeparator() +
					INDENT_CHAR.repeat(indent) + "Stack trace:" + System.lineSeparator();

			final StackTraceElement[] frames = exception.getStackTrace();

			if (frames != null)
			{
				for (StackTraceElement frame : frames)
					message += INDENT_CHAR.repeat(indent + 1) + frame.toString() + System.lineSeparator();
			}

			message += INDENT_CHAR.repeat(indent) + "+++ END EXECPTION +++" + System.lineSeparator();

			exception = exception.getCause();
			indent++;
		}

		return message;
	}

	public static PropertyDescriptor getLowestLogicalProperty(Class<?> targetClass, String propertyName) throws Exception
	{
		if (targetClass == null)
			throw new ArgumentNullException("targetClass");

		if (propertyName == null)
			throw new ArgumentNullException("propertyName");

		while (targetClass != null)
		{
			final BeanInfo beanInfo = Introspector.getBeanInfo(targetClass);

			if (beanInfo == null)
				return null;

			final PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();

			if (propertyDescriptors == null)
				return null;

			for (PropertyDescriptor propertyDescriptor : propertyDescriptors)
			{
				if (propertyName.equalsIgnoreCase(propertyDescriptor.getName()))
					return propertyDescriptor;
			}

			targetClass = targetClass.getSuperclass();
		}

		return null;
	}

	public static <TAnnotation extends Annotation> TAnnotation getOneAnnotation(Class<?> target, Class<? extends TAnnotation> annotationClass)
	{
		return getOneAnnotation(new CustomAnnotationProvider()
		{
			@Override
			public <TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass)
			{
				if (target == null)
					return null;
				else
					return target.getAnnotationsByType(annotationClass);
			}
		}, annotationClass);
	}

	public static <TAnnotation extends Annotation> TAnnotation getOneAnnotation(Method target, Class<? extends TAnnotation> annotationClass)
	{
		return getOneAnnotation(new CustomAnnotationProvider()
		{
			@Override
			public <TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass)
			{
				if (target == null)
					return null;
				else
					return target.getAnnotationsByType(annotationClass);
			}
		}, annotationClass);
	}

	public static <TAnnotation extends Annotation> TAnnotation getOneAnnotation(Parameter target, Class<? extends TAnnotation> annotationClass)
	{
		return getOneAnnotation(new CustomAnnotationProvider()
		{
			@Override
			public <TAnnotation extends Annotation> TAnnotation[] getAnnotations(Class<? extends TAnnotation> annotationClass)
			{
				if (target == null)
					return null;
				else
					return target.getAnnotationsByType(annotationClass);
			}
		}, annotationClass);
	}

	public static <TAnnotation extends Annotation> TAnnotation getOneAnnotation(CustomAnnotationProvider targetProvider, Class<? extends TAnnotation> annotationClass)
	{
		TAnnotation[] annotations;

		if (targetProvider == null)
			throw new ArgumentNullException("targetProvider");

		annotations = getAllAnnotations(targetProvider, annotationClass);

		if (annotations == null || annotations.length == 0)
			return null;
		else if (annotations.length > 1)
			throw new InvalidOperationException(String.format("Multiple custom attributes of type '%s' are defined on type '%s'.", annotationClass.getName(), targetProvider.getClass().getName()));
		else
			return annotations[0];
	}

	public static <TValue> TValue getValueOrDefault(TValue value, final TValue defaultValue)
	{
		value = value == null ? defaultValue : value;
		return value;
	}

	public static <TAnnotation extends Annotation> void getZeroAnnotations(CustomAnnotationProvider targetProvider, Class<? extends TAnnotation> annotationClass)
	{
		TAnnotation[] annotations;

		if (targetProvider == null)
			throw new ArgumentNullException("targetInstance");

		annotations = getAllAnnotations(targetProvider, annotationClass);

		if (annotations == null || annotations.length == 0)
			return;
		else
			throw new InvalidOperationException(String.format("Some custom attributes of type '%s' are defined on type '%s'.", annotationClass.getName(), targetProvider.getClass().getName()));
	}

	public static Object inferDbTypeFromClass(Class<?> valueClass)
	{
		if (valueClass == null)
			throw new ArgumentNullException("valueClass");

		return null;
	}

	public static <TSubclass> Class<? extends TSubclass> loadClassByName(String className, Class<TSubclass> subClass)
	{
		if (className == null)
			throw new ArgumentNullException("className");

		if (subClass == null)
			throw new ArgumentNullException("subClass");

		if (className.isEmpty())
			throw new ArgumentOutOfRangeException("className");

		try
		{
			return Class.forName(className).asSubclass(subClass);
		}
		catch (ClassNotFoundException ex)
		{
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public static <TObject> Class<? extends TObject> loadClassByName(String className)
	{
		if (className == null)
			throw new ArgumentNullException("className");

		if (className.isEmpty())
			throw new ArgumentOutOfRangeException("className");

		try
		{
			return (Class<? extends TObject>) Class.forName(className);
		}
		catch (ClassNotFoundException ex)
		{
			return null;
		}
	}

	public static Class<?> makeNonNullableClass(Class<?> conversionClass)
	{
		if (conversionClass == Byte.class)
			return byte.class;
		else if (conversionClass == Short.class)
			return byte.class;
		else if (conversionClass == Integer.class)
			return int.class;
		else if (conversionClass == Long.class)
			return long.class;
		else if (conversionClass == Character.class)
			return char.class;
		else if (conversionClass == Float.class)
			return float.class;
		else if (conversionClass == Double.class)
			return double.class;
		else if (conversionClass == Boolean.class)
			return boolean.class;
		else
			return null;
	}

	public static Class<?> makeNullableClass(Class<?> conversionClass)
	{
		if (conversionClass == byte.class)
			return Byte.class;
		else if (conversionClass == short.class)
			return Short.class;
		else if (conversionClass == int.class)
			return Integer.class;
		else if (conversionClass == long.class)
			return Long.class;
		else if (conversionClass == char.class)
			return Character.class;
		else if (conversionClass == float.class)
			return Float.class;
		else if (conversionClass == double.class)
			return Double.class;
		else if (conversionClass == boolean.class)
			return Boolean.class;
		else
			return conversionClass;
	}

	public static boolean setLogicalPropertyValue(Object targetInstance, String propertyName, Object propertyValue) throws Exception
	{
		return setLogicalPropertyValue(targetInstance, propertyName, propertyValue, false, true);
	}

	public static boolean setLogicalPropertyValue(Object targetInstance, String propertyName, Object propertyValue, boolean stayHard, boolean makeSoft) throws Exception
	{
		Class<?> targetClass;
		PropertyDescriptor propertyDescriptor;
		Map<String, Object> targetMap = null;

		if (targetInstance == null)
			return false;

		if (propertyName == null)
			return false;

		// STRATEGY: attempt reflection
		{
			Method method;
			targetClass = targetInstance.getClass();

			if (targetClass == null)
				return false;

			propertyDescriptor = getLowestLogicalProperty(targetClass, propertyName);

			if (propertyDescriptor != null &&
					(method = propertyDescriptor.getWriteMethod()) != null)
			{
				final Class<?> propertyClass = propertyDescriptor.getPropertyType();

				if (propertyClass == null)
					return false;

				propertyValue = changeClass(propertyClass, propertyValue);
				method.invoke(targetInstance, propertyValue);
				return true;
			}
		}

		// STRATEGY: attempt association
		if (!stayHard)
		{
			if (targetInstance instanceof Map<?, ?>)
				targetMap = (Map<String, Object>) targetInstance;

			if (targetMap != null)
			{
				if (!makeSoft && !targetMap.containsKey(propertyName))
					return false;

				targetMap.remove(propertyName);

				targetMap.put(propertyName, propertyValue);
				return true;
			}
		}

		return false;
	}

	public static boolean tryGetLogicalPropertyClass(Object targetInstance, String propertyName, MethodParameterModifier.Out<Class<?>> outPropertyClass) throws Exception
	{
		Class<?> targetClass, propertyClass;
		PropertyDescriptor propertyDescriptor;
		Map<String, Object> targetMap = null;
		Object propertyValue;
		final boolean stayHard = false;
		final boolean makeSoft = true;

		if (outPropertyClass == null)
			throw new InvalidOperationException("outPropertyClass");

		if (targetInstance == null)
			return false;

		if (propertyName == null)
			return false;

		// STRATEGY: attempt reflection
		{
			targetClass = targetInstance.getClass();

			propertyDescriptor = getLowestLogicalProperty(targetClass, propertyName);

			if (propertyDescriptor != null)
			{
				propertyClass = propertyDescriptor.getPropertyType();
				outPropertyClass.setValue(propertyClass);
				return true;
			}
		}

		// STRATEGY: attempt association
		if (!stayHard)
		{
			if (targetInstance instanceof Map<?, ?>)
				targetMap = (Map<String, Object>) targetInstance;

			if (targetMap != null && makeSoft &&
					targetMap.containsKey(propertyName))
			{
				propertyValue = targetMap.get(propertyName);

				if (propertyValue != null)
				{
					propertyClass = propertyValue.getClass();
					outPropertyClass.setValue(propertyClass);
					return true;
				}
			}
		}

		return false;
	}

	public static boolean tryGetLogicalPropertyValue(Object targetInstance, String propertyName, MethodParameterModifier.Out<Object> outPropertyValue) throws Exception
	{
		Class<?> targetClass;
		PropertyDescriptor propertyDescriptor;
		Map<String, Object> targetMap = null;
		Object propertyValue;
		final boolean stayHard = false;
		final boolean makeSoft = true;

		if (outPropertyValue == null)
			throw new InvalidOperationException("outPropertyValue");

		if (targetInstance == null)
			return false;

		if (propertyName == null)
			return false;

		// STRATEGY: attempt reflection
		{
			Method method;
			targetClass = targetInstance.getClass();

			if (targetClass == null)
				return false;

			propertyDescriptor = getLowestLogicalProperty(targetClass, propertyName);

			if (propertyDescriptor != null &&
					(method = propertyDescriptor.getReadMethod()) != null)
			{
				final Class<?> propertyClass = propertyDescriptor.getPropertyType();

				if (propertyClass == null)
					return false;

				propertyValue = method.invoke(targetInstance, null);
				propertyValue = changeClass(propertyClass, propertyValue);
				outPropertyValue.setValue(propertyValue);
				return true;
			}
		}

		// STRATEGY: attempt association
		if (!stayHard)
		{
			if (targetInstance instanceof Map<?, ?>)
				targetMap = (Map<String, Object>) targetInstance;

			if (targetMap != null && makeSoft &&
					targetMap.containsKey(propertyName))
			{
				propertyValue = targetMap.get(propertyName);
				outPropertyValue.setValue(propertyValue);
				return true;
			}
		}

		return false;
	}
}
