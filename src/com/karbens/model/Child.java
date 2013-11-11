package com.karbens.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Child implements Parcelable{

	private String mName ="";
	private String mContentUrl ="";
	private String mChildViewTime = "";
	private String mFilePath ="";
	private String mFrame="";
	private int mIsAnimated;
	//private int mAnimPath = 0;
	private int mTimeInterval = 0;
	private int mType = 0;
	private int mID = 0; // Framework Key
	private String mText="";
	private String mTextStyle="";
	private long pID = 0; // Parent Foreign Key
	

	public long getmID() {
		return mID;
	}

	public void setmID(int mID) {
		this.mID = mID;
	}

	public long getpID() {
		return pID;
	}

	public void setpID(long pID) {
		this.pID = pID;
	}

	public String getmName() {
		return mName;
	}
	
	public void setmName(String mName) {
		this.mName = mName;
	}
	
	public String getmContentUrl() {
		return mContentUrl;
	}
	
	public void setmContentUrl(String mContentUrl) {
		this.mContentUrl = mContentUrl;
	}
	
	public String getmChildViewTime() {
		return mChildViewTime;
	}
	
	public void setmChildViewTime(String mChildViewTime) {
		this.mChildViewTime = mChildViewTime;
	}
	
	
	public String getmFrame() {
		return mFrame;
	}
	
	public void setmFrame(String mFrame) {
		this.mFrame = mFrame;
	}
	
	public int getmTimeInterval() {
		return mTimeInterval;
	}
	
	public void setmTimeInterval(int mTimeInterval) {
		this.mTimeInterval = mTimeInterval;
	}
	
	public int getmType() {
		return mType;
	}
	
	public void setmType(int mType) {
		this.mType = mType;
	}

	public String getmText() {
		return mText;
	}

	public void setmText(String mText) {
		this.mText = mText;
	}

	public String getmTextStyle() {
		return mTextStyle;
	}

	public void setmTextStyle(String mTextStyle) {
		this.mTextStyle = mTextStyle;
	}

	public String getmFilePath() {
		return mFilePath;
	}

	public void setmFilePath(String mFilePath) {
		this.mFilePath = mFilePath;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public int getmIsAnimated() {
		return mIsAnimated;
	}

	public void setmIsAnimated(int mIsAnimated) {
		this.mIsAnimated = mIsAnimated;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(mName);
		dest.writeString(mContentUrl);
		dest.writeString(mChildViewTime);
		dest.writeString(mFilePath);
		dest.writeString(mFrame);
		dest.writeInt(mIsAnimated);
		dest.writeInt(mTimeInterval);
		dest.writeInt(mType);
		 
	}
	
	private Child(Parcel in)
	{
		mName = in.readString();
		mContentUrl = in.readString();
		mChildViewTime = in.readString();
		mFilePath = in.readString();
		mFrame = in.readString();
		mIsAnimated = in.readInt();
		mTimeInterval = in.readInt();
		mType = in.readInt();
	}
	
	public Child() 
	{
		// TODO Auto-generated constructor stub
		//mChildArr = new ArrayList<Child>();
	}
	
	public static final Parcelable.Creator<Child> CREATOR=new Parcelable.Creator<Child>() 
	{
				
		public Child createFromParcel(Parcel in)
		{
			return new Child(in);
		}

		@Override
		public Child[] newArray(int size) 
		{
			return new Child[size];
		}
			
	};

}
