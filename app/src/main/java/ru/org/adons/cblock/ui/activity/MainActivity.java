package ru.org.adons.cblock.ui.activity;

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
import ru.org.adons.cblock.ui.view.add.AddFragment;
import ru.org.adons.cblock.ui.view.main.MainFragment;
import ru.org.adons.cblock.ui.view.perm.PermFragment;
import ru.org.adons.cblock.ui.view.perm.PermViewModel;
import ru.org.adons.cblock.utils.Logging;
import rx.Completable;
import rx.Observable;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity implements IMainListener {

    private static final int PERMISSIONS_REQUEST = 311;

    @BindView(android.R.id.progress) ProgressBar progressBar;

    private final PermViewModel permViewModel = new PermViewModel();
    private boolean isShowWarning = false;

    private MainComponent mainComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        hideProgress();
        requestPermissions();
    }

    private void requestPermissions() {
        permViewModel.getPermissionsRequest(this)
                .doOnSubscribe(() -> isShowWarning = false)
                .flatMap(Observable::from)
                .filter(perm -> {
                    boolean isShow = ActivityCompat.shouldShowRequestPermissionRationale(this, perm);
                    isShowWarning = isShow || isShowWarning;
                    return !isShow;
                })
                .toList()
                .subscribe(request -> {
                    if (request.size() > 0) {
                        String[] permissions = request.toArray(new String[request.size()]);
                        ActivityCompat.requestPermissions(this, permissions, PERMISSIONS_REQUEST);
                    } else {
                        if (isShowWarning) {
                            showPermFragment();
                        } else {
                            initComponents();
                        }
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
                            showPermFragment();
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
        Completable.fromAction(this::getMainComponent)
                .doOnUnsubscribe(this::hideProgress)
                .subscribeOn(Schedulers.computation())
                .subscribe(this::onComponentsReady);
    }

    private void onComponentsReady() {
        Logging.d(this.getClass() + ":onComponentsReady");
        MainFragment fragment = getMainFragment();
        if (fragment == null) {
            clearFragmentBackStack();
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
     * @return {@link MainComponent} to provide data dependencies fot all View-Models
     */
    public MainComponent getMainComponent() {
        if (mainComponent == null) {
            CBlockApplication application = (CBlockApplication) getApplication();
            mainComponent = DaggerMainComponent.builder()
                    .applicationComponent(application.applicationComponent())
                    .build();
        }
        return mainComponent;
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

    private void showPermFragment() {
        BlockService.stop(this);
        clearFragmentBackStack();
        PermFragment fragment = getPermFragment();
        if (fragment == null) {
            fragment = new PermFragment();
            getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment, PermFragment.PERM_FRAGMENT_TAG).commit();
        }
    }

}
