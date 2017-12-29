package com.framgia.englishconversation.screen.comment;

import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.data.source.remote.comment.CommentRepository;
import java.util.List;

/**
 * Listens to user actions from the UI ({@link CommentFragment}), retrieves the data and updates
 * the UI as required.
 */
final class CommentPresenter
        implements CommentContract.Presenter, CommentRepository.CommentCallback {
    private static final String TAG = CommentPresenter.class.getName();
    private CommentRepository mRepository;
    private final CommentContract.ViewModel mViewModel;

    CommentPresenter(CommentContract.ViewModel viewModel, CommentRepository commentRepository) {
        mViewModel = viewModel;
        mRepository = commentRepository;
        mRepository.setOnEndScrollListener(mViewModel.getOnEndScrollListener());
        mRepository.getComment(this, null);
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
    }

    @Override
    public void onPause() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void fetchCommentData(Comment comment) {
        mRepository.getComment(this, comment);
    }

    @Override
    public void onChildAdded(List<Comment> comments, boolean isLoadingMore) {
        mViewModel.onChildAdded(comments);
    }

    @Override
    public void onChildChanged(Comment comment) {

    }

    @Override
    public void onChildRemoved(Comment comment) {

    }

    @Override
    public void onChildMoved(Comment comment) {

    }

    @Override
    public void onCancelled(String message) {
    }
}
