package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEDataRow;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.Base64Helper;
import com.openerp.util.drawer.DrawerItem;

public class partners extends BaseFragment implements OnItemClickListener {
	
	List<String> mItems = new ArrayList<String>();
	ListView mListView = null;
	List<Object> mPartners = new ArrayList<Object>();
	OEHelper oehelper;
	static int position1=200;
	static boolean checkforeditornot=false;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.partner_list, container,
				false);
		
		getActivity().setTitle(R.string.label_partner);
		OEHelper oehelper=new OEHelper(getActivity());
		//oehelper.product_productread();
		oehelper.readdatafromserverdb();
	//	oehelper.readproducttempalate();
		//oehelper.readDataFromServerUser();
		oehelper.readDataFromServer();
		
		MainActivity.global=2;
		mPartners.addAll(db().select());
		if (db().isEmptyTable()) {
			IdeaDemoRecords rec = new IdeaDemoRecords(getActivity());
			rec.createDemoRecords();
		}
		OEHelper.getTable(db());
//		oehelper=new OEHelper(getActivity());
//		//Toast.makeText(getActivity(), "toast....", Toast.LENGTH_LONG).show();
//		oehelper.readDataFromServerUser();
//		oehelper.readDataFromServer();
		mItems.clear();
		for (OEDataRow row : db().select()) {
			
			mItems.add(row.getString("name"));
		}
		
		mListView = (ListView) rootView.findViewById(R.id.partner_listview);
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.fragment_idea_list_item,OEHelper.name) {
		
			public View getView(int position, View convertView, ViewGroup parent) {
				View mView = convertView;
				if (mView == null)
					mView = getActivity().getLayoutInflater().inflate(
							R.layout.fragment_idea_list_item, parent, false);
				TextView txv = (TextView) mView.findViewById(R.id.txvIdeaName);
				txv.setText(OEHelper.name.get(position));
				txv.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				txv.setTypeface(font,Typeface.BOLD);
			//	txv.setTextColor(Color.rgb(84, 84, 84));
				//txv.setTextAppearance(getActivity(), android.R.attr.textAppearance);
				return mView;
			}
		});
		mListView.setOnItemClickListener(this);
		
		return rootView;
	}
	
	public Object databaseHelper(Context context) {
		return new IdeaDBHelper(context);
	}
	
	public List<DrawerItem> drawerMenus(Context context) {
		List<DrawerItem> menu = new ArrayList<DrawerItem>();
	//	menu.add(new DrawerItem("customer_home", "Customer", true));
		partners  customer1= new partners();
		Bundle args = new Bundle();
		args.putString("key", "partners");
		customer1.setArguments(args);
		menu.add(new DrawerItem("customer_home", "Partners", 5, 0, customer1));
		return menu;
	}

	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.partner, menu);
			
	}
	 
	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId())
		{

		case R.id.Dash_Board:
		 
			Dash_Board detail =new Dash_Board();
			
			FragmentListener frag = (FragmentListener) getActivity();
			frag.startDetailFragment(detail);
			
			return true; 
			
			
		case R.id.Search_partner:	
			//	Toast.makeText(getActivity(), "search called", 40).show();
			//	AutoCompleteTextView tv=new AutoCompleteTextView(getActivity());
				
				 
				final Dialog dialog = new Dialog(getActivity());
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.partner_search_custom_dialog);
			//	dialog.setTitle("Partner Search");
				dialog.setOnCancelListener(new OnCancelListener() {
					
					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub
					//	Toast.makeText(getActivity(), "cancle call",40).show();
						dialog.dismiss();
						
					}
				});
	 
				// set the custom dialog components - text, image and button
				AutoCompleteTextView autotext = (AutoCompleteTextView) dialog.findViewById(R.id.autoCompleteTextView_product_search);
				TextView txv = (TextView) dialog.findViewById(R.id.textView1);
				final ArrayAdapter adapter = new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,OEHelper.name);
				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				txv.setTypeface(font,Typeface.BOLD);
				autotext.setTypeface(font,Typeface.BOLD);
				autotext.setAdapter(adapter);
				autotext.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						
						String name=adapter.getItem(arg2).toString();
						callmethod_for_position_productdetail(OEHelper.name.indexOf(name));
						dialog.dismiss();				
					}
				});
				dialog.show();
				return true;
				
		}
		return true;
	}


	
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		view.setSelected(true);
		 callmethod_for_position_productdetail(position);
	}
private void callmethod_for_position_productdetail(int position) {
		
		
	partnerdetails detail = new partnerdetails();
	checkforeditornot=false;
	Bundle args = new Bundle();
	position1=position;
	args.putString("name",OEHelper.name.get(position));
	args.putString("phno",OEHelper.phNo.get(position));
	args.putString("mobile",OEHelper.mobile.get(position));
	args.putString("fax",OEHelper.fax.get(position));
	args.putString("email",OEHelper.email.get(position));
	args.putString("website",OEHelper.website.get(position));
	args.putString("address",OEHelper.address.get(position));
	args.putString("id",OEHelper.id.get(position));
	
	args.putString("city",OEHelper.city.get(position));
	args.putString("zip",OEHelper.zip.get(position));
	args.putString("street1",OEHelper.street1.get(position));
	args.putString("street2",OEHelper.street2.get(position));
	
	
	detail.setArguments(args);
	
	FragmentListener frag = (FragmentListener) getActivity();
	android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();

	frag.startDetailFragment(detail);
		
	}

}
