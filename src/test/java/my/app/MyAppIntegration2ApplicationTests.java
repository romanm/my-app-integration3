package my.app;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import my.app.domain.Gender;
import my.app.domain.Person;
import my.app.service.PersonService;

/**
 * @author Artem Bilan
 * @since 2.2.0
 * https://gist.github.com/artembilan/8d7d4cf6959c71be2cc9
 */
//@RunWith(SpringRunner.class)
//@SpringBootTest
public class MyAppIntegration2ApplicationTests {
	private static final Logger LOGGER = Logger.getLogger(MyAppIntegration2ApplicationTests.class);

	private @Value("${sql.sample.person.insert}") String sqlSamplePersonInsert;
	
	private void insertPersonRecord(PersonService personService) {	
		
		LOGGER.info("Creating person Instance");
		Person person = new Person();
		Calendar dateOfBirth = Calendar.getInstance();
		dateOfBirth.set(1980, 0, 1);
		person.setDateOfBirth(dateOfBirth.getTime());
		person.setName("My App Person");
		person.setGender(Gender.MALE);
		person = personService.createPerson(person);
		Assert.assertNotNull("Expected a non null instance of Person, got null", person);
		LOGGER.info("\n\tGenerated person "   );
		printlnSysOut(person);
	}

//	@Test
	public void contextLoads() {
		ConfigurableApplicationContext run 
		= SpringApplication.run(MyAppIntegration2Application.class, "");
		System.out.println(run);
		
		final Scanner scanner = new Scanner(System.in);
		
		final PersonService personService = run.getBean(PersonService.class);
		
		
		LOGGER.info("\n========================================================="
				  + "\n                                                         "
				  + "\n    My app test										    "
				  + "\n                                                         "
				  + "\n=========================================================" );
		
		//------------------------------------
		System.out.println("--------------findPersonByMontAndDay-----------------------");
		Map<String, Object> sm = new HashMap<>();
		sm.put("month", 12);
		sm.put("day", 26);
		List<Map<String, Object>> findPersonByMontAndDay = personService.findPersonByMontAndDay(sm);
		System.out.println(findPersonByMontAndDay.size());
		System.out.println(findPersonByMontAndDay);
		System.out.println("--------------findPersonByMontAndDay-----------------------END");
		List<Person> findPersonByName = personService.findPersonByName("KOSTYA");
		for(Person person:findPersonByName) {
			printlnSysOut(person);
		}
//		//------------------------------------
//		System.out.println("--------------getPersonById-----------------------");
//		Person person = personService.getPersonById(12);
//		printlnSysOut(person);
//		System.out.println("--------------updatePerson-----------------------");
//		person.setName("from xml");
//		personService.updatePerson(person);
//		printlnSysOut(person);
//		System.out.println("--------------updatePerson-----------------------END");
		//------------------------------------
		insertPersonRecord(personService);
		//------------------------------------

		
		LOGGER.info("\n========================================================="
				+ "\n                                                         "
				+ "\n    Please press 'q + Enter' to quit the application.    "
				+ "\n                                                         "
				+ "\n=========================================================" );

		System.out.println("Please enter a choice and press <enter>: ");
		System.out.println("\t1. Find person details");
		System.out.println("\t2. Create a new person detail");
		System.out.println("\tq. Quit the application");
		System.out.println(sqlSamplePersonInsert);
		System.out.print("Enter you choice: ");
		while (true) {
			if(true) break;
			final String input = scanner.nextLine();
			if("q".equals(input.trim())) {
				break;
			} else {
				System.out.println("Invalid choice\n\n");
			}

			System.out.println("Please enter a choice and press <enter>: ");
			System.out.println("\t1. Find person details");
			System.out.println("\t2. Create a new person detail");
			System.out.println("\tq. Quit the application");
			System.out.print("Enter you choice: ");
		}

		LOGGER.info("Exiting application...bye.");
	}

	private void printlnSysOut(Person person) {
		System.out.print(
				String.format("Person found - Person Id: '%d', Person Name is: '%s',  Gender: '%s'",
							  person.getPersonId(),person.getName(), person.getGender()));
		System.out.println(String.format(", Date of birth: '%1$td/%1$tm/%1$tC%1$ty'", person.getDateOfBirth()));
	}

}
