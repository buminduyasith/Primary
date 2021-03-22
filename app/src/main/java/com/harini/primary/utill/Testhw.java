//package com.harini.primary.utill;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Bundle;
//import android.provider.MediaStore;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ProgressBar;
//import android.widget.Toast;
//
//import com.google.android.gms.tasks.Continuation;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.OnFailureListener;
//import com.google.android.gms.tasks.OnSuccessListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.storage.FirebaseStorage;
//import com.google.firebase.storage.OnPausedListener;
//import com.google.firebase.storage.OnProgressListener;
//import com.google.firebase.storage.StorageReference;
//import com.google.firebase.storage.UploadTask;
//import com.harini.primary.R;
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//
//import java.io.ByteArrayOutputStream;
//import java.io.IOException;
//
//
//
//public class Testhw extends AppCompatActivity {
//
//    private FirebaseStorage fstorage;
//
//    private Button btnupload;
//    private ProgressBar loading_bar;
//
//    private final static int GALLERY_REQUEST_CODE = 2;
//    private final static int FILE_REQUEST_CODE = 3;
//    private final static String TAG="fstoragee";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_ad_home_work);
//
//        btnupload = findViewById(R.id.btnupload);
//        loading_bar = findViewById(R.id.loading_bar);
//        loading_bar.setVisibility(View.GONE);
//
//        fstorage = FirebaseStorage.getInstance();
//
//
//
//        btnupload.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                openfile();
//
//                //opengallery();
//            }
//        });
//    }
//
//
//    private void upload(Bitmap bitmap){
//
//        loading_bar.setVisibility(View.VISIBLE);
//        StorageReference storageReference = fstorage.getReference();
//
//        if(bitmap==null){
//            Log.d(TAG, "upload: bitmal null");
//            return;
//        }
//
//        StorageReference dpref = storageReference.child("images");
//        StorageReference ref = dpref.child("images/newimg25");
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
//        byte[] data = baos.toByteArray();
//
//        UploadTask uploadTask = dpref.putBytes(data);
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                loading_bar.setVisibility(View.GONE);
//
//                Log.d(TAG, "onFailure: "+e.getLocalizedMessage());
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                loading_bar.setVisibility(View.GONE);
//
//                Log.d(TAG, "onSuccess: "+taskSnapshot.getUploadSessionUri());
//                Log.d(TAG, "onSuccess: "+taskSnapshot.getBytesTransferred());
//                Log.d(TAG, "onSuccess: "+taskSnapshot.getMetadata().getPath());
//
//
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//
//                Log.d(TAG, "onProgress: "+snapshot.getBytesTransferred());
//            }
//        });
//
//
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//
//                return ref.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    Log.d(TAG, "onComplete: downloaduri"+downloadUri);
//                } else {
//                    // Handle failures
//                    // ...
//
//                    Log.d(TAG, "onComplete: fail downloaduri");
//                }
//            }
//        });
//
//
//
//
//        /*
//          Log.d(TAG, "upload: "+exception.getLocalizedMessage());
//            Log.d(TAG, "upload: "+exception.getMessage());
//            Log.d(TAG, "upload: "+exception.getCause());
//        }
//         */
//
//
//    }
//
//    private void opengallery(){
//
//        Intent galleyIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(galleyIntent, GALLERY_REQUEST_CODE);
//    }
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.d(TAG, "onActivityResult: reqcode"+requestCode);
//        if (resultCode == RESULT_OK) {
//
//            if (requestCode == GALLERY_REQUEST_CODE) {
//
//                Log.d(TAG, "onActivityResult:cameta worked ");
//
//
//                Log.d(TAG, "onActivityResult:cameta worked bitmap");
//                /* Bitmap bitmap = (Bitmap) data.getExtras().get("data");*/
//
//                Uri imageUri = data.getData();
//                Bitmap bitmap = null;
//                try {
//                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//                if(bitmap==null){
//                    Log.d(TAG, "onActivityResult: bitmap null ");
//                    return;
//                }
//
//                upload(bitmap);
//
//
//            }
//
//            else if(requestCode == FILE_REQUEST_CODE){
//
//                Uri uri = data.getData();
//
//                uploadfile(uri);
//
//                Log.d(TAG, "onActivityResult: uri"+uri);
//            }
//
//        }
//        else if(resultCode == RESULT_CANCELED){
//            Log.d(TAG, "onActivityResult: cancel");
//        }
//
//
//
//    }
//
//    private void openfile(){
//        Intent galleyIntent = new Intent();
//        galleyIntent.setAction(Intent.ACTION_GET_CONTENT);
//        galleyIntent.setType("application/pdf");
//        startActivityForResult(galleyIntent, FILE_REQUEST_CODE);
//    }
//
//    private void uploadfile(Uri uri){
//        StorageReference storageReference = fstorage.getReference();
//
//        StorageReference dpref = storageReference.child("files");
//        //StorageReference ref = dpref.child("images/newimg25");
//
//
//        //add timestampe to file path
//        StorageReference riversRef = dpref.child("homework/"+uri.getLastPathSegment());
//
//
//        UploadTask uploadTask = riversRef.putFile(uri);
//
//
//        uploadTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//                Log.d(TAG, "onFailure: "+e.getMessage());
//            }
//        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                Log.d(TAG, "onSuccess: "+taskSnapshot.getMetadata().getPath());
//
//            }
//        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
//                Log.d(TAG, "Upload is " + progress + "% done");
//            }
//        }).addOnPausedListener(new OnPausedListener<UploadTask.TaskSnapshot>() {
//            @Override
//            public void onPaused(@NonNull UploadTask.TaskSnapshot snapshot) {
//                Log.d(TAG, "Upload paused"+snapshot.getBytesTransferred()+"upload and total"+snapshot.getTotalByteCount());
//
//            }
//        });
//
//
//        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
//            @Override
//            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
//                if (!task.isSuccessful()) {
//                    throw task.getException();
//                }
//
//                // Continue with the task to get the download URL
//                return riversRef.getDownloadUrl();
//            }
//        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
//            @Override
//            public void onComplete(@NonNull Task<Uri> task) {
//                if (task.isSuccessful()) {
//                    Uri downloadUri = task.getResult();
//                    Log.d(TAG, "onComplete: link "+downloadUri);
//                } else {
//                    // Handle failures
//                    Log.d(TAG, "onComplete: link"+task.getException().getMessage());
//                    // ...
//                }
//            }
//        });
//
//
//
//    }
//}