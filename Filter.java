import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.lang.String;
public class Filter {
	public long timestamp;
  public ArrayList<ResultSet> results = new ArrayList<ResultSet>();
  public ArrayList<Integer> resultsDiff = new ArrayList<Integer>();
  /**
  * Update filter reultset rs object with sql statement and filtered values
  * @sql the sqlstring as prepared statement with 3 values
  * @mmsi the mmsi value
  * @timestamp get positions no later than the given timestamp.
  */
	public Filter(String sql, String mmsi, String timestamp) {
    try {
      DB DB = new DB();
      DB.UpdateSQL(sql, mmsi, mmsi, timestamp);
      int count = 0;
      int lastTime = -1;
      while(DB.rs.next()){
        if (lastTime == -1) {
          lastTime = DB.rs.getInt("timestamp");
        }

        int diff = DB.rs.getInt("timestamp") - lastTime;
        if (diff > 5400) {
          if (validPosition(DB.rs.getFloat("lat"), DB.rs.getFloat("lon"))) {
            DB thisDB = new DB();
            thisDB.UpdateSQL("SELECT * FROM shipplotter WHERE mmsi = ? and timestamp > ? and timestamp < ? ORDER BY timestamp",
                              mmsi, Long.toString(lastTime - 3600), "" + (lastTime + diff + 3600));
            results.add(thisDB.rs);
            resultsDiff.add(diff/60);
            System.out.printf("added one resultset. resultets = %d\n", results.size());
          }
        }
        lastTime = DB.rs.getInt("timestamp");
      }
    }
    catch (Exception e){
      System.out.println(e.getMessage());
    }
  }
  public boolean validPosition(float lat, float lon) {
    //check if position is valid for area kategat
    if(lat > 55.71 && lat < 56.09 && lon < 12.68 & lon > 12.51) {
      return true;
    }
    //Check if position is valid for area Køge bugt
    if(lat > 55.4 && lat < 55.54 && lon < 12.69 & lon > 12.21) {
      return true;
    }
    else {
      System.out.println("Position outside area, skiping this file");

      return false;}

  }
}
