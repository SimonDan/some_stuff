package debugger;

import debugger.util.*;

import java.util.List;

/**
 * Demonstration: Conditional Breakpoints
 *
 * @author Simon Danner, 16.10.2017
 */
public class SaveTime
{
  private static IDatabase database = new DBImpl(); //Schnittstelle zur Datenbank

  public static void main(String[] pArgs)
  {
    for (Person person : database.readPersonsFromDB())
    {
      List<Person> friends = person.getFriends();
      for (Person friend : friends)
        System.out.println(friend.getName() + " is a friend of " + person.getName() + ". " +
                               (PersonUtil.isTheOldest(friend, friends) ? " He is the oldest of his friends!" : ""));
    }
  }
}
