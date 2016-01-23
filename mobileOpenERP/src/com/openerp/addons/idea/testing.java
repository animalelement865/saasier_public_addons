package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
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

public class testing extends BaseFragment {

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
			public void onClick(View v) {
				
				cleanedBarcode = "";
				//	cleanedBarcode = "IN/00097_08312014";
				//	cleanedBarcode="IN/00083_0000198";
				//	cleanedBarcode = "EQUIP/99999";
				//	cleanedBarcode = "EQUIP/1";
				//	cleanedBarcode="LOC/12";
					//cleanedBarcode="OUT/00237";//01000/
				cleanedBarcode="OUT/00002";
				//	cleanedBarcode="MO/01000";
					
					if (cleanedBarcode.contains("in") == true
							|| cleanedBarcode.contains("IN") == true) {
//						Toast.makeText(getActivity(), "PRODUCT SELECTED ",
//								Toast.LENGTH_LONG).show();

//						OEHelper.check_for_product_from_where=1;
//						// sbu = "";
//						// sbu = "IN/00092_0000210";
//						OEHelper.sbu = "";
//						OEHelper.sbu = cleanedBarcode;
//						OEHelper oeh = new OEHelper(getActivity());
//						
//						OEHelper.available_qty_of_product="";
//						oeh.stock_product_lot_getpro_id();
//					//	oeh.stock_production_lot();
//						oeh.product_name();
//						oeh.readproducttempalate();
			//
//						// =================================================
			//
//						if (OEHelper.productid_from_stock_production_lot != null) {
//							if (OEHelper.idofproduct_product
//									.contains(OEHelper.productid_from_stock_production_lot)) {
			//
//								Product_Detail detail = new Product_Detail();
			//
//								Bundle args = new Bundle();
			//
//								indexofbarcodeproduct = OEHelper.idofproduct_product
//										.indexOf(OEHelper.productid_from_stock_production_lot);
			//
//								if (OEHelper.idofproduct_product.size() != 0) {
//									String id_product_product = OEHelper.idofproduct_product
//											.get(indexofbarcodeproduct);
			//
//									OEHelper.getidfrom_product_product = null;
//									OEHelper.getidfrom_product_product = id_product_product;
			//
//									OEHelper.current_product_name = null;
//									OEHelper.current_product_name = OEHelper.datatemplate
//											.get(indexofbarcodeproduct);
			//
//								}
			//
//								if (OEHelper.list_price_of_product_template.size() != 0) {
//									String sale_price_of_product_template = OEHelper.list_price_of_product_template
//											.get(indexofbarcodeproduct);
//									args.putString("saleprice",
//											sale_price_of_product_template);
//								}
//								if (OEHelper.standard_price_of_product_template.size() != 0) {
//									String cost_price_of_product_template = OEHelper.standard_price_of_product_template
//											.get(indexofbarcodeproduct);
//									args.putString("costprice",
//											cost_price_of_product_template);
//								}
//								if (OEHelper.ean13_of_product_product.size() != 0) {
//									String ean13_of_product_product1 = OEHelper.ean13_of_product_product
//											.get(indexofbarcodeproduct);
//									args.putString("ean13", ean13_of_product_product1);
//								}
//								if (OEHelper.default_code_of_product_product.size() != 0) {
//									String reference_of_product_product1 = OEHelper.default_code_of_product_product
//						 					.get(indexofbarcodeproduct);
//									args.putString("reference",
//											reference_of_product_product1);
//								}
//								if (OEHelper.type_of_product_template.size() != 0) {
//									String type_Of_product_template = OEHelper.type_of_product_template
//											.get(indexofbarcodeproduct);
//									args.putString("type", type_Of_product_template);
//								}
//								if (OEHelper.supply_method_product_template.size() != 0) {
//									String supply_method_product_template1 = OEHelper.supply_method_product_template
//											.get(indexofbarcodeproduct);
//									args.putString("supplymethod",
//											supply_method_product_template1);
//								}
//								if (OEHelper.procure_method_product_template.size() != 0) {
//									String procure_method_product_template1 = OEHelper.procure_method_product_template
//											.get(indexofbarcodeproduct);
//									args.putString("procuremethod",
//											procure_method_product_template1);
//								}
//								
//								// args.putParcelable("BundleIcon", image_of_produc1t);
//								if (OEHelper.datatemplate.size() != 0) {
//									args.putString("name", OEHelper.datatemplate
//											.get(indexofbarcodeproduct));
//								}
//								
//								if (OEHelper.uom_product_product.size() != 0) {
//									args.putString("uom", OEHelper.uom_product_product
//											.get(indexofbarcodeproduct));
//								}
//								// Toast.makeText(getActivity(), "EAN13_code matched",
//								// 90).show();
			//
//								detail.setArguments(args);
//								FragmentListener frag = (FragmentListener) getActivity();
//								frag.startDetailFragment(detail);
			//
//							} else {
//								Toast.makeText(getActivity(), "Not Matched  ",
//										Toast.LENGTH_LONG).show();
//							}
//						} else {
			//
//							Toast.makeText(getActivity(), "Not Matched Any Record ",
//									Toast.LENGTH_LONG).show();
//						}

//						if(cleanedBarcode.contains("_"));
//						{
//							int ind=cleanedBarcode.indexOf("_");
//							OEHelper.s_no="";
//							//OEHelper.s_no=cleanedBarcode.substring(ind+1);
//							//Toast.makeText(getActivity(), "s_no="+OEHelper.s_no, 8).show();
//						}
						
						OEHelper.s_no="";
						OEHelper.s_no=cleanedBarcode;
						OEHelper oehelper1 = new OEHelper(
								getActivity());
						oehelper1.production_lot_for_insert_in_move_to_consume();

//						OEValues val = new OEValues();
//						// Toast.makeText(getActivity(),
//						// ""+Integer.parseInt(OEHelper.mo_id_of_movestock.get(p)),6).show();
			//
			//
//						val.put("state","done");
//					//	val.put("prodlot_id",OEHelper.prodlot_id);
//						Toast.makeText(getActivity(), ""+OEHelper.prodlot_id, 8).show();
//						val.put("prodlot_id",""+249);
//						
//						// val.put("location_id",OEHelper.mo_location_from_movestock.get(p));
//						// 5728,2085
			//
//						oehelper1
//								.updaterecordconsumemove(
//										val,
//										Integer.parseInt(OEHelper.menufecturing_product_id
//												.get(product_to_consume_of_selected_product.p1)));
//						Toast.makeText(getActivity(), "Record Update", 50).show();
			//
			//
//						OEHelper.menufecturing_product
//								.remove(product_to_consume_of_selected_product.p1);
//						OEHelper.moqty
//								.remove(product_to_consume_of_selected_product.p1);
//						OEHelper.mo_location_from_movestock
//								.remove(product_to_consume_of_selected_product.p1);
//						OEHelper.menufecturing_product_id
//								.remove(product_to_consume_of_selected_product.p1);
//						OEHelper.mo_serial_from_movestock
//								.remove(product_to_consume_of_selected_product.p1);
//						OEHelper.mo_uom_from_movestock
//								.remove(product_to_consume_of_selected_product.p1);
						
					//	Menufecturing_OrdersListAll.call_product_to_consume_onces = 1;
						
						product_to_consume_of_selected_product p_c=new product_to_consume_of_selected_product();
						Inventory inventory=new Inventory();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(inventory);
						
					} else if (cleanedBarcode.contains("loc") == true
							|| cleanedBarcode.contains("LOC") == true) {
			//======================================================================			
//						Toast.makeText(getActivity(), "LOCATION SELECTED ",
//								Toast.LENGTH_LONG).show();
						Product_Detail.check_inventory_back_or_not=0;
						OEHelper.selected_stock_location_id="";
						String subbarcode=cleanedBarcode.substring(4);
						OEHelper.selected_stock_location_id=subbarcode;
				
						productlist_of_selecetd_location pro_list_locationwise =new productlist_of_selecetd_location();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(pro_list_locationwise);
						//---------------------------
//						if(OEHelper.name_of_stock_location.size()!=0)
//						{
//						stock_location_main st_loc=new stock_location_main();
//						//detail.setArguments(args);
//						FragmentListener frag = (FragmentListener) getActivity();
//						frag.startDetailFragment(st_loc);
//						}
//						else
//						{
//							Toast.makeText(getActivity(), "NO ANY RECORD MATCH", 30).show();
//						}
			//==========================================================================
//							Toast.makeText(getActivity(), "DESTINATION LOCATION SELECTED ",
//									Toast.LENGTH_LONG).show();				
//							move_stock_by_location2.selecteddestid="";
//							String subbarcode=cleanedBarcode.substring(4);
//							move_stock_by_location2.selecteddestid=subbarcode;
//							move_stock_by_location2.checkQRSCAN_OR_manually=2;
//							move_stock_by_location2 movestock=new move_stock_by_location2();
//							FragmentListener frag = (FragmentListener) getActivity();
//							frag.startDetailFragment(movestock);
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
							
							Dash_Board.checkloading=false;
							out_delivery_item_list out = new out_delivery_item_list();
							
							FragmentListener frag = (FragmentListener) getActivity();
							frag.startDetailFragment(out);
							
							
						} else {
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
