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
  private ArrayList<String> list = new ArrayList<String>();
  private BufferedReader in;
  public int sourceType;
  public String mmsi;
  public String name;
  public String type;
  private String file;
    /**
     *Constructor for a Marinetrafic ship to fetch from url
     *@param mmsi mmsi of ship
     *@param name name of ship entered exactly as marinetrafic does. is casesentitive
     */
    public MarinetraficShip(String mmsi, String name, String type) {
      try {
        this.mmsi = mmsi;
        this.name = name;
        this.type = type;
        sourceType = 0;
        String htmlName = name.replace(" ", "%20"); 
        String urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:" + mmsi +"/shipname:" + htmlName + "/per_page:50/page:1";
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
    public MarinetraficShip(String mmsi, String name, String type, String file) {
      this.mmsi = mmsi;
      this.name = name;
      this.type = type;
      this.file = file;
      sourceType = 1;
    }

  public String[] getData() {
    int length = list.size();
    String[] returnArray = new String[length];
    for (int i = 0; i < length; i++) {
      returnArray[i] = list.get(i);
    }
    return returnArray;
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
        System.out.printf("feching from file %s\n", name);
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
            if (parsedData.length() > 20){
		System.out.println(parsedData);
              String[] lineSplit = parsedData.split(",");
              String time = lineSplit[0];
              String speed = lineSplit[2];
              String lon = lineSplit[3];
              String lat = lineSplit[4];
              String course = lineSplit[5];
              list.add(mmsi + "," + time.substring(0, 10) + ",0," + type +"," + lat + "," + lon + "," + speed + "," + course + ",0,0,0,0," +
                       name + ",0,0,0,0,0,0,0,0,0");
            }

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
      System.out.println(name);
      System.out.printf("Fetched %d positions", list.size());
      in.close();
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
