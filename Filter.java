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

public class Filter {
	public ResultSet rs;
	public long timestamp;
  public ArrayList<> listStartTime= new ArrayList<int>();
  /**
  * Update filter reultset rs object with sql statement and filtered values
  */
	public void UpdateSQL(String sql, String mmsi) {
    DB DB = new DB();
    DB.UpdateSQL(sql, mmsi);
    rs = DB.rs;
    DB.close();
  }

  public void filter(ResultSet rawRs) {
    int lastTime = -1;
    while(rawRs.next()){
  		if (lastTime == -1) {
        lastTime = rawRs.getInt("timestamp");
      }
      if ((lastTime - rawRs.getInt("timestamp")) > 100000) {
        listStartTime.add(lastTime);
        lastTime = rawRs.getInt("timestamp");
      }
    }
  }
}
