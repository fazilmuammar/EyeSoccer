package com.ferhatproduction.eyesoccer.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ferhatproduction.eyesoccer.Class.AnimatorUtils;
import com.ferhatproduction.eyesoccer.Class.Params;
import com.ferhatproduction.eyesoccer.Fragment.ESTabEventsFragment;
import com.ferhatproduction.eyesoccer.Fragment.ESTabHomeFragment;
import com.ferhatproduction.eyesoccer.Fragment.ESTabNewsFragment;
import com.ferhatproduction.eyesoccer.Fragment.ESTabVideoFragment;
import com.ferhatproduction.eyesoccer.Fragment.ESTabWalletFragment;
import com.ferhatproduction.eyesoccer.R;
import com.ogaclejapan.arclayout.ArcLayout;

import java.util.ArrayList;
import java.util.List;

public class ESHome extends AppCompatActivity implements
        ESTabHomeFragment.OnFragmentInteractionListener,
        ESTabNewsFragment.OnFragmentNewsInteractionListener,
        ESTabVideoFragment.OnFragmentVideoInteractionListener,
        ESTabEventsFragment.OnFragmentEventsInteractionListener,
        View.OnClickListener{

    private ViewPagerAdapter adapter;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int lastTabPosition;
    private boolean tabActive = true;
    private ArcLayout arcLayout;
    private View menuLayout;
    private LinearLayout fab;
    private boolean radialMenuIsOpen = false;

    ImageView iconHome;
    ImageView iconVideo;
    ImageView iconNews;
    ImageView iconWallet;

    TextView iconHomeLabel, iconVideoLabel, iconNewsLabel, iconWalletLabel;

    String focusColor = "#E05929";
    String greyColor = "#666666";

    TextView mainTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.es_activity_home);

        adapter = new ESHome.ViewPagerAdapter(getSupportFragmentManager());

        fab = (LinearLayout)findViewById(R.id.fab);
        menuLayout = findViewById(R.id.menu_layout);
        menuLayout.setOnClickListener(this);
        arcLayout = (ArcLayout) findViewById(R.id.arc);

        fab.setOnClickListener(this);

        iconHome = (ImageView)findViewById(R.id.tabIconHome);
        iconVideo = (ImageView)findViewById(R.id.tabIconVideo);
        iconNews = (ImageView)findViewById(R.id.tabIconNews);
        iconWallet = (ImageView)findViewById(R.id.tabIconWallet);

        iconHomeLabel = (TextView)findViewById(R.id.tabIconHomeLabel);
        iconVideoLabel = (TextView)findViewById(R.id.tabIconVideoLabel);
        iconNewsLabel = (TextView) findViewById(R.id.tabIconNewsLabel);
        iconWalletLabel = (TextView) findViewById(R.id.tabIconWalletLabel);

        iconHome.setImageResource(R.drawable.icon_home_f);
        iconHomeLabel.setTextColor(Color.parseColor(focusColor));

        mainTitle = (TextView)findViewById(R.id.mainTitle);

        findViewById(R.id.radialWasit).setOnClickListener(this);
        findViewById(R.id.radialKlub).setOnClickListener(this);


        setupTabs();
    }

    private void setupTabs(){
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);


        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        LinearLayout tab1 = (LinearLayout)findViewById(R.id.tab1Button);
        LinearLayout tab2 = (LinearLayout)findViewById(R.id.tab2Button);
//        LinearLayout tab3 = (LinearLayout)findViewById(R.id.tab3Button);
        LinearLayout tab4 = (LinearLayout)findViewById(R.id.tab4Button);
        LinearLayout tab5 = (LinearLayout)findViewById(R.id.tab5Button);

        tab1.setOnClickListener(this);
        tab2.setOnClickListener(this);
//        tab3.setOnClickListener(this);
        tab4.setOnClickListener(this);
        tab5.setOnClickListener(this);

