package ru.org.adons.cblock.ui.viewmodel;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

import java.util.List;

import rx.Observable;

/**
 * Check all required permissions
 */
public class PermViewModel {

    private final String[] permissions = new String[]{
            Manifest.permission.READ_CALL_LOG,
            Manifest.permission.READ_CONTACTS,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.CALL_PHONE
    };

    /**
     * Check if need permissions request
     *
     * @param context Activity
     * @return array of required permissions
     */
    public Observable<List<String>> getPermissionsRequest(Context context) {
        return Observable.from(permissions)
                .filter(perm -> ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED)
                .toList();
    }

    /**
     * Check if all requested permissions were granted
     *
     * @param grantResults result from onRequestPermissionsResult()
     * @return true if all permissions granted
     */
    public Observable<Boolean> isPermissionsGranted(String[] permissions, int[] grantResults) {
        return Observable.range(0, grantResults.length)
                .map(i -> grantResults[i])
                .filter(res -> res == PackageManager.PERMISSION_GRANTED) // granted=0, not=-1
                .toList()
                .map(List::size)
                .map(size -> size == permissions.length);
    }

}
