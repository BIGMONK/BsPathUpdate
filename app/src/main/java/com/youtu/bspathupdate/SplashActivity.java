package com.youtu.bspathupdate;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.cy.lib.upgrade.Updater;
import com.cy.lib.upgrade.UpdaterConfiguration;
import com.cy.lib.upgrade.callback.UpdateCheckCallback;
import com.cy.lib.upgrade.model.UpdateInfo;
import com.cy.lib.upgrade.utils.AppInfoUtils;
import com.cy.lib.upgrade.utils.MD5Utils;
import com.cy.upgrade.interfacedef.DownloadUIHandler;
import com.cy.upgrade.interfacedef.UpdateCheckUIHandler;
import com.cy.upgrade.interfacedef.UpdateChecker;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {

    private String TAG = this.getClass().getSimpleName();
    private UpdateInfo mUpdateInfo;
    private static final int HANDLER_UPDATE_AVAILABLE = 1000;
    private static final int HANDLER_GOTO_MAINACTIVITY = 1001;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HANDLER_UPDATE_AVAILABLE:
                    setUpdateInfoAndUpdate(mUpdateInfo);
                    break;
                case HANDLER_GOTO_MAINACTIVITY:
                    OkGo.delete(updaUrl);
                    mHandler.removeMessages(HANDLER_UPDATE_AVAILABLE);
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    break;
            }
        }
    };
    private UpdaterConfiguration config;
    String updaUrl = "http://192.168.1.40:8080/yt-c-platform/caseserver/ApkVersionUpdate";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        UpdateInfo updateInfo = new UpdateInfo();
        //版本信息
        updateInfo.setVersionCode(2);
        updateInfo.setVersionName("v1.2");
        updateInfo.setUpdateTime("2016-12-30 16:57:43");
        updateInfo.setUpdateSize(1024);
        updateInfo.setUpdateInfo("更新日志:\n1.新增万能更新库，实现更新功能只要几行代码。");
        //是否强制安装
        updateInfo.setIsForceInstall(true);
        //更新类型：增量
        updateInfo.setUpdateType(UpdateInfo.UpdateType.INCREMENTAL_UPDATE);
        //增量更新信息
        final UpdateInfo.IncrementalUpdateInfo incrementalUpdateInfo = new UpdateInfo.IncrementalUpdateInfo();
        incrementalUpdateInfo.setFullApkMD5("e7eec01baac70f8a3688570439b9b467");
        incrementalUpdateInfo.setPatchUrl("http://bmob-cdn-4990.b0.upaiyun.com/2016/10/28/aa0bc17f40a91b0b80915a49b40c0174.patch");
        updateInfo.setIncrementalUpdateInfo(incrementalUpdateInfo);
        //安装模式：普通
        updateInfo.setInstallType(UpdateInfo.InstallType.NOTIFY_INSTALL);

        Log.d(TAG, "更新信息：" + new Gson().toJson(updateInfo));

        config = new UpdaterConfiguration();
        //延迟5秒跳转
        mHandler.sendEmptyMessageDelayed(HANDLER_GOTO_MAINACTIVITY, 5000);


        File oldApp = new File(AppInfoUtils.getApkPath(this));
        String MD5 = MD5Utils.get32BitsMD5(oldApp);
        //根据本地AppVersionCode检测更新
        OkGo.get(updaUrl)//
                .tag(this)//
                .params("versionCode", AppInfoUtils.getApkVersionCode(this))//
                .params("md5", MD5)//
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        Log.d(TAG, "OkGo.get onSuccess s=" + s);
                        UpdateGetResponeBean updateGetResponeBean = new Gson().fromJson(s, UpdateGetResponeBean.class);
                        if (updateGetResponeBean.getCode() == 2000) {
                            UpdateGetResponeBean.DataBean dataBean = updateGetResponeBean.getData();
                            //设置安装方式
                            switch (dataBean.getInstallType()) {
                                case "SILENT_INSTALL"://静默安装
                                    mUpdateInfo.setInstallType(UpdateInfo.InstallType.SILENT_INSTALL);
                                    break;
                                case "NOTIFY_INSTALL"://普通安装
                                    mUpdateInfo.setInstallType(UpdateInfo.InstallType.NOTIFY_INSTALL);
                                    break;
                            }
                            //是否强制安装
                            mUpdateInfo.setIsForceInstall(dataBean.isForceInstall());
                            //设置更新方式
                            switch (dataBean.getUpdateType()) {
                                case "TOTAL_UPDATE"://全量更新
                                    UpdateInfo.TotalUpdateInfo totalUpdateInfo = new UpdateInfo.TotalUpdateInfo();
                                    totalUpdateInfo.setApkUrl(dataBean.getIncrementalUpdateInfo().getPatchUrl());
                                    mUpdateInfo.setTotalUpdateInfo(totalUpdateInfo);
                                    break;

                                case "INCREMENTAL_UPDATE":
                                    UpdateInfo.IncrementalUpdateInfo incrementalUpdateInfo1 = new UpdateInfo.IncrementalUpdateInfo();
                                    incrementalUpdateInfo.setFullApkMD5(dataBean.getIncrementalUpdateInfo().getFullApkMD5());
                                    incrementalUpdateInfo.setPatchUrl(dataBean.getIncrementalUpdateInfo().getPatchUrl());
                                    mUpdateInfo.setIncrementalUpdateInfo(incrementalUpdateInfo);
                            }
                            //取消跳转
                            mHandler.removeMessages(HANDLER_GOTO_MAINACTIVITY);
                            //成功检测到更新
                            mHandler.sendEmptyMessage(HANDLER_UPDATE_AVAILABLE);
                        }else {

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);

                    }
                });




    }

    private void setUpdateInfoAndUpdate(final UpdateInfo updateInfo) {

        config.updateChecker(new UpdateChecker() {
            @Override
            public void check(UpdateCheckCallback callback) {
                //此处模拟更新信息获取,信息获取后需要将UpdateInfo设置到配置信息中，然后要调用相应的回调方法才能使整个流程完整执行
//                UpdateInfo updateInfo = new UpdateInfo();
//                updateInfo.setVersionCode(2);
//                updateInfo.setVersionName("v1.2");
//                updateInfo.setUpdateTime("2016/10/28");
//                updateInfo.setUpdateSize(1024);
//                updateInfo.setUpdateInfo("更新日志:\n1.新增万能更新库，实现更新功能只要几行代码。");
                //设置强制更新
//                updateInfo.setIsForceInstall(true);

                //使用全量更新信息
//                updateInfo.setUpdateType(UpdateInfo.UpdateType.TOTAL_UPDATE);
//                UpdateInfo.TotalUpdateInfo totalUpdateInfo = new UpdateInfo.TotalUpdateInfo();
//                totalUpdateInfo.setApkUrl("http://wap.apk.anzhi.com/data2/apk/201609/05/f06abcb0e2cba4c8ce2301c4b437a492_72932500.ap");
//                updateInfo.setTotalUpdateInfo(totalUpdateInfo);

                //设置增量更新信息,设置完整的apk的MD5及增量包下载地址(此处的增量包需要由bsdiff生成)
//                updateInfo.setUpdateType(UpdateInfo.UpdateType.INCREMENTAL_UPDATE);
//                UpdateInfo.IncrementalUpdateInfo incrementalUpdateInfo = new UpdateInfo.IncrementalUpdateInfo();
//                incrementalUpdateInfo.setFullApkMD5("e7eec01baac70f8a3688570439b9b467");
//                incrementalUpdateInfo.setPatchUrl("http://bmob-cdn-4990.b0.upaiyun.com/2016/10/28/aa0bc17f40a91b0b80915a49b40c0174.patch");
//                updateInfo.setIncrementalUpdateInfo(incrementalUpdateInfo);

                //设置普通模式的安装
//                updateInfo.setInstallType(UpdateInfo.InstallType.NOTIFY_INSTALL);

                //设置静默安装模式,设置此模式前必须确保手机对本应用授予了Root权限
                updateInfo.setInstallType(UpdateInfo.InstallType.SILENT_INSTALL);

                if (updateInfo != null) {
                    //设置更新信息，这样各模块就可以通过config.getUpdateInfo()共享这个数据了,注意这个方法一定要调用且要在UpdateCheckCallback.onCheckSuccess之前调用
                    config.updateInfo(updateInfo);
                    callback.onCheckSuccess();
                } else {
                    callback.onCheckFail("");
                }
            }
        });

        //处理UI时，在必要的时机需要调用config.getDownloader()的相关方法，才能保证流程正确执行
        config.updateUIHandler(new UpdateCheckUIHandler() {
            @Override
            public void setContext(Context context) {
                //此处的context为Updater.getInstance().check(Context context)方法传入的context
            }

            @Override
            public void hasUpdate() {
                //有更新时的UI展示
                Log.d(TAG,"LibUpgrade hasUpdate");
            }

            @Override
            public void noUpdate() {
                //没有更新时的UI展示
                Log.d(TAG,"LibUpgrade noUpdate");
            }

            @Override
            public void checkError(String error) {
                //更新检查失败时的UI展示
                Log.d(TAG,"LibUpgrade checkError");
            }
        });

        config.downloadUIHandler(new DownloadUIHandler() {
            @Override
            public void setContext(Context context) {
                //此处的context为Updater.getInstance().check(Context context)方法传入的context
            }

            @Override
            public void downloadStart() {
                //开始下载时的UI展示
                Log.d(TAG,"LibUpgrade downloadStart");
            }

            @Override
            public void downloadProgress(int progress, int total) {
                //下载进度的展示
                Log.d(TAG,"LibUpgrade downloadProgress  progress="+progress+"    total="+total);
            }

            @Override
            public void downloadComplete(String path) {
                //下载完成时的处理，此处应通过config.getUpdateInfo()获取更信息，然后再通过相应的安装器进行安装
                Log.d(TAG,"LibUpgrade downloadComplete path ="+path);
            }

            @Override
            public void downloadError(String error) {
                //下载失败时的UI提示
                Log.d(TAG,"LibUpgrade downloadError ");
            }

            @Override
            public void downloadCancel() {
                //下载取消时的UI提示
                Log.d(TAG,"LibUpgrade downloadCancel ");
            }
        });


        Updater.getInstance().init(config);

        Updater.getInstance().check(this);
    }
}