//        createTabIcons();
        lastTabPosition = 0;
    }

    private void setupViewPager(ViewPager viewPager) {

        adapter.addFragment(new ESTabHomeFragment(), "");
        adapter.addFragment(new ESTabVideoFragment(), "");
        adapter.addFragment(new ESTabNewsFragment(), "");
        adapter.addFragment(new ESTabWalletFragment(), "");
        viewPager.setAdapter(adapter);
    }

    private void createTabIcons() {

        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabOne.setText("Home");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);


        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabTwo.setText("Eye Watch");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabThree.setText("Eye News");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);

        TextView tabFour = (TextView) LayoutInflater.from(this).inflate(R.layout.es_home_tab, null);
        tabFour.setText("Eye Wallet");
        tabFour.setCompoundDrawablesWithIntrinsicBounds(0, R.mipmap.ic_launcher, 0, 0);
        tabLayout.getTabAt(3).setCustomView(tabFour);
    }

    @Override
    public void onFragmentInteraction(String id, int type) {
        hideMenu();

        if(type == Params.TYPE_NEWS) {
            Intent intent = new Intent(this, ESNewsDetail.class);
            intent.putExtra("id", id);
            intent.putExtra("type", type);
            startActivity(intent);
        }
    }

    @Override
    public void onFragmentVideoInteraction(String id, int type, String path, int duration, String title, long createdate) {
        hideMenu();
        Intent intent = new Intent(this, ESVideoDetail.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        intent.putExtra("path", path);
        intent.putExtra("duration", duration);
        intent.putExtra("title", title);
        intent.putExtra("createdate", createdate);
        startActivity(intent);
    }

    @Override
    public void onGoToList(int tabIndex) {
        if(tabIndex == 1){
            viewPager.setCurrentItem(1);
            mainTitle.setText(getResources().getString(R.string.title_video));
            resetTabs();
            iconVideo.setImageResource(R.drawable.icon_eyewatch_f);
            iconVideoLabel.setTextColor(Color.parseColor(focusColor));
        } else if(tabIndex == 2){
            viewPager.setCurrentItem(2);
            mainTitle.setText(getResources().getString(R.string.title_news));
            resetTabs();
            iconNews.setImageResource(R.drawable.icon_eyenews_f);
            iconNewsLabel.setTextColor(Color.parseColor(focusColor));
        }

    }

    @Override
    public void onFragmentEventInteraction(String id) {
        hideMenu();
        Intent intent = new Intent(this, ESEventDetail.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.fab) {
            Log.d("fab","fab click");
            onFabClick(view);
            return;
        }
// else if (view.getId() == R.id.menu_layout) {
//            Log.d("fab","layout click");
//            if(radialMenuIsOpen){
//                hideMenu();
//            }
//            return;
//        }

        hideMenu();

        switch (view.getId()){
            case R.id.tab1Button:
                viewPager.setCurrentItem(0);
                mainTitle.setText(getResources().getString(R.string.app_name));
                resetTabs();
                iconHome.setImageResource(R.drawable.icon_home_f);
                iconHomeLabel.setTextColor(Color.parseColor(focusColor));
                break;
            case R.id.tab2Button:
                viewPager.setCurrentItem(1);
                mainTitle.setText(getResources().getString(R.string.title_video));
                resetTabs();
                iconVideo.setImageResource(R.drawable.icon_eyewatch_f);
                iconVideoLabel.setTextColor(Color.parseColor(focusColor));
                break;
//            case R.id.tab3Button:
//                Log.d("log","Show Submenu");
//                break;
            case R.id.tab4Button:
                viewPager.setCurrentItem(2);
                mainTitle.setText(getResources().getString(R.string.title_news));
                resetTabs();
                iconNews.setImageResource(R.drawable.icon_eyenews_f);
                iconNewsLabel.setTextColor(Color.parseColor(focusColor));
                break;
            case R.id.tab5Button:
                viewPager.setCurrentItem(3);
                mainTitle.setText(getResources().getString(R.string.title_wallet));
                resetTabs();
                iconWallet.setImageResource(R.drawable.icon_eyewallet_f);
                iconWalletLabel.setTextColor(Color.parseColor(focusColor));
                break;

            case R.id.radialWasit:
                Intent iWasit = new Intent(this, ESRefereeList.class);
                startActivity(iWasit);
                break;

            case R.id.radialKlub:
                Intent iKlub = new Intent(this, ESClubList.class);
                startActivity(iKlub);
                break;


        }


    }

    private void resetTabs(){
        iconHome.setImageResource(R.drawable.icon_home);
        iconVideo.setImageResource(R.drawable.icon_eyewatch);
        iconNews.setImageResource(R.drawable.icon_eyenews);
        iconWallet.setImageResource(R.drawable.icon_eyewallet);

        iconHomeLabel.setTextColor(Color.parseColor(greyColor));
        iconVideoLabel.setTextColor(Color.parseColor(greyColor));
        iconNewsLabel.setTextColor(Color.parseColor(greyColor));
        iconWalletLabel.setTextColor(Color.parseColor(greyColor));
    }

    /*** called from News fragment ***/
    @Override
    public void onFragmentNewsInteraction(String id, int type) {
        hideMenu();
        Intent intent = new Intent(this, ESNewsDetail.class);
        intent.putExtra("id", id);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter implements ViewTreeObserver.OnScrollChangedListener {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);

        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public void onScrollChanged() {

        }

        @Override
        public int getItemPosition(Object object) {
            return super.getItemPosition(object);

        }

        @Override
        public long getItemId(int position) {
            return super.getItemId(position);
        }



    }

    private void onFabClick(View v) {
        if (v.isSelected()) {
            Log.d("fab","hide menu");
            radialMenuIsOpen = false;
            hideMenu();
        } else {
            Log.d("fab","show menu");
            radialMenuIsOpen = true;
            showMenu();
        }
        v.setSelected(!v.isSelected());
    }

    @SuppressWarnings("NewApi")
    private void showMenu() {
        menuLayout.setVisibility(View.VISIBLE);

        List<Animator> animList = new ArrayList<>();

        for (int i = 0, len = arcLayout.getChildCount(); i < len; i++) {
            animList.add(createShowItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new OvershootInterpolator());
        animSet.playTogether(animList);
        animSet.start();
    }

    @SuppressWarnings("NewApi")
    private void hideMenu() {

        List<Animator> animList = new ArrayList<>();

        for (int i = arcLayout.getChildCount() - 1; i >= 0; i--) {
            animList.add(createHideItemAnimator(arcLayout.getChildAt(i)));
        }

        AnimatorSet animSet = new AnimatorSet();
        animSet.setDuration(400);
        animSet.setInterpolator(new AnticipateInterpolator());
        animSet.playTogether(animList);
        animSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                menuLayout.setVisibility(View.INVISIBLE);
            }
        });
        animSet.start();

    }

    private Animator createShowItemAnimator(View item) {

        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

        item.setRotation(0f);
        item.setTranslationX(dx);
        item.setTranslationY(dy);

//        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
//                item,
//                AnimatorUtils.rotation(0f, 720f),
//                AnimatorUtils.translationX(dx, 0f),
//                AnimatorUtils.translationY(dy, 0f)
//        );

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.translationX(dx, 0f),
                AnimatorUtils.translationY(dy, 0f)
        );

        return anim;
    }

    private Animator createHideItemAnimator(final View item) {
        float dx = fab.getX() - item.getX();
        float dy = fab.getY() - item.getY();

//        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
//                item,
//                AnimatorUtils.rotation(720f, 0f),
//                AnimatorUtils.translationX(0f, dx),
//                AnimatorUtils.translationY(0f, dy)
//        );

        Animator anim = ObjectAnimator.ofPropertyValuesHolder(
                item,
                AnimatorUtils.translationX(0f, dx),
                AnimatorUtils.translationY(0f, dy)
        );

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                item.setTranslationX(0f);
                item.setTranslationY(0f);
            }
        });

        return anim;
    }

}
