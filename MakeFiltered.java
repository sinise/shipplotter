import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class MakeFiltered {
  public int id;
  public String name;
  public String dest;
  public float lat;
  public float lng;
  public String type;
  public long timestamp;

  public static void main(String[] args){
    if (args.length != 1) {
      System.out.println("Commandline argument must be a mmsi");
      return;
    }
    try {
    String file = args[0];
//    String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and mmsi = ? ORDER BY timestamp";
    String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and timestamp < ? ORDER BY timestamp";
    String timestamp = "1404086760";
    String ship;
    FileInputStream fstream = new FileInputStream(file);
    DataInputStream fin = new DataInputStream(fstream);
    BufferedReader in = new BufferedReader(new InputStreamReader(fin));

      //For each ship create kmlfiles
    while ((ship = in.readLine()) != null) {
      System.in.read();
      int filePrefix = 0;
      String[] newShip = ship.split(",");
      String mmsi = newShip[0];
      String name = newShip[1];
      Filter myFilter = new Filter(sql, mmsi, timestamp);
      for(int i = 0; i < myFilter.results.size(); i++) {
        String filname = "-" + filePrefix + "-" + mmsi;
        GenKml.makeKml(myFilter.results.get(i), filname);
        filePrefix++;
      }
    }
    } catch (Exception e) {
        System.out.println(e);
      }
  }
}
