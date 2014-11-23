import com.google.common.collect.Maps;

import java.net.URL;
import java.util.HashMap;
import java.util.List;

/**
 * Created by bhavneet.ahuja on 23/11/14.
 */
public class ParseInput {

    private URL inputUrl;

    private HashMap<String,List<String>> personToWineMap= Maps.newHashMap();

    public HashMap<String, List<String>> getPersonToWineMap() {
        return personToWineMap;
    }

    public void setPersonToWineMap(HashMap<String, List<String>> personToWineMap) {
        this.personToWineMap = personToWineMap;
    }

    public URL getInputUrl() {
        return inputUrl;
    }

    public void setInputUrl(URL inputUrl) {
        this.inputUrl = inputUrl;
    }

    public ParseInput(URL inputUrl) {
        this.inputUrl = inputUrl;
    }

    private static void readFile(){

        

    }



}
