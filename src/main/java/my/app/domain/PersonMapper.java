package my.app.domain;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class PersonMapper implements RowMapper<Person> {

	public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
		Person person = new Person();
		person.setPersonId(rs.getInt("id"));
		person.setName(rs.getString("name"));
		person.setGender(Gender.getGenderByIdentifier(rs.getString("gender")));
		person.setDateOfBirth(rs.getDate("dateOfBirth"));
		return person;
	}

}
