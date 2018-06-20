package httpfiledownloader;

import java.io.*;
import java.net.*;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.*;

/**
 * A file downloader for remote http/https webservers.
 * All content will be parsed and downloaded.
 *
 * @author Simon Danner, 16.06.2018
 */
public class WebserverFileDownloader
{
  private static final Pattern LINK_PATTERN = Pattern.compile("(?i)<a([^>]+)>(.+?)</a>");
  private static final Pattern HREF_PATTERN = Pattern.compile("\\s*(?i)href\\s*=\\s*(\"([^\"]*\")|'[^']*'|([^'\">\\s]+))");
  private static final Set<String> VISITED_URLS = new HashSet<>();

  /**
   * Entry-Point.
   *
   * @param pArgs the first argument should be the root URL to start from
   *              the second argument should be the destination path on your filesystem to store the files
   *              (optional) the third argument can be a username for basic auth
   *              (optional) the fourth argument can be a password for basic auth
   */
  public static void main(String[] pArgs)
  {
    if (!(pArgs.length == 2 || pArgs.length == 4) || pArgs[0] == null || pArgs[0].isEmpty() || pArgs[1] == null || pArgs[1].isEmpty())
      throw new RuntimeException("Invalid start arguments! Provide a root URL and a destination path!");

    if (!pArgs[0].startsWith("http") && !pArgs[0].startsWith("https"))
      throw new RuntimeException("Only http and https are allowed as protocol for the remote URL!");

    if (pArgs.length == 4)
    {
      if (pArgs[2] == null || pArgs[2].isEmpty() || pArgs[3] == null || pArgs[3].isEmpty())
        throw new RuntimeException("A username and password must be set properly, when using four arguments!");
      _setCredentials(pArgs[2], pArgs[3]);
    }

    long start = System.currentTimeMillis();
    System.out.println("START DOWNLOADING....");
    _downloadAndFindMore(pArgs[0], Paths.get(pArgs[1]));
    long duration = System.currentTimeMillis() - start;
    System.out.printf("FINISHED! Duration: %d min, %d sec",
                      TimeUnit.MILLISECONDS.toMinutes(duration),
                      TimeUnit.MILLISECONDS.toSeconds(duration) -
                          TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration))
    );
  }

  /**
   * Downloads the file behind the URL or parses the HTML directory for sub directories.
   *
   * @param pUrl             the URL to download from
   * @param pRootDestination the local root destination to store the files
   */
  private static void _downloadAndFindMore(String pUrl, Path pRootDestination)
  {
    if (!VISITED_URLS.add(pUrl)) //avoid cycles
      return;
    final Path destination = _relativize(pUrl, pRootDestination);
    if (destination == null)
      return;
    try
    {
      final URL url = new URL(pUrl);
      final String contentType = url.openConnection().getContentType();
      if (contentType != null && !contentType.contains("html"))
      {
        if (!Files.exists(destination))
          _downloadFile(url, destination);
      }
      else
      {
        final String content = _urlContentToString(pUrl);
        if (content == null)
          return;
        if (!url.getFile().contains(".")) //Parse directories of the webserver only
          _findContent(content, pUrl).forEach(pNewUrl -> _downloadAndFindMore(pNewUrl, pRootDestination));
      }
    }
    catch (IOException pE)
    {
      throw new RuntimeException("Unable to store downloaded file on your filesystem!", pE);
    }
  }

  /**
   * Parses a HTML document for sub directories.
   * It will search for all '<a href=...".
   *
   * @param pContent     the content of the HTML document
   * @param pOriginalUrl the original URL of the document
   * @return a list of URLs (sub directories)
   */
  private static List<String> _findContent(String pContent, String pOriginalUrl)
  {
    final List<String> urls = new ArrayList<>();
    final Matcher linkMatcher = LINK_PATTERN.matcher(pContent);
    while (linkMatcher.find())
    {
      final Matcher hrefMatcher = HREF_PATTERN.matcher(linkMatcher.group(1));
      while (hrefMatcher.find())
      {
        String hrefContent = hrefMatcher.group(1);
        if (hrefContent.startsWith("\"") && hrefContent.endsWith("\""))
          hrefContent = hrefContent.substring(1, hrefContent.length() - 1);
        if (!hrefContent.startsWith("?")) //Ignore param urls
        {
          String newUrl = pOriginalUrl;
          if (!newUrl.endsWith("/"))
            newUrl += "/";
          if (hrefContent.startsWith("/"))
            hrefContent = hrefContent.substring(1);
          urls.add(newUrl + hrefContent);
        }
      }
    }
    return urls;
  }

  /**
   * Reads the content of a file behind a URL into a string.
   *
   * @param pUrl the URL of the file
   * @return the content of the file as string
   */
  private static String _urlContentToString(String pUrl)
  {
    try (Scanner scanner = new Scanner(new URL(pUrl).openStream(), StandardCharsets.UTF_8.toString()))
    {
      scanner.useDelimiter("\\A");
      return scanner.hasNext() ? scanner.next() : "";
    }
    catch (FileNotFoundException pE)
    {
      return null;
    }
    catch (IOException pE)
    {
      throw new RuntimeException("Cannot read content from URL! URL: " + pUrl, pE);
    }
  }

  /**
   * Downloads a file behind a URL to a local destination.
   * It will create all parent directories.
   *
   * @param pUrl         the URL to download from
   * @param pDestination the local destination for the file
   */
  private static void _downloadFile(URL pUrl, Path pDestination)
  {
    try
    {
      ReadableByteChannel rbc = Channels.newChannel(pUrl.openStream());
      Files.createDirectories(pDestination.getParent());
      Files.createFile(pDestination);
      FileChannel.open(pDestination, StandardOpenOption.WRITE).transferFrom(rbc, 0, Long.MAX_VALUE);
    }
    catch (IOException pE)
    {
      throw new RuntimeException("Unable to download file! URL: " + pUrl.toString(), pE);
    }
  }

  /**
   * Relativizes the local destination path based on the remote URL.
   * Example:
   * URL: http://someURL.com/some/sub/directory/
   * Root-path: C:/Users/Name/Downloads
   * Relativized path: C:/Users/Name/Downloads/some/sub/directory
   *
   * @param pUrl      the remote URL
   * @param pRootPath the root path of the destination
   * @return the relativized path
   */
  private static Path _relativize(String pUrl, Path pRootPath)
  {
    int indexOfProtocol = pUrl.indexOf("//") + 2;
    int startIndex = pUrl.indexOf("/", indexOfProtocol);
    try
    {
      return Paths.get(pRootPath.toString() + (startIndex == -1 ? "" : pUrl.substring(startIndex)));
    }
    catch (InvalidPathException pE)
    {
      return null;
    }
  }

  /**
   * Sets the basic auth credentials for the connections.
   *
   * @param pUsername the user name
   * @param pPassword the password
   */
  private static void _setCredentials(String pUsername, String pPassword)
  {
    Authenticator.setDefault(new Authenticator()
    {
      protected PasswordAuthentication getPasswordAuthentication()
      {
        return new PasswordAuthentication(pUsername, pPassword.toCharArray());
      }
    });
  }
}
