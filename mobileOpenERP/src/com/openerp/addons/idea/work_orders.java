package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class work_orders extends BaseFragment implements OnItemClickListener {

	static int position_for_work_order = 0;
	TextView equ_name, work_center;
	ListView mListView = null;
	static int checkfirsttimein_view_WO=0;

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
		setHasOptionsMenu(true);

		View rootView = inflater.inflate(R.layout.work_order_list, container,
				false);

		getActivity().setTitle(R.string.label_work_order);
		MainActivity.global = 2;

		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		
		TextView productname1 = (TextView) rootView.findViewById(R.id.textView1);
		TextView productname2 = (TextView) rootView.findViewById(R.id.textView2);
		TextView productname3 = (TextView) rootView.findViewById(R.id.textView3);
		TextView productname4 = (TextView) rootView.findViewById(R.id.textView4);
		
		
		
		
		productname1.setTypeface(font,Typeface.BOLD);
		productname2.setTypeface(font,Typeface.BOLD);
		productname3.setTypeface(font,Typeface.BOLD);
		productname4.setTypeface(font,Typeface.BOLD);
		
		mListView = (ListView) rootView.findViewById(R.id.listforqr_equip);
		equ_name = (TextView) rootView.findViewById(R.id.equ_name);
	
		equ_name.setTypeface(font);
		work_center = (TextView) rootView.findViewById(R.id.work_center);
		work_center.setTypeface(font);
		OEHelper oehelper = new OEHelper(getActivity());
		List<String> k1 = oehelper.mrp_workcenter();
		oehelper.mrp_production_workcenter_line();
		
		equ_name.setText(QR_Equipment.currentname);
		if (OEHelper.work_centername_work_order.size() != 0) {
			work_center.setText(OEHelper.work_centername_work_order.get(0));
		}
		// oehelper=new OEHelper(getActivity());

		// List<String> k= oehelper.qr_equipmentname();

		if (OEHelper.data_of_mrp_production_workcenter_line.size() != 0) {

			mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.work_order_first,
					OEHelper.data_of_mrp_production_workcenter_line) {

				// mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				// R.layout.fragment_product_bind_item,
				// OEHelper.product_pty_stock_move) {

				public View getView(int position, View convertView,
						ViewGroup parent) {
					View mView = convertView;

					if (mView == null)
						mView = getActivity().getLayoutInflater().inflate(
								R.layout.work_order_first, parent, false);
					TextView txv = (TextView) mView.findViewById(R.id.MO);
					Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
					txv.setTypeface(font);
					TextView txv1 = (TextView) mView
							.findViewById(R.id.Schedule_date);
					txv1.setTypeface(font);
					TextView txv2 = (TextView) mView.findViewById(R.id.product);
					txv2.setTypeface(font);
					TextView txv3 = (TextView) mView.findViewById(R.id.Qty);
					txv3.setTypeface(font);
					// txv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
					txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv.setTextColor(Color.rgb(84, 84, 84));
					txv.setText(""
							+ OEHelper.mo_of_mrp_production_workcenter_line
									.get(position));
					txv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv1.setTextColor(Color.rgb(84, 84, 84));
					txv1.setText(""
							+ OEHelper.datestart_of_mrp_production_workcenter_line
									.get(position));
					txv2.setTextColor(Color.rgb(84, 84, 84));
					txv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv2.setText(""
							+ OEHelper.product_of_mrp_production_workcenter_line
									.get(position));
					txv3.setTextColor(Color.rgb(84, 84, 84));
					txv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv3.setText("" + OEHelper.qty_order.get(position));

					// txv.setTextAppearance(getActivity(),
					// android.R.attr.textAppearance);

					return mView;
				}

			});
		} else {
			Toast.makeText(getActivity(), "No Order Found", 30).show();
		}
		mListView.setOnItemClickListener(this);
		return rootView;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		checkfirsttimein_view_WO=0;
		position_for_work_order = 0;
		position_for_work_order = arg2;
		OEHelper.selected_moname_from_WO="";
		if(OEHelper.mo_of_mrp_production_workcenter_line.size()>position_for_work_order)
		{
		OEHelper.selected_moname_from_WO=OEHelper.mo_of_mrp_production_workcenter_line.get(position_for_work_order);
		}
		//
		// EDIT_SELECTED_WORK_ORDER edit_order=new EDIT_SELECTED_WORK_ORDER();
		// FragmentListener frag = (FragmentListener) getActivity();
		//
		// frag.startMainFragment(edit_order, true);

		work_order_detail edit_order = new work_order_detail();
		FragmentListener frag = (FragmentListener) getActivity();
		frag.startDetailFragment(edit_order);

	}

}
