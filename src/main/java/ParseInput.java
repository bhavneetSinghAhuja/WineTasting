import java.io.FileInputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by bhavneet.ahuja on 23/11/14.
 */
public class ParseInput {

    private String inputUrl;
    DBManager dbManager;

    public ArrayList<DBManager> personToWineMap= new ArrayList<DBManager>();

    public ParseInput(String inputUrl) throws IOException, SQLException {
        this.inputUrl = inputUrl;
        dbManager = new DBManager();
        readFile();
        insertIntoWineRank();
        insertIntoPersonRank();
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
            dbManager.insertIntoPersonToWineTaBLE(components[0].replace("person", ""), components[1].replace("wine", ""));

        }
    }

    public ArrayList<String> fansOfThisWine(String wine){

        ArrayList<String> listOfFans=new ArrayList<String>();

        return listOfFans;
    }

    public void printList(){
        String s=personToWineMap.toString();
        System.out.println(s);
    }

//    public static void main(String args[]) throws IOException, SQLException {
//        ParseInput p= new ParseInput("/Users/bhavneet.ahuja/Downloads/person_wine_3.txt");
//
//
//    }



}
