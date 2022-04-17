package com.iamcheng5.pet;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListener = null;
    private Button[] btnTabBar;
    private TextView[] tvTabBar;
    private final int[][] drawableTabBarBtn =
            {{R.drawable.ic_adopt_normal, R.drawable.ic_adopt_focused},
                    {R.drawable.ic_book_normal, R.drawable.ic_book_focus}};
    private int tabBarValue = 0;//紀錄目前翻到哪一個功能頁

    private NavController navController;
    private final int[] tabBarActionId = {R.id.action_global_adoptFragment, R.id.action_global_bookFragment};
    private final String[] tabBarFragmentTag = {"com.iamcheng5.pet.AdoptFragment", "com.iamcheng5.pet.BookFragment"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.setWindowStatusBarColor(this, R.color.white);
        mAuth = FirebaseAuth.getInstance();
        authListener = firebaseAuth -> {
            FirebaseUser user = firebaseAuth.getCurrentUser();
            if (user == null) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }

        };
        navController = getNavController();


        /*findViewById*/
        btnTabBar = new Button[]{findViewById(R.id.mainAct_btn_adopt), findViewById(R.id.mainAct_btn_book)};
        tvTabBar = new TextView[]{findViewById(R.id.mainAct_tv_adopt), findViewById(R.id.mainAct_tv_book)};
        /*setListener*/
        findViewById(R.id.mainAct_ll_adopt).setOnClickListener(this);
        findViewById(R.id.mainAct_ll_book).setOnClickListener(this);

        //navController.navigate(R.id.action_global_bookFragment);
        //navController.navigate(R.id.action_global_adoptFragment);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (authListener != null) {
            mAuth.removeAuthStateListener(authListener);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.mainAct_ll_adopt) {
            switchTabBar(0);
        } else if (v.getId() == R.id.mainAct_ll_book) {
            switchTabBar(1);
        }
    }

    private void switchTabBar(int newTabBarValue) {
        //只要不在前景就顯示
        if (newTabBarValue != tabBarValue || !getForegroundFragment().getTag().equals(tabBarFragmentTag[newTabBarValue])) {
            btnTabBar[tabBarValue].setBackground(ContextCompat.getDrawable(this, drawableTabBarBtn[tabBarValue][0]));
            tvTabBar[tabBarValue].setVisibility(View.INVISIBLE);
            tabBarValue = newTabBarValue;
            btnTabBar[tabBarValue].setBackground(ContextCompat.getDrawable(this, drawableTabBarBtn[tabBarValue][1]));
            tvTabBar[tabBarValue].setVisibility(View.VISIBLE);
            navController.navigate(tabBarActionId[newTabBarValue]);
        } else {
            //TODO 重複點擊
            /*switch (newTabBarValue) {
                case 0:
                    AdoptFragment adoptFragment = (AdoptFragment) getSupportFragmentManager().findFragmentById(R.id.mainAct_nav).getChildFragmentManager().findFragmentByTag("com.iamcheng5.adopet.fragment.AdoptFragment");
                    adoptFragment.repeatClickTabBar();
                    break;
                case 1:
                    FavoriteFragment favoriteFragment = (FavoriteFragment) getSupportFragmentManager().findFragmentById(R.id.mainAct_nav).getChildFragmentManager().findFragmentByTag("com.iamcheng5.adopet.fragment.FavoriteFragment");
                    favoriteFragment.repeatClickTabBar();
                    break;
                case 2:
                    ForumFragment forumFragment = (ForumFragment) getSupportFragmentManager().findFragmentById(R.id.mainAct_nav).getChildFragmentManager().findFragmentByTag("com.iamcheng5.adopet.fragment.ForumFragment");
                    forumFragment.repeatClickTabBar();
            }*/
        }

    }

    public Fragment getForegroundFragment() {
        for (Fragment fragment : getSupportFragmentManager().findFragmentById(R.id.mainAct_nav).getChildFragmentManager().getFragments()) {
            if (fragment.isVisible())
                return fragment;
        }
        return null;
    }

    private NavController getNavController() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.mainAct_nav);
        NavController navController = NavHostFragment.findNavController(fragment);
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(this, fragment.getChildFragmentManager(), fragment.getId());
        NavigatorProvider provider = navController.getNavigatorProvider();
        //由於FixFragmentNavigator的name和原生的一樣，會覆蓋過去
        provider.addNavigator(fragmentNavigator);
        navController.setGraph(R.navigation.navigation_main);
        return navController;
    }




}