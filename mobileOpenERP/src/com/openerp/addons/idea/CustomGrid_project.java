package com.openerp.addons.idea;

import java.util.ArrayList;
import java.util.List;

import com.openerp.R;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid_project extends BaseAdapter{
	  private Context mContext;
	  private final String[] web;
	  private final int[] Imageid; 
	  List<Integer> project_task_count=new ArrayList<Integer>();
	  List<Integer> project_issue_count=new ArrayList<Integer>();

	    public CustomGrid_project(Context c,String[] web,int[] Imageid,List<Integer> project_task_count,List<Integer> project_issue_count)
	    {
	        mContext = c;
	        this.Imageid = Imageid;
	        this.web = web;
	        this.project_task_count=project_task_count;
	        this.project_issue_count=project_issue_count;
	    }

		
		public int getCount() {
			// TODO Auto-generated method stub
			return web.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View grid;
			LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
	        if (convertView == null) {  
	        	
	        	grid = new View(mContext);
				grid = inflater.inflate(R.layout.gridview_first_for_project, null);
				
				Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Georgia.ttf");
				
	        	TextView name = (TextView) grid.findViewById(R.id.grid_text);
	        	ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
	        	TextView task = (TextView) grid.findViewById(R.id.task);
	        	TextView issue = (TextView) grid.findViewById(R.id.issue);
	        	
	        	name.setTypeface(font,Typeface.BOLD);
	        	
	        	task.setTypeface(font);
	        	issue.setTypeface(font);
	        
	        	name.setText(web[position]);
	        	imageView.setImageResource(Imageid[position]);
	        	task.setText("Task: "+project_task_count.get(position)+"");
	        	issue.setText("Issue: "+project_issue_count.get(position));
	        	
	        	
	        } else {
	        	grid = (View) convertView;
	        }
			
			return grid;
		}
}
