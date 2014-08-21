import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class AreaTest {
//	public static void main(String[] args){
  public AreaTest();
    try {
      System.out.println("");
      String sql = "SELECT * FROM areas WHERE id > ? and id > ? and id > ? ORDER BY id";
      DB DB = new DB();
      String next;
      String[] points;
      int nPoints;
      DB.UpdateSQL(sql, "0", "0", "0");
      while(DB.rs.next()){
        DB.rs.getString("typeCode"));
        points = DB.rs.getString("area")).replace(" ", ",");
      }
        nPoints = points.lengt;
        

    } catch (Exception e) {
        System.out.println(e);
      }
 }
}
