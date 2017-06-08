package ru.org.adons.cblock.ui.view.add;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.model.CallLogItem;
import ru.org.adons.cblock.ui.activity.IMainListener;
import ru.org.adons.cblock.ui.fragment.BaseFragment;
import ru.org.adons.cblock.utils.UiUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Add Phone View, show list of incoming or missed calls
 */
public class AddFragment extends BaseFragment<IMainListener> {

    public static final String ADD_FRAGMENT_TAG = "ADD_FRAGMENT_TAG";

    @BindView(R.id.recycler_view_call_log) RecyclerView callList;
    @BindView(R.id.image_button_close) ImageButton buttonClose;
    @BindView(R.id.image_button_done) ImageButton buttonDone;
    private Unbinder unbinder;

    private CallLogAdapter adapter;

    @Inject AddViewModel addViewModel;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListener(activity, IMainListener.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener.getMainComponent().inject(this);
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
        adapter = new CallLogAdapter();
        UiUtils.initList(getActivity(), callList, adapter);
        buttonClose.setOnClickListener(v -> listener.onBackPressed());
        buttonDone.setOnClickListener(v -> {
            addViewModel.addPhones(adapter.getItems());
            listener.onBackPressed();
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        addViewModel.getCallLogList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnSubscribe(() -> listener.showProgress())
                .subscribe(this::setBlockedItems, this::onError);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void setBlockedItems(List<CallLogItem> items) {
        addViewModel.setBlockedItems(items)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnSubscribe(() -> listener.showProgress())
                .doOnUnsubscribe(() -> listener.hideProgress())
                .subscribe(adapter::setItems, this::onError);
    }

}
