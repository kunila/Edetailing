package com.karbens.listeners;

public interface ContentDownloadListener {

	public void allDownloadComplete(int contentIndex);
	public void parsingComplete();
	public void downloadFinished();
}
