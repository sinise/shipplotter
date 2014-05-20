import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import java.util.Date;
import java.util.ArrayList;
import java.sql.*;
public class Shipplotter {
private final String USER_AGENT = "Mozilla/5.0";

public static void main(String[] args) throws Exception {
  Shipplotter http = new  Shipplotter();


    while (true) {
      ArrayList<String> lnArrayList = http.sendPost();
      http.uploadToDb(lnArrayList);
      Thread.sleep(15000L); //time i mm sec before each request

    }
}

private void uploadToDb(ArrayList<String> lnList) throws Exception {
try
  {
    Class.forName("com.mysql.jdbc.Driver");
  }
  catch (ClassNotFoundException e) {
    System.out.println("MySQL JDBC Driver not found !!");
    return;
  }
  System.out.println("MySQL JDBC Driver Registered!");
  Connection connection = null;
  Statement stmt = null;

  //connect and upload data
  try {
    connection = DriverManager.getConnection("jdbc:mysql://localhost/shipplotter", "shipplotter", "shipplotter");
    stmt = connection.createStatement();
    System.out.println("SQL Connection to database established!");
    int size = (lnList.size() - 1);
    for (int i = 0; i < size; i++) {
      String thisLn = lnList.get(i);
      String sql = "INSERT IGNORE INTO shipplotter " +
                   "VALUES ('"thisLn"')";
      stmt.executeUpdate(sql);
      System.out.println(thisLn);

    }
  } catch (SQLException e) {
    System.out.println("Connection Failed! Check output console");
//    System.out.println(e);
    return;
  } finally {
    try 
    {
      if(connection != null)
        connection.close();
        System.out.println("Connection closed !!");
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}

// HTTP POST request
private ArrayList<String> sendPost() throws Exception {
  Date date = new Date();
  String filename = "shiplog.csv";
  String url = "http://www.coaa.co.uk/shipinfo.php";
  URL obj = new URL(url);
  HttpURLConnection con = (HttpURLConnection) obj.openConnection();
  Writer out = new FileWriter(filename, true);
  ArrayList<String> output = new ArrayList<String>();

  //add reuqest header
  con.setRequestMethod("POST");
  con.setRequestProperty("User-Agent", USER_AGENT);
  con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

  String urlParameters = "Lines=0&Reg=57470&Extracode=48&Uponly=0&Mouset=86&LatN=+56.518685&LatS=+55.553905&LonE=++14.464614&LonW=++10.767836&Fwd=0&Ver=12.4.8&Tt=1399648978&Desig=219001684";

  // Send post request
  con.setDoOutput(true);
  DataOutputStream wr = new DataOutputStream(con.getOutputStream());
  wr.writeBytes(urlParameters);
  wr.flush();
  wr.close();

  int responseCode = con.getResponseCode();

  BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
  String ln;
  StringBuffer response = new StringBuffer();
  while ((ln = in.readLine()) != null) {
    if ((ln.charAt(26) == ' '))
      {
        ln = (ln.substring(0, 23) + " 0" + ln.substring(24));
      }
    ln = ("\n" + ln.substring(0, 9) + "," + ln.substring(10, 20) + "," + ln.substring(21, 23) + "," + ln.substring(24, 27) + "," +
          ln.substring(29, 38) + "," + ln.substring(41, 50) + "," + ln.substring(51, 56) + "," + ln.substring(57,62) + "," +
          ln.substring(63, 66) + "," + ln.substring(67, 71) + "," + ln.substring(72, 75) + "," + ln.substring(76, 78) + "," + 
          ln.substring(79, 99) + "," + ln.substring(100, 107) + "," + ln.substring(108, 128) + "," + ln.substring(129,134) + "," + 
          ln.substring(135, 140) + "," + ln.substring(141, 142) + "," + ln.substring(143, 150) + "," + ln.substring(151,154) + "," + 
          ln.substring(155, 157) + "," + ln.substring(158, 159));

    out.write(ln);
    output.add(ln);
    //String newInputLine = inputLine.replace("  ", "");
//    System.out.print(ln);
  }
  in.close();
  out.flush();
  out.close();
  return output;

  //print result
}

}
