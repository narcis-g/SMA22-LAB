package com.upt.cti.smartwallet.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.upt.cti.smartwallet.R;
import com.upt.cti.smartwallet.model.AppState;
import com.upt.cti.smartwallet.model.Payment;
import com.upt.cti.smartwallet.model.PaymentType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class AddPaymentActivity extends Activity {

    private ImageView iIcon;
    private Button bAddPhoto;
    private EditText eName, eCost;
    private Spinner sType;
    private TextView tTimestamp;
    private Payment payment;
    private Button bDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_payment);
        setTitle("Add or edit payment");

        // ui
        iIcon = (ImageView) findViewById(R.id.iIcon);
        bAddPhoto = (Button) findViewById(R.id.bAddPhoto);
        eName = (EditText) findViewById(R.id.eName);
        eCost = (EditText) findViewById(R.id.eCost);
        sType = (Spinner) findViewById(R.id.sType);
        tTimestamp = (TextView) findViewById(R.id.tTimestamp);
        bDelete = findViewById(R.id.bDelete);

        // spinner adapter
        String[] types = PaymentType.getTypes();
        final ArrayAdapter<String> sAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, types);
        sAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sType.setAdapter(sAdapter);

        // initialize UI if editing
        payment = AppState.get().getCurrentPayment();

        if (payment != null) {
            eName.setText(payment.getName());
            eCost.setText(String.valueOf(payment.getCost()));
            tTimestamp.setText("Time of payment: " + payment.timestamp);

            if (payment.getIconUrl() != null) {
                bAddPhoto.setVisibility(View.VISIBLE);
                loadIcon();
            } else {
                iIcon.setVisibility(View.GONE);
            }

            try {
                sType.setSelection(Arrays.asList(types).indexOf(payment.getType()));
            } catch (Exception e) {
            }
        } else {
            tTimestamp.setText("");
            bDelete.setVisibility(View.GONE);
        }


        // Create a storage reference from our app
//        FirebaseStorage storage = FirebaseStorage.getInstance();
//        StorageReference storageRef = storage.getReferenceFromUrl("gs://smart-wallet-4f214.appspot.com");

        // Create a reference to "icon.jpg"
        //StorageReference iconRef = storageRef.child("icon.jpg");

        // Create a reference to the full path of the icon
        //StorageReference iconPathRef = storageRef.child("wallet/<uid>/<timestamp>/icon.jpg");

        // While the file names are the same, the references point to different files
        //iconRef.getName().equals(iconPathRef.getName());    // true
        //iconRef.getPath().equals(iconPathRef.getPath());    // false

        // Get the data from an ImageView as bytes
//        iIcon.setDrawingCacheEnabled(true);
//        iIcon.buildDrawingCache();
//        Bitmap bitmap = iIcon.getDrawingCache();
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = iconRef.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            }
//        });
//
//        Uri file = Uri.fromFile(new File("path/to/images/icon.jpg"));
//        StorageReference riversRef = storageRef.child("wallet/<uid>/<timestamp>/" + file.getLastPathSegment());
//        uploadTask = riversRef.putFile(file);
//
//        // Register observers to listen for when the download is done or if it fails
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle unsuccessful uploads
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
//            }
//        });

//        StorageReference iconRef = storageRef.child("wallet/<uid>/<timestamp>/icon.jpg");
//
//        final long ONE_MEGABYTE = 1024 * 1024;
//        iconRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
//            @Override
//            public void onSuccess(byte[] bytes) {
//                // Data for "icon.jpg" returns, use this as needed
//                iIcon.setImageDrawable(new BitmapDrawable(getResources(), BitmapFactory.decodeByteArray(bytes, 0, bytes.length)));
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//            }
//        });
//
//        Picasso.with(this)
//                .load(payment.getIconUrl())
//                .into(iIcon);
    }

    private void loadIcon() {
        Picasso.with(this)
                .load(payment.getIconUrl())
                .networkPolicy(NetworkPolicy.OFFLINE)
                .into(iIcon, new Callback() {
                    @Override
                    public void onSuccess() {
                    }

                    @Override
                    public void onError() {
                        //Try again online if cache failed
                        Picasso.with(getApplicationContext())
                                .load(payment.getIconUrl())
                                .error(R.drawable.ic_delete)
                                .into(iIcon, new Callback() {
                                    @Override
                                    public void onSuccess() {
                                    }

                                    @Override
                                    public void onError() {
                                        Log.v("Picasso", "Could not fetch image");
                                    }
                                });
                    }
                });
    }

    public void clicked(View view) {

        switch (view.getId()) {
            case R.id.bSave:
                if (payment != null) // editing existing entry
                    save(payment.timestamp);
                else // add new entry
                    save(AppState.getCurrentTimeDate());
                break;
            case R.id.bDelete:
                if (payment != null)
                    delete(payment.timestamp);
                else
                    Toast.makeText(this, "Payment does not exist", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void save(String timestamp) {
        String name = eName.getText().toString();
        String cost = eCost.getText().toString();
        String type = sType.getSelectedItem().toString();

        if (!name.isEmpty() && !cost.isEmpty() && !type.isEmpty()) {

            Map<String, Object> map = new HashMap<>();
            map.put("name", name);
            map.put("cost", Double.parseDouble(cost));
            map.put("type", type);
            map.put("timestamp", timestamp);

            AppState.get().getDatabaseReference().child("wallet")
                    .child(AppState.get().getUserId()).child(timestamp).updateChildren(map);
            finish();

        } else {
            Toast.makeText(this, "Please fill in all information", Toast.LENGTH_SHORT).show();
        }
    }

    private void delete(String timestamp) {
        AppState.get().getDatabaseReference().child("wallet")
                .child(AppState.get().getUserId()).child(timestamp).removeValue();
        finish();
    }
}
