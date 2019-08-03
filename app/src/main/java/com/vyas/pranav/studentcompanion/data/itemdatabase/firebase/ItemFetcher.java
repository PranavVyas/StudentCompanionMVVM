package com.vyas.pranav.studentcompanion.data.itemdatabase.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class ItemFetcher {

    private final FirebaseFirestore mFirestoreDatabase;
    private final CollectionReference mCollectionReference;

    public ItemFetcher() {
        Logger.clearLogAdapters();
        Logger.addLogAdapter(new AndroidLogAdapter());
        mFirestoreDatabase = FirebaseFirestore.getInstance();
        mCollectionReference = mFirestoreDatabase.collection(Constants.PATH_SELL_SVNIT);
        Task<QuerySnapshot> querySnapshotTask = mCollectionReference.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    QuerySnapshot documentSnapshot = task.getResult();
                    List<DocumentSnapshot> documents = documentSnapshot.getDocuments();
                    List<ItemModel> books = new ArrayList<>();
                    if (!documents.isEmpty()) {
                        for (int i = 0; i < documents.size(); i++) {
                            DocumentSnapshot temp = documents.get(i);
                            ItemModel itemModel = temp.toObject(ItemModel.class);
                            books.add(itemModel);
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
