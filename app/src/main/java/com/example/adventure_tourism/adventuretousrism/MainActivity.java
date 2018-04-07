package com.example.adventure_tourism.adventuretousrism;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private ExpandableListAdapter expandable;
    private  ExpandableListView expandableListView;
    private  List<String> Adventures, Packages;
    private  List<String> titles;
    private Stack<Marker> tempMarker;
    private HashMap<String,List<String>> childDetails;
    private GoogleMap mMap;
    private HashMap<Marker, Integer> MarkerMap= new HashMap<>();
    private SupportMapFragment sfm;
    private Firebase fireRef,packRef,authRef;
    private DrawerLayout Drawer;
    private boolean loggedIn;
    private android.support.v4.app.FragmentManager SFM;
    private ChildEventListener childEventListener,childEventListenerPack;
    private CameraPosition centerMarker;
    private Handler handler;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Firebase.setAndroidContext(this);
        sfm = SupportMapFragment.newInstance();
        handler = new Handler();
        tempMarker = new Stack<>();

        /*--------------------Authentication-----------START-*/

        authRef = new Firebase("https://adventure-tourism.firebaseio.com");
        authRef.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                if (authData != null) {
                    // user is logged in
                    loggedIn = true;
                    Log.v("Logged in", "Yes");
                } else {
                    loggedIn = false;
                    Log.v("Logged in", "No");
                }
            }
        });
        /*--------------------Authentication-----------END--*/


        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if(isNetworkAvailable(this)){
            setMap(); // setting the map to the layout
        }else{
            Toast.makeText(MainActivity.this,"Internet Unavailable",Toast.LENGTH_LONG).show();
        }
        setMenu(); // setting the menu of the drawer


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(loggedIn){
                    //open basket fragment
                    Basket_View basket_view = new Basket_View();
                    android.support.v4.app.FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.map,basket_view);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else{
                     /* -----Fragment Processes ----- */
                    LoginUser loginUser = new LoginUser();
                    android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.map,loginUser);
                    trans.addToBackStack(null);
                    trans.commit();
                /* -----Fragment Processes ----- */
                }

            }
        });

        tv = (TextView) findViewById(R.id.cart_count);
        Drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, Drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        Drawer.setDrawerListener(toggle);
        toggle.syncState();

    }
    public void LogInUsers(String email, String password){
        authRef.authWithPassword(email, password, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                loggedIn = true;
                // open basket fragment
                Toast.makeText(MainActivity.this,"You are logged in",Toast.LENGTH_LONG).show();
                Log.v("Logged ", "Authenticated");
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                loggedIn = false;
                Log.v("Logged ", "Not Authenticated");
                Toast.makeText(MainActivity.this,"Wrong Log in",Toast.LENGTH_LONG).show();
                // toast wrong username password
            }
        });
    }

    public void SignUpUsers(String email,String password){
        authRef.createUser(email, password, new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> stringObjectMap) {
                Toast.makeText(MainActivity.this, "You can now Log In", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FirebaseError firebaseError) {
                Toast.makeText(MainActivity.this, "ERROR signing Up", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void setMarkers(){
        fireRef = new Firebase("https://adventure-tourism.firebaseio.com/Location");
        packRef = new Firebase("https://adventure-tourism.firebaseio.com/Package");

         childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {



                    Log.v("Child event listener ", " Ran ");
                    setMarker((dataSnapshot.child("Location").child("Latitude").getValue(Double.class)),
                            (dataSnapshot.child("Location").child("Longitude").getValue(Double.class)),
                            (dataSnapshot.child("Type").getValue(String.class)), dataSnapshot.child("SportID").getValue(Integer.class));



                    Log.v("Size of Marker Hashmap", String.valueOf(MarkerMap.size()));
                    Log.v("Size of details Hashmap", String.valueOf(childDetails.size()));

                    setClickListenerMarker();

                } else {
                    Log.v("child add", "Null");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
         childEventListenerPack = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {

                    setMarker((dataSnapshot.child("Location").child("Latitude").getValue(Double.class)),
                            (dataSnapshot.child("Location").child("Longitude").getValue(Double.class)),
                            dataSnapshot.child("Name").getValue(String.class), dataSnapshot.child("PackageID").getValue(Integer.class));

                    Log.v("packRef added", " data");
                    setClickListenerMarker();

                } else {
                    Log.v("child add", "Null");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        };
        packRef.addChildEventListener(childEventListenerPack);
        fireRef.addChildEventListener(childEventListener);



    }

    public void setMenu(){
        expandableListView = (ExpandableListView)findViewById(R.id.expandList);
        childDetails = new HashMap<>();

        Adventures = new ArrayList<>();
        Packages = new ArrayList<>();

        Adventures.add("Paragliding");
        Adventures.add("Mountain Bike");
        Adventures.add("Bungee Jump");

        Packages.add("Mountain Biking & Paragliding");
        Packages.add("Mountain Biking & Bungee Jump");
        Packages.add("Bungee Jump & Paragliding");

        childDetails.put("Adventure Sports", Adventures);
        childDetails.put("Packages", Packages);

        titles = new ArrayList<>(childDetails.keySet());
        expandable = new ExpandableListAdapter(this,titles,childDetails);
        expandableListView.setAdapter(expandable);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {

            }
        });
        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                Drawer.closeDrawer(Gravity.LEFT); // closing the navigational drawer

                Toast.makeText(MainActivity.this,"Select Marker for Details",Toast.LENGTH_LONG).show();

            if(MarkerMap.isEmpty()){
                Toast.makeText(MainActivity.this,"No Data Found",Toast.LENGTH_LONG).show();
            }else{
                for (Marker m : MarkerMap.keySet()) {

                    if (m.getTitle().equals(childDetails.get(titles.get(groupPosition)).get(childPosition))) {
                        m.setVisible(true);
                        tempMarker.add(m);


                    } else {
                        m.setVisible(false);
                    }
                }

                centerMarker();

                if (SFM.getBackStackEntryCount()>0){
                    SFM.popBackStack(null,SFM.POP_BACK_STACK_INCLUSIVE);
                }
            }


                return false;
            }
        });
    }
    public boolean isNetworkAvailable(final Context context) {
        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode== Activity.RESULT_OK){

                tv.setText(" ");

                if (SFM.getBackStackEntryCount()>0){
                    SFM.popBackStack(null,SFM.POP_BACK_STACK_INCLUSIVE);
                }else{}



            }else{}
        }else{
            Toast.makeText(this,"Error In Check Out",Toast.LENGTH_LONG);
        }
    }

    public void centerMarker(){

         if(tempMarker.isEmpty()){
             Log.v("centerMarker break ", " yes");
         }else{

             //centering the camera to a Marker
             LatLng coordinate = new LatLng(tempMarker.get(tempMarker.size()-1).getPosition().latitude,tempMarker.get(tempMarker.size()-1).getPosition().longitude);
             centerMarker = new CameraPosition.Builder()
                     .target(coordinate).build();
             mMap.animateCamera(CameraUpdateFactory.newCameraPosition(centerMarker));

             tempMarker.pop();

             handler.postDelayed(new Runnable() {
                 @Override
                 public void run() {
                     centerMarker();
                 }
             }, 2000);


         }

     }

    public void setMap(){
        sfm.getMapAsync(this);
        SFM = getSupportFragmentManager();
        SFM.beginTransaction().add(R.id.map, sfm).commit();

    }

    public void setMarker(Object Lat, Object Long, Object type, Object id){

            if(Lat!=null && Long !=null && type!=null && id!=null) {
                Marker mTemp = mMap.addMarker(new MarkerOptions().position(new LatLng((double)Lat, (double)Long)).title((String) type));
                mTemp.setVisible(false);
                Log.v("type value is", ((String) type).substring(0,7));
                 MarkerMap.put(mTemp, Integer.valueOf((int) id));
            }else{}
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        setMarkers();
    }

    @Override
    public void onBackPressed() {

        if (Drawer.isDrawerOpen(GravityCompat.START)) {
            Drawer.closeDrawer(GravityCompat.START);
        } else {

        }

        if(SFM.getBackStackEntryCount()==0){
            super.onBackPressed();
            Log.v("Back Stack", "Pressed");
        }else{
            SFM.popBackStack();
            Log.v("Back stack", "back stack entry count is " + SFM.getBackStackEntryCount());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        fireRef.removeEventListener(childEventListener);
        packRef.removeEventListener(childEventListenerPack);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem menuItem = menu.findItem(R.id.logoutOption);
        if(loggedIn){
           menuItem.setTitle("Log Out");
        }
        else {
            menuItem.setTitle("Log In");
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id == R.id.logoutOption){
            if(loggedIn){
                loggedIn = false;
                authRef.unauth();
                Toast.makeText(this,"You are Logged Out",Toast.LENGTH_LONG).show();
            }else{
                // TO DO: Not open a new fragment if already in login page
                 /* -----Fragment Processes ----- */
                LoginUser loginUser = new LoginUser();
                android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                trans.replace(R.id.map,loginUser);
                trans.addToBackStack(null);
                trans.commit();
                /* -----Fragment Processes ----- */
            }

        }

        return super.onOptionsItemSelected(item);
    }

    public void setClickListenerMarker(){
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                if(marker.getTitle().length()>15){
                    int x = MarkerMap.get(marker);
                    Log.v("Marker Integer", String.valueOf(x));

                /* -----Fragment Processes ----- */
                    Package_detail package_detail=new Package_detail();
                    Bundle args = new Bundle();
                    args.putInt("PackageID", x);
                    package_detail.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.map,package_detail);
                    trans.addToBackStack(null);
                    trans.commit();
                /* -----Fragment Processes ----- */

                }else{
                    int x = MarkerMap.get(marker);
                    Log.v("Marker Integer", String.valueOf(x));

                /* -----Fragment Processes ----- */
                    Hotel_detail hotel=new Hotel_detail();
                    Bundle args = new Bundle();
                    args.putInt("HotelID", x);
                    hotel.setArguments(args);
                    android.support.v4.app.FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
                    trans.replace(R.id.map,hotel);
                    trans.addToBackStack(null);
                    trans.commit();
                /* -----Fragment Processes ----- */
                }
                return false;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
