import com.google.common.collect.Lists;

import java.sql.*;
import java.util.List;

/**
 * Created by bhavneet.ahuja on 23/11/14.
 */
public  class DBManager {


    private Connection conn=null;
    private String url = "jdbc:mysql://localhost/";
    public String dbName = "winetaste";
    public String table = "person_to_wine";
    String driver = "com.mysql.jdbc.Driver";
    String userName = "root";

    public DBManager()
    {
        try {
            conn = getConnection();
            setUpTables();
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }

    private Connection getConnection(){
        try {
            Class.forName(driver).newInstance();
            Connection conn = DriverManager.getConnection(url + dbName, userName, "");
            return conn;
        }
        catch(Exception e){

        }
        return null;
    }

    public void setUpTables() throws SQLException {

       Statement statement = conn.createStatement();
       statement.executeUpdate("CREATE TABLE person_to_wine ( personId int NOT NULL, " +
               "wineId int NOT NULL,PRIMARY KEY ( personId, wineId ) )");
       statement.executeUpdate("CREATE TABLE personRank (personId int NOT NULL PRIMARY KEY, rank double )");
       statement.executeUpdate("CREATE TABLE wineRank (wineId int NOT NULL PRIMARY KEY, rank double )");
       statement.executeUpdate("CREATE INDEX Widx ON person_to_wine(wineId)");
       statement.executeUpdate("CREATE INDEX Pidx ON person_to_wine(personId)");


    }

    private void shutDownConnection() throws SQLException {
        conn.close();
    }


    public  void insertIntoPersonToWineTaBLE(String personId, String wineId) throws SQLException {

        int i=0;
        PreparedStatement statement = conn.prepareStatement(QueryUtils.INSERT_DATA);
        statement.setString(i++,personId);
        statement.setString(i++,wineId);
        statement.execute();
    }

    public void insertIntoPersonRankTable() throws  SQLException{
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO personRank(personId, rank) \"\n" +
                "\t\t\t\t+ \" SELECT person_to_wine.personId as personId, SUM(wineRank.rank) AS rank FROM " +
                "person_to_wine, " +
                "wineRank\"\n" +
                "\t\t\t\t+ \" WHERE (person_to_wine.wineId = wineRank.wineId) GROUP BY person_to_wine.personId");
    }

    public void insertIntoWineRankTable() throws  SQLException{
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO wineRank(wineId, rank)"
                + "SELECT wineId AS 'wineId', 10.0/COUNT(personId) as 'rank' FROM person_to_wine GROUP BY wineId;");
    }


    public List<String> selectPersons(String wineId) throws SQLException {

        List<String> persons = Lists.newArrayList();
        PreparedStatement statement = conn.prepareStatement(QueryUtils.SELECT_PERSONS);
        statement.setString(0,wineId);
        ResultSet resultSet= statement.executeQuery();
        while(resultSet.next()){
            persons.add(resultSet.getString("personId"));
        }

        return persons;

    }

    public static class QueryUtils{
        public static final String SELECT_PERSONS = "SELECT person FROM "+ "winetaste" + "." +"person_to_wine" +"  WHERE wine = ?";
        public static final String INSERT_DATA = "INSERT INTO "+ "winetaste" + "." +"person_to_wine" +"VALUES(?,?)";
    }
}
