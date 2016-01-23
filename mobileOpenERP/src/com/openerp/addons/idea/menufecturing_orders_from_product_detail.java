package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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

public class menufecturing_orders_from_product_detail extends BaseFragment implements OnItemClickListener {

	
	ListView lvforMenufecturingOrder;
	// int lastindex=0;
	 ArrayList<String> arrlistfornoproduct;
	 int count=0;
	 TextView txv;
		View mView;
		OEHelper oehelper;
		private Handler myHandler = new Handler();
		private Runnable runnable;
		Thread tread;
		private ProgressBar progressBar;
		int myProgress = 0;
		int progressStatus = 0;
		
	public Object databaseHelper(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
	
	//@SuppressWarnings("static-access")
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mView = inflater.inflate(R.layout.product_menufecturingall_order, container,
				false);
		
		getActivity().setTitle(R.string.label_menufacturing);
		lvforMenufecturingOrder=(ListView)mView.findViewById(R.id.listViewOfMenufecturingOrder);
	
Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView	TextView1 = (TextView) mView.findViewById(R.id.textView1);
		TextView	TextView2 = (TextView) mView.findViewById(R.id.textView2);
		TextView	TextView3 = (TextView) mView.findViewById(R.id.textView3);
		TextView	TextView4 = (TextView) mView.findViewById(R.id.textView4);
		TextView	txv11 = (TextView) mView.findViewById(R.id.textView11);
		TextView	txv19 = (TextView) mView.findViewById(R.id.textView19);
		TextView	txv33 = (TextView) mView.findViewById(R.id.textView33);
		
		
		TextView1.setTypeface(font,Typeface.BOLD);
		TextView2.setTypeface(font,Typeface.BOLD);
		TextView3.setTypeface(font,Typeface.BOLD);
		TextView4.setTypeface(font,Typeface.BOLD);
		txv11.setTypeface(font,Typeface.BOLD);
		txv19.setTypeface(font,Typeface.BOLD);
		txv33.setTypeface(font,Typeface.BOLD);
		
		progressBar = (ProgressBar) mView
				.findViewById(R.id.progressBarforconsumeproductlist);
		
		
		txv = (TextView) mView.findViewById(R.id.txv_for_product_no_mfo);
		
		txv.setTypeface(font);
		txv.setVisibility(View.GONE);
		
		if(Product_Detail.check_for_run_tread_or_not==1)
		{
			progressStatus=1;
		}
				
		if(Product_Detail.check_for_run_tread_or_not==0)
		{
			Product_Detail.check_for_run_tread_or_not=1;
		}
		
		//==================================
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
					
						
						if(OEHelper.menufecturing_product.size()!=0)
						{
							count=1;
						
							lvforMenufecturingOrder.setVisibility(View.VISIBLE);
							lvforMenufecturingOrder.setAdapter(new ArrayAdapter<String>(getActivity(),
								R.layout.menufecturing_order_list,OEHelper.menufecturing_product) {
						//		R.layout.menufecturing_order_list, OEHelper.product_pty_stock_move) {
							public View getView(int position, View convertView, ViewGroup parent) {
								View mView1 = convertView;
							
								if (mView1 == null)
									mView1 = getActivity().getLayoutInflater().inflate(
											R.layout.menufecturing_listall_item, parent, false);
								
							//	txv.setVisibility(mView.GONE);
								
								TextView name = (TextView) mView1.findViewById(R.id.menu_text);
								Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
								name.setTypeface(font);
								TextView pname = (TextView) mView1.findViewById(R.id.moproduct);
								pname.setTypeface(font);
								TextView qty = (TextView) mView1.findViewById(R.id.qty);
								qty.setTypeface(font);
								TextView state1 = (TextView) mView1.findViewById(R.id.state);
								state1.setTypeface(font);
								name.setText(OEHelper.menufecturing_orderlistAll.get(position));
								pname.setText(OEHelper.menufecturing_product.get(position));
								qty.setText(OEHelper.moqty.get(position));
								state1.setText(OEHelper.mostate.get(position));
								
						
								return mView1;
							}
							
						
						});
						
						
						}	
						else
						{
							txv.setVisibility(View.VISIBLE);
						}
						
						progressStatus = 0;

					}
				});

			}

			/* Do some task */
			private int performTask() {
				oehelper = new OEHelper(getActivity());
				oehelper.mo_record_of_selected_product();
				return ++myProgress;
			}
		};

		// String k;
			//below code for only start thread
		myProgress = 0;
		lvforMenufecturingOrder.setVisibility(View.GONE);
		progressBar.setVisibility(0); /* 0 – visible 4 - invisible 8 - Gone */

		tread = new Thread(runnable);
		// new Thread(runnable).start();
		tread.start();
		//=================================
		
	
		if(lvforMenufecturingOrder!=null)
		{

		lvforMenufecturingOrder.setOnItemClickListener(this);
		}
		return mView;
		
	}
		

	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
	
		Menufecturing_OrdersListAll.call_product_to_consume_onces=0;
		OEHelper.selected_mrp_id=null;
		OEHelper.selected_mrp_id=OEHelper.menufecturing_product_id.get(arg2);
		product_to_consume_of_selected_product p_to_c =new product_to_consume_of_selected_product();
		FragmentListener frag = (FragmentListener) getActivity();
		frag.startDetailFragment(p_to_c);
	
	}


}
