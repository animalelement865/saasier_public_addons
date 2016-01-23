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

public class product_to_consume_of_selected_product extends BaseFragment {

	View mView;
	OEHelper oehelper;
	ListView mListView = null;
	Button btnevent[];
	TextView tvpro, loc;
	EditText edit;
	public static int p1;
	int pos = 0;

	private ProgressBar progressBar;

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
		mView = inflater.inflate(R.layout.product_to_consume, container, false);

		getActivity().setTitle(R.string.label_product_to_consume);
		
		mListView = (ListView) mView.findViewById(R.id.listView1);
		mListView.setVisibility(View.VISIBLE);


Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView	TextView1 = (TextView) mView.findViewById(R.id.qty);
		TextView	TextView2 = (TextView) mView.findViewById(R.id.pro);
		TextView	TextView3 = (TextView) mView.findViewById(R.id.uom);
		TextView	TextView4 = (TextView) mView.findViewById(R.id.sno);
		TextView	txv11 = (TextView) mView.findViewById(R.id.btnevent);
		
		
		
		TextView1.setTypeface(font,Typeface.BOLD);
		TextView2.setTypeface(font,Typeface.BOLD);
		TextView3.setTypeface(font,Typeface.BOLD);
		TextView4.setTypeface(font,Typeface.BOLD);
		txv11.setTypeface(font,Typeface.BOLD);
		
		progressBar = (ProgressBar) mView
				.findViewById(R.id.progressBarforconsumeproductlist);
		progressBar.setVisibility(View.GONE);
		call_else_method();
		
	return mView;

	}

	public void call_else_method() {

//==============================================================
		
		btnevent = new Button[OEHelper.menufecturing_product.size()];
		
	//	Toast.makeText(getActivity(), "menufecturing_product="+OEHelper.menufecturing_product.size(), 40).show();
		if (OEHelper.menufecturing_product.size() > 0) {
			mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.product_to_consume_item,
					OEHelper.menufecturing_product) {

				public View getView(int position, View convertView,
						ViewGroup parent) {
					View mView = convertView;

					pos = 0;
					pos = position;

					if (mView == null)
						mView = getActivity().getLayoutInflater()
								.inflate(R.layout.product_to_consume_item,
										parent, false);

					TextView txv1 = (TextView) mView.findViewById(R.id.pro);
					Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
					txv1.setTypeface(font);
					if (OEHelper.menufecturing_product.size() > position) {
						txv1.setText(""
								+ OEHelper.menufecturing_product.get(position));
					}
					txv1.setTextAppearance(getActivity(),
							android.R.attr.textAppearance);
					// txv.setText("Product");

					TextView txv2 = (TextView) mView.findViewById(R.id.qty);
					txv2.setTypeface(font);
					if (OEHelper.moqty.size() > position) {
						txv2.setText("" + OEHelper.moqty.get(position));
					}
					txv2.setTextAppearance(getActivity(),
							android.R.attr.textAppearance);

					TextView txv3 = (TextView) mView.findViewById(R.id.uom);
					txv3.setTypeface(font);
					if (OEHelper.mo_uom_from_movestock.size() > position) {
						txv3.setText(""
								+ OEHelper.mo_uom_from_movestock.get(position));
					}
					txv3.setTextAppearance(getActivity(),
							android.R.attr.textAppearance);

					TextView txv4 = (TextView) mView.findViewById(R.id.sno);
					txv4.setTypeface(font);
					if (OEHelper.mo_serial_from_movestock.size() > position) {
						txv4.setText(""
								+ OEHelper.mo_serial_from_movestock
										.get(position));
					}
					txv4.setTextAppearance(getActivity(),
							android.R.attr.textAppearance);

					btnevent[position] = (Button) mView
							.findViewById(R.id.btnevent);
					// btnevent[position].setText(resid);

					btnevent[position].setTag(position);
					// btnevent[position].setId(position);
					btnevent[position]
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {

									try {
										p1 =  (Integer) arg0.getTag();
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
									

									QR_scan_for_insert_product_to_consume_selected_product t=new QR_scan_for_insert_product_to_consume_selected_product();
									//	testing t=new  testing();
										FragmentListener frag = (FragmentListener) getActivity();
										frag.startDetailFragment(t);

								}
							});

					return mView;
				}
			});
		} else {
			Toast.makeText(getActivity(), "No Record Found", 40).show();
		}
	}
}
