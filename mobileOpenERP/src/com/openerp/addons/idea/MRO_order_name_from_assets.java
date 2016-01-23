package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.mirasense.demos.productQR_scan_from_scandit;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class MRO_order_name_from_assets extends BaseFragment implements OnItemClickListener{

	
	ListView mListView;
	OEHelper oehelper;
	public static int position;
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

		View rootView = inflater.inflate(R.layout.partner_list,
				container, false);

		getActivity().setTitle(R.string.label_mro_order);
		MainActivity.global = 2;
		
		mListView = (ListView) rootView.findViewById(R.id.partner_listview);

		oehelper = new OEHelper(getActivity());
		oehelper.get_mro_order_from_assets();
	
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.qr_equip_item, OEHelper.name_mro_order_from_assets) {

			
			public View getView(int position, View convertView, ViewGroup parent) {
				View mView = convertView;

				if (mView == null)
					mView = getActivity().getLayoutInflater().inflate(
							R.layout.qr_equip_item, parent, false);
				TextView txv = (TextView) mView.findViewById(R.id.qr_name);
				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				txv.setTypeface(font,Typeface.BOLD);
				txv.setText(OEHelper.name_mro_order_from_assets.get(position));
				txv.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
				txv.setTextColor(Color.rgb(84, 84, 84));
				
				return mView;
			}
		});
		mListView.setOnItemClickListener(this);
		return rootView;
	}
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		
		position=0;
		position=arg2;
		mro_order_details mro=new mro_order_details();
		FragmentListener frag = (FragmentListener) getActivity();
		
		frag.startDetailFragment(mro);
		
	}

}
