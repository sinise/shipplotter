import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

public class MakeKmlsFromFileList {
	public static void main(String[] args){
		int filePrefix = 0;
    if (args.length != 1) {
      System.out.println("Commandline argument must be a comma seperatet textfile in this format <mmsi>,<name>,...  were the dots can be any value. Only first to values in the file are used");
      return;
    }

    try {
      String file = args[0];
      String sql = "SELECT * FROM shipplotter WHERE mmsi = ? and mmsi = ? and mmsi = ? ORDER BY timestamp";
      FileInputStream fstream = new FileInputStream(file);
      DataInputStream fin = new DataInputStream(fstream);
      BufferedReader in = new BufferedReader(new InputStreamReader(fin));
      DB DB = new DB();
      //For each ship create kmlfiles
      while ((ship = in.readLine()) != null) {
        String[] newShip = ship.split(",");
        String mmsi = newShip[0];
        String name = newShip[1];
        DB.UpdateSQL(sql, mmsi, mmsi, mmsi);
        GenKml.makeKml(DB.rs, name);
      }
    }
	}
}
