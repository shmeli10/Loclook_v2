package com.androiditgroup.loclook.v2.ui.publication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.Switch;
import android.widget.TextView;

import com.androiditgroup.loclook.v2.LocLookApp;
import com.androiditgroup.loclook.v2.R;
import com.androiditgroup.loclook.v2.adapters.BadgesAdapter;
import com.androiditgroup.loclook.v2.adapters.GalleryListAdapter;
import com.androiditgroup.loclook.v2.adapters.QuizAnswersAdapter;
import com.androiditgroup.loclook.v2.models.Badge;
import com.androiditgroup.loclook.v2.models.Publication;
import com.androiditgroup.loclook.v2.ui.auth.AuthActivity;
import com.androiditgroup.loclook.v2.ui.general.MainActivity;
import com.androiditgroup.loclook.v2.utils.Constants;
import com.androiditgroup.loclook.v2.utils.DBManager;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightGridView;
import com.androiditgroup.loclook.v2.utils.ExpandableHeightListView;
import com.androiditgroup.loclook.v2.utils.ParentFragment;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.view.View.VISIBLE;

/**
 * Created by sostrovschi on 1/25/17.
 */

public class PublicationFragment extends    ParentFragment
                                 implements View.OnClickListener,
                                            TextWatcher,
                                            CompoundButton.OnCheckedChangeListener,
                                            QuizAnswersAdapter.QuizAnswerCallback {

    private static LayoutInflater mInflater;

    private MainActivity mMainActivity;

    private RelativeLayout mPublicationContainer;
    private RelativeLayout mBadgeBlock;
    private RelativeLayout mQuizAnswersBlock;
    private RelativeLayout mShowPhotoBlockRL;
    private RelativeLayout mPhotoBlockRL;

    private ScrollView mScroll;
    private EditText mPublicationText;
    private TextView mLeftCharactersBody;
    private TextView mAnonymousStateTV;
    private TextView mQuizStateTV;
    private TextView mAddAnswerTV;
    private TextView mSelectedBadgeTV;
    private TextView mOpenGalleryTV;
    private TextView mCreatePhotoTV;
    private TextView mPhotosAddedSumTV;

    private Switch mAnonymousSwitch;
    private Switch mQuizSwitch;

    private ArrayList<String> mAnswersList = new ArrayList<>();
    private ArrayList<Bitmap> mPhotosList = new ArrayList<>();

    private ImageView mShowBadgesIV;
    private ImageView mSelectedBadgeIV;
    private ImageView mShowPhotoBlockIV;
    private ImageView mGalleryLeftScrollIV;
    private ImageView mGalleryRightScrollIV;

    private ExpandableHeightListView mQuizAnswersList;
    private ExpandableHeightGridView mChooseBadgeBlockGV;
    private RecyclerView mGalleryPhotosRV;

    private QuizAnswersAdapter mQuizAnswersAdapter;
    private BadgesAdapter mBadgesAdapter;
    private GalleryListAdapter mGalleryPhotosAdapter;

    private final int anonymousSwitchResId      = R.id.Publication_AnonymousSwitchBTN;
    private final int quizSwitchResId           = R.id.Publication_QuizSwitchBTN;
    private final int addAnswerResId            = R.id.Publication_QuizAnswersBlockAddAnswer;
    private final int openGalleryResId          = R.id.Publication_OpenGallery;
    private final int createPhotoResId          = R.id.Publication_CreatePhoto;
    private final int showPhotoBlockResId       = R.id.Publication_ShowPhotoBlock;
    private final int badgeBlockResId           = R.id.Publication_BadgeBlock;
    private final int galleryLeftScrollResId    = R.id.Publication_GalleryLeftScrollIV;
    private final int galleryRightScrollResId   = R.id.Publication_GalleryRightScrollBtn;

    private final int PUBLICATION_TEXT_LIMIT    = LocLookApp.getInstance().getResources().getInteger(R.integer.publication_length);
    private final int PHOTOS_LIMIT              = LocLookApp.getInstance().getResources().getInteger(R.integer.max_photos_sum);
    private final int VISIBLE_PHOTOS_LIMIT      = LocLookApp.getInstance().getResources().getInteger(R.integer.visible_photos_sum);
    private final int SMALL_PHOTO_WIDTH_LIMIT   = (int) LocLookApp.getInstance().getResources().getDimension(R.dimen.small_photo_max_width);
    private final int SMOOTH_ON                 = 2 * SMALL_PHOTO_WIDTH_LIMIT;

    private boolean isAvailableToDelete;
    private boolean isBadgesBlockHidden;
    private boolean isPhotoBlockHidden;

    private Badge selectedBadge = LocLookApp.badgesList.get(0);
    private String mCreatedPhotoPath;

    public PublicationFragment() {
        // Required empty public constructor
    }

    public static PublicationFragment newInstance(LayoutInflater inflater) {
        mInflater = inflater;
        Bundle args = new Bundle();
        PublicationFragment fragment = new PublicationFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mMainActivity = (MainActivity) getActivity();

        View view = inflater.inflate(R.layout.fragment_publication, container, false);

        MainActivity.mSlidingPaneLayout.setEnableTouchEvents(false);

        // selectedBadge = LocLookApp.badgesList.get(0);
        mScroll = (ScrollView) view.findViewById(R.id.Publication_ScrollView);

        mPublicationText = (EditText) view.findViewById(R.id.Publication_PublicationTextET);
        mPublicationText.addTextChangedListener(this);

        mPublicationContainer   = (RelativeLayout) view.findViewById(R.id.Publication_Container);
        mQuizAnswersBlock       = (RelativeLayout) view.findViewById(R.id.Publication_QuizAnswersBlock);
        mPhotoBlockRL           = (RelativeLayout) view.findViewById(R.id.Publication_PhotoBlock);

        mBadgeBlock = (RelativeLayout) view.findViewById(badgeBlockResId);
        mBadgeBlock.setOnClickListener(this);

        mShowPhotoBlockRL = (RelativeLayout) view.findViewById(showPhotoBlockResId);
        mShowPhotoBlockRL.setOnClickListener(this);

        mAnonymousStateTV = (TextView) view.findViewById(R.id.Publication_AnonymousStateTV);
        mQuizStateTV = (TextView) view.findViewById(R.id.Publication_QuizStateTV);
        mPhotosAddedSumTV = (TextView) view.findViewById(R.id.Publication_PhotosAddedSumTV);

        mLeftCharactersBody = (TextView) view.findViewById(R.id.Publication_LeftCharactersBodyTV);
        mLeftCharactersBody.setText("" +PUBLICATION_TEXT_LIMIT);

        mOpenGalleryTV = (TextView) view.findViewById(openGalleryResId);
        mOpenGalleryTV.setOnClickListener(this);

        mCreatePhotoTV = (TextView) view.findViewById(R.id.Publication_CreatePhoto);
        mCreatePhotoTV.setOnClickListener(this);

        mAnonymousSwitch = (Switch) view.findViewById(anonymousSwitchResId);
        mAnonymousSwitch.setOnCheckedChangeListener(this);

        mQuizSwitch = (Switch) view.findViewById(R.id.Publication_QuizSwitchBTN);
        mQuizSwitch.setOnCheckedChangeListener(this);

        mAddAnswerTV = (TextView) view.findViewById(addAnswerResId);
        mAddAnswerTV.setOnClickListener(this);

        for(int i=0; i<Constants.QUIZ_MIN_ANSWERS; i++)
            mAnswersList.add(null);

        mQuizAnswersAdapter = new QuizAnswersAdapter(mInflater, mAnswersList, this);
        mQuizAnswersList = (ExpandableHeightListView) view.findViewById(R.id.Publication_QuizAnswersList);
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

        mBadgesAdapter = new BadgesAdapter(mInflater);
        mChooseBadgeBlockGV.setAdapter(mBadgesAdapter);
        mChooseBadgeBlockGV.setOnItemClickListener(onBadgeClickListener);

        mGalleryPhotosAdapter = new GalleryListAdapter(mPhotosList);
        mGalleryPhotosRV = (RecyclerView) view.findViewById(R.id.Publication_GalleryRecyclerView);
        mGalleryPhotosRV.setAdapter(mGalleryPhotosAdapter);

        mGalleryLeftScrollIV = (ImageView) view.findViewById(galleryLeftScrollResId);
        mGalleryLeftScrollIV.setOnClickListener(this);

        mGalleryRightScrollIV = (ImageView) view.findViewById(galleryRightScrollResId);
        mGalleryRightScrollIV.setOnClickListener(this);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public String getFragmentTitle() {
        return LocLookApp.getInstance().getString(R.string.publication_text);
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        // вычисляем и отображаем кол-во символов, которые еще можно внести в поле
        mLeftCharactersBody.setText("" + (PUBLICATION_TEXT_LIMIT - mPublicationText.length()));
    }

    @Override
    public void afterTextChanged(Editable editable) { }

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
    public void onClick(View view) {

        switch (view.getId()) {
            // щелчок по "кнопке Добавить вариант ответа"
            case addAnswerResId:
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

                break;
            case badgeBlockResId:
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
                break;
            case showPhotoBlockResId:
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
                break;
            case openGalleryResId:
                Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, Constants.PICK_FILE_RC);
                break;
            case createPhotoResId:
                if (ActivityCompat.checkSelfPermission(LocLookApp.context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    getFile();
                } else {
                    String[] permissions = new String[]{Manifest.permission.CAMERA};
                    ActivityCompat.requestPermissions(getActivity(), permissions, Constants.CAMERA_PERMISSION_CODE);
                }
                break;
            case galleryLeftScrollResId:
                mGalleryPhotosRV.smoothScrollBy(-SMOOTH_ON, 0);
                break;
            case galleryRightScrollResId:
                mGalleryPhotosRV.smoothScrollBy(SMOOTH_ON, 0);
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
            // Badge selectedBadge = LocLookApp.badgesList.get(position);
            selectedBadge = LocLookApp.badgesList.get(position);

            if(selectedBadge != null) {
                mSelectedBadgeIV.setImageResource(selectedBadge.getIconResId());
                mSelectedBadgeTV.setText(selectedBadge.getName());
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
        Log.e("ABC", "PublicationFragment: sendPublication()");

        // если публикация без текста
        if (TextUtils.isEmpty(mPublicationText.getText())) {
            LocLookApp.showSimpleSnakeBar(mPublicationContainer, "empty_publication_text");
            return;
        }

        ArrayList<String> realQuizAnswersList = new ArrayList<>();
        // если надо включить опрос в публикацию
        if(mQuizSwitch.isChecked()) {
            for(int i=0; i<mAnswersList.size(); i++) {
                if(!TextUtils.isEmpty(mAnswersList.get(i)))
                    realQuizAnswersList.add(mAnswersList.get(i));
            }

            if(realQuizAnswersList.size() < Constants.QUIZ_MIN_ANSWERS) {
                LocLookApp.showSimpleSnakeBar(mPublicationContainer, "need_more_answers_text");
                return;
            }
        }

        Publication publication = DBManager.getInstance().createPublication(mPublicationText.getText().toString(), selectedBadge, realQuizAnswersList, mPhotosList, mAnonymousSwitch.isChecked());

        if(publication != null) {
            Log.e("ABC", "PublicationFragment: sendPublication(): publication is saved");
            LocLookApp.publicationsMap.put(publication.getId(), publication);
            mMainActivity.refreshFeed = true;
            mMainActivity.onBackPressed();
        }
        else {
            LocLookApp.showSimpleSnakeBar(mPublicationContainer, "publication_send_error_text");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            Bitmap photoBitmap = null;

            if (requestCode == Constants.PICK_FILE_RC) {
                try {
                    // get image from intent
                    InputStream inputStream = LocLookApp.context.getContentResolver().openInputStream(data.getData());

                    byte[] buffer = new byte[inputStream.available()];
                    inputStream.read(buffer);
                    inputStream.close();

                    // create a cropped bitmap
                    photoBitmap = cropBitmap(BitmapFactory.decodeByteArray(buffer, 0, buffer.length));
                } catch (Exception exc) {
                    exc.printStackTrace();
                    LocLookApp.showSimpleSnakeBar(mPublicationContainer, "get_photo_error_text");
                }
            } else if (requestCode == Constants.TAKE_PHOTO_RC) {
                try {
                    // create a cropped bitmap
                    if(mCreatedPhotoPath != null)
                        photoBitmap = cropBitmap(BitmapFactory.decodeFile(mCreatedPhotoPath));
                } catch (Exception exc) {
                    exc.printStackTrace();
                    LocLookApp.showSimpleSnakeBar(mPublicationContainer, "get_photo_error_text");
                }
            }

            if(photoBitmap != null) {
                mPhotosList.add(photoBitmap);
                mGalleryPhotosAdapter.notifyDataSetChanged();

                int photosSum = mPhotosList.size();

                mGalleryPhotosRV.smoothScrollToPosition(photosSum);
                mPhotosAddedSumTV.setText(String.valueOf(photosSum));

                if(photosSum == (VISIBLE_PHOTOS_LIMIT + 1)) {
                    mGalleryLeftScrollIV.setColorFilter(LocLookApp.getInstance().getResources().getColor(R.color.dark_grey));
                    mGalleryLeftScrollIV.setClickable(true);

                    mGalleryRightScrollIV.setColorFilter(LocLookApp.getInstance().getResources().getColor(R.color.dark_grey));
                    mGalleryRightScrollIV.setClickable(true);
                }

                if(photosSum == PHOTOS_LIMIT) {
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
                LocLookApp.showSimpleSnakeBar(mPublicationContainer, "get_photo_error_text");
            }
        }
    }

    private Bitmap cropBitmap(Bitmap src) {
        int width = LocLookApp.getPixelsFromDp(100);
        int height = LocLookApp.getPixelsFromDp(75);
        return ThumbnailUtils.extractThumbnail(src, width, height, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
    }
}
