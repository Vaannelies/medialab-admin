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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
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
    FirebaseStorage storage;
    ListView listView;
    ArrayList<String> ids;
    boolean hasMessage = false;
    StorageReference storageRef;




//    hotspots[0] = new Hotspot("strandwacht", new LatLng(-32, 138.2), "hallo");
//    {name: "hoi", location: new LatLng(32, 42), message: "lol"}];

//    List<{name: String, location: LatLng, message: String}> hotspots = [{name: "strandwacht", location: new LatLng(-32, 138.2), message: "hallo"}];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();


        // firestore database
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

                for (QueryDocumentSnapshot document : value) {
                    if (document.get("id") != null) {
                        String idString = document.getData().get("id").toString();
                        Log.d("STRING ID", idString);
                        if(document.contains("hasMessage") && document.getBoolean("hasMessage").equals(true)) {
                            idString = idString + " - Spraakopname";
                        }
                        ids.add(idString);

                    }

                    Log.d("DOCUMENT", document.getId() + " => " + document.getData().get("id"));

                }

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
                       StorageReference pathReference = storageRef.child("audio/" + ids.get(position) + ".3gp");
                       System.out.println("Path: " + pathReference);
                    } else {
                         Log.d("ERROR", "Doc doesn't exist");
                    }
                } else {
                    Log.d("ERROR", "Error: ", task.getException());
                }
            }
        });

        if(hasMessage == true) {
            System.out.println("Hasmessage is true");
            // Now that you know it has a message, search the storage for the message with the same ID.
            // Download that message
            // use that path as the source
            // play the recording
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

}


