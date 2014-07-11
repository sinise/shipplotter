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
  /**
  * Update filter reultset rs object with sql statement and filtered values
  * @sql the sqlstring as prepared statement with 3 values
  * @mmsi the mmsi value
  * @timestamp get positions no later than the given timestamp.
  */
	public Filter(String sql, String mmsi, String timestamp) {
    try {
      DB DB = new DB();
      System.out.println("1 filter");
      DB.UpdateSQL(sql, mmsi, mmsi, timestamp);

      System.out.println("2 filter");
      int count = 0;
      int lastTime = -1;
      while(DB.rs.next()){
        if (lastTime == -1) {
          lastTime = DB.rs.getInt("timestamp");
        }

        int diff = DB.rs.getInt("timestamp") - lastTime;
        if (diff > 3000 && validPosition(DB.rs.getFloat("lat"), DB.rs.getFloat("lon"))) {
          DB thisDB = new DB();
          thisDB.UpdateSQL("SELECT * FROM shipplotter WHERE mmsi = ? and timestamp > ? and timestamp < ? ORDER BY timestamp",
                            mmsi, Long.toString(lastTime - 18000), "" + (lastTime + 86000));
          results.add(thisDB.rs);
          System.out.printf("added one resultset. resultets = %d\n", results.size());
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
    if(lat > sss && lat < nnn && lon < eee & lon > www) {
      return true;
    }
    //Check if position is valid for area Køge bugt
    if(lat > sss && lat < nnn && lon < eee & lon > www) {
      return true;
    }

  }
}
