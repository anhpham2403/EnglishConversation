package com.framgia.englishconversation.screen.timeline;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.framgia.englishconversation.BaseFragment;
import com.framgia.englishconversation.R;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.auth.AuthenicationRepository;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRemoteDataSource;
import com.framgia.englishconversation.data.source.remote.timeline.TimelineRepository;
import com.framgia.englishconversation.databinding.FragmentTimelineBinding;
import com.framgia.englishconversation.utils.navigator.Navigator;

/**
 * Timeline Screen.
 */
public class TimelineFragment extends BaseFragment {

    private TimelineContract.ViewModel mViewModel;

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new TimelineViewModel(getContext(), new Navigator(getActivity()));

        AuthenicationRepository repository =
                new AuthenicationRepository(new AuthenicationRemoteDataSource());
        TimelineRepository timelineRepository =
                new TimelineRepository(new TimelineRemoteDataSource());
        TimelineContract.Presenter presenter =
                new TimelinePresenter(mViewModel, repository, timelineRepository);

        mViewModel.setPresenter(presenter);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        FragmentTimelineBinding binding =
                DataBindingUtil.inflate(inflater, R.layout.fragment_timeline, container, false);
        binding.setViewModel((TimelineViewModel) mViewModel);
        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    public void onStop() {
        mViewModel.onStop();
        super.onStop();
    }
}
