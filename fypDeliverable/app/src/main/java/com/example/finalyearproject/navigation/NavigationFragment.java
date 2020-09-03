package com.example.finalyearproject.navigation;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.finalyearproject.R;
import com.example.finalyearproject.SimplifiedCallback;
import com.example.finalyearproject.jSoup;
import com.example.finalyearproject.model.GuidedWalkPlaylist;
import com.example.finalyearproject.pager.TabbedInfo;
import com.mapbox.android.core.location.LocationEngine;
import com.mapbox.android.core.location.LocationEngineCallback;
import com.mapbox.android.core.location.LocationEngineProvider;
import com.mapbox.android.core.location.LocationEngineRequest;
import com.mapbox.android.core.location.LocationEngineResult;
import com.mapbox.api.directions.v5.DirectionsCriteria;
import com.mapbox.api.directions.v5.models.DirectionsResponse;
import com.mapbox.api.directions.v5.models.DirectionsRoute;
import com.mapbox.geojson.Point;
import com.mapbox.mapboxsdk.Mapbox;
import com.mapbox.services.android.navigation.ui.v5.NavigationViewOptions;
import com.mapbox.services.android.navigation.ui.v5.OnNavigationReadyCallback;
import com.mapbox.services.android.navigation.ui.v5.listeners.NavigationListener;
import com.mapbox.services.android.navigation.ui.v5.listeners.RouteListener;
import com.mapbox.services.android.navigation.v5.navigation.NavigationRoute;
import com.mapbox.services.android.navigation.v5.offroute.OffRouteListener;
import com.mapbox.services.android.navigation.v5.routeprogress.ProgressChangeListener;
import com.mapbox.services.android.navigation.v5.routeprogress.RouteProgress;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Response;

import static android.os.Looper.getMainLooper;


