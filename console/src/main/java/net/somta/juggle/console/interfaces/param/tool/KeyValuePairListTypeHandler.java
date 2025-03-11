package net.somta.juggle.console.interfaces.param.tool;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.somta.juggle.console.domain.flow.FlowParametersInfoAO;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class KeyValuePairListTypeHandler extends BaseTypeHandler<List<FlowParametersInfoAO.KeyValuePair>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<FlowParametersInfoAO.KeyValuePair> parameter, JdbcType jdbcType) throws SQLException {
        try {
            String json = objectMapper.writeValueAsString(parameter);
            ps.setString(i, json);
        } catch (IOException e) {
            throw new SQLException("Failed to convert List<KeyValuePair> to JSON", e);
        }
    }

    @Override
    public List<FlowParametersInfoAO.KeyValuePair> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, FlowParametersInfoAO.KeyValuePair.class));
        } catch (IOException e) {
            throw new SQLException("Failed to convert JSON to List<KeyValuePair>", e);
        }
    }

    @Override
    public List<FlowParametersInfoAO.KeyValuePair> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, FlowParametersInfoAO.KeyValuePair.class));
        } catch (IOException e) {
            throw new SQLException("Failed to convert JSON to List<KeyValuePair>", e);
        }
    }

    @Override
    public List<FlowParametersInfoAO.KeyValuePair> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        try {
            return objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, FlowParametersInfoAO.KeyValuePair.class));
        } catch (IOException e) {
            throw new SQLException("Failed to convert JSON to List<KeyValuePair>", e);
        }
    }
}
