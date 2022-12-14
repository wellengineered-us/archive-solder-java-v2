/*
	Copyright ©2020-2022 WellEngineered.us, all rights reserved.
	Distributed under the MIT license: https://opensource.org/licenses/MIT
*/

package us.wellengineered.solder.polyfills.data;

public final class DbParameterImpl implements DbParameter
{
	public DbParameterImpl()
	{
	}
	private DbType dbType;
	private ParameterDirection direction;
	private Boolean isNullable;
	private String parameterName;
	private Integer parameterOrdinal;
	private Byte precision;
	private Byte scale;
	private Integer size;
	private String sourceColumn;
	private Object value;

	@Override
	public DbType getDbType()
	{
		return this.dbType;
	}

	public void setDbType(DbType dbType)
	{
		this.dbType = dbType;
	}

	@Override
	public ParameterDirection getDirection()
	{
		return this.direction;
	}

	public void setDirection(ParameterDirection direction)
	{
		this.direction = direction;
	}

	@Override
	public String getParameterName()
	{
		return this.parameterName;
	}

	public void setParameterName(String parameterName)
	{
		this.parameterName = parameterName;
	}

	@Override
	public Integer getParameterOrdinal()
	{
		return this.parameterOrdinal;
	}

	public void setParameterOrdinal(Integer parameterOrdinal)
	{
		this.parameterOrdinal = parameterOrdinal;
	}

	@Override
	public Byte getPrecision()
	{
		return this.precision;
	}

	public void setPrecision(Byte precision)
	{
		this.precision = precision;
	}

	@Override
	public Byte getScale()
	{
		return this.scale;
	}

	public void setScale(Byte scale)
	{
		this.scale = scale;
	}

	@Override
	public Integer getSize()
	{
		return this.size;
	}

	public void setSize(Integer size)
	{
		this.size = size;
	}

	@Override
	public String getSourceColumn()
	{
		return this.sourceColumn;
	}

	public void setSourceColumn(String sourceColumn)
	{
		this.sourceColumn = sourceColumn;
	}

	@Override
	public Object getValue()
	{
		return this.value;
	}

	@Override
	public void setValue(Object value)
	{
		this.value = value;
	}

	public void setNullable(Boolean isNullable)
	{
		this.isNullable = isNullable;
	}

	@Override
	public Boolean isNullable()
	{
		return this.isNullable;
	}
}
