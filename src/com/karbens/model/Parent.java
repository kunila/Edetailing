package com.karbens.model;

import java.util.ArrayList;

import android.os.Parcel;
import android.os.Parcelable;

public class Parent implements Parcelable {
	
	private String mName = "";
	private int mId = 0;
	private String mContentUrl = "";
	private String mFrame = "";
	public int mHas_child =0;
	private String mParentViewTime ="";
	//private int mSlideBgPath = 0; // changes to string later
	private String mSlideBgPath =  "";
	private int mTimeInterval = 0;
	public ArrayList<Child> mChildArr = new ArrayList<Child>();
	private long cId = 0;
	private boolean isDisabled=false;
	
	
	public long getcId() {
		return cId;
	}

	public void setcId(long cId) {
		this.cId = cId;
	}
	
	public long getmId() {
		return mId;
	}

	public void setmId(int mId) {
		this.mId = mId;
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

	public int getmHas_child() {
		return mHas_child;
	}

	public void setmHas_child(int mHas_child) {
		this.mHas_child = mHas_child;
	}


	public String getmFrame() {
		return mFrame;
	}

	public void setmFrame(String mFrame) {
		this.mFrame = mFrame;
	}

	public String getmParentViewTime() {
		return mParentViewTime;
	}

	public void setmParentViewTime(String mParentViewTime) {
		this.mParentViewTime = mParentViewTime;
	}

	public String getmSlideBgPath() {
		return mSlideBgPath;
	}

	public void setmSlideBgPath(String mSlideBgPath) {
		this.mSlideBgPath = mSlideBgPath;
	}

	
	public int getmTimeInterval() {
		return mTimeInterval;
	}

	public void setmTimeInterval(int mTimeInterval) {
		this.mTimeInterval = mTimeInterval;
	}

	public ArrayList<Child> getmChildArr() {
		return mChildArr;
	}

	public void setmChildArr(ArrayList<Child> mChildArr) {
		this.mChildArr = mChildArr;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		
		//dest.writeSerializable(mChildArr);
		dest.writeString(mName);
		dest.writeString(mContentUrl);
		dest.writeInt(mHas_child);
		dest.writeString(mParentViewTime);
		dest.writeString(mSlideBgPath);
		dest.writeInt(mTimeInterval);
		//dest.writeTypedList(mChildArr);
		dest.writeList(mChildArr);
		//dest.writeList(is)
	}
	 	
	public static final Parcelable.Creator<Parent> CREATOR=new Parcelable.Creator<Parent>() {
		
	public Parent createFromParcel(Parcel in)
	{
		return new Parent(in);
	}

	@Override
	public Parent[] newArray(int size) 
	{
		// TODO Auto-generated method stub
		return new Parent[size];
	}
	
	};
	
	private Parent(Parcel in)
	{
		mName = in.readString();
		mContentUrl = in.readString();
		mHas_child = in.readInt();
		mParentViewTime = in.readString();
		mSlideBgPath = in.readString(); // changes to string later
		mTimeInterval = in.readInt();
		//mChildArr =  (ArrayList<Child>) in.readSerializable();
		//in.readTypedList(mChildArr,Child.CREATOR);
		in.readArrayList(Child.class.getClassLoader());
	}

	public Parent() 
	{
		// TODO Auto-generated constructor stub
		//mChildArr = new ArrayList<Child>();
	}


	public boolean getIsDisabled() {
		  return isDisabled;
		 }

		 public void setIsDisabled(boolean isDisabled) {
		  this.isDisabled = isDisabled;
		 }
	


	
	}

	
	


