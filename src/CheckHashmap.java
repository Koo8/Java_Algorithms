import java.util.HashMap;
import java.util.Map;

/**
 * hashmap has not order by its keyset, linkedHashmap has order
 * hashmap don't have same keys in the map.
 * note: hashmap is not ordered in a sensible way, but it does have its own maintained order
 * no matter how many times to run the program
 *
 */

public class CheckHashmap {

    public static void main(String[] args) {
        MapTest test = new MapTest(10);
        test.removeOne(4);
        test.addOne(4);
    }
}

class MapTest{
    Map<Integer, String> map = new HashMap();
    String str = null;

    MapTest(int capacity){
        if(capacity > 0){
            for (int i = 0; i < capacity; i++) {
                map.put(i*3,("--"+i+"--"));
            }
            for(Map.Entry<Integer,String > pair: map.entrySet() ) {
                System.out.println(pair.getKey() + " : " + pair.getValue());
            }
        } else {
            System.out.println("Please give a positive number");
        }
    }
    void removeOne(int key){
        str = map.get(key);
        map.remove(key);
        System.out.println("Map length "+ map.size());
        System.out.println(str + " has been removed");
        System.out.println("NOW : new map is --- ");
        for(Map.Entry<Integer,String > pair: map.entrySet() ) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
    }

    void addOne(int key){
        map.put(key, str);
        System.out.println("After Adding back, the map is --- ");
        for(Map.Entry<Integer,String > pair: map.entrySet() ) {
            System.out.println(pair.getKey() + " : " + pair.getValue());
        }
    }


}
