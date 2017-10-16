package debugger.util;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Beschreibt eine Person mit ihren Eigenschaften.
 *
 * @author Simon Danner, 16.10.2017
 */
public class Person
{
  //Beispiele
  public final static Person HANS_DAMPF = new Person("Hans Dampf", 36, "Hansstraße 5 12345 Hanshausen");
  public final static Person MAX_MUSTER = new Person("Max Muster", 18, "Musterstraße 27 54321 Musterstadt", HANS_DAMPF);

  private final String name;
  private final int age;
  private final String address;
  private final List<Person> friends;

  /**
   * Initialisiert eine Person.
   *
   * @param pName    der Name der Person
   * @param pAge     das Alter der Person
   * @param pAddress die Adresse der Person
   * @param pFriends die Freunde dieser Person (0 - beliebig)
   */
  public Person(String pName, int pAge, String pAddress, Person... pFriends)
  {
    if (pAge < 0)
      throw new IllegalArgumentException("age must not be negative!");

    name = pName;
    age = pAge;
    address = _checkNullEmpty(pAddress);
    friends = new ArrayList<>(Arrays.asList(pFriends));

    if (friends.contains(this))
      throw new IllegalArgumentException("you can not be your own friend!");
  }

  /**
   * Liefert den Namen dieser Person.
   *
   * @return der Name der Person
   */
  public String getName()
  {
    return name;
  }

  /**
   * Liefert das Alter (in Jahren) dieser Person.
   *
   * @return das Alter der Person
   */
  public int getAge()
  {
    return age;
  }

  /**
   * Liefert die Adresse dieser Person als Text.
   *
   * @return die Adresse der Person
   */
  public String getAddress()
  {
    return address;
  }

  /**
   * Liefert die Freunde dieser Person.
   *
   * @return eine Liste von Personen
   */
  public List<Person> getFriends()
  {
    return Collections.unmodifiableList(friends); //Nur lesen!
  }

  /**
   * Liefert eine Liste aller Freunde dieser Person, welche älter als ein bestimmtes Alter sind.
   *
   * @param pAge das bestimmte Grenzalter
   * @return eine Liste von Personen
   */
  public List<Person> getFriendsOlderThan(int pAge)
  {
    return friends.stream()
        .filter(pPerson -> pPerson.getAge() > pAge)
        .collect(Collectors.toList());
  }

  /**
   * Gibt an, ob diese Person in einer bestimmten Stadt wohnt.
   *
   * @param pCity die bestimmte/gesuchte Stadt
   * @return <tt>true</tt>, wenn die Peron in dieser Stadt wohnt
   */
  public boolean livesInCity(String pCity)
  {
    return address.contains(pCity);
  }

  /**
   * Fügt dieser Person einen neuen Freund hinzu.
   *
   * @param pFriend die Person, welche als Freund hinzugefügt werden soll
   */
  public void addFriend(Person pFriend)
  {
    if (pFriend == this)
      throw new IllegalArgumentException("you can not be your own friend!");

    if (friends.contains(pFriend))
      throw new IllegalArgumentException(pFriend.getName() + " is already a friend of " + getName());

    friends.add(pFriend);
  }

  @Override
  public String toString()
  {
    return "Person{" +
        "name='" + name + '\'' +
        ", age=" + age +
        ", address='" + address + '\'' +
        '}';
  }

  @Override
  public boolean equals(Object pO)
  {
    if (this == pO) return true;
    if (pO == null || getClass() != pO.getClass()) return false;
    Person person = (Person) pO;
    return age == person.age && name.equals(person.name);
  }

  @Override
  public int hashCode()
  {
    int result = name.hashCode();
    result = 31 * result + age;
    return result;
  }

  /**
   * Prüft, ob ein String null oder leer ist.
   * Wenn dies der Fall ist, wird das Programm mit einer Exception beendet.
   *
   * @param pToCheck der String, welcher überprüft werden soll
   * @return der überprüfte String
   */
  private String _checkNullEmpty(String pToCheck)
  {
    if (pToCheck == null || pToCheck.isEmpty())
      throw new IllegalArgumentException("argument must not be empty!");
    return pToCheck;
  }
}
