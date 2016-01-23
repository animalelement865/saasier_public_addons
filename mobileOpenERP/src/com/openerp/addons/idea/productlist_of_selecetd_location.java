package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.util.drawer.DrawerItem;

public class productlist_of_selecetd_location extends BaseFragment{

	
	ListView lv;
	ProgressBar pb;
	int myProgress=0;
	int progressStatus=0;
	private Handler myHandler = new Handler();
	//TextView name,ir,unit,qty;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.productlist_of_selected_location_main,
				container, false);
		
		getActivity().setTitle(R.string.label_productlist_locationwise);
		MainActivity.global = 2;
		lv = (ListView) rootView.findViewById(R.id.listfoproduct_of_location);
		
		pb=(ProgressBar) rootView.findViewById(R.id.progressBarforproductlistoflocation);
	//=============================================================	
		pb.setVisibility(View.VISIBLE);
		
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
						pb.setVisibility(8);
						progressStatus = 0;
						lv.setVisibility(View.VISIBLE);

						lv.setAdapter(new ArrayAdapter<String>(getActivity(),
								R.layout.productlist_of_location_first,
								OEHelper.datatemplate) {

							// mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
							// R.layout.fragment_product_bind_item,
							// OEHelper.product_pty_stock_move) {

							public View getView(int position, View convertView,
									ViewGroup parent) {
								View mView = convertView;

								if (mView == null)
									mView = getActivity().getLayoutInflater().inflate(
											R.layout.productlist_of_location_first, parent, false);
								TextView txv = (TextView) mView.findViewById(R.id.ir);
								Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
								txv.setTypeface(font);
								TextView txv1 = (TextView) mView
										.findViewById(R.id.name);
								txv1.setTypeface(font);
								TextView txv2 = (TextView) mView.findViewById(R.id.unit);
								txv2.setTypeface(font);
								TextView txv3 = (TextView) mView.findViewById(R.id.Qty);
								txv3.setTypeface(font);
								// txv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
								txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
								txv.setTextColor(Color.rgb(84, 84, 84));
								
								if(OEHelper.default_code_of_product_product.size()!=0 && OEHelper.default_code_of_product_product.size()>position )
								{
									txv.setText(""
										+ OEHelper.default_code_of_product_product
												.get(position));
								}
								
									txv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
									txv1.setTextColor(Color.rgb(84, 84, 84));
								
								
								if(OEHelper.datatemplate.size()!=0 && OEHelper.datatemplate.size()>position )
								{
									txv1.setText(""
										+ OEHelper.datatemplate
												.get(position));
								}
								
									txv2.setTextColor(Color.rgb(84, 84, 84));
									txv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
								
								if(OEHelper.uom_product_product1.size()!=0 && OEHelper.uom_product_product1.size()>position )
								{
									txv2.setText(""
										+ OEHelper.uom_product_product1
												.get(position));
								
								}
									txv3.setTextColor(Color.rgb(84, 84, 84));
									txv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
								
								if(OEHelper.sourcetotalqtynew1.length>position)
								{
									txv3.setText("" + OEHelper.sourcetotalqtynew1[position]);
								}
									
//									if(OEHelper.total_qty_of_product_in_selected_location.size()!=0 && OEHelper.total_qty_of_product_in_selected_location.size()>position )
//									{
//										txv3.setText(""
//											+ OEHelper.total_qty_of_product_in_selected_location
//													.get(position));
//									
//									}
//									
							
								return mView;
							}

						});
						
					}
				});

			}

			/* Do some task */
			private int performTask() {
			
				OEHelper oeh = new OEHelper(getActivity());		
			
				oeh.product_name();
				oeh.releted_selected_location_for_main();
				oeh.getproductfromlocationid();
			//	oeh.stock_location_for_qty_of_product_of_location();
				
												
				return ++myProgress;
			}
		}).start();

	
	//===============================================================
	
//		else
//		{
//			
//		}
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
