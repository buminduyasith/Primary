//package com.harini.primary.adapters;
//
//
//import android.text.Editable;
//import android.text.TextWatcher;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.google.firebase.firestore.DocumentSnapshot;
//import com.harini.primary.R;
//import com.harini.primary.models.ExamDetails;
//import com.harini.primary.models.StudentMarks;
//
//import java.util.ArrayList;
//
//public class StudentAddMarkAdapter extends RecyclerView.Adapter<StudentAddMarkAdapter.StudentAddMarkViewHolder> {
//
////    private ArrayList<StudentMarks> studentMarksArrayList;
//    public static ArrayList<StudentMarks> studentMarksArrayList;
//    private static  ArrayList<ExamDetails> examDetailsList ;
//    private StudentAddMarkAdapter.onItemClickListener mlistner;
//    private static final String TAG = "exammarks";
//    public  interface  onItemClickListener{
//        void onItemClick(StudentMarks studentMarks, int position);
//
//    }
//
//    public void setOnItemClickListner(StudentAddMarkAdapter.onItemClickListener listner){
//        mlistner = listner;
//    }
//
//
//    public StudentAddMarkAdapter(ArrayList<StudentMarks> studentMarks) {
//        this.studentMarksArrayList = studentMarks;
//        Log.d(TAG, "MyCardAdapter:cartsize "+studentMarks.size());
//        examDetailsList = new ArrayList<ExamDetails>();
//    }
//
//    @Override
//    public StudentAddMarkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_student_marks,parent,false);
//
//        if(v==null){
//            Log.d(TAG, "onCreateViewHolder: not null");
//        }
//
//        StudentAddMarkAdapter.StudentAddMarkViewHolder bestSellersViewHolder = new  StudentAddMarkAdapter.StudentAddMarkViewHolder(v,mlistner);
//
//        return bestSellersViewHolder;
//    }
//
//    @Override
//    public void onBindViewHolder(  StudentAddMarkAdapter.StudentAddMarkViewHolder holder, int position) {
//
//        StudentMarks sm = studentMarksArrayList.get(position);
//
//
//        holder.txtStudentName.setText(sm.getStudentName());
//        holder.etmark.setText(String.valueOf(sm.getMark()));
//        holder.txthideId.setText(sm.getStudentId());
//
//
//
//
//
//
//    }
//
//    @Override
//    public int getItemCount() {
//        return  studentMarksArrayList.size();
//    }
//
//    private static class List<T> {
//    }
//
//
//    public class StudentAddMarkViewHolder extends RecyclerView.ViewHolder {
//
//        public TextView txtStudentName,txthideId;
//        public EditText etmark;
//
//        public StudentAddMarkViewHolder( View itemView, onItemClickListener listener) {
//            super(itemView);
//            txtStudentName = itemView.findViewById(R.id.txtStudentName);
//            txthideId = itemView.findViewById(R.id.txtStudentName);
//            etmark = itemView.findViewById(R.id.etmark);
//
//            etmark.addTextChangedListener(new TextWatcher() {
//                @Override
//                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                }
//
//                @Override
//                public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    if(!etmark.getText().toString().isEmpty()){
//
//                        //ArrayList<StudentMarks> tempstudentMarksArrayList = studentMarksArrayList;
//                      //  int pos = getAdapterPosition();
//                      //  StudentMarks studentMarks = studentMarksArrayList.get(getAdapterPosition());
//                        studentMarksArrayList.get(getAdapterPosition()).setMark(Integer.valueOf(etmark.getText().toString()));
//                        //studentMarks.setMark((Integer.valueOf(etmark.getText().toString())));
//
//                      //  studentMarksArrayList.set(getAdapterPosition(),studentMarks);
//
//                       // studentMarksArrayList = tempstudentMarksArrayList;
//                       // studentMarksArrayList.set(getAdapterPosition(),studentMarks);
//
//                       // Log.d(TAG, "onTextChanged: "+studentMarksArrayList.toString());
//                        //studentMarksArrayList = tempstudentMarksArrayList;
//                        //studentMarksArrayList.add(pos,studentMarks);
//
////                        ExamDetails examDetails = new ExamDetails(studentMarksArrayList,"sinhala");
////                        examDetailsList.add(examDetails);
//                    }
//
//                }
//
//                @Override
//                public void afterTextChanged(Editable s) {
//
//                }
//            });
//
//
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                    if(listener != null){
//                        int pos = getAdapterPosition();
//                        if(pos!=RecyclerView.NO_POSITION){
//                            listener.onItemClick(studentMarksArrayList.get(pos),pos);
//                        }
//                    }
//
//
//                }
//            });
//
//        }
//    }
//}