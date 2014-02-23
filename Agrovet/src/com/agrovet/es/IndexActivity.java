package com.agrovet.es;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.agrovet.es.Fragments.AlmacenFragment;
import com.agrovet.es.Fragments.BaseFragment;
import com.agrovet.es.Fragments.CobrosFragment;
import com.agrovet.es.Fragments.InicioFragment;
import com.agrovet.es.libs.CustomTypeFace;

public class IndexActivity extends FragmentActivity implements
		ListView.OnItemClickListener {
	private ListView					mDrawerList;
	private DrawerLayout				mDrawer;
	private CustomActionBarDrawerToggle	mDrawerToggle;
	private static String				BUNDLE_SELECTEDFRAGMENT	= "BDL_SELFRG";
	private int							mSelectedFragment;
	private BaseFragment				mBaseFragment;
	private Context						context;
	ArrayList<String> datosPersonales;

	// cambiar
	private static final int			CASE_INICIO				= 0;
	private static final int			CASE_PRODUCTOS			= 1;
	private static final int			CASE_COBROS				= 2;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_index);
		
		//recuperamos los datos de la actividad pasada
		Bundle reicieveParams = getIntent().getExtras();
		datosPersonales = reicieveParams.getStringArrayList("datosUsuario");
		
		// tipo de letra
;		int titleId = Resources.getSystem().getIdentifier("action_bar_title",	
				"id", "android");
		TextView custom = (TextView) findViewById(titleId);
		custom.setTextAppearance(getApplicationContext(), R.style.CustomText);
		custom.setTypeface(CustomTypeFace.getInstance(this).getTypeFace());

		// enable ActionBar app icon to behave as action to toggle nav drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		ListView drawer_list = (ListView) findViewById(R.id.drawer);
		mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		context = this;
		mDrawer.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		_initMenu();
		mDrawerToggle = new CustomActionBarDrawerToggle(this, mDrawer);
		mDrawer.setDrawerListener(mDrawerToggle);
		
		mBaseFragment = selectFragment(CASE_INICIO);
		openFragment(mBaseFragment);
		
	}
	
	
	//abrir el fragmento
		private void openFragment(BaseFragment baseFragment) {
			if (baseFragment != null) {
				FragmentManager fragmentManager = getSupportFragmentManager();
				fragmentManager
						.beginTransaction()
						.replace(R.id.main_content, baseFragment)
						.commit();
			}
		}
		
		//iniciar el fragmento
		private BaseFragment selectFragment(int position) {
			BaseFragment baseFragment = null;

			switch(position){
				case CASE_INICIO:
					baseFragment = new InicioFragment();
		            Bundle args = new Bundle();
		            args.putStringArrayList("datosPersonales",datosPersonales);
		            baseFragment.setArguments(args);
					break;
				case CASE_PRODUCTOS:
					baseFragment = new AlmacenFragment();
					break;
				case CASE_COBROS:
					baseFragment = new CobrosFragment();
					break;

				default:
					break;
			}
			
			return baseFragment;
		}
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		// getMenuInflater().inflate(R.menu.index, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// If the nav drawer is open, hide action items related to the content
		// view
		// boolean drawerOpen = mDrawer.isDrawerOpen(mDrawerList);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		/*
		 * The action bar home/up should open or close the drawer.
		 * ActionBarDrawerToggle will take care of this.
		 */
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		switch(item.getItemId()){

		// About
			case R.id.action_settings:
				// Utils.showAbout(this);
				return true;
			default:
				break;
		}

		// Handle your other action bar items...
		return super.onOptionsItemSelected(item);
	}

	private class CustomActionBarDrawerToggle extends ActionBarDrawerToggle {

		public CustomActionBarDrawerToggle(Activity mActivity,
				DrawerLayout mDrawerLayout) {
			super(mActivity, mDrawerLayout, R.drawable.ic_navigation_drawer,
					R.string.app_name, R.string.title_activity_index);
		}

		@Override
		public void onDrawerClosed(View view) {
			getActionBar().setTitle(
					getResources().getString(R.string.title_activity_index));
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}

		@Override
		public void onDrawerOpened(View drawerView) {
			getActionBar().setTitle(
					getResources().getString(R.string.drawer_open));
			invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
		}
	}

	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			// Highlight the selected item, update the title, and close the
			// drawer
			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mBaseFragment = selectFragment(position);
			mSelectedFragment = position;

			if (mBaseFragment != null)
				openFragment(mBaseFragment);
			mDrawer.closeDrawer(mDrawerList);
		}
	}

	private void _initMenu() {
		mDrawerList = (ListView) findViewById(R.id.drawer);

		if (mDrawerList != null) {
			mDrawerList.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, options));

			mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
		}

	}

	public static final String[]	options	= { "Inicio", "Insumos",
			"Cobros"		};

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub

	}
}
