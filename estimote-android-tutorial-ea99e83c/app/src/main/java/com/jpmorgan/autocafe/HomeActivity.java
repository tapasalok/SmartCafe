package com.jpmorgan.autocafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.jpmorgan.autocafe.utils.CommonUtils;

public class HomeActivity extends AppCompatActivity {


    private MenuItem nav_location_item;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private TextView userName;
    private TextView textView;
    private TextView teamName;
    private int currentIndex = -1;
    private CommonUtils commonUtils;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main_screen);

        commonUtils = CommonUtils.getInstance(HomeActivity.this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);

        goToHomeFragment();
        // get menu from navigationView
        Menu menu = navigationView.getMenu();

        navHeader = navigationView.getHeaderView(0);
        userName = (TextView) navHeader.findViewById(R.id.userName);
        textView = (TextView) navHeader.findViewById(R.id.textView);
        teamName = (TextView) navHeader.findViewById(R.id.teamName);

        setResult(RESULT_OK);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (currentIndex == 0) {
                final AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this).
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }).
                        setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create();
                alertDialog.setTitle(getString(R.string.app_name));
                alertDialog.setMessage(getString(R.string.exit_message));
                alertDialog.setIcon(R.mipmap.ic_launcher);
                alertDialog.show();
            } else {
                goToHomeFragment();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home) {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            drawer.openDrawer(GravityCompat.START);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        // Create a new fragment and specify the fragment to show based on nav item clicked
        navigationView.getMenu().getItem(0).setChecked(false);
        navigationView.getMenu().getItem(1).setChecked(false);
        navigationView.getMenu().getItem(2).setChecked(false);
        navigationView.getMenu().getItem(3).setChecked(false);
        navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setChecked(false);
        Fragment fragment = null;
        Class fragmentClass = null;
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                currentIndex = 0;
                fragmentClass = HomeFragment.class;
                break;

            case R.id.nav_smart:
                currentIndex = 1;
                fragmentClass = MainFragment.class;
                break;

            case R.id.nav_menu:
                currentIndex = 2;
                if(CommonUtils.isVendorLogin()){
                    fragmentClass = MenuFragmentVendor.class;
                }else {
                    fragmentClass = MenuFragment.class;
                }
                break;


            case R.id.nav_signout:
                currentIndex = 3;
                AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
                builder.setMessage("Do you want to Signout?");
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setTitle(R.string.app_name);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent2 = new Intent(HomeActivity.this, LoginActivity.class);
                        startActivity(intent2);
                        finish();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
                break;

            case R.id.nav_aboutus:
                currentIndex = 4;
                fragmentClass = AboutUsFragment.class;
                break;

            default:
                currentIndex = 1;
                fragmentClass = HomeFragment.class;
                break;
        }

        if (menuItem.getItemId() == R.id.nav_signout) {
            return;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.flContent, fragment)/*.addToBackStack(null)*/.commit();

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        setTitle(menuItem.getTitle());
        // Close the navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
//        mDrawer.closeDrawers();

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    public void goToAboutusFragment() {
        navigationView.getMenu().getItem(4).getSubMenu().getItem(0).setChecked(true);
        setupDrawerContent(navigationView);
        selectDrawerItem(navigationView.getMenu().getItem(4).getSubMenu().getItem(0));
    }

    public void goToHomeFragment() {
        navigationView.getMenu().getItem(0).setChecked(true);
        setupDrawerContent(navigationView);
        selectDrawerItem(navigationView.getMenu().getItem(0));
    }

    public void goToSmartFragment() {
        navigationView.getMenu().getItem(1).setChecked(true);
        setupDrawerContent(navigationView);
        selectDrawerItem(navigationView.getMenu().getItem(1));
    }

    public void goToMenuFragment() {
        navigationView.getMenu().getItem(2).setChecked(true);
        setupDrawerContent(navigationView);
        selectDrawerItem(navigationView.getMenu().getItem(2));
    }

    public void goToSignOutFragment() {
        navigationView.getMenu().getItem(3).setChecked(true);
        setupDrawerContent(navigationView);
        selectDrawerItem(navigationView.getMenu().getItem(3));
    }

}
