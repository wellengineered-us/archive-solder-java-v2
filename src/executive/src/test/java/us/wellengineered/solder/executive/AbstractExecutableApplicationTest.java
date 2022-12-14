package us.wellengineered.solder.executive;

import org.junit.jupiter.api.Test;
import us.wellengineered.solder.utilities.appsettings.AppSettingsFacadeImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AbstractExecutableApplicationTest
{
	public AbstractExecutableApplicationTest()
	{
	}

	private final String CMDLN_DEBUGGER_LAUNCH = "debug";
	private final String CMDLN_TOKEN_BASEDIR = "basedir";
	private final String CMDLN_TOKEN_PROPERTY = "property";
	private final String CMDLN_TOKEN_SOURCEFILE = "sourcefile";
	private final String CMDLN_TOKEN_SOURCESTRATEGY_AQTN = "sourcestrategy";
	private final String CMDLN_TOKEN_STRICT = "strict";
	private final String CMDLN_TOKEN_TEMPLATEFILE = "templatefile";

	@Test
	public void shouldTestEasy() throws Exception
	{
				final AbstractExecutableApplication executableApplicationFascade =
				new AbstractExecutableApplication("Easy Test", "1.2.3.4")
				{
					@Override
					protected void coreShutdown() throws Exception
					{

					}

					@Override
					protected Map<String, ExecutableArgument<?>> coreGetArgumentMap()
					{
						Map<String, ExecutableArgument<?>> argumentMap;

						argumentMap = new LinkedHashMap<>();
						argumentMap.put(CMDLN_TOKEN_TEMPLATEFILE, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_TEMPLATEFILE, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_SOURCEFILE, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_SOURCEFILE, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_BASEDIR, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_BASEDIR, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_SOURCESTRATEGY_AQTN, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_SOURCESTRATEGY_AQTN, true, true, String.class, null));
						argumentMap.put(CMDLN_TOKEN_STRICT, new ExecutableArgumentImpl<Boolean>(CMDLN_TOKEN_STRICT, true, true, Boolean.class, null));
						argumentMap.put(CMDLN_TOKEN_PROPERTY, new ExecutableArgumentImpl<String>(CMDLN_TOKEN_PROPERTY, false, false, String.class, null));
						argumentMap.put(CMDLN_DEBUGGER_LAUNCH, new ExecutableArgumentImpl<Boolean>(CMDLN_DEBUGGER_LAUNCH, false, true, Boolean.class, null));

						return argumentMap;
					}

					@Override
					protected int coreStartup(String[] args, Map<String, List<Object>> arguments)
					{
						return 0;
					}
				};

		executableApplicationFascade.run(new String[] { "-basedir:xxx", "-strict:123" });
	}
}
