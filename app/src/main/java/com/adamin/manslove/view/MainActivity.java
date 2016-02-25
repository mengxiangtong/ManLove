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
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.adamin.manslove.R;
import com.adamin.manslove.adapter.MainAdapter;
import com.adamin.manslove.base.BaseActivity;
import com.adamin.manslove.domain.TabModel;
import com.adamin.manslove.presenter.main.MainPresenter;
import com.adamin.manslove.service.NetWorkService;
import com.adamin.manslove.utils.LogUtil;
import com.adamin.manslove.utils.SnackBarUtils;
import com.adamin.manslove.utils.StatusBarCompact;
import com.adamin.manslove.view.main.MainView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adamin.toolkits.utils.DialogUtil;
import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainView {
    @Bind(R.id.appbarlayout)
    AppBarLayout appBarLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.fab)
    FloatingActionButton floatingActionButton;
    @Bind(R.id.pager)
    ViewPager viewPager;
    @Bind(R.id.layout_error)
    View errorview;
    @Bind(R.id.img_logo)
    ImageView imglogo;
    @Bind(R.id.collap)
    CollapsingToolbarLayout collapsingToolbarLayout;
    private MainAdapter adapter;
    private AlertDialog alertDialog;
    private MainPresenter mainPresenter;
    private List<TabModel> tabModels;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
    }


    private void init() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        collapsingToolbarLayout.setTitle("ManLove");
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
                                        collapsingToolbarLayout.setBackground(new ColorDrawable(palette.getMutedColor(Color.parseColor("#3F51B5"))));
                                        toolbar.setBackground(new ColorDrawable(palette.getMutedColor(Color.parseColor("#3F51B5"))));
                                        tabLayout.setBackground(new ColorDrawable(palette.getDarkMutedColor(Color.parseColor("#3F51B5"))));
                                        collapsingToolbarLayout.setCollapsedTitleTextColor(palette.getDarkVibrantColor(Color.parseColor("#ffffff")));
                                        collapsingToolbarLayout.setExpandedTitleColor(palette.getDarkVibrantColor(Color.parseColor("#ffffff")));
                                        StatusBarCompact.compat(MainActivity.this,palette.getDarkMutedColor(Color.parseColor("#3F51B5")));

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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainPresenter.cancel(this);
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
}