package com.karbens.utility;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.xml.datatype.Duration;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Environment;
import android.widget.Toast;

import com.karbens.application.EdetailingApplication;
import com.karbens.listeners.ContentDownloadListener;
import com.karbens.model.Brand;
import com.karbens.model.Child;
import com.karbens.model.Content;
import com.karbens.model.Parent;
import com.karbens.parser.ContentParser;

public class DataManager {

	GetLatestContents getLatestContents = null;
	Context mContext= null;
	static Parent aParent = null;
	static Child aChild = null;
	boolean isConnected = false;
	Database db;
	// downloadContent dwnContent  = null;
	//DownloadParent dwnParent = null;
	//DownloadChild dwnChild = null;
	DownloadContent dwnContent = null;
	//String localDwnPth="";
	//int contentIndex=0;
	ContentDownloadListener mListener = null;
	//int counterDownloads =0;
	int parentSize =0;
	
	public DataManager(ContentDownloadListener listener)
	{
		mListener = listener;
		mContext = (Context) listener;
		isConnected = hasConnectivity();
		
		db = new Database(mContext);
		
		if(isConnected)
		{
			//check for updates available
			getLatestContents = new GetLatestContents();
			getLatestContents.execute(""+Constants.XML_URL+"?username=strides&password=strides123&date=&cid=");
			System.out.println(""+Constants.XML_URL+"?username=strides&password=strides123&date=&cid=");
		}
		else
		{
			//show content from sqlite
			
		
			// Load array list from database  //
			EdetailingApplication.mBrandArr = loadFromDb();
			
			//Toast.makeText(mContext, "No Internet Connectivity", Toast.LENGTH_SHORT).show();
		}
		
	}
	

	 public boolean hasConnectivity() 
	 {
	          
	     ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

	     if (connectivityManager==null) 
	     {

	        return false;
	     }

	     NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

	     if (networkInfo!=null && networkInfo.isAvailable() && networkInfo.isConnected()) 
	     {
	        return true;
	     }
	     else
	     {
	        return false;
	     }
	  }
	 
	 
	 public ArrayList<Brand> loadFromDb()
	 {
		 
		 ArrayList<Brand> brandArr = new ArrayList<Brand>();
		 
		 brandArr = (ArrayList<Brand>) db.getAllBrands();
		 
		 for(int i=0; i<brandArr.size(); i++)
		 {
			 Brand aBrand = brandArr.get(i);
			 
			 ArrayList<Content> contentArr  = new ArrayList<Content>();
			 
			 contentArr = (ArrayList<Content>) db.getContents(aBrand.getmId()); 
			 
			 for(int j=0; j<contentArr.size(); j++)
			 {
				Content aContent = contentArr.get(j);
				
				 ArrayList<Parent> parentArr = new ArrayList<Parent>();
				 
				 parentArr = (ArrayList<Parent>) db.getParents(aContent.getmId());
				 
				 for(int k=0; k<parentArr.size(); k++)
				 {
					 Parent aParent = parentArr.get(k);
					 
					 ArrayList<Child> childArr = new ArrayList<Child>();
					 
					 childArr = (ArrayList<Child>) db.getChilds(aParent.getmId());
				 }
				 
			 }
			 
		 }
		 
		 return brandArr;
	 }


