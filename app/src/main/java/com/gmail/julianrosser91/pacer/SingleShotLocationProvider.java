//package com.gmail.julianrosser91.pacer;
//
//import android.app.Activity;
//import android.content.Context;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//
///**
// * This class handles all user location requests. It attempts to get the most accurate measurement.
// * If GPS is unavailable, it will try to get network location, if that also isn't available it will
// * use the previously stored user location. A toast message will show if absolutely no location is
// * available, but this should never happen.
// *
// */
//
//public class SingleShotLocationProvider {
//
//    public interface LocationCallback {
//        void onNewLocationAvailable(GPSCoordinates location);
//        void onLocationPermissionDenied();
//        void noLocationAvailable();
//    }
//
//    private static LocationCallback activityCallback;
//    private static Activity context;
//    private static LocationManager locationManager;
//
//    public static void requestSingleUpdate(final LocationCallback locationCallback, Activity context) {
//        SingleShotLocationProvider.activityCallback = locationCallback;
//        SingleShotLocationProvider.context = context;
//        locationManager = (LocationManager) Pacer.getmInstance().getSystemService(Context.LOCATION_SERVICE);
//        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
//        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
//
//        checkLocationPermission();
//
//        if (isGPSEnabled) {
//            attemptToGetGpsLocation();
//        }
//        else if (isNetworkEnabled) {
//            getNetworkLocationIfAvailable();
//        }
//        else {
//            attemptToGetPreviousUserLocation();
//        }
//    }
//
//    private static void attemptToGetGpsLocation() {
//
//        final Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_FINE);
//
//        final TimeoutableLocationListener.TimeoutListener timeoutListener = new TimeoutableLocationListener.TimeoutListener() {
//            @Override
//            public void onTimeouted(2LocationListener sender) {
//                Utils.print("TIMEOUT GPS");
//                getNetworkLocationIfAvailable();
//            }
//        };
//        final TimeoutableLocationListener timeoutableGPSLocationListener = new TimeoutableLocationListener(locationManager, 5000, timeoutListener) {
//            @Override
//            public void onLocationChanged(Location location) {
//                stopLocationUpdateAndTimer();
//                Utils.print("GPS location successful");
//                onLocationFoundSuccessfully(location);
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//
//        new Handler(Looper.getMainLooper()) {
//        }.post(new Runnable() {
//            @Override
//            public void run() {
//                checkLocationPermission();
//                locationManager.requestSingleUpdate(criteria, timeoutableGPSLocationListener, null);
//            }
//        });
//
//    }
//
//    private static void getNetworkLocationIfAvailable() {
//
//        final Criteria criteria = new Criteria();
//        criteria.setAccuracy(Criteria.ACCURACY_COARSE);
//
//
//        TimeoutableLocationListener.TimeoutListener timeoutListener = new TimeoutableLocationListener.TimeoutListener() {
//            @Override
//            public void onTimeouted(LocationListener sender) {
//                Utils.print("TIMEOUT COURSE");
//                attemptToGetPreviousUserLocation();
//            }
//        };
//        final TimeoutableLocationListener timeoutableCoarseLocationListener = new TimeoutableLocationListener(locationManager, 5000, timeoutListener) {
//            @Override
//            public void onLocationChanged(Location location) {
//                Utils.print("COARSE location successful");
//                stopLocationUpdateAndTimer();
//                saveNewUserLocation(location, context);
//                activityCallback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
//            }
//
//            @Override
//            public void onStatusChanged(String provider, int status, Bundle extras) {
//            }
//
//            @Override
//            public void onProviderEnabled(String provider) {
//            }
//
//            @Override
//            public void onProviderDisabled(String provider) {
//            }
//        };
//
//        new Handler(Looper.getMainLooper()) {
//        }.post(new Runnable() {
//            @Override
//            public void run() {
//                checkLocationPermission();
//                locationManager.requestSingleUpdate(criteria, timeoutableCoarseLocationListener, null);
//            }
//        });
//    }
//
//    // todo - perhaps use location update callback to listen for eventual signal?
//    private static void attemptToGetPreviousUserLocation() {
//        Utils.print("attemptToGetPreviousUserLocation()");
//        GPSCoordinates gpsCoordinates = getPreviousUserLocation();
//
//        if (gpsCoordinates != null) {
//            activityCallback.onNewLocationAvailable(gpsCoordinates);
//        } else {
//            activityCallback.noLocationAvailable();
//
//            new Handler(Looper.getMainLooper()) {
//
//            }.post(new Runnable() {
//                @Override
//                public void run() {
//                    Utils.toast(CinemApp.getInstance().getResources().getString(R.string.location_unavailable));
//                }
//            });
//        }
//    }
//
//    private static GPSCoordinates getPreviousUserLocation() {
//
//        User user = CinemApp.getInstance().getUser();
//
//        if (user != null && user.getLocation_lat() != 0) {
//            return new GPSCoordinates(user.getLocation_lat(), user.getLocation_lon());
//        } else {
//            return null;
//        }
//    }
//
//    private static void onLocationFoundSuccessfully(Location location) {
//        saveNewUserLocation(location, context);
//        activityCallback.onNewLocationAvailable(new GPSCoordinates(location.getLatitude(), location.getLongitude()));
//    }
//
//    private static void saveNewUserLocation(Location location, Activity context) {
//        String userId = Utils.getUserUniqueId();
//        if (userId.length() > 10) {
//            GPSCoordinates coordinates = new GPSCoordinates(location.getLatitude(),
//                    location.getLongitude());
//            FirebaseDBHelper.saveUserLocationToDB(userId, coordinates);
//        }
//        if (CinemApp.getInstance().getUser() != null) {
//            CinemApp.getInstance().getUser().setLocation_lat((float) location.getLatitude());
//            CinemApp.getInstance().getUser().setLocation_lon((float) location.getLongitude());
//        }
//        Utils.getCountryCode(location.getLatitude(), location.getLongitude(), context);
//    }
//
//    public static void checkLocationPermission() {
//        if (ActivityCompat.checkSelfPermission(CinemApp.getInstance(), Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CinemApp.getInstance(),
//                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            Log.d("SingleShotLocation", "Permission denied, location not available");
//            activityCallback.onLocationPermissionDenied();
//        }
//    }
//}