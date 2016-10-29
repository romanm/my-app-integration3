package my.app.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class MapObjMapper implements RowMapper<Map<String,Object>>{

	@Override
	public Map<String, Object> mapRow(ResultSet rs, int rowNum) throws SQLException {
		Map<String, Object> map = testMap(rs);
		return map;
	}

	private Map<String, Object> testMap(ResultSet rs) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		for (int columnIndex = 1; columnIndex <= rs.getMetaData().getColumnCount(); columnIndex++) {
			String columnName = rs.getMetaData().getColumnName(columnIndex);
			int columnType = rs.getMetaData().getColumnType(columnIndex);
//			System.out.println(columnName + "/" + columnType);
			Object columnValue = null;
			switch (columnType) {
			case Types.DECIMAL: columnValue = rs.getDouble(columnIndex); break;
			case Types.BIGINT: columnValue = rs.getInt(columnIndex); break;
			case Types.INTEGER: columnValue = rs.getInt(columnIndex); break;
			case Types.VARCHAR: columnValue = rs.getString(columnIndex); break;
			case Types.DATE: columnValue = rs.getDate(columnIndex); break;
			}
			map.put(columnName, columnValue);
		}
		return map;
	}

}