	public class GetLatestContents extends AsyncTask<String, Void, ArrayList<Brand>>
	{

		
		@Override
		protected ArrayList<Brand> doInBackground(String... url) {
			String xml = null;

			try {
				// defaultHttpClient
				
				DefaultHttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(url[0]);

				HttpResponse httpResponse = httpClient.execute(httpPost);
				HttpEntity httpEntity = httpResponse.getEntity();
				xml = EntityUtils.toString(httpEntity);

				ContentParser parser = new ContentParser();
				EdetailingApplication.mBrandArr =  parser.getBranList(xml);
				
			
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				System.out.println(""+e.toString());
			
			}
			//return xml;
			
			
			for(int i =0; i<EdetailingApplication.mBrandArr.size(); i ++)
			{
				System.out.println("Brand :"+EdetailingApplication.mBrandArr.get(i).getmName());
				
				for(int j=0; j<EdetailingApplication.mBrandArr.get(i).getmContentArr().size(); j++)
				{
					System.out.println("Content :"+EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmName());
					
					long aContentId  =	EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmId();
					Content bContent =	db.getContent(aContentId);
				
					if(bContent!=null)
					{
						// Content already exists in the DB
						 System.out.println("Content already exists in the DB");
						  
						 EdetailingApplication.mBrandArr.get(i).getmContentArr().set(j, bContent);// Replace local content in place of parsed content
					}
					
					
					for(int k=0; k<EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().size(); k++)
					{
						System.out.println("Parent :"+EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmName());
						
						for(int l=0; l < EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().size(); l++)
						{
							System.out.println("Child :"+EdetailingApplication.mBrandArr.get(i).getmContentArr().get(j).getmParentArr().get(k).getmChildArr().get(l).getmName());
						}
					}
				}
				
			}
		
			//System.out.println("Strin of xml :"+xml);
			return EdetailingApplication.mBrandArr;
		}
		
		
		@Override
		protected void onPostExecute(ArrayList<Brand> result) {
				
			mListener.parsingComplete();
			
			super.onPostExecute(result);
		}
		 
		
	}


	public int downloadData(Content content,int contIndex,int operationType)  
	{
		
		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contIndex).setDownloadSize(0);
		
