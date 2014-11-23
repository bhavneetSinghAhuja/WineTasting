import java.io.FileInputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.Scanner;

/**
 * Created by bhavneet.ahuja on 23/11/14.
 */
public class ParseInput {

    private String inputUrl;
    DBManager dbManager;
    public int maxWineId = -1;

    public ArrayList<DBManager> personToWineMap= new ArrayList<DBManager>();

    public ParseInput(String inputUrl) throws SQLException {
        this.inputUrl = inputUrl;
        dbManager = new DBManager();

    }

    public void calculateResults() throws SQLException, IOException {
        readFile();
        insertIntoWineRank();
        insertIntoPersonRank();
        processResult();
    }

    public int getMaxWineId(){
        return maxWineId;
    }

    private void insertIntoPersonRank() throws SQLException {
       dbManager.insertIntoPersonRankTable();
    }

    private void insertIntoWineRank() throws SQLException {
       dbManager.insertIntoWineRankTable();
    }

    private void readFile() throws IOException, SQLException {

        FileInputStream inputStream = new FileInputStream(inputUrl);
        Scanner sc = new Scanner(inputStream, "UTF-8");
        while(sc.hasNextLine())
        {
            String[] components = sc.nextLine().split("\\s+");
            int personId= Integer.parseInt(components[0].replace("person", ""));
            int wineId = Integer.parseInt(components[1].replace("wine",""));
            if(maxWineId<wineId)
                maxWineId=wineId;
            dbManager.insertIntoPersonToWineTaBLE(personId,wineId);

        }
        sc.close();
    }

    public ArrayList<String> fansOfThisWine(String wine){

        ArrayList<String> listOfFans=new ArrayList<String>();

        return listOfFans;
    }

    public void printList(){
        String s=personToWineMap.toString();
        System.out.println(s);
    }

    public void processResult() throws SQLException {

        ResultSet rs=dbManager.executeQueryStep();
        BitSet wineTracker = new BitSet(maxWineId + 1); // for tracking the bottles
        int bottleCnt = 0;				// Total bottle count
        int currentId = -1;				// Currently active Person ID
        int currentCount = 0;
        while(rs.next()){
            int personId = rs.getInt(0); // seller id
            if (currentId == personId && currentCount == 3)
                continue; // skip if the person already receives three bottles

            if ( currentId != personId) {
                currentId = personId;
                currentCount = 0; // reset lastCnt
            }

            int wineId = rs.getInt(1);// bottle id
            if (wineTracker.get(wineId))
                continue; // skip if the bottle is taken
            wineTracker.set(wineId); // mark the bottle as "sold"

            System.out.println(personId + "\t" + wineId +"\n");
            currentCount++;

            bottleCnt++;

        }
        rs.close();
    }




}
