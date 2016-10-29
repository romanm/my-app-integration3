package my.app.service;

import java.util.List;
import java.util.Map;

import my.app.domain.Person;

/**
 * The Service used to create Person instance in database
 * @author Amol Nayak
 *
 */
public interface PersonService {

	/**
	 * Get a {@link Person} by id
	 *
	 * @param person id
	 * @return the retrieved {@link Person}
	 */
	Person getPersonById(int personId);

	/**
	 * Creates a {@link Person} instance from the {@link Person} instance passed
	 *
	 * @param person created person instance, it will contain the generated primary key and the formatted name
	 * @return the retrieved {@link Person}
	 */
	Person createPerson(Person person);

	/**
	 * Update a {@link Person} by its id
	 *
	 * @param person to update
	 * @return the retrieved {@link Person}
	 */
	Person updatePerson(Person person);

	/**
	 * Find the person by the person name, the name search is case insensitive, however the
	 * spaces are not ignored
	 *
	 * @param name
	 * @return the matching {@link Person} record
	 */
	List<Person> findPersonByName(String name);

	List<Map<String, Object>> findPersonByMontAndDay(Map<String, Object> seekParameters);

}
