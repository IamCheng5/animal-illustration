package com.iamcheng5.pet;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Arrays;
import java.util.List;

public class AdoptFragment extends Fragment implements View.OnClickListener {
    FirebaseAuth mAuth;
    private Button[] btnSelect;
    private ImageButton ibLogout;
    private Button btnAgain, btnLocation;
    private TextView tvQuestion, tvShelter, tvPhone, tvBodyType, tvColor;
    private ConstraintLayout clSelect;
    private ScrollView scrollDetail;
    private ImageView ivSex, ivSpecies, ivPicture;
    private int process = 0;
    private RequestOptions requestOptions = new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true);
    private List<Question> questionList = Arrays.asList(new Question("選擇動物", new String[]{"貓|貓", "狗|狗"}), new Question("選擇性別", new String[]{"公|M", "母|F"}), new Question("選擇體型", new String[]{"大型|BIG", "中型|MEDIUM", "小型|SMALL"}));
    private String[] queryArg = new String[3];
    private DatabaseHelper databaseHelper;
    private SQLiteDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        requireActivity().getOnBackPressedDispatcher().addCallback(this, Callback.getBackPressExitCallback(getContext(), getActivity()));
        databaseHelper = new DatabaseHelper(getContext());
        database = databaseHelper.getReadableDatabase();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            requireActivity().getOnBackPressedDispatcher().addCallback(this, Callback.getBackPressExitCallback(getContext(), getActivity()));
        } else {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_adopt, container, false);
        ((TextView) rootView.findViewById(R.id.adoptFgm_tv_email)).setText(mAuth.getCurrentUser().getEmail());
        btnSelect = new Button[]{rootView.findViewById(R.id.adoptFgm_btn_top), rootView.findViewById(R.id.adoptFgm_btn_center), rootView.findViewById(R.id.adoptFgm_btn_bottom)};
        ibLogout = rootView.findViewById(R.id.adoptFgm_ib_logout);
        btnAgain = rootView.findViewById(R.id.adoptFgm_btn_again);
        clSelect = rootView.findViewById(R.id.adoptFgm_cl_select);
        ivPicture = rootView.findViewById(R.id.adoptFgm_iv_picture);
        btnLocation = rootView.findViewById(R.id.adoptFgm_btn_location);
        tvColor = rootView.findViewById(R.id.adoptFgm_tv_color);
        ivSex = rootView.findViewById(R.id.adoptFgm_iv_sex);
        ivSpecies = rootView.findViewById(R.id.adoptFgm_iv_species);
        tvShelter = rootView.findViewById(R.id.adoptFgm_tv_shelter);
        tvPhone = rootView.findViewById(R.id.adoptFgm_tv_phone);
        tvBodyType = rootView.findViewById(R.id.adoptFgm_tv_bodytype);
        scrollDetail = rootView.findViewById(R.id.adoptFgm_scroll_detail);
        tvQuestion = rootView.findViewById(R.id.adoptFgm_tv_question);
        for (Button button : btnSelect) {
            button.setOnClickListener(this);
        }
        btnAgain.setOnClickListener(this);
        ibLogout.setOnClickListener(this);
        btnLocation.setOnClickListener(this);
        loadProcess();
        return rootView;
    }

    private void loadProcess() {
        if (process < 3) {
            tvQuestion.setVisibility(View.VISIBLE);
            clSelect.setVisibility(View.VISIBLE);
            scrollDetail.setVisibility(View.INVISIBLE);
            Question question = questionList.get(process);
            tvQuestion.setText(question.getQuestion());
            if (question.getOptionCount() == 3) {
                btnSelect[1].setVisibility(View.VISIBLE);
                for (int i = 0; i < 3; i++) {
                    btnSelect[i].setText(question.getOptions()[i].split("\\|")[0]);
                    btnSelect[i].setTag(question.getOptions()[i].split("\\|")[1]);
                }
            } else {
                btnSelect[1].setVisibility(View.GONE);
                btnSelect[0].setText(question.getOptions()[0].split("\\|")[0]);
                btnSelect[0].setTag(question.getOptions()[0].split("\\|")[1]);
                btnSelect[2].setText(question.getOptions()[1].split("\\|")[0]);
                btnSelect[2].setTag(question.getOptions()[1].split("\\|")[1]);
            }
        } else {
            scrollDetail.scrollTo(0, 0);
            tvQuestion.setVisibility(View.INVISIBLE);
            clSelect.setVisibility(View.INVISIBLE);
            scrollDetail.setVisibility(View.VISIBLE);
            String sql = "select * from pet_adopt where kind = \"" + queryArg[0] + "\" AND sex = \"" + queryArg[1] + "\" AND bodytype = \"" + queryArg[2] + "\" AND picture_url NOT NULL ORDER BY RANDOM() limit 1";
            Cursor cursor = database.rawQuery(sql, null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                Glide.with(getContext()).asDrawable().load(cursor.getString(11)).transition(withCrossFade()).apply(requestOptions).into(ivPicture);
                tvShelter.setText(cursor.getString(10));
                tvBodyType.setText(cursor.getString(6));
                tvColor.setText(cursor.getString(7));
                tvPhone.setText(cursor.getString(13));
                ivSex.setImageResource(queryArg[1].equals("M") ? R.drawable.ic_male : R.drawable.ic_female);
                ivSpecies.setImageResource(queryArg[0].equals("狗") ? R.drawable.ic_dog : R.drawable.ic_cat);

            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.adoptFgm_ib_logout) {
            new CustomDialog2(getContext(), getString(R.string.dialog_message_logout_message), getString(R.string.dialog_message_yes_text), getString(R.string.dialog_message_no_text),
                    CODE -> {
                        if (CODE == CustomDialog2.POSITIVE_BUTTON_CLICK) {
                            mAuth.signOut();
                        }
                    }).show();
        } else if (v.getId() == R.id.adoptFgm_btn_again) {
            process = 0;
            loadProcess();
        } else if (v.getId() == R.id.adoptFgm_btn_location) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q="+tvShelter.getText()));
            startActivity(intent);
        } else {
            queryArg[process] = (String) v.getTag();
            process += 1;
            loadProcess();
        }

    }
}