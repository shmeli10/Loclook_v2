package com.androiditgroup.loclook.v2.ui.publication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.LocLookApp_NEW;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.AddPublicationQuizAnswersAdapter;
import com.androiditgroup.loclook.v2.adapters.BadgesAdapter;
import com.androiditgroup.loclook.v2.adapters.GalleryListAdapter_NEW;
import com.androiditgroup.loclook.v2.constants.ErrorConstants;
import com.androiditgroup.loclook.v2.constants.SettingsConstants;
import com.androiditgroup.loclook.v2.data.BadgeController;
import com.androiditgroup.loclook.v2.data.PublicationController;
import com.androiditgroup.loclook.v2.interfaces.PublicationCreateInterface;
import com.androiditgroup.loclook.v2.models.BadgeModel;
import com.androiditgroup.loclook.v2.ui.general.MainActivity_NEW;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightGridView;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.ImageDelivery;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;

/**
 * Created by Serghei Ostrovschi on 1/25/16.
 */

public class PublicationFragment_NEW    extends     ParentFragment
                                        implements  //View.OnClickListener,
                                                    //TextWatcher,
                                                    PublicationCreateInterface,
                                                    CompoundButton.OnCheckedChangeListener,
                                                    AddPublicationQuizAnswersAdapter.QuizAnswerCallback {

    private static LayoutInflater               mInflater;

    private LocLookApp_NEW                      locLookApp_NEW;
    private MainActivity_NEW                    mMainActivity;
    private Handler                             mHandler;

    private BadgeController                     badgeController;
    private PublicationController               publicationController;

    private RelativeLayout                      mBadgeBlock;
    private RelativeLayout                      mQuizAnswersBlock;
    private RelativeLayout                      mShowPhotoBlockRL;
    private RelativeLayout                      mPhotoBlockRL;

    private ScrollView                          mScroll;
    private EditText                            mPublicationText;
    private TextView                            mLeftCharactersBody;
    private TextView                            mAnonymousStateTV;
    private TextView                            mQuizStateTV;
    private TextView                            mAddAnswerTV;
    private TextView                            mSelectedBadgeTV;
    private TextView                            mOpenGalleryTV;
    private TextView                            mCreatePhotoTV;
    private TextView                            mPhotosAddedSumTV;

    private SwitchCompat                        mAnonymousSwitch;
    private SwitchCompat                        mQuizSwitch;

    private ImageView                           mShowBadgesIV;
    private ImageView                           mSelectedBadgeIV;
    private ImageView                           mShowPhotoBlockIV;
    private ImageView                           mGalleryLeftScrollIV;
    private ImageView                           mGalleryRightScrollIV;

    private ExpandableHeightListView            mQuizAnswersList;
    private ExpandableHeightGridView            mChooseBadgeBlockGV;
    private RecyclerView                        mGalleryPhotosRV;

    private AddPublicationQuizAnswersAdapter    mQuizAnswersAdapter;
    private BadgesAdapter                       mBadgesAdapter;
    private GalleryListAdapter_NEW              mGalleryPhotosAdapter;

    private final int anonymousSwitchResId      = R.id.Publication_AnonymousSwitchBTN;
    private final int quizSwitchResId           = R.id.Publication_QuizSwitchBTN;
    private final int addAnswerResId            = R.id.Publication_QuizAnswersBlockAddAnswer;
    private final int openGalleryResId          = R.id.Publication_OpenGallery;
    private final int createPhotoResId          = R.id.Publication_CreatePhoto;
    private final int showPhotoBlockResId       = R.id.Publication_ShowPhotoBlock;
    private final int badgeBlockResId           = R.id.Publication_BadgeBlock;
    private final int galleryLeftScrollResId    = R.id.Publication_GalleryLeftScrollIV;
    private final int galleryRightScrollResId   = R.id.Publication_GalleryRightScrollBtn;

//    private final int PUBLICATION_TEXT_LIMIT    = LocLookApp.getInstance().getResources().getInteger(R.integer.publication_length);
//    private final int PHOTOS_LIMIT              = LocLookApp.getInstance().getResources().getInteger(R.integer.max_photos_sum);
//    private final int VISIBLE_PHOTOS_LIMIT      = LocLookApp.getInstance().getResources().getInteger(R.integer.visible_photos_sum);
//    private final int SMALL_PHOTO_WIDTH_LIMIT   = (int) LocLookApp.getInstance().getResources().getDimension(R.dimen.small_photo_max_width);
//    private final int SMOOTH_ON                 = 2 * SMALL_PHOTO_WIDTH_LIMIT;

    private BadgeModel          selectedBadge;

    private ArrayList<String>   mAnswersList    = new ArrayList<>();
    private List<Bitmap>        mTumbnailsList  = new ArrayList<>();
    private ArrayList<Bitmap>   mPhotosList     = new ArrayList<>();

    private Map<Integer, BadgeModel>    badgeMap           = new LinkedHashMap<>();
    private Map<Integer, Integer>       badgeImageResIdMap = new LinkedHashMap<>();

    private String mCreatedPhotoPath;

    private int smoothOn = 0;

    private boolean isAvailableToDelete;
    private boolean isBadgesBlockHidden;
    private boolean isPhotoBlockHidden;

    public PublicationFragment_NEW() {
        // Required empty public constructor
    }

    public static PublicationFragment_NEW newInstance(LayoutInflater inflater) {
        mInflater = inflater;

        Bundle args = new Bundle();

        PublicationFragment_NEW fragment = new PublicationFragment_NEW();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup      container,
                             Bundle         savedInstanceState) {

        mMainActivity   = (MainActivity_NEW) getActivity();
        locLookApp_NEW  = ((LocLookApp_NEW) mMainActivity.getApplication());

        badgeController         = locLookApp_NEW.getAppManager().getBadgeController();

        if(badgeController != null) {
            badgeMap = badgeController.getBadgeMap();

            // if(!LocLookApp.badgesMap.isEmpty()) {
            if((badgeMap != null) && (!badgeMap.isEmpty())) {

                Integer mapKey  = badgeMap.keySet().iterator().next();
                selectedBadge   = badgeMap.get(mapKey);
            }

            badgeImageResIdMap = badgeController.getBadgeImageResIdMap();
        }
        else
            locLookApp_NEW.showLog("PublicationFragment_NEW: onCreateView(): error: " +ErrorConstants.BADGE_CONTROLLER_NULL_ERROR);

        publicationController   = locLookApp_NEW.getAppManager().getPublicationController();

        View view = inflater.inflate(R.layout.fragment_send_publication,
                                     container,
                                     false);

        mHandler = new Handler() {
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case 1:
                        mMainActivity.onBackPressed();
                        break;
                    case -1:
                    default:
                        LocLookApp.showSimpleSnakeBar(mScroll, "publication_send_error_text");
                        break;
                }
                mMainActivity.hidePD();
            }
        };

        mScroll             = (ScrollView) view.findViewById(R.id.Publication_ScrollView);

        mPublicationText    = (EditText) view.findViewById(R.id.Publication_PublicationTextET);
        //mPublicationText.addTextChangedListener(this);
        mPublicationText.addTextChangedListener(onPublicationTextChangeListener);

        mQuizAnswersBlock   = (RelativeLayout) view.findViewById(R.id.Publication_QuizAnswersBlock);
        mPhotoBlockRL       = (RelativeLayout) view.findViewById(R.id.Publication_PhotoBlock);

        mBadgeBlock         = (RelativeLayout) view.findViewById(badgeBlockResId);
        //mBadgeBlock.setOnClickListener(this);
        mBadgeBlock.setOnClickListener(badgeBlockClickListener);

        mShowPhotoBlockRL   = (RelativeLayout) view.findViewById(showPhotoBlockResId);
        // mShowPhotoBlockRL.setOnClickListener(this);
        mShowPhotoBlockRL.setOnClickListener(photoBlockClickListener);

        mAnonymousStateTV   = (TextView) view.findViewById(R.id.Publication_AnonymousStateTV);
        mQuizStateTV        = (TextView) view.findViewById(R.id.Publication_QuizStateTV);
        mPhotosAddedSumTV   = (TextView) view.findViewById(R.id.Publication_PhotosAddedSumTV);

        mLeftCharactersBody = (TextView) view.findViewById(R.id.Publication_LeftCharactersBodyTV);
        //mLeftCharactersBody.setText("" +PUBLICATION_TEXT_LIMIT);
        mLeftCharactersBody.setText("" +SettingsConstants.PUBLICATION_MAX_LENGTH);

        mOpenGalleryTV      = (TextView) view.findViewById(openGalleryResId);
        //mOpenGalleryTV.setOnClickListener(this);
        mOpenGalleryTV.setOnClickListener(openGalleryClickListener);

        mCreatePhotoTV      = (TextView) view.findViewById(R.id.Publication_CreatePhoto);
        //mCreatePhotoTV.setOnClickListener(this);
        mCreatePhotoTV.setOnClickListener(createPhotoClickListener);

        mAnonymousSwitch    = (SwitchCompat) view.findViewById(anonymousSwitchResId);
        mAnonymousSwitch.setOnCheckedChangeListener(this);

        mQuizSwitch         = (SwitchCompat) view.findViewById(R.id.Publication_QuizSwitchBTN);
        mQuizSwitch.setOnCheckedChangeListener(this);

        mAddAnswerTV        = (TextView) view.findViewById(addAnswerResId);
        //mAddAnswerTV.setOnClickListener(this);
        mAddAnswerTV.setOnClickListener(addAnswerClickListener);

        for(int i=0; i<Constants.QUIZ_MIN_ANSWERS; i++)
            mAnswersList.add(null);

        mQuizAnswersAdapter = new AddPublicationQuizAnswersAdapter(mInflater, mAnswersList, this);
        mQuizAnswersList    = (ExpandableHeightListView) view.findViewById(R.id.Publication_QuizAnswersList);
        mQuizAnswersList.setExpanded(true);
        mQuizAnswersList.setAdapter(mQuizAnswersAdapter);

        isAvailableToDelete = true;
        isBadgesBlockHidden = true;
        isPhotoBlockHidden  = true;

        mSelectedBadgeIV    = (ImageView) view.findViewById(R.id.Publication_BadgeImageIV);
        mShowBadgesIV       = (ImageView) view.findViewById(R.id.Publication_ShowBadges);
        mShowPhotoBlockIV   = (ImageView) view.findViewById(R.id.Publication_ShowPhotos);
        mSelectedBadgeTV    = (TextView) view.findViewById(R.id.Publication_BadgeNameTV);

        mChooseBadgeBlockGV = (ExpandableHeightGridView) view.findViewById(R.id.Publication_ChooseBadgeBlockGV);

        mBadgesAdapter      = new BadgesAdapter(mInflater);
        mChooseBadgeBlockGV.setAdapter(mBadgesAdapter);
        mChooseBadgeBlockGV.setOnItemClickListener(onBadgeClickListener);

        //mGalleryPhotosAdapter = new GalleryListAdapter(mMainActivity, mPhotosList);
        mGalleryPhotosAdapter   = new GalleryListAdapter_NEW(mMainActivity, mPhotosList);
        mGalleryPhotosRV        = (RecyclerView) view.findViewById(R.id.Publication_GalleryRecyclerView);
        mGalleryPhotosRV.setAdapter(mGalleryPhotosAdapter);

        mGalleryLeftScrollIV    = (ImageView) view.findViewById(galleryLeftScrollResId);
        //mGalleryLeftScrollIV.setOnClickListener(this);
        mGalleryLeftScrollIV.setOnClickListener(galleryLeftScrollClickListener);

        mGalleryRightScrollIV   = (ImageView) view.findViewById(galleryRightScrollResId);
        //mGalleryRightScrollIV.setOnClickListener(this);
        mGalleryRightScrollIV.setOnClickListener(galleryRightScrollClickListener);

        setHasOptionsMenu(true);

        setSmoothOn();

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp_NEW.getInstance().getString(R.string.publication_text);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.publication_menu, menu);
        menu.getItem(0).setVisible(false);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // отправить публикацию
            case R.id.action_send:
                sendPublication();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

