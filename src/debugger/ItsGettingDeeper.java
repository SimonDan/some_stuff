package debugger;

import debugger.util.*;

import java.util.List;

/**
 * Demonstration der Aufruf-Reihenfolge während des Debuggings.
 *
 * @author Simon Danner, 16.10.2017
 */
public class ItsGettingDeeper
{
  private static IDatabase database = new DBImpl(); //Schnittstelle zur Datenbank

  public static void main(String[] pArgs)
  {
    _insertPersonAndCheck("Fancy Name", 99, "Fancy Town");
  }

  private static void _insertPersonAndCheck(String pName, int pAge, String pAddress)
  {
    Person person = new Person(pName, pAge, pAddress);
    List<Person> people = database.readPersonsFromDB();
    _checkIfValid(people);
    _checkIfExists(person, people);
    database.addPerson(person);
    System.out.println("Erfolg!");
  }

  private static void _checkIfValid(List<Person> pPeople)
  {
    for (Person person : pPeople)
      PersonUtil.checkValidPerson(person);
  }

  private static void _checkIfExists(Person pPerson, List<Person> pPeople)
  {
    PersonUtil.checkValidPerson(pPerson);
    if (pPeople.contains(pPerson))
      throw new IllegalArgumentException("person already added to the database!");
  }
}
