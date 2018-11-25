package debugger;

import debugger.util.Person;

import java.util.Random;

/**
 * Demonstration zur Evaluation von Ausdrücken zur Behebung eines Fehlers im Programm.
 *
 * @author Simon Danner, 16.10.2017
 */
public class OurFirstBug
{
  public static void main(String[] args)
  {
    System.out.println("Ein sehr aufwendiges Programm...");
    Random random = new Random();
    Person ourTestPerson = Person.MAX_MUSTER;
    //Unsere Person ist sehr beliebt -> Freunde hinzufügen
    for (int i = 0; i < 50; i++)
      ourTestPerson.addFriend(new Person("Person" + i, random.nextInt(100), "Adresse" + i));

    //Die Person muss entweder Freunde über 50 haben oder in Musterstadt wohnen, sonst haben wir ein Problem!
    if (ourTestPerson.getFriendsOlderThan(50).isEmpty() || !ourTestPerson.livesInCity("musterstadt"))
      throw new RuntimeException("A really critical error!!");

    System.out.println("Erfolg!");
  }
}
