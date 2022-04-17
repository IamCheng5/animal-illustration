package com.iamcheng5.pet;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.security.cert.TrustAnchor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class BookFragment extends Fragment {
    private boolean btnLock = true, itemLock = true;
    private SpeciesAdapter speciesAdapter;
    private BreedAdapter breedAdapter;
    private RecyclerView recBook;
    int process = 0, speciesPosition = -1, breedPosition = -1;
    private TextView[] tvFeatureLabel;
    private TextView[] tvFeature;
    private TextView tvName;
    private ImageView ivDetail;
    private RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true);
    List<String> speciesList = Arrays.asList("鳥|bird", "貓|cat", "狗|dog", "魚|fish", "馬|horse", "鼠|mouse", "兔|rabbit", "羊|sheep", "蛇|snake");
    List<Breed> breedList = new ArrayList<>();
    List<String> breedColumn = new ArrayList<>();
    FirebaseAuth mAuth;
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;
    private ScrollView scrollDetail;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getReadableDatabase();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_book, container, false);
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), backPressedCallback);
        ((TextView) rootView.findViewById(R.id.bookFgm_tv_email)).setText(mAuth.getCurrentUser().getEmail());
        recBook = rootView.findViewById(R.id.bookFgm_rec);
        scrollDetail = rootView.findViewById(R.id.bookFgm_scroll_detail);
        ivDetail = rootView.findViewById(R.id.bookFgm_iv_detail);
        tvName = rootView.findViewById(R.id.bookFgm_tv_name);
        tvFeature = new TextView[]{rootView.findViewById(R.id.bookFgm_tv_feature1), rootView.findViewById(R.id.bookFgm_tv_feature2), rootView.findViewById(R.id.bookFgm_tv_feature3)};
        tvFeatureLabel = new TextView[]{rootView.findViewById(R.id.bookFgm_tv_feature1_label), rootView.findViewById(R.id.bookFgm_tv_feature2_label), rootView.findViewById(R.id.bookFgm_tv_feature3_label)};
        initRecyclerView();
        return rootView;
    }

    private void initRecyclerView() {
        recBook.setDrawingCacheEnabled(true);
        recBook.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        recBook.setHasFixedSize(true);
        recBook.setLayoutManager(new MyLinearLayoutManager(getContext()));
        speciesAdapter = new SpeciesAdapter(speciesList);
        speciesAdapter.setOnItemClickListener(speciesItemClickListener);
        breedAdapter = new BreedAdapter();
        breedAdapter.setOnItemClickListener(breedItemClickListener);
        recBook.setAdapter(speciesAdapter);
    }

    private void loadProcess() {
        if (process == 0) {
            recBook.setAdapter(speciesAdapter);
            speciesAdapter.updateData(speciesList);
            scrollDetail.setVisibility(View.INVISIBLE);
            recBook.setVisibility(View.VISIBLE);
        } else if (process == 1) {
            Cursor cursor = database.rawQuery("select * from " + speciesList.get(speciesPosition).split("\\|")[1], null);
            breedList.clear();
            breedColumn.clear();
            breedColumn.addAll(Arrays.asList(cursor.getColumnName(2),cursor.getColumnName(3),cursor.getColumnName(4)));
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                do {
                    breedList.add(new Breed(cursor.getInt(0), cursor.getString(1), new String[]{cursor.getString(2), cursor.getString(3), cursor.getString(4)}, cursor.getString(5)));
                } while (cursor.moveToNext());
            }
            recBook.setAdapter(breedAdapter);
            breedAdapter.updateData(breedList);
            scrollDetail.setVisibility(View.INVISIBLE);
            recBook.setVisibility(View.VISIBLE);
        } else if (process == 2) {
            scrollDetail.scrollTo(0,0);
            Breed breed = breedList.get(breedPosition);
            Glide.with(getContext()).asDrawable().load(breed.getUrl()).transition(withCrossFade()).apply(requestOptions).into(ivDetail);
            tvName.setText(breed.getName());
            for(int i=0;i<3;i++){
                tvFeature[i].setText(breed.getFeature()[i]);
                tvFeatureLabel[i].setText(breedColumn.get(i));
            }
            scrollDetail.setVisibility(View.VISIBLE);
            recBook.setVisibility(View.INVISIBLE);
        }
    }

    private final SpeciesAdapter.OnItemClickListener speciesItemClickListener = position -> {
        process = 1;
        speciesPosition = position;
        loadProcess();
    };

    private final BreedAdapter.OnItemClickListener breedItemClickListener = position -> {
        process = 2;
        breedPosition = position;
        loadProcess();
    };

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requireActivity().getOnBackPressedDispatcher().addCallback(this, backPressedCallback);
            btnLock = true;
        } else {
        }
    }

    private final OnBackPressedCallback backPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if (process == 0) {
                new CustomDialog2(getContext(),
                        getString(R.string.dialog_message_quit_message),
                        getString(R.string.dialog_message_yes_text),
                        getString(R.string.dialog_message_no_text),
                        CODE -> {
                            if (CODE == CustomDialog2.POSITIVE_BUTTON_CLICK) {
                                getActivity().finish();
                                System.exit(0);
                            }

                        }).show();
            } else {
                process -= 1;
                loadProcess();
            }
        }
    };


}