public class NavigationFragment extends Fragment implements OnNavigationReadyCallback, NavigationListener,
        ProgressChangeListener, RouteListener, OffRouteListener {

    private Context applicationContext;
    private NavigationView nView;
    private static double ORIGIN_LONGITUDE;
    private static double ORIGIN_LATITUDE;
    private ArrayList<Point> waypoints;
    private LocationListeningCallback callback;
    private static final String TAG = "aaaa";
    private com.mapbox.services.android.navigation.ui.v5.NavigationView navigationView;
    private DirectionsRoute directionsRoute;
    private LocationEngine locationEngine;
    private boolean endRoute;

    public NavigationFragment(NavigationView nView, Context applicationContext) {
        this.nView = nView;
        this.applicationContext = applicationContext;
    }

    public void setWaypoints(ArrayList<com.example.finalyearproject.model.Location> order) {
        for (com.example.finalyearproject.model.Location x : order) {
            Double longitude = Double.valueOf(x.getLONGITUDE());
            Double latitude = Double.valueOf(x.getLATITUDE());
            Point y = Point.fromLngLat(longitude, latitude);
            waypoints.add(y);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        endRoute = false;
        return inflater.inflate(R.layout.fragment_navigation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        callback = new LocationListeningCallback(nView);
        locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());
        navigationView = view.findViewById(R.id.navigationView);
        navigationView.onCreate(savedInstanceState);
        navigationView.initialize(this);
        waypoints = new ArrayList<>();

        initLocationEngine();
    }

    @Override
    public void onStart() {
        super.onStart();
        navigationView.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        navigationView.onResume();
    }

    @Override
    public boolean allowRerouteFrom(Point offRoutePoint) {
        return false;
    }


    @Override
    public void onOffRoute(Point offRoutePoint) {
        navigationView.retrieveMapboxNavigation().addOffRouteListener(new OffRouteListener() {
            @Override
            public void userOffRoute(Location location) {
                Point destination = waypoints.get(waypoints.size() - 1);
                Point origin = Point.fromLngLat(ORIGIN_LONGITUDE, ORIGIN_LATITUDE);
                fetchRoute(origin, destination);
            }
        });
    }

    @Override
    public void onRerouteAlong(DirectionsRoute directionsRoute) {

    }

    @Override
    public void onFailedReroute(String errorMessage) {

    }

    @Override
    public void onArrival() {
        startInformationActivity(false);
    }

    @Override
    public void onProgressChange(Location location, RouteProgress routeProgress) {
        navigationView.retrieveMapboxNavigation().addProgressChangeListener(new ProgressChangeListener() {
            @Override
            public void onProgressChange(Location location, RouteProgress routeProgress) {
                if (routeProgress.distanceRemaining() > 0.0 && routeProgress.durationRemaining() < 20.0 && !endRoute) {
                    onCancelNavigation();
                    endRoute = true;
                    startWalkEndActivity();
                    startInformationActivity(true);


                }
            }
        });
    }

    private void startWalkEndActivity() {
        Intent intent = new Intent(getActivity(), WalkFinishActivity.class);
        getActivity().startActivity(intent);
    }

    @Override
    public void onNavigationReady(boolean isRunning) {
        ArrayList<com.example.finalyearproject.model.Location> order = GuidedWalkPlaylist.getWalkLocations();
        setWaypoints(order);
        Point destination = waypoints.get(waypoints.size() - 1);
        Point origin = Point.fromLngLat(ORIGIN_LONGITUDE, ORIGIN_LATITUDE);
        fetchRoute(origin, destination);
    }

    @Override
    public void onCancelNavigation() {
        navigationView.stopNavigation();
        getActivity().finish();
    }

    @Override
    public void onNavigationFinished() {
        navigationView.stopNavigation();
        getActivity().finish();
    }

    @Override
    public void onNavigationRunning() {

    }

    private void fetchRoute(Point origin, Point destination) {
        assert Mapbox.getAccessToken() != null;
        NavigationRoute.Builder builder = NavigationRoute.builder(getContext())
                .accessToken(Mapbox.getAccessToken())
                .origin(origin)
                .profile(DirectionsCriteria.PROFILE_WALKING).destination(destination);

        int count = 0;
        for (Point x : waypoints) {
            if (count < waypoints.size() - 1) {
                builder.addWaypoint(x);
            }
            count++;
        }


        builder.build().getRoute(new SimplifiedCallback() {
            @Override
            public void onResponse(Call<DirectionsResponse> call, Response<DirectionsResponse> response) {
                directionsRoute = response.body().routes().get(0);
                startNavigation();
            }
        });

    }

    private void startNavigation() {
        if (directionsRoute == null) {
            return;
        }
        NavigationViewOptions options = NavigationViewOptions.builder()
                .directionsRoute(directionsRoute)
                .shouldSimulateRoute(true) //when true, mapbox will simulate the route that the user will take, instead of using GPS
                .navigationListener(NavigationFragment.this)
                .progressChangeListener(this)
                .navigationListener(this)
                .routeListener(this)
                .build();

        navigationView.startNavigation(options);

        navigationView.retrieveMapboxNavigation().addOffRouteListener(new OffRouteListener() {
            @Override
            public void userOffRoute(Location location) {
                Point destination = waypoints.get(waypoints.size() - 1);
                Point origin = Point.fromLngLat(ORIGIN_LONGITUDE, ORIGIN_LATITUDE);
                fetchRoute(origin, destination);
            }
        });
    }


    @SuppressLint("MissingPermission")
    private void initLocationEngine() {
        locationEngine = LocationEngineProvider.getBestLocationEngine(getContext());

        LocationEngineRequest request = new LocationEngineRequest.Builder(1000L)
                .setPriority(LocationEngineRequest.PRIORITY_HIGH_ACCURACY)
                .setMaxWaitTime(5000L).build();

        locationEngine.requestLocationUpdates(request, callback, getMainLooper());
        locationEngine.getLastLocation(callback);
    }

    @Override
    public void userOffRoute(@NotNull Location location) {

    }


    private static class LocationListeningCallback implements LocationEngineCallback<LocationEngineResult> {

        private final WeakReference<NavigationView> activityWeakReference;

        LocationListeningCallback(NavigationView activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void onSuccess(LocationEngineResult result) {

// The LocationEngineCallback interface's method which fires when the device's location has changed.

            Location lastLocation = result.getLastLocation();
            ORIGIN_LONGITUDE = lastLocation.getLongitude();
            ORIGIN_LATITUDE = lastLocation.getLatitude();
        }

        @Override
        public void onFailure(@NonNull Exception exception) {

// The LocationEngineCallback interface's method which fires when the device's location can not be captured


        }
    }

    public void startInformationActivity(boolean finish) {
        Thread thread1 = new Thread() {
            public void run() {
                String hyperlink = GuidedWalkPlaylist.getWalkLocationsItem(0).getHYPERLINK();
                new jSoup(hyperlink).execute();
            }
        };

        Thread thread2 = new Thread() {
            public void run() {
                try {
                    Thread.sleep(3000);
                    Intent intent = new Intent(nView, TabbedInfo.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("thumbnailURL", GuidedWalkPlaylist.getWalkLocationsItem(0).getTHUMBNAIL());
                    intent.putExtras(bundle);
                    nView.startActivity(intent);
                    if (!finish) {
                        GuidedWalkPlaylist.deleteWalkLocationsItem(0);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        };
        thread1.start();
        thread2.start();

    }


}
