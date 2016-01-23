/*
 * OpenERP, Open Source Management Solution
 * Copyright (C) 2012-today OpenERP SA (<http://www.openerp.com>)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 
 */

package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Configuration;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mirasense.demos.productQR_scan_from_scandit;
import com.openerp.App;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.auth.OpenERPAccountManager;
import com.openerp.base.login.SyncWizard;
import com.openerp.orm.OEDataRow.IdName;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OESQLiteHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.OEDialog;
import com.openerp.support.OEUser;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

/**
 * The Class Idea.
 */

public class product extends BaseFragment implements OnItemClickListener {

	static List<String> mItems = new ArrayList<String>();
	ListView mListView = null;
	MainActivity mainobject;
	// databaseHelper dbhelper=new databaseHelper(getActivity(), null, null, 1);
	OESQLiteHelper oesqlhelper;
	Context context = getActivity();
	List<IdName> mDBTables = new ArrayList<IdName>();
	List<Object> mPartners = new ArrayList<Object>();
	public static Bitmap image_of_produc1t;
	Button btn;
	OEHelper oehelper;
	TextView tvloading;
	ProgressBar bar;
	ProgressDialog predailog;
	OEDialog pdialog;
	static int positionofimage =0;
	static int checkqtycallornot=0;
	
	//LoginUser loginUserASync;
	// JSONObject res=null;
	static int checkforfragment = 0;
	// OEDataRow row=(OEDataRow)db().select();

	private static int myProgress;
	private ProgressBar progressBar;
	private Handler myHandler = new Handler();

	private int progressStatus = 0;

	// OpenERPServerConnection openerp=new OpenERPServerConnection();
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.fragment_productlist,
				container, false);

		getActivity().setTitle(R.string.label_product);

		if (db().isEmptyTable()) {
			IdeaDemoRecords rec = new IdeaDemoRecords(getActivity());
			rec.createDemoRecords();
		}
