package com.openerp.addons.idea;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class Product_Detail extends BaseFragment implements OnClickListener {

	Button Inventory, ViewMFO, stockByLocation, COA;
	Bitmap image_of_product1;
	static String selected_uom=null;
	static String selected_qty=null;
	Bundle icon;

	OEHelper oehelper;

	public static String productnameselected = null;
	TextView product_qty1;
	static int check_inventory_back_or_not=0;
	static int check_for_run_tread_or_not=0;

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mView = inflater.inflate(R.layout.product_detail_form1, container,
				false);
		
		getActivity().setTitle(R.string.label_product_detail);
		Bundle args = getArguments();
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView productname9 = (TextView) mView.findViewById(R.id.product_type);
		TextView productname1 = (TextView) mView.findViewById(R.id.reference);
		TextView productname2 = (TextView) mView.findViewById(R.id.ean13);
		TextView productname3 = (TextView) mView.findViewById(R.id.sale_price);
		TextView productname4 = (TextView) mView.findViewById(R.id.procurement_method);
		TextView productname5 = (TextView) mView.findViewById(R.id.supply_method);
		TextView productname6 = (TextView) mView.findViewById(R.id.cost_price);
		TextView productname7 = (TextView) mView.findViewById(R.id.uom);
		TextView productname8 = (TextView) mView.findViewById(R.id.totalquantity);
		TextView productname11 = (TextView) mView.findViewById(R.id.information);
		TextView productname0 = (TextView) mView.findViewById(R.id.procurment);
		TextView productname10 = (TextView) mView.findViewById(R.id.stock);
		
		productname1.setTypeface(font,Typeface.BOLD);
		productname2.setTypeface(font,Typeface.BOLD);
		productname3.setTypeface(font,Typeface.BOLD);
		productname4.setTypeface(font,Typeface.BOLD);
		productname5.setTypeface(font,Typeface.BOLD);
		productname6.setTypeface(font,Typeface.BOLD);
		productname7.setTypeface(font,Typeface.BOLD);
		productname8.setTypeface(font,Typeface.BOLD);
		productname9.setTypeface(font,Typeface.BOLD);
		productname0.setTypeface(font,Typeface.BOLD);
		productname10.setTypeface(font,Typeface.BOLD);
		productname11.setTypeface(font,Typeface.BOLD);
	
		TextView productname = (TextView) mView.findViewById(R.id.product_name);
		productname.setTypeface(font);
		if (args != null && args.containsKey("name")) {
			productname.setText(args.getString("name"));
			productnameselected = args.getString("name");
		}
		TextView saleprice = (TextView) mView
				.findViewById(R.id.sale_price_result);
		
		saleprice.setTypeface(font);
		if (args != null && args.containsKey("saleprice"))
			saleprice.setText(args.getString("saleprice"));
		TextView costprice = (TextView) mView
				.findViewById(R.id.cost_price_result);
		costprice.setTypeface(font);
		if (args != null && args.containsKey("costprice"))
			costprice.setText(args.getString("costprice"));
		TextView Ean13 = (TextView) mView.findViewById(R.id.ean13result);
		Ean13.setTypeface(font);
		if (args != null && args.containsKey("ean13"))
			Ean13.setText(args.getString("ean13"));
		TextView type1 = (TextView) mView
				.findViewById(R.id.product_type_result);
		type1.setTypeface(font);
		if (args != null && args.containsKey("type"))
			type1.setText(args.getString("type"));
		TextView reference1 = (TextView) mView
				.findViewById(R.id.reference_result);
		reference1.setTypeface(font);
		if (args != null && args.containsKey("reference"))
			reference1.setText(args.getString("reference"));
		TextView supplymethod1 = (TextView) mView
				.findViewById(R.id.supply_method_result);
		supplymethod1.setTypeface(font);
		if (args != null && args.containsKey("supplymethod"))
			supplymethod1.setText(args.getString("supplymethod"));
		TextView procuremethod1 = (TextView) mView
				.findViewById(R.id.procurement_result);
		procuremethod1.setTypeface(font);
		if (args != null && args.containsKey("procuremethod"))
			procuremethod1.setText(args.getString("procuremethod"));
		TextView uom = (TextView) mView.findViewById(R.id.oum_result);
		uom.setTypeface(font);
		if (args != null && args.containsKey("uom"))
			uom.setText(args.getString("uom"));
		selected_uom="";
		selected_qty="";
		
		if (args != null && args.containsKey("uom"))
		selected_uom=args.getString("uom");
		TextView product_qty1 = (TextView) mView
				.findViewById(R.id.total_quantity_result);
		product_qty1.setTypeface(font);
		
//		double x=123.45678;
//		DecimalFormat df = new DecimalFormat("#.##");
//		String dx=df.format(x);
//		x=Double.valueOf(dx);
		
		if(OEHelper.check_for_product_from_where==0)
		{
			if (args != null && args.containsKey("qty_available"))
				product_qty1.setText(args.getString("qty_available"));
				selected_qty=args.getString("qty_available");
		}
		else
		{
			product_qty1.setText((OEHelper.available_qty_of_product));
			selected_qty=OEHelper.available_qty_of_product;
		}
		
		MainActivity.global = 2;
		COA = (Button) mView.findViewById(R.id.COA);
		ViewMFO = (Button) mView.findViewById(R.id.MFO);
		Inventory = (Button) mView.findViewById(R.id.Inventory);
		
		
		COA.setOnClickListener(this);
		ViewMFO.setOnClickListener(this);
		Inventory.setOnClickListener(this);
		COA.setTypeface(font,Typeface.BOLD);
		ViewMFO.setTypeface(font,Typeface.BOLD);
		Inventory.setTypeface(font,Typeface.BOLD);
		return mView;
	}

	public Object databaseHelper(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	public void onClick(View arg0) {
		switch (arg0.getId()) {

		case R.id.COA:
			
			COAlist coalist = new COAlist();
			FragmentListener frag2 = (FragmentListener) getActivity();
			frag2.startDetailFragment(coalist);
			
			break;

		case R.id.Inventory:
			
				check_inventory_back_or_not=0;
				QR_scan_for_get_move_record qrmove=new QR_scan_for_get_move_record();
				//testing qrmove=new testing();
				//	Inventory inventory = new Inventory();
				FragmentListener frag1 = (FragmentListener) getActivity();
				frag1.startDetailFragment(qrmove);
		
			break;
		
		case R.id.MFO:
			
			check_for_run_tread_or_not=0;
     		menufecturing_orders_from_product_detail detail = new menufecturing_orders_from_product_detail();
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(detail);
			
			break;
		}
	}

	@Override
	public void onPause() {
		
		try {
		
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		super.onPause();
	}
}

