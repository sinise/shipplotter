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
  */
	public Filter(String sql, String mmsi) {
    try {
      DB DB = new DB();
          System.out.println("1 filter");

      DB.UpdateSQL(sql, mmsi, mmsi, mmsi);
          System.out.println("2 filter");

      int lastTime = -1;
      while(DB.rs.next()){
  		  if (lastTime == -1) {
          lastTime = DB.rs.getInt("timestamp");
          System.out.printf("2,5 filter, " + lastTime);
          System.out.println("");

          System.out.println("3 filter");

        }
          int diff = lastTime - DB.rs.getInt("timestamp");
          System.out.println("diff is" + lastTime);

        if (diff > 1) {
          DB thisDB = new DB();
          System.out.println("4 filter");

          thisDB.UpdateSQL("SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and mmsi= ? ORDER BY timestamp", mmsi, mmsi, mmsi);
          System.out.println("5 filter");

//                            mmsi, Long.toString(lastTime - 10000), "" + (lastTime + 1000000));
          results.add(thisDB.rs);
          System.out.printf("added one resultset. resultets = %D", results.size());
          lastTime = DB.rs.getInt("timestamp");
        }
      }
    } catch (Exception e){
      System.out.println(e.getMessage());
      }

  }
}
