package com.framgia.englishconversation.screen.comment;

import com.framgia.englishconversation.BasePresenter;
import com.framgia.englishconversation.BaseViewModel;
import com.framgia.englishconversation.data.model.Comment;
import com.framgia.englishconversation.utils.OnEndScrollListener;
import java.util.List;

/**
 * This specifies the contract between the view and the presenter.
 */
interface CommentContract {
    /**
     * View.
     */
    interface ViewModel extends BaseViewModel<Presenter> {
        void onPause();

        void onResume();

        void onChildAdded(List<Comment> comment);

        void onEndScrolled();
    }

    /**
     * Presenter.
     */
    interface Presenter extends BasePresenter {
        void onPause();

        void onResume();

        void fetchCommentData(Comment lastComments);
    }
}
