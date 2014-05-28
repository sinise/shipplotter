import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;


public class Main {
  public static void main(String[] args) throws Exception {


  ArrayList<String> list = new ArrayList<String>();
  URL oracle = new URL("http://www.marinetraffic.com/dk/ais/index/positions/all/mmsi:230964000/shipname:FUTURA");
  BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));

  CharSequence ch = "<a class=\"thumbnailContainerInner\" href=";
  String inputLine;
  String inputLine2;


  while ((inputLine = in.readLine()) != null) {
//    String curentLine = inputLine;
//    boolean contain = (curentLine.contains(ch));
    if (inputline.contains(ch)) {
      list.add(curentLine);
//      System.out.println(curentLine);
    }
  }

  int count = 0;
  while (true) {
    BufferedReader in2 = new BufferedReader(new InputStreamReader(oracle.openStream()));
    count++;

    try {
      Thread.sleep(10000L);    // 4 second
    }
      catch (Exception e) {}     // this never happen... nobody check for it
    while ((inputLine2 = in2.readLine()) != null) {
      String curentLine = inputLine2;
      boolean contain = (curentLine.contains(ch));
      if (contain) {
 
        if (!list.contains(curentLine)) {
          list.add(curentLine);
          System.out.printf("%d \n", count);
          System.out.println("   ");
          System.out.println(curentLine);
          System.out.println("\n/n/N");
          while (nBytesRead != -1) {
            nBytesRead = audio.read(abData, 0, abData.length);
          if (nBytesRead >= 0) {
            auline.write(abData, 0, nBytesRead);
          }
          }
        }
      }
    }
  }

}
}
