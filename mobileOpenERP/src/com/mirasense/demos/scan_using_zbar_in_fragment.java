package com.mirasense.demos;

import java.util.List;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Handler;

import com.openerp.support.BaseFragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.R;
import com.barcode.CameraPreview;
import com.mirasense.scanditsdk.ScanditSDKAutoAdjustingBarcodePicker;
import com.mirasense.scanditsdk.interfaces.ScanditSDK;
import com.mirasense.scanditsdk.interfaces.ScanditSDKListener;
import com.openerp.MainActivity;
import com.openerp.addons.idea.Dash_Board;
import com.openerp.addons.idea.Product_Detail;
import com.openerp.addons.idea.QR_Equipment;
import com.openerp.addons.idea.partners;
import com.openerp.addons.idea.product;
import com.openerp.addons.idea.work_orders;
import com.openerp.base.account.AccountFragment;
import com.openerp.orm.OEHelper;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

/**
 * Simple demo application illustrating the use of the Scandit SDK.
 * 
 * Important information for the developer with respect to Android 2.1 support!
 * 
 * Android 2.1 differs from subsequent versions of Android OS in that it 
 * does not offer a camera preview mode in portrait mode (landscape only). 
 * Android 2.2+ offers both - a camera preview in landscape mode and in portrait 
 * mode. There are certain devices that run Android 2.2+ but do not properly
 * implement the methods needed for a portrait camera view. 
 * 
 * To address this difference between the Android versions, the Scandit SDK 
 * offers the following approaches and the developer needs to choose his 
 * preferred option:
 * 
 * If you are showing the scanner on the full screen in a new Activity:
 * 
 * - Instantiate the ScanditSDKAutoAdjustingBarcodePicker which will choose 
 * whether to use the new or legacy picker.
 * 
 * If you want to show the picker inside a view hierarchy/cropped/scaled you
 * have to make the distinction between the different pickers yourself. Fore 
 * devices that don't support the new picker the following options exist:
 * 
 * - a scan view in landscape mode scanning only(!) that is fully 
 * customizable by the developer - ScanditSDKBarcodePicker.class
 * 
 * - our own custom scan view with portrait mode scanning that offers only 
 * limited customization options (show/hide title & tool bars, 
 * but no additional Android UI elements) -  LegacyPortraitScanditSDKBarcodePicker.class
 * 
 * For devices that do support the new picker the following options exist:
 * 
 * - a scan view with portrait mode scanning that is fully customizable 
 * by the developer (RECOMMENDED) - ScanditSDKBarcodePicker.class
 * 
 * - any of the options listed under Android 2.1
 * 
 * We recommend that developers choose the scan view in portrait mode on Android 2.2.
 * It has the native Android look&feel and provides full customization. We provide our
 * own custom scan view (LegacyPortraitScanditSDKBarcodePicker.class) in Android 2.1
 * to provide backwards compatibility with Android 2.1. 
 *
 * To integrate the Scandit SDK, carry out the following three steps:
 * 
 * 1. Create a BarcodePicker object that manages camera access and 
 *    bar code scanning:
 *    
 *    e.g.
 *    ScanditSDKBarcodePicker barcodePicker = new ScanditSDKBarcodePicker(this, 
 *              R.raw.class, "your app key", true, 
                ScanditSDKBarcodePicker.LOCATION_PROVIDED_BY_SCANDIT_SDK);
 *
 *  IMPORTANT: Make sure add your app key here. It is available from your Scandit SDK account. 
 *
 * 2. Add it to the activity:    
 *    my_activity.setContentView(barcodePicker);
 * 
 * 3. Implement the ScanditSDKListener interface (didCancel, didScanBarcode, 
 *    didManualSearch) and register with the ScanditSDKOverlayView to receive 
 *    callbacks:
 *    barcodePicker.getOverlayView().addListener(this);
 * 
 * 
 * If you want to use the custom scan view for scanning in portrait mode in 
 * Android 2.1, instantiate the LegacyPortraitScanditSDKBarcodePicker
 * class (as shown in the example below). There is utility method available 
 * to determine whenever the default portrait scan view is not available
 * ScanditSDKBarcodePicker.canRunPortraitPicker().
 * 
 * 
 * 
 * Copyright 2010 Mirasense AG
 */

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied
 * See the License for the specific language governing premissions and
 * limitations under the License.
 */
