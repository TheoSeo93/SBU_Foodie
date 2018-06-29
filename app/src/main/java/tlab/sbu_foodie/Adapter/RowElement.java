package tlab.sbu_foodie.Adapter;

/**
 * Created by ilsung on 5/29/2018.
 */

public class RowElement {

    private String date;
    private String revenue;
    private String period;
    private String price="";
    private String name;

    public int getPriority() {
        return priority;
    }

    private int priority;
    private boolean isCombo;


    private String comboMenu;
    public RowElement(String date, String revenue, String period,String name, String price){
        this.date=date;
        this.revenue=revenue;
        this.period=period;
        this.price=price;
        this.name=name;
        this.priority=50;
    }
    public void setComboMenu(String comboMenu){
        this.comboMenu=comboMenu;
    }
    public void setCombo(boolean isCombo){
        this.isCombo=isCombo;
    }
    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getRevenue() {
        return revenue;
    }

    public String getPeriod() {
        return period;
    }

    public String getPrice() {
        return price;
    }

    public boolean isCombo() {
        return isCombo;
    }

    public String getComboMenu() {
        return comboMenu;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
