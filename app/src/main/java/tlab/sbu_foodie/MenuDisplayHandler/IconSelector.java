package tlab.sbu_foodie.MenuDisplayHandler;

import tlab.sbu_foodie.R;


public class IconSelector {
    private int bacon = R.drawable.ic_bacon;
    private int broccoli = R.drawable.ic_broccoli;
    private int burger = R.drawable.ic_burger;
    private int carrot = R.drawable.ic_carrot;
    private int corndog = R.drawable.ic_corndog;
    private int fish = R.drawable.ic_fish;
    private int frenchFries = R.drawable.ic_frenchfries;
    private int ham = R.drawable.ic_ham;
    private int sausage = R.drawable.ic_sausage;
    private int steak = R.drawable.ic_steak;
    private int sushi = R.drawable.ic_sushi;
    private int taco = R.drawable.ic_taco;
    private int noodles = R.drawable.ic_noodles;
    private int orange = R.drawable.ic_orange;
    private int salad = R.drawable.ic_salad;
    private int bread = R.drawable.ic_bread;
    private int cheese = R.drawable.ic_cheese;
    private int friedChicken = R.drawable.ic_fried_chicken;
    private int chicken = R.drawable.ic_chicken_leg;
    private int potato = R.drawable.ic_potatoes;
    private int sweetPotato =R.drawable.ic_sweet_potato;
    private int egg = R.drawable.ic_fried_egg;
    private int lettuce =R.drawable.ic_lettuce;
    private int tomato = R.drawable.ic_tomato;
    private int yogurt =R.drawable.ic_yogurt;
    private int watermelon = R.drawable.ic_watermelon;
    private int sandwich = R.drawable.ic_sandwich;
    private int hotdog = R.drawable.ic_hotdog;
    private int pizza = R.drawable.ic_pizza;
    private int beef = R.drawable.ic_beef;
    private int tofu = R.drawable.ic_tofu;
    private int eggPlant = R.drawable.ic_eggplant;
    private int brownie = R.drawable.ic_brownie;
    private int corn = R.drawable.ic_corn;
    private int rice = R.drawable.ic_rice;
    private int dumpling =R.drawable.ic_dumpling;
    private int muffin = R.drawable.ic_muffin;

    public int getIcon(String name){
        if(name.contains("Bacon"))
            return bacon;
        else if(name.contains("Eggplant")||name.contains("egg plant")||name.contains("Egg Plant"))
            return eggPlant;
        else if(name.contains("Broccoli"))
            return broccoli;
        else if(name.contains("Burger")||name.contains("burger"))
            return burger;
        else if(name.contains("Buffalo Wing")||name.contains("Buffalo Chicken")||name.contains("Fried Chicken"))
            return friedChicken;
        else if(name.contains("Chicken")||name.contains("chicken")||name.contains("Turkey")||name.contains("turkey"))
            return chicken;
        else if(name.contains("egg")||name.contains("Egg"))
            return egg;
        else if(name.contains("hotdog")||name.contains("Hot Dog")||name.contains("Hotdog"))
            return hotdog;
        else if(name.contains("Sandwich")||name.contains("sandwich"))
            return sandwich;
        else if(name.contains("Bread")||name.contains("bread"))
            return bread;
        else if(name.contains("Pizza")||name.contains("pizza"))
            return pizza;
        else if(name.contains("Carrot"))
            return carrot;
        else if(name.contains("Corn Dog"))
            return corndog;
        else if(name.contains("Corn")||name.contains("corn"))
            return corn;
        else if(name.contains("Brownie"))
            return brownie;
        else if(name.contains("Rice")||name.contains("rice"))
            return rice;
        else if(name.contains("dumpling")||name.contains("Dumpling"))
            return dumpling;
        else if(name.contains("fish"))
            return fish;
        else if(name.contains("French Fries")||name.contains("Fries"))
            return frenchFries;
        else if(name.contains("Ham"))
            return ham;
        else if(name.contains("Sausage"))
            return sausage;
        else if(name.contains("steak")||name.contains("Steak"))
            return steak;
        else if(name.contains("sushi")||name.contains("Sushi"))
            return sushi;
        else if(name.contains("taco")||name.contains("Taco"))
            return taco;
        else if(name.contains("Sweet Potato")||name.contains("sweet potato"))
            return sweetPotato;
        else if(name.contains("Potato")||name.contains("potato"))
            return potato;
        else if(name.contains("cheese")||name.contains("Cheese"))
            return cheese;
        else if(name.contains("lettuce")||name.contains("Lettuce"))
            return lettuce;
        else if(name.contains("Tomato")||name.contains("tomato"))
            return tomato;
        else if(name.contains("watermelon")||name.contains("Watermelon"))
            return watermelon;
        else if(name.contains("Yogurt")||name.contains("yogurt"))
            return yogurt;
        else if(name.contains("Potato")||name.contains("Potato"))
            return potato;
        else if(name.contains("Noodles")||name.contains("Noodle"))
            return noodles;
        else if(name.contains("Orange"))
            return orange;
        else if(name.contains("Salad"))
            return salad;
        else if(name.contains("Beef")||name.contains("beef"))
            return beef;
        else if(name.contains("Tofu")||name.contains("tofu"))
            return tofu;
        else if(name.contains("Muffin")||name.contains("muffin"))
            return muffin;
        else return -100000;
    }
}
