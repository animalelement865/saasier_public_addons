package com.openerp.addons.idea;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
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

public class project_issue extends BaseFragment{

	List<String> mItems = new ArrayList<String>();
	ListView mListView = null;
	//int selectedstageId=0;
	String changes_stage="";
	int position=0;
	int day,month,year;
	int hour,minute,ampm;
	int position1=0;
	int daychange,monthchange,yearchange;
	int hourchange,minutechange,ampmchange;
	
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

		View rootView = inflater.inflate(R.layout.project_issue_detail, container,
				false);

		getActivity().setTitle(R.string.label_issue_detail);
		
		OEHelper oehelper = new OEHelper(getActivity());
		oehelper.method_for_project_issue();
		oehelper.project_task_type();
		oehelper.call_method_res_user();
		
		MainActivity.global = 2;
		
		Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
		
		TextView productname1 = (TextView) rootView.findViewById(R.id.textView1);
		TextView productname2 = (TextView) rootView.findViewById(R.id.textView2);
		TextView productname3 = (TextView) rootView.findViewById(R.id.textView3);
		TextView productname4 = (TextView) rootView.findViewById(R.id.textView4);
		TextView productname6 = (TextView) rootView.findViewById(R.id.textView6);
		
		productname1.setTypeface(font,Typeface.BOLD);
		productname2.setTypeface(font,Typeface.BOLD);
		productname3.setTypeface(font,Typeface.BOLD);
		productname4.setTypeface(font,Typeface.BOLD);
		productname6.setTypeface(font,Typeface.BOLD);
		
		mListView = (ListView) rootView.findViewById(R.id.listforqr_equip);
		

