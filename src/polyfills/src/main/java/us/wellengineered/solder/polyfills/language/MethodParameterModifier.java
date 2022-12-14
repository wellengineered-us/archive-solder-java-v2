/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.language;

import us.wellengineered.solder.polyfills.exceptions.ArgumentNullException;
import us.wellengineered.solder.polyfills.exceptions.FailFastException;

public final class MethodParameterModifier
{
	public final static class In<TValue>
	{
		public In(Class<? extends TValue> valueClass, TValue value)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.valueClass = valueClass;
			this.value = value;

			if (value != null)
			{
				if (!this.getValueClass()
						.isAssignableFrom(value.getClass()))
					throw new FailFastException(
							String.format(
									"Input parameter type '%s' is not compatible with value type '%s'.",
									this.getValueClass().getName(),
									value == null ? null : value.getClass().getName())
					);
			}
		}

		private final TValue value;
		private final Class<? extends TValue> valueClass;

		public TValue getValue()
		{
			return this.value;
		}

		public Class<? extends TValue> getValueClass()
		{
			return this.valueClass;
		}
	}

	public final static class Out<TValue>
	{
		public Out()
		{
			this.value = null;
			this.set = false;
			this.valueClass = null;
		}

		public Out(Class<? extends TValue> valueClass)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.value = null;
			this.set = false;
			this.valueClass = valueClass;
		}

		private final Class<? extends TValue> valueClass;
		private boolean set;
		private TValue value;

		public TValue getValue()
		{
			if (!this.isSet())
			{
				throw new FailFastException(
						String.format(
								"Output parameter has not been set previously.")
				);
			}

			return this.value;
		}

		public void setValue(TValue value)
		{
			if (this.isSet())
			{
				throw new FailFastException(
						String.format(
								"Output parameter has been set previously.")
				);
			}

			if (value != null &&
					this.getValueClass() != null)
			{
				if (!this.getValueClass()
						.isAssignableFrom(value.getClass()))
					throw new FailFastException(
							String.format(
									"Output parameter type '%s' is not compatible with value type '%s'.",
									this.getValueClass().getName(),
									value == null ? null : value.getClass().getName())
					);
			}

			this.value = value;
			this.setSet(true);
		}

		public Class<? extends TValue> getValueClass()
		{
			return this.valueClass;
		}

		public boolean isSet()
		{
			return this.set;
		}

		private void setSet(boolean set)
		{
			this.set = set;
		}
	}

	public final static class Ref<TValue>
	{
		public Ref(Class<? extends TValue> valueClass)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.valueClass = valueClass;
			this.value = null;
			this.set = false;
		}

		public Ref(Class<? extends TValue> valueClass, TValue value)
		{
			if (valueClass == null)
				throw new ArgumentNullException("valueClass");

			this.valueClass = valueClass;
			this.value = value;

			if (value != null)
			{
				if (!this.getValueClass()
						.isAssignableFrom(value.getClass()))
					throw new FailFastException(
							String.format(
									"Input parameter type '%s' is not compatible with value type '%s'.",
									this.getValueClass().getName(),
									value == null ? null : value.getClass().getName())
					);
			}

			this.set = true;
		}

		private final Class<? extends TValue> valueClass;
		private boolean set;
		private TValue value;

		public TValue getValue()
		{
			if (!this.isSet())
			{
				throw new FailFastException(
						String.format(
								"Ref parameter has not been set previously.")
				);
			}

			return this.value;
		}

		public void setValue(TValue value)
		{
			if (this.isSet())
			{
				throw new FailFastException(
						String.format(
								"Ref parameter has been set previously.")
				);
			}

			if (value != null)
			{
				if (!this.getValueClass()
						.isAssignableFrom(value.getClass()))
					throw new FailFastException(
							String.format(
									"Ref parameter type '%s' is not compatible with value type '%s'.",
									this.getValueClass().getName(),
									value == null ? null : value.getClass().getName())
					);
			}

			this.value = value;
			this.setSet(true);
		}

		public Class<? extends TValue> getValueClass()
		{
			return this.valueClass;
		}

		public boolean isSet()
		{
			return this.set;
		}

		private void setSet(boolean set)
		{
			this.set = set;
		}
	}
}
