package activetrip;

import android.content.Context;

import java.io.*;
import java.util.Properties;

/**
 * Hilfsklasse zur Ermittlung von Konfiguartions-Werten dieser Applikation.
 * (siehe Assets)
 *
 * @author Simon Danner, 18.06.2017
 */
public final class Config
{
  private static final String CONFIG_NAME = "config.properties";
  private final Properties properties = new Properties();

  public static Config create(Context pContext)
  {
    return new Config(pContext); //Oder als Singleton?
  }

  private Config(Context pContext)
  {
    try
    {
      //Wir nehmen an, die Datei liegt direkt im Root-Assets-Verzeichnis
      properties.load(pContext.getAssets().open(CONFIG_NAME));
    }
    catch (IOException pE)
    {
      throw new RuntimeException(pE);
    }
  }

  public int getRadius()
  {
    return Integer.parseInt(properties.getProperty("RADIUS"));
  }

  public String someProperty()
  {
    return properties.getProperty("SOMETHING");
  }

  /*
   * AB HIER ZU JEDER CONFIG-PROPERTY EINE METHODE
   */
}
