/*
 * OpenERP, Open Source Management Solution
 * Copyright (C) 2012-today OpenERP SA (<http:www.openerp.com>)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http:www.gnu.org/licenses/>
 * 
 */
package com.openerp.base.login;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import openerp.OpenERP;
import android.R.string;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.openerp.App;
import com.openerp.R;
import com.openerp.auth.OpenERPAccountManager;
import com.openerp.orm.OEHelper;
import com.openerp.support.AppScope;
import com.openerp.support.BaseFragment;
import com.openerp.support.OEDialog;
import com.openerp.support.OEUser;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

/**
 * The Class Login.
 */
public class Login extends BaseFragment {

	/** The item arr. */
	public static String[] database;
	
	String[] itemArr = null;

	/** The context. */
	Context context = null;

	/** The m action mode. */
	ActionMode mActionMode;

	/** The open erp server url. */
	String openERPServerURL = "";
	boolean mAllowSelfSignedSSL = false;

	/** The edt server url. */
	EditText edtServerUrl = null;

	/** The arguments. */
	Bundle arguments = null;

	/** The db list spinner. */
	Spinner dbListSpinner = null;

	/** The root view. */
	View rootView = null;

	/** The login user a sync. */
	LoginUser loginUserASync = null;

	/** The edt username. */
	EditText edtUsername = null;

	/** The edt password. */
	EditText edtPassword = null;

	/** The OpenERP Object */
	OpenERP openerp = null;
	
	ArrayList arrdb;
	public static string mydatabase=null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		this.context = getActivity();
		scope = new AppScope(this);
		arrdb=new ArrayList();
		
