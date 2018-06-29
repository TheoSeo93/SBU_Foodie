package tlab.sbu_foodie.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.TextView;

import com.ramotion.fluidslider.FluidSlider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import pub.devrel.easypermissions.EasyPermissions;
import tlab.sbu_foodie.Adapter.RecyclerViewAdapter;
import tlab.sbu_foodie.Adapter.RowElement;
import tlab.sbu_foodie.MenuDisplayHandler.PercentageToTimeConvertor;
import tlab.sbu_foodie.MenuDisplayHandler.PeriodDefiner;
import tlab.sbu_foodie.MenuDisplayHandler.PriorityComparator;
import tlab.sbu_foodie.R;
import tlab.sbu_foodie.DataHandler.TSDProcessor;

import static java.util.Calendar.DAY_OF_WEEK;
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

public class MenuActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";
    public static final String MENU = "";
    public static final String venue = "";
    private static final String TAG = "";
    private CardView cvAdd;
    private TSDProcessor tsdProcessor;
    private String venueName = "";
    private View rootLayout;
    private int revealX;
    private int revealY;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private TextView revenueText;
    private ArrayList<RowElement> menuList;
    public static boolean isWeekend() {
        Date d = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(d);
        return c.get(DAY_OF_WEEK) == Calendar.SATURDAY || c.get(DAY_OF_WEEK) == Calendar.SUNDAY;
    }

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_menu);
            cvAdd = findViewById(R.id.cv_add);
            final Intent intent = getIntent();
            rootLayout = findViewById(R.id.root);
            menuList = new ArrayList<>();
            revenueText = findViewById(R.id.revenue);
            venueName = intent.getStringExtra(venue);
            revenueText.setText(venueName);
            revenueText.setTextColor(Color.BLACK);
            if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                    intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                    intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
                rootLayout.setVisibility(View.INVISIBLE);
                revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
                revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

                ViewTreeObserver viewTreeObserver = rootLayout.getViewTreeObserver();
                if (viewTreeObserver.isAlive()) {
                    viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                        @Override
                        public void onGlobalLayout() {
                            revealActivity(revealX, revealY);
                            rootLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    });
                }
            } else {
                rootLayout.setVisibility(View.VISIBLE);
            }

            // Java
            final FluidSlider slider = findViewById(R.id.fluidSlider);

            slider.setBeginTrackingListener(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    Log.d("D", "setBeginTrackingListener");
                    return Unit.INSTANCE;
                }
            });
            String startText = "";
            String endText = "";
            switch (venueName) {
                case WEST_SIDE:
                    if (isWeekend()) {
                        startText = "Brunch";
                        endText = "Midnight";
                    } else {
                        startText = "Breakfast";
                        endText = "Midnight";
                    }
                    break;
                case ROTH_COURT:
                    startText = "Lunch";
                    endText = "Dinner";
                    break;
                case ADMIN_CART:
                    startText = "Breakfast";
                    endText = "Lunch";
                    break;
                case TABLER_CART:
                    if (isWeekend()) {
                        startText = "Dinner";
                        endText = "Dinner";
                    } else {
                        startText = "Breakfast";
                        endText = "Dinner";
                    }
                    break;
                case EAST_COCINA:
                    startText = "Lunch";
                    endText = "Midnight";
                    break;
                case EAST_DELI:
                    startText = "Lunch";
                    endText = "Dinner";
                    break;
                case EAST_DINE:
                    if (isWeekend()) {
                        startText = "Brunch";
                        endText = "Midnight";
                    } else {
                        startText = "Breakfast";
                        endText = "Midnight";
                    }
                    break;
                case EAST_GRIL:
                    startText = "Breakfast";
                    endText = "Midnight";
                    break;
                case EAST_ITALIAN:
                    startText = "Lunch";
                    endText = "Midnight";
                    break;
                case SAC_COURT:
                    if (isWeekend()) {
                        startText = "Lunch";
                        endText = "Lunch";
                    } else {
                        startText = "Breakfast";
                        endText = "Dinner";
                    }
                    break;
                case NOBELGLS:
                    startText = "Breakfast";
                    endText = "Dinner";
                    break;
                case JAS:
                    startText = "Lunch";
                    endText = "Dinner";
                    break;
            }
            PercentageToTimeConvertor percentageToTimeConvertor = new PercentageToTimeConvertor();
            PeriodDefiner periodDefiner = new PeriodDefiner(venueName, isWeekend());
            slider.setStartText(startText);
            slider.setEndText(endText);
            recyclerView = findViewById(R.id.recyclerview);
            tsdProcessor = new TSDProcessor();
            slider.setPositionListener(pos -> {
                final String value = String.valueOf(percentageToTimeConvertor.convertIntoMinutes((int) (pos * 100), venueName, isWeekend()));
                slider.setBubbleText(value);
                return Unit.INSTANCE;
            });
            slider.setEndTrackingListener(new Function0<Unit>() {
                @Override
                public Unit invoke() {
                    String periodName = periodDefiner.computePeriod(percentageToTimeConvertor.converToMinutes(slider.getPosition() * 100, venueName, isWeekend()), false);
                    if(slider.getPosition()==1.0f)
                        periodName="closed";

                    InitializeRecyclerView(periodName, venueName);
                    return Unit.INSTANCE;
                }
            });
            slider.setPosition(percentageToTimeConvertor.reverseConvert(venueName, isWeekend()) / 100.0f);
            String periodName = periodDefiner.computePeriod(0, true);
            if(slider.getPosition()==1.0f)
                periodName="closed";

            InitializeRecyclerView(periodName, venueName);

        }


    private void InitializeRecyclerView(String period, String venueName) {
        menuList.clear();
        menuList=null;
        menuList = tsdProcessor.processString(venueName, period);
        if(menuList.size()==0){
            revenueText.setText(venueName+"\t"+"(Closed)");
            revenueText.setTextColor(Color.BLACK);
        } else {
            revenueText.setText(venueName);
            revenueText.setTextColor(Color.BLACK);
        }

        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        Collections.sort(menuList, new PriorityComparator());
        recyclerViewAdapter = new RecyclerViewAdapter(getApplicationContext(), menuList, venueName);
        recyclerView.setAdapter(recyclerViewAdapter);

    }


    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(400);
            circularReveal.setInterpolator(new AccelerateInterpolator());

            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        }
    }

    public void animateRevealClose() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, revealX, revealY, cvAdd.getHeight(), 40);
            mAnimator.setDuration(300);
            mAnimator.setInterpolator(new AccelerateInterpolator());
            mAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    cvAdd.setVisibility(View.INVISIBLE);
                    super.onAnimationEnd(animation);
                    MenuActivity.super.onBackPressed();
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                }
            });
            mAnimator.start();
        }
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {

    }


}
