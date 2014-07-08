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
		int filePrefix = 0;
    if (args.length != 1) {
      System.out.println("Commandline argument must be a mmsi");
      return;
    }
    String mmsi = args[0];
    String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and mmsi = ? ORDER BY timestamp";
    Filter myFilter = new Filter(sql, mmsi);

    for(int i = 0; i < myFilter.results.size(); i++) {
      String filname = filePrefix + "-" + mmsi;
      GenKml.makeKml(myFilter.results.get(i), filname);
      filePrefix++;
    }
	}
}
