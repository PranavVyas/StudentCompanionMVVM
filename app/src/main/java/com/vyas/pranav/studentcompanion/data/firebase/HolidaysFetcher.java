package com.vyas.pranav.studentcompanion.data.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.firebase.model.HolidayModel;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.ArrayList;
import java.util.List;

public class HolidaysFetcher {

    private final FirebaseFirestore mFirestore;
    private final CollectionReference mCollectionReference;
    //    private FirebaseDatabase mFirebaseDatabase;
//    private DatabaseReference mDatabaseReference;
    private OnHolidaysReceivedListener listener;

    public HolidaysFetcher() {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        mFirestore = FirebaseFirestore.getInstance();
        mCollectionReference = mFirestore.collection("holidays");
//        mFirebaseDatabase = FirebaseDatabase.getInstance();
//        mDatabaseReference = mFirebaseDatabase.getReference("Root");
    }

    public void getHolidayEntries() {
        mCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot result = task.getResult();
                    if (result != null) {
                        List<HolidayEntry> holidays = new ArrayList<>();
                        List<DocumentSnapshot> documents = result.getDocuments();
                        for (int i = 0; i < documents.size(); i++) {
                            DocumentSnapshot holidayDocument = documents.get(i);
                            HolidayModel holidayModel = holidayDocument.toObject(HolidayModel.class);
                            HolidayEntry holiday = new HolidayEntry();
                            holiday.setName(holidayModel.getName());
                            holiday.setDate(ConverterUtils.convertStringToDate(holidayModel.getDate()));
                            holiday.setDay(ConverterUtils.getDayOfWeek(holidayModel.getDate()));
                            holidays.add(holiday);
                        }
                        if (listener != null) {
                            listener.onHolidaysReceived(holidays);
                        }
                    }
                } else {
                    Logger.d("Error Occured while retriving data from the online database");
                }
            }
        });
//        mDatabaseReference = mDatabaseReference.child("Holidays");
//        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                List<HolidayEntry> holidayEntries = new ArrayList<>();
//                mDatabaseReference.removeEventListener(this);
//                Logger.d("Children Count : " + dataSnapshot.getChildrenCount());
//                Iterable<DataSnapshot> holidaysDataSnapshots = dataSnapshot.getChildren();
//                for (DataSnapshot holidaySnapshot :
//                        holidaysDataSnapshots) {
//                    HolidayModel holiday = holidaySnapshot.getValue(HolidayModel.class);
////                    if(holiday == null){
////                        Logger.d("Some Problem occurred while converting");
////                    }else {
//                    Date date = ConverterUtils.convertStringToDate(holiday.getDate());
//                    String name = holiday.getName();
//                    String day = ConverterUtils.getDayOfWeek(date);
//                    HolidayEntry holidayEntry = new HolidayEntry(name, day, date);
//                    holidayEntries.add(holidayEntry);
////                    }
//                }
//                if (listener != null) {
//                    listener.onHolidaysReceived(holidayEntries);
//                }
//                Logger.d("Size of List of Holiday Entry is : " + holidayEntries.size());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Logger.d("Error Occurred : " + databaseError.getDetails());
//            }
//        });
    }

    public void setOnHolidaysReceivedListener(OnHolidaysReceivedListener listener) {
        this.listener = listener;
    }

    public interface OnHolidaysReceivedListener {
        void onHolidaysReceived(List<HolidayEntry> holidayEntries);
    }
}
