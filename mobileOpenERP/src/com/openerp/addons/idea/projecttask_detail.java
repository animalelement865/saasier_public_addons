package com.openerp.addons.idea;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.addons.idea.DatePicker.DateWatcher;
import com.openerp.addons.idea.TimePicker.TimeWatcher;
import com.openerp.orm.OEHelper;
import com.openerp.orm.OEValues;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class projecttask_detail extends BaseFragment implements com.openerp.addons.idea.DatePicker.DateWatcher, TimeWatcher{

	List<String> mItems = new ArrayList<String>();
	ListView mListView = null;
	String changes_stage="";
	
	int k=0;
	ArrayAdapter<String> arr;
	ArrayList<String> arrlist_for_position=new ArrayList<String>();
	int position=0;
	int day,month,year;
	int hour,minute,ampm;
	int position1=0;
	int daychange,monthchange,yearchange;
	int hourchange,minutechange,ampmchange;
	
	boolean catalog_outdated=false;
	Date convertedDate;
	Date convertedDate2;
	
	
	@Override
	public Object databaseHelper(Context context) {
		return null;
	}

	@Override
	public List<DrawerItem> drawerMenus(Context context) {
		return null;
	}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);

		final View rootView = inflater.inflate(R.layout.project_task_detail, container,
				false);

		getActivity().setTitle(R.string.label_task_detail);
		
		OEHelper oehelper = new OEHelper(getActivity());
		oehelper.method_forproject_task_detail();
		oehelper.project_task_type();
		oehelper.call_method_res_user();
		
		MainActivity.global = 2;
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView productname1 = (TextView) rootView.findViewById(R.id.textView1);
		
		TextView productname3 = (TextView) rootView.findViewById(R.id.textView3);
		TextView productname4 = (TextView) rootView.findViewById(R.id.textView4);
		TextView productname5 = (TextView) rootView.findViewById(R.id.textView5);
		TextView productname6 = (TextView) rootView.findViewById(R.id.textView6);
		
		productname1.setTypeface(font,Typeface.BOLD);
		productname3.setTypeface(font,Typeface.BOLD);
		productname4.setTypeface(font,Typeface.BOLD);
		productname5.setTypeface(font,Typeface.BOLD);
		productname6.setTypeface(font,Typeface.BOLD);
		
		mListView = (ListView) rootView.findViewById(R.id.listforqr_equip);

		if (OEHelper.project_task_name.size() != 0){
//arr start==============================================================
		  k=0;
			   arr=	new ArrayAdapter<String>(getActivity(),
					R.layout.project_task_first,
					OEHelper.project_task_name) {
				
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View mView = convertView;
					
					
					if (mView == null)
						mView = getActivity().getLayoutInflater().inflate(
								R.layout.project_task_first, parent, false);
					 k++;
					TextView txv = (TextView) mView.findViewById(R.id.MO);
					Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
					txv.setTypeface(font);
					txv.setTag(position);
					
//					TextView txv1 = (TextView) mView
//							.findViewById(R.id.Schedule_date);
//					txv1.setTypeface(font);
					
					
					final TextView txv2 = (TextView) mView.findViewById(R.id.product);
					txv2.setTypeface(font);
					final TextView txv3 = (TextView) mView.findViewById(R.id.Qty);
					txv3.setTypeface(font);
					txv3.setTag(position);
					
					final TextView txv4 = (TextView) mView.findViewById(R.id.endingdate);
					txv4.setTypeface(font);
					final TextView txv5 = (TextView) mView.findViewById(R.id.stage);
					txv5.setTypeface(font);
					
					txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv.setTextColor(Color.rgb(84, 84, 84));
					txv.setText(""
							+ OEHelper.project_task_name
									.get(position));
//					txv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
//					txv1.setTextColor(Color.rgb(84, 84, 84));
//					txv1.setText(""
//							+ OEHelper.project_task_project
//									.get(position));
					txv2.setTextColor(Color.rgb(84, 84, 84));
					txv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv2.setText(""
							+ OEHelper.project_task_username
									.get(position));
					txv2.setTag(position);
//					 arrlist_for_position.add(OEHelper.project_task_stage.get(position1));
//					 Log.d(" arrlist="+arrlist_for_position, "ok..");
					txv2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
							Log.d(" view clicked", "checked finished");
							
							//Toast.makeText(getActivity(), "pos="+position1, 50).show();
							
							final Dialog dialog = new Dialog(getActivity());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialog_for_edit_task_stage);
							
						//	dialog.setTitle("Product Search");
							dialog.setOnCancelListener(new OnCancelListener() {
								
								@Override
								public void onCancel(DialogInterface dialog) {
									
									dialog.dismiss();
								}
							});
							
							TextView tv=(TextView) dialog.findViewById(R.id.stagetitle);
							tv.setText("Select User");
							final RadioGroup  radioSexGroup = (RadioGroup)dialog.findViewById(R.id.radiogroup);
							
							final RadioButton[] rb = new RadioButton[OEHelper.res_user_name.size()];
							
							radioSexGroup.setOrientation(RadioGroup.VERTICAL);
							
							   for (int i = 0; i < OEHelper.res_user_name.size(); i++) {
								   
							    rb[i] = new RadioButton(getActivity());
							    rb[i].setId(i);
							    radioSexGroup.addView(rb[i]);
							   
							    rb[i].setText(OEHelper.res_user_name.get(i));
							   
							   }
							   
							   for(int i=0;i<OEHelper.res_user_name.size();i++)
							   {
								   if(OEHelper.project_task_username.get(Integer.parseInt(txv2.getTag()+"")).toLowerCase().contains(OEHelper.res_user_name.get(i).toLowerCase()))
								   {
									   radioSexGroup.check(i);
									  // Toast.makeText(getActivity(), " check="+OEHelper.project_task_all_stage_set.indexOf(i), 5).show();	
								   }
									  
							   }
							   
							   radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						            @Override
						            public void onCheckedChanged(RadioGroup group, int checkedId) 
						            {
						            	 int selectedId = radioSexGroup.getCheckedRadioButtonId();
						            	 for(int i=0;i<OEHelper.res_user_name.size();i++)
						            	 {
						            		 if(rb[i].getId()==selectedId)
						            		 {
						            			 changes_stage= rb[i].getText().toString();
									            //    Toast.makeText(getActivity(),  ""+changes_stage, Toast.LENGTH_SHORT).show();
						            		 }
						            	 }
						            }
						        });
							Button btnDisplay = (Button)dialog.findViewById(R.id.btnupdate);
				            btnDisplay.setOnClickListener(new OnClickListener() {
				         
				                    @Override
				                    public void onClick(View v) {
				         
				                    int ind=OEHelper.res_user_name.indexOf(changes_stage);
				                    String stageid=OEHelper.res_user_id.get(ind);
				                    
				                    // Toast.makeText(getActivity(), ""+stageid+"  name="+changes_stage, 40).show();
				                    
				                    	Log.d("user id="+stageid," fine..");
				                    
				                    	OEHelper oehelper = new OEHelper(getActivity());
				                		OEValues val = new OEValues();

				                		val.put("user_id",Integer.parseInt(stageid));
				            			
				            			oehelper.updater_project_task_stage(val, Integer.parseInt(OEHelper.project_task_id.get(Integer.parseInt(txv2.getTag()+""))));
				            			//OEHelper.project_task_stage.remove(position);
				            			//OEHelper.project_task_stage.set(position, changes_stage);
				            			OEHelper oehelper1 = new OEHelper(getActivity());
				            			oehelper1.method_forproject_task_detail();
				            			oehelper1.project_task_type();
				            			//call_method_for_refrese();
				            			
				            			
				            			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
				          			
				          			    fm1.popBackStack("projecttask_detail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				          			    fm1.popBackStack();
				          			    
				            			projecttask_detail protask = new projecttask_detail();
				            			FragmentListener mFragment1 = (FragmentListener) getActivity();
				            			mFragment1.startMainFragment(protask, true);
				            			
				            			dialog.dismiss();
		               
						}

								
					});
				            dialog.show();
							
						}
					});
					txv3.setTextColor(Color.rgb(84, 84, 84));
					txv3.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv3.setText("" + OEHelper.project_task_startdate.get(position));
					txv3.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
						//========================================================================
							
							position1=Integer.parseInt(txv5.getTag()+"");
							Calendar cal = Calendar.getInstance();  
							day = cal.get(Calendar.DAY_OF_MONTH);
							month = cal.get(Calendar.MONTH)+1;
							year = cal.get(Calendar.YEAR);
							hour=cal.get(Calendar.HOUR);
							minute=cal.get(Calendar.MINUTE);
							
							daychange=cal.get(Calendar.DAY_OF_MONTH);
							monthchange=cal.get(Calendar.MONTH)+1;
							yearchange=cal.get(Calendar.YEAR);
							hourchange=cal.get(Calendar.HOUR);
							minutechange=cal.get(Calendar.MINUTE);
							ampmchange=cal.get(Calendar.AM_PM);
							
							final Dialog dialog = new Dialog(
									getActivity());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialog_for_edit_date_of_project);
							// dialog.setTitle("Product Search");
							Button b=(Button) dialog.findViewById(R.id.update);
							b.setOnClickListener(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									// TODO Auto-generated method stub
									
									
									Log.d("hour= "+hour, "minute= "+minute);
									Log.d("hourchange= "+hourchange, "minutechange= "+minutechange);
							
							if(year>yearchange)
							{
								Toast.makeText(getActivity(), "Can't set previous date", 50).show();
							}
							else if(year==yearchange && month>monthchange)
							{
								Toast.makeText(getActivity(), "Can't set previous date", 50).show();
							}
							else if(year==yearchange && month==monthchange && day>daychange)
							{
								Toast.makeText(getActivity(), "Can't set previous date", 50).show();
							}
							else if(year==yearchange && month==monthchange && day==daychange)
							{
								if(ampmchange==ampm)
								{
								if(hourchange<hour)
								{
									Toast.makeText(getActivity(), "Can't set previous date", 50).show();
								}
								else if(minutechange<=minute && hourchange==hour)
								{
									Toast.makeText(getActivity(), "Can't set previous date", 50).show();
								}
								else
								{
									OEHelper oehelper = new OEHelper(getActivity());
						    		OEValues val = new OEValues();
						    		//Calendar cal = Calendar.getInstance();  
						    		
						    		val.put("date_start",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+5);
									
						    		oehelper.updater_project_task_stage(val, Integer.parseInt(OEHelper.project_task_id.get(position1)));
					    			
					    			oehelper.method_for_project_task();
					    			oehelper.project_task_type();
					    			
					    			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
					      			
					  			    fm1.popBackStack("projecttask_detail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
					  			    fm1.popBackStack();
					  			    
					  			  projecttask_detail protask= new projecttask_detail();
					    			FragmentListener mFragment1 = (FragmentListener) getActivity();
					    			mFragment1.startMainFragment(protask, true);
					    			dialog.dismiss();
								  }
								}
								else if(ampm==0 && ampmchange==1)
								{
									OEHelper oehelper = new OEHelper(getActivity());
						    		OEValues val = new OEValues();
						    		Calendar cal = Calendar.getInstance();  
						    		
								    val.put("date_start",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+5);
									
						    		oehelper.updater_project_task_stage(val, Integer.parseInt(OEHelper.project_task_id.get(position1)));
					    			
					    			oehelper.method_for_project_task();
					    			oehelper.project_task_type();
					    			
					    			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
					      			
					  			    fm1.popBackStack("projecttask_detail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
					  			    fm1.popBackStack();
					  			    
					  			  projecttask_detail protask= new projecttask_detail();
					    			FragmentListener mFragment1 = (FragmentListener) getActivity();
					    			mFragment1.startMainFragment(protask, true);
					    			dialog.dismiss();
								}
								else if(ampm==1 && ampmchange==0)
								{
									Toast.makeText(getActivity(), "Can't set previous date", 50).show();
								}
							}
							else
							{
								OEHelper oehelper = new OEHelper(getActivity());
					    		OEValues val = new OEValues();
					    		Calendar cal = Calendar.getInstance();  
					    		
							    val.put("date_start",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+5);
								
					    		oehelper.updater_project_task_stage(val, Integer.parseInt(OEHelper.project_task_id.get(position1)));
				    			
				    			oehelper.method_for_project_task();
				    			oehelper.project_task_type();
				    			
				    			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
				      			
				  			    fm1.popBackStack("projecttask_detail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				  			    fm1.popBackStack();
				  			    
				    			projecttask_detail protask= new projecttask_detail();
				    			FragmentListener mFragment1 = (FragmentListener) getActivity();
				    			mFragment1.startMainFragment(protask, true);
				    			dialog.dismiss();
								
							}
							
						}
					});
					com.openerp.addons.idea.DatePicker d =  (com.openerp.addons.idea.DatePicker) dialog
							.findViewById(R.id.datePicker1);
					d.setDateChangedListener(new DateWatcher() {
						
						@Override
						public void onDateChanged(Calendar c) {
							
							//Toast.makeText(getContext(), "date change", 55).show();
							Log.e("","" + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
											+ " " + c.get(Calendar.YEAR));
							
							daychange=c.get(Calendar.DAY_OF_MONTH);
							monthchange=(c.get(Calendar.MONTH)+1);
							yearchange=c.get(Calendar.YEAR);
							
						}
					});
			com.openerp.addons.idea.TimePicker t = (com.openerp.addons.idea.TimePicker) dialog
					.findViewById(R.id.timePicker2);
			t.setTimeChangedListener(new TimeWatcher() {
				
				@Override
				public void onTimeChanged(int h, int m, int am_pm) {
					
					//Toast.makeText(getContext(), "time change", 55).show();
					
					hourchange=h;
					minutechange=m;
					ampmchange=am_pm;
					
					Log.d("ampm"+am_pm, "hourchange"+hourchange+""+minutechange+""+ampmchange);
					
				}
			});
			// t.setCurrentTimeFormate(TimePicker.OUR_12);
			t.setAMPMVisible(true);
			dialog.show();
			
						}});
					
					txv3.setTag(position);
					
					txv4.setTextColor(Color.rgb(84, 84, 84));
					txv4.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv4.setText("" + OEHelper.project_task_end_date.get(position));
					txv4.setTag(position);
					txv4.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							position1=Integer.parseInt(txv4.getTag()+"");
							String valid_until=OEHelper.project_task_startdate.get(position1);
							
						    if(!valid_until.equals("false"))
							{
								
						    	position1=Integer.parseInt(txv5.getTag()+"");
								Calendar cal = Calendar.getInstance();  
								day = cal.get(Calendar.DAY_OF_MONTH);
								month = cal.get(Calendar.MONTH)+1;
								year = cal.get(Calendar.YEAR);
								hour=cal.get(Calendar.HOUR);
								minute=cal.get(Calendar.MINUTE);
								
								daychange=cal.get(Calendar.DAY_OF_MONTH);
								monthchange=cal.get(Calendar.MONTH)+1;
								yearchange=cal.get(Calendar.YEAR);
								hourchange=cal.get(Calendar.HOUR);
								minutechange=cal.get(Calendar.MINUTE);
								ampmchange=cal.get(Calendar.AM_PM);
						    	
								
								Log.d("month="+month, "ok");
								
								final Dialog dialog = new Dialog(
										getActivity());
								dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
								dialog.setContentView(R.layout.dialog_for_edit_date_of_project);
								// dialog.setTitle("Product Search");
								Button b=(Button) dialog.findViewById(R.id.update);
								
								b.setOnClickListener(new OnClickListener() {
									
									@Override
									public void onClick(View arg0) {
										// TODO Auto-generated method stub
										
										String valid_until=OEHelper.project_task_startdate.get(position1);
										Calendar cal = Calendar.getInstance();  
										
									    String current = daychange+"/"+(monthchange)+"/"+yearchange+" "+hourchange+":"+minutechange+":"+10;
									    SimpleDateFormat dateFormat = new SimpleDateFormat(
									            "MM/dd/yyyy hh:mm:ss");
									    
									    SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
									    convertedDate = new Date();
									    convertedDate2 = new Date();
									    
									     try {
									    	 
											convertedDate = df2.parse(valid_until);
											convertedDate2 = dateFormat.parse(current);
											
										} catch (ParseException e) {
											e.printStackTrace();
										}

										if (convertedDate.after(convertedDate2)) {
									           
								        	Toast.makeText(getActivity(), "End date must be greater than start date", 64).show();
								        	Log.d("End date must be greater than start date", "checking truth");
								        	
								        } else {
								        	
								        	
								        	OEHelper oehelper = new OEHelper(getActivity());
								    		OEValues val = new OEValues();
								    		
								    		val.put("date_end",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+10);
								    		
											oehelper.updater_project_task_stage(val, Integer.parseInt(OEHelper.project_task_id.get(position1)));
											OEHelper oehelper1 = new OEHelper(getActivity());
											oehelper1.method_forproject_task_detail();
											oehelper.project_task_type();
											
											android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
															
										    fm1.popBackStack("projecttask_detail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
										    fm1.popBackStack();
											    
											projecttask_detail protask = new projecttask_detail();
											FragmentListener mFragment1 = (FragmentListener) getActivity();
											mFragment1.startMainFragment(protask, true);
											dialog.dismiss();
										}
										
									}
								});
								com.openerp.addons.idea.DatePicker d =  (com.openerp.addons.idea.DatePicker) dialog
										.findViewById(R.id.datePicker1);
								d.setDateChangedListener(new DateWatcher() {
									
									@Override
									public void onDateChanged(Calendar c) {
										
										//Toast.makeText(getContext(), "date change", 55).show();
										
										Log.e("",
												"" + c.get(Calendar.MONTH) + " " + c.get(Calendar.DAY_OF_MONTH)
														+ " " + c.get(Calendar.YEAR));
										
										daychange=c.get(Calendar.DAY_OF_MONTH);
										monthchange=(c.get(Calendar.MONTH)+1);
										yearchange=c.get(Calendar.YEAR);
										
										
										
									}
								});

								com.openerp.addons.idea.TimePicker t = (com.openerp.addons.idea.TimePicker) dialog
										.findViewById(R.id.timePicker2);
								t.setTimeChangedListener(new TimeWatcher() {
									
									@Override
									public void onTimeChanged(int h, int m, int am_pm) {
										
										hourchange=h;
										minutechange=m;
										ampmchange=am_pm;
										
										Log.d("ampm"+am_pm, "hourchange"+hourchange+""+minutechange+""+ampmchange);
										
									}
								});
								// t.setCurrentTimeFormate(TimePicker.OUR_12);
								t.setAMPMVisible(true);
								dialog.show();
							
							}
							else
							{
								Toast.makeText(getActivity(), "Starting date set first", 6).show();
							}
							
							
						}
					});
					
					txv5.setTextColor(Color.rgb(84, 84, 84));
					txv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv5.setText("" + OEHelper.project_task_stage.get(position));
					txv5.setTag(position);
					txv5.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
							//===========================================================================
							Log.d(" view clicked", "checked finished");
							
							final Dialog dialog = new Dialog(getActivity());
							dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
							dialog.setContentView(R.layout.dialog_for_edit_task_stage);
				
							dialog.setOnCancelListener(new OnCancelListener() {
								
								@Override
								public void onCancel(DialogInterface dialog) {
									
									dialog.dismiss();
								}
							});
							
							final RadioGroup  radioSexGroup = (RadioGroup)dialog.findViewById(R.id.radiogroup);
							
							final RadioButton[] rb = new RadioButton[OEHelper.project_task_all_stage_set.size()];
							   
							radioSexGroup.setOrientation(RadioGroup.VERTICAL);
							
							   for (int i = 0; i < OEHelper.project_task_all_stage_set.size(); i++) {
								   
							    rb[i] = new RadioButton(getActivity());
							    rb[i].setId(i);
							    radioSexGroup.addView(rb[i]);
							   
							    rb[i].setText(OEHelper.project_task_all_stage_set.get(i));
							   
							   }
							   
							   for(int i=0;i<OEHelper.project_task_all_stage_set.size();i++)
							   {
								   if(OEHelper.project_task_all_stage_set.get(i).equals(OEHelper.project_task_stage.get(Integer.parseInt(txv5.getTag()+""))))
								   {
									   radioSexGroup.check(i);
									  // Toast.makeText(getActivity(), " check="+OEHelper.project_task_all_stage_set.indexOf(i), 5).show();	
								   }
							   }
							   
							   radioSexGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

						            @Override
						            public void onCheckedChanged(RadioGroup group, int checkedId) 
						            {

						            	 int selectedId = radioSexGroup.getCheckedRadioButtonId();
						            	 for(int i=0;i<OEHelper.project_task_all_stage_set.size();i++)
						            	 {
						            		 if(rb[i].getId()==selectedId)
						            		 {
						            			 changes_stage= rb[i].getText().toString();
						            		 }
						            	 }
						            }
						        });
							Button btnDisplay = (Button)dialog.findViewById(R.id.btnupdate);
				            btnDisplay.setOnClickListener(new OnClickListener() {
				         
				                    @Override
				                    public void onClick(View v) {
				         
				                    int ind=OEHelper.project_task_all_stage_set.indexOf(changes_stage);
				                    String stageid=OEHelper.project_task_stage_id.get(ind);
				                    
				                   // Toast.makeText(getActivity(), ""+stageid+"  name="+changes_stage, 40).show();
				                    	
				                    	OEHelper oehelper = new OEHelper(getActivity());
				                		OEValues val = new OEValues();

				                		val.put("stage_id",stageid);
				            			
				            			oehelper.updater_project_task_stage(val, Integer.parseInt(OEHelper.project_task_id.get(Integer.parseInt(txv5.getTag()+""))));
				            			//OEHelper.project_task_stage.remove(position);
				            			//OEHelper.project_task_stage.set(position, changes_stage);
				            			OEHelper oehelper1 = new OEHelper(getActivity());
				            			oehelper1.method_forproject_task_detail();
				            			oehelper.project_task_type();
				            			//call_method_for_refrese();
				            			
				            			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
				          			
				          			    fm1.popBackStack("projecttask_detail", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				          			    fm1.popBackStack();
				          			    
				            			projecttask_detail protask = new projecttask_detail();
				            			FragmentListener mFragment1 = (FragmentListener) getActivity();
				            			mFragment1.startMainFragment(protask, true);
				            			
				            			dialog.dismiss();
						}
								
					});
				            dialog.show();
							//============================================================================
							
						}
					});
							
					return mView;
				}

			 };
//arr end================================================================
						mListView.setAdapter(arr);
		} else {
			Toast.makeText(getActivity(), "Task not found", 30).show();
		}
		return rootView;
	}


	@Override
	public void onTimeChanged(int h, int m, int am_pm) {
	}

	@Override
	public void onDateChanged(Calendar c) {
	}
}