package com.vyas.pranav.studentcompanion.repositories;

import android.content.Context;

import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.data.bookdatabase.firebase.BooksFetcher;

public class MarketPlaceRepository {
    private Context context;

//    private FirebaseDatabase mDb;
//    private DatabaseReference mRef;

    public MarketPlaceRepository(Context context) {
        this.context = context;
//        mDb = FirebaseDatabase.getInstance();
//        mRef = mDb.getReference("Root");
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        BooksFetcher booksFetcher = new BooksFetcher();
        //getList();
    }

//    public void getList(){
//        List<BookModel> books = new ArrayList<>();
//        mRef = mRef.child("MarketPlace");
//        mRef.addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(context, "children is "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
//                Logger.d("Childern is : "+dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(context, "children is "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
//                Logger.d("Childern is : "+dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//                Toast.makeText(context, "children is "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
//                Logger.d("Childern is : "+dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                Toast.makeText(context, "children is "+dataSnapshot.getChildrenCount(), Toast.LENGTH_SHORT).show();
//                Logger.d("Childern is : "+dataSnapshot.getChildrenCount());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }
}
