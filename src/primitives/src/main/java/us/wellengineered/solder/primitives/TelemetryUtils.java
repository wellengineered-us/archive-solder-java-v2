/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.primitives;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;

import java.util.UUID;

public final class TelemetryUtils
{
	public static String formatCallerInfo()
	{
		return formatCallerInfo(1 + 1);
	}

	public static String formatCallerInfo(int skipFrames)
	{
		final int SKIP_FRAMES = skipFrames + 1;
		final StackWalker stackWalker = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);
		final StackWalker.StackFrame stackFrame = stackWalker.walk(frames -> frames.skip(SKIP_FRAMES).findFirst()).get();

		return String.format("%s::%s(),%s", stackFrame.getClassName(), stackFrame.getMethodName(), formatCurrentThreadId());
	}

	public static String formatCurrentThreadId()
	{
		return String.format("%05X", Thread.currentThread().getId());
	}

	public static String formatObjectInfo(Object obj)
	{
		if (obj == null)
			throw new ArgumentNullException("obj");

		return String.format("%s@%05X", obj.getClass().getName(), System.identityHashCode(obj));
	}

	public static String formatOperation(String operation, UUID slotId, Object obj)
	{
		if (operation == null)
			throw new ArgumentNullException("operation");

		if (slotId == null)
			throw new ArgumentNullException("slotId");

		if (obj == null)
			throw new ArgumentNullException("obj");

		return String.format("%s => %s", formatOperation(operation, slotId), formatObjectInfo(obj));
	}

	public static String formatOperation(String operation, UUID slotId)
	{
		if (operation == null)
			throw new ArgumentNullException("operation");

		if (slotId == null)
			throw new ArgumentNullException("slotId");

		return String.format("%s#%s", operation, formatUUID(slotId));
	}

	public static String formatUUID(UUID uuid)
	{
		if (uuid == null)
			throw new ArgumentNullException("uuid");

		return uuid.toString().replace("-", "").toUpperCase();
	}
}
