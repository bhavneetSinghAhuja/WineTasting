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

       cleanUpFiles();
       Statement statement = conn.createStatement();
       statement.executeUpdate("CREATE TABLE person_to_wine ( personId int NOT NULL, " +
               "wineId int NOT NULL,PRIMARY KEY ( personId, wineId ) )");
       statement.executeUpdate("CREATE TABLE personRank (personId int NOT NULL PRIMARY KEY, rank double )");
       statement.executeUpdate("CREATE TABLE wineRank (wineId int NOT NULL PRIMARY KEY, rank double )");
       statement.executeUpdate("CREATE INDEX Widx ON person_to_wine(wineId)");
       statement.executeUpdate("CREATE INDEX Pidx ON person_to_wine(personId)");
       statement.close();

    }

    public void cleanUpFiles() throws SQLException {
        Statement statement = conn.createStatement();
        statement.executeUpdate("DROP TABLE person_to_wine");
        statement.executeUpdate("DROP TABLE personRank");
        statement.executeUpdate("DROP TABLE wineRank");
        statement.close();
    }

    private void shutDownConnection() throws SQLException {
        conn.close();
    }


    public  void insertIntoPersonToWineTaBLE(int personId, int wineId) throws SQLException {

        int i=0;
        PreparedStatement statement = conn.prepareStatement(QueryUtils.INSERT_DATA);
        statement.setInt(0,personId);
        statement.setInt(1, wineId);
        statement.execute();
        statement.close();

    }


    public void insertIntoPersonRankTable() throws  SQLException{
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO personRank(personId, rank) \"\n" +
                "\t\t\t\t+ \" SELECT person_to_wine.personId as personId, SUM(wineRank.rank) AS rank FROM " +
                "person_to_wine, " +
                "wineRank\"\n" +
                "\t\t\t\t+ \" WHERE (person_to_wine.wineId = wineRank.wineId) GROUP BY person_to_wine.personId");
        statement.close();

    }

    public void insertIntoWineRankTable() throws  SQLException{
        Statement statement = conn.createStatement();
        statement.executeUpdate("INSERT INTO wineRank(wineId, rank)"
                + "SELECT wineId AS 'wineId', 10.0/COUNT(personId) as 'rank' FROM person_to_wine GROUP BY wineId;");
        statement.close();

    }


    public List<Integer> selectPersons(int wineId) throws SQLException {

        List<Integer> persons = Lists.newArrayList();
        PreparedStatement statement = conn.prepareStatement(QueryUtils.SELECT_PERSONS);
        statement.setInt(0, wineId);
        ResultSet resultSet= statement.executeQuery();
        while(resultSet.next()){
            persons.add(resultSet.getInt("personId"));
        }
        statement.close();
        return persons;

    }

    public ResultSet executeQueryStep() throws SQLException {
        Statement st=conn.createStatement();
        ResultSet resultSet=st.executeQuery("SELECT personRank.personId, wineRank.wineId FROM personRank, " +
                "person_to_wine, " +
                "wineRank \"\n" +
                "+ \" WHERE personRank.personId = person_to_wine.personId AND person_to_wine.wineId = wineRank.wineId" +
                " " +
                "\"\n" +
                "+ \" ORDER BY personRank.rank DESC, personRank.personId ASC, wineRank.rank DESC");
        st.close();
        return resultSet;
    }

    public static class QueryUtils{
        public static final String SELECT_PERSONS = "SELECT person FROM "+ "winetaste" + "." +"person_to_wine" +"  WHERE wine = ?";
        public static final String INSERT_DATA = "INSERT INTO winetaste.person_to_wine(personId,wineId) VALUES(?,?)";
    }
}
