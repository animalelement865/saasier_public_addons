package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.base.login.SyncWizard;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class partnerdetails extends BaseFragment {
	ListView mListView = null;
	public  List<String> record = new ArrayList<String>();
	public  List<String> header = new ArrayList<String>();
	public static String id;
	public static  List<String> name = new ArrayList<String>();
	
	String phone1,mobile1,city1,_street1,_street2,zip1,fax1,email1,website1;
	public  static List<String> ids = new ArrayList<String>();
	OEHelper oehelper;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.partner_list_detail, container,
				false);
		
		getActivity().setTitle(R.string.label_partner_detail);
		Bundle args = getArguments();
		setHasOptionsMenu(true);
		//TextView txv = (TextView) rootView.findViewById(R.id.partner_detial_textView1);
		//TextView txv2 = (TextView) rootView.findViewById(R.id.partner_detial_textView2);
		//		if (args != null && args.containsKey("name"))
		//			txv.setText(args.getString("name"));
		//		if (args != null && args.containsKey("phno"))
		//			txv2.setText(args.getString("phno")+args.getString("mobile")+args.getString("fax")+args.getString("email")+
		//					args.getString("website")+args.getString("address"));

		MainActivity.global=2;
		oehelper=new OEHelper(getActivity());
		//	record.add(args.getString("name"));
		
		if(partners.checkforeditornot==false)
		{
		record.add(args.getString("phno"));
		record.add(args.getString("mobile"));
		record.add(args.getString("fax"));
		record.add(args.getString("email"));
		record.add(args.getString("website"));
		
		
		record.add(args.getString("address"));
		id=null;
		id=args.getString("id");
		}
		else
		{
			//record.add(MainActivity.name.get(partners.position1));
			record.add(OEHelper.phNo.get(partners.position1));
			record.add(OEHelper.mobile.get(partners.position1));
			record.add(OEHelper.fax.get(partners.position1));
			record.add(OEHelper.email.get(partners.position1));
			record.add(OEHelper.website.get(partners.position1));
			record.add(OEHelper.address.get(partners.position1));
			id=null;
			id=OEHelper.id.get(partners.position1);
		}
		header.add("Phone");
		header.add("Mobile");
		header.add("Fax");
		header.add("Email");
		header.add("Website");
		header.add("Address");

		
		if(partners.checkforeditornot==false)
		{
		phone1=args.getString("phno");
		mobile1=args.getString("mobile");
		fax1=args.getString("fax");
		email1=args.getString("email");
		website1=args.getString("website");
		_street1=args.getString("street1");
		_street2=args.getString("street2");
		city1=args.getString("city");
		zip1=args.getString("zip");
		
		}
		else
		{
			phone1=OEHelper.phNo.get(partners.position1);
			mobile1=OEHelper.mobile.get(partners.position1);
			fax1=OEHelper.fax.get(partners.position1);
			email1=OEHelper.email.get(partners.position1);
			website1=OEHelper.website.get(partners.position1);
			_street1=OEHelper.street1.get(partners.position1);
			_street2=OEHelper.street2.get(partners.position1);
			city1=OEHelper.city.get(partners.position1);
			zip1=OEHelper.zip.get(partners.position1);
		}
		
		mListView = (ListView) rootView.findViewById(R.id.partner_detail_listView1);
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(getActivity(),
				R.layout.partner_list_item,record) {

			public View getView(int position, View convertView, ViewGroup parent) {
				View mView = convertView;
				if (mView == null)
					mView = getActivity().getLayoutInflater().inflate(
							R.layout.partner_list_item, parent, false);
				TextView txv = (TextView) mView.findViewById(R.id.partner_ItemName);

				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				txv.setTypeface(font,Typeface.BOLD);
				txv.setText(record.get(position));
				txv.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
				txv.setTextColor(Color.rgb(84, 84, 84));
//				txv.setTextAppearance(getActivity(), android.R.attr.textAppearance);
				txv.setGravity(Gravity.CENTER);

				TextView txv2 = (TextView) mView.findViewById(R.id.partner_HeaderName);
			
				txv2.setTypeface(font,Typeface.BOLD);
				txv2.setText(header.get(position));
				txv2.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
			//	txv2.setTextAppearance(getActivity(), android.R.attr.textAppearance);
				txv2.setGravity(Gravity.CENTER);
				txv2.setBackgroundColor(Color.LTGRAY);
				txv2.setTextColor(Color.WHITE);
				return mView;
			}


		};
		mListView.setAdapter(adapter);
		//	adapter.add
		return rootView;
	}
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.idea_item, menu);
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item){

		switch(item.getItemId())
		{

		case R.id.idea_item_edit:
			//	            Do whatever you want when Home is clicked.

			
			Bundle args = new Bundle();
			menu_edit menuedit=new menu_edit();
			
			args.putString("phno",phone1);
			args.putString("mobile",mobile1);
			args.putString("fax",fax1);
			args.putString("email",email1);
			args.putString("website",website1);
		
			
			
			args.putString("city",city1);
			args.putString("zip",zip1);
			args.putString("street1",_street1);
			args.putString("street2",_street2);
	//Toast.makeText(getActivity(),""+_street1+""+_street1+""+city1+""+zip1,50).show();
			
			menuedit.setArguments(args);
			
			FragmentListener mFragment = (FragmentListener) getActivity();
			android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
		  //  Toast.makeText(getActivity(), "back called",40).show();
		 //   fm.popBackStack("menu_edit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
		 //   fm.popBackStack();
		   
			mFragment.startMainFragment(menuedit, true);
			return true; 
		case R.id.idea_item_delete:
			OEHelper.name.clear();
			
		//	MainActivity.checkfordelete=false;
			MainActivity.checkfordelete1=false;
		//	oehelper.readDataFromServerUser();
	

				oehelper.deletedata(Integer.parseInt(id));
				if(MainActivity.checkfordelete1==true)
				{ 
					AlertDialog.Builder builder1=new AlertDialog.Builder(getActivity());
					
					 builder1.setMessage("Can Not Delete This CustomerID");
			            builder1.setCancelable(true);
			            
			            oehelper.readDataFromServer();
			            builder1.setPositiveButton("Cancle",
			                    new DialogInterface.OnClickListener() {
			                public void onClick(DialogInterface dialog, int id) {
			                    dialog.cancel();
			                }
			            });
	
			            AlertDialog alert11 = builder1.create();
			            alert11.show();
					//Toast.makeText(getActivity(),"can't delete",40).show();
				}
				else
				{
					Toast.makeText(getActivity(),"deleted",40).show();
				}
				

				oehelper.readDataFromServer();
	
				

			partners partner = new partners();
			FragmentListener mFragment1 = (FragmentListener) getActivity();
			
			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
			  //  Toast.makeText(getActivity(), "back called",40).show();
			    fm1.popBackStack("partners", FragmentManager.POP_BACK_STACK_INCLUSIVE);
			    fm1.popBackStack();

			    android.support.v4.app.FragmentManager fm2 = getActivity().getSupportFragmentManager();
			    fm2.popBackStack("partners", FragmentManager.POP_BACK_STACK_INCLUSIVE);
			    fm2.popBackStack();
		//	mFragment1.startMainFragment(partner, false);
			    mFragment1.startDetailFragment(partner);
			//	            Do whatever you want when Home is clicked.
		//	Toast.makeText(getActivity(), "Home is clicked", Toast.LENGTH_SHORT).show();
			return true; 
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	public Object databaseHelper(Context context) {
		return new IdeaDBHelper(context);
	}


	public List<DrawerItem> drawerMenus(Context context) {
		return null;
	}

}
