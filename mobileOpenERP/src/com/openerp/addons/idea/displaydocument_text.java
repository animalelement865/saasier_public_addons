package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.util.drawer.DrawerItem;

public class displaydocument_text extends BaseFragment {

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

		View rootView = inflater.inflate(R.layout.displaydocument_text, container,
				false);

		getActivity().setTitle(R.string.label_document_page);
		TextView name1=(TextView) rootView.findViewById(R.id.name1);
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		name1.setTypeface(font);
		name1.setText(""+OEHelper.name_from_document_page);
		TextView content=(TextView) rootView.findViewById(R.id.content1);
		content.setTypeface(font);
		if(OEHelper.url_from_document_page!=null)
		{
			content.setText(Html.fromHtml(OEHelper.url_from_document_page));
		}
		
		MainActivity.global = 2;
		return rootView;
	}

}
