package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.List;




import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.R;
import com.openerp.MainActivity;
import com.openerp.orm.OEDataRow.IdName;
//import com.openerp.addons.idea.product.LoginUser;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OESQLiteHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.OEDialog;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class QR_Equipment extends BaseFragment implements OnItemClickListener {

	public static int  positioncurrentequipmen=0;
	static String currentname  =null;
	ListView mListView = null;	

	public static Bitmap image_of_produc1t;
	Button btn;
	OEHelper oehelper;
	TextView tvloading;
	ProgressBar bar;
	ProgressDialog predailog;
	
	
	
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
		
		
		
	
	View rootView = inflater.inflate(R.layout.qr_equipmentlist, container,
			false);
	
	getActivity().setTitle(R.string.label_equipment);
	
	MainActivity.global=2;
	
	mListView = (ListView) rootView.findViewById(R.id.listforqr_equip);
	
	 oehelper=new OEHelper(getActivity());	
	 

		List<String> k= oehelper.qr_equipmentname();

		if(OEHelper.qr_equip_name.size()!=0)
		{
	
			mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
			R.layout.qr_equip_item, OEHelper.qr_equip_name) {
	
//		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
//				R.layout.fragment_product_bind_item, OEHelper.product_pty_stock_move) {
				
		public View getView(int position, View convertView, ViewGroup parent) {
			View mView = convertView;
		
			if (mView == null)
				mView = getActivity().getLayoutInflater().inflate(
						R.layout.qr_equip_item, parent, false);
			TextView txv = (TextView) mView.findViewById(R.id.qr_name);
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
			txv.setTypeface(font);
			txv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
			
			txv.setTextColor(Color.rgb(84,84,84));
			
			if(OEHelper.qr_equip_name.size()!=0 && OEHelper.qr_equip_name.size()>position)
			{
			txv.setText(""+OEHelper.qr_equip_name.get(position));
			}
			//txv.setTextAppearance(getActivity(), android.R.attr.textAppearance);
			
			return mView;
		}

	});
		}
		else
		{
			Toast.makeText(getActivity(), "No Any Record", 30).show();
		}
	mListView.setOnItemClickListener(this);
	return rootView;

	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		
		OEHelper oehelper1=new OEHelper(getActivity());
		List<String> k= oehelper1.qr_equipment_detail();
		
		
		positioncurrentequipmen=0;
		positioncurrentequipmen=arg2;
		currentname=null;
		currentname=OEHelper.qr_equip_name.get(arg2);
		
		OEHelper.selected_Assets_id="";
		if(OEHelper.qr_equip_asset_id.size()>positioncurrentequipmen)
		{
			OEHelper.selected_Assets_id=OEHelper.qr_equip_asset_id.get(positioncurrentequipmen);
		}
		
		QR_equip_detail equipment =new QR_equip_detail();
	
	//equipment.setArguments(args);
	FragmentListener frag = (FragmentListener) getActivity();
	frag.startDetailFragment(equipment);
	//frag.startMainFragment(detail, false);
		
	}
}
