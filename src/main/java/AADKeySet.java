import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.ArrayList;


public class AADKeySet {

    @JsonAlias("keys")
    private ArrayList<JsonWebKey> keys = new ArrayList<>();

    public ArrayList<JsonWebKey> getKeys() {
        return keys;
    }

    public void setKeys(ArrayList<JsonWebKey> keys) {
        this.keys = keys;
    }
}


