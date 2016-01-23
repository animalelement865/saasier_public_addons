package com.openerp.addons.idea;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.openerp.R;
import com.openerp.orm.OEHelper;
import com.openerp.support.BaseFragment;
import com.openerp.support.fragment.FragmentListener;
import com.openerp.util.drawer.DrawerItem;

public class Inventorymain extends BaseFragment implements OnItemClickListener {

	View mView;
	ListView mListView = null;
	TextView tv;
	Button movesock;
	private Handler myHandler = new Handler();
	private Runnable runnable;
	Thread tread;
	private ProgressBar progressBar;
	int myProgress = 0;
	int progressStatus = 0;
	OEHelper oehelper;

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
		mView = inflater.inflate(R.layout.inventory, container, false);

		getActivity().setTitle(R.string.label_inventory);
		
		
		
		progressBar = (ProgressBar) mView
				.findViewById(R.id.progressBarforconsumeproductlist);
		
		mListView = (ListView) mView.findViewById(R.id.listview);
		tv = (TextView) mView.findViewById(R.id.tvforinventory);
	
		movesock = (Button) mView.findViewById(R.id.movestochforinventory);
		movesock.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				//move_stock_by_location movestock = new move_stock_by_location();
				move_stock_by_location2 movestock = new move_stock_by_location2();
				FragmentListener frag = (FragmentListener) getActivity();
				frag.startDetailFragment(movestock);
			}
		});
		
		
		if(Product_Detail.check_inventory_back_or_not==1)
		{
			progressStatus=1;
		}
		
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
						progressBar.setVisibility(8);
					
						mListView.setVisibility(View.VISIBLE);
						
							
						
							if (OEHelper.picking_id_of_stock_move.size() != 0) {
								if (OEHelper.picking_id_of_stock_move.size() < 5) {
									tv.setVisibility(View.VISIBLE);
								}
								
								
								mListView.setAdapter(new ArrayAdapter<String>(getActivity(),
										R.layout.fragment_idea_list_item,
										OEHelper.picking_id_of_stock_move) {

									public View getView(int position, View convertView,
											ViewGroup parent) {
										View mView = convertView;
										if (mView == null)
											mView = getActivity().getLayoutInflater()
													.inflate(R.layout.fragment_idea_list_item,
															parent, false);
										TextView txv = (TextView) mView
												.findViewById(R.id.txvIdeaName);
										String pickdetail = OEHelper.picking_id_of_stock_move
												.get(position);
										String pick_sub = "";
										String pick_sub1 = "";
										String pick_sub2 = "";
									
										pick_sub = pickdetail;
										int index = pickdetail.indexOf('"');

										if (index > 0) {
											pick_sub = pickdetail.substring(index + 1);
											pick_sub = pick_sub.substring(0, pick_sub.length() - 2);
										
											if (pickdetail.contains("IN")
													|| pickdetail.contains("OUT")) {
												int indext1 = pick_sub.indexOf('\\');
												if (indext1 > 0) {
												
													pick_sub1 = pick_sub.substring(0, indext1);
													pick_sub2 = pick_sub.substring(indext1 + 2);
													pick_sub = pick_sub1 + "  " + pick_sub2;
												}
											} else {
										
												pick_sub = "Manual Move";
											}
											// pick_sub=pick_sub.replace('\\',' ');
										}
										else
										{
											pick_sub = "Manual Move";
										}
										
										txv.setText(" "
												+ pick_sub
												+ "   QTY="
												+ OEHelper.product_pty_separate_stock_move
														.get(position));
										return mView;
									}

								});

								
								// String s = null;
								// s.split("[,]");

							} else {
							//	Toast.makeText(getActivity(), "No Any Record", 0).show();
								//tv.setVisibility(View.GONE);
								
							}
						
						
							
//						else
//						{
//							txv.setVisibility(View.VISIBLE);
//						}
						
						progressStatus = 0;

					}
				});

			}

			/* Do some task */
			private int performTask() {
				OEHelper oehelper1 = new OEHelper(getActivity());
				oehelper1.availablestock_stock_move();
				return ++myProgress;
			}
		};

		// String k;

		myProgress = 0;
		mListView.setVisibility(View.GONE);
		progressBar.setVisibility(0); /* 0 – visible 4 - invisible 8 - Gone */

		tread = new Thread(runnable);
		// new Thread(runnable).start();
		tread.start();
		
		mListView.setOnItemClickListener(this);
		
		
		

		// Toast.makeText(getActivity(), "inventory called",30).show();
		return mView;
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

	}
}