public class scan_using_zbar_in_fragment extends BaseFragment {

	 Camera mCamera;
	    private CameraPreview mPreview;
	    private Handler autoFocusHandler;

	    TextView scanText;
	    Button scanButton;

	    ImageScanner scanner;

	    private boolean barcodeScanned = false;
	    private boolean previewing = true;

	    static {
	        System.loadLibrary("iconv");
	    } 

   
	    public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			setHasOptionsMenu(true);
			
			
			
		
		View rootView = inflater.inflate(R.layout.main, container,
				false);
		
		
		MainActivity.global=2;
	//	Toast.makeText(getActivity(), "call codebar", 30).show();
		
		
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

	        autoFocusHandler = new Handler();
	        mCamera = getCameraInstance();

	        /* Instance barcode scanner */
	        scanner = new ImageScanner();
	        scanner.setConfig(0, Config.X_DENSITY, 3);
	        scanner.setConfig(0, Config.Y_DENSITY, 3);

	        mPreview = new CameraPreview(getActivity(), mCamera, previewCb, autoFocusCB);
	        FrameLayout preview = (FrameLayout)rootView.findViewById(R.id.cameraPreview);
	        preview.addView(mPreview);

	        scanText = (TextView)rootView.findViewById(R.id.scanText);

	        scanButton = (Button)rootView.findViewById(R.id.ScanButton);

	        scanButton.setOnClickListener(new OnClickListener() {
	                public void onClick(View v) {
	                    if (barcodeScanned) {
	                        barcodeScanned = false;
	                        scanText.setText("Scanning...");
	                        mCamera.setPreviewCallback(previewCb);
	                        mCamera.startPreview();
	                        previewing = true;
	                        mCamera.autoFocus(autoFocusCB);
	                    }
	                }
	            });
	    
	  
	
		return rootView;

		}


	    public void onPause() {
	        super.onPause();
	    		Toast.makeText(getActivity(), "stop ", 30).show();
	        releaseCamera();
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
	
public static Camera getCameraInstance(){
    Camera c = null;
    try {
        c = Camera.open();
    } catch (Exception e){
    }
    return c;
}

private void releaseCamera() {
    if (mCamera != null) {
        previewing = false;
        mCamera.setPreviewCallback(null);
        mCamera.release();
        mCamera = null;
    }
}

private Runnable doAutoFocus = new Runnable() {
        public void run() {
            if (previewing)
                mCamera.autoFocus(autoFocusCB);
        }
    };

PreviewCallback previewCb = new PreviewCallback() {
        public void onPreviewFrame(byte[] data, Camera camera) {
            Camera.Parameters parameters = camera.getParameters();
            Size size = parameters.getPreviewSize();

            Image barcode = new Image(size.width, size.height, "Y800");
            barcode.setData(data);

            int result = scanner.scanImage(barcode);
            
            if (result != 0) {
                previewing = false;
                mCamera.setPreviewCallback(null);
                mCamera.stopPreview();
                
                SymbolSet syms = scanner.getResults();
                for (Symbol sym : syms) {
                    scanText.setText("barcode result " + sym.getData());
                    if(OEHelper.qr_equip_asset_qr_code.size()!=0)
            		{
            			if(OEHelper.qr_equip_asset_qr_code.size()>QR_Equipment.positioncurrentequipmen)
            			{
            				if(OEHelper.qr_equip_asset_qr_code.get(QR_Equipment.positioncurrentequipmen).equals(sym.getData()))
            				{
            					work_orders wo=new work_orders();
                    			FragmentListener frag = (FragmentListener) getActivity();
                    			frag.startDetailFragment(wo);
            				}
            				else
            				{
            					Toast.makeText(getActivity(), "QR Code Not Matched ", 30).show();
            				}
            					
            			}
            		}
                    else
                    {
                    	Toast.makeText(getActivity(), "No Any Record", 30).show();
                    }
                  //  OEHelper.qr_equip_asset_qr_code.get(QR_Equipment.positioncurrentequipmen);
                	
                    barcodeScanned = true;
                }
            }
        }
    };

// Mimic continuous auto-focusing
AutoFocusCallback autoFocusCB = new AutoFocusCallback() {
        public void onAutoFocus(boolean success, Camera camera) {
            autoFocusHandler.postDelayed(doAutoFocus, 1000);
       }
    };
	
}
