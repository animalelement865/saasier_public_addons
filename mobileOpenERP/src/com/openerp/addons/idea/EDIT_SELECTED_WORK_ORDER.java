//package com.openerp.addons.idea;
//
//import java.util.List;
//
//import android.app.FragmentManager;
//import android.content.Context;
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import com.openerp.R;
//import com.openerp.orm.OEHelper;
//import com.openerp.orm.OEValues;
//import com.openerp.support.BaseFragment;
//import com.openerp.support.fragment.FragmentListener;
//import com.openerp.util.drawer.DrawerItem;
//
//public class EDIT_SELECTED_WORK_ORDER extends BaseFragment implements
//		OnClickListener {
//
//	OEHelper oehelper;
//	EditText etforhour, etforqty;
//	Button btnforupdateorder, btnforcancle;
//
//	@Override
//	public Object databaseHelper(Context context) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//
//	@Override
//	public List<DrawerItem> drawerMenus(Context context) {
//		// TODO Auto-generated method stub
//		return null;
//
//	}
//
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		setHasOptionsMenu(true);
//
//		View rootView = inflater.inflate(R.layout.edit_work_order, container,
//				false);
//		btnforcancle = (Button) rootView.findViewById(R.id.cancle_work_order);
//		btnforupdateorder = (Button) rootView
//				.findViewById(R.id.update_work_order);
//		etforhour = (EditText) rootView.findViewById(R.id.hour_result);
//		etforqty = (EditText) rootView.findViewById(R.id.qty_result);
//		btnforcancle.setOnClickListener(this);
//		btnforupdateorder.setOnClickListener(this);
//
//		if (OEHelper.qty_order.size() != 0
//				&& OEHelper.qty_order.size() > work_orders.position_for_work_order) {
//			etforqty.setText(OEHelper.qty_order
//					.get(work_orders.position_for_work_order));
//		}
//		if (OEHelper.hours_order.size() != 0
//				&& OEHelper.hours_order.size() > work_orders.position_for_work_order) {
//			etforhour.setText(OEHelper.hours_order
//					.get(work_orders.position_for_work_order));
//		}
//
//		return rootView;
//	}
//
//	@Override
//	public void onClick(View arg0) {
//		// TODO Auto-generated method stub
//		if (arg0.getId() == R.id.update_work_order) {
//
//			OEValues val = new OEValues();
//			
//			if (!etforqty.getText().toString().equals("")) {
//				val.put("qty", etforqty.getText().toString());
////				Toast.makeText(getActivity(),
////						"edit qty=" + etforqty.getText().toString(), 40).show();
//			} else {
//				if (OEHelper.qty_order.size() != 0
//						&& OEHelper.qty_order.size() > work_orders.position_for_work_order) {
//					val.put("qty", OEHelper.qty_order
//							.get(work_orders.position_for_work_order));
////					Toast.makeText(
////							getActivity(),
////							"edit qty_order2="
////									+ OEHelper.qty_order
////											.get(work_orders.position_for_work_order),
////							40).show();
//				}
//
//			}
//
//			oehelper = new OEHelper(getActivity());
//
//			if (OEHelper.id_of_selected_work_order.size() != 0
//					&& OEHelper.id_of_selected_work_order.size() > work_orders.position_for_work_order) {
//				oehelper.editWorkOrder_from_mrp_production_workcenter_line(val,
//						OEHelper.id_of_selected_work_order
//								.get(work_orders.position_for_work_order));
//			//	oehelper.mrp_workcenter();
//				oehelper.mrp_production_workcenter_line();
//			}
//
//			work_orders work_order = new work_orders();
//
//			android.support.v4.app.FragmentManager fm1 = getActivity()
//					.getSupportFragmentManager();
//
//			fm1.popBackStack("work_orders",
//					FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			fm1.popBackStack();
//
//			android.support.v4.app.FragmentManager fm2 = getActivity()
//					.getSupportFragmentManager();
//
//			fm2.popBackStack("work_orders",
//					FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			fm2.popBackStack();
//
//			FragmentListener mFragment = (FragmentListener) getActivity();
//			 Toast.makeText(getActivity(),
//						 "Update Successfully",60).show();
//		//	mFragment.startMainFragment(work_order, false);
//			mFragment.startDetailFragment(work_order);
//
//		}
//		if (arg0.getId() == R.id.cancle_work_order) {
//			work_orders work_order = new work_orders();
//
//			android.support.v4.app.FragmentManager fm1 = getActivity()
//					.getSupportFragmentManager();
//
//			fm1.popBackStack("work_orders",
//					FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			fm1.popBackStack();
//
//			android.support.v4.app.FragmentManager fm2 = getActivity()
//					.getSupportFragmentManager();
//
//			fm2.popBackStack("work_orders",
//					FragmentManager.POP_BACK_STACK_INCLUSIVE);
//			fm2.popBackStack();
//
//			FragmentListener mFragment = (FragmentListener) getActivity();
//			//mFragment.startMainFragment(work_order, false);
//			mFragment.startDetailFragment(work_order);
//
//		}
//	}
//
//}
