package debugger.util;

import java.util.List;

/**
 * Beschreibt die Schnittstelle zur Datenbank des Systems.
 *
 * @author Simon Danner, 16.10.2017
 */
public interface IDatabase
{
  /**
   * Liefert eine Liste aller Personen, welche in der Datenbank gespeichert sind.
   *
   * @return eine Liste von Personen
   */
  List<Person> readPersonsFromDB();

  /**
   * Fügt der Datenbank eine Person hinzu
   *
   * @param pPerson die Person, welche hinzugeführt werden soll
   */
  void addPerson(Person pPerson);

  /**
   * Enfernt eine Person aus der Datenbank.
   *
   * @param pPerson die Person, welche entfernt werden soll
   * @return <tt>true</tt>, wenn die Person entfernt wurde
   */
  boolean removePerson(Person pPerson);
}