/*    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // вычисляем и отображаем кол-во символов, которые еще можно внести в поле
        //mLeftCharactersBody.setText("" + (PUBLICATION_TEXT_LIMIT - mPublicationText.length()));
        mLeftCharactersBody.setText("" + (SettingsConstants.PUBLICATION_MAX_LENGTH - mPublicationText.length()));
    }

    @Override
    public void afterTextChanged(Editable editable) { }*/

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
        switch (compoundButton.getId()) {

            // щелчок по "переключателю вкл./выкл. анонимность"
            case anonymousSwitchResId:
                mAnonymousStateTV.setText(checked ? R.string.state_on_text: R.string.state_off_text);
                break;
            // щелчок по "переключателю вкл./выкл. опрос"
            case quizSwitchResId:

                if(checked){
                    mQuizStateTV.setText(R.string.state_on_text);
                    mQuizAnswersBlock.setVisibility(VISIBLE);
                    autoScrollDown();
                }
                else {
                    mQuizStateTV.setText(R.string.state_off_text);
                    mQuizAnswersBlock.setVisibility(View.GONE);
                }

                break;
        }
    }

    @Override
    public void onDelete(int quizAnswerId) {
        if(isAvailableToDelete) {
            // удаляем поле с вариантом ответа
            mAnswersList.remove(quizAnswerId);

            if (mAnswersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                mAddAnswerTV.setBackgroundResource(R.color.colorPrimaryDark);
                mAddAnswerTV.setClickable(true);
            }
            mQuizAnswersAdapter.notifyDataSetChanged();

            isAvailableToDelete = false;

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    isAvailableToDelete = true;
                }
            }, 1000);
        }
    }

    private GridView.OnItemClickListener onBadgeClickListener = new GridView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            selectedBadge = badgeMap.get(position+1);  // LocLookApp.badgesMap.get(String.valueOf(position+1));

            if(selectedBadge != null) {

                if(badgeImageResIdMap.containsKey(selectedBadge.getBadgeId()))
                    mSelectedBadgeIV.setImageResource(badgeImageResIdMap.get(position+1));

                mSelectedBadgeTV.setText(selectedBadge.getBadgeName());
            }
        }
    };

    private void autoScrollDown() {
        mScroll.post(new Runnable() {
            @Override
            public void run() {
                mScroll.smoothScrollTo(0, mScroll.getHeight());
            }
        });
    }

    private void sendPublication() {
        LocLookApp_NEW.showLog("PublicationFragment_NEW: sendPublication()");

        final String publicationText = mPublicationText.getText().toString();

        // если публикация без текста
        if (TextUtils.isEmpty(publicationText)) {
            showSimpleSnakeBar(mScroll, "empty_publication_text");
            return;
        }

        mMainActivity.showPD();

        Thread t = new Thread(new Runnable() {
            public void run() {
                ArrayList<String> realQuizAnswersList = new ArrayList<>();
                // если надо включить опрос в публикацию
                if(mQuizSwitch.isChecked()) {
                    for(int i=0; i<mAnswersList.size(); i++) {
                        if(!TextUtils.isEmpty(mAnswersList.get(i)))
                            realQuizAnswersList.add(mAnswersList.get(i));
                    }

                    if(realQuizAnswersList.size() < Constants.QUIZ_MIN_ANSWERS) {
                        showSimpleSnakeBar(mScroll, "need_more_answers_text");
                        return;
                    }
                }

                if(publicationController != null) {
                    try {
                        if(publicationController.createPublication(publicationText,
                                                                selectedBadge.getBadgeId(),
                                                                realQuizAnswersList,
                                                                mPhotosList,
                                                                mAnonymousSwitch.isChecked()))
                            locLookApp_NEW.showLog("PublicationFragment_NEW: sendPublication(): publication created successfully");
                        else
                            locLookApp_NEW.showLog("PublicationFragment_NEW: sendPublication(): publication NOT created");

                    } catch (Exception exc) {
                        locLookApp_NEW.showLog("PublicationFragment_NEW: sendPublication(): error: " +exc.getMessage());
                    }
                }
                else
                    locLookApp_NEW.showLog("PublicationFragment_NEW: sendPublication(): error: " +ErrorConstants.PUBLICATION_CONTROLLER_NULL_ERROR);

                // boolean publicationCreated = DBManager.getInstance().createPublication(mPublicationText.getText().toString(), selectedBadge, realQuizAnswersList, mPhotosList, mAnonymousSwitch.isChecked());

//                if(publicationCreated)
//                    mHandler.sendEmptyMessage(1);
//                else
//                    mHandler.sendEmptyMessage(-1);
            }
        });
        t.start();

        // LocLookApp.showActionSnakeBar(mScroll, "sending_publication_text", "action_publication_text", "action_result_publication_text");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap tumbnailBitmap = null;
            Bitmap photoBitmap = null;

            if (requestCode == Constants.PICK_FILE_RC) {
                try {
                    // get image from intent
                    InputStream inputStream = LocLookApp.context.getContentResolver().openInputStream(data.getData());

                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    inputStream.close();

                    // create a cropped bitmap                    
                    tumbnailBitmap = ImageDelivery.getResizedBitmap(BitmapFactory.decodeByteArray(buffer, 0, buffer.length), Constants.TUMBNAIL_WIDTH, Constants.TUMBNAIL_HEIGHT);
                    photoBitmap = ImageDelivery.getResizedBitmap(BitmapFactory.decodeByteArray(buffer, 0, buffer.length), Constants.PHOTO_WIDTH, Constants.PHOTO_HEIGHT);
                } catch (Exception exc) {
                    exc.printStackTrace();
                    LocLookApp.showSimpleSnakeBar(mScroll, "get_photo_error_text");
                }
            } else if (requestCode == Constants.TAKE_PHOTO_RC) {
                try {
                    // create a cropped bitmap
                    if(mCreatedPhotoPath != null)                        
                        tumbnailBitmap = ImageDelivery.getResizedBitmap(BitmapFactory.decodeFile(mCreatedPhotoPath), Constants.TUMBNAIL_WIDTH, Constants.TUMBNAIL_HEIGHT);
                        photoBitmap = ImageDelivery.getResizedBitmap(BitmapFactory.decodeFile(mCreatedPhotoPath), Constants.PHOTO_WIDTH, Constants.PHOTO_HEIGHT);
                } catch (Exception exc) {
                    exc.printStackTrace();
                    LocLookApp.showSimpleSnakeBar(mScroll, "get_photo_error_text");
                }
            }

            if((tumbnailBitmap != null) && (photoBitmap != null)) {
                mTumbnailsList.add(tumbnailBitmap);
                mPhotosList.add(photoBitmap);
                mGalleryPhotosAdapter.notifyDataSetChanged();

                int photosSum = mPhotosList.size();

                mGalleryPhotosRV.smoothScrollToPosition(photosSum);
                mPhotosAddedSumTV.setText(String.valueOf(photosSum));

                //if(photosSum == (VISIBLE_PHOTOS_LIMIT + 1)) {
                if(photosSum == (SettingsConstants.VISIBLE_PHOTO_MAX_SUM + 1)) {
                    mGalleryLeftScrollIV.setColorFilter(LocLookApp.getInstance().getResources().getColor(R.color.dark_grey));
                    mGalleryLeftScrollIV.setClickable(true);

                    mGalleryRightScrollIV.setColorFilter(LocLookApp.getInstance().getResources().getColor(R.color.dark_grey));
                    mGalleryRightScrollIV.setClickable(true);
                }

                //if(photosSum == PHOTOS_LIMIT) {
                if(photosSum == SettingsConstants.ADD_PHOTO_MAX_SUM) {
                    mOpenGalleryTV.setBackgroundResource(R.color.light_grey);
                    mOpenGalleryTV.setClickable(false);

                    mCreatePhotoTV.setBackgroundResource(R.color.light_grey);
                    mCreatePhotoTV.setClickable(false);
                }
            }
        }
    }

    public void getFile() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (captureIntent.resolveActivity(LocLookApp.context.getPackageManager()) != null) {
            // Create the File where the photo should go
            try {
                // создаем временный файл
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "_";
                File storageDir = LocLookApp.context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                File image  = File.createTempFile(
                        imageFileName,  /* prefix */
                        ".jpg",         /* suffix */
                        storageDir      /* directory */
                );

                // Continue only if the File was successfully created
                if (image != null) {
                    Uri outputFileUri = Uri.fromFile(image);
                    captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                    startActivityForResult(captureIntent, Constants.TAKE_PHOTO_RC);

                    mCreatedPhotoPath = image.getAbsolutePath();
                }
            } catch (IOException e) {
                e.printStackTrace();
                showSimpleSnakeBar(mScroll, "get_photo_error_text");
            }
        }
    }

    private int getSmoothOn() {
        return smoothOn;
    }

    private void setSmoothOn() {

        int smallPhotoMaxWidth = (int) locLookApp_NEW.getResources().getDimension(R.dimen.small_photo_max_width);

        if(smallPhotoMaxWidth > 0)
            smoothOn = 2 * smallPhotoMaxWidth;
    }

    private void showSimpleSnakeBar(View view, String resName) {
        int resId   = getActivity().getResources().getIdentifier("@string/" +resName, null, getActivity().getPackageName());
        String text = getActivity().getResources().getString(resId);
        Snackbar.make(view, text, Snackbar.LENGTH_LONG).show();
    }

    View.OnClickListener badgeBlockClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isBadgesBlockHidden) {
                mShowBadgesIV.setImageResource(R.drawable.hide_data);
                mChooseBadgeBlockGV.setVisibility(VISIBLE);
                isBadgesBlockHidden = false;
                autoScrollDown();
            }
            else {
                mShowBadgesIV.setImageResource(R.drawable.show_data);
                mChooseBadgeBlockGV.setVisibility(View.GONE);
                isBadgesBlockHidden = true;
            }
        }
    };

    View.OnClickListener photoBlockClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(isPhotoBlockHidden) {
                mShowPhotoBlockIV.setImageResource(R.drawable.hide_data);
                mPhotoBlockRL.setVisibility(VISIBLE);
                isPhotoBlockHidden = false;
                autoScrollDown();
            }
            else {
                mShowPhotoBlockIV.setImageResource(R.drawable.show_data);
                mPhotoBlockRL.setVisibility(View.GONE);
                isPhotoBlockHidden = true;
            }
        }
    };

    View.OnClickListener createPhotoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if (ActivityCompat.checkSelfPermission(LocLookApp.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                getFile();
            } else {
                String[] permissions = new String[]{Manifest.permission.CAMERA};
                ActivityCompat.requestPermissions(getActivity(), permissions, Constants.CAMERA_PERMISSION_CODE);
            }
        }
    };

    View.OnClickListener openGalleryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, Constants.PICK_FILE_RC);
        }
    };

    // щелчок по "кнопке Добавить вариант ответа"
    View.OnClickListener addAnswerClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            if(mAnswersList.size() < Constants.QUIZ_MAX_ANSWERS) {
                // добавляем поле с возможностью удаления
                mAnswersList.add(null);

                if (mAnswersList.size() == Constants.QUIZ_MAX_ANSWERS) {
                    mAddAnswerTV.setBackgroundResource(R.color.light_grey);
                    mAddAnswerTV.setClickable(false);
                }
                mQuizAnswersAdapter.notifyDataSetChanged();
                autoScrollDown();
            }
        }
    };

    View.OnClickListener galleryLeftScrollClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGalleryPhotosRV.smoothScrollBy(-getSmoothOn(), 0);
        }
    };

    View.OnClickListener galleryRightScrollClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mGalleryPhotosRV.smoothScrollBy(getSmoothOn(), 0);
        }
    };

    TextWatcher onPublicationTextChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            // вычисляем и отображаем кол-во символов, которые еще можно внести в поле
            //mLeftCharactersBody.setText("" + (PUBLICATION_TEXT_LIMIT - mPublicationText.length()));
            mLeftCharactersBody.setText("" + (SettingsConstants.PUBLICATION_MAX_LENGTH - mPublicationText.length()));
        }

        @Override
        public void afterTextChanged(Editable s) { }
    };

    @Override
    public void onPublicationCreateSuccess() {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("PublicationFragment_NEW: onPublicationCreateSuccess()");
    }

    @Override
    public void onPublicationCreateError(String error) {
        LocLookApp.showLog("-------------------------------------");
        LocLookApp_NEW.showLog("PublicationFragment_NEW: onPublicationCreateError(): error: " +error);
    }
}
