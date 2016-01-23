package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class cameraQRScanMain extends BaseFragment implements
		ScanditSDKListener {

	private ScanditSDK mBarcodePicker;
	int indexofbarcodeproduct = 0;
	String EAN13_code = null;
	OEHelper oeh;
	
	public static final String sScanditSdkAppKey = "a9J2Fu/JEeOSwdn9NTRuk2N79KHBTzTQMAUChbxWC/4";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		getActivity().setTitle(R.string.label_qr_main_activity);
		MainActivity.global = 2;
		
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
				getActivity(), sScanditSdkAppKey,
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

		mBarcodePicker = picker;
		
		mBarcodePicker.getOverlayView().addListener(this);
		
		mBarcodePicker.getOverlayView().showSearchBar(true);
		
		return picker;
	}
	@Override
	public void onPause() {
		
		mBarcodePicker.stopScanning();
		super.onPause();
	}

	@Override
	public void onResume() {
	
		mBarcodePicker.startScanning();
		super.onResume();
	}
	
	public void didScanBarcode(String barcode, String symbology) {
		
		String cleanedBarcode = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		if (cleanedBarcode.contains("in") == true
				|| cleanedBarcode.contains("IN") == true) {

			OEHelper.check_for_product_from_where=1;
			OEHelper.sbu = "";
			OEHelper.sbu = cleanedBarcode;
			OEHelper oeh = new OEHelper(getActivity());
			OEHelper.available_qty_of_product="";
			
			oeh.stock_product_lot_getpro_id();
			oeh.product_name();
			oeh.readproducttempalate();

			// =================================================

			if (OEHelper.productid_from_stock_production_lot != null) {
				if (OEHelper.idofproduct_product
						.contains(OEHelper.productid_from_stock_production_lot)) {

					Product_Detail detail = new Product_Detail();

					Bundle args = new Bundle();
					indexofbarcodeproduct = OEHelper.idofproduct_product
							.indexOf(OEHelper.productid_from_stock_production_lot);

					if (OEHelper.idofproduct_product.size() != 0) {
						String id_product_product = OEHelper.idofproduct_product
								.get(indexofbarcodeproduct);

						OEHelper.getidfrom_product_product = null;
						OEHelper.getidfrom_product_product = id_product_product;

						OEHelper.current_product_name = null;
						OEHelper.current_product_name = OEHelper.datatemplate
								.get(indexofbarcodeproduct);
					}
					if (OEHelper.list_price_of_product_template.size() != 0) {
						String sale_price_of_product_template = OEHelper.list_price_of_product_template
								.get(indexofbarcodeproduct);
						args.putString("saleprice",
								sale_price_of_product_template);
					}
					if (OEHelper.standard_price_of_product_template.size() != 0) {
						String cost_price_of_product_template = OEHelper.standard_price_of_product_template
								.get(indexofbarcodeproduct);
						args.putString("costprice",
								cost_price_of_product_template);
					}
					if (OEHelper.ean13_of_product_product.size() != 0) {
						String ean13_of_product_product1 = OEHelper.ean13_of_product_product
								.get(indexofbarcodeproduct);
						args.putString("ean13", ean13_of_product_product1);
					}
					if (OEHelper.default_code_of_product_product.size() != 0){
						String reference_of_product_product1 = OEHelper.default_code_of_product_product
								.get(indexofbarcodeproduct);
						args.putString("reference",
								reference_of_product_product1);
					}
					if (OEHelper.type_of_product_template.size() != 0) {
						String type_Of_product_template = OEHelper.type_of_product_template
								.get(indexofbarcodeproduct);
						args.putString("type", type_Of_product_template);
					}
					if (OEHelper.supply_method_product_template.size() != 0) {
						String supply_method_product_template1 = OEHelper.supply_method_product_template
								.get(indexofbarcodeproduct);
						args.putString("supplymethod",
								supply_method_product_template1);
					}
					if (OEHelper.procure_method_product_template.size() != 0) {
						String procure_method_product_template1 = OEHelper.procure_method_product_template
								.get(indexofbarcodeproduct);
						args.putString("procuremethod",
								procure_method_product_template1);
					}
					// args.putParcelable("BundleIcon", image_of_produc1t);
					if (OEHelper.datatemplate.size() != 0) {
						args.putString("name", OEHelper.datatemplate
								.get(indexofbarcodeproduct));
					}
					if (OEHelper.uom_product_product.size() != 0) {
						args.putString("uom", OEHelper.uom_product_product
								.get(indexofbarcodeproduct));
					}

					detail.setArguments(args);
					FragmentListener frag = (FragmentListener) getActivity();
					frag.startDetailFragment(detail);

				} else {
//					Toast.makeText(getActivity(), "Not Matched  ",
//							Toast.LENGTH_SHORT).show();
				}
			} else {

				Toast.makeText(getActivity(), "Not Matched  ",
						Toast.LENGTH_LONG).show();
			}

		} else if (cleanedBarcode.contains("loc") == true
				|| cleanedBarcode.contains("LOC") == true) {
			

			OEHelper.selected_stock_location_id="";
			String subbarcode=cleanedBarcode.substring(4);
			OEHelper.selected_stock_location_id=subbarcode;
			
			productlist_of_selecetd_location pro_list_locationwise =new productlist_of_selecetd_location();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(pro_list_locationwise);
			
//===================================================================	

		} else if (cleanedBarcode.contains("equip") == true
				|| cleanedBarcode.contains("EQUIP") == true) {
			//Toast.makeText(getActivity(), "QR EQUIP SELECTED", Toast.LENGTH_SHORT).show();
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
			
			
		}else {
			Toast.makeText(getActivity(), "QR Code Not Matched  ",
					Toast.LENGTH_LONG).show();
		}
		// ===============================================================================
		// EAN13_code=cleanedBarcode;
		// ================================================================================
	}

	public void didManualSearch(String entry) {
		// Toast.makeText(this, "User entered: " + entry,
		// Toast.LENGTH_LONG).show();
	}

	public void didCancel() {
		mBarcodePicker.stopScanning();
		// finish();
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
