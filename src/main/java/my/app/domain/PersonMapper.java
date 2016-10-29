package my.app.domain;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person> {

	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
//		System.out.println("----------------------");
//		Map<String, Object> map = testMap(rs);
//		System.out.println(map);
//		System.out.println("----------------------");
		Person person = new Person();
		person.setPersonId(rs.getInt("id"));
		person.setName(rs.getString("name"));
		person.setGender(Gender.getGenderByIdentifier(rs.getString("gender")));
		person.setDateOfBirth(rs.getDate("dateOfBirth"));
		return person;
	}

	private Map<String, Object> testMap(ResultSet rs) throws SQLException {
		Map<String, Object> map = new HashMap<>();
		for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
			String columnName = rs.getMetaData().getColumnName(i);
			int columnType = rs.getMetaData().getColumnType(i);
//			System.out.println(columnName + "/" + columnType);
			Object columnValue = null;
			switch (columnType) {
			case Types.INTEGER: columnValue = rs.getInt(i); break;
			case Types.VARCHAR: columnValue = rs.getString(i); break;
			case Types.DATE: columnValue = rs.getDate(i); break;
			}
			map.put(columnName, columnValue);
		}
		return map;
	}
	
}
