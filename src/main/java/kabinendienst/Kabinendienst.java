package kabinendienst;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Autor: Simon
 */
public class Kabinendienst
{
  private static List<String> names;
  private static Date start;
  private static int duration, teamSize;
  private static BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
  private static SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

  public static void main(String[] args)
  {
    System.out.println("Willkommen beim Kabinendient-Plan Generator");
    readTimezone();
    readNames();
    try
    {
      buildList();
    }
    catch (Exception e)
    {
      e.printStackTrace();
      System.out.println("CRITICAL ERROR: Unerwarteter Fehler beim Schreiben! -> ABBRUCH!");
      return;
    }
    System.out.println("Liste erfolgreich erzeugt!");
  }

  private static void readTimezone()
  {
    //Startdatum
    System.out.println("Gib zuerst das Start-Datum der Liste ein! (Format: dd.mm.yyyy)");
    do
    {
      start = _checkDate();
    }
    while(start == null);
    //Rhythmus
    System.out.println("Gib jetzt an nach wievielen Tagen gewechselt werden soll (Erlaubter Bereich: 1 - 999)");
    do
    {
      duration = _checkDuration();
    }
    while (duration == 0);
  }

  private static void readNames()
  {
    System.out.println("Gib jetzt an, ob du die Spielernamen aus einer Datei einlesen willst oder ob du sie eingeben willst (1 oder 2)");
    int mode;
    do
    {
      mode = _checkMode();
    }
    while (mode == 0);

    if (mode == 1)
      names = _readNamesFromFile();
    else
      names = _inputNames();

    System.out.println("Gib jetzt noch an wieviele Person für einen Zeitraum zuständig sind. (Bereich 1 - " + names.size() + ")");
    do
    {
      teamSize = _checkTeamSize(names.size());
    }
    while (teamSize == 0);
  }

  private static void buildList() throws IOException
  {
    System.out.println("Liste wird jetzt generiert...");
    System.out.println("Du musst jetzt nur noch den Pfad bzw. den Namen der Datei angeben, wo die Liste gespeichert werden soll");
    File target;
    do
    {
      target = _checkPathToFile();
    }
    while(target == null);

    Iterator<String> nameIterator = names.iterator();
    FileWriter writer = new FileWriter(target, false);

    writer.write("KABINENDIENST");
    writer.write(ln() + ln());

    while(nameIterator.hasNext())
    {
      Date endOfDuration = _calculateEnd();
      String line = dateFormat.format(start) + " bis " + dateFormat.format(endOfDuration) + ": ";
      String team = "";
      for (int i = 0; i < teamSize; i++)
        if (nameIterator.hasNext())
          team += nameIterator.next() + ", ";
      team = team.substring(0, team.length() - 2);
      line += team;
      writer.write(line + ln() + ln());
      _calculateNewDate();
    }

    writer.write(ln() + "...generated on " + new Date() + ln());
    writer.flush();
  }

  private static Date _checkDate()
  {
    try
    {
      return dateFormat.parse(console.readLine());
    }
    catch (Exception e)
    {
      System.out.println("ERROR: Falsches Format! Nochmal...");
      return null;
    }
  }

  private static int _checkDuration()
  {
    try
    {
      int dur = Integer.parseInt(console.readLine());
      if (dur < 1 || dur > 999)
      {
        System.out.println("ERROR: Außerhalb des Bereichs! Nochmal...");
        return 0;
      }
      return dur;
    }
    catch (Exception e)
    {
      System.out.println("ERROR: Falsches Format! Nochmal...");
      return 0;
    }
  }

  private static int _checkMode()
  {
    try
    {
      int mode = Integer.parseInt(console.readLine());
      if (mode != 1 && mode != 2)
      {
        System.out.println("ERROR: Nur 1 oder 2 möglich! Nochmal...");
        return 0;
      }
      return mode;
    }
    catch (Exception e)
    {
      System.out.println("ERROR: Falsches Format! Nochmal...");
      return 0;
    }
  }

  private static List<String> _readNamesFromFile()
  {
    List<String> names = new ArrayList<String>();
    System.out.println("Gib den Pfad zur Textdatei mit den Namen an. (Ein Name pro Zeile in Datei)");
    File file;
    do
    {
      file = _checkPathToFile();
    }
    while(file == null);

    try
    {
      BufferedReader in = new BufferedReader(new FileReader(file));
      String name;
      while ((name = in.readLine()) != null)
        names.add(name);
    }
    catch (Exception e)
    {
      System.out.println("CRITICAL ERROR: Unerwarteter Fehler beim Lesen aus der Datei -> ABBRUCH!");
      System.exit(0);
    }
    //Collections.sort(names);
    return names;
  }

  private static List<String> _inputNames()
  {
    List<String> names = new ArrayList<String>();
    String input;
    System.out.println("Gib jetzt nacheinander die Namen der Spieler ein. (Weiter: Enter, Fertig: fertig eingeben)");
    while (true)
    {
      try
      {
        input = console.readLine();
      }
      catch (Exception e)
      {
        System.out.println("ERROR: Falsche Eingabe! Nochmal...");
        continue;
      }
      if (input.equals("fertig"))
        return names;
      names.add(input);
    }
  }

  private static File _checkPathToFile()
  {
    try
    {
      return new File(console.readLine());
    }
    catch (Exception e)
    {
      System.out.println("ERROR: Pfad ungueltig! Nochmal...");
      return null;
    }
  }

  private static int _checkTeamSize(int maxSize)
  {
    try
    {
      int size = Integer.parseInt(console.readLine());
      if (size < 1 || size > maxSize)
      {
        System.out.println("ERROR: Außerhalb des Bereichs! Nochmal...");
        return 0;
      }
      return size;
    }
    catch (Exception e)
    {
      System.out.println("ERROR: Falsches Format! Nochmal...");
      return 0;
    }
  }

  private static void _calculateNewDate()
  {
    long durationMilli = duration * 24 * 3600 * 1000;
    long oldDateMilli = start.getTime();
    start = new Date(oldDateMilli + durationMilli);
  }

  private static Date _calculateEnd()
  {
    long durationMilli = (duration-1) * 24 * 3600 * 1000;
    long oldDateMilli = start.getTime();
    return new Date(oldDateMilli + durationMilli);
  }

  private static String ln()
  {
    return System.getProperty("line.separator");
  }
}
