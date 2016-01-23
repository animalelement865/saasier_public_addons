package com.openerp.addons.idea;

import com.openerp.R;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomGrid extends BaseAdapter{
	  private Context mContext;
	  private final String[] web;
	  private final int[] Imageid; 

	    public CustomGrid(Context c,String[] web,int[] Imageid ) {
	        mContext = c;
	        this.Imageid = Imageid;
	        this.web = web;
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
				grid = inflater.inflate(R.layout.grid_single, null);
	        	TextView textView = (TextView) grid.findViewById(R.id.grid_text);
	        	ImageView imageView = (ImageView)grid.findViewById(R.id.grid_image);
	        	Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/Georgia.ttf");
	        	textView.setTypeface(font,Typeface.BOLD);
	        	textView.setText(web[position]);
	        	imageView.setImageResource(Imageid[position]);
	        } else {
	        	grid = (View) convertView;
	        }
			
			return grid;
		}
}
