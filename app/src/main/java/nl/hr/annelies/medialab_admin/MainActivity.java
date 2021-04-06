package nl.hr.annelies.medialab_admin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Any;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Location currentLocation;

    Button rulesButton;
    Button settingsButton;
    Integer id;
    FirebaseFirestore db;
    ListView listView;
    ArrayList<String> ids;
    boolean hasMessage = false;




//    hotspots[0] = new Hotspot("strandwacht", new LatLng(-32, 138.2), "hallo");
//    {name: "hoi", location: new LatLng(32, 42), message: "lol"}];

//    List<{name: String, location: LatLng, message: String}> hotspots = [{name: "strandwacht", location: new LatLng(-32, 138.2), message: "hallo"}];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = FirebaseFirestore.getInstance();

        System.out.println(db.collection("kids").get());

        db.collection("kids").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.w("ERROR", "Listen failed.", error);
                    return;
                }
                ids = new ArrayList<String>();

                List<String> cities = new ArrayList<>();
                for (QueryDocumentSnapshot document : value) {
                    if (document.get("id") != null) {
                        String idString = document.getData().get("id").toString();
                        Log.d("STRING ID", idString);
                        ids.add(idString);
                    }

                    Log.d("DOCUMENT", document.getId() + " => " + document.getData().get("id"));

                }
                Log.d("DATA", "Current cites in CA: " + cities);

                listView = findViewById(R.id.list_view);
                listView.setOnItemClickListener(MainActivity.this);
                listView.setAdapter(new ArrayAdapter<String>(MainActivity.this, R.layout.list_item, ids));
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        db.collection("kids").document(ids.get(position)).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if(doc.contains("hasMessage")) {
                        System.out.println(doc.getBoolean("hasMessage"));
                       hasMessage = doc.getBoolean("hasMessage");
                    } else {
                         Log.d("ERROR", "Doc doesn't exist");
                    }
                } else {
                    Log.d("ERROR", "Error: ", task.getException());
                }
            }
        });

        if(hasMessage == true) {
            System.out.println("Hasmessage");
        } else {
            System.out.println("no message yet");
            Log.i("Hello", "You clicked item: " + id + "at position: " + position);
            System.out.println(ids.get(position));
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setTitle("Weet u zeker dat dit kind gevonden is?");
            builder.setMessage("U staat op het punt om bij de ouders te melden dat dit kind gevonden is: " + ids.get(position));
            builder.setPositiveButton("Kind is gevonden",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
//                                NavHostFragment.findNavController(FirstFragment.this)
//                                    .navigate(R.id.action_FirstFragment_to_SecondFragment);

                            System.out.println("Hallo " + db.collection("kids").document(ids.get(position)).get());
//                        if(hoi
//                        if(hoi.get("found").equals(true))
                            view.findViewById(R.id.list_item).setBackgroundColor(Color.parseColor("#00FF00"));
                            db.collection("kids").document(ids.get(position)).update("found", true);
//                        SharedPreferences.Editor editor = sharedPreferences.edit();
//                        editor.putBoolean("kidLost", true);
//                        editor.apply();
//                        buttonLost.setVisibility(View.GONE);
//                        buttonFound.setVisibility(View.VISIBLE);
//
//                        startActivity(new Intent(getActivity(), MicActivity.class));
                        }
                    });
            builder.setNegativeButton("Annuleren", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }

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


