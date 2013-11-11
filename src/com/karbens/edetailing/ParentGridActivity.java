package com.karbens.edetailing;

import java.io.File;
import java.util.ArrayList;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import com.karbens.application.EdetailingApplication;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;

public class ParentGridActivity extends Activity{

	LayoutInflater layoutInflater = null;
	Parent mParent = null;
	ArrayList<Parent> mParentArr = null;
     
    int contentIndex = 0;
	int brandIndex = 0;
	boolean isEditMode = false;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.parent_grid);
		
		Bundle b = getIntent().getExtras();
		if(b!=null)
		{
			brandIndex = b.getInt("brandindex");
			contentIndex = b.getInt("contentindex");
		}
		
		GridView gridView = (GridView) findViewById(R.id.parent_View);
		
		// Instance of ImageAdapter Class
		gridView.setAdapter(new ImageAdapter(this));
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		//new MenuInflater(this).inflate(R.menu.main, menu);
		
		MenuInflater inflater = getMenuInflater();
		 inflater.inflate(R.menu.main,menu);
		
		return super.onCreateOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch(item.getItemId()) 
        {
        	case R.id.ViewPresentation:
        		//Refresh list
        	
        		goToPresentation();
        		
        	
        		
        	case R.id.EditParents:
        		//Refresh list
        		
        		isEditMode = true;
        		
        		//item.setVisible(false);
        		
        		//MenuItem saveItem = (MenuItem) findViewById(R.id.SaveParents);
        		//saveItem.setVisible(true);
        		
        		
        		
        	case R.id.SaveParents:
        		//View Presentation
        		isEditMode = false;
        		
        		//item.setVisible(false);
        		
        		//MenuItem editItem = (MenuItem) findViewById(R.id.EditParents);
        		//editItem.setVisible(true);
        		
        		saveParents();
        		
        		
        	default:
            	return super.onOptionsItemSelected(item);
        }
        
		
		//return super.onOptionsItemSelected(item);
	}
	

	private void goToPresentation() 
	{
		// TODO Auto-generated method stub
		
		Intent i = new Intent(ParentGridActivity.this, ParentActivity.class);
		
		i.putExtra("brandindex", brandIndex);
		i.putExtra("contentindex", contentIndex);
		
		startActivity(i);
		
	}

	private void saveParents() 
	{
		// TODO Auto-generated method stub
		
		mParentArr = new ArrayList<Parent>();
		
		for(int i=0;i<EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().size();i++)
		{
			mParent = EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(i);
			
			if(mParent.getIsDisabled() == false)
			{
				mParentArr.add(mParent);
			}
			
		}
		
		EdetailingApplication.mParentArr = mParentArr;
	}


	public class ImageAdapter extends BaseAdapter {
		
		private Context mContext;

		// Constructor
		public ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() 
		{
			if(EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().size()!=0)  
			{
				return EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().size();  
			}
			else
			{
				return 0;
			}
			
		}

		@Override
		public Object getItem(int position) 
		{
			return EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(position);  
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			final int index = position;
        	
			//LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			layoutInflater = getLayoutInflater();
        	View layout = layoutInflater.inflate(R.layout.parent_grid_item,null);
        	
        	final ImageView imgView = (ImageView) layout.findViewById(R.id.parent_item_image);
        	
        	//imgView.setBackgroundResource((int)EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(position).getmSlideBgPath());
        	
        	System.out.println("local Path : "+EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(position).getmSlideBgPath());
        
        	File imgFile = new  File(EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(position).getmSlideBgPath());
        	
        	if(imgFile.exists())
        	{

        	    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
        	    imgView.setImageBitmap(myBitmap);

        	}
        	
        	
        	imgView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					// Sending image id to FullScreenActivity
							
					if(isEditMode == true)
					{
						mParent = EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(index);
						
						if(mParent.getIsDisabled() == true) // disabled
						{
							
							EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(index).setIsDisabled(false);
							imgView.setAlpha(1f);
						}
						else // enabled
						{
							EdetailingApplication.mBrandArr.get(brandIndex).getmContentArr().get(contentIndex).getmParentArr().get(index).setIsDisabled(true);
							imgView.setAlpha(0.5f);
						}
					}
				}
			});
        	
        	return layout;
			
		}

	}
	
}
