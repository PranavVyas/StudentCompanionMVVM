package com.vyas.pranav.studentcompanion.data.bookdatabase.firebase;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class BooksFetcher {

    private FirebaseFirestore mFirestoreDatabase;
    private CollectionReference mCollectionReference;

    public BooksFetcher() {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        mFirestoreDatabase = FirebaseFirestore.getInstance();
        mCollectionReference = mFirestoreDatabase.collection("sell");
        Task<QuerySnapshot> querySnapshotTask = mCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot documentSnapshot = task.getResult();
                    List<DocumentSnapshot> documents = documentSnapshot.getDocuments();
                    List<BookModel> books = new ArrayList<>();
                    if (!documents.isEmpty()) {
                        for (int i = 0; i < documents.size(); i++) {
                            DocumentSnapshot temp = documents.get(i);
                            BookModel bookModel = temp.toObject(BookModel.class);
                            books.add(bookModel);
                        }
                    }
                    Logger.d("Books has total Member variables as " + books.size());
                } else {
                    Logger.d("Error Occurred");
                }
            }
        });
    }
}
