package com.karbens.edetailing;


import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

import com.karbens.application.EdetailingApplication;
import com.karbens.listeners.ContentDownloadListener;
import com.karbens.listeners.DownloadProgressListener;
import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;
import com.karbens.parser.ContentParser;
import com.karbens.utility.DataManager;
import com.karbens.utility.Database;
import com.karbens.utility.HttpUtil;
import com.karbens.utility.DataManager.DownloadChild;
import com.karbens.utility.DataManager.DownloadParent;

import android.opengl.Visibility;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

public class ContentManagerActivity extends Activity implements ContentDownloadListener,DownloadProgressListener{
	
	LinearLayout gridlin = null;
	Brand mBrand = null;
	Content mContent = null;
	Parent mParent = null;
	Child mChild = null;
	LayoutInflater layoutInflater = null;
	DataManager mDataManager = null;
	ContentParser mParser = null;
	ArrayList<Parent> mParentArr = new ArrayList<Parent>();
	ArrayList<Content> mContentArr = new ArrayList<Content>(); 
    ArrayList<Child> mChildArr = null;
    ContentDownloadListener mListener = null;
    ImageAdapter mAdapter = null;
    
    //for progress bar
    private static int myProgressWaited=0;
    private Runnable myThread ;
	//private int progressStatus=0;
	private Handler myHandler=new Handler();
	boolean proStatus=false;
	
	private int counter=0;
	private long fileSize = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_view);
		
		GridView gridView = (GridView) findViewById(R.id.grid_view);
		
		if(mDataManager == null)
		{
			mDataManager=new DataManager(this);
		}
		
		//progressbar
	
		// Instance of ImageAdapter Class
		mAdapter = new ImageAdapter(this);
		gridView.setAdapter(mAdapter);

		
	}
	

	public class ImageAdapter extends BaseAdapter {
		
		private Context mContext;

		// Constructor
		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			if(EdetailingApplication.mBrandArr==null)
			{
				return 0;
			}
			else
			{
				return EdetailingApplication.mBrandArr.get(0).getmContentArr().size();  //mThumbIds.length;
			}
			
		}

		@Override
		public Object getItem(int position) {
			return EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position);  //mThumbIds[position];
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			
			final int index = position;
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater = getLayoutInflater();
			final ProgressBar progressBar;
		
        	View layout = layoutInflater.inflate(R.layout.grid_item,null);
        	ImageView imgView = (ImageView) layout.findViewById(R.id.grid_item_image);
        	imgView.setBackgroundResource(R.drawable.content_frame);
        	
        	final Button pauseButton = (Button) layout.findViewById(R.id.pauseBtn);
        	myProgressWaited=0;
	        progressBar=(ProgressBar)layout.findViewById(R.id.player_exp_bar);
	        
	        int contentSize = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getContentSize();
	        
	        int progressValue = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getProgressValue();
	        
	        progressBar.setProgress(progressValue);
	        
	        System.out.println("progressValue :"+progressValue);

	        System.out.println("contentSize :"+contentSize);
	        
			progressBar.setMax(Math.abs(contentSize));
			
	     // int downloadCounter = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getProgressCount();
	        
        	Button downloadbutton = (Button) layout.findViewById(R.id.dwnBtn);
        	downloadbutton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
			        progressBar.setVisibility(View.VISIBLE);
			        pauseButton.setVisibility(View.VISIBLE);
			    	//Fresh Download
			        counter = mDataManager.downloadData(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index),index,0);//calls downloadData with index

			     
				}
			});
        	
        	pauseButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					progressBar.setVisibility(View.INVISIBLE);
			        pauseButton.setVisibility(View.INVISIBLE);
					Toast.makeText(mContext, "Cancel download", Toast.LENGTH_LONG).show();
					
					
					// Cancel all current downloads for this content
					for(int i = 0;i < EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getmDwnContentArr().size(); i++)
					{
						EdetailingApplication.mBrandArr.get(0).getmContentArr().get(index).getmDwnContentArr().get(i).cancel(true);
					}
					
					// Now insert whatever has been downloaded
					
					
				}
			});
 
        	
        	
        	TextView contentLbl = (TextView) layout.findViewById(R.id.grid_item_label);
        	
        	System.out.println(""+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getmName());
        	contentLbl.setText(""+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(position).getmName());
        	
        	imgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// Sending image id to FullScreenActivity
					Intent i = new Intent(ContentManagerActivity.this, ParentGridActivity.class);

					i.putExtra("brandindex", 0);
					i.putExtra("contentindex", index);
					
					startActivity(i);
				}
			});
        	
        	return layout;
			
		}

	}


	public int downloadProgress() {
		 
		while (fileSize <= 1000000) {
 
			fileSize++;
 
			if (fileSize == 100000) {
				return 10;
			} else if (fileSize == 200000) {
				return 20;
			} else if (fileSize == 300000) {
				return 30;
			}
			// ...add your own
 
		}
 
		return 100;
 
	}

	 
	@Override
	public void allDownloadComplete() {

	}


	@Override
	public void parsingComplete() {
		mAdapter.notifyDataSetChanged();
	}


	@Override
	public void downloadFinished() {
		mAdapter.notifyDataSetChanged();
	}


	@Override
	public void updateDownloadProgress(int contentId, long totalread) {
		
		
		System.out.println("Total Read :"+totalread);
		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentId).setProgressValue(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentId).getProgressValue()+(int)totalread);
		
		
		mAdapter.notifyDataSetChanged();
	}

}