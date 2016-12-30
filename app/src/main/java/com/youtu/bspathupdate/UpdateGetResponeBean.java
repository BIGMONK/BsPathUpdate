package com.youtu.bspathupdate;

/**
 * Created by djf on 2016/12/30.
 */

public class UpdateGetResponeBean {

    /**
     * code : 10000
     * data : {"forceInstall":false,"incrementalUpdateInfo":{"fullApkMD5":"125","patchUrl":"www.p1andp5.com"},"installType":"NOTIFY_INSTALL","updateSize":500,"updateTime":"2016-12-30 16:57:43","updateType":"INCREMENTAL_UPDATE","versionCode":5,"versionName":"apk5.0"}
     * message : 成功
     */

    private int code;
    private DataBean data;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public static class DataBean {
        /**
         * forceInstall : false
         * incrementalUpdateInfo : {"fullApkMD5":"125","patchUrl":"www.p1andp5.com"}
         * installType : NOTIFY_INSTALL
         * updateSize : 500
         * updateTime : 2016-12-30 16:57:43
         * updateType : INCREMENTAL_UPDATE
         * versionCode : 5
         * versionName : apk5.0
         */

        private boolean forceInstall;
        private IncrementalUpdateInfoBean incrementalUpdateInfo;
        private String installType;
        private int updateSize;
        private String updateTime;
        private String updateType;
        private int versionCode;
        private String versionName;

        public boolean isForceInstall() {
            return forceInstall;
        }

        public void setForceInstall(boolean forceInstall) {
            this.forceInstall = forceInstall;
        }

        public IncrementalUpdateInfoBean getIncrementalUpdateInfo() {
            return incrementalUpdateInfo;
        }

        public void setIncrementalUpdateInfo(IncrementalUpdateInfoBean incrementalUpdateInfo) {
            this.incrementalUpdateInfo = incrementalUpdateInfo;
        }

        public String getInstallType() {
            return installType;
        }

        public void setInstallType(String installType) {
            this.installType = installType;
        }

        public int getUpdateSize() {
            return updateSize;
        }

        public void setUpdateSize(int updateSize) {
            this.updateSize = updateSize;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }

        public String getUpdateType() {
            return updateType;
        }

        public void setUpdateType(String updateType) {
            this.updateType = updateType;
        }

        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public static class IncrementalUpdateInfoBean {
            /**
             * fullApkMD5 : 125
             * patchUrl : www.p1andp5.com
             */

            private String fullApkMD5;
            private String patchUrl;

            public String getFullApkMD5() {
                return fullApkMD5;
            }

            public void setFullApkMD5(String fullApkMD5) {
                this.fullApkMD5 = fullApkMD5;
            }

            public String getPatchUrl() {
                return patchUrl;
            }

            public void setPatchUrl(String patchUrl) {
                this.patchUrl = patchUrl;
            }
        }
    }
}
