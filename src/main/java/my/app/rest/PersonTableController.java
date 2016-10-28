package my.app.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import my.app.domain.Person;
import my.app.service.PersonService;

@RestController
public class PersonTableController {
	private @Autowired PersonService personService;

	@RequestMapping("/list")
	public List<Person> findPersonByName(@RequestParam(value="name", defaultValue="KOSTYA") String name) {
		List<Person> findPersonByName = personService.findPersonByName(name);
		return findPersonByName;
	}

}
