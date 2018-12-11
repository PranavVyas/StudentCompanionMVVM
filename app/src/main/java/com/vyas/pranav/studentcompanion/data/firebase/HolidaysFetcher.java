package com.vyas.pranav.studentcompanion.data.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.firebase.model.HolidayModel;
import com.vyas.pranav.studentcompanion.data.holidaydatabase.HolidayEntry;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;

public class HolidaysFetcher {

    private FirebaseDatabase mFirebaseDatabase;
    private DatabaseReference mDatabaseReference;
    private OnHolidaysReceivedListener listener;

    public HolidaysFetcher() {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseDatabase.getReference("Root");
    }

    public void getHolidayEntries() {
        mDatabaseReference = mDatabaseReference.child("Holidays");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HolidayEntry> holidayEntries = new ArrayList<>();
                mDatabaseReference.removeEventListener(this);
                Logger.d("Children Count : " + dataSnapshot.getChildrenCount());
                Iterable<DataSnapshot> holidaysDataSnapshots = dataSnapshot.getChildren();
                for (DataSnapshot holidaySnapshot :
                        holidaysDataSnapshots) {
                    HolidayModel holiday = holidaySnapshot.getValue(HolidayModel.class);
//                    if(holiday == null){
//                        Logger.d("Some Problem occurred while converting");
//                    }else {
                    Date date = ConverterUtils.convertStringToDate(holiday.getDate());
                    String name = holiday.getName();
                    String day = ConverterUtils.getDayOfWeek(date);
                    HolidayEntry holidayEntry = new HolidayEntry(name, day, date);
                    holidayEntries.add(holidayEntry);
//                    }
                }
                if (listener != null) {
                    listener.onHolidaysReceived(holidayEntries);
                }
                Logger.d("Size of List of Holiday Entry is : " + holidayEntries.size());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Logger.d("Error Occurred : " + databaseError.getDetails());
            }
        });
    }

    public void setOnHolidaysReceivedListener(OnHolidaysReceivedListener listener) {
        this.listener = listener;
    }

    public interface OnHolidaysReceivedListener {
        void onHolidaysReceived(List<HolidayEntry> holidayEntries);
    }
}
