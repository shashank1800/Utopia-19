package com.rnsit.utopia.EndlessRecycler;

import android.widget.AbsListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rnsit.utopia.AdapterObjects.TechObject;
import com.rnsit.utopia.MainActivity;
import com.rnsit.utopia.Results;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
public abstract class TechEndlessRecyclerOnScrollListener extends RecyclerView.OnScrollListener{

    private static final int TOTAL_ITEM_EACH_LOAD = 10;
    private boolean isScrolling = false;
    private boolean isLastItemReached = false;
    private LinearLayoutManager mLinearLayoutManager;
    private FirebaseFirestore db;

    public TechEndlessRecyclerOnScrollListener(LinearLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
            isScrolling = true;
        }
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        int firstVisibleItemPosition = mLinearLayoutManager.findFirstVisibleItemPosition();
        int visibleItemCount = mLinearLayoutManager.getChildCount();
        int totalItemCount = mLinearLayoutManager.getItemCount();

        if (MainActivity.isScrollListenerEnabled && isScrolling && (firstVisibleItemPosition + visibleItemCount == totalItemCount) && !isLastItemReached) {
            MainActivity.isScrollListenerEnabled = false;
            isScrolling = false;

            Query nextQuery = db.collection("Technical")
                    .orderBy("timeStamp",Query.Direction.DESCENDING)
                    .startAfter(Results.lastVisible)
                    .limit(TOTAL_ITEM_EACH_LOAD);
            nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> t) {
                    if (t.isSuccessful()) {
                        for (DocumentSnapshot document : t.getResult()) {
                            TechObject mTechObject = document.toObject(TechObject.class);
                            Results.techs.add(mTechObject);
                        }
                        MainActivity.isScrollListenerEnabled = true;
                        Results.mTechViewAdapter.notifyDataSetChanged();
                        if(!(t.getResult().size()==0))
                            Results.lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                        if (t.getResult().size()+2< TOTAL_ITEM_EACH_LOAD) {
                            isLastItemReached = true;
                        }
                    }
                }
            });
        }
    }
    public abstract void onLoadMore();
}