/*
 * OpenERP, Open Source Management Solution
 * Copyright (C) 2012-today OpenERP SA (<http://www.openerp.com>)
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 * 
 */
package com.openerp;

import openerp.OpenERP;
import android.app.Application;
import android.os.Handler;
import android.util.Log;

import com.openerp.auth.OpenERPAccountManager;
import com.openerp.orm.OEHelper;
import com.openerp.support.OEUser;

public class App extends Application {

	public static final String TAG = App.class.getSimpleName();
	public static OpenERP mOEInstance = null;
	
	
	
	private Handler myHandler = new Handler();
	private Runnable runnable;
	Thread tread;

	int myProgress = 0;
	int progressStatus = 0;

	@Override
	public void onCreate() {
		Log.d(TAG, "App->onCreate()");
		super.onCreate();
		final OEUser user = OEUser.current(getApplicationContext());
		
		
		//============================================================
		if(MainActivity.check_dashboard_call_or_other==1)
		{
		runnable = new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				while (progressStatus == 0) {
					progressStatus = performTask();

				}
				/* Hides the Progress bar */
				myHandler.post(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub

						progressStatus = 0;

					}
				});

			}

			/* Do some task */
			private int performTask() {
				Log.d("oeuser call", "background");
				if (user != null) {
					try {
						Log.d("user not null", "background");
						mOEInstance = new OpenERP(user.getHost(),
								user.isAllowSelfSignedSSL());
						Log.d("openerp call", "background");
						mOEInstance.authenticate(user.getUsername(),
								user.getPassword(), user.getDatabase());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (!OpenERPAccountManager.isAnyUser(getApplicationContext())) {
					mOEInstance = null;
				}
				return ++myProgress;
			}
		};

		myProgress = 0;

		tread = new Thread(runnable);
		// new Thread(runnable).start();
		tread.start();
		}
		else
		{
			Log.d("oeuser call", "ok...");
			if (user != null) {
				try {
					Log.d("user not null", "ok...");
					mOEInstance = new OpenERP(user.getHost(),
							user.isAllowSelfSignedSSL());
					Log.d("openerp call", "ok...");
					mOEInstance.authenticate(user.getUsername(),
							user.getPassword(), user.getDatabase());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if (!OpenERPAccountManager.isAnyUser(getApplicationContext())) {
				mOEInstance = null;
			}
		}
		//===========================================================
		
	}

	public OpenERP getOEInstance() {
		Log.d(TAG, "App->getOEInstance()");
		
		return mOEInstance;
	}

	public void setOEInstance(OpenERP openERP) {
		Log.d(TAG, "App->setOEInstance()");
		mOEInstance = openERP;
	}
}
