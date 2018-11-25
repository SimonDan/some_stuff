package debugger.util;

import java.util.*;

/**
 * Mockup-Implementierug der DB-Schnittstelle.
 *
 * @author Simon Danner, 16.10.2017
 */
public class DBImpl implements IDatabase
{
  private final List<Person> data = new ArrayList<>();

  public DBImpl()
  {
    //Dummy-Data
    data.add(new Person("Dummy Name", 44, "Dummystadt"));
    data.add(new Person("", 12, "Alte Stadt", Person.HANS_DAMPF));
    Person manyFriends = new Person("Viele Freunde", 99, "Grosses Haus");
    Random random = new Random();
    for (int i = 0; i < 50; i++)
      manyFriends.addFriend(new Person("Person" + i, random.nextInt(100), "Adresse" + i));
    manyFriends.addFriend(new Person("Old Guy", 200, "Old Town"));
    data.add(manyFriends);
  }

  @Override
  public List<Person> readPersonsFromDB()
  {
    return Collections.unmodifiableList(data);
  }

  @Override
  public void addPerson(Person pPerson)
  {
    data.add(pPerson);
  }

  @Override
  public boolean removePerson(Person pPerson)
  {
    return data.remove(pPerson);
  }
}
