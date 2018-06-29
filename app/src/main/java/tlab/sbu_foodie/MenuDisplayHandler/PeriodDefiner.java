package tlab.sbu_foodie.MenuDisplayHandler;

import java.util.Calendar;

import static tlab.sbu_foodie.VenueNames.ADMIN_CART;
import static tlab.sbu_foodie.VenueNames.EAST_COCINA;
import static tlab.sbu_foodie.VenueNames.EAST_DELI;
import static tlab.sbu_foodie.VenueNames.EAST_DINE;
import static tlab.sbu_foodie.VenueNames.EAST_GRIL;
import static tlab.sbu_foodie.VenueNames.EAST_ITALIAN;
import static tlab.sbu_foodie.VenueNames.JAS;
import static tlab.sbu_foodie.VenueNames.NOBELGLS;
import static tlab.sbu_foodie.VenueNames.ROTH_COURT;
import static tlab.sbu_foodie.VenueNames.SAC_COURT;
import static tlab.sbu_foodie.VenueNames.TABLER_CART;
import static tlab.sbu_foodie.VenueNames.WEST_SIDE;

/**
 * Created by ilsung on 5/29/2018.
 */

public class PeriodDefiner {
    String time;
    String venue;
    final static String BREAKFAST = "Breakfast";
    final static String LUNCH = "Lunch";
    final static String BRUNCH = "Brunch";
    final static String MIDNIGHT = "Midnight";
    final static String DINNER = "Dinner";
    boolean isWeekend;
    int minutes;
    public PeriodDefiner(String venue, boolean isWeekend) {
        this.venue = venue;
        this.isWeekend = isWeekend;

    }

    /**
     * This method returns either Breakfast, Brunch, Lunch, Dinner or Midnight depending on the places(West dining, East dining, etc)
     * I need this method to show the app users what's being offered on specific time on the specific place.
     * Brunch is only available to the West Dine in and East Dine in only on Weekends. See https://cafes.compass-usa.com/StonyBrook/SitePages/Menu.aspx?lid=a1
     */
    public String computePeriod(float minutes,boolean now) {
        if(now){
            int hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
            int minute = Calendar.getInstance().get(Calendar.MINUTE);
            minutes = hours * 60 + minute;
        }
        switch (venue) {
            case WEST_SIDE:
                if (isWeekend) {
                    if (minutes >= 510 && minutes <= 630)
                        return BREAKFAST;
                    else if (minutes > 630 && minutes <= 900)
                        return BRUNCH;
                    else if (minutes > 900 && minutes <= 1260)
                        return DINNER;
                    else if (minutes > 1260)
                        return MIDNIGHT;
                } else {
                    if (minutes >= 420 && minutes <= 630)
                        return BREAKFAST;
                    else if (minutes > 630 && minutes <= 1020)
                        return LUNCH;
                    else if (minutes > 1020 && minutes <= 1260)
                        return DINNER;
                    else if (minutes > 1260)
                        return MIDNIGHT;
                }
                break;
            case ROTH_COURT:
                if (isWeekend) {
                    if (minutes >= 660 && minutes < 1020)
                        return LUNCH;
                    else if (minutes >= 1020)
                        return DINNER;
                } else {
                    if (minutes >= 660 && minutes < 1020)
                        return LUNCH;
                    else if (minutes >= 1020)
                        return DINNER;
                }
                break;
            case ADMIN_CART:
                if (minutes >= 510 && minutes <= 660)
                    return BREAKFAST;
                else if (minutes > 660)
                    return LUNCH;
                break;

            case TABLER_CART:
                return DINNER;

            case EAST_COCINA:
                if (minutes >= 660 && minutes < 900)
                    return LUNCH;
                else if (minutes >= 900 && minutes < 1260)
                    return DINNER;
                else if (minutes >= 1260)
                    return MIDNIGHT;
                break;
            case EAST_DELI:
                if (minutes >= 660 && minutes < 900)
                    return LUNCH;
                else if (minutes >= 900 && minutes < 1260)
                    return DINNER;
                break;
            case EAST_DINE:
                if (isWeekend) {
                    if (minutes > 510 && minutes <= 630)
                        return BREAKFAST;
                    else if (minutes > 630 && minutes <= 900)
                        return BRUNCH;
                    else if (minutes > 900 && minutes <= 1260)
                        return DINNER;
                    else if (minutes > 1260)
                        return MIDNIGHT;
                } else {
                    if (minutes >= 420 && minutes <= 630)
                        return BREAKFAST;
                    else if (minutes > 630 && minutes <= 1020)
                        return LUNCH;
                    else if (minutes > 1020 && minutes <= 1260)
                        return DINNER;
                    else if (minutes > 1260)
                        return MIDNIGHT;
                }
                break;
            case EAST_GRIL:

                if (minutes >= 480 && minutes < 660)
                    return BREAKFAST;
                else if (minutes >= 660 && minutes < 1020) {
                    return LUNCH;
                } else if (minutes >= 1020 && minutes < 1260)
                    return DINNER;
                else if (minutes >= 1260)
                    return MIDNIGHT;
                break;

            case EAST_ITALIAN:
                if (isWeekend) {
                    if (minutes >= 720 && minutes < 1020) {
                        return LUNCH;
                    } else if (minutes >= 1020 && minutes < 1260)
                        return DINNER;
                    else if (minutes >= 1260)
                        return MIDNIGHT;
                } else {
                    if (minutes >= 660 && minutes < 1020) {
                        return LUNCH;
                    } else if (minutes >= 1020 && minutes < 1260)
                        return DINNER;
                    else if (minutes >= 1260)
                        return MIDNIGHT;
                }
                break;
            case SAC_COURT:
                if (isWeekend) {
                    if (minutes >= 720 && minutes < 1080)
                        return DINNER;
                } else {
                    if (minutes >= 450 && minutes < 630)
                        return BREAKFAST;
                    else if (minutes >= 630 && minutes < 1020)
                        return LUNCH;
                    else if (minutes >= 1020)
                        return DINNER;
                }
                break;
            case NOBELGLS:
                if (minutes >= 450 && minutes < 660)
                    return BREAKFAST;
                else if (minutes >= 660 && minutes < 1020)
                    return LUNCH;
                else if (minutes >= 1020)
                    return DINNER;
                break;
            case JAS:
                if (isWeekend) {
                    if (minutes >= 720 && minutes < 1020)
                        return LUNCH;
                    else if (minutes >= 1020)
                        return DINNER;
                } else {
                    if (minutes >= 660 && minutes < 1020)
                        return LUNCH;
                    else if (minutes >= 1020)
                        return DINNER;
                }
                break;
        }
        return null;
    }
}