		if (OEHelper.project_issue_name.size() != 0){
		
			mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.project_issue_first,
					OEHelper.project_issue_name) {
				
				public View getView(int position, View convertView,
						ViewGroup parent) {
					View mView = convertView;
					
					if (mView == null)
						mView = getActivity().getLayoutInflater().inflate(
								R.layout.project_issue_first, parent, false);
					TextView txv = (TextView) mView.findViewById(R.id.MO);
					Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
					txv.setTypeface(font);
					final TextView txv2 = (TextView) mView
							.findViewById(R.id.stage);
					txv2.setTypeface(font);
					
					TextView txv1 = (TextView) mView.findViewById(R.id.product);
					txv1.setTypeface(font);
					TextView txv3 = (TextView) mView.findViewById(R.id.partner_id1);
					txv3.setTypeface(font);
					txv3.setTag(position);
					
					
					final TextView txv5 = (TextView) mView.findViewById(R.id.date);
					txv5.setTypeface(font);
					txv5.setTag(position);
					
					txv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv.setTextColor(Color.rgb(84, 84, 84));
					txv.setText(""
							+ OEHelper.project_issue_name
									.get(position));
					txv1.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv1.setTextColor(Color.rgb(84, 84, 84));
					txv1.setText(""
							+ OEHelper.project_task_username
									.get(position));
					txv1.setTag(position);
					txv1.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
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
							TextView tv=(TextView) dialog.findViewById(R.id.stagetitle);
							tv.setText("Select User");
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
								   if(OEHelper.project_task_username.get(Integer.parseInt(""+txv2.getTag())).toLowerCase().contains(OEHelper.res_user_name.get(i).toLowerCase()))
								   {
									   radioSexGroup.check(i);
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
				                    
				                 				                    
				                    	OEHelper oehelper = new OEHelper(getActivity());
				                		OEValues val = new OEValues();
				                		
				                		val.put("user_id",Integer.parseInt(stageid));
				            			oehelper.updater_project_issue_stage(val, Integer.parseInt(OEHelper.project_issue_id.get(Integer.parseInt(""+txv2.getTag()))));
				            			
				            			oehelper.method_for_project_issue();
				            			oehelper.project_task_type();
				            			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
					          			
				          			    fm1.popBackStack("project_issue", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				          			    fm1.popBackStack();
				          			    
				            			project_issue protask = new project_issue();
				            			FragmentListener mFragment1 = (FragmentListener) getActivity();
				            			mFragment1.startMainFragment(protask, true);
				            			
				            			dialog.dismiss();
				                    }
				         
				                });
				                dialog.show();
							
						}
					});
					txv2.setTextColor(Color.rgb(84, 84, 84));
					txv2.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv2.setText(""
							+ OEHelper.project_task_stage
									.get(position));
					txv2.setTag(position);
					txv2.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							
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
								   if(OEHelper.project_task_all_stage_set.get(i).equals(OEHelper.project_task_stage.get(Integer.parseInt(""+txv2.getTag()))))
								   {
									   radioSexGroup.check(i);
									   
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
				                    
				                    
				                    	OEHelper oehelper = new OEHelper(getActivity());
				                		OEValues val = new OEValues();
				                		
				                		val.put("stage_id",Integer.parseInt(stageid));
				                		
				            			oehelper.updater_project_issue_stage(val, Integer.parseInt(OEHelper.project_issue_id.get(Integer.parseInt(""+txv2.getTag()))));
				            			
				            			oehelper.method_for_project_issue();
				            			oehelper.project_task_type();
				            		
				            			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
					          			
				          			    fm1.popBackStack("project_issue", FragmentManager.POP_BACK_STACK_INCLUSIVE);
				          			    fm1.popBackStack();
				          			    
				            			project_issue protask = new project_issue();
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
					txv3.setText("" + OEHelper.project_issue_partner_id.get(position));
									
					txv5.setTextColor(Color.rgb(84, 84, 84));
					txv5.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
					txv5.setText("" + OEHelper.project_task_startdate.get(position));
					txv5.setTag(position);
					txv5.setOnClickListener(new OnClickListener() {
											
								@Override
								public void onClick(View arg0) {
									
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
										    		Calendar cal = Calendar.getInstance();  
										    		
										    		val.put("date",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+5);
													
										    		oehelper.updater_project_issue_stage(val, Integer.parseInt(OEHelper.project_issue_id.get(position1)));
									    			
									    			oehelper.method_for_project_issue();
									    			oehelper.project_task_type();
									    			
									    			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
									      			
									  			    fm1.popBackStack("project_issue", FragmentManager.POP_BACK_STACK_INCLUSIVE);
									  			    fm1.popBackStack();
									  			    
									    			project_issue protask = new project_issue();
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
										    		
										    		val.put("date",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+5);
													
										    		oehelper.updater_project_issue_stage(val, Integer.parseInt(OEHelper.project_issue_id.get(position1)));
									    			
									    			oehelper.method_for_project_issue();
									    			oehelper.project_task_type();
									    			
									    			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
									      			
									  			    fm1.popBackStack("project_issue", FragmentManager.POP_BACK_STACK_INCLUSIVE);
									  			    fm1.popBackStack();
									  			    
									    			project_issue protask = new project_issue();
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
									    		
									    		val.put("date",yearchange+"/"+(monthchange)+"/"+daychange+" "+hourchange+":"+minutechange+":"+5);
												
									    		oehelper.updater_project_issue_stage(val, Integer.parseInt(OEHelper.project_issue_id.get(position1)));
								    			
								    			oehelper.method_for_project_issue();
								    			oehelper.project_task_type();
								    			
								    			android.support.v4.app.FragmentManager fm1 = getActivity().getSupportFragmentManager();
								      			
								  			    fm1.popBackStack("project_issue", FragmentManager.POP_BACK_STACK_INCLUSIVE);
								  			    fm1.popBackStack();
								  			    
								    			project_issue protask = new project_issue();
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
							});
						
					return mView;
				}

			});
		}
		else {
			Toast.makeText(getActivity(), "Issue not found", 30).show();
		}
		return rootView;
	}

}