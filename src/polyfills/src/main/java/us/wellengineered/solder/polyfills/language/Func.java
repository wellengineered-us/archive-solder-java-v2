/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.language;

public final class Func
{
	@FunctionalInterface
	public interface Arity0
	{
		void invoke() throws Exception;
	}

	@FunctionalInterface
	public interface Arity1<TReturn>
	{
		TReturn invoke() throws Exception;
	}

	@FunctionalInterface
	public interface Arity2<TArg0, TReturn>
	{
		TReturn invoke(TArg0 arg0) throws Exception;
	}

	@FunctionalInterface
	public interface Arity3<TArg0, TArg1, TReturn>
	{
		TReturn invoke(TArg0 arg0, TArg1 arg1) throws Exception;
	}

	@FunctionalInterface
	public interface Arity4<TArg0, TArg1, TArg2, TReturn>
	{
		TReturn invoke(TArg0 arg0, TArg1 arg1, TArg2 arg2) throws Exception;
	}
}
