package com.karbens.model;
          
import java.util.ArrayList;

import android.R.string;
import android.os.Parcel;
import android.os.Parcelable;

public class Brand implements Parcelable {

	private String mName = "";
	private int mId = 0;
	private ArrayList<Content> mContentArr= new ArrayList<Content>();
	
	
	public int getmId() {
		
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
	public ArrayList<Content> getmContentArr() {
		return mContentArr;
	}
	public void setmContentArr(ArrayList<Content> mContentArr) {
		this.mContentArr = mContentArr;
	}


	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}


	@Override
	public void writeToParcel(Parcel dest, int flags) {
		//dest.writeSerializable(mContentArr);
		dest.writeInt(mId);
		dest.writeString(mName);
		//dest.writeTypedList(mContentArr);
		dest.writeList(mContentArr);
		
	}
	
	public static final Parcelable.Creator<Brand> CREATOR=new Parcelable.Creator<Brand>() {
		
		public Brand createFromParcel(Parcel in){
			return new Brand(in);
		}

		@Override
		public Brand[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Brand[size];
		}
		
		};
		
		private Brand(Parcel in)
		{
			mName = in.readString();
			mId = in.readInt();
			//mContentArr = (ArrayList<Content>) in.readSerializable();
			//in.readTypedList(mContentArr,Content.CREATOR);
			in.readArrayList(Content.class.getClassLoader());
		}


		public Brand() {
			// TODO Auto-generated constructor stub
			//mContentArr = new ArrayList<Content>();
		}

}
