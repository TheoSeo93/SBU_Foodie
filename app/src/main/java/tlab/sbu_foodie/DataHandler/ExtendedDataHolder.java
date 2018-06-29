package tlab.sbu_foodie.DataHandler;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is for passing a huge amount of string to another activity as an singleton object.
 */

public class ExtendedDataHolder {

    private static ExtendedDataHolder ourInstance = new ExtendedDataHolder();

    private final Map<String, Object> extras = new HashMap<>();

    private ExtendedDataHolder() {
    }

    public static ExtendedDataHolder getInstance() {
        return ourInstance;
    }

    public void putExtra(String name, Object object) {
        extras.put(name, object);
    }

    public Object getExtra(String name) {
        return extras.get(name);
    }

    public boolean hasExtra(String name) {
        return extras.containsKey(name);
    }

    public void clear() {
        extras.clear();
    }
}
