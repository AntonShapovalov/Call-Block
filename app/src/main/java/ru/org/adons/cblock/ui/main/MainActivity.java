package ru.org.adons.cblock.ui.main;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.ui.base.BaseAppComponent;
import ru.org.adons.cblock.ui.base.BaseAppModule;
import ru.org.adons.cblock.ui.base.DaggerBaseAppComponent;
import ru.org.adons.cblock.utils.Logging;
import rx.Completable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IMainListener {

    private BaseAppComponent baseAppComponent;

    @BindView(android.R.id.progress) ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initComponents(this::getBaseAppComponent);
    }

    /**
     * Init all required Dagger components in background thread
     */
    private void initComponents(Action0 action) {
        Completable.fromAction(action)
                .doOnSubscribe(s -> {
                    showProgress();
                    Logging.d(this.getClass() + ":initComponents: subscribe");
                })
                .doOnUnsubscribe(() -> {
                    Logging.d(this.getClass() + ":initComponents: unsubscribe");
                    hideProgress();
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this::onComponentsReady);
    }

    private void onComponentsReady() {
        MainFragment fragment = getMainFragment();
        if (fragment == null) {
            fragment = new MainFragment();
            getFragmentManager().beginTransaction().add(R.id.fragment_container, fragment, MainFragment.MAIN_FRAGMENT_TAG).commit();
        }
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.MAIN_FRAGMENT_TAG);
    }


    public BaseAppComponent getBaseAppComponent() {
        if (baseAppComponent == null) {
            CBlockApplication application = (CBlockApplication) getApplication();
            baseAppComponent = DaggerBaseAppComponent.builder()
                    .applicationComponent(application.applicationComponent())
                    .baseAppModule(new BaseAppModule())
                    .build();
        }
        return baseAppComponent;
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showError(String message) {
        runOnUiThread(() -> Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show());
    }

}
