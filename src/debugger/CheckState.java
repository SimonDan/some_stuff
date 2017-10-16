package debugger;

import debugger.util.Person;

/**
 * Einführung: Zustände prüfen.
 *
 * @author Simon Danner, 16.10.2017
 */
public class CheckState
{
  private static int someNumber = 5;
  private static String someText = "checkMyState";
  private final static Person somePerson = new Person("Lisa Maier", 23, "Am Lurzenhof 1 84036 Landshut",
                                                      Person.HANS_DAMPF, Person.MAX_MUSTER);

  public static void main(String[] pArgs)
  {
    someNumber += 10;
    System.out.println(someNumber);
    System.out.println(someText);

    for (Person person : somePerson.getFriends())
      System.out.println(person.getName());

    doSomething();
    System.out.println(somePerson.getAge());
  }

  private static void doSomething()
  {
    someNumber *= 2;
    someText = someText.toUpperCase();
  }
}
