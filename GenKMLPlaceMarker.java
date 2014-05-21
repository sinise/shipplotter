import java.io.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

//import com.google.marker;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class GenKMLPlaceMarker {
	public int id;
	public String name;
	public String address;
	public float lat;
	public float lng;
	public String type;

	public static void main(String[]args ){
		Statement stmt;
		ResultSet rs;
		GenKMLPlaceMarker KML = new GenKMLPlaceMarker();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
			String url = "jdbc:mysql://localhost/shipplotter";
			Connection con = DriverManager.getConnection(url, "shipplotter", "shipplotter");

			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			TransformerFactory tranFactory = TransformerFactory.newInstance(); 
		    Transformer aTransformer = tranFactory.newTransformer(); 

			Document doc = builder.newDocument();
			Element root = doc.createElement("kml");
			root.setAttribute("xmlns", "http://earth.google.com/kml/2.1");
			doc.appendChild(root);
			Element dnode = doc.createElement("Document");
			root.appendChild(dnode);
			
			Element rstyle = doc.createElement("Style");
			rstyle.setAttribute("id", "restaurantStyle");
			Element ristyle = doc.createElement("IconStyle");
			ristyle.setAttribute("id", "restaurantIcon");
			Element ricon = doc.createElement("Icon");
			Element riconhref = doc.createElement("href");
			riconhref.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pal2/icon63.png"));
			rstyle.appendChild(ristyle);
			ricon.appendChild(riconhref);
			ristyle.appendChild(ricon);
			dnode.appendChild(rstyle);
			
			Element bstyle = doc.createElement("Style");
			bstyle.setAttribute("id", "barStyle");
			Element bistyle = doc.createElement("IconStyle");
			bistyle.setAttribute("id", "barIcon");
			Element bicon = doc.createElement("Icon");
			Element biconhref = doc.createElement("href");
			biconhref.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pal2/icon27.png"));
			bstyle.appendChild(bistyle);
			bicon.appendChild(biconhref);
			bistyle.appendChild(bicon);
			dnode.appendChild(bstyle);
			
			stmt = con.createStatement();
			rs = stmt.executeQuery("SELECT * FROM shipplotter WHERE mmsi=219000062");
			while(rs.next()){
				KML.id = rs.getInt("mmsi");
				KML.name = rs.getString("name");
				KML.address = rs.getString("dest");
				KML.lat = rs.getFloat("lat");
				KML.lng = rs.getFloat("lon");
				KML.type = rs.getString("type");

				Element placemark = doc.createElement("Placemark");
				dnode.appendChild(placemark);
				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(KML.name));
				placemark.appendChild(name);
				Element descrip = doc.createElement("description");
				descrip.appendChild(doc.createTextNode(KML.address));
				placemark.appendChild(descrip);
				Element styleUrl = doc.createElement("styleUrl");
				styleUrl.appendChild(doc.createTextNode( "#" + KML.type+ "Style"));
				placemark.appendChild(styleUrl);
				Element point = doc.createElement("Point");
				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(KML.lng+ "," + KML.lat));
				point.appendChild(coordinates);
				placemark.appendChild(point);
			}
			Source src = new DOMSource(doc);
			Result dest = new StreamResult(new File("PlaceMarkers.kml")); 
			aTransformer.transform(src, dest);
			System.out.println("Completed.....");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
