package com.openerp.addons.idea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;
import com.tabactivity.tab1Activity;
import com.tabactivity.tab2Activity;

public class WO_to_MO_details extends BaseFragment {

	View mView;
	
	ListView mListView = null;
	
	TextView tvpro, loc;
	EditText edit;
	public static int p;
	int pos = 0;

	private Handler myHandler = new Handler();
	private Runnable runnable;
	Thread tread;
	private ProgressBar progressBar;
	
	int progressStatus = 0;

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
		mView = inflater.inflate(R.layout.wo_to_mo_details, container, false);

		MainActivity.global = 2;
		getActivity().setTitle(R.string.label_wo_to_mo);
		
		OEHelper oehelper = new OEHelper(getActivity());
		oehelper.getMFO_id_from_WO();
	
Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView	TextView1 = (TextView) mView.findViewById(R.id.qty);
		TextView	TextView2 = (TextView) mView.findViewById(R.id.pro);
		TextView	TextView3 = (TextView) mView.findViewById(R.id.uom);
		TextView	TextView4 = (TextView) mView.findViewById(R.id.sno);
	
		
		
		
		TextView1.setTypeface(font,Typeface.BOLD);
		TextView2.setTypeface(font,Typeface.BOLD);
		TextView3.setTypeface(font,Typeface.BOLD);
		TextView4.setTypeface(font,Typeface.BOLD);
		
	
		mListView = (ListView) mView.findViewById(R.id.listView1);
		mListView.setVisibility(View.GONE);

		progressBar = (ProgressBar) mView
				.findViewById(R.id.progressBarforconsumeproductlist);
		progressBar.setVisibility(View.GONE);

		//if (Menufecturing_OrdersListAll.call_product_to_consume_onces == 0) 
		
		//	Menufecturing_OrdersListAll.call_product_to_consume_onces = 1;
			runnable = new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (progressStatus == 0) {
						 performTask();
						
					}
					/* Hides the Progress bar */
					myHandler.post(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							progressBar.setVisibility(8);
							mListView.setVisibility(View.VISIBLE);
							
							if (OEHelper.mo_product_from_movestock.size() > 0) {
								mListView.setAdapter(new ArrayAdapter<String>(
										getActivity(),
										R.layout.molist_from_wo_item,
										OEHelper.mo_product_from_movestock) {

									public View getView(int position,
											View convertView, ViewGroup parent) {
										View mView = convertView;

										pos = 0;
										pos = position;

										if (mView == null)
											mView = getActivity()
													.getLayoutInflater()
													.inflate(
															R.layout.molist_from_wo_item,
															parent, false);
										// TextView txv = (TextView) mView
										// .findViewById(R.id.ref);
										// if
										// (OEHelper.mo_ref_from_movestock.size()
										// > position) {
										// txv.setText(""
										// + OEHelper.mo_ref_from_movestock
										// .get(position));
										// }
										// txv.setTextAppearance(getActivity(),
										// android.R.attr.textAppearance);

										TextView txv1 = (TextView) mView
												.findViewById(R.id.pro);
										Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
										txv1.setTypeface(font);
										if (OEHelper.mo_product_from_movestock
												.size() > position) {
											txv1.setText(""
													+ OEHelper.mo_product_from_movestock
															.get(position));
										}
										txv1.setTextAppearance(getActivity(),
												android.R.attr.textAppearance);
										// txv.setText("Product");

										TextView txv2 = (TextView) mView
												.findViewById(R.id.qty);
										txv2.setTypeface(font);
										if (OEHelper.mo_qty_from_movestock
												.size() > position) {
											txv2.setText(""
													+ OEHelper.mo_qty_from_movestock
															.get(position));
										}
										txv2.setTextAppearance(getActivity(),
												android.R.attr.textAppearance);

										TextView txv3 = (TextView) mView
												.findViewById(R.id.uom);
										txv3.setTypeface(font);
										if (OEHelper.mo_uom_from_movestock
												.size() > position) {
											txv3.setText(""
													+ OEHelper.mo_uom_from_movestock
															.get(position));
										}
										txv3.setTextAppearance(getActivity(),
												android.R.attr.textAppearance);

										TextView txv4 = (TextView) mView
												.findViewById(R.id.sno);
										txv4.setTypeface(font);
										if (OEHelper.mo_serial_from_movestock
												.size() > position) {
											txv4.setText(""
													+ OEHelper.mo_serial_from_movestock
															.get(position));
										}
										txv4.setTextAppearance(getActivity(),
												android.R.attr.textAppearance);

										
										return mView;
									}
								});
							} else {
								Toast.makeText(getActivity(),
										"No Record Found", 40).show();
							}

							

						}
					});

				}

				/* Do some task */
				private int performTask() {
				//	if(work_orders.checkfirsttimein_view_WO==0)	
					{
					//	work_orders.checkfirsttimein_view_WO=1;	
					OEHelper oehelper = new OEHelper(getActivity());
					oehelper.mrp_production_move_id();
					
					}
					return ++progressStatus;
				}
			};

		
			progressBar.setVisibility(0); /* 0 – visible 4 - invisible 8 - Gone */

			tread = new Thread(runnable);
			// new Thread(runnable).start();
			tread.start();

		

		
		return mView;

	}

	
	

		
		

	
	
	}
