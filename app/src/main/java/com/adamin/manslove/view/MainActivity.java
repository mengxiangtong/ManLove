package com.adamin.manslove.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.adamin.manslove.R;
import com.adamin.manslove.adapter.MainAdapter;
import com.adamin.manslove.base.App;
import com.adamin.manslove.base.BaseActivity;
import com.adamin.manslove.domain.TabModel;
import com.adamin.manslove.presenter.main.MainPresenter;
import com.adamin.manslove.service.NetWorkService;
import com.adamin.manslove.utils.LogUtil;
import com.adamin.manslove.utils.SnackBarUtils;
import com.adamin.manslove.utils.StatusBarCompact;
import com.adamin.manslove.view.gank.GankActivity;
import com.adamin.manslove.view.huasheng.HuashengActivity;
import com.adamin.manslove.view.jueduilingyu.JueDuiActivity;
import com.adamin.manslove.view.lexun.LeXunActivity;
import com.adamin.manslove.view.liebao.LiebaoActivity;
import com.adamin.manslove.view.main.MainView;
import com.adamin.manslove.view.meizitu.MeiziTuActivity;
import com.adamin.manslove.view.tg.TgActivity;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
//import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adamin.toolkits.utils.DialogUtil;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {
    @Bind(R.id.appbarlayout)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.layout_error)
    View errorview;
    @Bind(R.id.img_logo)
    ImageView imglogo;
    @Bind(R.id.collap)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.drawer)
    DrawerLayout drawerLayout;
    @Bind(R.id.nav_view)
    NavigationView navigationView;
    //    @Bind(R.id.nav_view2)
//    NavigationView navigationView2;
    private MainAdapter adapter;
    private AlertDialog alertDialog;
    private MainPresenter mainPresenter;
    private List<TabModel> tabModels;
    private AlertDialog showmain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        UmengUpdateAgent.update(this);
        startService(new Intent(MainActivity.this, NetWorkService.class));

    }


    private void init() {


        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        collapsingToolbarLayout.setTitle("美人图");

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        alertDialog = DialogUtil.buildCustomDialog(MainActivity.this, "正在获取tab数据,请稍后");
        tabModels = new ArrayList<>();
        adapter = new MainAdapter(getSupportFragmentManager(), tabModels);
        viewPager.setAdapter(adapter);
        errorview.setVisibility(View.GONE);
        mainPresenter = new MainPresenter(this);
        mainPresenter.fetchData(this);
        startService(new Intent(MainActivity.this, NetWorkService.class));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onPageSelected(final int position) {

                collapsingToolbarLayout.setTitle(tabModels.get(position).getName());

                Picasso.with(MainActivity.this).load(tabModels.get(position).getImage()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        imglogo.setImageBitmap(bitmap);
                        Palette.from(bitmap)
                                .generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {
//                                        collapsingToolbarLayout.setBackground(new ColorDrawable(palette.getVibrantColor(Color.parseColor("#3F51B5"))));
//                                        toolbar.setBackground(new ColorDrawable(palette.getVibrantColor(Color.parseColor("#3F51B5"))));
//                                        tabLayout.setBackground(new ColorDrawable(palette.getVibrantColor(Color.parseColor("#3F51B5"))));
//                                        collapsingToolbarLayout.setCollapsedTitleTextColor(palette.getDarkMutedColor(Color.parseColor("#ffffff")));
//                                        collapsingToolbarLayout.setExpandedTitleColor(palette.getDarkMutedColor(Color.parseColor("#ffffff")));
//                                        StatusBarCompact.compat(MainActivity.this,palette.getDarkMutedColor(Color.parseColor("#3F51B5")));

                                    }
                                });
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                });

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//            SystemBarTintManager systemBarTintManager=new SystemBarTintManager(this);
//            systemBarTintManager.setStatusBarTintEnabled(true);
//            systemBarTintManager.setStatusBarTintColor(Color.parseColor("#212121"));
            //         Set the padding to match the Status Bar height
//            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }
        StatusBarCompact.compat(this, Color.parseColor("#424242"));
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
//        navigationView2.setNavigationItemSelectedListener(this);

        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_gonggao, null);
        showmain = new AlertDialog.Builder(MainActivity.this)
                .setCancelable(false)
                .setView(view)
                .create();
        Button evaluate = (Button) view.findViewById(R.id.pingjia);
        evaluate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showmain.dismiss();
            }
        });
        if (App.utilsSharedPreferences.getIsFirst()) {
            showmain.show();
            App.utilsSharedPreferences.setIsfirst(false);
        }

    }

    @Override
    protected void onDestroy() {
        if (mainPresenter != null) {
            mainPresenter.cancel(this);

        }
        super.onDestroy();

    }

    @Override
    public void netWorkOpen() {
        LogUtil.error(MainActivity.class, "开启网络 ");
        viewPager.setVisibility(View.VISIBLE);
        errorview.setVisibility(View.GONE);
        tabModels.clear();
        adapter.notifyDataSetChanged();
        mainPresenter.fetchData(MainActivity.this);
    }

    @Override
    public void netWorkClose() {
        LogUtil.error(MainActivity.class, "关闭网络");
        viewPager.setVisibility(View.GONE);
        errorview.setVisibility(View.VISIBLE);

    }


    @Override
    public void showLoadingAlert() {
        alertDialog.show();
    }

    @Override
    public void hideLoaingAlert() {
        alertDialog.dismiss();

    }

    @Override
    public void SetTabData(String response) {
        LogUtil.error(MainActivity.class, response);

    }

    @Override
    public void SetTabData(List<TabModel> tabModels) {
        this.tabModels.addAll(tabModels);
        adapter.notifyDataSetChanged();
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void showError(Exception e) {
        SnackBarUtils.showSnackBar(MainActivity.this, e.getMessage(), SnackBarUtils.ERROR);


    }

    public void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_mzt:
//                drawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(MainActivity.this, MeiziTuActivity.class));
                return true;
            case R.id.nav_liebao:
                startActivity(new Intent(MainActivity.this, LiebaoActivity.class));
                return true;
            case R.id.nav_gank:
                startActivity(new Intent(MainActivity.this, GankActivity.class));
                return true;
            case R.id.nav_huasheng:
                startActivity(new Intent(MainActivity.this, HuashengActivity.class));
                return  true;
            case R.id.nav_juedui:
                startActivity(new Intent(MainActivity.this, JueDuiActivity.class));
                return  true;
            case R.id.nav_lexun:
                startActivity(new Intent(MainActivity.this, LeXunActivity.class));
                return  true;
            case R.id.nav_tiangou:
                startActivity(new Intent(MainActivity.this, TgActivity.class));
                return true;
            default:
                SnackBarUtils.showSnackBar(MainActivity.this, "正在开发(●'◡'●)", SnackBarUtils.INFO);
                return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(Gravity.LEFT)){
            drawerLayout.closeDrawers();
        }else {
            super.onBackPressed();

        }
    }
}
