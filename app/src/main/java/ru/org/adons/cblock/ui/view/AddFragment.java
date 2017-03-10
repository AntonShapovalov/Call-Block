package ru.org.adons.cblock.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.ui.adapter.CallLogAdapter;
import ru.org.adons.cblock.ui.base.BaseFragment;
import ru.org.adons.cblock.ui.viewmodel.AddViewModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Add Phone View, show list of incoming or missed calls
 */
public class AddFragment extends BaseFragment<IAddListener> {

    static final String ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG";

    @BindView(R.id.recycler_view_call_log) RecyclerView callList;
    private Unbinder unbinder;

    private final AddViewModel addViewModel = new AddViewModel();
    private final CallLogAdapter adapter = new CallLogAdapter();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListener(activity, IAddListener.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener.getBaseAppComponent().inject(addViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(getActivity(), callList, adapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        addViewModel.getCallLogList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnSubscribe(() -> listener.showProgress())
                .doOnNext(items -> listener.hideProgress())
                .subscribe(adapter::setItems, this::onError);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
