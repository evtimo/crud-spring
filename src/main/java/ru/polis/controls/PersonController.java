package ru.polis.controls;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.polis.interfaces.PersonDAO;
import ru.polis.objects.Person;

@RestController
public class PersonController {

    @Autowired
    private PersonDAO personBD;

    @GetMapping
    @RequestMapping(path = "/get_all")
    public ResponseEntity<List<Person>> personAll() {
        List<Person> listPerson = personBD.findAll();
        return new ResponseEntity<List<Person>>(listPerson, HttpStatus.OK);
    }

    @GetMapping
    @RequestMapping(path = "/get_person")
    public ResponseEntity<Person> personByID(@RequestParam(value = "id") Integer id) {
        Person person;
        try {
            person = personBD.getPersonByID(id);
        } catch (NullPointerException e) {
            return new ResponseEntity<Person>(HttpStatus.I_AM_A_TEAPOT);
        } catch (EmptyResultDataAccessException e) {
            return new ResponseEntity<Person>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Person>(person, HttpStatus.OK);
    }

    @PostMapping
    @RequestMapping(path = "/post_person")
    public ResponseEntity<Person> personAdd(@RequestBody Person person) {
        personBD.insert(person);
        return new ResponseEntity<Person>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/init")
    public ResponseEntity<Person> init() {

        personBD.drop();
        personBD.create();

        Person person = new Person();
        person.setFirstName("Evgeny");
        person.setLastName("Timoshchuk");
        person.setAge(20);
        personBD.insert(person);

        person.setFirstName("Andrey");
        person.setLastName("Varlamov");
        person.setAge(30);
        personBD.insert(person);

        return new ResponseEntity<Person>(HttpStatus.OK);
    }

    @RequestMapping(path = "/delete")
    public ResponseEntity<Person> delete() {
        personBD.deleteAllPerson();
        return new ResponseEntity<Person>(HttpStatus.OK);
    }

}
