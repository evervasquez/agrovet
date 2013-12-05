package com.agrovet.es.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Movimientos implements Parcelable {
	//private ArrayList<String> datos;
	private String concepto;
	private String fecha;
	private String monto;
	
	public String getConcepto() {
		return concepto;
	}

	public void setConcepto(String concepto) {
		this.concepto = concepto;
	}

	public String getFecha() {
		return fecha;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getMonto() {
		return monto;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public Movimientos() {
		concepto=null;
		fecha = null;
		monto = null;
	}

	public Movimientos(Parcel in) {
		concepto=null;
		fecha = null;
		monto = null;
		readFromParcel(in);
	}
	
	public static final Parcelable.Creator<Movimientos> CREATOR = new Parcelable.Creator<Movimientos>() {
        public Movimientos createFromParcel(Parcel in) {
                return new Movimientos(in);
        }

        public Movimientos[] newArray(int size) {
                return new Movimientos[size];
        }
};

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
    public void writeToParcel(Parcel dest, int arg1) {
            // TODO Auto-generated method stub
            dest.writeString(concepto);
            dest.writeString(fecha);
            dest.writeString(monto);
    }
	
	  private void readFromParcel(Parcel in) {
          in.readString();
  }
}
