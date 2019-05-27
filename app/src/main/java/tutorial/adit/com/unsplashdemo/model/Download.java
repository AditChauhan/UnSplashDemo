/*
 * Copyright (c) 2019 Created by Adit Chauhan
 */

package tutorial.adit.com.unsplashdemo.model;

import java.text.DecimalFormat;

public class Download {

    int downloadProgress;
    Double currentFileSize;
    Double totalFileSize;
    DecimalFormat df;

    public Download() {
        df = new DecimalFormat("0.0");
    }

    public int getDownloadProgress() {
        return downloadProgress;
    }

    public void setDownloadProgress(int downloadProgress) {
        this.downloadProgress = downloadProgress;
    }

    public String getCurrentFileSize() {
        return df.format(currentFileSize);
    }

    public void setCurrentFileSize(Double currentFileSize) {
        this.currentFileSize = currentFileSize;
    }

    public String getTotalFileSize() {
        return df.format(totalFileSize);
    }

    public void setTotalFileSize(Double totalFileSize) {
        this.totalFileSize = totalFileSize;
    }
}