		ArrayList<DownloadContent> dwnCntArr = new ArrayList<DownloadContent> ();
		
		
		for (int l = 0; l < content.getmParentArr().size(); l++) 
		{
			final int parentIndex = l; // parent index
			aParent = content.getmParentArr().get(l);
			parentSize = content.getmParentArr().size();
			
			if(aParent.getmSlideBgPath() == "")
			{
				dwnContent = new DownloadContent(contIndex, parentIndex, 0, 0, content,operationType);
				String fileName = aParent.getmContentUrl().substring(aParent.getmContentUrl().lastIndexOf('/')+1, aParent.getmContentUrl().length());
				dwnContent.execute(aParent.getmContentUrl(),fileName,content.getmName()+"/"+aParent.getmName());
				dwnCntArr.add(dwnContent);
				
				// Get download size
				EdetailingApplication.mBrandArr
				.get(0)
				.getmContentArr()
				.get(contIndex)
				.setDownloadSize(
						EdetailingApplication.mBrandArr.get(0)
								.getmContentArr().get(contIndex)
								.getDownloadSize()
								+ 1);
			}
			
			
			
			for(int i=0;i<aParent.getmChildArr().size();i++)
			{
				
				final int childIndex = i; // Child Index
				aChild =  aParent.getmChildArr().get(i);
				parentSize= parentSize+aParent.getmChildArr().size();
				
				if(aChild.getmType()==2 || (aChild.getmType()==3)) //image or video or reference button
				{
					
					if(aChild.getmFilePath() == "")
					{
						dwnContent = new DownloadContent(contIndex, parentIndex, childIndex, 1, content,operationType);
						String childFileName = aChild.getmContentUrl().substring(aChild.getmContentUrl().lastIndexOf('/')+1, aChild.getmContentUrl().length());
						String childFolder=content.getmName()+"/"+aParent.getmName();
						//String childFileName = aParent.getmContentUrl().substring(aParent.getmContentUrl().lastIndexOf('/')+1, aParent.getmContentUrl().length());
						
						dwnContent.execute(aChild.getmContentUrl(),childFileName,childFolder);
						
						dwnCntArr.add(dwnContent);
						
						
						
						// Get download size
						EdetailingApplication.mBrandArr
						.get(0)
						.getmContentArr()
						.get(contIndex)
						.setDownloadSize(
								EdetailingApplication.mBrandArr.get(0)
										.getmContentArr().get(contIndex)
										.getDownloadSize()
										+ 1);
						
					}
					
					
					if(operationType == 0)
					{
						// Get download content size
						EdetailingApplication.mBrandArr
						.get(0)
						.getmContentArr()
						.get(contIndex)
						.setContentSize(
								EdetailingApplication.mBrandArr.get(0)
										.getmContentArr().get(contIndex)
										.getContentSize()
										+ 1);
					}
					
					
				}
				
			}
			
			if(operationType == 0)
			{
				// Get download content size
				EdetailingApplication.mBrandArr
				.get(0)
				.getmContentArr()
				.get(contIndex)
				.setContentSize(
						EdetailingApplication.mBrandArr.get(0)
								.getmContentArr().get(contIndex)
								.getContentSize()
								+ 1);
			}
			else if(operationType == 1)
			{
				// Reset Progress value to 0
				EdetailingApplication.mBrandArr
				.get(0)
				.getmContentArr()
				.get(contIndex)
				.setProgressValue(0);
			}
			
		}
		
		
		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contIndex).setmDwnContentArr(dwnCntArr);
		dwnCntArr = null;
		
		//totCount=counterDownloads;
		return 0;
		
		
	}
	
	public class DownloadContent extends AsyncTask<String, Void, String>
	{
		int pIndex = 0;
		int chIndex = 0;
		int mType = 0;
		Content aContent = null;
		int contentIndex = 0;
		String localDwnPth = "";
		int operationType = 0; // Fresh Download
		
		public DownloadContent(int cntIndex,int pPosition,int chPosition,int type,Content content,int opType) // index - position,type - Parent/Child
		{
			pIndex = pPosition; // Parent Index
			chIndex = chPosition; // Child Index
			mType = type;
			aContent = content;
			contentIndex = cntIndex;
			operationType = opType;
			
		}

		@Override
		protected String doInBackground(String... params) {

			String downloadUrl = params[0];
			String fileName = params[1];
			String folderName = params[2];
			System.out.println("URL :"+downloadUrl);
			//System.out.println("FOLDER NAME :"+folderName);
			//System.out.println("FILE NAME :"+fileName);
			
			localDwnPth = HttpUtil.download(contentIndex,downloadUrl, fileName, folderName,operationType);
			
			/*if(localDwnPth!=null)
			{
				counterDownloads=counterDownloads+1;
				System.out.println("counter: "+counterDownloads);
			}*/
			return localDwnPth;
		}
		
	    @Override
	    protected void onCancelled() {
	    	// TODO Auto-generated method stub
	    	EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadStatus(2);
	    	
			String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
			EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setLastDownloadDate(currentDateTimeString);
	    	
	    	EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadCancelCount(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getDownloadCancelCount()+1);
	    	int cancelCount = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getDownloadCancelCount();
	    	int downloadedCount = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getProgressValue();
	    	
	    	if((cancelCount + downloadedCount) == EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getDownloadSize())
	    	{
	    		EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadedSize(downloadedCount);
	    		
	    		insertContentToDB(contentIndex);
	    	}
	    	
	    	System.out.println("Async Cancelled..");
	    	super.onCancelled();
	    }
	    
	    
		@Override
		protected void onPostExecute(String result) {
			
			//Update the data structure with the local file path
			
			if(mType==0) // Parent
			{
				//System.out.println("Path to update :"+result);
				
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).setmSlideBgPath(result);
				System.out.println("Updated to Parent Data Structure :"+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).getmSlideBgPath());
			}
			else // Child
			{
				//System.out.println("Path to update :"+result);
				
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).getmChildArr().get(chIndex).setmFilePath(result);
				System.out.println("Updated to Child Data Structure :"+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(pIndex).getmChildArr().get(chIndex).getmFilePath());
				
					
			}
			
			
			EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setProgressValue(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getProgressValue()+1);
			
			System.out.println("Incremented Progress :"+EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getProgressValue());
			
			
			if(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getDownloadSize() == EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getProgressValue())
			{

				String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setLastDownloadDate(currentDateTimeString);
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadStatus(1);
				
				EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).setDownloadedSize(EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getContentSize());
				
				insertContentToDB(contentIndex);
			}
	
			mListener.downloadFinished();
			
			super.onPostExecute(result);
		}
	}
	

	
	private void insertContentToDB(int contentIndex) 
	{
		
		db = new Database(mContext);

		//for (int i = 0; i < EdetailingApplication.mBrandArr.size(); i++) 
		//{
			Brand aBrand = EdetailingApplication.mBrandArr.get(0);

			long aBrandId = aBrand.getmId();
			Brand bBrand =db.getBrand(aBrandId);
			long brandKey = 0;
			
			if(bBrand==null)
			{
				brandKey = db.createBrand(aBrand);
			}
			else
			{
				System.out.println("Brand already exists in the DB");
				db.updateBrand(aBrand);
			}
				
				//for(int j = 0; j <EdetailingApplication.mBrandArr.get(i).getmContentArr().size(); j++)	
				//{
					Content aContent;
					aContent = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex);
					aContent.setbId(brandKey);
				
					long aContentId  =	aContent.getmId();
					Content bContent =	db.getContent(aContentId);
					long contentKey = 0;
					
					if(bContent==null)
					{
						contentKey =db.createContent(aContent);
					}
					else
					{
						// Content already exists in the DB
						System.out.println("Content already exists in the DB CID :"+aContent.getmId());
						
						System.out.println("*** Content downloaded size :"+aContent.getDownloadedSize()+" ***");
						
						db.updateContent(aContent);
					}
					
						for(int k = 0; k <EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().size(); k++)	
						{
							Parent aParent = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(k);
							aParent.setcId(contentKey);
							long aParentId = aParent.getmId();
							long parentKey = 0;
							Parent bParent = db.getParent(aParentId);
							
							if(bParent==null)
							{
								parentKey =db.createParent(aParent);
							}
							else
							{
								System.out.println("Parent already exists in the DB PID :"+aParent.getmId());
								db.updateParent(aParent);
							}
							
								for (int l = 0; l < EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(k).getmChildArr().size(); l++) 
								{
									Child aChild = EdetailingApplication.mBrandArr.get(0).getmContentArr().get(contentIndex).getmParentArr().get(k).getmChildArr().get(l);
									aChild.setpID(parentKey);
									long aChildId = aChild.getmID();
									Child bChild = db.getChild(aChildId);
									if(bChild==null)
									{
										db.createChild(aChild);
									}
									else
									{
										System.out.println("Child already exists in the DB CHID"+aChild.getmID());
										db.updateChild(aChild);
									}
										
								}
						 	
					    }
					
						
		copyDataBase();		
						
		mListener.allDownloadComplete(contentIndex);
		
	}
	
	 //for db copying temporary function
	 public void copyDataBase() {
	       System.out.println("in copy data base at finally");
	        try {
	            File sd = Environment.getExternalStorageDirectory();
	            File data = Environment.getDataDirectory();
	            if (sd.canWrite()) {
	                String currentDBPath = "/data/" + mContext.getPackageName()
	                        + "/databases/MyDay";
	                String backupDBPath = "DB_29Apr.sqlite";
	                File currentDB = new File(data, currentDBPath);
	                File backupDB = new File(sd, backupDBPath);
	                if (currentDB.exists()) {
	                    FileChannel src = new FileInputStream(currentDB)
	                            .getChannel();
	                    FileChannel dst = new FileOutputStream(backupDB)
	                            .getChannel();
	                    dst.transferFrom(src, 0, src.size());
	                    src.close();
	                    dst.close();
	                }
	            }
	        } catch (Exception e) {
	            System.out.println("in copy of data base 10 ");

	        }
	    }
	
}
