import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.text.SimpleDateFormat;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.OutputKeys;

//import com.google.marker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Filter {
	public ResultSet rs;
	public long timestamp;

  /**
  * Update filter reultset rs object with sql statement and filtered values
  */
	public void UpdateSQL(String sql, String mmsi) {
    DB DB = new DB();
    DB.UpdateSQL(sql, mmsi);
    rs = DB.rs;
    DB.close();
  }
/*			while(DB.rs.next()){
				count++;
				KML.timestamp = DB.rs.getLong("timestamp");
				KML.id = DB.rs.getInt("mmsi");
				KML.name = DB.rs.getString("name");
				KML.dest = DB.rs.getString("dest");
				KML.lat = DB.rs.getFloat("lat");
				KML.lng = DB.rs.getFloat("lon");
				KML.type = DB.rs.getString("type");

				String formatedTime = format.format(KML.timestamp * 1000);

		} catch (Exception e){
			System.out.println(e.getMessage());
		}
*/

}
