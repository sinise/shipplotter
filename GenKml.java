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

public class GenKml {
	public int id;
	public String name;
	public String dest;
	public float lat;
	public float lng;
	public String type;
	public long timestamp;

	public static void makeKml(ResultSet rs, String filename, String speedTreshold){
        int filePrefix = 0;
//    ResultSet rs = args[0];
//    String filname = args[1];

		GenKMLPlaceMarker KML = new GenKMLPlaceMarker();
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
		try {

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

			int count = 0;
			while(rs.next()){
				count++;
					System.out.printf("lines %d\n "+filename, count);
				KML.timestamp = rs.getLong("timestamp");
				KML.id = rs.getInt("mmsi");
				KML.name = rs.getString("name");
				KML.dest = rs.getString("dest");
				KML.lat = rs.getFloat("lat");
				KML.lng = rs.getFloat("lon");
				KML.type = rs.getString("type");

				String formatedTime = format.format(KML.timestamp * 1000);

				Element placemark = doc.createElement("Placemark");
				dnode.appendChild(placemark);

				Element name = doc.createElement("name");
				name.appendChild(doc.createTextNode(KML.name));
				placemark.appendChild(name);

				Element descrip = doc.createElement("description");
				descrip.appendChild(doc.createTextNode(KML.dest));
				placemark.appendChild(descrip);

				Element styleUrl = doc.createElement("styleUrl");
				styleUrl.appendChild(doc.createTextNode( "#" + KML.type+ "Style"));
				placemark.appendChild(styleUrl);

				Element TimeStamp = doc.createElement("TimeStamp");
				Element when = doc.createElement("when");
				when.appendChild(doc.createTextNode(formatedTime + ""));
				TimeStamp.appendChild(when);
				placemark.appendChild(TimeStamp);

				Element point = doc.createElement("Point");
				Element coordinates = doc.createElement("coordinates");
				coordinates.appendChild(doc.createTextNode(KML.lng+ "," + KML.lat));
				point.appendChild(coordinates);
				placemark.appendChild(point);
				if(count % 10000 == 0) {
					System.out.printf("lines %d\n", count);
					Source src = new DOMSource(doc);
					Result dest = new StreamResult(new File(filename + "-" + filePrefix + ".kml"));
					filePrefix++;
					aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
					aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
					aTransformer.transform(src, dest);

					doc = builder.newDocument();
					root = doc.createElement("kml");
					root.setAttribute("xmlns", "http://earth.google.com/kml/2.1");
					doc.appendChild(root);
					dnode = doc.createElement("Document");
					root.appendChild(dnode);

					rstyle = doc.createElement("Style");
					rstyle.setAttribute("id", "restaurantStyle");
					ristyle = doc.createElement("IconStyle");
					ristyle.setAttribute("id", "restaurantIcon");
					ricon = doc.createElement("Icon");
					riconhref = doc.createElement("href");
					riconhref.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pal2/icon63.png"));
					rstyle.appendChild(ristyle);
					ricon.appendChild(riconhref);
					ristyle.appendChild(ricon);
					dnode.appendChild(rstyle);
					bstyle = doc.createElement("Style");
					bstyle.setAttribute("id", "barStyle");

					bistyle = doc.createElement("IconStyle");
					bistyle.setAttribute("id", "barIcon");
					bicon = doc.createElement("Icon");
					biconhref = doc.createElement("href");
					biconhref.appendChild(doc.createTextNode("http://maps.google.com/mapfiles/kml/pal2/icon27.png"));
					bstyle.appendChild(bistyle);
					bicon.appendChild(biconhref);
					bistyle.appendChild(bicon);
					dnode.appendChild(bstyle);
				}
			}

			Source src = new DOMSource(doc);
			Result dest = new StreamResult(new File(filename + "-" + filePrefix + ".kml")); 
			aTransformer.setOutputProperty(OutputKeys.INDENT, "yes");
			aTransformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
			aTransformer.transform(src, dest);
			System.out.println("Completed.....");
		} catch (Exception e){
			System.out.println(e.getMessage());
		}
	}
}
