package joslabs.taka;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.internal.NavigationMenuItemView;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import joslabs.taka.notification.Notification;
import joslabs.taka.specificbuilding.AddRooms;
import joslabs.taka.specificbuilding.Expandable;
import joslabs.taka.specificbuilding.ExpandableFirebase;
import joslabs.taka.specificbuilding.Locationdesc;
import joslabs.taka.specificbuilding.SpecificBuilding;

import static joslabs.taka.R.id.map;

public class MainActivity extends AppCompatActivity
        implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnMapReadyCallback,  View.OnClickListener, GoogleMap.OnMarkerClickListener {

    //  public static final String TAG = MapOffers.class.getSimpleName();
    private LocationRequest mLocationRequest;
    private GoogleApiClient googleApiClient;
    SharedPreferences pref,sharedpref;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private CameraPosition mCameraPosition;
    private static final String KEY_CAMERA_POSITION = "camera_position";

    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private final LatLng mDefaultLocation = new LatLng(-1.2921, 36.8219);
    private static final int DEFAULT_ZOOM = 15;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    GoogleApiClient mGoogleApiClient;

    private GoogleMap googleMap;
    MapView mapView;
    String latsLongs, dates, times, datestime, servicetypename, firebaseId, clientId, serviceId, serviceProvidename, markertype,markert, sproviderId, sphourpay,url,image;

    Marker mymarker, markerb,markerc,markerd,markere,markerf;
    Button btndate, btntime, btnschedule, btnorder, btnnext;
    private int mYear, mMonth, mDay, mHour, mMinute,x,maxPoints,p;

    TextView servicetype, servicepay, paymode, timedate, spname, txtsview,tvTitle,tvSnippet,advname,advurl,advdesc,txtsfireid,txtnext,txtprev;
    ImageView imgadvert,advimg,imgnext,imgprevious;
    RelativeLayout layercost, layertime, relativemap,layerservice;
    DatabaseReference dbref;
    FirebaseDatabase firebaseDatabase;
    ArrayList kk;
    List<Locationdesc> locationdescList;
    Locationdesc locTag;

    static boolean calledAlready=false;
    //  private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:

                    return true;
                case R.id.navigation_dashboard:
                    Intent ints=new Intent(getApplicationContext(), Expandable.class);
                    startActivity(ints);
                    return true;
                case R.id.navigation_notifications:
                      ints=new Intent(getApplicationContext(),ExpandableFirebase.class);
                    startActivity(ints);
                    return true;


            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(map);
        firebaseDatabase=FirebaseDatabase.getInstance();

        pref = getApplicationContext().getSharedPreferences("regd", 0);

         if(!calledAlready)
        {
            try {
                firebaseDatabase.setPersistenceEnabled(true);
                calledAlready = true;
            }
            catch (Exception e)
            {
                Toast.makeText(this, "Exception occured", Toast.LENGTH_SHORT).show();
                Log.e("exfire",e.getMessage().toString());
            }
        }


        dbref= firebaseDatabase.getReference();
locationdescList=new ArrayList<>();

        mapView.onCreate(savedInstanceState);

        mapView.onResume();
        try {
            MapsInitializer.initialize(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        mGoogleApiClient = new GoogleApiClient
                .Builder(getApplicationContext())
                //.enableAutoManage(getActivity(), 34992, this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
   //    locationChecker(mGoogleApiClient, this);
        // Create the LocationRequest object
     /*   mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        */
        // googleMap = mMap;


        txtsview= (TextView) findViewById(R.id.txtsview);
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap mMap) {
                mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker) {

                        //was return false

                        return false;

                    }
                });
                mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
                    @Override
                    public void onMapLongClick(LatLng latLng) {
                        Log.e("coords",latLng.toString());
                    }
                });

                googleMap = mMap;
                mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
                    @Override
                    public void onInfoWindowClick(Marker marker) {
                        final Locationdesc locationdesc= (Locationdesc) marker.getTag();

                        Log.e("Keyfirem",locationdesc.getLockey());
                                Intent ints=new Intent(getApplicationContext(),AddRooms.class);
                                ints.putExtra("Key",locationdesc.getLockey());
                        Toast.makeText(MainActivity.this,locationdesc.getLockey(),Toast.LENGTH_SHORT);
                                startActivity(ints);
                    }
                });
                LatLng petrols = new LatLng(-1.2179869, 36.8902669);
                LatLng petrolsb = new LatLng(-1.184111315374463,36.89908839762211);
                LatLng petrolsc = new LatLng(-1.186461433333951,36.89907096326351);
                LatLng petrolsd = new LatLng(-1.186796302354474,36.89807586371899);
                LatLng petrolse= new LatLng(-1.1868244595075093,36.89817812293768);
                LatLng petrolsf = new LatLng(-1.187479448438779,36.90379600971937);
              /*  kk=new ArrayList();
                kk.add(new LatLng(-1.2179869, 36.8902669));
                kk.add(new LatLng(-1.184111315374463,36.89908839762211) );
                kk.add( new LatLng(-1.186461433333951,36.89907096326351));
                kk.add(new LatLng(-1.186796302354474,36.89807586371899));

               mymarker=googleMap.addMarker(new MarkerOptions().position(petrols).title("Baraza plaza").snippet("Opp ACk").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                mymarker.showInfoWindow();
                markerb=googleMap.addMarker(new MarkerOptions().position(petrolsb).title("Glorious Corner").snippet("Opp Supper ").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markerc=googleMap.addMarker(new MarkerOptions().position(petrolsc).title("Hekima Apartment").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markerd=googleMap.addMarker(new MarkerOptions().position(petrolsd).title("Base").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markere=googleMap.addMarker(new MarkerOptions().position(petrolse).title("Zimma home").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markerf=googleMap.addMarker(new MarkerOptions().position(petrolsf).title("Nasaka").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                googleMap.setInfoWindowAdapter(new myAdapter());
             //Log.e("dataxs",loc.desc());

                CameraPosition cameraPosition = new CameraPosition.Builder().target(petrols ).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                setFirebase();
           */
              dbref.child("nairobi").child("plots").addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(DataSnapshot dataSnapshot) {
//                     String datas=dataSnapshot.getValue().toString();
  //                    Log.e("childx",datas);
                      for (DataSnapshot child:dataSnapshot.getChildren()) {
                          try {

                              Log.e("childs",child.toString());
                              //Log.e("child", child.getValue(Locationdesc.class).toString());
                              locationdescList.add(child.getValue(Locationdesc.class));
                              Log.e("childxx", child.getValue(Locationdesc.class).toString()+"\n"+locationdescList.size()+"\n"+locationdescList.get(0).desc);
                          } catch (Exception e) {
                              Log.e("childerror",e.getMessage());
                          }
                          }
                          setFirebase();
                      }

                  @Override
                  public void onCancelled(DatabaseError databaseError) {
Log.e("firebaseError",databaseError.getMessage());
                  }
              });
            /*   dbref.child("nairobi").child("plots").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("dataux",dataSnapshot.getValue().toString());

               Locationdesc loc=dataSnapshot.getValue(Locationdesc.class);
               // locationdescList.add(loc);
               // Log.e("datauxx",loc.desc+"\n"+locationdescList.size());
                       /// LatLng petrolsf= (LatLng) kk.get(1);
                        //int x=kk.size();
              //  setFirebase();


                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        Log.e("datax",dataSnapshot.getValue().toString());
                        Locationdesc loc=dataSnapshot.getValue(Locationdesc.class);
                        Log.e("dataxx",loc.desc);
                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

*/

            }
        });



        LatLng petrols = new LatLng(-1.2179869, 36.8902669);
        // googleMap.addMarker(new MarkerOptions().position(petrols).title("Abundance Spares").snippet("Opposite Mwala Auto Spares +254711368518").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
        //   mymarker=googleMap.addMarker(new MarkerOptions().position(petrols).title("Previous position").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
        //CameraPosition cameraPosition = new CameraPosition.Builder().target(petrols ).zoom(15).build();
        //  googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

        // mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //BottomNavigationViewH
        BottomNavigationViewHelper.disableShiftMode(navigation);
        BottomNavigationMenuView bottomNavigationMenuView =
                (BottomNavigationMenuView) navigation.getChildAt(0);
        View v = bottomNavigationMenuView.getChildAt(0);
        BottomNavigationItemView itemView = (BottomNavigationItemView) v;


        View badge = LayoutInflater.from(this)
                .inflate(R.layout.notification_blayer, bottomNavigationMenuView, false);

        itemView.addView(badge);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void setFirebase() {
        Log.e("dataxux",locationdescList.size()+"");
        kk=new ArrayList();
        LatLng petrols = new LatLng(-1.2179869, 36.8902669);
        kk.add(new LatLng(-1.2179869, 36.8902669));
        kk.add(new LatLng(-1.184111315374463,36.89908839762211) );
        kk.add( new LatLng(-1.186461433333951,36.89907096326351));
        kk.add(new LatLng(-1.186796302354474,36.89807586371899));
        for (int z=0;z<locationdescList.size();z++) {
            locTag=new Locationdesc();
            String []latslong=locationdescList.get(z).getShortdesc().split(",");
//            Log.e("coods",latslong[0]+"and longs\n"+latslong[1]);
            String lats=latslong[0];
            float lat=Float.valueOf(latslong[0]);
            float llong=Float.valueOf(latslong[1]);
            LatLng newposition=new LatLng(lat,llong);
            locTag.setDesc(locationdescList.get(z).getDesc());
            locTag.setTitle(locationdescList.get(z).getTitle());
            locTag.setLockey(locationdescList.get(z).getLockey());
                           // mymarker = googleMap.addMarker(new MarkerOptions().position((LatLng) kk.get(z)).title("data"+z).snippet("Opp ACk").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
          mymarker = googleMap.addMarker(new MarkerOptions().position(newposition).title("data"+z).snippet("Opp ACk").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
          mymarker.setTag(locTag);
            googleMap.setInfoWindowAdapter(new myAdapter());
           mymarker.showInfoWindow();
                        }

        CameraPosition cameraPosition = new CameraPosition.Builder().target(petrols).zoom(15).build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        //  googleMap.addMarker(new MarkerOptions().position(petrols).title("Abundance Spares").snippet("Opposite Mwala Auto Spares +254711368518").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
        //   Marker markerb,markerc,markerd,markere,markerf;
              /* mymarker=googleMap.addMarker(new MarkerOptions().position(petrols).title("Baraza plaza").snippet("Opp ACk").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                mymarker.showInfoWindow();
                markerb=googleMap.addMarker(new MarkerOptions().position(petrolsb).title("Glorious Corner").snippet("Opp Supper ").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markerc=googleMap.addMarker(new MarkerOptions().position(petrolsc).title("Hekima Apartment").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markerd=googleMap.addMarker(new MarkerOptions().position(petrolsd).title("Base").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markere=googleMap.addMarker(new MarkerOptions().position(petrolse).title("Zimma home").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                markerf=googleMap.addMarker(new MarkerOptions().position(petrolsf).title("Nasaka").snippet("@joslas.co.ke").icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_maphere)));
                googleMap.setInfoWindowAdapter(new myAdapter());
//                Log.e("dataxs",loc.getDesc());
                CameraPosition cameraPosition = new CameraPosition.Builder().target(petrols ).zoom(15).build();
                googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));*/
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleM) {
        googleMap = googleM;

    }



    @Override
    public void onLocationChanged(Location location) {

    }


   /* public void locationChecker(final GoogleApiClient mGoogleApiClient, final Activity activity) {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        //  Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                        //  handleNewLocation(location);


                        if (ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(
                                    activity, 1000);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        break;
                }
            }
        });
    }*/
    private class myAdapter implements GoogleMap.InfoWindowAdapter {
        private final View myContentsView;

        myAdapter(){
            myContentsView = getLayoutInflater().inflate(R.layout.mapicon, null);
        }

        @Override
        public View getInfoContents(Marker marker) {
final Locationdesc locationdesc= (Locationdesc) marker.getTag();
            tvTitle = ((TextView)myContentsView.findViewById(R.id.title));
             tvTitle.setText(marker.getTitle());
            Toast.makeText(MainActivity.this, locationdesc.getTitle(), Toast.LENGTH_SHORT).show();
            tvSnippet = ((TextView)myContentsView.findViewById(R.id.snippet));
            tvSnippet.setText(marker.getSnippet());

            imgnext=myContentsView.findViewById(R.id.imgnext);


            imgnext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent ints=new Intent(v.getContext(),AddRooms.class);
                    ints.putExtra("Key",locationdesc.getLockey());
                   v.getContext().startActivity(ints);
                }
            });
            //tvSnippet.setText(marker.getSnippet());
            // advimg= (ImageView) myContentsView.findViewById(R.id.advimg);


          /*  MarkerTag markerTag = (MarkerTag) marker.getTag();
            Picasso.with(getApplicationContext())
                    .load( markerTag.getImgadvert()).resize(50,50)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(advimg);

            Log.e("imgadvert",markerTag.getImgadvert());
*/


            return myContentsView;
        }
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }


    }
}
