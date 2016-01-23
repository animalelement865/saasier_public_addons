package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.util.drawer.DrawerItem;

public class stock_location_main extends BaseFragment {

	ListView mListView;
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.fragment_idea, container,
				false);
		
	MainActivity.global=2;

		mListView = (ListView) rootView.findViewById(R.id.listview);
		
		if(OEHelper.name_of_stock_location.size()!=0)
		{
		mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
				R.layout.fragment_idea_list_item,OEHelper.name_of_stock_location) {
			
			public View getView(int position, View convertView, ViewGroup parent) {
				View mView = convertView;
				if (mView == null)
					mView = getActivity().getLayoutInflater().inflate(
							R.layout.fragment_idea_list_item,parent,false);
				TextView txv = (TextView) mView.findViewById(R.id.txvIdeaName);
				Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
				txv.setTypeface(font);
				txv.setText(OEHelper.name_of_stock_location.get(position));
				
				return mView;
			}

		});
		}
		else
		{
			Toast.makeText(getActivity(), "No Record", 50).show();
		}
	//	mListView.setOnItemClickListener(this);

			
		return rootView;
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
