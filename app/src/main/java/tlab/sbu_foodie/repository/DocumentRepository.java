package tlab.sbu_foodie.repository;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

/**
 * Created by ilsung on 8/28/2018.
 */

public class DocumentRepository {

    public static final String VENUE_ID_REF = "venue";
    public static final String ID_REF ="id";
    private FirebaseFirestore db;
    private CollectionReference venueCollection;

    public DocumentRepository() {
        this.db = FirebaseFirestore.getInstance();
        this.venueCollection = db.collection("rating");
    }


    public Query getRatingQuery(String venueName) {

        return  venueCollection.whereEqualTo(VENUE_ID_REF, venueName);
    }

}
