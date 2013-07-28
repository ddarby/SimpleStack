package com.gfranks.sudoku.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;

import android.app.FragmentManager;
import android.content.res.Configuration;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Surface;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.gfranks.sudoku.R;
import com.gfranks.sudoku.dialogs.SudokuHUD;
import com.gfranks.sudoku.fragments.SudokuFragment;
//import com.unitychat.common.UnityChatHelper;

public class MainActivity extends Activity {

    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private ActionBarDrawerToggle mDrawerToggle;
    private String[] mSudokuMenuOptions;
    private SudokuFragment sudokuFragment;
    private int current_orientation;
    private ImageView logoImageView;
    private boolean splashShowing = true;
    private SudokuHUD hud;

//    private UnityChatHelper chatHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSudokuMenuOptions = getResources().getStringArray(R.array.sudoku_menu_timer_show);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mSudokuMenuOptions));
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,                  /* host Activity */
                mDrawerLayout,         /* DrawerLayout object */
                R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
                R.string.drawer_open,  /* "open drawer" description for accessibility */
                R.string.drawer_close  /* "close drawer" description for accessibility */
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        current_orientation = getCurrentScreenOrientation();

        showSplashScreen();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    /* Called whenever we call invalidateOptionsMenu() */
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        if (splashShowing) {
            menu.findItem(R.id.action_undo_last).setVisible(false);
        } else {
            boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
            menu.findItem(R.id.action_undo_last).setVisible(!drawerOpen);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch(item.getItemId()) {
            case R.id.action_undo_last:
                sudokuFragment.undoLastMove();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
                mDrawerLayout.closeDrawer(Gravity.LEFT);
            } else {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
            return false;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    public void showSplashScreen() {
        Animation mAnim = AnimationUtils.loadAnimation(this, R.anim.simple);
        mAnim.setDuration(2000);
        hud = new SudokuHUD(this);
        hud.show();
        mAnim.setAnimationListener(new Animation.AnimationListener(){
            public void onAnimationEnd(Animation animation) {
                hud.dismiss();
                logoImageView.setVisibility(View.GONE);
                setupSudokuFragment();
                splashShowing = false;
                invalidateOptionsMenu();
            }

            public void onAnimationRepeat(Animation animation) { }

            public void onAnimationStart(Animation animation) { }
        });

        logoImageView = (ImageView)findViewById(R.id.splashLogo);
        logoImageView.startAnimation(mAnim);
    }

    public void setupSudokuFragment() {
        sudokuFragment = new SudokuFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, sudokuFragment).commit();
    }

    /* The click listner for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            sudokuFragment.loadArgument(position);
            mDrawerLayout.closeDrawer(mDrawerList);
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);

        if (current_orientation != newConfig.orientation) {
            sudokuFragment.orienationChanged();
        }

        current_orientation = newConfig.orientation;
    }

    @Override
    protected void onPause() {
        sudokuFragment.storeCurrentGame();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        sudokuFragment.storeCurrentGame();
        super.onDestroy();
    }

    public void timerShown() {
        mSudokuMenuOptions = getResources().getStringArray(R.array.sudoku_menu_timer_hide);
        mDrawerList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.drawer_list_item, mSudokuMenuOptions));
        mDrawerList.invalidateViews();
    }

    public void timerHidden() {
        mSudokuMenuOptions = getResources().getStringArray(R.array.sudoku_menu_timer_show);
        mDrawerList.setAdapter(new ArrayAdapter<String>(MainActivity.this,
                R.layout.drawer_list_item, mSudokuMenuOptions));
        mDrawerList.invalidateViews();
    }

    private int getCurrentScreenOrientation() {
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        int orientation;
        // if the device's natural orientation is portrait:
        if ((rotation == Surface.ROTATION_0
                || rotation == Surface.ROTATION_180) && height > width ||
                (rotation == Surface.ROTATION_90
                        || rotation == Surface.ROTATION_270) && width > height) {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
            }
        }
        // if the device's natural orientation is landscape or if the device
        // is square:
        else {
            switch(rotation) {
                case Surface.ROTATION_0:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
                case Surface.ROTATION_90:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                    break;
                case Surface.ROTATION_180:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE;
                    break;
                case Surface.ROTATION_270:
                    orientation =
                            ActivityInfo.SCREEN_ORIENTATION_REVERSE_PORTRAIT;
                    break;
                default:
                    orientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
                    break;
            }
        }

        return orientation;
    }
}