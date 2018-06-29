package tlab.sbu_foodie.MenuDisplayHandler;

import android.os.Build;

import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Stream;

public class PriorityGenerator {
    private static HashMap<String, Integer> data = new HashMap<>();

    private static PriorityGenerator instance = new PriorityGenerator();

    public PriorityGenerator() {


    }

    public static PriorityGenerator getInstance() {
        return instance;
    }

    public void putData(String text) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Stream.of(text.split("\n")).map(line -> Arrays.asList(line.split("\t"))).forEach(list -> {
                String name = list.get(0);
                int priority = Integer.valueOf(list.get(1).trim());
                if(name.contains("sauce")||name.contains("Sauce"))
                    priority=100;
                else if(name.contains("Char Siu Pork"))
                    priority=0;
                else if(name.contains("Buffalo Chicken"))
                    priority=1;
                data.put(name, priority);
            });
        } else {
            String[] lines = text.split("\\r?\\n");
            for(int i=0;i<lines.length;i++){
                String[] elements = lines[i].split("\\t");
                    String name =elements[0];

                    int priority = Integer.valueOf(elements[1].trim());
                    if(name.contains("sauce")||name.contains("Sauce"))
                        priority=100;
                    else if(name.contains("Char Siu Pork"))
                        priority=0;
                    else if(name.contains("Buffalo Chicken"))
                        priority=1;
                    data.put(name,priority);
                }
        }
    }
        public HashMap<String, Integer> getData () {
            return data;
        }

    }