			// TODO Auto-generated method stub
			// Inflate the layout for this fragment
		rootView = inflater.inflate(R.layout.fragment_login, container, false);
		dbListSpinner = (Spinner) rootView.findViewById(R.id.lstDatabases);
		handleArguments((Bundle) getArguments());
		getActivity().setTitle(R.string.label_login);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(false);
		getActivity().getActionBar().setHomeButtonEnabled(false);
		edtUsername = (EditText) rootView.findViewById(R.id.edtUsername);
		edtPassword = (EditText) rootView.findViewById(R.id.edtPassword);
		edtPassword.setOnEditorActionListener(new OnEditorActionListener() {
	
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
						|| (actionId == EditorInfo.IME_ACTION_DONE)) {
					goNext();
				}
				return false;
			}
		});
		return rootView;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.openerp.support.FragmentHelper#handleArguments(android.os.Bundle)
	 */
	public void handleArguments(Bundle bundle) {
		arguments = bundle;
		if (arguments != null && arguments.size() > 0) {
			if (arguments.containsKey("openERPServerURL")) {
				openERPServerURL = arguments.getString("openERPServerURL");
				mAllowSelfSignedSSL = arguments
						.getBoolean("allow_self_signed_ssl");
				 database = arguments.getStringArray("databases");
			//	database=arguments.getStringArray("databases");
			//	arrdb.add(databases[0]);
			//	mydatabase=(string) arrdb.get(0);
				Log.d("databases="+database[0],"ok=====");
				List<String> dbLists = new ArrayList<String>();
				dbLists.addAll(Arrays.asList(database));
				loadDatabaseList(dbLists);
			}
		}
	}

	/**
	 * Load database list.
	 */
	private void loadDatabaseList(List<String> dbList) {
		try {
			dbList.add(0,
					getActivity().getString(R.string.login_select_database));
			ArrayAdapter<String> dbAdapter = new ArrayAdapter<String>(
					getActivity(), R.layout.spinner_custom_layout, dbList);
			dbAdapter.setDropDownViewResource(R.layout.spinner_custom_layout);
			dbListSpinner.setAdapter(dbAdapter);
		//	getdatabase();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateOptionsMenu(android.view.Menu,
	 * android.view.MenuInflater)
	 */
	
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu_fragment_login, menu);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onOptionsItemSelected(android.view.MenuItem
	 * )
	 */
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// handle item selection

		switch (item.getItemId()) {
		case R.id.menu_login_account:
			Log.d("LoginFragment()->ActionBarMenuClicked", "menu_login_account");
			goNext();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void goNext() {
		edtUsername.setError(null);
		edtPassword.setError(null);
		if (TextUtils.isEmpty(edtUsername.getText())) {
			edtUsername.setError(getResources().getString(
					R.string.toast_provide_username));
		} else if (TextUtils.isEmpty(edtPassword.getText())) {
			edtPassword.setError(getResources().getString(
					R.string.toast_provide_password));
		} else if (dbListSpinner.getSelectedItemPosition() == 0) {
			Toast.makeText(getActivity(),
					getResources().getString(R.string.toast_select_database),
					Toast.LENGTH_LONG).show();
		} else {
			loginUserASync = new LoginUser();
			loginUserASync.execute((Void) null);
		}
	}

	/**
	 * The Class LoginUser.
	 */
	private class LoginUser extends AsyncTask<Void, Void, Boolean> {

		/** The pdialog. */
		OEDialog pdialog;

		/** The error msg. */
		String errorMsg = "";

		/** The user data. */
		OEUser userData = null;

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPreExecute()
		 */
		
		protected void onPreExecute() {
			pdialog = new OEDialog(getActivity(), false, getResources()
					.getString(R.string.title_loggin_in));
			pdialog.show();
			edtPassword.setError(null);
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#doInBackground(Params[])
		 */
		
		protected Boolean doInBackground(Void... params) {

			String userName = edtUsername.getText().toString();
			String password = edtPassword.getText().toString();
			String database = dbListSpinner.getSelectedItem().toString();
			OEHelper openerp = new OEHelper(getActivity(), false,
					mAllowSelfSignedSSL);
			App app = (App) scope.context().getApplicationContext();
			app.setOEInstance(null);
			userData = openerp.login(userName, password, database,
					openERPServerURL);
			
			
			if (userData != null) {
				return true;
			} else {
				errorMsg = getResources().getString(
						R.string.toast_invalid_username_password);
			}
			
		//	openerp.readdatafromserverdb();
		//	OEHelper oehelper=new OEHelper(getActivity());
			//Toast.makeText(getActivity(), "toast....", Toast.LENGTH_LONG).show();
		//	oehelper.readDataFromServerUser();
		//	oehelper.readDataFromServer();
		
			//Toast.makeText(getActivity(), "toast....", Toast.LENGTH_LONG).show();
		//	oehelper.readdatafromserverdb();
			
			return false;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
		 */
		
		protected void onPostExecute(final Boolean success) {
			
			
//			OEHelper oehelper=new OEHelper(getActivity());
//			oehelper.product_productread();
//			oehelper.readdatafromserverdb();
//			oehelper.readproducttempalate();
//			//oehelper.readDataFromServerUser();
//			oehelper.readDataFromServer();
//			
			if (success) {
				Log.v("Creating Account For Username :",
						userData.getAndroidName());
				if (OpenERPAccountManager.fetchAllAccounts(getActivity()) != null) {
					if (OpenERPAccountManager.isAnyUser(getActivity())) {
						OpenERPAccountManager.logoutUser(getActivity(),
								OpenERPAccountManager
										.currentUser(getActivity())
										.getAndroidName());
					}
				}
				if (OpenERPAccountManager
						.createAccount(getActivity(), userData)) {
					loginUserASync.cancel(true);
					pdialog.hide();
					SyncWizard syncWizard = new SyncWizard();
					FragmentListener mFragment = (FragmentListener) getActivity();
					mFragment.startMainFragment(syncWizard, false);

				}
			} else {
				edtPassword.setError(errorMsg);
			}
			loginUserASync.cancel(true);
			pdialog.hide();
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see android.os.AsyncTask#onCancelled()
		 */
		
		protected void onCancelled() {
			loginUserASync.cancel(true);
			pdialog.hide();
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onDestroyView()
	 */
	
	public void onDestroyView() {
		super.onDestroyView();
		rootView = null; // now cleaning up!
	}

	
	public Object databaseHelper(Context context) {
		return null;
	}

	
	public List<DrawerItem> drawerMenus(Context context) {
		return null;
	}
}
