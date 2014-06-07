import java.io.*;

public class CsvToLog {

public static void main(String[] args) throws Exception {
  String filnameCsv = args[0];
  string filnameLog = args[1];

}
  // HTTP POST request
  void convert(String filnameCsv, String filenameLog) throws Exception {
    File
    Writer out = new FileWriter(filenameLog);
    
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
            ln.substring(155, 157) + "," + ln.substring(158, 159) + "," + date.toString()) + "," + date.getTime();

      out.write(ln);

      //String newInputLine = inputLine.replace("  ", "");
      System.out.print(ln);
    }
    in.close();
    out.flush();
    out.close();

    //print result
  }

}
