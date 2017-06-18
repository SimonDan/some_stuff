package activetrip;

import android.content.Context;
import android.location.*;

import java.io.IOException;
import java.util.Locale;

/**
 * @author Simon Danner, 18.06.2017
 */
public final class LocationUtil
{
  private LocationUtil()
  {
  }

  public static String getCountry(Context pContext, Location pLocation)
  {
    Geocoder geocoder = new Geocoder(pContext, Locale.getDefault());
    try
    {
      Address address = geocoder.getFromLocation(pLocation.getLatitude(), pLocation.getLongitude(), 1).get(0);
      return address.getCountryName();
    }
    catch (IOException pE)
    {
      throw new RuntimeException(pE);
    }
  }
}
