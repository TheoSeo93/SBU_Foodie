package tlab.sbu_foodie.Activities;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import br.com.bloder.magic.view.MagicButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import tlab.sbu_foodie.DataHandler.ExtendedDataHolder;
import tlab.sbu_foodie.DataHandler.LatLngData;
import tlab.sbu_foodie.DataHandler.TSDProcessor;
import tlab.sbu_foodie.MenuDisplayHandler.PriorityGenerator;
import tlab.sbu_foodie.Model.RatingModel;
import tlab.sbu_foodie.R;
import tlab.sbu_foodie.TwitterRSS.HTTPDataHandler;
import tlab.sbu_foodie.TwitterRSS.RSSObject;
import tlab.sbu_foodie.UIComponent.BottomSheetDialog;
import tlab.sbu_foodie.repository.DocumentRepository;

import static tlab.sbu_foodie.VenueNames.ADMIN_CART;
import static tlab.sbu_foodie.VenueNames.EAST_COCINA;
import static tlab.sbu_foodie.VenueNames.EAST_DELI;
import static tlab.sbu_foodie.VenueNames.EAST_DINE;
import static tlab.sbu_foodie.VenueNames.EAST_GRIL;
import static tlab.sbu_foodie.VenueNames.EAST_HALAL;
import static tlab.sbu_foodie.VenueNames.EAST_ISLAND;
import static tlab.sbu_foodie.VenueNames.EAST_ITALIAN;
import static tlab.sbu_foodie.VenueNames.EAST_KOSHER;
import static tlab.sbu_foodie.VenueNames.JAS;
import static tlab.sbu_foodie.VenueNames.NOBELGLS;
import static tlab.sbu_foodie.VenueNames.ROTH_COURT;
import static tlab.sbu_foodie.VenueNames.SAC_COURT;
import static tlab.sbu_foodie.VenueNames.TABLER_CART;
import static tlab.sbu_foodie.VenueNames.WEST_SIDE;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";


    public static final String EAST = "East Side Dining Hall";
    //Latitudes and longitudes of each places to be shown on the map
    private String pdfUrl = null;
    private static final String TAG = "";
    private LatLngData latLngData;
    private HashMap<String, LatLng> latLngHashMap;
    private Location currentLocation;
    private GoogleMap map;
    private TypedArray places;
    private StorageReference menuFileRef;
    private StringBuilder menuRead;
    private boolean mRequestingLocationUpdates;
    private Marker clickedMarker;

    private int notificationCount;

    private final String RSS_LINK = "https://twitrss.me/twitter_user_to_rss/?user=SBU_Eats";
    private final String RSS_TO_JSONAPI = "https://api.rss2json.com/v1/api.json?rss_url=";
    private SharedPreferences prefs;
    private Gson gson = new Gson();
    private RSSObject rssObject;
    private DocumentRepository repository;
    private HashMap<String, Double> ratings;
    private String[] names = {WEST_SIDE, NOBELGLS, TABLER_CART, ROTH_COURT, SAC_COURT, ADMIN_CART, JAS, EAST_HALAL, EAST_KOSHER, EAST_COCINA, EAST_DELI, EAST_DINE, EAST_GRIL, EAST_ISLAND, EAST_ITALIAN};

    @BindView(R.id.main_activity_container)
    View rootLayout;
    @BindView(R.id.alert)
    Button alert;
    @BindView(R.id.badge)
    TextView alertBadge;
    @BindView(R.id.dining_hour)
    MagicButton diningHour;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ratings = new HashMap<>();
        repository = new DocumentRepository();

        for (int i = 0; i < names.length; i++) {
            final String name = names[i];
            repository.getRatingQuery(name).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    double rate = 0;
                    int count = 0;
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        RatingModel model = snapshot.toObject(RatingModel.class);
                        try{
                            rate += model.getRating();
                        }catch(NullPointerException ex) {
                            Log.d("excption","Null Pointer Exception has occurred from getting a model ratings");
                        }
                        count++;
                    }
                    double avg = rate / count;
                    double val = Math.round(avg * 100) / 100.0;
                    ratings.put(name, val);
                }
            });
            ExtendedDataHolder.getInstance().putExtra("RATE", ratings);
        }
            setContentView(R.layout.activity_main_default);

        latLngHashMap = new HashMap<>();
        ButterKnife.bind(this);
        latLngData = new LatLngData();
        //Load the three-most-recent twitter feeds from https://twitter.com/sbu_eats
        //If the argument is null, then RSS gets loaded only for displaying the number of updated feed on the alert badge
        loadRSS(null);
        alert.setAlpha(0.1f);
        //Display twitter feeds on the popup window
        alert.setOnClickListener(ae -> {
            View v = displayPopupWindow(alert);
            loadRSS(v);
        });

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        places = getResources().obtainTypedArray(R.array.places);

        latLngHashMap.put(WEST_SIDE, latLngData.getWEST_DINING());
        latLngHashMap.put(EAST, latLngData.getEAST_DINING());
        latLngHashMap.put(JAS, latLngData.getJASMINE());
        latLngHashMap.put(NOBELGLS, latLngData.getGLS());
        latLngHashMap.put(SAC_COURT, latLngData.getSAC());
        latLngHashMap.put(ADMIN_CART, latLngData.getADMIN());
        latLngHashMap.put(ROTH_COURT, latLngData.getROTH());
        latLngHashMap.put(TABLER_CART, latLngData.getTABLER());

        //Firebase reference where the dining menu file is being stored.
        menuFileRef = FirebaseStorage.getInstance().getReference().child("test.txt/test.txt");
        updateValuesFromBundle(savedInstanceState);

        //Reads the text file by its URL
        try {
            downloadWithURL();
            diningHour.setMagicButtonClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ExtendedDataHolder.getInstance().putExtra("pdfUrl", pdfUrl);
                    Intent intent = new Intent(getBaseContext(), WebViewActivity.class);
                    startActivity(intent);
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        InputStream is = this.getResources().openRawResource(R.raw.data);
        byte[] buffer;
        try {
            buffer = new byte[is.available()];
            while (is.read(buffer) != -1) ;
            String text = new String(buffer);
            PriorityGenerator.getInstance().putData(text);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //If version is higher than LOLLIPOP, apply animation else don't
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            rootLayout.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    revealActivity(rootLayout.getWidth() / 2, rootLayout.getHeight() / 2);
                }
            });
        }
    }

    /*
     * Displays RSS feed on the popup window.
     */
    private View displayPopupWindow(View anchorView) {
        PopupWindow popup = new PopupWindow(MainActivity.this);
        View layout = getLayoutInflater().inflate(R.layout.feed_item, null);
        popup.setContentView(layout);
        popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        popup.setOutsideTouchable(true);
        popup.setFocusable(true);
        popup.setBackgroundDrawable(new BitmapDrawable());
        popup.showAsDropDown(anchorView);
        return layout;
    }

    /*
     * Loads RSS feed
     */
    private void loadRSS(View layout) {
        @SuppressLint("StaticFieldLeak") AsyncTask<String, String, String> loadRSSAsync = new AsyncTask<String, String, String>() {
            @Override
            protected String doInBackground(String... strings) {
                String result;
                HTTPDataHandler http = new HTTPDataHandler();
                result = http.getHTTPData(strings[0]);
                return result;

            }

            @Override
            protected void onPostExecute(String s) {
                rssObject = new Gson().fromJson(s, RSSObject.class);
                alertBadge.setBackground(getResources().getDrawable(R.drawable.item_count));
                if (layout != null) {
                    SwipeSelector swipeSelector = layout.findViewById(R.id.swipe);
                    try {
                        SwipeItem item1 = new SwipeItem(0, rssObject.getItems().get(0).getPubDate(), rssObject.getItems().get(0).getTitle());
                        SwipeItem item2 = new SwipeItem(0, rssObject.getItems().get(1).getPubDate(), rssObject.getItems().get(1).getTitle());
                        SwipeItem item3 = new SwipeItem(0, rssObject.getItems().get(2).getPubDate(), rssObject.getItems().get(2).getTitle());
                        SwipeItem item4 = new SwipeItem(0, rssObject.getItems().get(3).getPubDate(), rssObject.getItems().get(3).getTitle());
                        SwipeItem item5 = new SwipeItem(0, rssObject.getItems().get(4).getPubDate(), rssObject.getItems().get(4).getTitle());
                        SwipeItem item6 = new SwipeItem(0, rssObject.getItems().get(5).getPubDate(), rssObject.getItems().get(5).getTitle());
                        swipeSelector.setItems(
                                item1, item2, item3, item4, item5, item6, item6
                        );
                    }catch(Exception ex){

                    }
                    notificationCount = 0;
                    alertBadge.setVisibility(View.INVISIBLE);
                    alert.setAlpha(0.2f);
                }
                prefs = getApplicationContext().getSharedPreferences("notifications", Context.MODE_PRIVATE);
                String storedHashMapString = prefs.getString("hashString", "none");
                java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>() {
                }.getType();

                if (!storedHashMapString.equals("none")) {
                    HashMap<String, String> loadedData = gson.fromJson(storedHashMapString, type);
                    try {
                        if (!loadedData.containsValue(rssObject.getItems().get(0).getTitle()))
                            notificationCount++;
                        if (!loadedData.containsValue(rssObject.getItems().get(1).getTitle()))
                            notificationCount++;
                        if (!loadedData.containsValue(rssObject.getItems().get(2).getTitle()))
                            notificationCount++;
                        if (!loadedData.containsValue(rssObject.getItems().get(3).getTitle()))
                            notificationCount++;
                        if (!loadedData.containsValue(rssObject.getItems().get(4).getTitle()))
                            notificationCount++;
                        if (!loadedData.containsValue(rssObject.getItems().get(5).getTitle()))
                            notificationCount++;
                    }catch(Exception ex){
                        Toast.makeText(getApplicationContext(),"Currently Twitter RSS is not working." ,Toast.LENGTH_SHORT).show();
                    }
                    alertBadge.setText(String.valueOf(notificationCount));
                } else {
                    alertBadge.setText(String.valueOf(6));
                    notificationCount = 6;
                }
                if (notificationCount > 0)
                    alert.setAlpha(1.0f);
                else if (notificationCount == 0)
                    alertBadge.setVisibility(View.INVISIBLE);
                HashMap<String, String> temp = new HashMap<String, String>();
                try {
                    temp.put("key1", rssObject.getItems().get(0).getTitle());
                    temp.put("key2", rssObject.getItems().get(1).getTitle());
                    temp.put("key3", rssObject.getItems().get(2).getTitle());
                    temp.put("key4", rssObject.getItems().get(3).getTitle());
                    temp.put("key5", rssObject.getItems().get(4).getTitle());
                    temp.put("key6", rssObject.getItems().get(5).getTitle());
                }catch(Exception ex){

                }
                //convert to string using gson
                String newString = gson.toJson(temp);
                //save in shared prefs
                prefs = getSharedPreferences("notifications", MODE_PRIVATE);
                prefs.edit().putString("hashString", newString).apply();
            }
        };
        StringBuilder url_get_data = new StringBuilder(RSS_TO_JSONAPI);
        url_get_data.append(RSS_LINK);
        loadRSSAsync.execute(url_get_data.toString());
    }

    protected void revealActivity(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(rootLayout.getWidth(), rootLayout.getHeight()) * 1.1);
            // create the animator for this view (the start radius is zero)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(rootLayout, x, y, 0, finalRadius);
            circularReveal.setDuration(1700);
            circularReveal.setInterpolator(new AccelerateInterpolator());
            // make the view visible and start the animation
            rootLayout.setVisibility(View.VISIBLE);
            circularReveal.start();
        }
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, currentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }


        }
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {

        if (rootLayout != null) {
            Snackbar.make(rootLayout, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }








    public void presentActivity(Point v, String title) {
        View view = new View(this);
        view.setX(v.x);
        view.setY(v.y);
        ActivityOptionsCompat options = ActivityOptionsCompat.
                makeSceneTransitionAnimation(this, view, "transition");
        int revealX = (int) (view.getX() + view.getWidth() / 2);
        int revealY = (int) (view.getY() + view.getHeight() / 2);
        ExtendedDataHolder extras = ExtendedDataHolder.getInstance();
        extras.putExtra("extra", menuRead.toString());

        Intent intent = new Intent(this, MenuActivity.class);
        intent.putExtra(MenuActivity.EXTRA_CIRCULAR_REVEAL_X, revealX);
        intent.putExtra(MenuActivity.EXTRA_CIRCULAR_REVEAL_Y, revealY);
        intent.putExtra(MenuActivity.venue, title);

        ActivityCompat.startActivity(this, intent, options.toBundle());
    }




    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Projection projection = map.getProjection();
                LatLng markerLocation = marker.getPosition();
                Point screenPosition = projection.toScreenLocation(markerLocation);
                if (clickedMarker.getTitle().equals(EAST)) {
                    BottomSheetDialog bottomSheetDialog = new BottomSheetDialog();
                    bottomSheetDialog.setScreenPosition(screenPosition);
                    bottomSheetDialog.setMenuRead(menuRead);
                    bottomSheetDialog.show(getSupportFragmentManager(), "Bottomsheet");
                    return;
                } else {
                    presentActivity(screenPosition, marker.getTitle());
                    finish();
                }
            }
        });
        initPlacesMarker();
        map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        map.moveCamera(CameraUpdateFactory.newLatLng(latLngData.getSAC()));
        map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if (ratings.containsKey(marker.getTitle()))
                    marker.setSnippet("Click to See the Menu \t ★: " + ratings.get(marker.getTitle()));
                else
                    marker.setSnippet("Click to See the Menu");
                clickedMarker = marker;

                marker.showInfoWindow();
                return true;
            }
        });
    }

    private void initPlacesMarker() {
        Iterator valIterator = latLngHashMap.values().iterator();
        Iterator keyIterator = latLngHashMap.keySet().iterator();
        for (int i = 0; i < latLngHashMap.size(); i++) {
            Bitmap markerBitmap = BitmapFactory.decodeResource(getResources(), places.getResourceId(i, 0));
            MarkerOptions marker = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(markerBitmap));
            String element = (String) keyIterator.next();
            marker.position((LatLng) valIterator.next());
            marker.title(element);
            marker.alpha(0.9f);
            map.addMarker(marker);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }



    private void downloadWithURL() throws IOException {
        int SDK_INT = android.os.Build.VERSION.SDK_INT;
        if (SDK_INT > 8) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            menuFileRef.getDownloadUrl()
                    .addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri downloadUrl) {
                            menuRead = new StringBuilder();
                            try {
                                URL url = new URL(downloadUrl.toString());
                                BufferedReader in = new BufferedReader(
                                        new InputStreamReader(
                                                url.openStream()));
                                String inputLine;
                                while ((inputLine = in.readLine()) != null)
                                    menuRead.append(inputLine).append(System.lineSeparator());
                                in.close();
                                TSDProcessor tsdProcessor = new TSDProcessor();
                                pdfUrl = tsdProcessor.getTimeSchedule(menuRead.toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle failed download
                }
            });
        } else return;

    }


}
