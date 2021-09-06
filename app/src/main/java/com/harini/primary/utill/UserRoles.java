package com.harini.primary.utill;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class UserRoles {

    private static final String TAG = "setuserroles";
    private String role;
    private String uid;
    private FirebaseFirestore db;


    public UserRoles(String role, String uid) {
        this.role = role;
        this.uid = uid;
        db = FirebaseFirestore.getInstance();
    }


    public void setUserRole(){

        Map<String, Object> userRole = new HashMap<>();
        userRole.put("role", role);

        db.collection("userRole")
                .document(uid)
                .set(userRole)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onFailure: userrole set  ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: setuserrole fail "+e.getLocalizedMessage());
            }
        });



              /*  .add(userRole)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d(TAG, "onFailure: userrole set  ");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Log.d(TAG, "onFailure: setuserrole fail "+e.getLocalizedMessage());
            }
        });
*/

    }
}
