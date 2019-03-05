package com.rnsit.utopia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.rnsit.utopia.AdapterObjects.CFLObject;
import com.rnsit.utopia.AdapterObjects.SportsObject;
import com.rnsit.utopia.AdapterObjects.TechObject;
import com.rnsit.utopia.Adapters.CFLViewAdapter;
import com.rnsit.utopia.Adapters.SportsViewAdapter;
import com.rnsit.utopia.Adapters.TechViewAdapter;

import java.util.ArrayList;

public class Results extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    public BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private DrawerLayout mDrawerLayout;
    private Context context;
    private FirebaseFirestore db;
    private Query query;
    private static final int TOTAL_ITEM_EACH_LOAD = 7;
    public static DocumentSnapshot lastVisible;

    private RecyclerView mRecyclerFun,mRecyclerSports,mRecyclerCult,mRecyclerTech,mRecyclerLit;
    /*private FunViewAdapter mFunViewAdapter;
    private CultViewAdapter mCultViewAdapter;
    private LitViewAdapter mLitViewAdapter;*/
    private SportsViewAdapter mSportsViewAdapter;
    private TechViewAdapter mTechViewAdapter;
    private CFLViewAdapter mCFLViewAdapter;

    private LinearLayoutManager linearLayoutManager1,linearLayoutManager2,linearLayoutManager3,linearLayoutManager4,linearLayoutManager5;

    /*private ArrayList<FunObject> funs;
    private ArrayList<CultObject> cults;
    private ArrayList<LitObject> lits;*/
    private ArrayList<SportsObject> sports;
    private ArrayList<TechObject> techs;
    private ArrayList<CFLObject> cflObjects;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*funs = new ArrayList<FunObject>();
        cults = new ArrayList<CultObject>();
        lits = new ArrayList<LitObject>();*/
        sports = new ArrayList<SportsObject>();
        techs = new ArrayList<TechObject>();
        cflObjects = new ArrayList<CFLObject>();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNav);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        mRecyclerFun = (RecyclerView) findViewById(R.id.rec_fun);
        mRecyclerCult = (RecyclerView) findViewById(R.id.rec_cult);
        mRecyclerSports = (RecyclerView) findViewById(R.id.rec_sports);
        mRecyclerLit = (RecyclerView) findViewById(R.id.rec_lit);
        mRecyclerTech = (RecyclerView) findViewById(R.id.rec_tech);

        mRecyclerFun.setHasFixedSize(true);
        mRecyclerCult.setHasFixedSize(true);
        mRecyclerSports.setHasFixedSize(true);
        mRecyclerLit.setHasFixedSize(true);
        mRecyclerTech.setHasFixedSize(true);

        linearLayoutManager1 = new LinearLayoutManager(context);
        linearLayoutManager1.setOrientation(RecyclerView.VERTICAL);

        linearLayoutManager2 = new LinearLayoutManager(context);
        linearLayoutManager2.setOrientation(RecyclerView.VERTICAL);

        linearLayoutManager3 = new LinearLayoutManager(context);
        linearLayoutManager3.setOrientation(RecyclerView.VERTICAL);

        linearLayoutManager4 = new LinearLayoutManager(context);
        linearLayoutManager4.setOrientation(RecyclerView.VERTICAL);

        linearLayoutManager4 = new LinearLayoutManager(context);
        linearLayoutManager4.setOrientation(RecyclerView.VERTICAL);

        linearLayoutManager5 = new LinearLayoutManager(context);
        linearLayoutManager5.setOrientation(RecyclerView.VERTICAL);

        mRecyclerFun.setLayoutManager(linearLayoutManager1);
        mRecyclerCult.setLayoutManager(linearLayoutManager2);
        mRecyclerSports.setLayoutManager(linearLayoutManager3);
        mRecyclerLit.setLayoutManager(linearLayoutManager4);
        mRecyclerTech.setLayoutManager(linearLayoutManager5);

        /*mFunViewAdapter = new FunViewAdapter(context,funs);
        mCultViewAdapter = new CultViewAdapter(context,cults);
        mLitViewAdapter = new LitViewAdapter(context,lits);*/
        mSportsViewAdapter = new SportsViewAdapter(context,sports);
        mTechViewAdapter = new TechViewAdapter(context,techs);
        mCFLViewAdapter = new CFLViewAdapter(context,cflObjects);

        mRecyclerFun.setAdapter(mCFLViewAdapter);
        mRecyclerCult.setAdapter(mCFLViewAdapter);
        mRecyclerSports.setAdapter(mSportsViewAdapter);
        mRecyclerLit.setAdapter(mCFLViewAdapter);
        mRecyclerTech.setAdapter(mTechViewAdapter);

        bottomNavigationView.setSelectedItemId(R.id.bot_sports);

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        switch (id){
            case R.id.bot_sports:
                ClearAdapter();
                ClearVisibility();
                mRecyclerSports.setVisibility(View.VISIBLE);

                db = FirebaseFirestore.getInstance();
                query = db.collection("Sports")
                        .limit(TOTAL_ITEM_EACH_LOAD);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                SportsObject  mSportsObject= document.toObject(SportsObject.class);
                                sports.add(mSportsObject);
                            }
                            mSportsViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case R.id.bot_fun:
                ClearAdapter();
                ClearVisibility();
                mRecyclerFun.setVisibility(View.VISIBLE);
                db = FirebaseFirestore.getInstance();
                query = db.collection("Fun")
                        .limit(TOTAL_ITEM_EACH_LOAD);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                CFLObject mCFLObject = document.toObject(CFLObject.class);
                                cflObjects.add(mCFLObject);
                            }
                            mCFLViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;

            case R.id.bot_cultural:
                ClearAdapter();
                ClearVisibility();
                mRecyclerCult.setVisibility(View.VISIBLE);

                db = FirebaseFirestore.getInstance();
                query = db.collection("Cultural")
                        .limit(TOTAL_ITEM_EACH_LOAD);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                CFLObject mCFLObject = document.toObject(CFLObject.class);
                                cflObjects.add(mCFLObject);
                            }
                            mCFLViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;

            case R.id.bot_literature:
                ClearAdapter();
                ClearVisibility();
                mRecyclerLit.setVisibility(View.VISIBLE);

                db = FirebaseFirestore.getInstance();
                query = db.collection("Literature")
                        .limit(TOTAL_ITEM_EACH_LOAD);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                CFLObject mCFLObject = document.toObject(CFLObject.class);
                                cflObjects.add(mCFLObject);
                            }
                            mCFLViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
            case R.id.bot_technical:
                ClearAdapter();
                ClearVisibility();
                mRecyclerTech.setVisibility(View.VISIBLE);

                db = FirebaseFirestore.getInstance();
                query = db.collection("Technical")
                        .limit(TOTAL_ITEM_EACH_LOAD);
                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {
                                TechObject mTechObject = document.toObject(TechObject.class);
                                techs.add(mTechObject);
                            }
                            mTechViewAdapter.notifyDataSetChanged();
                        }
                    }
                });
                break;
        }
        return true;
    }

    private void ClearVisibility() {
        mRecyclerFun.setVisibility(View.GONE);
        mRecyclerCult.setVisibility(View.GONE);
        mRecyclerSports.setVisibility(View.GONE);
        mRecyclerLit.setVisibility(View.GONE);
        mRecyclerTech.setVisibility(View.GONE);
    }

    private void ClearAdapter() {
        /*mFunViewAdapter.clear();
        mCultViewAdapter.clear();
        mLitViewAdapter.clear();*/
        mSportsViewAdapter.clear();
        mTechViewAdapter.clear();
        mCFLViewAdapter.clear();
    }
}
