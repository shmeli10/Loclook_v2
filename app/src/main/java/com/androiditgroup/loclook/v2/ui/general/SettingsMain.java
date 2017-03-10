package com.androiditgroup.loclook.v2.ui.general;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
//import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;

//import com.github.aakira.expandablelayout.ExpandableLayoutListener;
//import com.github.aakira.expandablelayout.ExpandableRelativeLayout;
//import com.softintercom.smartcyclealarm.Adapters.SettingsAdapter;
//import com.softintercom.smartcyclealarm.Global.Vars;
//import com.softintercom.smartcyclealarm.Helpers.BannerFragment;
//import com.softintercom.smartcyclealarm.Holders.SettingsLineHolder;
//import com.softintercom.smartcyclealarm.MainActivity;
//import com.softintercom.smartcyclealarm.R;

import java.util.ArrayList;

public class SettingsMain { // extends BannerFragment {

//    private Button menuicon;
//    private Button calibrate, testBut;
//    private ScrollView scroll;
//    private ListView lv, listViewTop, listViewCenter, listViewBottom;
//    private int selectedRow;
//    public SettingsAdapter lineAdapter, adapterTop, adapterCenter, adapterBottom;
//    public Animation animation;
//    public ExpandableRelativeLayout expandableLayout;
//    public ArrayList<SettingsLineHolder> arrayTop = new ArrayList<SettingsLineHolder>();
//    public ArrayList<SettingsLineHolder> arrayCenter = new ArrayList<SettingsLineHolder>();
//    public ArrayList<SettingsLineHolder> arrayBottom = new ArrayList<SettingsLineHolder>();
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//
//        Vars.settingsMainActivity = this;
//        Vars.lockMenu(false);
//
//        arrayTop = new ArrayList<SettingsLineHolder>();
//        arrayCenter = new ArrayList<SettingsLineHolder>();
//        arrayBottom = new ArrayList<SettingsLineHolder>();
//
//        arrayTop.add(new SettingsLineHolder(R.drawable.ic_set_alarm, Vars.TYPE_1, Vars.SUB_TYPE_1));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_alarm_sound, Vars.TYPE_2, Vars.SUB_TYPE_2));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_vol1, Vars.TYPE_3, Vars.SUB_TYPE_3));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_mess, Vars.TYPE_2, Vars.SUB_TYPE_4));
////        arrayCenter.add(new SettingsLineHolder(Vars.TYPE_1, Vars.SUB_TYPE_5)); // weekend alarm
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_enhance, Vars.TYPE_1, Vars.SUB_TYPE_6));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_mood, Vars.TYPE_1, Vars.SUB_TYPE_7));
//        boolean hasFlash = getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//        if(hasFlash)arrayCenter.add(new SettingsLineHolder(R.drawable.set_heart, Vars.TYPE_1, Vars.SUB_TYPE_8));
//        //arrayCenter.add(new SettingsLineHolder(Vars.TYPE_1, Vars.SUB_TYPE_8));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_phase, Vars.TYPE_2, Vars.SUB_TYPE_9));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_snooze, Vars.TYPE_2, Vars.SUB_TYPE_10));
//        arrayCenter.add(new SettingsLineHolder(R.drawable.ic_set_vibration, Vars.TYPE_2, Vars.SUB_TYPE_11));
//        //arrayBottom.add(new SettingsLineHolder(Vars.TYPE_1, Vars.SUB_TYPE_12));
//        arrayBottom.add(new SettingsLineHolder(R.drawable.ic_set_aid, Vars.TYPE_2, Vars.SUB_TYPE_13));
//        arrayBottom.add(new SettingsLineHolder(R.drawable.ic_set_db, Vars.TYPE_2, Vars.SUB_TYPE_14));
//        arrayBottom.add(new SettingsLineHolder(R.drawable.set_calibr, Vars.TYPE_2, Vars.SUB_TYPE_15));
//
//        View rootView = inflater.inflate(R.layout.settings_main, container, false);
//        //menuNavigator = new MenuNavigator(MainActivity.mLeftMenu,Vars.pageHolder, getActivity(),SettingsMain.this);
//        menuicon = (Button) rootView.findViewById(R.id.icon1);
//        menuicon.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                ((MainActivity) Vars.mainActivity).openMenu(true);
//            }
//        });
//
//        scroll = (ScrollView) rootView.findViewById(R.id.sett_scroll);
//
//        listViewTop = (ListView) rootView.findViewById(R.id.sett_list_top);
//        adapterTop = new SettingsAdapter(getActivity(), arrayTop);
//        listViewTop.setAdapter(adapterTop);
//
//        listViewCenter = (ListView) rootView.findViewById(R.id.sett_list_center);
//        adapterCenter = new SettingsAdapter(getActivity(), arrayCenter);
//        listViewCenter.setAdapter(adapterCenter);
//
//        listViewBottom = (ListView) rootView.findViewById(R.id.sett_list_bottom);
//        adapterBottom = new SettingsAdapter(getActivity(), arrayBottom);
//        listViewBottom.setAdapter(adapterBottom);
//
//
//        View top = rootView.findViewById(R.id.sett_list_top);
//        View bott = rootView.findViewById(R.id.sett_list_bottom);
//        View center = rootView.findViewById(R.id.expandableLayout);
//
//        top.setVisibility(View.VISIBLE);
//        bott.setVisibility(View.VISIBLE);
//        center.setVisibility(View.VISIBLE);
//
//        expandableLayout = (ExpandableRelativeLayout) rootView.findViewById(R.id.expandableLayout);
//
//
//        int centerHeight = Vars.getListViewHeight(listViewCenter);
//        ViewGroup.LayoutParams params = expandableLayout.getLayoutParams();
//        params.height = centerHeight;
//        expandableLayout.setLayoutParams(params);
//
//        Vars.setNewListViewHeight(listViewBottom);
//
//        expandableLayout.setListener(new ExpandableLayoutListener() {
//            @Override
//            public void onAnimationStart() {
//                Vars.settCenterMoving = true;
//            }
//            @Override
//            public void onAnimationEnd() {
//                Vars.settCenterMoving = false;
//            }
//            @Override
//            public void onPreOpen() {
//            }
//            @Override
//            public void onPreClose() {
//            }
//            @Override
//            public void onOpened() {
//            }
//            @Override
//            public void onClosed() {
//            }
//        });
//
//        expandableLayout.post(new Runnable() {
//
//            @Override
//            public void run() {
//                Vars.settCenterMoving = false;
//
//                if(Vars.screenshot){
//                    scroll.fullScroll(ScrollView.FOCUS_DOWN);
//                }
//                else{
//                    scroll.scrollTo(0, Vars.settScrollY);
//                }
//
//                if(!Vars.alarmModeIsOn) {
//                    expandableLayout.collapse(1, null);
//                }
//            }
//        });
//
//        super.setBanner(rootView, getActivity());
//
//        return rootView;
//    }
//
//    public void expandCenter(boolean exp){
//        if(exp) expandableLayout.expand();
//        else expandableLayout.collapse();
//    }
//
//
//    public void openHeartRate(){
//        if(Vars.tryingToCheckBloxTrue){
//            isCameraPermissionGranted();
//        }
//        else{
//            canOpenHeartRate(false);
//        }
//
//    }
//
//
//    public boolean isCameraPermissionGranted() {
//        if (Build.VERSION.SDK_INT >= 23) {
//            if (getActivity().checkSelfPermission(Manifest.permission.CAMERA)
//                    == PackageManager.PERMISSION_GRANTED) {
//                Log.v("Permission", "Permission is granted");
//                canOpenHeartRate(true);
//                return true;
//            } else {
//                Log.v("Permission", "Permission is revoked");
//                this.requestPermissions(new String[]{Manifest.permission.CAMERA}, 0); // Fragment
//                //ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA}, 0); // Activity
//                return false;
//            }
//        } else { //permission is automatically granted on sdk<23 upon installation
//            Log.v("Permission", "Permission is granted");
//            canOpenHeartRate(true);
//            return true;
//        }
//    }
//
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            afterSaveState();
//        } else {
//            Vars.heartCheckBox.toggle();
//            Vars.heartImgOff();
//            //do nothing
//            canOpenHeartRate(false);
//        }
//    }
//    private void canOpenHeartRate(boolean canOpen){
//        Vars.heartCheckBox.setChecked(canOpen);
//        Vars.showHeartRate = canOpen;
//        Vars.saveData();
//    }
//
////    @Override
////    public void onSaveInstanceState( Bundle outState ) {
////        super.onSaveInstanceState(outState);
////        canOpenHeartRate();
////    }
//
//    public void afterSaveState() {
//        Thread t = new Thread() {
//            public void run() {
//                try {
//                    canOpenHeartRate(true);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        };
//
//        t.start();
//    }
//
//    @Override
//    public void onDestroy() {
//        if(scroll != null){
//            Vars.settScrollY = scroll.getScrollY();
//        }
//        super.onDestroy();
//    }
}