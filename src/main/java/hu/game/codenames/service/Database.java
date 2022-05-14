package hu.game.codenames.service;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;

public class Database {
    Connection conn;
    String db="";
    URI dbUri = new URI(db);
    String username = dbUri.getUserInfo().split(":")[0];
    String password = dbUri.getUserInfo().split(":")[1];
    String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
    public Database() throws SQLException, URISyntaxException {
        this.conn= DriverManager.getConnection(dbUrl, username, password);;
    }


    public void create(){
        try {
            this.conn= DriverManager.getConnection(dbUrl, username, password);;
            Statement stmt = conn.createStatement();
            String sql = "CREATE TABLE data (\n" +
                    "sessionId varchar(100),\n" +
                    "tablaId integer,\n"+
                    "utalasId integer,\n" +
                    "kezdes Timestamp,\n" +
                    "befejezes Timestamp,\n" +
                    "tipp integer,\n" +
                    "tipp1 varchar(100),\n" +
                    "tipp2 varchar(100),\n" +
                    "tipp3 varchar(100),\n" +
                    "tipp4 varchar(100),\n" +
                    "tipp5 varchar(100),\n" +
                    "tipp6 varchar(100),\n" +
                    "tipp7 varchar(100),\n" +
                    "tipp8 varchar(100),\n" +
                    "tipp9 varchar(100),\n" +
                    "tipp10 varchar(100),\n" +
                    "tipp11 varchar(100),\n" +
                    "tipp12 varchar(100)\n" +
                    ");";
            stmt.executeUpdate(sql);
            stmt.close();
            conn.close();
            System.out.println("Created table in given database...");

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public boolean check(int id,String session) throws SQLException {
        this.conn= DriverManager.getConnection(dbUrl, username, password);;
        String query = "Select * from data where tablaId = ? and sessionId = ?";
        int rowCount = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setInt(1, id);
            ps.setString(2, session);
            ResultSet rs = ps.executeQuery();
            if (rs.last()) {
                rowCount = rs.getRow();
                rs.beforeFirst();
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException se) {
            throw se;
        }
        if(0 == rowCount){
            return false;
        }else{
            return true;
        }
    }

    public int numberOfGame(String session) throws SQLException {
        this.conn= DriverManager.getConnection(dbUrl, username, password);;
        String query = "Select * from data where sessionId = ?";
        int rowCount = 0;
        try {
            PreparedStatement ps = conn.prepareStatement(query,ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            ps.setString(1, session);
            ResultSet rs = ps.executeQuery();
            if (rs.last()) {
                rowCount = rs.getRow();
                rs.beforeFirst();
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException se) {
            throw se;
        }
        return rowCount;
    }
    public void save(Info a) throws SQLException {
        this.conn= DriverManager.getConnection(dbUrl, username, password);
        a.end = new Timestamp(System.currentTimeMillis());
        try {
            PreparedStatement st = conn.prepareStatement("INSERT INTO data (utalasId,sessionId, tablaId,kezdes,befejezes,tipp, tipp1,tipp2,tipp3,tipp4,tipp5,tipp6,tipp7,tipp8,tipp9,tipp10,tipp11,tipp12) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)");
            st.setInt(1, a.clueId);
            st.setString(2, a.sessionId);
            st.setInt(3, a.boardId);
            st.setTimestamp(4, a.begin);
            st.setTimestamp(5, a.end);
            st.setInt(6, a.tipp);
            st.setString(7, a.tipps[0]);
            st.setString(8, a.tipps[1]);
            st.setString(9, a.tipps[2]);
            st.setString(10, a.tipps[3]);
            st.setString(11, a.tipps[4]);
            st.setString(12, a.tipps[5]);
            st.setString(13, a.tipps[6]);
            st.setString(14, a.tipps[7]);
            st.setString(15, a.tipps[8]);
            st.setString(16, a.tipps[9]);
            st.setString(17, a.tipps[10]);
            st.setString(18, a.tipps[11]);
            st.executeUpdate();
            st.close();
            conn.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

}
