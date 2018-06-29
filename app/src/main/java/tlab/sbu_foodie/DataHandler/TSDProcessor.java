package tlab.sbu_foodie.DataHandler;


import android.os.Build;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.stream.Stream;

import tlab.sbu_foodie.Adapter.RowElement;
import tlab.sbu_foodie.MenuDisplayHandler.PriorityGenerator;

import static tlab.sbu_foodie.VenueNames.EAST_DINE;
import static tlab.sbu_foodie.VenueNames.WEST_SIDE;


public final class TSDProcessor {
    private ArrayList<RowElement> rows;
    private String currentDate;
    private ExtendedDataHolder extras;

    public TSDProcessor() {
        rows = new ArrayList<>();
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date = new Date();
        currentDate = dateFormat.format(date);
        extras = ExtendedDataHolder.getInstance();

    }

    /**
     * Processes the data and populated two {@link Map} objects with the data.
     *
     * @throws Exception if the input string does not follow the <code>.tsd</code> data format

     */

    public ArrayList<RowElement> processString(String venueType, String periodName) {
        String tsdString = "";
        if (extras.hasExtra("extra")) {
            tsdString = (String) extras.getExtra("extra");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Stream.of(tsdString.split("\n"))
                    .map(line -> Arrays.asList(line.split("\t")))
                    .forEach(list -> {


                            if (list.get(1).equals(venueType) && list.get(0).equals(currentDate) && list.get(2).equals(periodName)) {

                                String date = list.get(0);
                                String revenue = list.get(1);
                                String period = list.get(2);
                                String name = list.get(3);
                                String price = "";
                                if (list.size() >= 5)
                                    price = list.get(4);

                                System.out.print(date+revenue+period+name+price);
                                rows.add(new RowElement(date, revenue, period, name, price));
                            }

                        });

        }else{
            String[] lines = tsdString.split("\\r?\\n");
            for(int i=0;i<lines.length;i++) {
                String[] elements = lines[i].split("\\t");
                if (elements.length >= 3) {
                    if (elements[1].equals(venueType) && elements[0].equals(currentDate) && elements[2].equals(periodName)) {

                        String date = elements[0];
                        String revenue = elements[1];
                        String period = elements[2];
                        String name = elements[3];
                        String price = "";
                        if (elements.length >= 5)
                            price = elements[4];
                        rows.add(new RowElement(date, revenue, period, name, price));
                    }
                }
            }
        }
        if(!venueType.equals(EAST_DINE)&&!venueType.equals(WEST_SIDE)){
            for(int i=0;i<rows.size();i++){
                if(i+1<rows.size()){
                    if(rows.get(i+1).getPrice().equals("0.00")){
                        rows.get(i).setCombo(true);
                        rows.get(i).setComboMenu(rows.remove(i+1).getName());

                    }
                }
            }
        } else {
            for(int i=0;i<rows.size();i++){
                if(PriorityGenerator.getInstance().getData().containsKey(rows.get(i).getName()))
                rows.get(i).setPriority((PriorityGenerator.getInstance().getData()).get(rows.get(i).getName()));

            }

        }
        return rows;
    }
    public ArrayList<RowElement> getRows() {
        return rows;
    }


}

