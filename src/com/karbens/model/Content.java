package com.karbens.model;

import java.util.ArrayList;
import com.karbens.utility.DataManager.DownloadContent;
import android.os.Parcel;
import android.os.Parcelable;

public class Content implements Parcelable{
	
	 private String mName = "";
	 private int mId =0; // framework ID
	 private long bId =0; // Brand Foreign Key
	 private long pkId =0; // Content Primary Key
	 private int downloadStatus = 0;
	 private String lastDownloadDate;
	 
	 private int progressValue = 0;
	 private int contentSize = 0;
	 private int downloadedSize = 0;
	 private int downloadCancelCount = 0;
	 

	 private int downloadSize;
	 private int urlCount;
	 private boolean isDownloading;
	 
	private ArrayList<Parent> mParentArr = new ArrayList<Parent>();
	private ArrayList<DownloadContent> mDwnContentArr = new ArrayList<DownloadContent>();
	 
	public long getbId() {
		return bId;
	}

	public void setbId(long bId) {
		this.bId = bId;
	}
	
	public int getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
	}
	
	public long getPkId() {
		return pkId;
	}

	public void setPkId(long pkId) {
		this.pkId = pkId;
	}
	
	public String getmName() {
		return mName;
	}

	public void setmName(String mName) {
		this.mName = mName;
	}

	public int getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(int downloadStatus) {
		this.downloadStatus = downloadStatus;
	}


	public String getLastDownloadDate() {
		return lastDownloadDate;
	}

	public void setLastDownloadDate(String lastDownloadDate) {
		this.lastDownloadDate = lastDownloadDate;
	}

	public ArrayList<Parent> getmParentArr() {
		return mParentArr;
	}

	public void setmParentArr(ArrayList<Parent> mParentArr) {
		this.mParentArr = mParentArr;
	}
	
	
	public int getProgressValue() {
		return progressValue;
	}

	public void setProgressValue(int progressValue) {
		this.progressValue = progressValue;
	}

	public int getContentSize() {
		return contentSize;
	}

	public void setContentSize(int contentSize) {
		this.contentSize = contentSize;
	}

	public int getDownloadedSize() {
		return downloadedSize;
	}

	public void setDownloadedSize(int downloadedSize) {
		this.downloadedSize = downloadedSize;
	}

	public int getDownloadSize() {
		return downloadSize;
	}

	public void setDownloadSize(int downloadSize) {
		this.downloadSize = downloadSize;
	}
	
	
	public int getUrlCount() {
		return urlCount;
	}

	public void setUrlCount(int urlCount) {
		this.urlCount = urlCount;
	}

	public ArrayList<DownloadContent> getmDwnContentArr() {
		return mDwnContentArr;
	}

	public void setmDwnContentArr(ArrayList<DownloadContent> mDwnContentArr) {
		this.mDwnContentArr = mDwnContentArr;
	}
	
	public boolean getIsDownloading() {
		return isDownloading;
	}

	public void setIsDownloading(boolean isDownloading) {
		this.isDownloading = isDownloading;
	}
	
	public int getDownloadCancelCount() {
		return downloadCancelCount;
	}

	public void setDownloadCancelCount(int downloadCancelCount) {
		this.downloadCancelCount = downloadCancelCount;
	}
	

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(mId);
		dest.writeString(mName);
		//dest.writeTypedList(mParentArr);
		dest.writeList(mParentArr);
	}
	 	
	public static final Parcelable.Creator<Content> CREATOR=new Parcelable.Creator<Content>() 
	{
		
	public Content createFromParcel(Parcel in)
	{
		return new Content(in);
	}

	@Override
	public Content[] newArray(int size) 
	{
		return new Content[size];
	}
	
	};
	
	private Content(Parcel in)
	{
		mName = in.readString();
		mId = in.readInt();
		//in.readTypedList(mParentArr, Parent.CREATOR);
		in.readArrayList(Parent.class.getClassLoader());
		
		
		//mParentArr = (ArrayList<Parent>) in.readSerializable();
	}

	public Content() {
		//mParentArr = new ArrayList<Parent>();
		
	}

	
}
