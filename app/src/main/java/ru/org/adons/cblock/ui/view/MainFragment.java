package ru.org.adons.cblock.ui.view;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.ui.adapter.BlockListAdapter;
import ru.org.adons.cblock.ui.base.BaseFragment;
import ru.org.adons.cblock.ui.viewmodel.MainViewModel;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Main View, show list of blocking numbers
 */
public class MainFragment extends BaseFragment<IMainListener> {

    static final String MAIN_FRAGMENT_TAG = "MAIN_FRAGMENT_TAG";

    @BindView(R.id.recycler_view_block_list) RecyclerView blockList;
    @BindView(R.id.switch_block_service) Switch switchService;
    @BindView(R.id.fab_add) FloatingActionButton fabAdd;
    private Unbinder unbinder;

    private final MainViewModel mainViewModel = new MainViewModel();
    private final BlockListAdapter adapter = new BlockListAdapter();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setListener(activity, IMainListener.class);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listener.getBaseAppComponent().inject(mainViewModel);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initList(getActivity(), blockList, adapter);
        //
        fabAdd.setOnClickListener(v -> listener.showAddFragment());
        //
        switchService.setOnCheckedChangeListener((v, isChecked) -> mainViewModel.changeServiceState(isChecked)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(this::showToast, this::onError));
    }

    @Override
    public void onResume() {
        super.onResume();
        mainViewModel.getBlockList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .doOnSubscribe(() -> listener.showProgress())
                .doOnUnsubscribe(() -> listener.hideProgress())
                .subscribe(adapter::setItems, this::onError);
        //
        mainViewModel.getSwitchState()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(bindToLifecycle())
                .subscribe(switchService::setChecked, this::onError);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private void showToast(String text) {
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
    }

}
