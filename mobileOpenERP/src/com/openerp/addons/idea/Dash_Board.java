package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.List;

import openerp.OpenERP;
import android.content.Context;
import android.graphics.Typeface;
import android.media.audiofx.AudioEffect.OnControlStatusChangeListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.base.login.Login;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OESQLiteHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class Dash_Board extends BaseFragment {

	MainActivity mainobject;
	// databaseHelper dbhelper=new databaseHelper(getActivity(), null, null, 1);
	OESQLiteHelper oesqlhelper;
	Context context = getActivity();
	OEHelper oe;
	GridView grid;
	public static boolean checkfirstcall=false;
	static boolean checkloading=false;

	
	int myProgress = 0;
	int progressStatus = 0;
	String[] web = { "Partner", "Product", "QR Equipment", "QR Code Scanner",
			"MF Order","Project Management" };
	int[] imageId = {

	R.drawable.partner, R.drawable.product,

	R.drawable.qr_equipment, R.drawable.camerascan,

	R.drawable.mf_order,R.drawable.project_mgt
	};

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.gridviewfordashboard,
				container, false);

//		if (db().isEmptyTable()) {
//			IdeaDemoRecords rec = new IdeaDemoRecords(getActivity());
//			rec.createDemoRecords();
//		}
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
	//	web[0].getTsetTypeface(font,Typeface.BOLD);
		
		MainActivity.check_dashboard_call_or_other=1;

		final Handler myHandler = new Handler();
		Runnable runnable;
		Thread tread;

		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (progressStatus == 0) {
					progressStatus = performTask();

				}
				/* Hides the Progress bar */
				myHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						progressStatus = 0;

					}
				});

			}

			private int performTask() {
				oe = new OEHelper(getActivity());
				return ++myProgress;
			}
		};
		myProgress = 0;

		tread = new Thread(runnable);
		// new Thread(runnable).start();
		tread.start();
		// Toast.makeText(getActivity(), "dash board", 7).show();
		// ========================
		MainActivity.global = 1;

		getActivity().setTitle(R.string.label_dashboard);

		CustomGrid adapter = new CustomGrid(getActivity(), web, imageId);
		grid = (GridView) rootView.findViewById(R.id.grid);
		grid.setAdapter(adapter);
		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				if (web[+position] == "Partner") {
					if (OEHelper.check_connection_exit.equals("1")) {
						view.setSelected(true);
						MainActivity.check_dashboard_call_or_other=2;
						partners partner = new partners();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(partner);
					} else {
						Toast.makeText(getActivity(), "Server Connection Fail",
								7).show();
					}

				} else if (web[+position] == "Product") {
					if (OEHelper.check_connection_exit.equals("1")) {
						// Toast.makeText(getActivity(), "yes", 7).show();
						MainActivity.check_dashboard_call_or_other=2;
						product.checkforfragment = 0;
						view.setSelected(true);
						product product1 = new product();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(product1);
					} else {
						Toast.makeText(getActivity(), "Server Connection Fail",
								7).show();
					}

				} else if (web[+position] == "QR Equipment") {
					if (OEHelper.check_connection_exit.equals("1")) {
						MainActivity.check_dashboard_call_or_other=2;
						product.checkforfragment = 0;
						view.setSelected(true);
						QR_Equipment product1 = new QR_Equipment();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(product1);
					} else {
						Toast.makeText(getActivity(), "Server Connection Fail",
								7).show();
					}
					
				} else if (web[+position] == "QR Code Scanner") {
					if (OEHelper.check_connection_exit.equals("1")) {
						product.checkforfragment = 0;
						checkfirstcall=false;
						checkloading=false;
						view.setSelected(true);
					//	testing camera = new testing();
						MainActivity.check_dashboard_call_or_other=2;
						cameraQRScanMain camera = new cameraQRScanMain();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(camera);
					} else {
						Toast.makeText(getActivity(), "Server Connection Fail",
								7).show();
					}

				} else if (web[+position] == "MF Order") {
					if (OEHelper.check_connection_exit.equals("1")) {
						product.checkforfragment = 0;
						view.setSelected(true);
						MainActivity.check_dashboard_call_or_other=2;
						Menufecturing_OrdersListAll mforder = new Menufecturing_OrdersListAll();
						FragmentListener frag = (FragmentListener) getActivity();
						frag.startDetailFragment(mforder);
					} else {
						Toast.makeText(getActivity(), "Server Connection Fail",
								7).show();
					}
				}
					 else if (web[+position] == "Project Management") {
							if (OEHelper.check_connection_exit.equals("1")) {
								product.checkforfragment = 0;
								view.setSelected(true);
								MainActivity.check_dashboard_call_or_other=2;
								
								project_name_dash_board i=new project_name_dash_board();
								//project_name i=new project_name();
								FragmentListener frag = (FragmentListener) getActivity();
								frag.startDetailFragment(i);
								
							} else {
								Toast.makeText(getActivity(), "Server Connection Fail",
										7).show();
							}
					
				}
			}
		});

		return rootView;
	}

	public Object databaseHelper(Context context) {

		return new IdeaDBHelper(context);
	}

	public List<DrawerItem> drawerMenus(Context context) {

		List<DrawerItem> menu = new ArrayList<DrawerItem>();
		menu.add(new DrawerItem("idea_home", "", true));
		Dash_Board Dash_Board1 = new Dash_Board();
		Bundle args = new Bundle();
		args.putString("key", "Dash Board");
		Dash_Board1.setArguments(args);
		menu.add(new DrawerItem("idea_home", "Dash Board", 0, 0, Dash_Board1));

		return menu;

	}
	
}
