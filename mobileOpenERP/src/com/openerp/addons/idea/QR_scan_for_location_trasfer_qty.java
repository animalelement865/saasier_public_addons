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

public class QR_scan_for_location_trasfer_qty extends BaseFragment implements ScanditSDKListener {

	 
	
	// The main object for recognizing a displaying barcodes.
	private ScanditSDK mBarcodePicker;
	int indexofbarcodeproduct = 0;
	String EAN13_code = null;
	public static String destination_id_selected=null;
	OEHelper oeh;

	// Enter your Scandit SDK App key here.
	// Your Scandit SDK App key is available via your Scandit SDK web account.
	public static final String sScanditSdkAppKey = "a9J2Fu/JEeOSwdn9NTRuk2N79KHBTzTQMAUChbxWC/4";

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		getActivity().setTitle(R.string.label_qr_sorce_location);
		MainActivity.global = 2;
		// Toast.makeText(getActivity(), "call codebar", 30).show();

		oeh = new OEHelper(getActivity());
		
		// initializeAndStartBarcodeScanning();
		// View rootView = inflater.inflate(R.layout.main, container, false);
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
	    //container.removeView(picker);
		//container.addView(picker);
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
//	public void initializeAndStartBarcodeScanning() {
//		// Switch to full screen.
//
//	}

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
		String cleanedBarcode = "";
		for (int i = 0; i < barcode.length(); i++) {
			if (barcode.charAt(i) > 30) {
				cleanedBarcode += barcode.charAt(i);
			}
		}

		
		// ===============================================================================
		// EAN13_code=cleanedBarcode;
		
	//	Toast.makeText(getActivity(), "QR Code = "+cleanedBarcode, 30).show();
		  //===============================================================================
		if (cleanedBarcode.contains("loc") == true
				|| cleanedBarcode.contains("LOC") == true) {
			
			Toast.makeText(getActivity(), "SOURCE LOCATION SELECTED ",
					Toast.LENGTH_LONG).show();
			
			move_stock_by_location.selectedsourceid="";
			String subbarcode=cleanedBarcode.substring(4);
			move_stock_by_location.selectedsourceid=subbarcode;
			
			move_stock_by_location.checkQRSCAN_OR_manually=1;
			
			move_stock_by_location movestock=new move_stock_by_location();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(movestock);
			
			
		}
		else
		{
			Toast.makeText(getActivity(), "SOURCE LOCATION NOT MATCHED ",
					Toast.LENGTH_LONG).show();
		}
		
		
//			
//			OEHelper oe = new OEHelper(getActivity());
//			//oe.stock_location();
////			oe.stockmoveforqtytransfer();
//			
//			OEValues val = new OEValues();
//			
//			//int id=Integer.parseInt(OEHelper.getidfrom_product_product);
//			
//		//for insert==================	
//			
//			 DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//			 Date date = new Date();
//			 String date1=	 dateFormat.format(date);
//			 
//			val.put("product_qty", move_stock_by_location.transferstock);
//			val.put("location_dest_id",OEHelper.desti_id1);//destination id   OEHelper.desti_id1
//			val.put("location_id",OEHelper.sourceid);//source id  OEHelper.sourceid
//			val.put("state","done");
//			val.put("product_id",OEHelper.getidfrom_product_product);
//			val.put("date",date1);
//			val.put("name",OEHelper.current_product_name);//name of product where transfer//OEHelper.current_product_name
//			val.put("company_id",OEHelper.companyid1);//Integer.parseInt(OEHelper.companyid1);
//			//val.put("weight_uom_id",1);//Integer.parseInt(OEHelper.weight_uom_id1)
//			val.put("date_expected",date1);
//			val.put("product_uom",OEHelper.productuom2);
//			
//
////			oe.insertstockqty(val);
//			 Toast.makeText(getActivity(),
//					 move_stock_by_location.transferstock+" kg qty transfer",50).show();
//	      
			
		// ================================================================================
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

	@Override
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