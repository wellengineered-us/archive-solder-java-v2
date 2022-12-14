/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.language;

import java.util.Objects;

public final class Tuple
{
	public static class Arity0
	{
		public Arity0()
		{
		}
	}

	public static class Arity1<TValue1> extends Arity0
	{
		public Arity1(TValue1 value1)
		{
			this.value1 = value1;
		}

		private final TValue1 value1;

		public TValue1 getValue1()
		{
			return this.value1;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;

			if (obj == null || getClass() != obj.getClass())
				return false;

			Arity1<?> arity1 = (Arity1<?>) obj;

			return Objects.equals(this.getValue1(), arity1.getValue1());
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(this.getValue1());
		}
	}

	public static class Arity2<TValue1, TValue2> extends Arity1<TValue1>
	{
		public Arity2(TValue1 value1, TValue2 value2)
		{
			super(value1);

			this.value2 = value2;
		}

		private final TValue2 value2;

		public TValue2 getValue2()
		{
			return this.value2;
		}

		@Override
		public boolean equals(Object obj)
		{
			if (this == obj)
				return true;

			if (obj == null || getClass() != obj.getClass())
				return false;

			Arity2<?, ?> arity2 = (Arity2<?, ?>) obj;

			return  Objects.equals(this.getValue1(), arity2.getValue1()) &&
					Objects.equals(this.getValue2(), arity2.getValue2());
		}

		@Override
		public int hashCode()
		{
			return Objects.hash(this.getValue1(), this.getValue2());
		}
	}
}
