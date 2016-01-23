package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.util.drawer.DrawerItem;

public class COA_details extends BaseFragment {

	ListView mListView = null;

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
		View rootView = inflater.inflate(R.layout.coa_detail, container, false);
		getActivity().setTitle(R.string.label_COA_detail);

		// if(OEHelper.gmp_product_specs_name.size()!=0)
		// {
		// mListView = (ListView) rootView.findViewById(R.id.listview);
		// mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
		// R.layout.fragment_idea_list_item,OEHelper.gmp_product_specs_name) {
		//
		// public View getView(int position, View convertView, ViewGroup parent)
		// {
		// View mView = convertView;
		// if (mView == null)
		// mView = getActivity().getLayoutInflater().inflate(
		// R.layout.fragment_idea_list_item, parent, false);
		// TextView txv = (TextView) mView.findViewById(R.id.txvIdeaName);
		// // if(OEHelper.coa_id.size()!=0)
		// // {
		// // txv.setText(""+OEHelper.coa_id.get(position));
		// // }
		// txv.setText(""+OEHelper.gmp_product_specs_name.get(position));
		// return mView;
		// }
		//
		//
		// });
		// mListView.setOnItemClickListener(this);

		// }

		if (OEHelper.gmp_product_specs_name.size() != 0) {
			TextView name = (TextView) rootView.findViewById(R.id.namecoaspecs);
			Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
			name.setTypeface(font);
			if (OEHelper.gmp_product_specs_name.size() != 0) {
				name.setText(OEHelper.gmp_product_specs_name.get(0));
			}
			TextView unit = (TextView) rootView.findViewById(R.id.indicator);
			unit.setTypeface(font);
			// if (args != null && args.containsKey("saleprice"))
			if (OEHelper.gmp_product_specs_indicator.size() != 0) {
				unit.setText(OEHelper.gmp_product_specs_indicator.get(0));
			}
			TextView indicator = (TextView) rootView.findViewById(R.id.unit);
			indicator.setTypeface(font);
			// if (args != null && args.containsKey("costprice"))
			if (OEHelper.gmp_product_specs_unit.size() != 0) {
				indicator.setText(OEHelper.gmp_product_specs_unit.get(0));
			}
			TextView value = (TextView) rootView.findViewById(R.id.value);
			value.setTypeface(font);
			// if (args != null && args.containsKey("ean13"))
			if (OEHelper.gmp_product_specs_value.size() != 0) {
				value.setText(OEHelper.gmp_product_specs_value.get(0));

			}
		} else {
			Toast.makeText(getActivity(), "No Any Record", Toast.LENGTH_SHORT).show();
		}
		return rootView;
	}

}
