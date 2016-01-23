package com.tabactivity;

import java.util.List;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.support.BaseFragment;
import com.openerp.util.drawer.DrawerItem;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class tab1Activity  extends BaseFragment
{
	View mView;
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
		mView = inflater.inflate(R.layout.product_to_consume, container,
				false);
		
		
		Toast.makeText(getActivity(), "tab1", 50).show();
		MainActivity.global=2;
		return mView;
	}
}

