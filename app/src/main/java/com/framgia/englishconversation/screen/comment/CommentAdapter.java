package com.framgia.englishconversation.screen.comment;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.englishconversation.BR;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.model.MediaModel;
import com.framgia.englishconversation.databinding.ItemCommentAudioBinding;
import com.framgia.englishconversation.databinding.ItemCommentImageBinding;
import com.framgia.englishconversation.databinding.ItemCommentOnlyTextBinding;
import com.framgia.englishconversation.databinding.ItemCommentVideoBinding;
import com.framgia.englishconversation.utils.Constant;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import java.util.List;

/**
 * Created by anh on 12/21/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.BaseCommentViewHolder> {
    private List<Comment> mComments;

    public CommentAdapter(List<Comment> comments) {
        mComments = comments;
    }

    public void updateData(List<Comment> comments) {
        if (comments == null) {
            return;
        }
        mComments.addAll(comments);
        notifyDataSetChanged();
    }

    public void updateData(Comment comment) {
        mComments.add(0, comment);
        notifyItemInserted(0);
    }

    @Override
    public int getItemViewType(int position) {
        return mComments.get(position).getCommentType();
    }

    @Override
    public CommentAdapter.BaseCommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case MediaModel.MediaType.ONLY_TEXT:
                ItemCommentOnlyTextBinding onlyTextBinding =
                        ItemCommentOnlyTextBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new OnlyTextViewHolder(onlyTextBinding);
            case MediaModel.MediaType.AUDIO:
                ItemCommentAudioBinding audioBinding =
                        ItemCommentAudioBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new AudioViewHolder(audioBinding);

            case MediaModel.MediaType.VIDEO:
                ItemCommentVideoBinding videoBinding =
                        ItemCommentVideoBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new VideoViewHolder(videoBinding);

            case MediaModel.MediaType.IMAGE:
                ItemCommentImageBinding imageBinding =
                        ItemCommentImageBinding.inflate(LayoutInflater.from(parent.getContext()),
                                parent, false);
                return new ImageViewHolder(imageBinding);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(BaseCommentViewHolder holder, int position) {
        holder.bindData(mComments.get(position));
    }

    @Override
    public int getItemCount() {
        return mComments != null ? mComments.size() : 0;
    }

    public Comment getLastComment() {
        return mComments != null && !mComments.isEmpty() ? mComments.get(mComments.size() - 1)
                : null;
    }

    /**
     * Display comment model with only text (without media)
     */
    public class OnlyTextViewHolder extends BaseCommentViewHolder {
        private ItemCommentOnlyTextBinding mBinding;

        public OnlyTextViewHolder(ItemCommentOnlyTextBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display comment model with audio media
     */
    public class AudioViewHolder extends BaseCommentViewHolder {
        private ItemCommentAudioBinding mBinding;

        public AudioViewHolder(ItemCommentAudioBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setViewModel(new MediaViewModel(model, mBinding.getRoot().getContext()));
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display comment model with image media
     */
    public class ImageViewHolder extends BaseCommentViewHolder {
        private ItemCommentImageBinding mBinding;

        public ImageViewHolder(ItemCommentImageBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setCommentViewModel(model);
            mBinding.executePendingBindings();
        }
    }

    /**
     * Display comment model with video media
     */
    public class VideoViewHolder extends BaseCommentViewHolder {
        private ItemCommentVideoBinding mBinding;

        VideoViewHolder(ItemCommentVideoBinding itemView) {
            super(itemView.getRoot());
            mBinding = itemView;
        }

        @Override
        public void bindData(Comment model) {
            mBinding.setViewModel(new MediaViewModel(model, mBinding.getRoot().getContext()));
            mBinding.executePendingBindings();
        }
    }

    public class MediaViewModel extends BaseObservable {
        private Comment mComment;
        private SimpleExoPlayer mExoPlayer;

        public MediaViewModel(Comment comment, Context context) {
            mComment = comment;
            init(context);
        }

        @Bindable
        public Comment getComment() {
            return mComment;
        }

        public void setComment(Comment comment) {
            mComment = comment;
            notifyPropertyChanged(BR.comment);
        }

        @Bindable
        public SimpleExoPlayer getExoPlayer() {
            return mExoPlayer;
        }

        public void setExoPlayer(SimpleExoPlayer exoPlayer) {
            mExoPlayer = exoPlayer;
            notifyPropertyChanged(BR.exoPlayer);
        }

        public void init(Context context) {
            final SimpleExoPlayer player =
                    ExoPlayerFactory.newSimpleInstance(context, new DefaultTrackSelector());
            setExoPlayer(player);
            Uri uri = Uri.parse(mComment.getMediaModel().getUrl());
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
            DefaultHttpDataSourceFactory dataSourceFactory =
                    new DefaultHttpDataSourceFactory(Constant.USER_AGENT);
            final ExtractorMediaSource mediaSource =
                    new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
            player.addListener(new Player.DefaultEventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    super.onPlayerStateChanged(playWhenReady, playbackState);
                    if (playbackState == Player.STATE_IDLE) {
                        player.prepare(mediaSource, true, false);
                    }
                }
            });
        }
    }

    /**
     * Base timeline model
     */
    public abstract class BaseCommentViewHolder extends RecyclerView.ViewHolder {

        public BaseCommentViewHolder(View itemView) {
            super(itemView);
        }

        public abstract void bindData(Comment model);
    }
}
