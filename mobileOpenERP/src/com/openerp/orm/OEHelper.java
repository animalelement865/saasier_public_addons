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
package com.openerp.orm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import openerp.OEArguments;
import openerp.OEDomain;
import openerp.OpenERP;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.openerp.App;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.addons.idea.QR_equip_detail;
import com.openerp.addons.idea.move_stock_by_location2;
import com.openerp.addons.idea.work_order_detail;
import com.openerp.base.ir.Ir_model;
import com.openerp.base.ir.product_model;
import com.openerp.orm.OEFieldsHelper.OERelationData;
import com.openerp.support.OEUser;
import com.openerp.util.OEDate;
import com.openerp.util.PreferenceManager;

public class OEHelper {

	public static String sbu = "";
	public static final String TAG = "com.openerp.orm.OEHelper";
	Context mContext = null;
	OEDatabase mDatabase = null;
	OEUser mUser = null;
	PreferenceManager mPref = null;
	int mAffectedRows = 0;
	List<Long> mResultIds = new ArrayList<Long>();
	List<OEDataRow> mRemovedRecordss = new ArrayList<OEDataRow>();
	OpenERP mOpenERP = null;
	App mApp = null;
	boolean withUser = true;
	boolean mAllowSelfSignedSSL = false;

	public static String selectedsourceid = null;

	public static List<String> menufecturing_order_list;
	public static List<String> menufecturing_order_origin;
	public static List<String> menufecturing_orderlistAll = new ArrayList<String>();
	public static List<String> menufecturing_product = new ArrayList<String>();
	public static List<String> menufecturing_product_id = new ArrayList<String>();
	public static List<String> moqty = new ArrayList<String>();
	public static List<String> mostate = new ArrayList<String>();
	public static String selected_product_id_of_mrp = null;
	public static String selected_mrp_id = null;

	public static float sourcetotalqtynew1[];
	public static float sourcetotalqty = 0;
	public static String s_no = "";
	public static String prodlot_id = "";
	public static List<String> total_qty_of_product_in_selected_location = new ArrayList<String>();
	// public static List<String> menufecturing_order_lis;

	JSONObject obj;
	public static List<String> list_price_of_product_template = new ArrayList<String>();
	public static List<String> standard_price_of_product_template = new ArrayList<String>();
	public static List<String> ean13_of_product_product = new ArrayList<String>();
	public static List<String> purchase_ok_from_templateproduct = new ArrayList<String>();
	public static List<String> sale_ok_from_templateproduct = new ArrayList<String>();
	public static List<String> supply_method_product_template = new ArrayList<String>();
	public static List<String> procure_method_product_template = new ArrayList<String>();
	public static List<String> type_of_product_template = new ArrayList<String>();
	public static List<String> product_qty = new ArrayList<String>();
	public static List<String> default_code_of_product_product = new ArrayList<String>();
	public static List<String> uom_product_product = new ArrayList<String>();
	public static List<String> uom_product_product1 = new ArrayList<String>();
	public static Bitmap mIcon11 = null;
	public static List<Bitmap> image_of_product = new ArrayList<Bitmap>();
	public static List<Bitmap> image_of_QR = new ArrayList<Bitmap>();
	public static List<String> product_pty_stock_move = new ArrayList<String>();
	public static List<String> picking_id_of_stock_move = new ArrayList<String>();
	public static List<String> product_pty_separate_stock_move = new ArrayList<String>();

	public static List<String> column = new ArrayList<String>();
	public static List<String> coa_main_list = new ArrayList<String>();
	public static List<String> database = new ArrayList<String>(); //
	public static List<String> data = new ArrayList<String>(); // productname
	public static ArrayList<String> datatemplate = new ArrayList<String>(); // pro_template

	public static List<String> partnername = new ArrayList<String>();
	public static List<String> idofproduct_product = new ArrayList<String>(); //
	public static String getidfrom_product_product = null; // used in OEHelper
	public static List<String> name = new ArrayList<String>();
	public static List<String> phNo = new ArrayList<String>();
	public static List<String> objectdata = new ArrayList<String>();
	public static List<String> mobile = new ArrayList<String>();
	public static List<String> fax = new ArrayList<String>();
	public static List<String> email = new ArrayList<String>();
	public static List<String> website = new ArrayList<String>();
	public static List<String> address = new ArrayList<String>();
	public static List<String> zip = new ArrayList<String>();
	public static List<String> street1 = new ArrayList<String>();
	public static List<String> street2 = new ArrayList<String>();
	public static List<String> city = new ArrayList<String>();
	public static List<String> id = new ArrayList<String>();
	public static List<String> coa_id = new ArrayList<String>();
	public static List<String> gmp_product_specs_name = new ArrayList<String>();
	public static List<String> gmp_product_specs_unit = new ArrayList<String>();
	public static List<String> gmp_product_specs_indicator = new ArrayList<String>();
	public static List<String> gmp_product_specs_value = new ArrayList<String>();

	public static List<String> qr_equip_name = new ArrayList<String>();
	public static List<String> qr_equip_serial = new ArrayList<String>();
	public static List<String> qr_equip_criticality = new ArrayList<String>();
	public static List<String> qr_equip_asset_no = new ArrayList<String>();
	public static List<String> qr_equip_asset_model = new ArrayList<String>();
	public static List<String> qr_equip_asset_qr_code = new ArrayList<String>();
	public static List<String> qr_equip_asset_id = new ArrayList<String>();
	public static List<String> sop_selected_id_from_assets = new ArrayList<String>();

	public static String url_from_document_page;
	public static String name_from_document_page;
	public static String flag_from_document_page;

	public static List<String> stocklocation_qr = new ArrayList<String>();

	public static List<String> data_of_mrp_production_workcenter_line = new ArrayList<String>();
	public static List<String> datestart_of_mrp_production_workcenter_line = new ArrayList<String>();
	public static List<String> mo_of_mrp_production_workcenter_line = new ArrayList<String>();
	public static List<String> product_of_mrp_production_workcenter_line = new ArrayList<String>();

	public static List<String> id_of_stock_location_releted = new ArrayList<String>();

	public static List<String> mo_qty_from_movestock = new ArrayList<String>();
	public static List<String> mo_id_of_movestock = new ArrayList<String>();
	public static List<String> mo_product_from_movestock = new ArrayList<String>();
	// public static List<String> mo_ref_from_movestock = new
	// ArrayList<String>();
	public static List<String> mo_serial_from_movestock = new ArrayList<String>();
	public static List<String> mo_uom_from_movestock = new ArrayList<String>();
	public static List<String> mo_location_from_movestock = new ArrayList<String>();
	// public static String selectmo_id_from_wo;
	public static String selected_moname_from_WO;
	public static String selected_Assets_id;
	// ArrayList<String>();
	// public static String coa_id=null;
	public static String current_product_name;
	public static String selected_coa_id;
	public static String selected_mrp_work_center_id;
	public static String check_connection_exit = null;
	public static String companyid1 = null;
	public static String sourceid = null;
	public static String weight_uom_id1 = null;
	public static String desti_id1 = null;
	public static String productuom2 = null;
	public static String productid_from_stock_production_lot = null;
	public static String selected_stock_location_id = null;

	public static List<String> qty_order = new ArrayList<String>();
	public static List<String> hours_order = new ArrayList<String>();
	public static List<String> cycle_order = new ArrayList<String>();
	public static List<Integer> id_of_selected_work_order = new ArrayList<Integer>();
	public static List<String> work_centername_work_order = new ArrayList<String>();
	public static List<String> delay_hours_work_order = new ArrayList<String>();
	public static List<String> state_of_work_order = new ArrayList<String>();
	public static List<String> date_end_work_order = new ArrayList<String>();
	public static List<String> uom_work_order = new ArrayList<String>();

	public static List<String> sourcelocation_of_stock_location = new ArrayList<String>();
	public static List<String> destinationlocation_of_stock_location = new ArrayList<String>();
	public static List<String> sorceid_of_stock_location = new ArrayList<String>();
	public static List<String> dest_id_of_stock_location = new ArrayList<String>();
	public static List<String> name_of_stock_location = new ArrayList<String>();
	public static List<String> parent_stock_location_id = new ArrayList<String>();
	public static List<String> product_idlist_of_selected_location = new ArrayList<String>();

	public static List<String> name_mro_order_from_assets = new ArrayList<String>();
	public static List<String> maintanance_type_mro_order_from_assets = new ArrayList<String>();
	public static List<String> description_mro_order_from_assets = new ArrayList<String>();
	public static List<String> date_planned_mro_order_from_assets = new ArrayList<String>();
	public static List<String> parts_location_mro_order_from_assets = new ArrayList<String>();
	public static List<String> problem_description_mro_order_from_assets = new ArrayList<String>();
	public static List<String> state_mro_order_from_assets = new ArrayList<String>();
	public static List<String> id_mro_order_from_assets = new ArrayList<String>();

	int flag_of_loop_of_related_location_id = 0;

	public static int check_for_product_from_where = 0;
	public static String available_qty_of_product = null;
	public static List<String> direct_qty_of_product = new ArrayList<String>();
	public static List<Integer> project_task_count = new ArrayList<Integer>();
	public static List<String> project_name = new ArrayList<String>();
	public static List<String> project_ids = new ArrayList<String>();
	public static String selected_project_id;

	public static List<String> project_task_name = new ArrayList<String>();
	public static List<String> project_task_project = new ArrayList<String>();
	public static List<String> project_task_startdate = new ArrayList<String>();
	public static List<String> project_task_end_date = new ArrayList<String>();
	public static List<String> project_task_username = new ArrayList<String>();
	public static List<String> project_task_stage = new ArrayList<String>();
	public static List<String> project_task_all_stage_set = new ArrayList<String>();
	public static List<String> project_task_stage_id = new ArrayList<String>();
	public static List<String> project_task_id = new ArrayList<String>();
	
	public static List<String> project_issue_name = new ArrayList<String>();
	public static List<String> project_issue_partner_id = new ArrayList<String>();
	public static List<String> project_issue_id = new ArrayList<String>();
	public static List<Integer> project_issue_count = new ArrayList<Integer>();
	
	public static List<String> res_user_name = new ArrayList<String>();
	public static List<String> res_user_id = new ArrayList<String>();
	
	public static String out_id_selected=null;
	public static int id_of_out_delivery=0;
	public static List<String> productlist_for_out_delivery = new ArrayList<String>();
	public static List<String> customername = new ArrayList<String>();
	public static List<String> origin_for_out_delivery = new ArrayList<String>();
	public static List<String> mindate_for_out_delivery = new ArrayList<String>();
	public static List<String> date_for_out_delivery = new ArrayList<String>();
	public static List<String> state_for_out_delivery = new ArrayList<String>();
	public static List<String> qty_for_out_delivery = new ArrayList<String>();
	public static List<String> id_stmove_for_insert_serial = new ArrayList<String>();
	
	public static String selected_mo_id_from_scanqr=null;
	public static String selected_serial_no_for_insert_on_stmove=null;
	public static int position_where_serial_no_insert=0;
	public static List<String> serial_no_for_delivery_order = new ArrayList<String>();
	// String tempfirst_location_save=null;
	
	// public static String prod_id=null;
	// public static String proname=null;
	public OEHelper(Context context, OEDatabase oeDatabase) {
		this(context, oeDatabase, false);
	}

