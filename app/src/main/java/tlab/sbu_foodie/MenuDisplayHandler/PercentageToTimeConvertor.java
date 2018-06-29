package tlab.sbu_foodie.MenuDisplayHandler;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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


public class PercentageToTimeConvertor {
    private static final int OldRange = 100;
    public float converToMinutes(float pos, String venue, boolean isWeekend) {
        int min = 0;
        int max = 0;
        switch (venue) {
            case WEST_SIDE:
                if (isWeekend) {
                    min = 510;
                    max = 1620;
                } else {
                    min = 420;
                    max = 1620;
                }
                break;
            case ROTH_COURT:
                if (isWeekend) {
                    min = 540;
                    max = 1500;
                } else {
                    min = 480;
                    max = 1500;
                }
                break;
            case ADMIN_CART:
                min = 510;
                max = 900;
                break;
            case TABLER_CART:

                min = 1050;
                max = 1440;

                break;
            case EAST_COCINA:
                min = 660;
                max = 1500;
                break;
            case EAST_DELI:
                min = 660;
                max = 1140;
                break;
            case EAST_DINE:
                if (isWeekend) {
                    min = 510;
                    max = 1500;
                } else {
                    min = 420;
                    max = 1500;
                }
                break;
            case EAST_GRIL:
                min = 480;
                max = 1500;
                break;
            case EAST_ITALIAN:
                if (isWeekend) {
                    min = 720;
                    max = 1500;
                } else {
                    min = 660;
                    max = 1500;
                }
                break;
            case SAC_COURT:
                if (isWeekend) {
                    min = 720;
                    max = 1080;
                } else {
                    min = 450;
                    max = 1200;
                }
                break;
            case NOBELGLS:
                min = 450;
                max = 1260;
                break;
            case JAS:
                if (isWeekend) {
                    min = 720;
                    max = 1200;
                } else {
                    min = 660;
                    max = 1200;
                }
                break;
            default:
                break;
        }
        return convert(min, max, pos);
    }
    public String convertIntoMinutes(float pos, String venue, boolean isWeekend) {
        int min = 0;
        int max = 0;
        switch (venue) {
            case WEST_SIDE:
                if (isWeekend) {
                    min = 510;
                    max = 1620;
                } else {
                    min = 420;
                    max = 1620;
                }
                break;
            case ROTH_COURT:
                if (isWeekend) {
                    min = 540;
                    max = 1500;
                } else {
                    min = 480;
                    max = 1500;
                }
                break;
            case ADMIN_CART:
                min = 510;
                max = 900;
                break;
            case TABLER_CART:

                min = 1050;
                max = 1440;

                break;
            case EAST_COCINA:
                min = 660;
                max = 1500;
                break;
            case EAST_DELI:
                min = 660;
                max = 1140;
                break;
            case EAST_DINE:
                if (isWeekend) {
                    min = 510;
                    max = 1500;
                } else {
                    min = 420;
                    max = 1500;
                }
                break;
            case EAST_GRIL:
                min = 480;
                max = 1500;
                break;
            case EAST_ITALIAN:
                if (isWeekend) {
                    min = 720;
                    max = 1500;
                } else {
                    min = 660;
                    max = 1500;
                }
                break;
            case SAC_COURT:
                if (isWeekend) {
                    min = 720;
                    max = 1080;
                } else {
                    min = 450;
                    max = 1200;
                }
                break;
            case NOBELGLS:
                min = 450;
                max = 1260;
                break;
            case JAS:
                if (isWeekend) {
                    min = 720;
                    max = 1200;
                } else {
                    min = 660;
                    max = 1200;
                }
                break;
            default:
                break;
        }
        return minutesSinceMidnight(convert(min, max, pos));
    }

    public String minutesSinceMidnight(float minutes) {
        DateFormat _sdf = new SimpleDateFormat("hh:mm a");
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.AM_PM, Calendar.AM);
        cal.set(Calendar.HOUR, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.add(Calendar.MINUTE, (int)minutes);
        return _sdf.format(cal.getTime());
    }

    public float convert(float min, float max, float pos) {

        float newRange = (max - min);
        return (((pos) * newRange) / OldRange) + min;
    }

    public float revert(float min, float max, float val) {
        float newRange = 100;
        float oldRange = max - min;
        return (((val - min) * newRange) / oldRange);

    }


    public float reverseConvert(String venue, boolean isWeekend) {
        float hours = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        float minutes = Calendar.getInstance().get(Calendar.MINUTE);
        int min = 0;
        int max = 0;
        switch (venue) {
            case WEST_SIDE:
                if (isWeekend) {
                    min = 510;
                    max = 1620;
                } else {
                    min = 420;
                    max = 1620;
                }
                break;
            case ROTH_COURT:
                if (isWeekend) {
                    min = 540;
                    max = 1500;
                } else {
                    min = 480;
                    max = 1500;
                }
                break;
            case ADMIN_CART:
                min = 510;
                max = 900;
                break;

            case TABLER_CART:
                min = 1050;
                max = 1448;
                break;
            case EAST_COCINA:
                min = 660;
                max = 1500;
                break;
            case EAST_DELI:
                min = 660;
                max = 1140;
                break;
            case EAST_DINE:
                if (isWeekend) {
                    min = 510;
                    max = 1500;
                } else {
                    min = 420;
                    max = 1500;
                }
                break;
            case EAST_GRIL:
                min = 480;
                max = 1500;
                break;
            case EAST_ITALIAN:
                if (isWeekend) {
                    min = 720;
                    max = 1500;
                } else {
                    min = 660;
                    max = 1500;
                }
                break;
            case SAC_COURT:
                if (isWeekend) {
                    min = 720;
                    max = 1080;
                } else {
                    min = 450;
                    max = 1200;
                }
                break;
            case NOBELGLS:
                min = 660;
                max = 1260;
                break;
            case JAS:
                if (isWeekend) {
                    min = 720;
                    max = 1200;
                } else {
                    min = 660;
                    max = 1200;
                }
                break;
            default:
                break;
        }
        float minFloat = min;
        float maxFloat = max;
        float val = hours * 60 + minutes;
        return revert(minFloat, maxFloat, val);
    }

}
