package com.openerp.addons.idea;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class project_name_dash_board extends BaseFragment  {

	//ListView mListView = null;
	int position1;
	GridView grid;
	String[] web;
	int[] imageId;
	
	
	//databaseHelper dbhelper=new databaseHelper(getActivity(), null, null, 1);
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
//		View rootView = inflater.inflate(R.layout.partner_list, container,
//				false);
		View rootView = inflater.inflate(R.layout.gridviewforproject, container,
				false);
		MainActivity.global = 2;
		getActivity().setTitle(R.string.label_project_name);
		
		if (db().isEmptyTable()) {
			IdeaDemoRecords rec = new IdeaDemoRecords(getActivity());
			rec.createDemoRecords();
		}
		
		OEHelper oehelper = new OEHelper(getActivity());
		oehelper.method_for_project_project();
		oehelper.method_for_project_task();
		oehelper.method_for_project_issue_count();
		
		web=new String[OEHelper.project_name.size()];
		imageId =new int[OEHelper.project_name.size()];
		
		for(int i=0;i<OEHelper.project_name.size();i++)
		{
			web[i]=OEHelper.project_name.get(i);
			imageId[i]=R.drawable.project_mgt;
			
		}
		CustomGrid_project adapter = new CustomGrid_project(getActivity(), web, imageId,OEHelper.project_task_count,OEHelper.project_issue_count);
		grid = (GridView) rootView.findViewById(R.id.grid);
		grid.setAdapter(adapter);

		grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				 position1=position;
				    OEHelper.selected_project_id=OEHelper.project_ids.get(position1);
				   // Toast.makeText(getActivity(), ""+OEHelper.selected_project_id, 50).show();
					AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
					//builder1.setIcon(R.drawable.project_mgt);
					builder1.setTitle("PROJECT DETAIL");
					builder1.setMessage("Click button for detail");

					builder1.setPositiveButton("Issue",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							
							project_issue ptask=new project_issue();
							FragmentListener frag = (FragmentListener) getActivity();
							frag.startDetailFragment(ptask);
							
							dialog.dismiss();
						
						}
					});
					builder1.setNegativeButton("Task",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								
									projecttask_detail ptask=new projecttask_detail();
									FragmentListener frag = (FragmentListener) getActivity();
									frag.startDetailFragment(ptask);
									//Toast.makeText(getActivity(), "OPPPs..Not Found Any Issue Yet", 40).show();
									dialog.dismiss();
									
								}
							});
					
					
					AlertDialog alert11 = builder1.create();
					alert11.setCanceledOnTouchOutside(true);
					alert11.show();
			}
		});
		
		return rootView;
	}

	
	public Object databaseHelper(Context context) {
		return new IdeaDBHelper(context);
	}
	
	
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		
//	    position1=position;
//	    OEHelper.selected_project_id=OEHelper.project_ids.get(position1);
//	   // Toast.makeText(getActivity(), ""+OEHelper.selected_project_id, 50).show();
//		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
//		//builder1.setIcon(R.drawable.project_mgt);
//		builder1.setTitle("PROJECT DETAIL");
//		builder1.setMessage("Click button for detail");
//
//		builder1.setPositiveButton("Issue",new DialogInterface.OnClickListener() {
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				
//				project_issue ptask=new project_issue();
//				FragmentListener frag = (FragmentListener) getActivity();
//				frag.startDetailFragment(ptask);
//				
//				dialog.dismiss();
//			
//			}
//		});
//		builder1.setNegativeButton("Task",
//				new DialogInterface.OnClickListener() {
//					@Override
//					public void onClick(DialogInterface dialog, int which) {
//					
//						projecttask_detail ptask=new projecttask_detail();
//						FragmentListener frag = (FragmentListener) getActivity();
//						frag.startDetailFragment(ptask);
//						//Toast.makeText(getActivity(), "OPPPs..Not Found Any Issue Yet", 40).show();
//						dialog.dismiss();
//						
//					}
//				});
//		
//		
//		AlertDialog alert11 = builder1.create();
//		alert11.setCanceledOnTouchOutside(true);
//		alert11.show();
//	}

	@Override
	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
}
