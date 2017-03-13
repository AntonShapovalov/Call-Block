package ru.org.adons.cblock.ui.view;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.org.adons.cblock.R;
import ru.org.adons.cblock.app.CBlockApplication;
import ru.org.adons.cblock.service.BlockService;
import ru.org.adons.cblock.ui.base.BaseAppComponent;
import ru.org.adons.cblock.ui.base.BaseAppModule;
import ru.org.adons.cblock.ui.base.DaggerBaseAppComponent;
import ru.org.adons.cblock.ui.viewmodel.PermViewModel;
import ru.org.adons.cblock.utils.Logging;
import rx.Completable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IMainListener, IAddListener {

    private static final int PERMISSIONS_REQUEST = 311;

    @BindView(android.R.id.progress) ProgressBar progressBar;

    private final PermViewModel permViewModel = new PermViewModel();

    private BaseAppComponent baseAppComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        requestPermissions();
    }

    private void requestPermissions() {
        permViewModel.getRequestPermissions(this)
                .subscribe(request -> {
                    if (request.size() > 0) {
                        String[] permissions = request.toArray(new String[request.size()]);
                        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
                    } else {
                        initComponents();
                    }
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST) {
            permViewModel.isPermissionsGranted(permissions, grantResults)
                    .subscribe(yes -> {
                        if (yes) {
                            initComponents();
                        } else {
                            BlockService.stop(this);
                            clearFragmentBackStack();
                            PermFragment fragment = getPermFragment();
                            if (fragment == null) {
                                fragment = new PermFragment();
                                getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, PermFragment.PERM_FRAGMENT_TAG).commit();
                            }
                        }
                    });
        }
    }

    /**
     * Init all required Dagger components in background thread
     */
    private void initComponents() {
        showProgress();
        Logging.d(this.getClass() + ":initComponents");
        Completable.fromAction(this::getBaseAppComponent)
                .doOnUnsubscribe(this::hideProgress)
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onComponentsReady);
    }

    private void onComponentsReady() {
        Logging.d(this.getClass() + ":onComponentsReady");
        MainFragment fragment = getMainFragment();
        if (fragment == null) {
            clearFragmentBackStack(); // remove PermFragment if exists in back stack
            fragment = new MainFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, MainFragment.MAIN_FRAGMENT_TAG).commit();
        }
    }

    private MainFragment getMainFragment() {
        return (MainFragment) getFragmentManager().findFragmentByTag(MainFragment.MAIN_FRAGMENT_TAG);
    }

    private AddFragment getAddFragment() {
        return (AddFragment) getFragmentManager().findFragmentByTag(AddFragment.ADD_FRAGMENT_TAG);
    }

    private PermFragment getPermFragment() {
        return (PermFragment) getFragmentManager().findFragmentByTag(PermFragment.PERM_FRAGMENT_TAG);
    }

    private void clearFragmentBackStack() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
    }

    /**
     * @return {@link BaseAppComponent} to provide data dependencies fot all View-Models
     */
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

    @Override
    public void showAddFragment() {
        AddFragment fragment = getAddFragment();
        if (fragment == null) {
            fragment = new AddFragment();
        }
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment, AddFragment.ADD_FRAGMENT_TAG);
        transaction.addToBackStack(MainFragment.MAIN_FRAGMENT_TAG);
        transaction.commit();
    }

}
