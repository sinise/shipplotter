import java.net.*;
import java.io.*;
import java.awt.Toolkit;
import java.util.*;
//import java.sql.*;
import java.text.SimpleDateFormat;

public class MarineTraficCrawler {
  public static void main(String[] args) throws Exception {
    ArrayList<MarinetraficShip> shipsHtml = new ArrayList<MarinetraficShip>();
    ArrayList<MarinetraficShip> shipsUrl = new  ArrayList<MarinetraficShip>();
    ArrayList<String> errors = new ArrayList<String>();

    //if wrong argument size
    if (args.length != 1) {
      System.out.println("wrong argument specification \n Use: MarineTraficCrawler.java file");
      System.out.println("where file is a comma seperatet text file in this format <mmsi>,<name>,<html file>");
      System.out.println("html file is optional. if not provided data will be fetched from Marinetrafic.com");
    }


    try {
      //get the file containing list of ships
      String file = args[0];
      String ship;
      FileInputStream fstream = new FileInputStream(args[0]);
      DataInputStream fin = new DataInputStream(fstream);
      BufferedReader in = new BufferedReader(new InputStreamReader(fin));
      UploadToDB DB = new UploadToDB();
      //For each ship create a MarinetraficShip object and place it in the apporpriate array list 
      // for either html or url source
      while ((ship = in.readLine()) != null) {
        String[] newShip = ship.split(",");
        if (newShip.length == 4) {
          shipsHtml.add(new MarinetraficShip(newShip[0], newShip[1], newShip[2], newShip[3]));
        }
        if (newShip.length == 3) {
          shipsUrl.add(new MarinetraficShip(newShip[0], newShip[1], newShip[2]));
        }
        if (newShip.length != 3 && newShip.length != 4) {
          errors.add(ship);
        }
        System.out.printf("There was %d errors, when fetching ships from file and newShip.length is %d", errors.size(), newShip.length);
      }

      //fetch data for each ship with html source
      for (int i = 0; i < shipsHtml.size(); i++) {
        shipsHtml.get(i).fetchData();
//        for (int j = 0; j < shipsHtml.get(i).listOut.length(); j++) {
          DB.upload(shipsHtml.get(i).getData());
//          System.out.println(shipsHtml.get(i).list.get(j));
//        }
      }

      //fetch data for each ship in url source
      for (int i = 0; i < shipsUrl.size(); i++) {
        shipsUrl.get(i).fetchData();
//        for (int j = 0; j < shipsUrl.get(i).listOut.length(); j++) {
        DB.upload(shipsUrl.get(i).getData());
//System.out.println(shipsUrl.get(i).list.get(j));
//        }
      }
    }
    catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
    }


  }
}
