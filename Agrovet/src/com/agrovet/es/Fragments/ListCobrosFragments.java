package com.agrovet.es.Fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTabHost;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TabHost.OnTabChangeListener;

import com.agrovet.es.R;
import com.agrovet.es.models.Movimientos;
import com.agrovet.es.models.Movimientoss;

public class ListCobrosFragments extends BaseFragment {
	FragmentTabHost		mTabHost;
	public View			root;
	Movimientos			datos;
	public static ArrayList<Movimientoss>	movimon;

	@Override
	public int getTitleResourceId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		//llenadatos();
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.listcobros_fragment, container, false);
		
		
		mTabHost = (FragmentTabHost) root.findViewById(android.R.id.tabhost);
		
		mTabHost.setup(getActivity(), getChildFragmentManager(), R.id.egresos);
		mTabHost.clearAllTabs();
		mTabHost.addTab(mTabHost.newTabSpec("Ingresos")
				.setIndicator("Ingresos"), IngresosFragment.class, null);
		mTabHost.addTab(mTabHost.newTabSpec("Egresos").setIndicator("Egresos"),
				EgresosFragment.class, null);
		
		movimon = CobrosFragment.movimientos;
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				// TODO Auto-generated method stub
				if (arg0.equals("Ingresos")) {
					selectItem(0);
				} else {
					selectItem(1);
				}
			}
		});
		
		return root;
	}

	@Override
	public void onResume() {
		selectItem(0);
		super.onResume();
	}

	@SuppressWarnings("unused")
	public void selectItem(int position) {
		BaseFragment fragTwo;
		int id_layout;
		FragmentManager fm = getFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		ArrayList<String> mostrar = new ArrayList<String>();
		mostrar = null;
		if (position == 0) {
			fragTwo = new IngresosFragment();
			id_layout = R.id.ingresos;
		} else {
			fragTwo = new EgresosFragment();
			id_layout = R.id.egresos;
		}
		
		Bundle bundle = new Bundle();
		if(fragTwo == null) {
		bundle.putStringArrayList("movi", mostrar);
		fragTwo.setArguments(bundle);
		ft.add(id_layout, fragTwo);
		ft.commit();
		}
	}

}
