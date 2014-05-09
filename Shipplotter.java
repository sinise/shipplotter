import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;

public class Shipplotter {

  private final String USER_AGENT = "Mozilla/5.0";

  public static void main(String[] args) throws Exception {

    Shipplotter http = new  Shipplotter();

    System.out.println("\nTesting 2 - Send Http POST request");
    http.sendPost();

  }

  // HTTP POST request
  private void sendPost() throws Exception {

    String url = "http://www.coaa.co.uk/shipinfo.php";
    URL obj = new URL(url);
    HttpURLConnection con = (HttpURLConnection) obj.openConnection();

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
    System.out.println("\nSending 'POST' request to URL : " + url);
    System.out.println("Post parameters : " + urlParameters);
    System.out.println("Response Code : " + responseCode);

    BufferedReader in = new BufferedReader(
            new InputStreamReader(con.getInputStream()));
    String ln;
    StringBuffer response = new StringBuffer();
    while ((ln = in.readLine()) != null) {
      if ((ln.charAt(26) == ' '))
        {
          ln = (ln.substring(0, 23) + " 0" + ln.substring(24));
        }
      System.out.println(ln);

      ln = (ln.substring(0, 9) + "," + ln.substring(10, 20) + "," + ln.substring(21, 23) + "," + ln.substring(24, 27) + "," +
            ln.substring(29, 38) + "," + ln.substring(41, 50) + "," + ln.substring(51, 56) + "," + ln.substring(57,62) + "," +
            ln.substring(63, 66) + "," + ln.substring(67, 71) + "," + ln.substring(72, 75) + "," + ln.substring(76, 78) + "," + 
            ln.substring(79, 99) + "," + ln.substring(100, 107) + "," + ln.substring(108, 128) + "," + ln.substring(129,134) + "," + 
            ln.substring(135, 140) + "," + ln.substring(141, 142) + "," + ln.substring(143, 150) + "," + ln.substring(151,154) + "," + 
            ln.substring(155, 157) + "," + ln.substring(158, 159));

      //String newInputLine = inputLine.replace("  ", "");
      //newInputLine = inputLine.replace(" ", ",");
      System.out.println(ln);

    }
    in.close();

    //print result
    System.out.println(response.toString());

  }

}
