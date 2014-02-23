package com.agrovet.es.Fragments;

import java.util.ArrayList;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import br.com.dina.ui.widget.UITableView;
import br.com.dina.ui.widget.UITableView.ClickListener;

import com.agrovet.es.R;
import com.agrovet.es.models.Movimientoss;

public class IngresosFragment extends BaseFragment {
	ArrayList<Movimientoss>	movimientos;
	private static String	TAG	= "IngresosFragment";
	private View			root;
	UITableView				tableView1;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		root = inflater.inflate(R.layout.ingresos_fragment, container, false);
		tableView1 = (UITableView) root.findViewById(R.id.tableView);
		movimientos = ListCobrosFragments.movimon;
		initlista();
		Log.v(TAG, "onCreateView");
		return root;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.v(TAG, "onCreate");
	}

	@Override
	public void onResume() {
		// initlista();
		super.onResume();
	}

	private void initlista() {
		tableView1.clear();
		CustomClickListener listener = new CustomClickListener();
		tableView1.setClickListener(listener);
		if(movimientos.size() > 0) {
		for (int i = 0; i < movimientos.size(); i++) {

			if (Integer.parseInt(movimientos.get(i).getTipoconcepto()) == 1) {
				tableView1.addBasicItem(movimientos.get(i).getConcepto(),
						"Monto : S/." + movimientos.get(i).getMonto(),
						"Fecha : " + movimientos.get(i).getFecha(), "");
			}
		}
		Log.v(TAG, "initlista");
		tableView1.commit();
		}else {
			Toast.makeText(getActivity().getApplicationContext(),"No se encontraron Ingresos de Dinero", Toast.LENGTH_SHORT).show();
		}
	}

	private class CustomClickListener implements ClickListener {
		public void onClick(int index) {

		}

	}

	@Override
	public int getTitleResourceId() {
		// TODO Auto-generated method stub
		return 0;
	}
}
