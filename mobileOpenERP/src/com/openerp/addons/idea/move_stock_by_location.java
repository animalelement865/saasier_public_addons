package com.openerp.addons.idea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class move_stock_by_location extends BaseFragment {
	View mView;
	SeekBar seekBar;
	TextView tvforqty, tvforproductname;
	float qty1 = 0;
	Button qrscan, moveqty;
	public static float transferstock = 0;
	// String arr[]={"sourcelocation1","sourcelocation2","sourcelocation3"};
	Spinner lvforsourcelocation, lvfordestinationlocation;
	ArrayAdapter<String> arr1, arr2;
	public static String selectedsourceid = null;
	public static String selecteddestid = null;
	private ProgressBar progressBar;
	int progressStatus = 0;
	int myProgress = 0;

	public static int checkQRSCAN_OR_manually = 0;
	private Handler myHandler = new Handler();

	// int position=0;
	// String item=null;
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

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.move_stock_bylocation, container,
				false);
		moveqty = (Button) mView.findViewById(R.id.movestock);
		qrscan = (Button) mView.findViewById(R.id.btnqrlocation);
		seekBar = (SeekBar) mView.findViewById(R.id.seekBar1);
		tvforqty = (TextView) mView.findViewById(R.id.tvforqty);
		lvforsourcelocation = (Spinner) mView
				.findViewById(R.id.spinnerforsorcelocation);
		lvfordestinationlocation = (Spinner) mView
				.findViewById(R.id.spinnerfordestinationlocation);
		tvforproductname = (TextView) mView
				.findViewById(R.id.product_stock_move);
		tvforproductname.setText("" + Product_Detail.productnameselected);
		progressBar = (ProgressBar) mView
				.findViewById(R.id.progressBarforsorcelocation);

//		// OEHelper.product_pty_stock_move.clear();
//		if (OEHelper.product_pty_stock_move.size() != 0) {
//			tvforqty.setText(OEHelper.product_pty_stock_move.get(0));
//
//		}

		// Toast.makeText(getActivity(), "stock location", 40).show();
		if (OEHelper.product_pty_stock_move.size() != 0) {
			String qty = OEHelper.product_pty_stock_move.get(0);
			try {
				qty1 = Float.parseFloat(qty);

				// seekBar.setMax(qty1);

			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// Toast.makeText(getActivity(), "transferstock="+transferstock,
				// 8).show();
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {

				transferstock = ((qty1 * progress) / 100);
				if (OEHelper.product_pty_stock_move.size() != 0) {
					tvforqty.setText("" + Math.round(transferstock));

				}

				// if(OEHelper.product_pty_stock_move.size()!=0)
				// {
				// OEHelper.product_pty_stock_move.get(0);
				// }

			}
		});

		qrscan.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// testing qr_scan_location=new testing();
				QR_scan_for_location_trasfer_qty qr_scan_location = new QR_scan_for_location_trasfer_qty();
				FragmentListener frag = (FragmentListener) getActivity();
				frag.startDetailFragment(qr_scan_location);

			}
		});
		moveqty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				OEValues val = new OEValues();

				// int id=Integer.parseInt(OEHelper.getidfrom_product_product);

				// for insert==================

				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String date1 = dateFormat.format(date);

				val.put("product_qty", Math.round(transferstock));
				val.put("location_dest_id", OEHelper.desti_id1);// destination id OEHelper.desti_id1				
				// val.put("location_id",OEHelper.sourceid);//source id
				val.put("location_id", selectedsourceid);
				// val.put("type","out");
				val.put("state", "done");
				val.put("product_id", OEHelper.getidfrom_product_product);
				val.put("date", date1);
				val.put("name", OEHelper.current_product_name);// name of product where transfer
															   //OEHelper.current_product_name
				val.put("company_id", OEHelper.companyid1);// Integer.parseInt(OEHelper.companyid1);
				// val.put("weight_uom_id",1);//Integer.parseInt(OEHelper.weight_uom_id1)
				val.put("date_expected", date1);
				val.put("product_uom", OEHelper.productuom2);
				OEHelper oehelper1 = new OEHelper(getActivity());
				oehelper1.insertstockqty(val);
				// =======================================

				Toast.makeText(getActivity(),"Stock Transfer :" +  Math.round(transferstock), 40).show();
			}
		});

		OEHelper oehelper = new OEHelper(getActivity());
		oehelper.sourceLocation_parent();
		oehelper.sourceLocationfromstock_warehouse();
	
		
		lvforsourcelocation.setVisibility(View.VISIBLE);

		arr1 = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_custom_layout,
				OEHelper.sourcelocation_of_stock_location);
		arr2 = new ArrayAdapter<String>(getActivity(),
				R.layout.spinner_custom_layout,
				OEHelper.destinationlocation_of_stock_location);
		arr1.setDropDownViewResource(R.layout.spinner_custom_layout);
		arr2.setDropDownViewResource(R.layout.spinner_custom_layout);

		lvforsourcelocation.setAdapter(arr1);
		lvfordestinationlocation.setAdapter(arr2);

		lvforsourcelocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
					public void onItemSelected(AdapterView<?> parent,
							View view, int pos, long id) {

						// Toast.makeText(getActivity(), "onitem", 50).show();
						if (pos > 0 || checkQRSCAN_OR_manually == 1) {

							if (checkQRSCAN_OR_manually == 1) {
								checkQRSCAN_OR_manually = 0;

							} else {
								selectedsourceid = "";
								selectedsourceid = OEHelper.sorceid_of_stock_location
										.get(pos - 1);
							}

							progressBar.setVisibility(View.VISIBLE);
							lvforsourcelocation.setVisibility(View.GONE);
							myProgress = 0;

							new Thread(new Runnable() {

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
											lvforsourcelocation
													.setVisibility(View.VISIBLE);
											seekBar.setProgress(0);
																					
											tvforqty.setText(""
													+ Math.round(OEHelper.sourcetotalqty));
											qty1 = OEHelper.sourcetotalqty;
										

											progressStatus = 0;

										}
									});

								}

								/* Do some task */
								private int performTask() {
								
									OEHelper oehelper = new OEHelper(getActivity());
									oehelper.releted_selected_stock_location_id();
									oehelper.main_for_get_available_qty();
								
									return ++myProgress;
								}
							}).start();

						}

						// tvforproductname.setText("" +selectedsourceid);
					}

					public void onNothingSelected(AdapterView<?> parent) {
					}
				});

				lvfordestinationlocation.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub

						if (arg2 > 0) {

							selecteddestid = "";
							selecteddestid = OEHelper.dest_id_of_stock_location
									.get(arg2 - 1);

							OEHelper oehelper = new OEHelper(getActivity());
							oehelper.stockmoveforqtytransfer();

							// Toast.makeText(getActivity(),
							// "id="+selecteddestid, 8).show();
						}

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}
				});

		return mView;
	}

	@Override
	public void onPause() {

		super.onPause();
	}
}
