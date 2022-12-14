package us.wellengineered.solder.exeappcli;

import us.wellengineered.solder.executive.AbstractExecutableApplication;
import us.wellengineered.solder.executive.ExecutableArgument;
import us.wellengineered.solder.executive.ExecutableException;
import us.wellengineered.solder.injection.DependencyDomain;
import us.wellengineered.solder.injection.ResourceException;
import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;
import us.wellengineered.solder.primitives.StringUtils;
import us.wellengineered.solder.utilities.appsettings.AppSettingsFacadeImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class Program
{
	public Program()
	{
	}

	public static void main(String[] args) throws Exception
	{
		if (args == null)
			throw new ArgumentNullException("args");

		/*if (args.length != 1 || StringUtils.isNullOrEmptyString(args[0]))
		{
			System.out.println("USAGE: ... host-config-[file]-path");
			System.exit(-1);
			return;
		}

		final String path = args[0];*/

		// simply delegate to static bootstrapping method...
		AbstractExecutableApplication.createRun(args, SolderExeApp.class);
	}

	public static class SolderExeApp extends AbstractExecutableApplication
	{
		public SolderExeApp()
		{
			super("SolderExeApp", "1.0.0");
		}

		@Override
		protected Map<String, ExecutableArgument<?>> coreGetArgumentMap()
		{
			return new HashMap<>();
		}

		@Override
		protected int coreStartup(String[] args, Map<String, List<Object>> arguments) throws Exception
		{
			//System.out.println(String.format(">>"));
			//System.in.read();
			//System.in.read();

			System.out.println(String.format("before"));
			this.waitMainThread();
			System.out.println(String.format("after"));
			return 0;
		}

		@Override
		protected void coreShutdown() throws Exception
		{
			System.out.println(String.format("shutdown"));
			this.resumeMainThread();
		}
	}
}
