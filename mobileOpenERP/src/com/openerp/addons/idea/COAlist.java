package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class COAlist extends BaseFragment implements OnItemClickListener{

	View mView;
	ListView mListView = null;
	OEHelper oehelper;
	static int positionselected=0;
	
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
		mView = inflater.inflate(R.layout.coa_list, container,
				false);
		
		getActivity().setTitle(R.string.label_COA);
		oehelper=new OEHelper(getActivity());
		oehelper.gmp_coaname();
		
		TextView pname = (TextView) mView.findViewById(R.id.productname);
		pname.setText(" "+OEHelper.current_product_name);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		pname.setTypeface(font,Typeface.BOLD);
		if(OEHelper.coa_main_list.size()!=0)
		{
		mListView = (ListView) mView.findViewById(R.id.listview);
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.coa_list_item,OEHelper.coa_main_list) {
			
			public View getView(int position, View convertView, ViewGroup parent) {
				View mView = convertView;
				if (mView == null)
					mView = getActivity().getLayoutInflater().inflate(
							R.layout.coa_list_item, parent, false);
				
				TextView vendername = (TextView) mView.findViewById(R.id.vendernamefrompartnerid);
				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				vendername.setTypeface(font);
			//	String pickdetail=OEHelper.picking_id_of_stock_move.get(position);
			
				
				vendername.setText(" "+OEHelper.coa_main_list.get(position));
				
				return mView;
			}
			
			
		});
		
		mListView.setOnItemClickListener(this);
	
		}
		else
		{
			Toast.makeText(getActivity(), "No Any Record", 0).show();
		}
		
		//Toast.makeText(getActivity(), "inventory called",30).show();
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		// TODO Auto-generated method stub
		//Toast.makeText(getActivity(), "click "+OEHelper.coa_main_list.get(arg2), 0).show();
		positionselected=0;
		//positionselected=arg2;
		if(OEHelper.coa_id.size()!=0)
		{
		OEHelper.selected_coa_id=null;
		OEHelper.selected_coa_id=OEHelper.coa_id.get(arg2);
		}
		OEHelper oehelper=new OEHelper(getActivity());
		oehelper.gmp_product_specs();
		COA_details coa_detail =new COA_details();
		FragmentListener frag1 = (FragmentListener) getActivity();
		frag1.startDetailFragment(coa_detail);
		
	}

}
