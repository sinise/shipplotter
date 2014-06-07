import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
//import java.sql.*;
import java.text.SimpleDateFormat;

public class MarineTraficCrawler {
  public static void main(String[] args) throws Exception {

    ArrayList<String> returnedList = fetchMarineTrafic(1);

    for (int i = 0; i < returnedList.size(); i++) {
     System.out.println(returnedList.get(i));
    }


  }
  /**
   * fetch content from Marinetrafic. either from a saved html file or from url
   * @param choice 0 to fecch online 1 to fetch from file
   */
  public static ArrayList<String> fetchMarineTrafic(int choice){
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    ArrayList<String> list = new ArrayList<String>();
    BufferedReader in;

    try{
      if (choice == 0) {
        URL url;
        URLConnection uc;
        String urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:230964000/shipname:FUTURA/per_page:3000/page:1";
        url = new URL(urlString);
        uc = url.openConnection();
        uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
        uc.connect();
        in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
      }
      else {
          FileInputStream fstream = new FileInputStream("l575.htm");
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
    return list;
  }
}
