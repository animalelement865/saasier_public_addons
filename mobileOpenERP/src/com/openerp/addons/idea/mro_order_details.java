package com.openerp.addons.idea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.util.drawer.DrawerItem;

public class mro_order_details extends BaseFragment implements OnClickListener{

	Button confirm,cancel;
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

		View rootView = inflater.inflate(R.layout.mro_order_detail,
				container, false);

		getActivity().setTitle(R.string.label_mro_order_details);

		MainActivity.global = 2;
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView	TextView1 = (TextView) rootView.findViewById(R.id.maintenance_type);
		TextView	TextView2 = (TextView) rootView.findViewById(R.id.description);
		TextView	TextView3 = (TextView) rootView.findViewById(R.id.date_planned);
		TextView	TextView4 = (TextView) rootView.findViewById(R.id.location);
		TextView	txv11 = (TextView) rootView.findViewById(R.id.problem_description);
		TextView	txv14 = (TextView) rootView.findViewById(R.id.plan_date);
		TextView	txv19 = (TextView) rootView.findViewById(R.id.duration);
		TextView	txv33 = (TextView) rootView.findViewById(R.id.production_date);
		
		
		
		TextView1.setTypeface(font,Typeface.BOLD);
		TextView2.setTypeface(font,Typeface.BOLD);
		TextView3.setTypeface(font,Typeface.BOLD);
		TextView4.setTypeface(font,Typeface.BOLD);
		txv11.setTypeface(font,Typeface.BOLD);
		txv14.setTypeface(font,Typeface.BOLD);
		txv19.setTypeface(font,Typeface.BOLD);
		txv33.setTypeface(font,Typeface.BOLD);
		
		confirm=(Button) rootView.findViewById(R.id.btnstart);
		cancel=(Button) rootView.findViewById(R.id.cancle1);
		
		if(OEHelper.state_mro_order_from_assets.get(MRO_order_name_from_assets.position).equals("draft"))
		{
			confirm.setText("Confirm MT");
		}
		else if(OEHelper.state_mro_order_from_assets.get(MRO_order_name_from_assets.position).equals("ready"))
		{
			confirm.setText("Done");
		}
		else if(OEHelper.state_mro_order_from_assets.get(MRO_order_name_from_assets.position).equals("done"))
		{
			confirm.setVisibility(View.INVISIBLE);
			cancel.setVisibility(View.INVISIBLE);
		}
		confirm.setOnClickListener(this);
		cancel.setOnClickListener(this);
		
		TextView name_mro = (TextView) rootView
				.findViewById(R.id.name_mro);
		
		name_mro.setTypeface(font);
		if (OEHelper.name_mro_order_from_assets.size() != 0) {
			name_mro.setText(OEHelper.name_mro_order_from_assets
					.get(MRO_order_name_from_assets.position));
		}
		
		TextView m_type_detail = (TextView) rootView
				.findViewById(R.id.m_type_detail);
		m_type_detail.setTypeface(font);
		if (OEHelper.maintanance_type_mro_order_from_assets.size() != 0) {
			m_type_detail.setText(OEHelper.maintanance_type_mro_order_from_assets
					.get(MRO_order_name_from_assets.position));
		}
		
		TextView serial = (TextView) rootView.findViewById(R.id.des_result);
		serial.setTypeface(font);
		if (OEHelper.description_mro_order_from_assets.size() != 0) {
			serial.setText(OEHelper.description_mro_order_from_assets
					.get(MRO_order_name_from_assets.position));
		}
		
		TextView Asset_no = (TextView) rootView.findViewById(R.id.date_result);
		Asset_no.setTypeface(font);
		if (OEHelper.date_planned_mro_order_from_assets.size() != 0) {
			Asset_no.setText(OEHelper.date_planned_mro_order_from_assets
					.get(MRO_order_name_from_assets.position));
		}
		
		TextView QR_code = (TextView) rootView.findViewById(R.id.location_resule);
		QR_code.setTypeface(font);
		if (OEHelper.parts_location_mro_order_from_assets.size() != 0) {
			QR_code.setText(OEHelper.parts_location_mro_order_from_assets
					.get(MRO_order_name_from_assets.position));
		}
		
		TextView pro_result = (TextView) rootView.findViewById(R.id.pro_result);
		pro_result.setTypeface(font);
		if (OEHelper.problem_description_mro_order_from_assets.size() != 0) {
			pro_result.setText(OEHelper.problem_description_mro_order_from_assets
					.get(MRO_order_name_from_assets.position));
		}
		
		return rootView;
	}

	@Override
	public void onClick(View arg0) {
		
		OEHelper oehelper = new OEHelper(getActivity());
		OEValues val = new OEValues();
		if(arg0.getId()==R.id.btnstart)
		{
			
			if(confirm.getText().toString().equals("Done"))
			{
				
				DateFormat dateFormat = new SimpleDateFormat(
						"yyyy/MM/dd HH:mm:ss");
				Date date = new Date();
				String date1 = dateFormat.format(date);
			   // Toast.makeText(getActivity(), "done", 4).show();	
				val.put("state", "done");
				val.put("date_execution", date1);
				oehelper.update_state_for_mro(val,Integer.parseInt(OEHelper.id_mro_order_from_assets.get(MRO_order_name_from_assets.position)));
				
				confirm.setVisibility(View.INVISIBLE);
				cancel.setVisibility(View.INVISIBLE);
			}
			else if(confirm.getText().toString().equals("Confirm MT"))
			{
				//Toast.makeText(getActivity(), "confirm", 4).show();	
				val.put("state", "ready");
				oehelper.update_state_for_mro(val,Integer.parseInt(OEHelper.id_mro_order_from_assets.get(MRO_order_name_from_assets.position)));
				
				confirm.setText("Done");
			}
			
		}
		else 
		{
			//Toast.makeText(getActivity(), "cancel", 4).show();	
			val.put("state", "cancel");
			oehelper.update_state_for_mro(val,Integer.parseInt(OEHelper.id_mro_order_from_assets.get(MRO_order_name_from_assets.position)));
			
			confirm.setVisibility(View.INVISIBLE);
			cancel.setVisibility(View.INVISIBLE);
			
			OEHelper.name_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.maintanance_type_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.description_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.date_planned_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.parts_location_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.problem_description_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.state_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
			OEHelper.id_mro_order_from_assets.remove(MRO_order_name_from_assets.position);
		}
		
	}
}
