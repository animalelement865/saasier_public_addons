package com.openerp.addons.idea;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class work_order_detail extends BaseFragment implements OnClickListener {

	
	TextView schedule_date,end_date_planned,no_cycle,no_hours,startdateactual,enddateactual,qty,name,delayhour,product,uom;
	Button btnstart,btnpendind,view_detail;
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
		
		
	
	View rootView = inflater.inflate(R.layout.work_center_detail, container,
			false);
	getActivity().setTitle(R.string.label_work_order_detail);
	
	Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
	
	
	TextView productname1 = (TextView) rootView.findViewById(R.id.end_date);
	TextView productname2 = (TextView) rootView.findViewById(R.id.enddate);
	TextView productname3 = (TextView) rootView.findViewById(R.id.sch_date);
	TextView productname4 = (TextView) rootView.findViewById(R.id.no_of_hour);
	TextView productname5 = (TextView) rootView.findViewById(R.id.no_of_cycle);
	TextView productname6 = (TextView) rootView.findViewById(R.id.Product_To_Produce);
	TextView productname7 = (TextView) rootView.findViewById(R.id.startdate);
	TextView productname8 = (TextView) rootView.findViewById(R.id.workinghours);
	TextView productname9 = (TextView) rootView.findViewById(R.id.hours);
	TextView productname10 = (TextView) rootView.findViewById(R.id.productname_wo);
	TextView productname11 = (TextView) rootView.findViewById(R.id.qty);
	TextView productname12 = (TextView) rootView.findViewById(R.id.plan_date);
	TextView productname13 = (TextView) rootView.findViewById(R.id.production_date);
	TextView productname14 = (TextView) rootView.findViewById(R.id.duration);
	
	
	productname1.setTypeface(font,Typeface.BOLD);
	productname2.setTypeface(font,Typeface.BOLD);
	productname3.setTypeface(font,Typeface.BOLD);
	productname4.setTypeface(font,Typeface.BOLD);
	productname5.setTypeface(font,Typeface.BOLD);
	productname6.setTypeface(font,Typeface.BOLD);
	productname7.setTypeface(font,Typeface.BOLD);
	productname8.setTypeface(font,Typeface.BOLD);
	productname9.setTypeface(font,Typeface.BOLD);
	productname10.setTypeface(font,Typeface.BOLD);
	productname11.setTypeface(font,Typeface.BOLD);
	productname12.setTypeface(font,Typeface.BOLD);
	productname13.setTypeface(font,Typeface.BOLD);
	productname14.setTypeface(font,Typeface.BOLD);
	
	btnstart=(Button) rootView.findViewById(R.id.btnstart);
	btnstart.setOnClickListener(this);
	btnpendind=(Button) rootView.findViewById(R.id.pending);
	btnpendind.setOnClickListener(this);
	btnpendind.setVisibility(View.INVISIBLE);
	view_detail=(Button) rootView.findViewById(R.id.View_Details);
	view_detail.setOnClickListener(this);
	
	btnstart.setTypeface(font,Typeface.BOLD);
	btnpendind.setTypeface(font,Typeface.BOLD);
	view_detail.setTypeface(font,Typeface.BOLD);
	
	if(OEHelper.state_of_work_order.get(work_orders.position_for_work_order).equals("draft"))
	{
		//Toast.makeText(getActivity(), "draft", 20).show();
		btnstart.setText("Start");
		btnpendind.setVisibility(View.INVISIBLE);
	}
	else if(OEHelper.state_of_work_order.get(work_orders.position_for_work_order).equals("startworking"))
	{
		btnstart.setText("Finished");
		btnpendind.setVisibility(View.VISIBLE);
	}
	
	schedule_date=(TextView) rootView.findViewById(R.id.sch_date_result);
	
	schedule_date.setTypeface(font);
	end_date_planned=(TextView) rootView.findViewById(R.id.end_date_result);
	end_date_planned.setTypeface(font);
	no_cycle=(TextView) rootView.findViewById(R.id.cycle_resule);
	no_cycle.setTypeface(font);
	no_hours=(TextView) rootView.findViewById(R.id.hour_result);
	no_hours.setTypeface(font);
	startdateactual=(TextView) rootView.findViewById(R.id.startdateresult);
	startdateactual.setTypeface(font);
	enddateactual=(TextView) rootView.findViewById(R.id.enddateresult);
	enddateactual.setTypeface(font);
	name=(TextView) rootView.findViewById(R.id.name_wo_detail);
	name.setTypeface(font);
	delayhour=(TextView) rootView.findViewById(R.id.workinghourresult);
	delayhour.setTypeface(font);
	qty=(TextView) rootView.findViewById(R.id.qtyresult);
	qty.setTypeface(font);
	uom=(TextView) rootView.findViewById(R.id.hourresult);//unit of measure
	uom.setTypeface(font);
	product=(TextView) rootView.findViewById(R.id.product_wo_result);
	product.setTypeface(font);
	
	if(OEHelper.data_of_mrp_production_workcenter_line.size()!=0  && OEHelper.data_of_mrp_production_workcenter_line.size() > work_orders.position_for_work_order)
	{
		name.setText(""+OEHelper.data_of_mrp_production_workcenter_line.get(work_orders.position_for_work_order));
	}
	if(OEHelper.delay_hours_work_order.size()!=0  && OEHelper.delay_hours_work_order.size() > work_orders.position_for_work_order)
	{
		delayhour.setText(""+OEHelper.delay_hours_work_order.get(work_orders.position_for_work_order));
	}
	if(OEHelper.datestart_of_mrp_production_workcenter_line.size()!=0  && OEHelper.datestart_of_mrp_production_workcenter_line.size() > work_orders.position_for_work_order)
	{
		schedule_date.setText(""+OEHelper.datestart_of_mrp_production_workcenter_line.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.date_end_work_order.size()!=0  && OEHelper.date_end_work_order.size() > work_orders.position_for_work_order)
	{
		end_date_planned.setText(""+OEHelper.date_end_work_order.get(work_orders.position_for_work_order));
	}
	
	if (OEHelper.hours_order.size() != 0
			&& OEHelper.hours_order.size() > work_orders.position_for_work_order) {
		no_hours.setText(OEHelper.hours_order
				.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.cycle_order.size()!=0  && OEHelper.cycle_order.size() > work_orders.position_for_work_order)
	{
		no_cycle.setText(""+OEHelper.cycle_order.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.datestart_of_mrp_production_workcenter_line.size()!=0  && OEHelper.datestart_of_mrp_production_workcenter_line.size() > work_orders.position_for_work_order)
	{
		startdateactual.setText(""+OEHelper.datestart_of_mrp_production_workcenter_line.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.date_end_work_order.size()!=0  && OEHelper.date_end_work_order.size() > work_orders.position_for_work_order)
	{
		enddateactual.setText(""+OEHelper.date_end_work_order.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.qty_order.size()!=0  && OEHelper.qty_order.size() > work_orders.position_for_work_order)
	{
		qty.setText(""+OEHelper.qty_order.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.uom_work_order.size()!=0  && OEHelper.uom_work_order.size() > work_orders.position_for_work_order)
	{
		uom.setText(""+OEHelper.uom_work_order.get(work_orders.position_for_work_order));
	}
	
	if(OEHelper.product_of_mrp_production_workcenter_line.size()!=0  && OEHelper.product_of_mrp_production_workcenter_line.size() > work_orders.position_for_work_order)
	{
		product.setText(""+OEHelper.product_of_mrp_production_workcenter_line.get(work_orders.position_for_work_order));
	}
	return rootView;
	
	}
	
	
	@Override
	public void onClick(View arg0) {
		
		
		OEHelper oehelper = new OEHelper(getActivity());
		OEValues val = new OEValues();
		DateFormat dateFormat = new SimpleDateFormat(
				"yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		String date1 = dateFormat.format(date);
	
		
		if(arg0.getId()==R.id.btnstart)
		{	
		
		if(btnstart.getText().toString().equals("Start"))
		{
			val.put("state","startworking");
			//val.put("phone",args.getString("phno"));
		
			val.put("date_start",date1);
			oehelper.mrp_production_workcenter_line_for_update_state(val, OEHelper.id_of_selected_work_order.get(work_orders.position_for_work_order));
			btnstart.setText("Finished");
			btnpendind.setVisibility(View.VISIBLE);
			OEHelper.datestart_of_mrp_production_workcenter_line.remove(work_orders.position_for_work_order);
			OEHelper.datestart_of_mrp_production_workcenter_line.add(work_orders.position_for_work_order,""+date1);
			startdateactual.setText(""+OEHelper.datestart_of_mrp_production_workcenter_line.get(work_orders.position_for_work_order));
		}
		else
		{
			if(btnstart.getText().toString().equals("Finished"))
			{
				float ans=0;
				
				if(OEHelper.datestart_of_mrp_production_workcenter_line.size()>work_orders.position_for_work_order)
				{
					
					 String start_date=	OEHelper.datestart_of_mrp_production_workcenter_line.get(work_orders.position_for_work_order);
					 OEHelper.date_end_work_order.remove(work_orders.position_for_work_order);
					 OEHelper.date_end_work_order.add(work_orders.position_for_work_order,""+date1);
					 enddateactual.setText(""+OEHelper.date_end_work_order.get(work_orders.position_for_work_order)); 
					 
					 //===========
				
				     SimpleDateFormat dateFormat1 = new SimpleDateFormat(
				            "yyyy-MM-dd HH:mm:ss");

				     try {

				        Date oldDate = dateFormat1.parse(start_date);
				        System.out.println(oldDate);

				        Date currentDate = new Date();

				        long diff = currentDate.getTime() - oldDate.getTime();
				        long seconds = diff / 1000;
				        long minutes = seconds / 60;
				        long hours = minutes / 60;
				        long days = hours / 24;
				        ans=0;
				        ans=seconds/3600;
				        if(oldDate.before(currentDate)) {

				            Log.e("oldDate", "is previous date");
				            Log.e("Difference: ", " seconds: " + seconds + " minutes: " + minutes
				            		+ " hours: " + hours + " days: " + days+"ans : "+ans);
				        }
				     }catch (ParseException e) {

				        e.printStackTrace();
				    }
				    
				  //1 minute = 60 seconds
				  //1 hour = 60 x 60 = 3600
				  //1 day = 3600 x 24 = 86400
				  //==========
				 
				  }
					val.put("state","done");
					val.put("date_finished",date1);
					val.put("delay",""+ans);
					oehelper.mrp_production_workcenter_line_for_update_state(val, OEHelper.id_of_selected_work_order.get(work_orders.position_for_work_order));
					
					btnpendind.setVisibility(View.INVISIBLE);
					btnstart.setVisibility(View.INVISIBLE);
					
//					OEHelper.mo_of_mrp_production_workcenter_line.remove(work_orders.position_for_work_order);
//					OEHelper.datestart_of_mrp_production_workcenter_line.remove(work_orders.position_for_work_order);
//					OEHelper.product_of_mrp_production_workcenter_line.remove(work_orders.position_for_work_order);
//					OEHelper.qty_order.remove(work_orders.position_for_work_order);
					
					}
		      }
		}
		if(arg0.getId()==R.id.pending)
		{
			
			if(btnpendind.getText().toString().equals("Pending"))
			{
				val.put("state","pause");
				oehelper.mrp_production_workcenter_line_for_update_state(val, OEHelper.id_of_selected_work_order.get(work_orders.position_for_work_order));
				btnpendind.setText("Resume");
				btnstart.setVisibility(View.INVISIBLE);
			}
			else
			{
				val.put("state","startworking");
				oehelper.mrp_production_workcenter_line_for_update_state(val, OEHelper.id_of_selected_work_order.get(work_orders.position_for_work_order));
				btnpendind.setText("Pending");
				btnstart.setVisibility(View.VISIBLE);
			}
		}
		if(arg0.getId()==R.id.View_Details)
		{
		//	Menufecturing_OrdersListAll.call_product_to_consume_onces=0;
			WO_to_MO_details wodetails_view = new WO_to_MO_details();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(wodetails_view);
		}
	}
}
