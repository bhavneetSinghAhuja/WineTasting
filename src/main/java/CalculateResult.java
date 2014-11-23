import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by bhavneet.ahuja on 24/11/14.
 */
public class CalculateResult {

    public static void main(String args[]) throws IOException, SQLException {
        ParseInput parseInput=new ParseInput("/Users/bhavneet.ahuja/Downloads/person_wine_3.txt");

        //calculating the results and printing out in the console
        parseInput.calculateResults();
    }
}
