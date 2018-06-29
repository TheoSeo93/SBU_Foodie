package tlab.sbu_foodie.Activities;

import android.Manifest;
import android.animation.Animator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.nihaskalam.progressbuttonlibrary.CircularProgressButton;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import tlab.sbu_foodie.UIComponent.BottomSheetDialog;
import tlab.sbu_foodie.BuildConfig;
import tlab.sbu_foodie.DataHandler.ExtendedDataHolder;
import tlab.sbu_foodie.GoogleMapRoute.MapAnimator;
import tlab.sbu_foodie.GoogleMapRoute.getPolyline;
import tlab.sbu_foodie.MenuDisplayHandler.PriorityGenerator;
import tlab.sbu_foodie.R;
import tlab.sbu_foodie.TwitterRSS.HTTPDataHandler;
import tlab.sbu_foodie.TwitterRSS.RSSObject;

import static tlab.sbu_foodie.VenueNames.ADMIN_CART;
import static tlab.sbu_foodie.VenueNames.JAS;
import static tlab.sbu_foodie.VenueNames.NOBELGLS;
import static tlab.sbu_foodie.VenueNames.ROTH_COURT;
import static tlab.sbu_foodie.VenueNames.SAC_COURT;
import static tlab.sbu_foodie.VenueNames.TABLER_CART;
import static tlab.sbu_foodie.VenueNames.WEST_SIDE;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private LocationSettingsRequest mLocationSettingsRequest;
    private SettingsClient mSettingsClient;
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 3000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    public static final String EAST = "East Side Dining Hall";

    //Latitudes and longitudes of each places to be shown on the map
    private final LatLng WEST_DINING = new LatLng(40.9133375, -73.1304427);
    private final LatLng EAST_DINING = new LatLng(40.9169623, -73.12085330000002);
    private final LatLng JASMINE = new LatLng(40.9158895, -73.11976759999999);
    private final LatLng GLS = new LatLng(40.9121319, -73.12993599999999);
    private final LatLng SAC = new LatLng(40.9142667, -73.124188);
    private final LatLng ADMIN = new LatLng(40.9147317, -73.12030019999997);
    private final LatLng ROTH = new LatLng(40.9107867, -73.12382980000001);
    private final LatLng TABLER = new LatLng(40.9098733, -73.1270826);
    private static final String TAG = "";

    private HashMap<String, LatLng> latLngHashMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Location currentLocation;
    private GoogleMap map;
    private TypedArray places;
    private StorageReference menuFileRef;
    private StringBuilder menuRead;
    private boolean mRequestingLocationUpdates;
    private Marker clickedMarker;
    private LatLng origin;
    private int notificationCount;
    private LatLng dest;
    private boolean navigationMode;
    private LocationCallback mLocationCallback;
    private CircularProgressButton navigation;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private LocationRequest mLocationRequest;
    private View rootLayout;
    private Button alert;
    private TextView alertBadge;
    private final String RSS_LINK = "https://twitrss.me/twitter_user_to_rss/?user=SBU_Eats";
    private final String RSS_TO_JSONAPI = "https://api.rss2json.com/v1/api.json?rss_url=";
    private SharedPreferences prefs;
    private Gson gson = new Gson();
    private RSSObject rssObject;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        latLngHashMap = new HashMap<>();
        alert = findViewById(R.id.alert);
        alertBadge = findViewById(R.id.badge);
        alert.setAlpha(0.1f);

        //Load the three-most-recent twitter feeds from https://twitter.com/sbu_eats
        //If the argument is null, then RSS gets loaded only for displaying the number of updated feed on the alert badge
        loadRSS(null);

        //Display twitter feeds on the popup window
        alert.setOnClickListener(ae -> {
            View v = displayPopupWindow(alert);
            loadRSS(v);
        });
        rootLayout = findViewById(R.id.main_activity_container);

        //Button for navigation mode
        navigation = findViewById(R.id.navigation);
        navigation.setSweepDuration(10000);
        navigation.setIndeterminateProgressMode(true);

        mSettingsClient = LocationServices.getSettingsClient(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        places = getResources().obtainTypedArray(R.array.places);

        latLngHashMap.put(WEST_SIDE, WEST_DINING);
        latLngHashMap.put(EAST, EAST_DINING);
        latLngHashMap.put(JAS, JASMINE);
        latLngHashMap.put(NOBELGLS, GLS);
        latLngHashMap.put(SAC_COURT, SAC);
        latLngHashMap.put(ADMIN_CART, ADMIN);
        latLngHashMap.put(ROTH_COURT, ROTH);
        latLngHashMap.put(TABLER_CART, TABLER);

        //Firebase reference where the dining menu file is being stored.
        menuFileRef = FirebaseStorage.getInstance().getReference().child("test.txt/test.txt");
        updateValuesFromBundle(savedInstanceState);

        //Reads the text file by its URL
        try {
            downloadWithURL();
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
                    SwipeItem item1 = new SwipeItem(0, rssObject.getItems().get(0).getPubDate(), rssObject.getItems().get(0).getTitle());
                    SwipeItem item2 = new SwipeItem(0, rssObject.getItems().get(1).getPubDate(), rssObject.getItems().get(1).getTitle());
                    SwipeItem item3 = new SwipeItem(0, rssObject.getItems().get(2).getPubDate(), rssObject.getItems().get(2).getTitle());
                    swipeSelector.setItems(
                            item1, item2, item3
                    );
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
                    if (!loadedData.containsValue(rssObject.getItems().get(0).getTitle()))
                        notificationCount++;
                    if (!loadedData.containsValue(rssObject.getItems().get(1).getTitle()))
                        notificationCount++;
                    if (!loadedData.containsValue(rssObject.getItems().get(2).getTitle()))
                        notificationCount++;
                    alertBadge.setText(String.valueOf(notificationCount));

                } else {
                    alertBadge.setText(String.valueOf(3));
                    notificationCount = 3;
                }
                if (notificationCount > 0)
                    alert.setAlpha(1.0f);
                else if (notificationCount == 0)
                    alertBadge.setVisibility(View.INVISIBLE);
                HashMap<String, String> temp = new HashMap<String, String>();
                temp.put("key1", rssObject.getItems().get(0).getTitle());
                temp.put("key2", rssObject.getItems().get(1).getTitle());
                temp.put("key3", rssObject.getItems().get(2).getTitle());
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

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();

            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we remove location updates. Here, we resume receiving
        // location updates if the user has requested them.
        if (mRequestingLocationUpdates && checkPermissions()) {
            startLocationUpdates();
        } else if (!checkPermissions()) {
            requestPermissions();
        }

    }

    public LatLng getOrigin() {
        return origin;
    }

    public LatLng getDest() {
        return dest;
    }

    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, mRequestingLocationUpdates);
        savedInstanceState.putParcelable(KEY_LOCATION, currentLocation);
        super.onSaveInstanceState(savedInstanceState);
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            currentLocation = task.getResult();

                        } else {
                            navigation.showError();
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.main_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
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


    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");
            navigation.showError();
            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                navigationMode = true;
                navigation.showProgress();
                map.setMyLocationEnabled(true);
                getLastLocation();
            } else {
                navigationMode = false;
                navigation.showError();
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }


    private void startLocationUpdates() {
        // Begin by checking if the device has the necessary location settings.
        mSettingsClient.checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");

                        //noinspection MissingPermission
                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                        mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                                mLocationCallback, Looper.myLooper());


                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        int statusCode = ((ApiException) e).getStatusCode();
                        switch (statusCode) {
                            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                                Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                        "location settings ");
                                try {
                                    // Show the dialog by calling startResolutionForResult(), and check the
                                    // result in onActivityResult().
                                    ResolvableApiException rae = (ResolvableApiException) e;
                                    rae.startResolutionForResult(MainActivity.this, REQUEST_CHECK_SETTINGS);
                                } catch (IntentSender.SendIntentException sie) {
                                    Log.i(TAG, "PendingIntent unable to execute request.");
                                }
                                break;
                            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                String errorMessage = "Location settings are inadequate, and cannot be " +
                                        "fixed here. Fix in Settings.";
                                Log.e(TAG, errorMessage);
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                                mRequestingLocationUpdates = false;
                        }

                    }
                });
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
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        if (!mRequestingLocationUpdates) {
            Log.d(TAG, "stopLocationUpdates: updates never requested, no-op.");
            return;
        }

        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mRequestingLocationUpdates = false;

                    }
                });
    }

    public void onClickFirstBtn(View view) {
        if (!navigationMode) {
            requestPermissions();
        } else {
            getLastLocation();
        }
        if (navigation.isIdle()) {
            if (!navigationMode) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                navigationMode = true;
                map.setMyLocationEnabled(true);
            }
            navigation.showProgress();
        } else if (navigation.isErrorOrCompleteOrCancelled()) {
            navigation.showIdle();
        } else if (navigation.isProgress()) {
            navigation.showComplete();
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            navigationMode = false;
            map.setMyLocationEnabled(false);

        } else {
            navigation.showProgress();
        }

    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;

        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if (!checkPermissions()) {
                    requestPermissions();
                } else {
                    getLastLocation();
                }
                map.setMinZoomPreference(12);
                return false;
            }


        });

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
                } else
                    presentActivity(screenPosition, marker.getTitle());
            }
        });

        initPlacesMarker();
        map.moveCamera(CameraUpdateFactory.newLatLng(SAC));
        map.animateCamera(CameraUpdateFactory.zoomTo(15.0f));

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (!checkPermissions() && manager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                    startLocationUpdates();


                double latitude = -1;
                double longitude = -1;
                clickedMarker = marker;
                if (currentLocation != null) {
                    latitude = currentLocation.getLatitude();
                    longitude = currentLocation.getLongitude();

                }
                marker.showInfoWindow();


                ArrayList<LatLng> listLocsToDraw = new ArrayList<>();
                listLocsToDraw.add(marker.getPosition());

                listLocsToDraw.add(new LatLng(latitude, longitude));
                if (map == null) {
                    return false;
                }
                if (listLocsToDraw.size() < 2) {
                    return false;
                }
                origin = new LatLng(latitude, longitude);
                dest = marker.getPosition();
                // Getting URL to the Google Directions API
                setUpPolyLine();
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
            marker.snippet("Click to see the menu");
            marker.alpha(0.9f);
            Marker m = map.addMarker(marker);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        finish();
    }

    public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

        List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
        JSONArray jRoutes;
        JSONArray jLegs;
        JSONArray jSteps;

        try {

            jRoutes = jObject.getJSONArray("routes");

            /** Traversing all routes */
            for (int i = 0; i < jRoutes.length(); i++) {
                jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
                List path = new ArrayList<HashMap<String, String>>();

                /** Traversing all legs */
                for (int j = 0; j < jLegs.length(); j++) {
                    jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

                    /** Traversing all steps */
                    for (int k = 0; k < jSteps.length(); k++) {
                        String polyline = "";
                        polyline = (String) ((JSONObject) ((JSONObject) jSteps.get(k)).get("polyline")).get("points");
                        List<LatLng> list = decodePoly(polyline);

                        /** Traversing all points */
                        for (int l = 0; l < list.size(); l++) {
                            HashMap<String, String> hm = new HashMap<String, String>();
                            hm.put("lat", Double.toString((list.get(l)).latitude));
                            hm.put("lng", Double.toString((list.get(l)).longitude));
                            path.add(hm);
                        }
                    }
                    routes.add(path);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception ignored) {
        }

        return routes;
    }


    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }
        return poly;
    }

    public void setUpPolyLine() {
        LatLng source = getOrigin();
        LatLng destination = getDest();
        if (source != null && destination != null) {

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://maps.googleapis.com/maps/api/directions/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            getPolyline polyline = retrofit.create(getPolyline.class);
            polyline.getPolylineData(source.latitude + "," + source.longitude, destination.latitude + "," + destination.longitude, "walking")
                    .enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                            JsonObject gson = new JsonParser().parse(response.body().toString()).getAsJsonObject();
                            try {

                                Single.just(parse(new JSONObject(gson.toString())))
                                        .subscribeOn(Schedulers.io())
                                        .observeOn(AndroidSchedulers.mainThread())
                                        .subscribe(new Consumer<List<List<HashMap<String, String>>>>() {
                                            @Override
                                            public void accept(List<List<HashMap<String, String>>> lists) throws Exception {

                                                drawPolyline(lists);
                                            }
                                        });

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<JsonObject> call, Throwable t) {

                        }
                    });
        } else
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
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

    void drawPolyline(List<List<HashMap<String, String>>> result) {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!checkPermissions() && manager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !navigationMode) {
            MapAnimator.getInstance().removePolyLines();
            return;
        }
        List<LatLng> points = null;
        points = new ArrayList();
        if (result.size() == 0)
            return;
        List<HashMap<String, String>> path = result.get(result.size() - 1);
        for (int j = 0; j < path.size(); j++) {
            HashMap point = path.get(j);
            double lat = Double.parseDouble((String) point.get("lat"));
            double lng = Double.parseDouble((String) point.get("lng"));
            LatLng position = new LatLng(lat, lng);
            points.add(position);
        }

        if (map != null) {
            MapAnimator.getInstance().animateRoute(map, points);
        }

    }

}
