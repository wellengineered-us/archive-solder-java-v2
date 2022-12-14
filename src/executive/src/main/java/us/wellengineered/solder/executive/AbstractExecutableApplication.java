/*
	Copyright ©2020 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.executive;

import us.wellengineered.solder.injection.DependencyDomain;
import us.wellengineered.solder.injection.DependencyManager;
import us.wellengineered.solder.injection.ResourceManager;
import us.wellengineered.solder.injection.resolutions.InstanceDependencyResolutionImpl;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;
import us.wellengineered.solder.polyfills.exceptions.InvalidOperationException;
import us.wellengineered.solder.polyfills.language.MethodParameterModifier;
import us.wellengineered.solder.primitives.*;
import us.wellengineered.solder.utilities.appsettings.AppSettingsFacade;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractExecutableApplication extends AbstractLifecycle<Exception, Exception> implements ExecutableApplication
{
	protected AbstractExecutableApplication(String name, String version)
	{
		this.onShutdownThread = new Thread(this::onShutdown);

		this.name = name;
		this.version = version;
	}

	private final Thread onShutdownThread;
	private static final String APPCONFIG_ID_REGEX_UNBOUNDED = "[a-zA-Z_\\.][a-zA-Z_\\.0-9]";
	private static final String APPCONFIG_ARGS_REGEX = "-(" + APPCONFIG_ID_REGEX_UNBOUNDED + "{0,63}):(.{0,})";
	private static final String APPCONFIG_PROPS_REGEX = "(" + APPCONFIG_ID_REGEX_UNBOUNDED + "{0,63})=(.{0,})";
	private final String name;
	private final String version;
	protected final int ONE = 1;

	protected final int ZERO = 0;
	//private final CancellationToken cancellationToken = new AtomicCancellationTokenImpl();
	private final Semaphore mainThreadSemaphore = new Semaphore(ONE);

	/*protected final CancellationToken getCancellationToken()
	{
		return this.cancellationToken;
	}*/

	protected final void waitMainThread() throws Exception
	{
		this.getMainThreadSemaphore().acquire();
	}

	protected final void resumeMainThread() throws Exception
	{
		this.getMainThreadSemaphore().release();
	}

	private final Semaphore getMainThreadSemaphore()
	{
		return this.mainThreadSemaphore;
	}

	private static String getAppConfigArgsRegexPattern()
	{
		return APPCONFIG_ARGS_REGEX;
	}

	private static String getAppConfigIdRegexUnboundedPattern()
	{
		return APPCONFIG_ID_REGEX_UNBOUNDED;
	}

	private static String getAppConfigPropsRegexPattern()
	{
		return APPCONFIG_PROPS_REGEX;
	}

	public String getName()
	{
		return this.name;
	}

	public String getVersion()
	{
		return this.version;
	}

	@Override
	protected void coreCreate(boolean creating) throws Exception
	{
		if (creating)
		{
			Runtime.getRuntime().addShutdownHook(this.onShutdownThread);
		}
	}

	@Override
	protected void coreDispose(boolean disposing) throws Exception
	{
		if (disposing)
		{
			Runtime.getRuntime().removeShutdownHook(this.onShutdownThread);
		}
	}

	private final void onShutdown()
	{
		// this method gets called on another thread from main()...
		try
		{
			this.coreShutdown();
		}
		catch (Exception ex)
		{
			throw new FailFastException(ex);
		}
	}

	protected final static boolean isRunningDebugMode()
	{
		final boolean isDebug =
				java.lang.management.ManagementFactory.getRuntimeMXBean().
						getInputArguments().toString().indexOf("-agentlib:jdwp") > 0;

		return isDebug;
	}

	// this method gets called on another thread from main()...
	protected abstract void coreShutdown() throws Exception;

	protected void coreUxDisplayArgumentErrorMessage(Iterable<Message> argumentMessages)
	{
		if (argumentMessages == null)
			throw new ArgumentNullException("argumentMessages");

		System.out.print(System.lineSeparator());
		for (Message argumentMessage : argumentMessages)
			System.out.println(argumentMessage.getDescription());
	}

	protected void coreUxDisplayArgumentMapMessage(Map<String, ExecutableArgument<?>> argumentMap)
	{
		String argumentTokens;
		boolean first = true;

		if (argumentMap == null)
			throw new ArgumentNullException("argumentMap");

		argumentTokens = StringUtils.EMPTY_STRING;

		for (Map.Entry<String, ExecutableArgument<?>> argumentEntry : argumentMap.entrySet())
		{
			if (argumentEntry == null)
				continue;

			final ExecutableArgument<?> executableArgument = argumentEntry.getValue();

			if (executableArgument == null)
				continue;

			if (!first)
				argumentTokens += " ";

			argumentTokens += (!executableArgument.isRequired() ? "[" : StringUtils.EMPTY_STRING) +
					String.format("-%s:value%s", executableArgument.getName(), !executableArgument.isBounded() ? "(s)" : StringUtils.EMPTY_STRING) +
					(!executableArgument.isRequired() ? "]" : StringUtils.EMPTY_STRING);

			first = false;
		}

		System.out.print(System.lineSeparator());
		System.out.println(String.format("USAGE: `%s` %s", this.getName(), argumentTokens));
	}

	protected void coreUxDisplayBannerMessage()
	{
		System.out.println(String.format("[%s v%s]",
				this.getName(),
				this.getVersion()));
	}

	protected void coreUxDisplayFailureMessage(Exception exception)
	{
		if (exception == null)
			throw new ArgumentNullException("exception");

		System.out.print(System.lineSeparator());
		System.out.println(exception != null ? ReflectionUtils.getErrors(exception, 0) : "<unknown>");

		System.out.print(System.lineSeparator());
		System.out.println(String.format("The operation failed to complete."));
	}

	protected void coreUxDisplayRawArgumentsMessage(String[] args, Iterable<String> arguments)
	{
		if (args == null)
			throw new ArgumentNullException("args");

		if (arguments == null)
			throw new ArgumentNullException("arguments");

		System.out.print(System.lineSeparator());
		System.out.println(String.format("RAW CMDLN: %s", String.join(" ", args)));
		System.out.print(System.lineSeparator());
		System.out.println(String.format("RAW ARGS:"));

		int index = 0;
		for (String argument : arguments)
			System.out.println(String.format("[%s] => %s", index++, argument));
	}

	protected void coreUxDisplaySuccessMessage(double durationSeconds)
	{
		System.out.print(System.lineSeparator());
		System.out.println(String.format("Operation completed successfully; duration: '%s'.", durationSeconds));
	}

	protected abstract Map<String, ExecutableArgument<?>> coreGetArgumentMap();

	private Map<String, List<String>> parseCommandLineArguments(String[] args)
	{
		Map<String, List<String>> arguments;
		Pattern pattern;
		Matcher matcher;
		String key, value;
		List<String> argumentValues;

		if (args == null)
			throw new ArgumentNullException("args");

		arguments = new LinkedHashMap<>();
		pattern = Pattern.compile(getAppConfigArgsRegexPattern(), Pattern.CASE_INSENSITIVE);

		if (pattern == null)
			throw new InvalidOperationException();

		for (String arg : args)
		{
			matcher = pattern.matcher(arg);

			if (matcher == null)
				continue;

			if (!matcher.matches())
				continue;

			if (matcher.groupCount() != (3 - 1))
				continue;

			key = matcher.group(1).toString();
			value = matcher.group(2).toString();

			// key is required
			if (StringUtils.isNullOrWhiteSpace(key))
				continue;

			// val is required
			if (StringUtils.isNullOrWhiteSpace(value))
				continue;

			if (!arguments.containsKey(key))
				arguments.put(key, new ArrayList<>());

			argumentValues = arguments.get(key);

			// duplicate values are ignored
			if (argumentValues.contains(value))
				continue;

			argumentValues.add(value);
		}

		return arguments;
	}

	private boolean tryParseCommandLineArgumentProperty(String arg, MethodParameterModifier.Out<String> outKey, MethodParameterModifier.Out<String> outValue)
	{
		Pattern pattern;
		Matcher matcher;
		String key, value;

		if (arg == null)
			throw new ArgumentNullException("arg");

		if (outKey == null)
			throw new ArgumentNullException("outKey");

		if (outValue == null)
			throw new ArgumentNullException("outValue");

		pattern = Pattern.compile(getAppConfigPropsRegexPattern(), Pattern.CASE_INSENSITIVE);

		if (pattern == null)
			throw new InvalidOperationException();

		matcher = pattern.matcher(arg);

		if (matcher == null)
			return false;

		if (!matcher.matches())
			return false;

		if (matcher.groupCount() != (3 - 1))
			return false;

		key = matcher.group(1).toString();
		value = matcher.group(2).toString();

		// key is required
		if (StringUtils.isNullOrWhiteSpace(key))
			return false;

		// val is required
		if (StringUtils.isNullOrWhiteSpace(value))
			return false;

		outKey.setValue(key);
		outValue.setValue(value);

		return true;
	}

	public final int run(String[] args) throws ExecutableException
	{
		try
		{
			this.getMainThreadSemaphore().acquire();
		}
		catch (Exception ex)
		{
			throw new ExecutableException(ex);
		}

		try (DependencyDomain dependencyDomain = DependencyDomain.getDefault())
		{
			final ResourceManager resourceManager = dependencyDomain.getResourceManager();
			final DependencyManager dependencyManager = dependencyDomain.getDependencyManager();

			resourceManager.check();

			//dependencyManager
					//.addResolution(ExecutableApplication.class, "", true, new InstanceDependencyResolutionImpl<>(this));

			final int result = this.coreRunUx(args);

			resourceManager.check();

			return result;
		}
		catch (Exception ex)
		{
			this.coreUxDisplayFailureMessage(ex);
			throw new ExecutableException(ex);
		}
		finally
		{
			this.getMainThreadSemaphore().release();
		}
	}

	protected abstract int coreStartup(String[] args, Map<String, List<Object>> arguments) throws Exception;

	private int coreRunUx(String[] args) throws Exception
	{
		int returnCode;
		Map<String, ExecutableArgument<?>> argumentMap;
		List<Message> argumentValidationMessages;

		List<String> argumentValues;
		Map<String, List<String>> arguments;

		Map<String, List<Object>> finalArguments;
		List<Object> finalArgumentValues;
		Object finalArgumentValue;

		final Instant startInstant = Instant.now();

		this.coreUxDisplayBannerMessage();

		arguments = this.parseCommandLineArguments(args);
		argumentMap = this.coreGetArgumentMap();

		finalArguments = new LinkedHashMap<>();
		argumentValidationMessages = new ArrayList<>();

		if (argumentMap != null)
		{
			for (String argumentToken : argumentMap.keySet())
			{
				boolean argumentExists;
				int argumentValueCount = 0;
				ExecutableArgument<?> argumentSpec;

				final MethodParameterModifier.Out<List<String>> outArgumentValues = new MethodParameterModifier.Out<>();

				if (argumentExists = MapUtils.tryMapGetValue(arguments, argumentToken, outArgumentValues))
				{
					if ((argumentValues = outArgumentValues.getValue()) != null)
						argumentValueCount = argumentValues.size();
				}
				else
					argumentValues = null;

				final MethodParameterModifier.Out<ExecutableArgument<?>> outArgumentSpec = new MethodParameterModifier.Out<>();

				if (!MapUtils.tryMapGetValue(argumentMap, argumentToken, outArgumentSpec))
					continue;

				if ((argumentSpec = outArgumentSpec.getValue()) == null)
					continue;

				if (argumentSpec.isRequired() && !argumentExists)
				{
					argumentValidationMessages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("A required argument was not specified: '%s'.", argumentToken), Severity.ERROR));
					continue;
				}

				if (argumentSpec.isBounded() && argumentValueCount > 1)
				{
					argumentValidationMessages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("A bounded argument was specified more than once: '%s'.", argumentToken), Severity.ERROR));
					continue;
				}

				if (argumentValues != null)
				{
					finalArgumentValues = new ArrayList<>();

					if (argumentSpec.getValueClass() != null)
					{
						for (String argumentValue : argumentValues)
						{
							// explicit generic type erasure here
							final Class clazz = argumentSpec.getValueClass();
							final MethodParameterModifier.Out outFinalArgumentValue = new MethodParameterModifier.Out(clazz);

							if (!StringUtils.tryParse(argumentSpec.getValueClass(), argumentValue, outFinalArgumentValue))
								argumentValidationMessages.add(new MessageImpl(StringUtils.EMPTY_STRING, String.format("An argument '%s' value '%s' was specified that failed to parse to the target type '%s'.", argumentToken, argumentValue, argumentSpec.getValueClass().getName()), Severity.ERROR));
							else
								finalArgumentValues.add((finalArgumentValue = outFinalArgumentValue.getValue()));
						}
					}
					else
					{
						for (String argumentValue : argumentValues)
							finalArgumentValues.add(argumentValue);
					}

					finalArguments.put(argumentToken, finalArgumentValues);
				}
			}
		}

		if (!argumentValidationMessages.isEmpty())
		{
			this.coreUxDisplayArgumentErrorMessage(argumentValidationMessages);
			this.coreUxDisplayArgumentMapMessage(argumentMap);
			//this.displayRawArgumentsMessage(args);

			returnCode = -1;
		}
		else
			returnCode = this.coreStartup(args, finalArguments);

		final Instant endInstant = Instant.now();
		final Double durationSeconds = ChronoUnit.MILLIS.between(startInstant, endInstant) / 1000.0;

		this.coreUxDisplaySuccessMessage(durationSeconds);

		return returnCode;
	}

	public static <TExecutableApplication extends ExecutableApplication> int createRun(String[] args, Class<? extends TExecutableApplication> instantiateClass) throws Exception
	{
		if (args == null)
			throw new ArgumentNullException("args");

		if (instantiateClass == null)
			throw new ArgumentNullException("instantiateClass");

		try (TExecutableApplication exeApp = ReflectionUtils.createInstanceAssignableToClass(instantiateClass, null, true))
		{
			exeApp.create();
			return exeApp.run(args);
		}
	}
}
