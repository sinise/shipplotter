import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * fetch content from Marinetrafic. either from a saved html file or from url
 * @param choice 0 to fecch online 1 to fetch from file
 */

public class MarinetraficShip
{
  private URL url;
  private URLConnection uc;
  private String urlstring;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  public ArrayList<String> list = new ArrayList<String>();
  private BufferedReader in;
  public int sourceType;
  public String mmsi;
  public String name;
  private String file;
    /**
     *Constructor for a Marinetrafic ship to fetch from url
     *@param mmsi mmsi of ship
     *@param name name of ship entered exactly as marinetrafic does. is casesentitive
     */
    public MarinetraficShip(String mmsi, String name) {
      try {
        this.mmsi = mmsi;
        this.name = name;
        sourceType = 0;
        String urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:" + mmsi +"/shipname:" + name + "/per_page:5000/page:1";
        url = new URL(urlString);
      }
      catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    }

    /**
     *Constructor for a Marinetrafic ship to fetch from url
     *@param mmsi mmsi of ship
     *@param name name of ship entered exactly as marinetrafic does. is casesentitive
     *@param file file to fetch from
     */
    public MarinetraficShip(String file, String mmsi, String name) {
      this.mmsi = mmsi;
      this.name = name;
      this.file = file;
      sourceType = 1;
    }

  public void fetchData() {
    try {

      if (sourceType == 0) {
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.connect();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      }

      if (sourceType == 1) {
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream fin = new DataInputStream(fstream);
        in = new BufferedReader(new InputStreamReader(fin));
      }
      String ch;
      String parsedData = "";
      String regEx = "<td><span>";
      String regStartLine = "<tr><td><span>";
      while ((ch = in.readLine()) != null) {
        if (ch.contains(regEx)) {
          int indexEnd = ch.lastIndexOf("</span>");
          int  indexStart = ch.indexOf("<span>") + 6;
          if (ch.contains(regStartLine)) {
            list.add(parsedData);
            Date d = sdf.parse(ch.substring(indexStart, indexEnd));
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            long timestamp = c.getTimeInMillis();
            parsedData = parsedData.valueOf(timestamp);
          }
          else {
            parsedData = parsedData + ("," + ch.substring(indexStart, indexEnd));
          }
        }
      }
      System.out.printf("Fetched %d positions", list.size());
      in.close();
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
