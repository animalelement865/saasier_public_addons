package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class out_delivery_item_list extends BaseFragment{
	
	
	ListView mListView = null;
	private Handler myHandler = new Handler();
	private Runnable runnable;
	Thread tread;
	private ProgressBar progressBar;
	int myProgress = 0;
	int progressStatus = 0;
	TextView customer,scheduledate,creationdate,state,origin;
	Button qrcodeforprod_id;
	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View rootView = inflater.inflate(R.layout.out_delivery_product,
				container, false);
		
		getActivity().setTitle(R.string.label_Delivery_order);
		MainActivity.global = 2;
		
		progressBar = (ProgressBar) rootView
				.findViewById(R.id.progressBarforgetproductlinefordelivery_order);
		OEHelper oehelper =new OEHelper(getActivity());
		oehelper.out_delivery_using_stock_picking();
		
		//Toast.makeText(getActivity(), "out delivery screen called", 34).show();
		
	//	oehelper.getserialno_from_moname();
		
		customer=(TextView) rootView.findViewById(R.id.Tcustomerans);
		scheduledate=(TextView) rootView.findViewById(R.id.Tmintimeans);
		creationdate=(TextView) rootView.findViewById(R.id.Tcreationdateans);
		state=(TextView) rootView.findViewById(R.id.Tstateans);
		origin=(TextView) rootView.findViewById(R.id.Toriginans);
		
		qrcodeforprod_id=(Button) rootView.findViewById(R.id.qrcode_to_prod_id);
		qrcodeforprod_id.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				if(Dash_Board.checkloading==true)
				{
					//testing2 out = new testing2();
					QR_scan_for_mo_id_for_insert_serial_no out=new QR_scan_for_mo_id_for_insert_serial_no();
					FragmentListener frag = (FragmentListener) getActivity();
					frag.startDetailFragment(out);
				}
				else
				{
					Toast.makeText(getActivity(), "Wait", 40).show();
				}
			//	frag.startMainFragment(out, true);
				
			//	Toast.makeText(getActivity(), "qr scan for prod id", 30).show();
			}
		});
		
		if(OEHelper.customername.size()!=0)
		{
			customer.setText(""+OEHelper.customername.get(0));
		}
		if(OEHelper.mindate_for_out_delivery.size()!=0)
		{
			scheduledate.setText(""+OEHelper.mindate_for_out_delivery.get(0));
		}
		if(OEHelper.state_for_out_delivery.size()!=0)
		{
			state.setText(""+OEHelper.state_for_out_delivery.get(0));
		}
		if(OEHelper.origin_for_out_delivery.size()!=0)
		{
			if(OEHelper.origin_for_out_delivery.get(0).equals(""))
			{
				origin.setText("false");
			}
			else
			{
			origin.setText(""+OEHelper.origin_for_out_delivery.get(0));
			}
		}
		if(OEHelper.mindate_for_out_delivery.size()!=0)
		{
			creationdate.setText(""+OEHelper.mindate_for_out_delivery.get(0));
		}
		mListView = (ListView) rootView.findViewById(R.id.listview);
		//==================================================================
		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (progressStatus == 0) {
					progressStatus = performTask();

				}
				/* Hides the Progress bar */
				myHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						progressBar.setVisibility(8);
					
						mListView.setVisibility(View.VISIBLE);
						
							//if (OEHelper.picking_id_of_stock_move.size() != 0) 
							{
								
								mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
										R.layout.out_delivery_product_item, OEHelper.productlist_for_out_delivery) {

									public View getView(int position, View convertView, ViewGroup parent) {
										View mView = convertView;
										
//										 customername.add(partner1);
//										 origin_for_out_delivery.add(origin1); 
//										 mindate_for_out_delivery.add(min_date1); 
//										 date_for_out_delivery.add(date1); 
//										 state_for_out_delivery.add(state1); 
										
										
										if (mView == null)
											mView = getActivity().getLayoutInflater().inflate(
													R.layout.out_delivery_product_item, parent, false);
										TextView txv = (TextView) mView
												.findViewById(R.id.outproduct);
										
										txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
										
										Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
										txv.setTypeface(font,Typeface.BOLD);
										txv.setText(""+ OEHelper.productlist_for_out_delivery.get(position));
										
										TextView qty=(TextView) mView.findViewById(R.id.outqty);
										qty.setTypeface(font,Typeface.BOLD);
										qty.setText(""+OEHelper.qty_for_out_delivery.get(position));
										
										if(Dash_Board.checkfirstcall==true)
										{
											if(OEHelper.position_where_serial_no_insert==position)
											{
												if(OEHelper.serial_no_for_delivery_order.get(position).equals(""))
												{
													OEValues val = new OEValues();
													
													val.put("prodlot_id",""+OEHelper.selected_serial_no_for_insert_on_stmove);
													
													OEHelper oehelper=new OEHelper(getContext());
													Log.d("id="+Integer.parseInt(OEHelper.id_stmove_for_insert_serial.get(position)), "inserted id");
													oehelper.updater_serial_no_baseon_moid_on_delivery_order(val, Integer.parseInt(OEHelper.id_stmove_for_insert_serial.get(position)));
													
													TextView serial1=(TextView) mView.findViewById(R.id.serial_no);
													serial1.setTypeface(font,Typeface.BOLD);
													serial1.setText(""+OEHelper.selected_serial_no_for_insert_on_stmove);
												}
												else
												{
													Toast.makeText(getActivity(), "This product having already serial no", 56).show();
												}
											}
										}
										
										TextView serial = (TextView) mView
												.findViewById(R.id.serial_no);
										
										serial.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
										
										
										serial.setTypeface(font,Typeface.BOLD);
										serial.setText(""+ OEHelper.serial_no_for_delivery_order.get(position));
										
										return mView;
									}
								});

							} 
						progressStatus = 0;

					}
				});

			}
			/* Do some task */
			private int performTask() {
				
				if(Dash_Board.checkfirstcall==false)
				{
					
				OEHelper oehelper =new OEHelper(getActivity());
				//oehelper.out_delivery_using_stock_picking();
				oehelper.getProductlist_for_delivery_from_stock_move();
				Dash_Board.checkloading=true;
				}
				else
				{
					//Toast.makeText(getActivity(), "insert tv of serial no", 40).show();
				}
				return ++myProgress;
			}
		};

		myProgress = 0;
		mListView.setVisibility(View.GONE);
		progressBar.setVisibility(0); /* 0 – visible 4 - invisible 8 - Gone */
		tread = new Thread(runnable);
		// new Thread(runnable).start();
		tread.start();
		//==================================================================
		
		return rootView;
		
	}
	
	@Override
	public Object databaseHelper(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
