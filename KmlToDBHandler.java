import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
import java.text.SimpleDateFormat;

/**
 * fetch content from Marinetrafic. either from a saved html file or from url
 * @param choice 0 to fecch online 1 to fetch from file
 */

public class KmlToDBHandler
{
  private ArrayList<String> pointList = new ArrayList<String>();
  private BufferedReader in;
  public String name;
  public String type;
  private String file;
    /**
     *Constructor for a polygon in kml
     *@param file the kml file containing a polygon
     */
    public KmlToDBHandler(String file) {
      try {
        this.file = file;
      }
      catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
      }
    }

/*  public String[] getData() {
    int length = pointList.size();
    float[][] returnArray = new float[length][2];
    for (int i = 0; i < length; i++) {
      returnArray[i][i] = pointList.get(i);
    }
    return returnArray;
  }
*/
  public void fetchData() {
    try {
      System.out.printf("feching from kml file %s\n", file);
      FileInputStream fstream = new FileInputStream(file);
      DataInputStream fin = new DataInputStream(fstream);
      in = new BufferedReader(new InputStreamReader(fin));

      trimData();

    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }

// trim the data and put in the array list which can be acceced from other classes
  public void trimData() {
    try{
      String ch;
      String regExName = "<name>";
      String typeName = "null";
      String regP1 = "SimpleData name=\"";
      String regP3 = "<name>";
      //set typeName of area
      while((ch = in.readLine()) != null && typeName.equals("null")){
//          System.out.println(ch);
        if (ch.contains(regExName) && typeName == "null"){
          int indexStart = ch.indexOf("<name>" );
          int indexEnd = ch.lastIndexOf("</name>");
          typeName = ch.substring(indexStart + 6, indexEnd);
          System.out.println(typeName);
        }
      }

      //run true the file and extract polygons with their name
      while((ch = in.readLine()) != null) {

        if (ch.contains("<Placemark>")){
          String polyName = "";
          while ((ch = in.readLine()).contains("<SimpleData") || polyName == ""){
            if (ch.contains(regP1)){
              polyName = polyName + ch.substring(ch.indexOf("name=") + 5, ch.lastIndexOf("<"));
            }

            if (ch.contains(regP3)){
              polyName = ch.substring(ch.indexOf("name") + 5, ch.lastIndexOf("</name>"));

            }

          }
          //find coordinates
          while(!(ch = in.readLine()).contains("</coordinates>")){
            if (ch.contains("<coordinates>")){
              String line = typeName + ",0 " + polyName + ",0 " + in.readLine();
              System.out.println(line);
              System.out.println("---------------------------------------------------");
              pointList.add(line);
            }
          }
        }
      }
      in.close();
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }
  }
}
