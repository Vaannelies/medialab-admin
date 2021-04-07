package nl.hr.annelies.medialab_admin;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseStorage storage;
    ListView listView;
    ArrayList<ListItemModel> ids;

    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // storage
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // ask for permission
        // request permission for recording audio
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 200);


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
                ids = new ArrayList<>();

                for (QueryDocumentSnapshot document : value) {
                    if (document.get("id") != null) {
                        String idString = document.getData().get("id").toString();
                        Log.d("STRING ID", idString);
                        System.out.println("FOUND: " + document.getBoolean("found"));
                        System.out.println("HASMESSAGE: " + document.getBoolean("hasMessage"));
                        ids.add(new ListItemModel(idString, document.getBoolean("found"), document.getBoolean("hasMessage")));
                    }
                    Log.d("DOCUMENT", document.getId() + " => " + document.getData().get("id"));
                }

                listView = findViewById(R.id.list_view);
                AdapterListView adapter = new AdapterListView(MainActivity.this, ids);
                listView.setAdapter(adapter);
            }
        });
    }


}


