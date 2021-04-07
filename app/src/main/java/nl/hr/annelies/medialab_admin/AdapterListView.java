package nl.hr.annelies.medialab_admin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class AdapterListView extends BaseAdapter {

    Context context;
    ArrayList<ListItemModel> arr;
    FirebaseFirestore db;
    FirebaseStorage storage;
    StorageReference storageRef;
    boolean dHasMessage;
    MediaPlayer mediaPlayer;
    String fileName = "/sdcard/myaudio.3gp";
    File localFile = null;



    public AdapterListView(Context context, ArrayList<ListItemModel> arr) {
            this.context = context;
            this.arr = arr;

            // storage
            storage = FirebaseStorage.getInstance();
            storageRef = storage.getReference();


            // firestore database
            db = FirebaseFirestore.getInstance();
            System.out.println(db.collection("kids").get());

        }

        @Override
        public int getCount() {
            return arr.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

            TextView id = convertView.findViewById(R.id.item_id);
            Button playMessage = convertView.findViewById(R.id.item_has_message);
            Button markFound = convertView.findViewById(R.id.button_found);

            playMessage.setVisibility(GONE);

            id.setText(arr.get(position).getId());
            if(arr.get(position).getFound() == true) {
                convertView.setBackgroundColor(Color.parseColor("#00FF00"));
                markFound.setVisibility(GONE);
                playMessage.setVisibility(VISIBLE);
            }

            if(arr.get(position).getHasMessage() == true) {
                playMessage.setText("Bericht");
                playMessage.setEnabled(true);
            } else {
                playMessage.setText("Nog geen bericht");
                playMessage.setEnabled(false);
            }
//
            markFound.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        db.collection("kids").document(arr.get(position).getId()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                if(task.isSuccessful()) {
                                    DocumentSnapshot doc = task.getResult();
                                    if(doc.contains("hasMessage")) {
                                        System.out.println(doc.getBoolean("hasMessage"));
                                        dHasMessage = doc.getBoolean("hasMessage");
                                    } else {
                                        Log.d("ERROR", "Doc doesn't exist");
                                    }
                                } else {
                                    Log.d("ERROR", "Error: ", task.getException());
                                }
                            }
                        });

                        System.out.println("no message yet");
                        Log.i("Hello", "You clicked item: " + id + "at position: " + position);
                        System.out.println(arr.get(position).getId());
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setCancelable(true)
                                .setTitle("Weet u zeker dat dit kind gevonden is?")
                                .setMessage("U staat op het punt om bij de ouders te melden dat dit kind gevonden is: " + arr.get(position).getId())
                                .setPositiveButton("Kind is gevonden",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                System.out.println("Hallo " + db.collection("kids").document(arr.get(position).getId()).get());
                                                db.collection("kids").document(arr.get(position).getId()).update("found", true);
//
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
                });

//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//            }

            playMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    StorageReference pathReference = storageRef.child("Audio/" + arr.get(position).getId() + ".3gp");
                    System.out.println("Path: " + pathReference);


                    File localFile = new File("/sdcard/", "new_audio.3gp");
                    try {
                        localFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pathReference.getFile(localFile);

                    System.out.println("downloaded " + localFile);
                    fileName = localFile.toString();

                    try {
                        System.out.println("filename " + fileName);
                        playAudio(v);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        return convertView;
}


        public void playAudio(View view) throws  IOException
        {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(fileName);
            mediaPlayer.prepare();
            mediaPlayer.start();
        }

}
