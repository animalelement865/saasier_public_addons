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
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class QR_scan_for_insert_product_to_consume_selected_product extends BaseFragment implements
		ScanditSDKListener {

	private ScanditSDK mBarcodePicker;
	int indexofbarcodeproduct = 0;
	String EAN13_code = null;
	OEHelper oeh;
	

	// Enter your Scandit SDK App key here.
	// Your Scandit SDK App key is available via your Scandit SDK web account.
	public static final String sScanditSdkAppKey = "a9J2Fu/JEeOSwdn9NTRuk2N79KHBTzTQMAUChbxWC/4";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		getActivity().setTitle(R.string.label_qr_main_activity);
		MainActivity.global = 2;
		// Toast.makeText(getActivity(), "call codebar", 30).show();

		// initializeAndStartBarcodeScanning();

		// View rootView = inflater.inflate(R.layout.main, container,
		// false);

		// getActivity().requestWindowFeature(Window.FEATURE_NO_TITLE);
		getActivity().getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);

		// We instantiate the automatically adjusting barcode picker that will
		// choose the correct picker to instantiate. Be aware that this picker
		// should only be instantiated if the picker is shown full screen as the
		// legacy picker will rotate the orientation and not properly work in
		// non-fullscreen.
		ScanditSDKAutoAdjustingBarcodePicker picker = new ScanditSDKAutoAdjustingBarcodePicker(
				getActivity(), sScanditSdkAppKey,
				ScanditSDKAutoAdjustingBarcodePicker.CAMERA_FACING_BACK);

		// Add both views to activity, with the scan GUI on top.
		// setContentView(picker);
		// container.removeView(picker);
		// container.addView(picker);
		// setContentView(picker);
		mBarcodePicker = picker;
		
		// Register listener, in order to be notified about relevant events
		// (e.g. a successfully scanned bar code).
		mBarcodePicker.getOverlayView().addListener(this);
		
		// Show a search bar in the scan user interface.
		mBarcodePicker.getOverlayView().showSearchBar(true);
		
		return picker;

		// return rootView;
	}
	@Override
	public void onPause() {
		// When the activity is in the background immediately stop the
		// scanning to save resources and free the camera.
		mBarcodePicker.stopScanning();
		super.onPause();
	}

	@Override
	public void onResume() {
		// Once the activity is in the foreground again, restart scanning.
		mBarcodePicker.startScanning();
		super.onResume();
	}

	/**
	 * Initializes and starts the bar code scanning.
	 */
	// public void initializeAndStartBarcodeScanning() {
	// // Switch to full screen.
	//
	// }

	/**
	 * Called when a barcode has been decoded successfully.
	 * 
	 * @param barcode
	 *            Scanned barcode content.
	 * @param symbology
	 *            Scanned barcode symbology.
	 */
	public void didScanBarcode(String barcode, String symbology) {
		// Remove non-relevant characters that might be displayed as rectangles
		// on some devices. Be aware that you normally do not need to do this.
		// Only special GS1 code formats contain such characters.
		// oeh = new OEHelper(getActivity());
		// oeh.stock_move_for_pickingid();
		String cleanedBarcode = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		// ===============================================================================

//		Toast.makeText(getActivity(),  symbology+": "+cleanedBarcode,
//				Toast.LENGTH_LONG).show();

		if (cleanedBarcode.contains("in") == true
				|| cleanedBarcode.contains("IN") == true) {
			
			OEHelper.s_no="";
			OEHelper.s_no=cleanedBarcode;
			OEHelper oehelper1 = new OEHelper(
					getActivity());
			oehelper1.production_lot_for_insert_in_move_to_consume();

			OEValues val = new OEValues();
			// Toast.makeText(getActivity(),
			// ""+Integer.parseInt(OEHelper.mo_id_of_movestock.get(p)),6).show();


			val.put("state","done");
			val.put("prodlot_id",OEHelper.prodlot_id);
			
			// val.put("location_id",OEHelper.mo_location_from_movestock.get(p));
			// 5728,2085

			oehelper1
					.updaterecordconsumemove(
							val,
							Integer.parseInt(OEHelper.menufecturing_product_id
									.get(product_to_consume_of_selected_product.p1)));
			Toast.makeText(getActivity(), "Record Update", 50).show();

		//	dialog.dismiss();

			OEHelper.menufecturing_product
					.remove(product_to_consume_of_selected_product.p1);
			OEHelper.moqty
					.remove(product_to_consume_of_selected_product.p1);
			OEHelper.mo_location_from_movestock
					.remove(product_to_consume_of_selected_product.p1);
			OEHelper.menufecturing_product_id
					.remove(product_to_consume_of_selected_product.p1);
			OEHelper.mo_serial_from_movestock
					.remove(product_to_consume_of_selected_product.p1);
			OEHelper.mo_uom_from_movestock
					.remove(product_to_consume_of_selected_product.p1);
			
		//	Menufecturing_OrdersListAll.call_product_to_consume_onces = 1;
			
			product_to_consume_of_selected_product p_c=new product_to_consume_of_selected_product();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(p_c);
		
		}
			
	}

	/**
	 * Called when the user entered a bar code manually.
	 * 
	 * @param entry
	 *            The information entered by the user.
	 */
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
