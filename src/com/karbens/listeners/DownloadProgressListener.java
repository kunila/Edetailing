package com.karbens.listeners;

public interface DownloadProgressListener {

	public void updateDownloadProgress(int contentId,long totalread);
}
