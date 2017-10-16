package debugger.util;

import java.util.List;

/**
 * Some useful methods for class Person
 *
 * @author Simon Danner, 16.10.2017
 */
public final class PersonUtil
{
  private PersonUtil()
  {
  }

  /**
   * Prüft, ob es sich um eine gültige Person handelt.
   *
   * @param pPerson die Person, welche überprüft werden soll
   */
  public static void checkValidPerson(Person pPerson)
  {
    _checkNotNullNotEmpty(pPerson.getName());
    _checkNotNullNotEmpty(pPerson.getAddress());
    if (pPerson.getAge() < 0)
      throw new IllegalArgumentException("age must not be negative!");
  }

  /**
   * Gibt an, ob es sich bei einer bestimmten Person, um die älteste aus einer Menge von Personen handelt.
   *
   * @param pPerson die zu prüfende Person
   * @param pPeople die Menge von Personen
   * @return <tt>true</tt>, wenn es sich um die älteste Person handelt
   */
  public static boolean isTheOldest(Person pPerson, List<Person> pPeople)
  {
    return pPeople.stream()
        .mapToInt(Person::getAge)
        .max()
        .orElse(-1) == pPerson.getAge();
  }

  /**
   * Überprüft, ob eine String nicht null und nicht leer ist.
   *
   * @param pToCheck der String der geprüft werden soll
   */
  private static void _checkNotNullNotEmpty(String pToCheck)
  {
    if (pToCheck == null || pToCheck.isEmpty())
      throw new IllegalArgumentException("argument must not be null or empty!");
  }
}
