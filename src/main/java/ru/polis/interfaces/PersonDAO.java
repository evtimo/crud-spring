package ru.polis.interfaces;

import java.util.List;

import ru.polis.objects.Person;

public interface PersonDAO extends PersonTable{

	public void insert(Person person); 				// Create
	public List<Person> findAll(); 					// Read
	public void updateByID(int id, Person person); 	// Update
	public void deleteByID(int id); 				// Delete

	// *********************************

	public Person getPersonByID(int id);

	public void deleteAllPerson();
}
