import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
//import java.sql.*;
import java.text.SimpleDateFormat;

public class MarineTraficCrawler {
  public static void main(String[] args) throws Exception {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    ArrayList<String> list = new ArrayList<String>();
//  BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

    URL url;
    URLConnection uc;

    String urlString = "http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:230964000/shipname:FUTURA/per_page:3000/page:1";
    String regEx = "<td><span>";
    String regStartLine = "<tr><td><span>";
//    System.out.println("Getting content for URl : " + urlString);
    url = new URL(urlString);
    uc = url.openConnection();
    uc.addRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.0)");
    uc.connect();
    uc.getInputStream();
    BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
    String ch;
    String parsedData = "";

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

//          parsedData = ch;
        }
        else {
          parsedData = parsedData + ("," + ch.substring(indexStart, indexEnd));
//          parsedData = parsedData + ("," + ch);
        }

      }
//      System.out.print(parsedData);
    }
    for (int i = 0; i < list.size(); i++) {
      System.out.println(list.get(i));
    }
    System.out.printf("listen er %d lanf", list.size());
}
}
