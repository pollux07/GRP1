package com.leon.daniel.grp1;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.leon.daniel.grp1.Utils.Common;
import com.leon.daniel.grp1.Utils.PsPagerAdapter;
import com.leon.daniel.grp1.Utils.VolleySingelton;
import com.leon.daniel.grp1.Utils.WebService;
import com.rd.PageIndicatorView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context mCtx;
    SwipeRefreshLayout mSwipeRefresh;
    ScrollView mScrollMain;
    PsPagerAdapter mPsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        VolleySingelton.getInstance(getApplicationContext());
        mCtx = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        String userId = Common.getPreference(mCtx, Common.USER_ID, null);
        if (null == userId) {
            Intent loginIntent = new Intent(mCtx, LoginActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            finish();
            overridePendingTransition(0, 0);

            startActivity(loginIntent);
            overridePendingTransition(0, 0);
        } else {
            loadProfile(userId);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //CONTENT ACTIVITY
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.main_swipe);
        mScrollMain = (ScrollView) findViewById(R.id.main_scrollview);
        mScrollMain.setVisibility(View.VISIBLE);
        ViewPager viewPagerPromos = (ViewPager) findViewById(R.id.games_promo_vp);
        PageIndicatorView pageIndicator = (PageIndicatorView) findViewById(R.id.page_inidcater_promo);

        int[] promGames = new int[] {R.drawable.resident_evil7_gold_edition_promo,
                R.drawable.spiderman_promo, R.drawable.days_gone_promo_ps4,
                R.drawable.detroit_become_human_promo, R.drawable.death_stranding_promo,
                R.drawable.anthem_promo};

        int[] gamesPs = new int[] {R.drawable.ff_xv_ps4, R.drawable.horizon_zero_dawn_ps4,
                R.drawable.mgv_ps4, R.drawable.last_of_us_ps4, R.drawable.persona_5_ps4};
        int[] gamesXb = new int[] {R.drawable.ac_ezio_xbo, R.drawable.destiny_2_xbo,
                R.drawable.gears_of_war_4_xbo, R.drawable.halo_5_xbo, R.drawable.quantum_break_xbo};
        int[] gamesSwitch = new int[] {R.drawable.pokken_tournament_dx_switch,
                R.drawable.super_mario_odyssey_switch, R.drawable.zelda_breath_of_the_wild_switch,
                R.drawable.splatton2_switch, R.drawable.mario_kart8_deluxe_switch};
        int[] gamesNPortable = new int[] {R.drawable.pokemon_ultra_sun_n3ds,
                R.drawable.pokemon_ultra_moon_3ds, R.drawable.monster_hunter_generation_3ds,
                R.drawable.super_smash_bros_3ds};
        int[] gamesConsoles = new int[] {R.drawable.ps4_bundle, R.drawable.ps4_pro_bundle,
                R.drawable.ps_vr, R.drawable.xbox_one_s, R.drawable.xbox_one_x,
                R.drawable.xbox_ones_gears_war4_bundle, R.drawable.switch_splatoon};

        mPsPagerAdapter = new PsPagerAdapter(mCtx, promGames);
        viewPagerPromos.setAdapter(mPsPagerAdapter);

        pageIndicator.setViewPager(viewPagerPromos);

        TextView psTitle = (TextView) findViewById(R.id.tv_ps_title);
        psTitle.setText(getString(R.string.ps_title));
        LinearLayout layoutPs = (LinearLayout) findViewById(R.id.linear_ps);
        for (int i = 0; i < gamesPs.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(15, 15, 15, 15);
            Picasso.with(mCtx)
                    .load(gamesPs[i])
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //infoGameAction(int idGame[i]);
                }
            });
            layoutPs.addView(imageView);
        }

        TextView xbTitle = (TextView) findViewById(R.id.tv_xb_title);
        xbTitle.setText(getString(R.string.xb_title));
        LinearLayout layoutXb = (LinearLayout) findViewById(R.id.linear_xb);
        for (int i = 0; i < gamesXb.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(15, 15, 15, 15);
            Picasso.with(mCtx)
                    .load(gamesXb[i])
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //infoGameAction(int idGame[i]);
                }
            });
            layoutXb.addView(imageView);
        }

        TextView switchTitle = (TextView) findViewById(R.id.tv_nintendo_title);
        switchTitle.setText(getString(R.string.nintendo_title));
        LinearLayout layoutNintendo = (LinearLayout) findViewById(R.id.linear_nintendo);
        for (int i = 0; i < gamesSwitch.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(15, 15, 15, 15);
            Picasso.with(mCtx)
                    .load(gamesSwitch[i])
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //infoGameAction(int idGame[i]);
                }
            });
            layoutNintendo.addView(imageView);
        }

        TextView nPortableTitle = (TextView) findViewById(R.id.tv_nportable_title);
        nPortableTitle.setText(getString(R.string.nin_portable));
        LinearLayout layoutPortableN = (LinearLayout) findViewById(R.id.linear_nportable);
        for (int i = 0; i < gamesNPortable.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(15, 15, 15, 15);
            Picasso.with(mCtx)
                    .load(gamesNPortable[i])
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //infoGameAction(int idGame[i]);
                }
            });
            layoutPortableN.addView(imageView);
        }

        TextView consolesTitle = (TextView) findViewById(R.id.tv_consoles_title);
        consolesTitle.setText(getString(R.string.consoles_title));
        LinearLayout layoutConsoles = (LinearLayout) findViewById(R.id.linear_consoles);
        for (int i = 0; i < gamesConsoles.length; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setId(i);
            imageView.setPadding(15, 15, 15, 15);
            Picasso.with(mCtx)
                    .load(gamesConsoles[i])
                    .into(imageView);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //infoGameAction(int idGame[i]);
                }
            });
            layoutConsoles.addView(imageView);
        }


        mSwipeRefresh.setColorSchemeResources(R.color.colorAccent, R.color.colorPrimaryDark);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mSwipeRefresh.setRefreshing(true);
                //TODO:agregar metodo de carga de imagienes
            }
        });
    }

    private void loadProfile(String userId) {
        //TODO: add swiperefresh
        Map<String, String> params = new HashMap<>();
        params.put("user_id", userId);

        WebService.loadProfile(mCtx, params, new WebService.RequestListener() {
            @Override
            public void onSucces(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    int code = jsonResponse.getInt("code");
                    String status = jsonResponse.getString("status");

                    if (code == Common.RESPONSE_OK && status.equals(Common.OK_STATUS)) {
                        String name = jsonResponse.getString("name");
                        if (null  == name) {
                            providePersonalInfo();
                        } else {
                            Toast.makeText(mCtx, "Bienvenido", Toast.LENGTH_SHORT).show();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError() {

            }
        });
    }

    private void providePersonalInfo() {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mCtx);
        alertBuilder.setMessage(getString(R.string.requirement));
        alertBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        final AlertDialog dialog = alertBuilder.create();
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profileIntent = new Intent(mCtx, ProfileActivity.class);
                startActivity(profileIntent);
                dialog.dismiss();
            }
        });
    }

    private void infoGameAction(){
        Intent infoGameIntent = new Intent(mCtx, InfoGamesActivity.class);
        startActivity(infoGameIntent);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_slideshow:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            case R.id.logout:
                Common.deletePreference(mCtx, Common.USER_ID);
                startActivity(new Intent(mCtx, LoginActivity.class));
                finish();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
