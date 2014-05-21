import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SqlToLog {
    public static void main(String[] args) {

        Connection con = null;
        PreparedStatement pst = null;
        ResultSet rs = null;

        String url = "jdbc:mysql://localhost/shipplotter";
        String user = "shipplotter";
        String password = "shipplotter";

        try {
            con = DriverManager.getConnection(url, user, password);
            pst = con.prepareStatement("SELECT mmsi,timestamp,status,type,lat,lon,speed,course,heading FROM shipplotter WHERE type=33");
            rs = pst.executeQuery();
            String mmsi;
            int timestamp;
            int status;
            int type;
            String lat;
            String lon;
            String speed;
            String course;
            String heading;
            int count = 0;
            while (rs.next()) {
                mmsi = rs.getString(1);
                timestamp = rs.getInt(2);
                status = rs.getInt(3);
                type = rs.getInt(4);
                lat = rs.getString(5);
                lon = rs.getString(6);
                speed = rs.getString(7);
                course = rs.getString(8);
                heading = rs.getString(9);
                while (mmsi.length() < 9)
                {
                  mmsi = "0" + mmsi;
                }
                if (mmsi.length() < 9)
                  count++;
                System.out.println(mmsi + ";" + timestamp + ";" + status + ";" + type + ";" + lat + ";" + lon + ";" + speed + ";" + course + ";" + heading + ";" + count);
            }

        } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(SqlToLog.class.getName());
                lgr.log(Level.SEVERE, ex.getMessage(), ex);

        } finally {

            try {
                if (rs != null) {
                    rs.close();
                }
                if (pst != null) {
                    pst.close();
                }
                if (con != null) {
                    con.close();
                }

            } catch (SQLException ex) {
                Logger lgr = Logger.getLogger(SqlToLog.class.getName());
                lgr.log(Level.WARNING, ex.getMessage(), ex);
            }
        }
    }
}
