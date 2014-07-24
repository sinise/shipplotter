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
  private String urlString;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  private ArrayList<String> list = new ArrayList<String>();
  private BufferedReader in;
  public int sourceType;
  public String mmsi;
  public String name;
  public String type;
  private String file;
  private String cookie;
  private String htmlName;
  private int page;
  private int lastPage;
  private int start;
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
        htmlName = name.replace(" ", "%20"); 
        urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:" + mmsi +"/shipname:" + htmlName + "/per_page:50/page:1";
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
     *@param file is not used for anything so just type something
     *@param cookie the txt file containing the cookie
     */
    public MarinetraficShip(String mmsi, String name, String type, String start, String cookie) {
      try {
        this.mmsi = mmsi;
        this.name = name;
        this.type = type;
        this.cookie = cookie;
        this.start = Integer.parseInt(start);
        sourceType = 2;
        htmlName = name.replace(" ", "%20"); 
      }
      catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    }

    /**
     *Constructor for a Marinetrafic ship to fetch from url with login
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
      if (sourceType == 2) {
        System.out.println("sourcetype er 2");

        urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:" + mmsi +"/shipname:" + htmlName + "/per_page:50/page:" + page;
        url = new URL(urlString);
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:26.0) Gecko/20100101 Firefox/26.0");
        uc.setRequestProperty("Cookie", "AUTH=EMAIL=tony.sadownichik@greenpeace.org&CHALLENGE=US1KIfRUfmcsKeERcCip; mt_user[User][ID]=Q2FrZQ%3D%3D.f0rvCaXH");
        uc.connect();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        String ch;
        String regEx = "<span>1 of";
        while ((ch = in.readLine()) != null) {
          if (ch.contains(regEx)) {
            int indexEnd = ch.lastIndexOf("</span>");
            int  indexStart = ch.indexOf("<span>") + 11;
            lastPage = Integer.parseInt(ch.substring(indexStart, indexEnd));
            System.out.println(lastPage);
          }
        }
        page = start;
        for(int i = 0; page < lastPage + 1; i++) {
          try{
            urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:" + mmsi +"/shipname:" + htmlName + "/per_page:50/page:" + page;
            url = new URL(urlString);
            uc = url.openConnection();
            uc.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:26.0) Gecko/20100101 Firefox/26.0");
            uc.setRequestProperty("Cookie", "AUTH=EMAIL=tony.sadownichik@greenpeace.org&CHALLENGE=US1KIfRUfmcsKeERcCip; mt_user[User][ID]=Q2FrZQ%3D%3D.f0rvCaXH");
            uc.connect();
            in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            trimData();
            System.out.println(" " + page + " of " + lastPage);
            page++;
          }
            catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
          }
        }
      }
      if (sourceType == 0) {
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:26.0) Gecko/20100101 Firefox/26.0");
        uc.connect();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
        trimData();

      }
      if (sourceType == 1) {
        System.out.printf("feching from file %s\n", name);
        FileInputStream fstream = new FileInputStream(file);
        DataInputStream fin = new DataInputStream(fstream);
        in = new BufferedReader(new InputStreamReader(fin));
        trimData();
      }
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

// trim the data and put in the array list which can be acceced from other classes
  public void trimData() {
    try{
      String ch;
      String parsedData = "";
      String regEx = "<td><span>";
      String regStartLine = "<tr><td><span>";
      while ((ch = in.readLine()) != null) {
//        System.out.println(ch);
        if (ch.contains(regEx)) {
          int indexEnd = ch.lastIndexOf("</span>");
          int  indexStart = ch.indexOf("<span>") + 6;
          if (ch.contains(regStartLine)) {
            if (parsedData.length() > 20){
//           		System.out.println(parsedData);
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
      System.out.print(name);
      System.out.printf("Fetched %d positions", list.size());
      in.close();
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
