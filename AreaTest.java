import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.ArrayList;
import java.text.SimpleDateFormat;

public class AreaTest {

  /**
   *  Construkter for AreaTest.
   */
  public AreaTest(){
    try {
      ArrayList<Polygon2D> legal = new ArrayList<Polygon2D>();
      ArrayList<Polygon2D> ilegal = new ArrayList<Polygon2D>();

      //get the arreas
      System.out.println("");
      String sql = "SELECT * FROM areas WHERE id > ? and id > ? and id > ? ORDER BY id";
      DB DB = new DB();
      String next;
      DB.UpdateSQL(sql, "0", "0", "0");

      while(DB.rs.next()){
        int typeCode = DB.rs.getInt("typeCode");
        String[] points =  DB.rs.getString("area").lineSplit(",");
        int nPoints = points.legnth;
        float[] xCords = new float[nPoints];
        float[] yCords = new float[nPoints];

        for(int i = 0; i < nPoints; i++){
          String[] point = points[i].lineSplit(" ");
          xCords[i] = point[0];
          yCords[i] = point[1];
        }
        if(typeCode == 0){
          legal.add(Polygon2D(yCords, xCords, nPoints));
        }
        if(typeCode == 1){
          ilegal.add(Polygon2D(yCords, xCords, nPoints));
        }
//        System.out.println(points);
      }
//      nPoints = points.length;
    } catch (Exception e) {
        System.out.println(e);
      }
 }

  /*
   * 
   */
  public boolean test(double xPoint, double yPoint){
    for(int i = 0; i < ilegal.size(); i++){
      if(ilegal.get(i).contains(xPoint, yPoint)){
        return false;
      }
    }
    for(int i = 0; i < legal.size(); i++){
      if(!(legal.get(i).contains(xPoint, yPoint))){
        return false;
      }
    }
    return true;
  }
}