//INTERNAL 
		MainActivity.global = 2;
		OEHelper.check_for_product_from_where=0;
		TextView txv4 = (TextView) rootView
				.findViewById(R.id.textView1);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		txv4.setTypeface(font,Typeface.BOLD);
		mListView = (ListView) rootView.findViewById(R.id.listview);

		oehelper = new OEHelper(getActivity());
		

		btn = (Button) rootView.findViewById(R.id.barcode);

		List<String> k = oehelper.product_name();
	
	//	oehelper.releted_selected_stock_location_id();
		
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.fragment_product_bind_item, OEHelper.datatemplate) {

			public View getView(int position, View convertView, ViewGroup parent) {
				View mView = convertView;

				if (mView == null)
					mView = getActivity().getLayoutInflater().inflate(
							R.layout.fragment_product_bind_item, parent, false);
				TextView txv = (TextView) mView
						.findViewById(R.id.tx_productname);
				TextView txv4 = (TextView) mView
						.findViewById(R.id.textView1);
				TextView txv5 = (TextView) mView
						.findViewById(R.id.textView2);
				txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				txv.setTypeface(font,Typeface.BOLD);
				txv4.setTypeface(font,Typeface.BOLD);
				txv5.setTypeface(font);
			//	txv.setTextColor(Color.rgb(84, 84, 84));
				txv.setText(":"+ OEHelper.datatemplate.get(position));
				// txv.setText(""+OEHelper.product_pty_stock_move);
//				txv.setTextAppearance(getActivity(),
//						android.R.attr.textAppearance);
				TextView txv1 = (TextView) mView
						.findViewById(R.id.tx_reference);
				txv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
				
				txv1.setTypeface(font);
			//	txv1.setTextColor(Color.rgb(84, 84, 84));
				txv1.setText(":"
						+ OEHelper.default_code_of_product_product
								.get(position));
//				txv1.setTextAppearance(getActivity(),
//						android.R.attr.textAppearance);
				// txv.setText("Product");
				Log.d("row***************" + OEHelper.data, "*************");
				return mView;
			}
		});
		btn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {

				OEHelper oehelper1 = new OEHelper(getActivity());
				List<String> p = oehelper1.readproducttempalate();
				checkqtycallornot=0;
				productQR_scan_from_scandit detail = new productQR_scan_from_scandit();
				FragmentListener frag = (FragmentListener) getActivity();
				frag.startDetailFragment(detail);

				// barcoderead.EAN13_code=("2100002000003");
				// String selected=barcoderead.EAN13_code;
				// int
				// indexofbarcodeproduct=OEHelper.ean13_of_product_product.indexOf(selected);
				// //String
				// idofproduct=MainActivity.idofproduct_product.get(indexofbarcodeproduct);
				//
				// String
				// sale_price_of_product_template=OEHelper.list_price_of_product_template.get(indexofbarcodeproduct);
				// String
				// cost_price_of_product_template=OEHelper.standard_price_of_product_template.get(indexofbarcodeproduct);
				// String
				// ean13_of_product_product1=OEHelper.ean13_of_product_product.get(indexofbarcodeproduct);
				// String
				// reference_of_product_product1=OEHelper.default_code_of_product_product.get(indexofbarcodeproduct);
				// String
				// type_Of_product_template=OEHelper.type_of_product_template.get(indexofbarcodeproduct);
				// String
				// supply_method_product_template1=OEHelper.supply_method_product_template.get(indexofbarcodeproduct);
				// String
				// procure_method_product_template1=OEHelper.procure_method_product_template.get(indexofbarcodeproduct);
				// image_of_produc1t=OEHelper.image_of_product.get(indexofbarcodeproduct);
				//
				// oeh.menufecturingData();
				// // Toast.makeText(getActivity(),
				// ""+MainActivity.getidfrom_product_product, 60).show();
				// Product_Detail detail =new Product_Detail();
				// Bundle args = new Bundle();
				// // args.putParcelable("BundleIcon", image_of_produc1t);
				// args.putString("name",
				// MainActivity.data.get(indexofbarcodeproduct));
				// args.putString("saleprice", sale_price_of_product_template);
				// args.putString("type", type_Of_product_template);
				// args.putString("reference", reference_of_product_product1);
				// args.putString("costprice", cost_price_of_product_template);
				// args.putString("supplymethod",
				// supply_method_product_template1);
				// args.putString("procuremethod",
				// procure_method_product_template1);
				// args.putString("ean13", ean13_of_product_product1);
				//
				//
				// detail.setArguments(args);
				// FragmentListener frag = (FragmentListener) getActivity();
				// frag.startDetailFragment(detail);

			}
		});

		mListView.setOnItemClickListener(this);
		return rootView;
	}

	public Object databaseHelper(Context context) {

		return new IdeaDBHelper(context);
	}



	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.product, menu);

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		switch (item.getItemId()) {

		case R.id.Dash_Board:

			Dash_Board detail = new Dash_Board();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(detail);
			
			return true;
			
		case R.id.Search_product:	
			
			final Dialog dialog = new Dialog(getActivity());
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.product_search_custom_dialog);
		//	dialog.setTitle("Product Search");
			dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					
					dialog.dismiss();
				}
			});
 
			AutoCompleteTextView autotext = (AutoCompleteTextView) dialog.findViewById(R.id.autoCompleteTextView_product_search);
			final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,OEHelper.datatemplate);
			TextView txv = (TextView) dialog.findViewById(R.id.textView1);
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
			autotext.setTypeface(font,Typeface.BOLD);
			autotext.setAdapter(adapter);
			txv.setTypeface(font,Typeface.BOLD);
			autotext.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					
					String name=adapter.getItem(arg2).toString();
					callmethod_for_position_productdetail(OEHelper.datatemplate.indexOf(name));
					dialog.dismiss();				
				}
			});
			
			dialog.show();
	   		return true;
		  }	
		return true;
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
		callmethod_for_position_productdetail(position);
	}

	private void callmethod_for_position_productdetail(int position) {		
		checkqtycallornot=0;
		OEHelper oehelper1 = new OEHelper(getActivity());
		List<String> p = oehelper1.readproducttempalate();
	
		
		
		Product_Detail detail = new Product_Detail();
	//	Product_Detail_new detail = new Product_Detail_new();
		Bundle args = new Bundle();

		positionofimage=0;
		positionofimage=position;
		
		if (OEHelper.idofproduct_product.size() != 0) {
			String id_product_product = OEHelper.idofproduct_product
					.get(position);

			OEHelper.getidfrom_product_product = null;
			OEHelper.getidfrom_product_product = id_product_product;

			OEHelper.current_product_name = null;
			OEHelper.current_product_name = OEHelper.datatemplate.get(position);

		}
	//	oehelper1.available_qty_of_selected_location();

		if (OEHelper.list_price_of_product_template.size() != 0) {
			String sale_price_of_product_template = OEHelper.list_price_of_product_template
					.get(position);
			args.putString("saleprice", sale_price_of_product_template);
		}
		if (OEHelper.standard_price_of_product_template.size() != 0) {
			String cost_price_of_product_template = OEHelper.standard_price_of_product_template
					.get(position);
			args.putString("costprice", cost_price_of_product_template);
		}
		if (OEHelper.ean13_of_product_product.size() != 0) {
			String ean13_of_product_product1 = OEHelper.ean13_of_product_product
					.get(position);
			args.putString("ean13", ean13_of_product_product1);
		}
		if (OEHelper.default_code_of_product_product.size() != 0) {
			String reference_of_product_product1 = OEHelper.default_code_of_product_product
					.get(position);
			args.putString("reference", reference_of_product_product1);
		}
		if (OEHelper.type_of_product_template.size() != 0) {
			String type_Of_product_template = OEHelper.type_of_product_template
					.get(position);
			args.putString("type", type_Of_product_template);
		}
		if (OEHelper.supply_method_product_template.size() != 0) {
			String supply_method_product_template1 = OEHelper.supply_method_product_template
					.get(position);
			args.putString("supplymethod", supply_method_product_template1);
		}
		if (OEHelper.procure_method_product_template.size() != 0) {
			String procure_method_product_template1 = OEHelper.procure_method_product_template
					.get(position);
			args.putString("procuremethod", procure_method_product_template1);
		}
		if (OEHelper.uom_product_product.size() != 0) {
			String sale_price_of_product_template = OEHelper.uom_product_product
					.get(position);
			args.putString("uom", sale_price_of_product_template);
		}
		
		if (OEHelper.direct_qty_of_product.size() != 0) {
			String sale_price_of_product_template = OEHelper.direct_qty_of_product
					.get(position);
			args.putString("qty_available", sale_price_of_product_template);
		}
//		 if(OEHelper.image_of_product.size()!=0)
//		 {
//			 if(OEHelper.image_of_product.size()>position)
//			 {
//		 image_of_produc1t=OEHelper.image_of_product.get(position);
//			 }
//		 }

		if (OEHelper.datatemplate.size() != 0) {
			args.putString("name", OEHelper.datatemplate.get(position));
		}

		detail.setArguments(args);
		FragmentListener frag = (FragmentListener) getActivity();
		frag.startDetailFragment(detail);
		// frag.startMainFragment(detail, false);
	}

	@Override
	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		//Toast.makeText(getActivity(), "hiii", 5).show();
	}
}
