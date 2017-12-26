package com.framgia.englishconversation.screen.audiodetail;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.os.Build;

import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.TimelineModel;
import com.framgia.englishconversation.utils.Constant;
import com.framgia.englishconversation.utils.navigator.Navigator;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

/**
 * Created by fs-sournary.
 * Date on 12/19/17.
 * Description: ViewModel of audio detail screen
 */

public class AudioDetailViewModel extends BaseObservable implements AudioDetailContract.View {

    public static final int INDEX_AUDIO = 0;

    private int mCurrentWindow;
    private long mPlaybackPosition;
    private String mPathAudio;
    private SimpleExoPlayer mExoPlayer;
    private AudioDetailActivity mActivity;
    private TimelineModel mTimelineModel;
    private Navigator mNavigator;
    private AudioDetailContract.Presenter mPresenter;

    public AudioDetailViewModel(AudioDetailActivity activity, TimelineModel timelineModel) {
        mActivity = activity;
        mTimelineModel = timelineModel;
        mNavigator = new Navigator(activity);
        initComponents();
    }

    @Override
    public void onStart() {
        mPresenter.onStart();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            setUpExoPlayer();
        }
    }

    @Override
    public void onResume() {
        if (Util.SDK_INT < Build.VERSION_CODES.M && mExoPlayer == null) {
            setUpExoPlayer();
        }
    }

    @Override
    public void onPause() {
        if (Util.SDK_INT <= Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        mPresenter.onStop();
        if (Util.SDK_INT > Build.VERSION_CODES.M) {
            releasePlayer();
        }
    }

    private void initComponents() {
        mPathAudio = mTimelineModel.getMedias().get(INDEX_AUDIO).getUrl();
    }

    private void setUpExoPlayer() {
        if (mExoPlayer != null) {
            return;
        }
        mExoPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mActivity),
                new DefaultTrackSelector(),
                new DefaultLoadControl());
        Uri uriAudio = Uri.parse(mPathAudio);
        mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
        mExoPlayer.prepare(getMediaSource(uriAudio), true, false);
        setExoPlayer(mExoPlayer);
    }

    private MediaSource getMediaSource(Uri uri) {
        return new ExtractorMediaSource(
                uri,
                new DefaultHttpDataSourceFactory(Constant.USER_AGENT),
                new DefaultExtractorsFactory(), null, null);
    }

    private void releasePlayer() {
        if (mExoPlayer == null) {
            return;
        }
        mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
        mPlaybackPosition = mExoPlayer.getCurrentPosition();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    public void onFinishClick() {
        mNavigator.finishBySharedElementIfAvailable();
    }

    @Bindable
    public SimpleExoPlayer getExoPlayer() {
        return mExoPlayer;
    }

    public void setExoPlayer(SimpleExoPlayer exoPlayer) {
        mExoPlayer = exoPlayer;
        notifyPropertyChanged(BR.exoPlayer);
    }

    @Override
    public void setPresenter(AudioDetailContract.Presenter presenter) {
        mPresenter = presenter;
    }

    public TimelineModel getTimelineModel() {
        return mTimelineModel;
    }
}
