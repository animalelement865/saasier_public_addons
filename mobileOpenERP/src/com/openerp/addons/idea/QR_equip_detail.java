package com.openerp.addons.idea;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import youtube_play_support.OpenYouTubePlayerActivity;
import youtube_play_support.playvideo_from_asset;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.barcode.CameraTestActivity;
import com.barcode.ScanditSDKDemoSimpleforQR_Equip_detail;
import com.mirasense.demos.scan_using_zbar_in_fragment;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class QR_equip_detail extends BaseFragment implements OnClickListener {

	Button work_orders, viewSOP, perform_MT, MT_records;
	OEHelper oehelper;
	public static String id_asset_selected = null;
	public static String  selected_document_page_id=null;
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

		View rootView = inflater.inflate(R.layout.qr_equip_detail1, container,
				false);

		getActivity().setTitle(R.string.label_equipment_detail);
		
		MainActivity.global = 2;

		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		MT_records = (Button) rootView.findViewById(R.id.button2);
		perform_MT = (Button) rootView.findViewById(R.id.button4);
		viewSOP = (Button) rootView.findViewById(R.id.button1);
		work_orders = (Button) rootView.findViewById(R.id.button3);
		MT_records.setOnClickListener(this);
		work_orders.setOnClickListener(this);
		perform_MT.setOnClickListener(this);
		viewSOP.setOnClickListener(this);
		MT_records.setTypeface(font,Typeface.BOLD);
		work_orders.setTypeface(font,Typeface.BOLD);
		perform_MT.setTypeface(font,Typeface.BOLD);
		viewSOP.setTypeface(font,Typeface.BOLD);


		
	
		TextView productname1 = (TextView) rootView.findViewById(R.id.textView2);
		TextView productname2 = (TextView) rootView.findViewById(R.id.textView5);
		TextView productname3 = (TextView) rootView.findViewById(R.id.textView6);
		TextView productname4 = (TextView) rootView.findViewById(R.id.textView7);
		TextView productname5 = (TextView) rootView.findViewById(R.id.textView8);
		TextView productname6 = (TextView) rootView.findViewById(R.id.textView9);
		
		TextView name = (TextView) rootView.findViewById(R.id.enddate);
		
		productname1.setTypeface(font,Typeface.BOLD);
		productname2.setTypeface(font,Typeface.BOLD);
		productname3.setTypeface(font,Typeface.BOLD);
		productname4.setTypeface(font,Typeface.BOLD);
		productname5.setTypeface(font,Typeface.BOLD);
		productname6.setTypeface(font,Typeface.BOLD);
		
		
		
		name.setTypeface(font);
		name.setText(QR_Equipment.currentname + "");
		TextView model = (TextView) rootView
				.findViewById(R.id.startdate_result);
		model.setTypeface(font);
		if (OEHelper.sop_selected_id_from_assets.size() != 0 && OEHelper.sop_selected_id_from_assets.size()>QR_Equipment.positioncurrentequipmen) {
			selected_document_page_id=OEHelper.sop_selected_id_from_assets.get(QR_Equipment.positioncurrentequipmen);
		}
		
		if (OEHelper.qr_equip_asset_model.size() != 0) {
			model.setText(OEHelper.qr_equip_asset_model
					.get(QR_Equipment.positioncurrentequipmen));
		}
		TextView serial = (TextView) rootView.findViewById(R.id.start_date);
		serial.setTypeface(font);
		if (OEHelper.qr_equip_serial.size() != 0) {
			serial.setText(OEHelper.qr_equip_serial
					.get(QR_Equipment.positioncurrentequipmen));
		}
		TextView Asset_no = (TextView) rootView.findViewById(R.id.textView0);
		Asset_no.setTypeface(font);
		if (OEHelper.qr_equip_asset_no.size() != 0) {
			Asset_no.setText(OEHelper.qr_equip_asset_no
					.get(QR_Equipment.positioncurrentequipmen));
		}
		TextView QR_code = (TextView) rootView.findViewById(R.id.qr_code);
		QR_code.setTypeface(font);
		if (OEHelper.qr_equip_asset_qr_code.size() != 0) {
			QR_code.setText(OEHelper.qr_equip_asset_qr_code
					.get(QR_Equipment.positioncurrentequipmen));
		}
		TextView Criticality = (TextView) rootView.findViewById(R.id.textView1);
		Criticality.setTypeface(font);
		if (OEHelper.qr_equip_criticality.size() != 0) {
			Criticality.setText(OEHelper.qr_equip_criticality
					.get(QR_Equipment.positioncurrentequipmen));
		}

//		OEHelper oehelper1 = new OEHelper(getActivity());
//		oehelper1.qr_equipmentimage();

		ImageView image_of_product2 = (ImageView) rootView
				.findViewById(R.id.imageView1);
		// image_of_product2.setOnClickListener(this);
		
		if (OEHelper.image_of_QR.size() != 0) {
			if (OEHelper.image_of_QR.size() > QR_Equipment.positioncurrentequipmen) {
				image_of_product2.setImageBitmap(OEHelper.image_of_QR
						.get(QR_Equipment.positioncurrentequipmen));
			}
		}
		if (OEHelper.qr_equip_asset_id.size() != 0) {
			if (OEHelper.qr_equip_asset_id.size() > QR_Equipment.positioncurrentequipmen) {
				id_asset_selected = OEHelper.qr_equip_asset_id
						.get(QR_Equipment.positioncurrentequipmen);
			}
		}

		// Toast.makeText(getActivity(),"qr detail",6).show();
		return rootView;
	}

	@Override
	public void onClick(View arg0) {

		// oehelper.asset_asset_mrp_workcenter_rel();

		if (arg0.getId() == R.id.button3) {

		//	 ScanditSDKDemoSimpleforQR_Equip_detail scandit=new ScanditSDKDemoSimpleforQR_Equip_detail();
			work_orders scandit = new work_orders();
			FragmentListener frag = (FragmentListener) getActivity();

			frag.startDetailFragment(scandit);

		} else if (arg0.getId() == R.id.button1) {
			
			OEHelper oehelper1 = new OEHelper(getActivity());
			oehelper1.document_page_for_video_play();
			
			if(OEHelper.flag_from_document_page.equals("1"))
			{
			
			 try {
					
				 Intent lVideoIntent = new Intent(null, Uri.parse("ytv://"+OEHelper.url_from_document_page), getActivity(), OpenYouTubePlayerActivity.class);
				 lVideoIntent.putExtra("force_fullscreen",true); 
				 startActivity(lVideoIntent);

				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
			 	} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
		   		} 
			 
			}else
			{
				displaydocument_text doc=new displaydocument_text();
				FragmentListener frag = (FragmentListener) getActivity();
				
				frag.startDetailFragment(doc);
			}
		}
		else if (arg0.getId() == R.id.button2) {
			
			Toast.makeText(getActivity(), "MT Records", 40).show();
			
		} else {

			MRO_order_name_from_assets mro=new MRO_order_name_from_assets();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(mro);
 			
		}

	}

}
