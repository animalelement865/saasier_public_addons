package com.openerp.addons.idea;

import java.io.IOException;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import openerp.OpenERP;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.App;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class menu_edit extends BaseFragment {
	OpenERP mOpenERP = null;
	App mApp = null;
	OEHelper oehelper=null;
	Bundle args;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.edit_details, container,
				false);
		
		
		MainActivity.global=2;
		args = getArguments();
		mApp = (App) getActivity().getApplicationContext();
		mOpenERP= mApp.getOEInstance();
		oehelper=new OEHelper(getActivity());
		final EditText phone=(EditText)rootView.findViewById(R.id.editText_phone);
		final EditText mob=(EditText)rootView.findViewById(R.id.editText_mobile);
		final EditText fax=(EditText)rootView.findViewById(R.id.editText_fax);
		final EditText email=(EditText)rootView.findViewById(R.id.editText_email);
		final EditText website=(EditText)rootView.findViewById(R.id.EditText_wabsite);
		final EditText street=(EditText)rootView.findViewById(R.id.EditText_street);
		final EditText street2=(EditText)rootView.findViewById(R.id.EditText_street2);
		final EditText city=(EditText)rootView.findViewById(R.id.EditText_city);
		final EditText zip=(EditText)rootView.findViewById(R.id.EditText_zip);
		final Button b=(Button)rootView.findViewById(R.id.menu_edit_button);
		
		
		
		
		
		
		phone.setText(args.getString("phno"));
		mob.setText(args.getString("mobile"));
		fax.setText(args.getString("fax"));
		email.setText(args.getString("email"));
		website.setText(args.getString("website"));
		street.setText(args.getString("street1"));
		street2.setText(args.getString("street2"));
		city.setText(args.getString("city"));
		zip.setText(args.getString("zip"));
		//final JSONObject values=new JSONObject();
		b.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				//				OEHelper.editdata(phone.toString(),mob.toString(),fax.toString(),
				//						email.toString(),website.toString(),street.toString()
				//						,street2.toString(),city.toString(),zip.toString());
				
				

				final String pho=phone.getText().toString();
				final String mobile=mob.getText().toString();
				final String fa=fax.getText().toString();
				final String ema=email.getText().toString();
				final String web=website.getText().toString();
				final String st1=street.getText().toString();
				final String st2=	street2.getText().toString();
				final String ci=city.getText().toString();
				final String zi=zip.getText().toString();
				
				
				OEValues val = new OEValues();
				
				
				if(!pho.equals(""))//(pho!=null)//
				{
					val.put("phone", phone.getText().toString());
					//val.put("phone",args.getString("phno"));
				}
				else
				{
					val.put("phone",args.getString("phno"));
				}
				if(!mobile.equals(""))//(mobile!=null)//
				{
					//val.put("mobile", args.getString("mobile"));
					val.put("mobile", mob.getText().toString());
				}	
				else
				{
					 Toast.makeText(getActivity(), "else called",40).show();
					val.put("mobile", args.getString("mobile"));
				}
				
				if(!fa.equals(""))//(fa!=null)//
				{
					val.put("fax", fax.getText().toString());
				}	
				else
				{
					val.put("fax", args.getString("fax"));
				}
				if(!ema.equals(""))
				{
					val.put("email", email.getText().toString());
				}	
				else
				{
					val.put("email", args.getString("email"));
				}
				if(!web.equals(""))//(web!=null)//
				{
					val.put("website", website.getText().toString());
				}	
				else
				{
					val.put("website", args.getString("website"));
				}
				if(!st1.equals(""))//(st1!=null)
				{
					val.put("street", street.getText().toString());
				}	
				else
				{
					val.put("street", args.getString("street1"));
				}
				
				if(!st2.equals(""))//(st2!=null)
				{
					val.put("street2", street2.getText().toString());
				}	
				else
				{
					val.put("street2", args.getString("street2"));
				}
				if(!ci.equals(""))//(ci!=null)
				{
					val.put("city", city.getText().toString());
				}	
				else
				{
					val.put("city", args.getString("city"));
				}
				if(!zi.equals(""))//(zi!=null)
				{
					val.put("zip", zip.getText().toString());
				}	
				else
				{
					val.put("zip", args.getString("zip"));
				}
			
				

					partners.checkforeditornot=true;

				oehelper.updatedata(val,Integer.parseInt(partnerdetails.id));
				oehelper.readDataFromServer();
				
//				partners par =new partners();
//				FragmentListener frag1 = (FragmentListener) getActivity();
//				android.support.v4.app.FragmentManager fm = getActivity().getSupportFragmentManager();
				  //  Toast.makeText(getActivity(), "back called",40).show();
				 //   fm1.popBackStack("partners", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				 //   fm1.popBackStack();
			//	frag1.startDetailFragment(par);
				partnerdetails  pdetail=new partnerdetails();
				FragmentListener frag = (FragmentListener) getActivity();
				android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
				  //  Toast.makeText(getActivity(), "back called",40).show();
				    fm1.popBackStack("partnerdetails", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				    fm1.popBackStack();
				    
				    android.support.v4.app.FragmentManager fm2 = getActivity().getSupportFragmentManager();
				    fm2.popBackStack("partnerdetails", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				    fm2.popBackStack();
					  //  Toast.makeText(getActivity(), "back called",40).show();
					 //   fm2.popBackStack("menu_edit", FragmentManager.POP_BACK_STACK_INCLUSIVE);
					 //   fm2.popBackStack();
				frag.startDetailFragment(pdetail);
			}
		});
		final Button cancle=(Button)rootView.findViewById(R.id.Cancleofedit);
		cancle.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				partners.checkforeditornot=true;
				
				partnerdetails  pdetail=new partnerdetails();
				FragmentListener frag = (FragmentListener) getActivity();
				android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
				  //  Toast.makeText(getActivity(), "back called",40).show();
				    fm1.popBackStack("partnerdetails", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				    fm1.popBackStack();
				    
				    android.support.v4.app.FragmentManager fm2 = getActivity().getSupportFragmentManager();
				    fm2.popBackStack("partnerdetails", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				    fm2.popBackStack();
				  //  Toast.makeText(getActivity(), "back called",40).show();
				 //   fm1.popBackStack("partners", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				 //   fm1.popBackStack();
				frag.startDetailFragment(pdetail);
			}
		});
		return rootView;
	}




	public Object databaseHelper(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}

}
