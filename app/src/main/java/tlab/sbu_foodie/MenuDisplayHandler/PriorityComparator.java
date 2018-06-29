package tlab.sbu_foodie.MenuDisplayHandler;

import java.util.Comparator;

import tlab.sbu_foodie.Adapter.RowElement;

public class PriorityComparator implements Comparator<RowElement> {

    @Override
    public int compare(RowElement r1, RowElement r2) {
            if(r1.getPriority()>r2.getPriority())
                return 1;
            else if(r1.getPriority()<r2.getPriority())
                return -1;
            else return 0;
    }
}
