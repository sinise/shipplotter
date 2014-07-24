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
    if (args.length != 2) {
      System.out.println("Commandline argument must be a file with ships followed by the speedTreshold. See MakeKmlFromFile.java for more info");
      return;
    }
    try {
    String file = args[0];
    float speedTreshold = Float.valueOf(args[1]);
//    String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and mmsi = ? ORDER BY timestamp";
    String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and timestamp < ? ORDER BY timestamp";
    String timestamp = "1404086760999";
    String ship;
    FileInputStream fstream = new FileInputStream(file);
    DataInputStream fin = new DataInputStream(fstream);
    BufferedReader in = new BufferedReader(new InputStreamReader(fin));

      //For each ship create kmlfiles
    while ((ship = in.readLine()) != null) {
      System.out.println("start whileloop");

//      System.in.read();
      int filePrefix = 0;
      String[] newShip = ship.split(",");
      String mmsi = newShip[0];
      String name = newShip[1];
      Filter myFilter = new Filter(sql, mmsi, timestamp);
      System.out.println("createt filter");

      for(int i = 0; i < myFilter.results.size(); i++) {

        String filname = name + "-" + mmsi + "-" + filePrefix;
        GenKml.makeKml(myFilter.results.get(i), filname, speedTreshold);

        filePrefix++;

      }
    }
    } catch (Exception e) {
        System.out.println(e);
      }
  }
}
