package nl.hr.annelies.medialab_admin;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    Location currentLocation;

    Button rulesButton;
    Button settingsButton;
    Integer id;
    FirebaseFirestore db;
    ListView listView;




//    hotspots[0] = new Hotspot("strandwacht", new LatLng(-32, 138.2), "hallo");
//    {name: "hoi", location: new LatLng(32, 42), message: "lol"}];

//    List<{name: String, location: LatLng, message: String}> hotspots = [{name: "strandwacht", location: new LatLng(-32, 138.2), message: "hallo"}];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        System.out.println(db.collection("kids").get());

        db.collection("kids").get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
               @Override
               public void onComplete(@NonNull Task<QuerySnapshot> task) {
                   if (task.isSuccessful()) {

                       ArrayList<String> ids = new ArrayList<String>() ;

                       for (QueryDocumentSnapshot document : task.getResult()) {
                           String idString = document.getData().get("id").toString();
                           Log.d("STRING ID", idString);
                           ids.add(idString);
                           Log.d("DOCUMENT", document.getId() + " => " + document.getData().get("id"));
                       }

                       listView = findViewById(R.id.list_view);
                       listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, ids));


                   } else {
                       Log.w("ERROR", "Error getting documents.", task.getException());
                   }
               }
           });

//        JSONArray idList = new JSONArray(db.collection("kids").get());




    }

//
//        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        editor.putInt("id", 176204);
//        editor.apply();
//
//        id = sharedPreferences.getInt("id", 0);
//
//
//        hotspots[0] = new Hotspot("strandwacht", new LatLng(52.11463474012185, 4.280247697237467), "hallo");
//        hotspots[1] = new Hotspot("pier", new LatLng(52.11796848556339, 4.280011749484001), "pier");
//        hotspots[2] = new Hotspot("restaurantje ofzo", new LatLng(52.11224552563259, 4.278095452177875), "restaurantje denk ik");
////        hotspots[1] = new Hotspot("informatie punt", new LatLng(52.11796848556339, 4.280011749484001), "info enzo");
//
//        // buttons
//        rulesButton = findViewById(R.id.button_1);
//        settingsButton = findViewById(R.id.button_3);
//
//        rulesButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(nl.hr.annelies.medialab.MainActivity.this, RulesActivity.class));
//            }
//        });
//
//        settingsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(nl.hr.annelies.medialab.MainActivity.this, SettingsActivity.class));
//            }
//        });
//
//        // request permission for recording audio
//        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//
//        // map fragment
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(nl.hr.annelies.medialab.MainActivity.this);
//        fetchLocation();
//
//
//        // onclicklistener for the tooltip:
////        recordButton.setOnTouchListener(new View.OnTouchListener() {
////            @Override
////            public boolean onTouch(View view, MotionEvent event) {
////                System.out.println(event);
////                System.out.println(event.getAction());
////                // up = 1, down is 0
////
////                if (event.getAction() == 0) {
////
//////                    mediaRecorder.start();
////                    try {
////                        recordAudio(view);
////                        micImage.setImageResource(R.drawable.mic_orange);
////
////
////                    } catch (IOException e) {
////                        e.printStackTrace();
////                    }
////                } else if (event.getAction() == 1) {
////                    stopAudio(view);
////                    micImage.setImageResource(R.drawable.mic_grey);
////                }
////
////
////                return false;
////            }
////        });
//
//
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//
//
//    // mapp stuffffff
//
//    private void fetchLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            System.out.println("no permission");
//            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
//            return;
//        }
//        Task<Location> task = fusedLocationProviderClient.getLastLocation();
//        task.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    currentLocation = location;
//                    if(currentLocation != null) {
//                        Toast.makeText(getApplicationContext(), currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();
//                        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//                        assert supportMapFragment != null;
//                        supportMapFragment.getMapAsync(nl.hr.annelies.medialab.MainActivity.this);
//                    }
//                }
//            }
//        });
//    }
//
//    private void enableUserLocation() {
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            System.out.println("Setmylocationenabled trueee");
//            mMap.setMyLocationEnabled(true);
//        } else {
//            //Ask for permission
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
//                // Show user a dialog for displaying why the permission is needed and then ask for the permission
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//            } else {
//                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 200);
//            }
//        }
//    }
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//
//        mMap = googleMap;
//        enableUserLocation();
//
//        if(currentLocation != null) {
//            mMap.getUiSettings().setCompassEnabled(true);
//
//            // user
//            LatLng userLocation = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));
////            MarkerOptions markOptUser = new MarkerOptions().position(userLocation).title("Huidige locatie").icon(BitmapDescriptorFactory.fromResource(R.drawable.rudy_grey));
////            mMap.addMarker(markOptUser);
//
//            // hotspots
//            System.out.println(hotspots);
//            for(int i = 0; i < hotspots.length; i++) {
//                System.out.println(hotspots[i]);
//                String name = "markHotspot" + i;
//                mMap.addMarker(new MarkerOptions().position(hotspots[i].location).title("Hotspot " + i).icon(BitmapDescriptorFactory.fromResource(R.drawable.rudy_grey_small)));
//            }
////            MarkerOptions markOptStrWacht= new MarkerOptions().position(userLocation).title("Huidige locatie").icon(BitmapDescriptorFactory.fromResource(R.drawable.rudy_colored));
//        }
//    }
//
//
//    void goToRules() {
//        startActivity(new Intent(this, SettingsActivity.class));
//    }
}


