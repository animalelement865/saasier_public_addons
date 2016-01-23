package com.openerp.addons.idea;

import java.util.List;

import android.app.FragmentManager;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class testing2 extends BaseFragment {

	int indexofbarcodeproduct = 0;
	String cleanedBarcode = "";
	
	Button ok;

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
		View rootView = inflater.inflate(R.layout.testing,
				container, false);
		ok=(Button) rootView.findViewById(R.id.ok);
		ok.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				cleanedBarcode = "";
				
				cleanedBarcode="MO/00003";
				//cleanedBarcode="MO/00230";
				
				if (cleanedBarcode.contains("in") == true
						|| cleanedBarcode.contains("IN") == true) {

					
					OEHelper.s_no="";
					OEHelper.s_no=cleanedBarcode;
					OEHelper oehelper1 = new OEHelper(
							getActivity());
					oehelper1.production_lot_for_insert_in_move_to_consume();


					
				} else if (cleanedBarcode.contains("loc") == true
						|| cleanedBarcode.contains("LOC") == true) {
		//======================================================================			
//					Toast.makeText(getActivity(), "LOCATION SELECTED ",
//							Toast.LENGTH_LONG).show();
					Product_Detail.check_inventory_back_or_not=0;
					OEHelper.selected_stock_location_id="";
					String subbarcode=cleanedBarcode.substring(4);
					OEHelper.selected_stock_location_id=subbarcode;
			
					productlist_of_selecetd_location pro_list_locationwise =new productlist_of_selecetd_location();
					FragmentListener frag = (FragmentListener) getActivity();
					frag.startDetailFragment(pro_list_locationwise);
					//---------------------------
//					if(OEHelper.name_of_stock_location.size()!=0)
//					{
//					stock_location_main st_loc=new stock_location_main();
//					//detail.setArguments(args);
//					FragmentListener frag = (FragmentListener) getActivity();
//					frag.startDetailFragment(st_loc);
//					}
//					else
//					{
//						Toast.makeText(getActivity(), "NO ANY RECORD MATCH", 30).show();
//					}
		//==========================================================================
//						Toast.makeText(getActivity(), "DESTINATION LOCATION SELECTED ",
//								Toast.LENGTH_LONG).show();				
//						move_stock_by_location2.selecteddestid="";
//						String subbarcode=cleanedBarcode.substring(4);
//						move_stock_by_location2.selecteddestid=subbarcode;
//						move_stock_by_location2.checkQRSCAN_OR_manually=2;
//						move_stock_by_location2 movestock=new move_stock_by_location2();
//						FragmentListener frag = (FragmentListener) getActivity();
//						frag.startDetailFragment(movestock);
					//*****************************************************
					
				} else if (cleanedBarcode.contains("equip") == true
						|| cleanedBarcode.contains("EQUIP") == true) {
				//	Toast.makeText(getActivity(), "QR EQUIP SELECTED", 30).show();
					indexofbarcodeproduct = 0;

					OEHelper oeh = new OEHelper(getActivity());
					oeh.qr_equipmentname();
					oeh.qr_equipment_detail();
					
					if (OEHelper.qr_equip_asset_qr_code.contains(cleanedBarcode) == true) {

						indexofbarcodeproduct = OEHelper.qr_equip_asset_qr_code
								.indexOf(cleanedBarcode);
						if (OEHelper.qr_equip_name.size() != 0
								&& OEHelper.qr_equip_name.size() > indexofbarcodeproduct) {
							QR_Equipment.currentname = null;
							QR_Equipment.currentname = OEHelper.qr_equip_name
									.get(indexofbarcodeproduct);
						}
						
						QR_Equipment.positioncurrentequipmen = indexofbarcodeproduct;
						
						OEHelper.selected_Assets_id="";
						if(OEHelper.qr_equip_asset_id.size()>QR_Equipment.positioncurrentequipmen)
						{
							OEHelper.selected_Assets_id=OEHelper.qr_equip_asset_id.get(QR_Equipment.positioncurrentequipmen);
						}
						
						QR_equip_detail detail = new QR_equip_detail();
						
						Bundle args = new Bundle();
						detail.setArguments(args);
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(detail);
						
					} else {

					}

				} 
				 else if (cleanedBarcode.contains("out") == true
							|| cleanedBarcode.contains("OUT") == true) {
					 
						//Toast.makeText(getActivity(), "OUT selected", 30).show();
						
					    OEHelper.out_id_selected="";
						OEHelper.out_id_selected=cleanedBarcode;
						
						
						out_delivery_item_list out = new out_delivery_item_list();
						
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(out);
						
						
					} 
				 else if (cleanedBarcode.contains("MO") == true
							|| cleanedBarcode.contains("mo") == true) {
					 
						//Toast.makeText(getActivity(), "OUT selected", 30).show();
					 Dash_Board.checkfirstcall=true;
					    OEHelper.selected_mo_id_from_scanqr="";
						OEHelper.selected_mo_id_from_scanqr=cleanedBarcode;
						
						Log.d("under test2","ok...");
						OEHelper oehelper =new OEHelper(getActivity());
						oehelper.getserialno_from_moname();
						
						Log.d("enter in test2","ok..."+OEHelper.selected_mo_id_from_scanqr);
					//	OEHelper oehelper =new OEHelper(getActivity());
					//	oehelper.getserialno_from_moname();
						//cleanedBarcode="MO/01000";
						
						
						out_delivery_item_list out = new out_delivery_item_list();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(out);
						
						
					}else {
					Toast.makeText(getActivity(), "QR Code Not Matched  ",
							Toast.LENGTH_LONG).show();
				}
				
			}
		});
		//================================================================
		

		// =======================================================
		// Toast.makeText(getActivity(), "check="+sbu, 6).show();
		return rootView;
	}
}