	public OEHelper(Context context, OEDatabase oeDatabase,
			boolean allowSelfSignedSSL) {
		Log.d(TAG, "OEHelper->OEHelper()");
		try {
			mAllowSelfSignedSSL = allowSelfSignedSSL;
			init();
			mContext = context;
			mDatabase = oeDatabase;
			mApp = (App) context.getApplicationContext();
			mOpenERP = mApp.getOEInstance();
			mUser = OEUser.current(context);
			if (mOpenERP == null && mUser != null) {
				mUser = login(mUser.getUsername(), mUser.getPassword(),
						mUser.getDatabase(), mUser.getHost());

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public OEHelper(Context context) {
		try {
			mAllowSelfSignedSSL = false;
			init();
			mContext = context;
			mApp = (App) context.getApplicationContext();
			mOpenERP = mApp.getOEInstance();
			mUser = OEUser.current(context);
			if (mUser != null) {
				Log.d("log...in" + MainActivity.check_dashboard_call_or_other,
						"ok...y");
				mUser = login(mUser.getUsername(), mUser.getPassword(),
						mUser.getDatabase(), mUser.getHost());
			} else {
				Log.d("not ..log...in", "ohh..no.");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OEHelper(Context context, boolean withUser) {
		this(context, withUser, false);
	}

	public OEHelper(Context context, boolean withUser,
			boolean allowSelfSignedSSL) {
		try {
			mAllowSelfSignedSSL = allowSelfSignedSSL;
			init();
			mContext = context;
			mApp = (App) context.getApplicationContext();
			mOpenERP = mApp.getOEInstance();
			this.withUser = withUser;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void init() {
	}

	public OEUser login(String username, String password, String database,
			String serverURL) {
		Log.d(TAG, "OEHelper->login()");
		OEUser userObj = null;
		try {
			check_connection_exit = "0";

			mOpenERP = new OpenERP(serverURL, mAllowSelfSignedSSL);
			check_connection_exit = "1";
			JSONObject response = mOpenERP.authenticate(username, password,
					database);

			int userId = 0;
			if (response.get("uid") instanceof Integer) {
				mApp.setOEInstance(mOpenERP);
				if (OEUser.current(mContext) == null || !withUser) {
					Log.i("enter if=", "dhattteriki.. stop");
					userId = response.getInt("uid");

					OEFieldsHelper fields = new OEFieldsHelper(new String[] {
							"partner_id", "tz", "image", "company_id" });
					OEDomain domain = new OEDomain();
					domain.add("id", "=", userId);
					JSONObject res = mOpenERP
							.search_read("res.users", fields.get(),
									domain.get()).getJSONArray("records")
							.getJSONObject(0);

					userObj = new OEUser();
					userObj.setAvatar(res.getString("image"));

					userObj.setDatabase(database);
					userObj.setHost(serverURL);
					userObj.setIsactive(true);
					userObj.setAndroidName(androidName(username, database));
					userObj.setPartner_id(res.getJSONArray("partner_id")
							.getInt(0));
					userObj.setTimezone(res.getString("tz"));
					userObj.setUser_id(userId);
					userObj.setUsername(username);
					userObj.setPassword(password);
					userObj.setAllowSelfSignedSSL(mAllowSelfSignedSSL);
					String company_id = new JSONArray(
							res.getString("company_id")).getString(0);
					userObj.setCompany_id(company_id);
				} else {
					userObj = OEUser.current(mContext);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();

		}
		return userObj;
	}

	public OEUser getUser() {
		return mUser;
	}

	private String androidName(String username, String database) {
		StringBuffer android_name = new StringBuffer();
		android_name.append(username);
		android_name.append("[");
		android_name.append(database);
		android_name.append("]");
		return android_name.toString();
	}

	public boolean syncWithServer() {
		return syncWithServer(false, null, null, false, -1, false);
	}

	public boolean syncWithServer(boolean removeLocalIfNotExists) {
		return syncWithServer(false, null, null, false, -1,
				removeLocalIfNotExists);
	}

	public boolean syncWithServer(OEDomain domain,
			boolean removeLocalIfNotExists) {
		return syncWithServer(false, domain, null, false, -1,
				removeLocalIfNotExists);
	}

	public boolean syncWithServer(OEDomain domain) {
		return syncWithServer(false, domain, null, false, -1, false);
	}

	public boolean syncWithServer(boolean twoWay, OEDomain domain,
			List<Object> ids) {
		return syncWithServer(twoWay, domain, ids, false, -1, false);
	}

	public int getAffectedRows() {
		return mAffectedRows;
	}

	public List<OEDataRow> getRemovedRecords() {
		return mRemovedRecordss;
	}

	public List<Integer> getAffectedIds() {
		List<Integer> ids = new ArrayList<Integer>();
		for (Long id : mResultIds) {
			ids.add(Integer.parseInt(id.toString()));
		}
		return ids;
	}

	public boolean syncWithMethod(String method, OEArguments args) {
		return syncWithMethod(method, args, false);
	}

	public boolean syncWithMethod(String method, OEArguments args,
			boolean removeLocalIfNotExists) {
		Log.d(TAG, "OEHelper->syncWithMethod()");
		Log.d(TAG, "Model: " + mDatabase.getModelName());
		Log.d(TAG, "User: " + mUser.getAndroidName());
		Log.d(TAG, "Method: " + method);
		boolean synced = false;
		OEFieldsHelper fields = new OEFieldsHelper(
				mDatabase.getDatabaseColumns());
		try {
			JSONObject result = mOpenERP.call_kw(mDatabase.getModelName(),
					method, args.getArray());
			if (result.getJSONArray("result").length() > 0)
				mAffectedRows = result.getJSONArray("result").length();
			synced = handleResultArray(fields, result.getJSONArray("result"),
					false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return synced;
	}

	public boolean syncWithServer(boolean twoWay, OEDomain domain,
			List<Object> ids, boolean limitedData, int limits,
			boolean removeLocalIfNotExists) {
		boolean synced = false;
		Log.d(TAG, "OEHelper->syncWithServer()");
		Log.d(TAG, "Model: " + mDatabase.getModelName());
		if (mUser != null)
			Log.d(TAG, "User: " + mUser.getAndroidName());
		OEFieldsHelper fields = new OEFieldsHelper(
				mDatabase.getDatabaseColumns());
		try {
			if (domain == null) {
				domain = new OEDomain();
			}
			if (ids != null) {
				domain.add("id", "in", ids);
			}
			if (limitedData) {
				mPref = new PreferenceManager(mContext);
				int data_limit = mPref.getInt("sync_data_limit", 60);
				domain.add("create_date", ">=",
						OEDate.getDateBefore(data_limit));
			}

			if (limits == -1) {
				limits = 50;
			}
			JSONObject result = mOpenERP.search_read(mDatabase.getModelName(),
					fields.get(), domain.get(), 0, limits, null, null);
			mAffectedRows = result.getJSONArray("records").length();
			synced = handleResultArray(fields, result.getJSONArray("records"),
					removeLocalIfNotExists);

		} catch (Exception e) {
			e.printStackTrace();
		}
		Log.d(TAG, mDatabase.getModelName() + " synced");

		return synced;
	}

	private boolean handleResultArray(OEFieldsHelper fields, JSONArray results,
			boolean removeLocalIfNotExists) {
		boolean flag = false;
		try {
			fields.addAll(results);
			// Handling many2many and many2one records
			List<OERelationData> rel_models = fields.getRelationData();
			for (OERelationData rel : rel_models) {
				OEHelper oe = rel.getDb().getOEInstance();
				oe.syncWithServer(false, null, rel.getIds(), false, 0, false);
			}

			List<Long> result_ids = mDatabase.createORReplace(
					fields.getValues(), removeLocalIfNotExists);
			mResultIds.addAll(result_ids);

			Log.i("mResultIds!!!!!!!!!!!!!!!" + mResultIds,
					"mResultIds!!!!!!!!!");
			mRemovedRecordss.addAll(mDatabase.getRemovedRecords());
			if (result_ids.size() > 0) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean isModelInstalled(String model) {
		boolean installed = true;
		Ir_model ir_model = new Ir_model(mContext);
		try {
			OEFieldsHelper fields = new OEFieldsHelper(new String[] { "model" });
			OEDomain domain = new OEDomain();
			domain.add("model", "=", model);
			JSONObject result = mOpenERP.search_read(ir_model.getModelName(),
					fields.get(), domain.get());
			if (result.getInt("length") > 0) {
				installed = true;
				JSONObject record = result.getJSONArray("records")
						.getJSONObject(0);
				OEValues values = new OEValues();
				values.put("id", record.getInt("id"));
				values.put("model", record.getString("model"));
				values.put("is_installed", installed);
				int count = ir_model.count("model = ?", new String[] { model });
				if (count > 0)
					ir_model.update(values, "model = ?", new String[] { model });
				else
					ir_model.create(values);
			} else {
				installed = false;
			}
		} catch (Exception e) {
			Log.d(TAG, "OEHelper->isModuleInstalled()");
			Log.e(TAG, e.getMessage() + ". No connection with OpenERP server");
		}
		return installed;
	}

	public List<OEDataRow> search_read_remain() {
		Log.d(TAG, "OEHelper->search_read_remain()");
		return search_read(true);
	}

	private OEDomain getLocalIdsDomain(String operator) {
		OEDomain domain = new OEDomain();
		JSONArray ids = new JSONArray();
		for (OEDataRow row : mDatabase.select()) {
			ids.put(row.getInt("id"));
		}
		domain.add("id", operator, ids);
		return domain;
	}

	public static void getTable(OEDatabase oedb) {

		SQLiteDatabase db = oedb.getReadableDatabase();
		// Cursor cursor;
		// cursor = db.rawQuery("SELECT city FROM res_partner",null);
		// String table = cursor.getString(1);
		//
		// Log.v("tablename..."+table,"new...!!!");

		Log.v("tablename..." + oedb.tableName(), "new...!!!");

	}

	private List<OEDataRow> search_read(boolean getRemain) {
		List<OEDataRow> rows = new ArrayList<OEDataRow>();
		try {
			OEFieldsHelper fields = new OEFieldsHelper(
					mDatabase.getDatabaseServerColumns());
			JSONObject domain = null;
			if (getRemain)
				domain = getLocalIdsDomain("not in").get();
			JSONObject result = mOpenERP.search_read(mDatabase.getModelName(),
					fields.get(), domain, 0, 100, null, null);
			for (int i = 0; i < result.getJSONArray("records").length(); i++) {
				JSONObject record = result.getJSONArray("records")
						.getJSONObject(i);
				OEDataRow row = new OEDataRow();
				row.put("id", record.getInt("id"));
				for (OEColumn col : mDatabase.getDatabaseServerColumns()) {
					row.put(col.getName(), record.get(col.getName()));

				}
				rows.add(row);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rows;
	}

	public List<OEDataRow> search_read() {
		Log.d(TAG, "OEHelper->search_read()");
		return search_read(false);
	}

	public void delete(int id) {
		Log.d(TAG, "OEHelper->delete()");
		try {
			mOpenERP.unlink(mDatabase.getModelName(), id);
			mDatabase.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object call_kw(String method, OEArguments arguments) {
		return call_kw(method, arguments, new JSONObject());
	}

	public Object call_kw(String method, OEArguments arguments,
			JSONObject context) {
		return call_kw(null, method, arguments, context, null);
	}

	public Object call_kw(String method, OEArguments arguments,
			JSONObject context, JSONObject kwargs) {
		return call_kw(null, method, arguments, context, kwargs);
	}

	public Object call_kw(String model, String method, OEArguments arguments,
			JSONObject context, JSONObject kwargs) {
		Log.d(TAG, "OEHelper->call_kw()");
		JSONObject result = null;
		if (model == null) {
			model = mDatabase.getModelName();
		}
		try {
			if (context != null) {
				arguments.add(mOpenERP.updateContext(context));
			}
			if (kwargs == null)
				result = mOpenERP.call_kw(model, method, arguments.getArray());
			else
				result = mOpenERP.call_kw(model, method, arguments.getArray(),
						kwargs);
			return result.get("result");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Integer create(OEValues values) {
		Log.d(TAG, "OEHelper->create()");
		Integer newId = null;
		try {
			JSONObject result = mOpenERP.createNew(mDatabase.getModelName(),
					generateArguments(values));
			newId = result.getInt("result");
			values.put("id", newId);
			mDatabase.create(values);
			return newId;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return newId;
	}

	public Boolean update(OEValues values, Integer id) {
		Log.d(TAG, "OEHelper->update()");
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues(mDatabase.getModelName(),
					generateArguments(values), id);

			if (flag)
				mDatabase.update(values, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	private JSONObject generateArguments(OEValues values) {
		Log.d(TAG, "OEHelper->generateArguments()");
		JSONObject arguments = new JSONObject();
		try {
			for (String key : values.keys()) {
				if (values.get(key) instanceof OEM2MIds) {

					Log.d("call update if", "call");
					OEM2MIds m2mIds = (OEM2MIds) values.get(key);
					JSONArray m2mArray = new JSONArray();
					m2mArray.put(6);
					m2mArray.put(false);
					m2mArray.put(m2mIds.getJSONIds());
					arguments.put(key, new JSONArray("[" + m2mArray.toString()
							+ "]"));
				} else {
					Log.d("call update else", "call");
					arguments.put(key, values.get(key));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arguments;
	}

	public boolean moduleExists(String name) {
		Log.d(TAG, "OEHelper->moduleExists()");
		boolean flag = false;
		try {
			OEDomain domain = new OEDomain();
			domain.add("name", "ilike", name);
			OEFieldsHelper fields = new OEFieldsHelper(new String[] { "state" });
			JSONObject result = mOpenERP.search_read("ir.module.module",
					fields.get(), domain.get());
			JSONArray records = result.getJSONArray("records");
			if (records.length() > 0
					&& records.getJSONObject(0).getString("state")
							.equalsIgnoreCase("installed")) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public OpenERP openERP() {
		return mOpenERP;
	}

	public void readdatafromserverdb() {
		// =====================================
		// for res.partner

		try {
			OEFieldsHelper fields1 = new OEFieldsHelper(new String[] { "name" });

			OEDomain domain2 = new OEDomain();
			JSONArray result2 = mOpenERP.search_read("res.partner",
					fields1.get(), domain2.get(), 0, 400, "id", "ASC")
					.getJSONArray("records");// 356

			for (int i = 0; i < result2.length(); i++) {
				obj = result2.getJSONObject(i);
				// String id = obj.getString("id");
				String value = obj.getString("name");
				// Log.d("Item name: ", value);
				partnername.add("" + value);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	// =========================================
	// product.product
	public List<String> product_imageread() {
		image_of_product.clear();
		// idofproduct_product.clear();

		// data.clear();
		// MainActivity.idofproduct_product.clear();
		// OEUser userObj = null;

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"image" });

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("product.product",
					fields2.get(), domain3.get(), 0, 141, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				// String id = obj.getString("id");
				// obj=result3.getJSONObject(i);
				// String value = obj.getString("name_template");
				String image1 = obj.getString("image");
				
				// String value = obj.getString("name_template");

				// if(idofproduct_product.contains(id))
				// {

				// data.add(""+value);
				// String id = obj.getString("id");

				byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
				Bitmap decodedByte = BitmapFactory.decodeByteArray(
						decodedString, 0, decodedString.length);

				image_of_product.add(decodedByte);

				// data.add(""+value);

				Log.i("data" + data, "ok...");
				// idofproduct_product.add(id);
				// }
			}
			
			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return default_code_of_product_product;

	}

	// ===============================================================

	public List<String> product_name() {

		// purchase_ok_from_templateproduct.clear();
		datatemplate.clear();
		idofproduct_product.clear();
		default_code_of_product_product.clear();
		uom_product_product1.clear();
		direct_qty_of_product.clear();
		// ,"purchase_ok","type","sale_ok"
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(
					new String[] { "id", "name_template", "default_code",
							"uom_id", "qty_available" });

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("product.product",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				String uom_id1 = obj.getString("uom_id");
				String value = obj.getString("name_template");

				// Log.d("stock_real=" + stock_real, "yes find...");
				// String saleok = obj.getString("sale_ok");
				// String purchaseok = obj.getString("purchase_ok");
				String default_code1 = obj.getString("default_code");
				String qty_available1 = obj.getString("qty_available");
				Log.i("id=" + id, "name=" + value + "qty_available1="
						+ qty_available1);
				Log.println(4, "print check2", "second priority");
				Log.println(3, "print check1", "first priority");

				// if(purchaseok=="true" || saleok=="true")
				// {
				// if(!type1.equals("service"))
				// {
				// purchase_ok_from_templateproduct.add(purchaseok);

				int index_prt_sub_id3 = uom_id1.indexOf("\"");
				if (index_prt_sub_id3 > 0) {
					String sub = uom_id1.substring(index_prt_sub_id3 + 1);
					String lastsub = sub.substring(0, sub.length() - 2);
					// Log.i("product id=" + lastsub, uom_product_product +
					// "");
					uom_product_product1.add(lastsub);
				}

				Log.d("available_qty_of_product=" + available_qty_of_product,
						"ok...");
				if (check_for_product_from_where == 1) {
					Log.i("call if", "ok");
					direct_qty_of_product.add(available_qty_of_product);
					// product_pty_stock_move.add(available_qty_of_product+"");
				} else {
					Log.i("call else", "ok");
					direct_qty_of_product.add(qty_available1);
					// product_pty_stock_move.add(qty_available1+"");
				}

				datatemplate.add(value);
				idofproduct_product.add(id);

				default_code_of_product_product.add(default_code1);
				// }
				// }

			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return datatemplate;
	}

	public List<String> readproducttempalate() {
		// purchase_ok_from_templateproduct.clear();
		supply_method_product_template.clear();
		procure_method_product_template.clear();
		list_price_of_product_template.clear();
		standard_price_of_product_template.clear();
		type_of_product_template.clear();
		ean13_of_product_product.clear();
		uom_product_product.clear();

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"list_price", "standard_price", "type", "supply_method",
					"procure_method", "ean13", "uom_id" });// purchase_ok

			OEDomain domain3 = new OEDomain();

			JSONArray result3 = mOpenERP.search_read("product.product",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				String value = obj.getString("list_price");
				String supply_method1 = obj.getString("supply_method");
				String procure_method1 = obj.getString("procure_method");
				String type1 = obj.getString("type");
				String ean_13 = obj.getString("ean13");
				String value1 = obj.getString("standard_price");
				String uom_id1 = obj.getString("uom_id");

				if (idofproduct_product.contains(id)) {
					// purchase_ok_from_templateproduct.add(purchaseok);

					int index_prt_sub_id3 = uom_id1.indexOf("\"");
					if (index_prt_sub_id3 > 0) {
						String sub = uom_id1.substring(index_prt_sub_id3 + 1);
						String lastsub = sub.substring(0, sub.length() - 2);
						// Log.i("product id=" + lastsub, uom_product_product +
						// "");
						uom_product_product.add("" + lastsub);
					}

					type_of_product_template.add(type1);
					ean13_of_product_product.add(ean_13);
					supply_method_product_template.add(supply_method1);
					procure_method_product_template.add(procure_method1);
					list_price_of_product_template.add("" + value);
					standard_price_of_product_template.add("" + value1);

					// idofproduct_product.add(id);

				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list_price_of_product_template;
		// return supply_method_product_template;

	}

	// gmp.coa name list
	public List<String> gmp_coaname() {

		coa_main_list.clear();
		coa_id.clear();
		// type_of_product_template.clear();
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"product_id", "partner_id" });// vender=partner id

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("gmp.coa", fields2.get(),
					domain3.get(), 0, 0, "id", "ASC").getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				// String id = obj.getString("id");
				String idsubstring = null;
				String pro_id = obj.getString("product_id");
				String coaid = obj.getString("id");
				String partner_id = obj.getString("partner_id");
				int product_id = Integer.parseInt(getidfrom_product_product);
				if (product_id < 10) {

					String idsubstring1 = pro_id.substring(1, 3);

					if (idsubstring1.contains(",") == true) {
						idsubstring = pro_id.substring(1, 2);
					}

				} else if (product_id > 9 && product_id < 100) {

					String idsubstring2 = pro_id.substring(1, 4);

					if (idsubstring2.contains(",") == true) {
						idsubstring = pro_id.substring(1, 3);
					}

				} else if (product_id > 99 && product_id < 1000) {
					String idsubstring2 = pro_id.substring(1, 5);

					if (idsubstring2.contains(",") == true) {
						idsubstring = pro_id.substring(1, 4);
					}

				}
				if (idsubstring.equals(getidfrom_product_product)) {
					int index_prt_sub_id = partner_id.indexOf("\"");
					if (index_prt_sub_id > 0) {
						String sub = partner_id.substring(index_prt_sub_id + 1);
						String lastsub = sub.substring(0, sub.length() - 2);
						coa_main_list.add(" " + lastsub);
						coa_id.add(coaid);

						// OEHelper.current_product_name;
					}
				}
			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return coa_main_list;
	}

	public List<String> availablestock_stock_move() {

		float sourcetotalqty2 = 0;
		picking_id_of_stock_move.clear();
		product_pty_stock_move.clear();
		String loca_source_id = "";

		// product_pty_separate_stock_move.clear();
		// id_of_stock_location_releted.clear();
		// float firsttotalqty = 0;
		// float secondtotalqty = 0;
		// String loca_dest_id = "";
		// String prod_id = "";
		// String measurecheck = "ketan";

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"product_qty", "product_id", "picking_id", "location_id",
					"prodlot_id" });

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.move",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String idmain = obj.getString("id");
				String pro_id = obj.getString("product_id");
				String prodlot_id1 = obj.getString("prodlot_id");
				String pro_qty = obj.getString("product_qty");
				String pick_id = obj.getString("picking_id");
				// String product_uom1 = obj.getString("product_uom");

				String location_source_id = obj.getString("location_id");

				if (location_source_id.contains(",")) {
					int index = location_source_id.indexOf(",");
					loca_source_id = location_source_id.substring(1, index);

				}

				if (pro_id.contains(",")) {
					int index = pro_id.indexOf(",");
					pro_id = pro_id.substring(1, index);

				}

				if (prodlot_id1.contains(",")) {
					int index = prodlot_id1.indexOf(",");
					prodlot_id1 = prodlot_id1.substring(1, index);

				}

				Log.d("prodlot_id1=" + prodlot_id1, "picking_id=" + pick_id
						+ "pro_id=" + pro_id + "location_source_id = "
						+ location_source_id);

				if (prodlot_id1.equals(prodlot_id)) {

					if (pro_id.equals(getidfrom_product_product)) {

						// Log.i("prodlot_id1="+prodlot_id1,
						// "picking_id="+pick_id+"pro_id="+pro_id+"@@@@@");
						product_pty_separate_stock_move.add(pro_qty);

						if (!pick_id.contains("OUT")
								&& !pick_id.contains("INT")) {
							picking_id_of_stock_move.add(pick_id);
							selectedsourceid = "";
							selectedsourceid = loca_source_id;
							Log.i("selectedsourceid id=" + selectedsourceid,
									"loc id selected");
						}
					}
				}
			}
			// float available_stock = sumofinstock - sumofoutstock;

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return picking_id_of_stock_move;
		// return supply_method_product_template;
	}

	public List<String> gmp_product_specs() {

		gmp_product_specs_name.clear();
		gmp_product_specs_unit.clear();
		gmp_product_specs_value.clear();
		gmp_product_specs_indicator.clear();

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] {
					"gmp_coa_id", "indicator", "value", "unit", "name" });

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("gmp.product_specs",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String gmp_id = obj.getString("gmp_coa_id");
				String name1 = obj.getString("name");
				String indicator1 = obj.getString("indicator");
				String unit1 = obj.getString("unit");

				String value1 = obj.getString("value");

				String idsubstring1 = null;

				// int id=Integer.parseInt(getidfrom_product_product);
				int id = Integer.parseInt(selected_coa_id);
				String codid = selected_coa_id;

				if (id < 10) {
					Log.i("specs sub1", "ok.........");
					idsubstring1 = gmp_id.substring(1, 3);

					if (idsubstring1.contains(",") == true) {

						String onlyidfromgmp_id = gmp_id.substring(1, 2);
						if (onlyidfromgmp_id.equals(codid)) {
							Log.i("enter in check....... ", "ok.........");
							// if(id==selected_coa_id)
							// {
							gmp_product_specs_name.add(name1);// +","+
							gmp_product_specs_unit.add("" + unit1);
							gmp_product_specs_value.add("" + value1);
							gmp_product_specs_indicator.add("" + indicator1);

							// gmp_product_specs_name.add(""+id);
						}
						// Log.i("specs sub="+onlyidfromgmp_id,"ok.........");
					}

				} else if (id > 9 && id < 100) {
					Log.i("specs sub1", "ok.........");
					String idsubstring2 = gmp_id.substring(1, 4);

					if (idsubstring2.contains(",") == true) {

						String onlyidfromgmp_id = gmp_id.substring(1, 3);
						if (onlyidfromgmp_id.equals(codid)) {
							Log.i("enter in check....... ", "ok.........");
							// if(id==selected_coa_id)
							// {
							gmp_product_specs_name.add(name1);// +","+
							// gmp_product_specs_name.add(name1);//+","+
							gmp_product_specs_unit.add("" + unit1);
							gmp_product_specs_value.add("" + value1);
							gmp_product_specs_indicator.add("" + indicator1);

							// gmp_product_specs_name.add(""+id);
						}

					}

				} else if (id > 99 && id < 1000) {
					Log.i("specs sub1", "ok.........");
					String idsubstring2 = gmp_id.substring(1, 5);

					if (idsubstring2.contains(",") == true) {
						Log.i("specs sub", "ok.........");
						String onlyidfromgmp_id = gmp_id.substring(1, 4);
						if (onlyidfromgmp_id.equals(codid)) {
							Log.i("enter in check....... ", "ok.........");
							// if(id==selected_coa_id)
							// {
							// gmp_product_specs_name.add(name1+","+indicator1+" ,"+unit1+" ,"+value1);//+","+

							gmp_product_specs_name.add(name1);// +","+
							// gmp_product_specs_name.add(name1);//+","+

							gmp_product_specs_unit.add("" + unit1);
							gmp_product_specs_value.add("" + value1);
							gmp_product_specs_indicator.add("" + indicator1);

							// gmp_product_specs_name.add(""+id);
						}
					}

				} else if (id > 999 && id < 10000) {
					Log.i("specs sub1", "ok.........");
					String idsubstring2 = gmp_id.substring(1, 6);

					if (idsubstring2.contains(",") == true) {
						Log.i("specs sub", "ok.........");
						String onlyidfromgmp_id = gmp_id.substring(1, 5);
						if (onlyidfromgmp_id.equals(codid)) {
							Log.i("enter in check....... ", "ok.........");
							// if(id==selected_coa_id)
							// {
							// gmp_product_specs_name.add(name1+","+indicator1+" ,"+unit1+" ,"+value1);//+","+

							gmp_product_specs_name.add(name1);// +","+
							gmp_product_specs_unit.add("" + unit1);
							gmp_product_specs_value.add("" + value1);
							gmp_product_specs_indicator.add("" + indicator1);

							// gmp_product_specs_name.add(""+id);
						}
					}
				}

			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return gmp_product_specs_name;
	}

	public void menufecturingData() {
		// ========================================================
		// menufecturingorderview
		// ===============================================================

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] {
					"product_id", "id", "product_qty", "origin", "name" });

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("mrp.production",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");
			menufecturing_order_list = new ArrayList<String>();
			product_qty = new ArrayList<String>();
			menufecturing_order_origin = new ArrayList<String>();
			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
				String pro_id = obj.getString("product_id");
				String product_qty1 = obj.getString("product_qty");
				String name = obj.getString("name");
				String origin = obj.getString("origin");

				String idsubstring = null;
				Log.i("name=" + name, "pro_id=" + pro_id + "   origin="
						+ origin);
				if (getidfrom_product_product != null) {

					int id = Integer.parseInt(getidfrom_product_product);
					if (id < 10) {

						String idsubstring1 = pro_id.substring(1, 3);

						if (idsubstring1.contains(",") == true) {
							idsubstring = pro_id.substring(1, 2);
						}

					} else if (id > 9 && id < 100) {

						String idsubstring2 = pro_id.substring(1, 4);

						if (idsubstring2.contains(",") == true) {
							idsubstring = pro_id.substring(1, 3);
						}

					} else if (id > 99 && id < 1000) {
						String idsubstring2 = pro_id.substring(1, 5);

						if (idsubstring2.contains(",") == true) {
							idsubstring = pro_id.substring(1, 4);
						}

					}
					if (idsubstring.equals(getidfrom_product_product)) {

						product_qty.add(product_qty1);
						menufecturing_order_origin.add(origin);
						menufecturing_order_list.add(name);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void menufecturingDataall() {
		// ==============================================================
		// menufecturingorderview
		// ==============================================================
		menufecturing_product.clear();
		menufecturing_orderlistAll.clear();
		menufecturing_product_id.clear();
		moqty.clear();
		mostate.clear();

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] {
					"product_id", "id", "name", "state", "product_qty" });

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("mrp.production",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
				String pro_id = obj.getString("product_id");
				String state1 = obj.getString("state");
				String qty1 = obj.getString("product_qty");
				String name = obj.getString("name");
				String ids = obj.getString("id");

				Log.i("ids=" + ids, "pro_id=" + qty1);

				String proname = null;
				if (pro_id.contains("\"")) {
					int index = pro_id.indexOf("\"");
					proname = pro_id.substring(index + 1, pro_id.length() - 2);

				}
				if (!state1.equals("cancel") && !state1.equals("done")) {

					menufecturing_product.add(proname);
					menufecturing_orderlistAll.add(name);
					moqty.add(qty1);
					mostate.add(state1);
					menufecturing_product_id.add(ids);
				}
				Log.i("mo list=" + menufecturing_orderlistAll, "pro_id="
						+ pro_id);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void mrp_production_move_id() {
		// ===============================================================================================

		mo_qty_from_movestock.clear();
		mo_product_from_movestock.clear();
		// mo_ref_from_movestock.clear();
		mo_serial_from_movestock.clear();
		mo_uom_from_movestock.clear();
		mo_location_from_movestock.clear();
		mo_id_of_movestock.clear();

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"product_id", "product_qty", "mo_id", "product_uom",
					"state", "location_id" });// "product_ids",//,"company_id","location_dest_id","date","production_id","origin",

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("stock.move",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			// product_model model = new product_model(mContext);
			// OEHelper openerp = model.getOEInstance();
			// List<OEDataRow> records = openerp.search_read();

			for (int i = 0; i < result4.length(); i++) {
				obj = result4.getJSONObject(i);

				String id1 = obj.getString("id");
				// String origin1 = obj.getString("origin");
				String mo_id1 = obj.getString("mo_id");
				String state1 = obj.getString("state");
				// String date1 = obj.getString("date");
				// String company_id1 = obj.getString("company_id");
				String product_id1 = obj.getString("product_id");
				String product_uom1 = obj.getString("product_uom");
				// String prodlot_id1 = obj.getString("prodlot_id");

				// String production_id1 = obj.getString("production_id");
				// String location_dest_id1 = obj.getString("location_dest_id");
				String product_qty1 = obj.getString("product_qty");
				// String serial_no1 = obj.getString("serial_no");
				String location_id1 = obj.getString("location_id");
				// String Serial_number_temp=obj.getString("");

				// Log.i("prodlot_id1 = " + prodlot_id1,
				// "  product_id1 "+product_id1+" "+id1);

				// String proref = null;
				String proname = null;
				String uom = null;
				String loca_id = null;
				// String loca_dest_id=null;
				// String loca_source_id=null;
				// String pro_id=null;

				if (location_id1.contains("\"")) {
					int index = location_id1.indexOf("\"");
					loca_id = location_id1.substring(index + 1,
							location_id1.length() - 2);
				}
				if (selected_mrp_id.equals(mo_id1) && state1.equals("waiting")
						|| selected_mrp_id.equals(mo_id1)
						&& state1.equals("Waiting"))// //state1.equals("waiting")//state1.equals("assigned")
				{
					// Log.i("serial_no1="+serial_no1,"ok...");
					if (product_id1 != null) {

						if (product_id1.contains("\"")) {
							int proindex = product_id1.indexOf("\"");
							proname = product_id1.substring(proindex + 1,
									product_id1.length() - 2);
							// Log.d("check for call selected mrp_id","second test");
							//
							// int proindex1 = product_id1.indexOf("]");
							// proref = product_id1.substring(proindex + 2,
							// proindex1);
							// proname = product_id1.substring(proindex1 + 1);
							// int lastind = proname.length() - 2;
							// proname = proname.substring(0, lastind);
							// }
							// int proindex3 = product_uom1.indexOf("\"");
							//
							// uom = product_uom1.substring(proindex3 + 1,
							// product_uom1.length() - 2);
						}

					}

					int proindex3 = product_uom1.indexOf("\"");
					if (proindex3 != 0) {
						uom = product_uom1.substring(proindex3 + 1,
								product_uom1.length() - 2);
					}
					// =================================
					// if (location_dest_id1.contains(",")) {
					// int index = location_dest_id1.indexOf(",");
					// loca_dest_id = location_dest_id1.substring(1, index);
					//
					// }

					// if (location_id1.contains(",")) {
					// int index = location_id1.indexOf(",");
					// loca_source_id = location_id1.substring(1, index);
					//
					// }

					// if (product_id1.contains(",")) {
					// int index = product_id1.indexOf(",");
					// pro_id = product_id1.substring(1, index);
					//
					// }
					// ================================

					mo_id_of_movestock.add(id1);
					mo_location_from_movestock.add(loca_id);
					mo_qty_from_movestock.add(product_qty1);
					mo_product_from_movestock.add(proname);
					// mo_ref_from_movestock.add(proref);
					mo_serial_from_movestock.add("");
					mo_uom_from_movestock.add(uom);
					// Log.i(" origin1="+origin1+"product_id1="+product_id1+"production_id1="+production_id1+"product_qty1="+product_qty1,"ok..");

					Log.i("OEHelper.mo_id_of_movestock.get(p) ="
							+ OEHelper.mo_id_of_movestock.size(),
							"proindex1=.." + "state1=" + state1);
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void readDataFromServer() {

		OEFieldsHelper fields = new OEFieldsHelper(new String[] { "name",
				"phone", "mobile", "fax", "email", "website", "street",
				"street2", "city", "zip" });
		OEDomain domain = new OEDomain();
		// domain.add("id", "=", 2);
		JSONArray res;
		try {
			res = mOpenERP.search_read("res.partner", fields.get(),
					domain.get(), 0, 400, "id", "ASC").getJSONArray("records");
			
			JSONObject json_data = null;
			String name1;
			String phone1;
			String mobile1;
			String fax1;
			String email1;
			String website1;
			String city1;
			String streetk;
			String street2k;
			String zip1;
			String id1;
			name.clear();
			phNo.clear();
			mobile.clear();
			fax.clear();
			email.clear();
			website.clear();
			address.clear();
			zip.clear();
			city.clear();
			street1.clear();
			street2.clear();
			id.clear();
			
			for(int i = 0; i < res.length(); i++) {

				json_data = res.getJSONObject(i);
				phone1 = json_data.getString("phone");
				name1 = json_data.getString("name");
				mobile1 = json_data.getString("mobile");
				fax1 = json_data.getString("fax");
				email1 = json_data.getString("email");
				website1 = json_data.getString("website");
				city1 = json_data.getString("city");
				streetk = json_data.getString("street");
				street2k = json_data.getString("street2");
				zip1 = json_data.getString("zip");
				id1 = json_data.getString("id");
				// image=json_data.getString("image");
				
				name.add(name1);
				phNo.add(phone1);
				mobile.add(mobile1);
				fax.add(fax1);
				email.add(email1);
				website.add(website1);
				address.add(streetk + "," + street2k + "," + city1 + "-" + zip1);
				city.add(city1);
				zip.add(zip1);
				street1.add(streetk);
				street2.add(street2k);
				id.add(id1);

				Log.d("res id=" + id1, "ids");

			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void deletedata(int id) {
		Log.d(TAG, "OEHelper->delete()");
		try {
			mOpenERP.unlink("res.partner", id);
			// mDatabase.delete(id);
		} catch (Exception e) {
			MainActivity.checkfordelete1 = true;

			e.printStackTrace();
		}
	}

	public Boolean updatedata(OEValues values, Integer id) {
		Log.d(TAG, "OEHelper->update()");
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("res.partner",
					generateArguments(values), id);
			if (flag)
				mDatabase.update(values, id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public List<String> qr_equipmentname() {
		qr_equip_name.clear();
		// purchase_ok_from_templateproduct.clear();

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"name" });

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("asset.asset",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				String name1 = obj.getString("name");
				// String value1 = obj.getString("value");
				qr_equip_name.add("" + name1);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return qr_equip_name;
	}

	public List<String> qr_equipment_detail() {

		qr_equip_serial.clear();
		qr_equip_criticality.clear();
		qr_equip_asset_no.clear();
		qr_equip_asset_model.clear();
		qr_equip_asset_qr_code.clear();
		qr_equip_asset_id.clear();
		sop_selected_id_from_assets.clear();

		// image_of_QR.clear();
		// purchase_ok_from_templateproduct.clear();

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"model", "serial", "criticality", "asset_number",
					"qr_code", "sop_id" });// ,"qr_image"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("asset.asset",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				// String name1 = obj.getString("name");
				String serial1 = obj.getString("serial");
				String model1 = obj.getString("model");
				String criticality1 = obj.getString("criticality");
				String asset_number1 = obj.getString("asset_number");
				String qr_code1 = obj.getString("qr_code");
				// String image1 = obj.getString("qr_image");
				String sop_id1 = obj.getString("sop_id");
				String sop_idsub = null;

				if (sop_id1.contains(",")) {
					int index = sop_id1.indexOf(",");
					sop_idsub = sop_id1.substring(1, index);
					Log.i("sop_idsub=" + sop_idsub, " ok ");
					sop_selected_id_from_assets.add(sop_idsub);
				}

				Log.i("sop_selected_id_from_assets="
						+ sop_selected_id_from_assets, " ok ");

				qr_equip_serial.add(serial1);
				qr_equip_criticality.add(criticality1);
				qr_equip_asset_no.add(asset_number1);
				qr_equip_asset_model.add(model1);
				qr_equip_asset_qr_code.add(qr_code1);
				qr_equip_asset_id.add(id);

				// byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
				// Bitmap decodedByte =
				// BitmapFactory.decodeByteArray(decodedString, 0,
				// decodedString.length);
				Log.i("bitmap=" + qr_code1, "ok..................");
				// image_of_QR.add(decodedByte);

			}
			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return qr_equip_asset_no;

	}

	public List<String> document_page_for_video_play() {

		flag_from_document_page = "0";
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"content", "name" });// ,"qr_image"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("document.page",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				String content1 = obj.getString("content");
				String name1 = obj.getString("name");

				if (QR_equip_detail.selected_document_page_id.equals(id)) {

					flag_from_document_page = "1";
					if (content1.contains("src")) {
						int index = content1.indexOf("src");
						String contentsub = content1.substring(index + 7);
						if (contentsub.contains("\"")) {
							int ind = contentsub.indexOf("\"");

							String temp = contentsub.substring(0, ind);
							if (temp.contains("/v/")) {
								int index1 = temp.indexOf("/v/");
								int index2 = temp.indexOf("?");
								String subtemp = temp.substring(index1 + 3,
										index2);
								url_from_document_page = "";
								url_from_document_page = subtemp;
								Log.i("sop page id=" + id + "name=" + name1,
										"url=" + url_from_document_page);
							}

						}

					} else {
						name_from_document_page = "";
						name_from_document_page = name1;
						url_from_document_page = "";
						url_from_document_page = content1;
						flag_from_document_page = "2";
						Log.i("sop page id=" + id + "name=" + name1,
								"content1=" + content1);
					}
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return qr_equip_asset_no;

	}

	// public List<String> qr_equipmentimage() {
	// image_of_QR.clear();
	//
	// try {
	// OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
	// "qr_img" });// ,"qr_image"
	//
	// OEDomain domain3 = new OEDomain();
	// JSONArray result3 = mOpenERP.search_read("asset.asset",
	// fields2.get(), domain3.get(), 0, 0, "id", "ASC")
	// .getJSONArray("records");
	//
	// for (int i = 0; i < result3.length(); i++) {
	// obj = result3.getJSONObject(i);
	// // String id = obj.getString("id");
	// // String name1 = obj.getString("name");
	// String image1 = obj.getString("qr_img");
	// byte[] decodedString = Base64.decode(image1, Base64.DEFAULT);
	// Bitmap decodedByte = BitmapFactory.decodeByteArray(
	// decodedString, 0, decodedString.length);
	// // Log.i("bitmap="+image1,"ok..................");
	// image_of_QR.add(decodedByte);
	//
	// }
	//
	// // MainActivity.data.add(""+result2);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// return qr_equip_name;
	// }

	public List<String> mrp_workcenter() {
		selected_mrp_work_center_id = null;

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"asset_ids" });// ,"qr_image"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("mrp.workcenter",
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				// String name1 = obj.getString("name");

				String acc_id = obj.getString("asset_ids");

				// int assetid=Integer.parseInt(acc_id);
				// String selectedid=;

				if (acc_id.contains(QR_equip_detail.id_asset_selected)) {
					// String taxid = obj.getString("tax_id");
					// Log.i("acc_id"+acc_id+","+QR_equip_detail.id_asset_selected,"dddd....");
					// asset_asset_id_from_assest_rel.add("asse_id="+acc_id);
					selected_mrp_work_center_id = id;
					Log.i("selected_mrp_work_center_id"
							+ selected_mrp_work_center_id,
							"QR_equip_detail.id_asset_selected...."
									+ QR_equip_detail.id_asset_selected);
				}
				Log.i("selected_mrp_work_center_id"
						+ selected_mrp_work_center_id,
						"QR_equip_detail.id_asset_selected...."
								+ QR_equip_detail.id_asset_selected);

				// mrp_workcenter_id_from_asset_rel.add(id);
				// mrp_workcenter_id_from_asset_rel.add(asset_asset_id);

			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data_of_mrp_production_workcenter_line;
	}

	public List<String> mrp_production_workcenter_line() {

		id_of_selected_work_order.clear();
		qty_order.clear();
		hours_order.clear();
		cycle_order.clear();
		datestart_of_mrp_production_workcenter_line.clear();
		mo_of_mrp_production_workcenter_line.clear();
		product_of_mrp_production_workcenter_line.clear();
		uom_work_order.clear();
		work_centername_work_order.clear();
		data_of_mrp_production_workcenter_line.clear();
		delay_hours_work_order.clear();
		date_end_work_order.clear();

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"workcenter_id", "name", "hour", "qty", "cycle", "state",
					"production_id", "date_start", "product", "date_finished",
					"uom", "delay" });// ,"qr_image"
										// ,"product_id"
			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read(
					"mrp.production.workcenter.line", fields2.get(),
					domain3.get(), 0, 0, null, "ASC").getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				int idmain = obj.getInt("id");
				// String name1 = obj.getString("name");
				String pro_id = obj.getString("production_id");
				String date_start1 = obj.getString("date_start");
				String work_center_id = obj.getString("workcenter_id");
				String name1 = obj.getString("name");
				String state1 = obj.getString("state");
				String qty1 = obj.getString("qty");
				String cycle1 = obj.getString("cycle");
				String hour1 = obj.getString("hour");
				String product1 = obj.getString("product");
				String name = obj.getString("name");
				String uom = obj.getString("uom");
				String delayhour = obj.getString("delay");

				// String datestart = obj.getString("date_start");
				String datefinished = obj.getString("date_finished");
				String idsubstring = null;

				if (selected_mrp_work_center_id != null) {
					int id = Integer.parseInt(selected_mrp_work_center_id);

					if (!state1.equals("done")) {

						if (id < 10) {

							String idsubstring1 = work_center_id
									.substring(1, 3);

							if (idsubstring1.contains(",") == true) {
								idsubstring = work_center_id.substring(1, 2);
								// Log.i("move ---sub", "ok.........");
							}

						} else if (id > 9 && id < 100) {

							String idsubstring2 = work_center_id
									.substring(1, 4);

							if (idsubstring2.contains(",") == true) {
								idsubstring = work_center_id.substring(1, 3);
								// Log.i("move--- sub", "ok.........");
							}

						} else if (id > 99 && id < 1000) {
							String idsubstring2 = work_center_id
									.substring(1, 5);

							if (idsubstring2.contains(",") == true) {
								idsubstring = work_center_id.substring(1, 4);
								// Log.i("move ---sub", "ok.........");
							}

						} else if (id > 999 && id < 10000) {
							String idsubstring2 = work_center_id
									.substring(1, 6);

							if (idsubstring2.contains(",") == true) {
								idsubstring = work_center_id.substring(1, 5);
								// Log.i("move ---sub", "ok.........");
							}

						}
						Log.i("move ---sub", "ok.........");
						if (idsubstring.equals(selected_mrp_work_center_id)) {

							Log.i("product1t=" + product1, "proid=" + pro_id);
							// String pick_id1=pick_id.substring(7,9);
							// product_pty_separate_stock_move.add(pro_qty);
							// picking_id_of_stock_move.add(pick_id);
							// String sourceString = "<b>" +"Name = "+ "</b> ";
							// String name5=""+Html.fromHtml(sourceString);
							int index_prt_sub_id = pro_id.indexOf("\\");
							if (index_prt_sub_id > 0) {
								String sub = pro_id
										.substring(index_prt_sub_id + 2);
								String lastsub = sub.substring(0,
										sub.length() - 2);
								mo_of_mrp_production_workcenter_line.add("MO "
										+ lastsub);
							}

							int index_prt_sub_id2 = product1.indexOf("]");
							if (index_prt_sub_id2 > 0) {
								if (product1
										.substring(0, index_prt_sub_id2 + 1) != null) {
									String sub = product1
											.substring(index_prt_sub_id2 + 1);
									String lastsub = sub.substring(0,
											sub.length() - 2);
									product_of_mrp_production_workcenter_line
											.add(lastsub);
								} else {
									int index_prt_sub_id3 = product1
											.indexOf("\"");

									if (index_prt_sub_id3 > 0) {

										String sub = product1
												.substring(index_prt_sub_id3 + 1);
										String lastsub = sub.substring(0,
												sub.length() - 2);
										product_of_mrp_production_workcenter_line
												.add(lastsub);
									}
								}
							}

							int index_prt_sub_id3 = work_center_id
									.indexOf("\"");
							if (index_prt_sub_id3 > 0) {
								String sub = work_center_id
										.substring(index_prt_sub_id3 + 1);
								String lastsub = sub.substring(0,
										sub.length() - 2);
								work_centername_work_order.add(lastsub);
							}
							int index_prt_sub_id4 = uom.indexOf("\"");
							if (index_prt_sub_id4 > 0) {
								String sub = uom
										.substring(index_prt_sub_id4 + 1);
								String lastsub = sub.substring(0,
										sub.length() - 2);
								uom_work_order.add(lastsub);
							}
							// Log.i("state1="
							// + state1, "ok...");
							delay_hours_work_order.add(delayhour);
							date_end_work_order.add(datefinished);
							datestart_of_mrp_production_workcenter_line
									.add(date_start1);
							state_of_work_order.add("" + state1);

							qty_order.add(qty1);
							hours_order.add(hour1);
							cycle_order.add(cycle1);
							id_of_selected_work_order.add(idmain);

							// data_of_mrp_production_workcenter_line.add("Name = "
							// + name1 + "\nCycle = " + cycle1 + "\nQty = " +
							// qty1
							// + "\nHours = " + hour1);\
							data_of_mrp_production_workcenter_line.add(name1);

							// Log.i("line id="+datestart_work_order,"line id");
							Log.i("state1" + state1 + "  " + name + "*" + uom
									+ "*" + delayhour, "line id");
							Log.i("final=" + idsubstring + "hours=" + hour1
									+ "name=" + name1 + "cycle=" + cycle1
									+ "qty=" + qty1, "ok.........");

							// product_pty_stock_move.add(pro_qty);

						}
					}
				}
				// String subwork_center_id=work_center_id.substring(start)
				// Log.i(id+"work_center_id"+work_center_id,"dddd....");
			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data_of_mrp_production_workcenter_line;
	}

	public List<String> sourceLocation_parent() {
		parent_stock_location_id.clear();

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"location_id" });// ,"qr_image","complete_name"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.location", // stock.warehouse
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				// String usage1 = obj.getString("usage");
				String location_id1 = obj.getString("location_id");
				
				// if(usage1.equals("internal"))
				// {
				if (location_id1.contains(",")) {
					int index = location_id1.indexOf(",");
					String loca_id = location_id1.substring(1, index);
					parent_stock_location_id.add(loca_id);

					Log.i("parent=" + parent_stock_location_id, "okkkkk?");

				}
				// }
			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return parent_stock_location_id;
	}

	public List<String> sourceLocationfromstock_warehouse() {
		destinationlocation_of_stock_location.clear();
		dest_id_of_stock_location.clear();
		sorceid_of_stock_location.clear();
		sourcelocation_of_stock_location.clear();
		sourcelocation_of_stock_location.add(0,
				mContext.getString(R.string.select_sorcelocation));
		destinationlocation_of_stock_location.add(0,
				mContext.getString(R.string.select_destinationlocation));

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"name", "usage" });// ,"qr_image","complete_name"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.location", // stock.warehouse
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id");
				String name1 = obj.getString("name");
				String usage1 = obj.getString("usage");

				if (usage1.equals("internal")) {
					if (!parent_stock_location_id.contains(id)) {

						sourcelocation_of_stock_location.add("" + name1);
						sorceid_of_stock_location.add("" + id);
						dest_id_of_stock_location.add("" + id);
						destinationlocation_of_stock_location.add("" + name1);

						Log.i("sorce id=" + sorceid_of_stock_location, "ok...!");

					}
				}
			}

			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sourcelocation_of_stock_location;

	}

	public void stock_product_lot_getpro_id() {
		productid_from_stock_production_lot = null;
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"ref", "product_id", "stock_available" });// "name",,,"serial_no"

			// OEFieldsHelper fields2 = new OEFieldsHelper(new String[] {""}
			// );// "name"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.production.lot",
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id"); // format of return QR
													// IN/00015_00206
				String referenceofserial_no = obj.getString("ref");
				String stock_available1 = obj.getString("stock_available");
				String product_id1 = obj.getString("product_id");

				Log.d("ref..=" + referenceofserial_no, "ok");

				// Log.v("id reference=" + referenceofserial_no,
				// " stock_available1= "
				// +"productid="+product_id1);
				if (sbu.equals(referenceofserial_no))
				// if(testing.sbu.equals(referenceofserial_no)) //change when
				// call cameraQRScanMain
				{

					String productsub = "";
					productsub = product_id1.substring(1);

					int ind = productsub.indexOf(",");
					productsub = productsub.substring(0, ind);
					productid_from_stock_production_lot = productsub;

					available_qty_of_product = stock_available1;
					Log.v("product_id1=" + product_id1, " stock_available1= "
							+ stock_available1);
				}

				// Log.i("id=" + id, obj + "+product name");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void releted_selected_stock_location_id() {
		// name_of_stock_location.clear();
		id_of_stock_location_releted.clear();
		// productid_from_stock_production_lot = null;
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"location_id" });// "name",,,"serial_no"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.location",
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id"); // format of return QR
				String location_id1 = obj.getString("location_id");
				// String product_id1 = obj.getString("prodlot_id");
				if (location_id1.contains(",")) {
					int index = location_id1.indexOf(",");
					String loca_id = location_id1.substring(1, index);
					Log.v(" id= " + id, "location id=" + location_id1);
					if (selectedsourceid.equals(loca_id)
							|| selectedsourceid.equals(id)) {
						id_of_stock_location_releted.add("" + id);
						Log.v(" id= " + id, "location id=" + location_id1);

					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ==================================================
	public List<String> main_for_get_available_qty() {
		sourcetotalqty = 0;
		float firsttotalqty = 0;
		float secondtotalqty = 0;
		String loca_dest_id = "";
		String loca_source_id = "";
		String prod_id = "";
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"location_dest_id", "location_id", "state", "product_id",
					"product_qty", "type" });

			OEDomain domain3 = new OEDomain();

			JSONArray result3 = mOpenERP.search_read("stock.move",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String idmain = obj.getString("id");
				String product_id1 = obj.getString("product_id");
				String location_source_id = obj.getString("location_id");
				String location_dest_id1 = obj.getString("location_dest_id");
				double product_qty1 = obj.getDouble("product_qty");
				String state1 = obj.getString("state");
				String type1 = obj.getString("type");
				if (location_dest_id1.contains(",")) {
					int index = location_dest_id1.indexOf(",");
					loca_dest_id = location_dest_id1.substring(1, index);
				}
				if (location_source_id.contains(",")) {
					int index = location_source_id.indexOf(",");
					loca_source_id = location_source_id.substring(1, index);
				}
				if (product_id1.contains(",")) {
					int index = product_id1.indexOf(",");
					prod_id = product_id1.substring(1, index);
				}

				Log.d("product_qty1=" + product_qty1 + "type1==" + type1,
						"ok... loca_source_id==" + loca_source_id);
				if (!id_of_stock_location_releted.contains(loca_source_id)
						&& id_of_stock_location_releted.contains(loca_dest_id)
						&& state1.equals("done")
						&& getidfrom_product_product.equals(prod_id)) {

					firsttotalqty = firsttotalqty + (float) product_qty1;
					Log.i("firsttotalqty=" + firsttotalqty, "ok... qty=="
							+ product_qty1);
				}

				if (id_of_stock_location_releted.contains(loca_source_id)
						&& !id_of_stock_location_releted.contains(loca_dest_id)
						&& state1.equals("done")
						&& getidfrom_product_product.equals(prod_id)) {

					secondtotalqty = secondtotalqty + (float) product_qty1;
					Log.i("secondtotalqty=" + secondtotalqty, "ok... qty=="
							+ product_qty1);
				}

				sourcetotalqty = firsttotalqty - secondtotalqty;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product_pty_stock_move;
	}

	public List<String> stockmoveforqtytransfer() {

		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"location_dest_id", "company_id", "product_uom" });// "weight_uom_id, "location_id"

			OEDomain domain3 = new OEDomain();

			JSONArray result3 = mOpenERP.search_read("stock.move",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {

				obj = result3.getJSONObject(i);
				// String idmain = obj.getString("id");
				// String location_source_id = obj.getString("location_id");
				String location_dest_id1 = obj.getString("location_dest_id");
				String companyid = obj.getString("company_id");
				// String producid = obj.getString("product_id");
				// String weightuomid = obj.getString("weight_uom_id");
				String productuom = obj.getString("product_uom");
				// String type1 = obj.getString("type");
				// Log.i("type=" + type1, "ok(((((((((((((=" );
				// Log.d("location destination="+location_dest_id1,"location source="+location_source_id+"id="+idmain+"pro_qty="+pro_qty);
				String idsubstring = null;

				int desti_id = Integer
						.parseInt(move_stock_by_location2.selecteddestid);

				if (desti_id < 10) {

					String idsubstring1 = location_dest_id1.substring(1, 3);

					if (idsubstring1.contains(",") == true) {
						idsubstring = location_dest_id1.substring(1, 2);

						// Log.i("dest id1="+idsubstring1,"id="+idmain+"pro_qty="+pro_qty);
					}

				} else if (desti_id > 9 && desti_id < 100) {

					String idsubstring2 = location_dest_id1.substring(1, 4);

					if (idsubstring2.contains(",") == true) {
						idsubstring = location_dest_id1.substring(1, 3);
						Log.i("dest id2=", "ok.........");
					}

				}

				else if (desti_id > 99 && desti_id < 1000) {
					String idsubstring2 = location_dest_id1.substring(1, 5);

					if (idsubstring2.contains(",") == true) {
						idsubstring = location_dest_id1.substring(1, 4);
						Log.i("dest id3=", "ok.........");
					}

				} else if (desti_id > 999 && desti_id < 10000) {
					String idsubstring2 = location_dest_id1.substring(1, 6);

					if (idsubstring2.contains(",") == true) {
						idsubstring = location_dest_id1.substring(1, 5);
						Log.i("dest id4=", "ok.........");
					}

				}
				if (idsubstring.equals(desti_id + "")) {

					// code for company_id

					companyid = companyid.substring(1, 5);

					if (companyid.contains(",") == true) {
						companyid = companyid.substring(0, 4);
						companyid1 = null;
						companyid1 = companyid;
						// Log.i("companyid1=" + companyid1, "ok..");

						// Log.i("dest id1="+idsubstring1,"id="+idmain+"pro_qty="+pro_qty);
					}
					if (companyid.contains(",") == true) {

						companyid = companyid.substring(0, 3);
						companyid1 = null;
						companyid1 = companyid;
					}

					if (companyid.contains(",") == true) {
						companyid1 = null;
						companyid = companyid.substring(0, 2);
						companyid1 = companyid;
						// companyid1=idsubstring;
						// Log.i("companyid1=" + companyid1, "ok..");
					}
					if (companyid.contains(",") == true) {
						companyid1 = null;
						companyid = companyid.substring(0, 1);
						companyid1 = companyid;

					}
					if (companyid.contains(",") == true) {
						companyid1 = null;
						companyid = companyid.substring(0, 0);
						companyid1 = companyid;

					}
					// for sorce id==================================
					productuom = productuom.substring(1, 5);

					if (productuom.contains(",") == true) {
						productuom = productuom.substring(0, 4);
						productuom2 = null;
						productuom2 = productuom;
						// Log.i("productuom2=" + productuom2, "ok..");
						// Log.i("dest id1="+idsubstring1,"id="+idmain+"pro_qty="+pro_qty);
					}
					if (productuom.contains(",") == true) {
						productuom2 = null;
						productuom = productuom.substring(0, 3);
						productuom2 = productuom;
					}

					if (productuom.contains(",") == true) {
						productuom2 = null;
						productuom = productuom.substring(0, 2);
						productuom2 = productuom;
						// companyid1=idsubstring;
						// Log.i("productuom2=" + productuom2, "ok..");
					}
					if (productuom.contains(",") == true) {
						productuom2 = null;
						productuom = productuom.substring(0, 1);
						productuom2 = productuom;

					}
					if (productuom.contains(",") == true) {
						productuom2 = null;
						productuom = productuom.substring(0, 0);
						productuom2 = productuom;
					}

					desti_id1 = idsubstring;
				
					Log.i("desti get=" + desti_id, "productuom=" + productuom
							+ "companyid=" + companyid + "selectedsourceid="
							+ selectedsourceid);

					// Log.d("location"+sourcelocation_of_stock_location,
					// product_pty_stock_move.add(pro_qty);
				}

			}
			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return product_pty_stock_move;
	}

	public Integer insertstockqty(OEValues values) {
		Integer newId = null;
		try {
			JSONObject result = mOpenERP.createNew("stock.move",
					generateArguments(values));
			// newId = result.getInt("result");
			// values.put("id", newId);
			Log.i("new created id " + result, " yes success");
			Log.d("insert called", " complete insert");
			// mDatabase.create(values);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return newId;
	}

	public void getproductfromlocationid() {
		sourcetotalqtynew1 = new float[idofproduct_product.size()];
		float firsttotalqty[] = new float[idofproduct_product.size()];
		float secondtotalqty[] = new float[idofproduct_product.size()];

		// float firsttotalqty = 0;
		// float secondtotalqty = 0;
		// float sourcetotalqtynew = 0;
		// product_idlist_of_selected_location.clear();

		String measurecheck[] = new String[idofproduct_product.size()];
		String loca_dest_id = "";
		String loca_source_id = "";
		String prod_id = "";
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"location_dest_id", "location_id", "state", "product_id",
					"product_qty", "type", "product_uom" });

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.move",
					fields2.get(), domain3.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String idmain = obj.getString("id");
				String product_id1 = obj.getString("product_id");
				String location_source_id = obj.getString("location_id");
				String location_dest_id1 = obj.getString("location_dest_id");
				double product_qty1 = obj.getDouble("product_qty");
				String state1 = obj.getString("state");
				String type1 = obj.getString("type");
				String product_uom1 = obj.getString("product_uom");
				if (location_dest_id1.contains(",")) {
					int index = location_dest_id1.indexOf(",");
					loca_dest_id = location_dest_id1.substring(1, index);
				}
				if (location_source_id.contains(",")) {
					int index = location_source_id.indexOf(",");
					loca_source_id = location_source_id.substring(1, index);
				}
				if (product_id1.contains(",")) {
					int index = product_id1.indexOf(",");
					prod_id = product_id1.substring(1, index);
				}

				for (int k = 0; k < idofproduct_product.size(); k++)

				// for(int k=0;k<product_idlist_of_selected_location.size();k++)
				{

					if (!id_of_stock_location_releted.contains(loca_source_id)
							&& id_of_stock_location_releted
									.contains(loca_dest_id)
							&& state1.equals("done")
							&& prod_id.equals(idofproduct_product.get(k))// idofproduct_product.get(k)
					) {

						// if(product_uom1.contains("kg"))
						// {
						// measurecheck[Integer.parseInt(idofproduct_product.get(k))]="kg";
						// }
						// else if(product_uom1.contains("lb"))
						// {
						// measurecheck[Integer.parseInt(idofproduct_product.get(k))]="lb";
						// }
						// else
						// {
						// measurecheck[Integer.parseInt(idofproduct_product.get(k))]="ketan";
						// }
						firsttotalqty[k] = firsttotalqty[k]
								+ (float) product_qty1;
						Log.i("firsttotalqty=" + product_qty1, "product_uom1="
								+ product_uom1);
					}
					if (id_of_stock_location_releted.contains(loca_source_id)
							&& !id_of_stock_location_releted
									.contains(loca_dest_id)
							&& state1.equals("done")
							&& prod_id.equals(idofproduct_product.get(k))) {
						
						// &&
						// product_id1.equals(product_idlist_of_selected_location.get(k)
						// if(product_uom1.contains("lb"))
						// {
						// if(measurecheck[Integer.parseInt(idofproduct_product.get(k))].equals("kg"))
						// {
						//
						// product_qty1= (((float)product_qty1)/2.2046);
						// Log.d("converted qty=" + product_qty1,"okkkkyyy" );
						// }
						//
						// }
						// if(product_uom1.contains("kg"))
						// {
						// if(measurecheck[Integer.parseInt(idofproduct_product.get(k))].equals("lb"))
						// {
						// product_qty1= (((float)product_qty1*2.2046));
						// Log.d("converted qty=" + product_qty1,"okkkkyyy" );
						// }
						//
						// }

						secondtotalqty[k] = secondtotalqty[k]
								+ (float) product_qty1;
						
						Log.i("secondtotalqty=" + product_qty1, "product_uom1="
								+ product_uom1);
					}
					
					sourcetotalqtynew1[k] = firsttotalqty[k]
							- secondtotalqty[k];
					// sourcetotalqtynew=sourcetotalqtynew+sourcetotalqtynew1;
				}
			}
			for (int j = 0; j < 250; j++) {
				Log.i("submit==" + sourcetotalqtynew1[j], "ok.....=");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void releted_selected_location_for_main() {
		id_of_stock_location_releted.clear();
		flag_of_loop_of_related_location_id = 0;
		// tempfirst_location_save=null;
		// name_of_stock_location.clear();
		// id_of_stock_location_releted.clear();
		// productid_from_stock_production_lot = null;
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"location_id" });// "name",,,"serial_no"
		
			// OEFieldsHelper fields2 = new OEFieldsHelper(new String[] {""}
			// );// "name"

			// Log.i("selected loc id="+selected_stock_location_id,
			// "selected location id");

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.location",
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id"); // format of return QR
				String location_id1 = obj.getString("location_id");

				if (location_id1.contains(",")) {
					int index = location_id1.indexOf(",");
					String loca_id = location_id1.substring(1, index);
					Log.i("location_id=" + location_id1, " ok ");

					if (selected_stock_location_id.equals(loca_id)
							|| selected_stock_location_id.equals(id)) {
						id_of_stock_location_releted.add("" + id);
						// tempfirst_location_save=selected_stock_location_id;
						Log.v(" id= " + id, "location id=" + location_id1);
					}
				}
			}
			// =====================================================================
			// id_of_stock_location_releted.remove("12");
			Log.i("@@@id_of_stock_location_releted="
					+ id_of_stock_location_releted, " ok ");

			String childid = null;
			for (int i = 0; i < id_of_stock_location_releted.size(); i++) {
				// if(!id_of_stock_location_releted.get(i).equals(id))
				{
					childid = id_of_stock_location_releted.get(i);

					for (int i1 = 0; i1 < result3.length(); i1++) {
						JSONObject obj1 = result3.getJSONObject(i1);
						String id = obj1.getString("id"); // format of return QR
						String location_id1 = obj1.getString("location_id");

						if (location_id1.contains(",")) {
							int index = location_id1.indexOf(",");
							String loca_id = location_id1.substring(1, index);
							// Log.i("location_id=" + location_id1," ok " );
							if (childid.equals(loca_id) || childid.equals(id)) {
								if (!id_of_stock_location_releted.contains(id)) {
									flag_of_loop_of_related_location_id = 1;

									id_of_stock_location_releted.add("" + id);
									Log.v(" final all id "
											+ id_of_stock_location_releted,
											"id=" + id);
								}
							}
						}
					}
				}
			}

			// id_of_stock_location_releted.add(tempfirst_location_save);
			// ====================================================
			// MainActivity.data.add(""+result2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean updaterecordconsumemove(OEValues values, Integer id) {
		Log.d(TAG, "OEHelper->update()");
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("stock.move",
					generateArguments(values), id);
			mOpenERP.updateContext(generateArguments(values));
			Log.d("call main edit 5", "call main edit 5");
			if (flag) {
				mDatabase.update(values, id);
				Log.d("call main edit", "call main edit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	// public Boolean updaterecordconsumemove() {
	// Log.d(TAG, "OEHelper->update()");
	//
	// JSONObject json=new JSONObject();
	// Boolean flag = false;
	// try {
	//
	// flag= mOpenERP.updateValues("stock.move",
	// json.put("state","assigned"),5708);
	// } catch (Exception e) {
	// Log.d("e.getMessage()="+e.getMessage(), "ok..");
	// e.printStackTrace();
	// }
	// return flag;
	// }

	public void mo_record_of_selected_product() {
		// ===============================================================================================
		menufecturing_product.clear();
		menufecturing_orderlistAll.clear();
		menufecturing_product_id.clear();
		moqty.clear();
		mostate.clear();

		mo_serial_from_movestock.clear();
		mo_uom_from_movestock.clear();
		mo_location_from_movestock.clear();

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"product_id", "product_qty", "mo_id", "serial_no", "name",
					"product_uom", "state", "location_id" });// "product_ids",//,"company_id","location_dest_id","date","production_id","origin",

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("stock.move",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			// product_model model = new product_model(mContext);
			// OEHelper openerp = model.getOEInstance();
			// List<OEDataRow> records = openerp.search_read();

			for (int i = 0; i < result4.length(); i++) {
				obj = result4.getJSONObject(i);

				String id1 = obj.getString("id");
				String mo_id1 = obj.getString("mo_id");
				String state1 = obj.getString("state");
				String name1 = obj.getString("name");
				String product_id1 = obj.getString("product_id");
				String product_uom1 = obj.getString("product_uom");
				String product_qty1 = obj.getString("product_qty");
				String location_id1 = obj.getString("location_id");

				// String production_id1 = obj.getString("production_id");
				// String location_dest_id1 = obj.getString("location_dest_id");
				// String company_id1 = obj.getString("company_id");
				// String origin1 = obj.getString("origin");

				String pro_id = null;
				if (product_id1.contains(",")) {
					int index = product_id1.indexOf(",");
					pro_id = product_id1.substring(1, index);

				}
				String proname = null;
				String uom = null;
				String loca_id = null;
				// String loca_dest_id=null;
				// String loca_source_id=null;
				// String pro_id=null;

				if (location_id1.contains("\"")) {
					int index = location_id1.indexOf("\"");
					loca_id = location_id1.substring(index + 1,
							location_id1.length() - 2);
				}
				// Log.i("mo_id="+mo_id1,"product_id...="+product_id1);
				if (!mo_id1.equals("0")
						&& pro_id.equals(OEHelper.getidfrom_product_product)
						&& !mo_id1.equals("false")
						&& pro_id.equals(OEHelper.getidfrom_product_product)) {

					Log.i("mo_id=" + mo_id1, "ok...");
					if (product_id1 != null) {

						if (product_id1.contains("\"")) {
							int proindex = product_id1.indexOf("\"");
							proname = product_id1.substring(proindex + 1,
									product_id1.length() - 2);

						}

					}

					int proindex3 = product_uom1.indexOf("\"");
					if (proindex3 != 0) {
						uom = product_uom1.substring(proindex3 + 1,
								product_uom1.length() - 2);
					}

					if (!state1.equals("cancel") && !state1.equals("done")) {
						menufecturing_product.add(proname);
						menufecturing_orderlistAll.add(name1);
						menufecturing_product_id.add(id1);
						moqty.add(product_qty1);
						mostate.add(state1);
						mo_serial_from_movestock.add("");
						mo_uom_from_movestock.add(uom);
						mo_location_from_movestock.add(loca_id);

						Log.i("origin=", "name=" + name1);
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void production_lot_for_insert_in_move_to_consume()

	{
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"ref", "product_id" });// "name",,,"serial_no"

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("stock.production.lot",
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id"); // format of return QR
													// IN/00015_00206
				String referenceofserial_no = obj.getString("ref");
				String product_id = obj.getString("product_id");
				// Log.v("id out=" + id+"   "+product_id,
				// " ref out= " + referenceofserial_no);

				if (s_no.equals(referenceofserial_no)) {

					prodlot_id = "";
					prodlot_id = id;

					Log.v("id=" + id, " ref= " + referenceofserial_no);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Boolean mrp_production_workcenter_line_for_update_state(
			OEValues values, Integer id) {

		Log.d(TAG, "OEHelper->update()");
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("mrp.production.workcenter.line",
					generateArguments(values), id);
			mOpenERP.updateContext(generateArguments(values));
			Log.d("call main edit 5", "call main edit 5");
			if (flag) {
				mDatabase.update(values, id);
				Log.d("call main edit", "call main edit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public Boolean update_state_for_mro(OEValues values, Integer id) {

		Log.d(TAG, "OEHelper->update()");
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("mro.order",
					generateArguments(values), id);
			mOpenERP.updateContext(generateArguments(values));
			Log.d("call main edit 5", "call main edit 5");
			if (flag) {
				mDatabase.update(values, id);
				Log.d("call main edit", "call main edit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void getMFO_id_from_WO() {

		selected_moname_from_WO = selected_moname_from_WO.substring(3);
		selected_moname_from_WO = "MO/" + selected_moname_from_WO;

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"name" });

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("mrp.production",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);

				String name = obj.getString("name");
				String ids = obj.getString("id");

				Log.i("mo name out=" + selected_moname_from_WO, "ids=" + name);
				if (name.equals(selected_moname_from_WO)) {
					selected_mrp_id = "";
					selected_mrp_id = ids;
					Log.i("mo name=" + name, "ids=" + ids);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void get_mro_order_from_assets() {

		name_mro_order_from_assets.clear();
		maintanance_type_mro_order_from_assets.clear();
		description_mro_order_from_assets.clear();
		date_planned_mro_order_from_assets.clear();
		parts_location_mro_order_from_assets.clear();
		problem_description_mro_order_from_assets.clear();
		state_mro_order_from_assets.clear();
		id_mro_order_from_assets.clear();
		
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"name", "maintenance_type", "description", "asset_id",
					"date_planned", "parts_location_id", "state",
					"problem_description" });

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("mro.order",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);

				String id1 = obj.getString("id");
				String name = obj.getString("name");
				String asset_id1 = obj.getString("asset_id");
				String state1 = obj.getString("state");
				String maintenance_type1 = obj.getString("maintenance_type");
				String description1 = obj.getString("description");
				String date_planned1 = obj.getString("date_planned");

				String parts_location_id1 = obj.getString("parts_location_id");
				String problem_description1 = obj
						.getString("problem_description");

				Log.d(" state1=" + state1, "ok..");
				if (asset_id1.contains(",")) {
					int ind = asset_id1.indexOf(",");
					asset_id1 = asset_id1.substring(1, ind);

				}
				if (asset_id1.equals(selected_Assets_id)) {
					if (!state1.equals("cancel")) {
						if (parts_location_id1.contains("\"")) {
							int ind = parts_location_id1.indexOf("\"");
							parts_location_id1 = parts_location_id1.substring(
									ind + 1, parts_location_id1.length() - 2);

						}
						name_mro_order_from_assets.add(name);
						maintanance_type_mro_order_from_assets
								.add(maintenance_type1);
						description_mro_order_from_assets.add(description1);
						date_planned_mro_order_from_assets.add(date_planned1);
						parts_location_mro_order_from_assets
								.add(parts_location_id1);
						problem_description_mro_order_from_assets
								.add(problem_description1);
						state_mro_order_from_assets.add(state1);
						id_mro_order_from_assets.add(id1);
					}

				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void method_for_project_project() {
		
		project_name.clear();
		project_task_count.clear();
		project_issue_count.clear();
		project_ids.clear();

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"name" });

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("project.project",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);

				String name = obj.getString("name");
				String ids = obj.getString("id");
				project_task_count.add(0);
				project_issue_count.add(0);
				project_ids.add("" + ids);
				Log.d("pro ids=" + ids, "ok");

				project_name.add(name);
				Log.i("ids=" + ids, "name=" + name);

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void method_for_project_task() {
		// project_task_count.clear();
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"project_id" });
//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("project.task",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);

				String project_id1 = obj.getString("project_id");
				//Log.i("pro_ids" + project_id1, "ok.." + project_name);
				// String ids = obj.getString("id");

				String projec_id = null;
				for (int j = 0; j < project_name.size(); j++) {
					int count = 0;

					if (project_id1.contains(",")) {
						int index = project_id1.indexOf(",");
						projec_id = project_id1.substring(1, index);
						Log.d("" + projec_id, "ok...");
						{
							if (projec_id.equals(project_ids.get(j))) {
								count = project_task_count.get(j);
								Log.d("before count=" + project_task_count,
										"ok");
								count++;
								project_task_count.set(j, count);
								Log.d("after count=" + project_task_count, "ok");

							}

						}
					}
				}
			}
			Log.i("ids=", "project_id1=" + project_task_count);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void method_for_project_issue_count() {
		
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"project_id" });
//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("project.issue",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);

				String project_id1 = obj.getString("project_id");
				Log.i("pro_ids" + project_id1, "ok.." + project_name);
				// String ids = obj.getString("id");

				String projec_id = null;
				for (int j = 0; j < project_name.size(); j++) {
					int count = 0;

					if (project_id1.contains(",")) {
						int index = project_id1.indexOf(",");
						projec_id = project_id1.substring(1, index);
						Log.d("" + projec_id, "ok...");
						{
							if (projec_id.equals(project_ids.get(j))) {
								count = project_issue_count.get(j);
								Log.d("before count=" + project_issue_count,
										"ok");
								count++;
								project_issue_count.set(j, count);
								Log.d("after count=" + project_issue_count, "ok");

							}

						}
					}
				}
			}
			Log.i("ids=", "project_id1=" + project_issue_count);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void method_forproject_task_detail() {
		
		project_task_name.clear();
		project_task_project.clear();
		project_task_startdate.clear();
		project_task_end_date.clear();
		project_task_username.clear();
		project_task_stage.clear();
	//	project_task_all_stage_set.clear();
	//	project_task_stage_id.clear();
		project_task_id.clear();

		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id","name", "project_id", "user_id"
					, "date_start", "date_end","stage_id" });// stage

			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("project.task",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
				
				String id1 = obj.getString("id");
				String project_id1 = obj.getString("project_id");
				String name1 = obj.getString("name");
				String userid = obj.getString("user_id");
				String date_start1 = obj.getString("date_start");
				String stage_id1 = obj.getString("stage_id");
				String date_end1 = obj.getString("date_end");
				
			//	Log.d("stage id="+stage_id1, "ok..");
				String stage_id2=stage_id1;
				String projec_id = null;
				if (stage_id1.contains("\"")) {
					int ind = stage_id1.indexOf("\"");
					stage_id1 = stage_id1.substring(ind + 1,
							stage_id1.length() - 2);

				}
				
				if (stage_id2.contains(",")) {
					int ind = stage_id2.indexOf(",");
					stage_id2 = stage_id2.substring(1,ind
							);
					
					Log.d("stage id=" + stage_id2, "ok...");

				}
				
				if (project_id1.contains(",")) {
					int index = project_id1.indexOf(",");
					projec_id = project_id1.substring(1, index);
					Log.d("" + projec_id, "ok...");

					if (projec_id.equals(selected_project_id)) {
						if (userid.contains("\"")) {
							int ind = userid.indexOf("\"");
							userid = userid.substring(ind + 1,
									userid.length() - 2);

						}
						if (project_id1.contains("\"")) {
							int ind = project_id1.indexOf("\"");
							project_id1 = project_id1.substring(ind + 1,
									project_id1.length() - 2);
						}
						
						project_task_id.add(id1);
						project_task_name.add(name1);
						project_task_project.add(project_id1);
						project_task_startdate.add(date_start1);
						project_task_end_date.add(date_end1);
						project_task_username.add(userid);
						project_task_stage.add(stage_id1);

						
					}
					
					
					Log.d("stage1" + project_task_all_stage_set , "ok..");
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean updater_project_task_stage(OEValues values, Integer id) {
		Log.d(TAG, "OEHelper->update()");
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("project.task",
					generateArguments(values), id);
			mOpenERP.updateContext(generateArguments(values));
			Log.d("call main edit 5", "call main edit 5");
			if (flag) {
				mDatabase.update(values, id);
				Log.d("call main edit", "call main edit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	
	public void method_for_project_issue() {
		
		project_issue_name.clear();
		project_issue_partner_id.clear();
		project_task_project.clear();
		project_task_startdate.clear();
		project_task_username.clear();
		project_task_stage.clear();
		project_issue_id.clear();
		
		
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"project_id","stage_id","name","user_id","partner_id","date" });
//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("project.issue",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {
				
				obj = result4.getJSONObject(i);

				String id1 =   obj.getString("id");
				String project_id1 = obj.getString("project_id");
				String stage_id1 = obj.getString("stage_id");
				String name1 = obj.getString("name");
				String user_id1 = obj.getString("user_id");
				String partner_id1 = obj.getString("partner_id");
				String date1 = obj.getString("date");
				
				String stage_id2=stage_id1;
				
				if (stage_id2.contains(",")) {
					int ind = stage_id2.indexOf(",");
					stage_id2 = stage_id2.substring(1,ind
							);
					
					Log.d("stage id=" + stage_id2, "ok...");

				}
				
				String projec_id = null;
				if (stage_id1.contains("\"")) {
					int ind = stage_id1.indexOf("\"");
					stage_id1 = stage_id1.substring(ind + 1,
							stage_id1.length() - 2);
				}
				
				if (partner_id1.contains("\"")) {
					int ind = partner_id1.indexOf("\"");
					partner_id1 = partner_id1.substring(ind + 1,
							partner_id1.length() - 2);

				}
				
				if (project_id1.contains(",")) {
					int index = project_id1.indexOf(",");
					projec_id = project_id1.substring(1, index);
					Log.d("" + projec_id, "ok...");

					if (projec_id.equals(selected_project_id)) {
						if (user_id1.contains("\"")) {
							int ind = user_id1.indexOf("\"");
							user_id1 = user_id1.substring(ind + 1,
									user_id1.length() - 2);

						}
						if (project_id1.contains("\"")) {
							int ind = project_id1.indexOf("\"");
							project_id1 = project_id1.substring(ind + 1,
									project_id1.length() - 2);
						}
						
						project_issue_id.add(id1);
						project_issue_name.add(name1);
						project_task_project.add(project_id1);
						project_task_startdate.add(date1);
						//project_task_end_date.add(date_end1);
						project_task_username.add(user_id1);
						project_task_stage.add(stage_id1);
						project_issue_partner_id.add(partner_id1);
						
						Log.i("id=" + project_issue_id, "name=.."+project_issue_name );
						//Log.i("name=" + project_issue_name.get(0)+", project="+project_task_project.get(0)+",date="+project_task_startdate.get(0), "ok.." );
						//Log.i("userid=" + project_task_username.get(0)+",stage= "+project_task_stage.get(0), ".partner=" + project_issue_partner_id.get(0));
						
					}
//					if(!project_task_all_stage_set.contains(stage_id1))
//					{
//						project_task_stage_id.add(stage_id2);
//						project_task_all_stage_set.add(stage_id1);
//						Log.d("id task="+project_task_stage_id, "name="+project_task_all_stage_set);
//						
//					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Boolean updater_project_issue_stage(OEValues values, Integer id) {
		//Log.d("values="+values.getString("stage_id"), "ok.."+id);
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("project.issue",
					generateArguments(values), id);
			mOpenERP.updateContext(generateArguments(values));
			Log.d("call main edit 5", "call main edit 5");
			if (flag) {
				mDatabase.update(values, id);
				Log.d("call main edit", "call main edit");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
	public void project_task_type() {
		// project_task_count.clear();
	
		project_task_stage_id.clear();
		project_task_all_stage_set.clear();
		
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"name" });
			//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("project.task.type",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
				 
				String id1 = obj.getString("id");
				String name1 = obj.getString("name");
				project_task_stage_id.add(id1);
				project_task_all_stage_set.add(name1);
				
				Log.i("project_task_stage_id="+project_task_stage_id, "project_task_all_stage_set=" + project_task_all_stage_set);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void call_method_res_user()
	{
		res_user_name.clear();
		res_user_id.clear();
		
		
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"login" });
			//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("res.users",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
			
				String id1 = obj.getString("id");
				String name1 = obj.getString("login");
				res_user_name.add(name1);
				res_user_id.add(id1);
				Log.i("name1="+name1, "id=" + id1);
				//project_task_all_stage_set.add(name1);
				
			//	Log.i("project_task_stage_id="+project_task_stage_id, "project_task_all_stage_set=" + project_task_all_stage_set);
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void out_delivery_using_stock_picking()
	{
		//id 1314
		 customername.clear(); 
		 origin_for_out_delivery.clear(); 
		 mindate_for_out_delivery.clear(); 
		 date_for_out_delivery.clear(); 
		 state_for_out_delivery.clear(); 
		
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"name","partner_id","min_date","date","state","origin" });
			//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("stock.picking",    //stock.picking.out
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
				 
				int id1 = obj.getInt("id");
				String name1 = obj.getString("name");
				String partner1 = obj.getString("partner_id");
				String min_date1 = obj.getString("min_date");
				String date1 = obj.getString("date");
				String state1 = obj.getString("state");
				String origin1 = obj.getString("origin");
				
				Log.i("st picking="+partner1+"min_date1="+min_date1, "st date1=" + date1+"state1="+state1+"origin1="+origin1);
				if(name1.equals(out_id_selected))
				{
					if (partner1.contains("\"")) {
						int ind = partner1.indexOf("\"");
						partner1 = partner1.substring(ind + 1,
								partner1.length() - 2);

					}
					 id_of_out_delivery=id1;
					 customername.add(partner1);
					 origin_for_out_delivery.add(origin1); 
					 mindate_for_out_delivery.add(min_date1); 
					 date_for_out_delivery.add(date1); 
					 state_for_out_delivery.add(state1); 
					Log.i("st picking="+id_of_out_delivery, "st picking=" + name1);
				}
		
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void getProductlist_for_delivery_from_stock_move()
	{
		productlist_for_out_delivery.clear();
		qty_for_out_delivery.clear();
		id_stmove_for_insert_serial.clear();
		serial_no_for_delivery_order.clear();
		try {
			OEFieldsHelper fields4 = new OEFieldsHelper(new String[] { "id",
					"product_id","picking_id","product_qty","prodlot_id" });//,"prodlot_id",,"name"
			//stage_id
			OEDomain domain4 = new OEDomain();
			JSONArray result4 = mOpenERP.search_read("stock.move",
					fields4.get(), domain4.get(), 0, 0, "id", "ASC")
					.getJSONArray("records");

			for (int i = 0; i < result4.length(); i++) {

				obj = result4.getJSONObject(i);
				 
				int id1 = obj.getInt("id");
				String productid = obj.getString("product_id");
				//String moid1 = obj.getString("name");
				String picking_id = obj.getString("picking_id");
				String product_qty1 = obj.getString("product_qty");
				String sr_no_id1 = obj.getString("prodlot_id");
				
				
				if (picking_id.contains(",")) {
					int ind = picking_id.indexOf(",");
					picking_id = picking_id.substring(1,ind);
					
					Log.d("picking="+picking_id, "product id"+id_of_out_delivery);
					if(picking_id.equals(""+id_of_out_delivery))
					{
						Log.i("picking="+picking_id, "product id"+productid);
						if (productid.contains("\"")) {
							int ind1 = productid.indexOf("\"");
							productid = productid.substring(ind1 + 1,
									productid.length() - 2);

						}
						if (product_qty1.contains("\"")) {
							int ind1 = product_qty1.indexOf("\"");
							product_qty1 = product_qty1.substring(ind1 + 1,
									product_qty1.length() - 2);

						}
						
						if (sr_no_id1.contains(",")) {
							int ind1 = sr_no_id1.indexOf(",");
							sr_no_id1 = sr_no_id1.substring(1,ind1);
						
							serial_no_for_delivery_order.add(""+sr_no_id1);
						}
						else
						{
							serial_no_for_delivery_order.add("");
						}
						qty_for_out_delivery.add(""+product_qty1);
						productlist_for_out_delivery.add(""+productid);
						id_stmove_for_insert_serial.add(""+id1);
						Log.d("prodlot_id1="+sr_no_id1, "pro="+productid+"ID1="+id1);
					}
				}
				
				
			}
			

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void getserialno_from_moname()
	{
		Log.d("enter product qrcode","ok..ketan");
		try {
			OEFieldsHelper fields2 = new OEFieldsHelper(new String[] { "id",
					"product_id","sr_no_id","production_id" });// "name",,,"serial_no",qr_code

			OEDomain domain3 = new OEDomain();
			JSONArray result3 = mOpenERP.search_read("product.qrcode",
					fields2.get(), domain3.get(), 0, 0, null, "ASC")
					.getJSONArray("records");

			
			Log.d("result="+result3,"ok..ketan");
			for (int i = 0; i < result3.length(); i++) {
				obj = result3.getJSONObject(i);
				String id = obj.getString("id"); // format of return QR
													// IN/00015_00206
				//String referenceofserial_no = obj.getString("ref");
				//String name1 = obj.getString("name");
				String productid = obj.getString("product_id");
				//String qr_code1 = obj.getString("qr_code");
				String sr_no_id1 = obj.getString("sr_no_id");
				
				Log.d("sr no="+sr_no_id1, "ok...");
				String production_id1 = obj.getString("production_id");
				if (production_id1.contains("\"")) {
					int ind1 = production_id1.indexOf("\"");
					production_id1 = production_id1.substring(ind1 + 1,
							production_id1.length() - 2);
					production_id1=production_id1.substring(4);
					
				}
				
				if (productid.contains("\"")) {
					int ind1 = productid.indexOf("\"");
					productid = productid.substring(ind1 + 1,
							productid.length() - 2);

				}
				
				if(("MO/"+(production_id1)).equals(selected_mo_id_from_scanqr))
				{
					
					if(productlist_for_out_delivery.contains(productid))
					{
						
						Log.d("product name array="+production_id1, "ok.."+productid);
						//if (sr_no_id1.contains(",")) {
						//	int ind = sr_no_id1.indexOf(",");
						//	sr_no_id1 = sr_no_id1.substring(1,ind);
							selected_serial_no_for_insert_on_stmove="";
							selected_serial_no_for_insert_on_stmove=sr_no_id1;
							position_where_serial_no_insert=productlist_for_out_delivery.indexOf(productid);
							Log.v("yes match mo name from production lot", "inside"+selected_serial_no_for_insert_on_stmove);
						//}
						
					}
					else
					{
						Log.v("product id not matched", "ok=");
					}
				}
				else{
					Log.v("mo id not matched"+("MO/"+(production_id1)), "ok="+selected_mo_id_from_scanqr);
				}
				
				//String mo_id=obj.getString("mo_id");
				// Log.v("id out=" + id+"   "+product_id,
				// " ref out= " + referenceofserial_no);
				//	Log.v("product_id1=" + productid+"  sr_no_id1="+sr_no_id1, " production_id1="+production_id1);
				
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public Boolean updater_serial_no_baseon_moid_on_delivery_order(OEValues values, Integer id) {
		Log.d("values="+values, "ok.."+id);
		Boolean flag = false;
		try {
			flag = mOpenERP.updateValues("stock.move",
					generateArguments(values), id);
			mOpenERP.updateContext(generateArguments(values));
			Log.d("stock move edit prodlot id", "edited");
			if (flag) {
				mDatabase.update(values, id);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}
}
