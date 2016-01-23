package com.openerp.addons.idea;

import java.util.List;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import com.openerp.MainActivity;
import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class project_name extends BaseFragment implements OnItemClickListener {


	ListView mListView = null;
	int position1;
	
	//databaseHelper dbhelper=new databaseHelper(getActivity(), null, null, 1);
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View rootView = inflater.inflate(R.layout.partner_list, container,
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
		
		mListView = (ListView) rootView.findViewById(R.id.partner_listview);
		
		if(OEHelper.project_name.size()!=0)
		{
			
			mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
					R.layout.fragment_idea_list_item, OEHelper.project_name) {
				
				public View getView(int position, View convertView, ViewGroup parent) {
					View mView = convertView;
					if (mView == null)
						mView = getActivity().getLayoutInflater().inflate(
								R.layout.fragment_idea_list_item, parent, false);
					TextView txv = (TextView) mView.findViewById(R.id.txvIdeaName);
					
					Typeface font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Georgia.ttf");
					txv.setTypeface(font,Typeface.BOLD);
					
					String sourceString = "<b>"+OEHelper.project_name.get(position)+"</b>"+"<br>"+"Task="+OEHelper.project_task_count.get(position);
					String name5=""+Html.fromHtml(sourceString);
					txv.setText(name5);
					
					return mView;
				}
	
			});
		}
		else
		{
			Toast.makeText(getActivity(), "No any project available", 40).show();
		}
		mListView.setOnItemClickListener(this);
		return rootView;
	}

	
	public Object databaseHelper(Context context) {
		return new IdeaDBHelper(context);
	}
	
	
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	    position1=position;
	    OEHelper.selected_project_id=OEHelper.project_ids.get(position1);
		AlertDialog.Builder builder1 = new AlertDialog.Builder(getActivity());
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
						dialog.dismiss();
						
					}
				});
		
		AlertDialog alert11 = builder1.create();
		alert11.setCanceledOnTouchOutside(true);
		alert11.show();
	}

	@Override
	public List<DrawerItem> drawerMenus(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
}
