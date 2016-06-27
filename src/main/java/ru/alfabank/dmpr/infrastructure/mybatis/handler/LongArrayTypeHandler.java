package ru.alfabank.dmpr.infrastructure.mybatis.handler;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LongArrayTypeHandler extends BaseTypeHandler<long[]> {

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, long[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, StringUtils.join(parameter, ','));
    }

    @Override
    public long[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        throw new UnsupportedOperationException();
    }
}
