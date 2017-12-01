package sredoc.smart_traffic.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;
import sredoc.smart_traffic.R;
import sredoc.smart_traffic.utils.CommonUtils;
import sredoc.smart_traffic.utils.NetworkUtils;
import sredoc.smart_traffic.utils.PermissionManager;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;

public class BaseActivity extends AppCompatActivity {

    public static final int REQUEST_CODE_LOCATION = 0;
    public static final int REQUEST_CODE_CAMERA = 1;
    public static final int REQUEST_CODE_STORAGE = 2;
    public static final int NAV_ICON_NONE = 0;
    public static final int NAV_ICON_BACK = 1;
    public static final int NAV_ICON_CROSS = 2;
    public Context context;
    private Unbinder unbinder;
    private Toolbar toolbar;
    private Snackbar snackbar;
    private ProgressDialog progressDialog;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private PermissionManager.PermissionListener permissionListener;

    //Support vector in drawable
    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
    }

    @Override protected void onStart() {
        super.onStart();
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void setUnbinder(Unbinder unbinder) {
        this.unbinder = unbinder;
    }

    public void setUpToolbar(Toolbar toolbar, int navIconType) {
        if (toolbar == null) return;
        this.toolbar = toolbar;
        setSupportActionBar(toolbar);
        if (getSupportActionBar() == null) return;
        if (navIconType == NAV_ICON_NONE) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        } else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            Drawable navIcon = null;
            switch (navIconType) {
                case NAV_ICON_BACK:
                    navIcon = ContextCompat.getDrawable(context, R.drawable.abc_ic_ab_back_material);
                    break;
                case NAV_ICON_CROSS:
//                    navIcon = ContextCompat.getDrawable(context,R.drawable.ic_close_accent);
                    break;
            }
            getSupportActionBar().setHomeAsUpIndicator(navIcon);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setToolbarTitle(String title) {
        if (toolbar == null) return;
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle != null) tvTitle.setText(title);
    }

    public void setToolbarTitle(@StringRes int id) {
        if (toolbar == null) return;
        TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        if (tvTitle != null) tvTitle.setText(id);
    }

    public void setToolbarAction(String action, ToolbarActionClickListener toolbarActionClickListener) {
        if (toolbar == null) return;
        TextView tvAction = (TextView) findViewById(R.id.tvAction);
        if (tvAction != null) {
            tvAction.setVisibility(View.VISIBLE);
            tvAction.setText(action);
            tvAction.setOnClickListener(v -> {
                if (toolbarActionClickListener != null) {
                    toolbarActionClickListener.onToolbarActionClicked();
                }
            });
        }
    }

    public void hideToolbarAction() {
        if (toolbar == null) return;
        TextView tvAction = (TextView)findViewById(R.id.tvAction);
        if (tvAction != null) {
            tvAction.setVisibility(View.GONE);
        }
    }

    public void hideBackButton(){
        if (toolbar == null) return;
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }


    public boolean isNetworkConnected() {
        return NetworkUtils.isConnectedToInternet(this);
    }

//    public void showError(String message, String action, final Runnable responseAction) {
//        if(TextUtils.isEmpty(message)) return;
//        snackbar = Snackbar.make(findViewById(R.id.clContainer), message, Snackbar.LENGTH_SHORT);
//        snackbar.setAction(action, v -> {
//            dismissSnackbar();
//            if (responseAction != null) new Handler().postDelayed(responseAction, 500);
//        });
//        View sbView = snackbar.getView();
//        TextView tvText = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
//        TextView tvAction = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_action);
//        snackbar.show();
//    }

    public void setProgressDialog(boolean active) {
        if(active){
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            if (context == null) return;
//            progressDialog = CommonUtils.showLoadingDialog(context);
        }else{
            if (progressDialog != null && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        }
    }

    public void dismissSnackbar() {
        if (snackbar != null) snackbar.dismiss();
    }

    public void showToast(String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public void showToast(@StringRes int resId) {
        Toast.makeText(context, resId, Toast.LENGTH_SHORT).show();
    }

    public CompositeDisposable getCompositeDisposable() {return compositeDisposable;}

    /**
     * Call this to request permission(s)
     *
     * @param requestCode
     */
    public void requestPermission(int requestCode, PermissionManager.PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        switch (requestCode) {
            case REQUEST_CODE_LOCATION:
                PermissionManager.requestPermissions(permissionListener,this, requestCode, new String[]{
                        ACCESS_FINE_LOCATION,
                        ACCESS_COARSE_LOCATION
                });
                break;
        }
    }

    /**
     * Toggle menu item visibility
     * @param menu
     * @param id
     * @param visibility
     */
    public void toggleMenuItemVisibility(Menu menu,int id,boolean visibility){
        if(menu == null) return;
        MenuItem menuItem = menu.findItem(id);
        if(menuItem == null) return;
        menuItem.setVisible(visibility);
    }

    public void showUpdateGooglePlayServicesDialog(){
        new MaterialDialog.Builder(context)
                .title("Update Google Play Services")
                .content("This app won\\'t run unless you update GooglePlay services. Do you want to update GooglePlay services?")
                .canceledOnTouchOutside(false)
                .positiveText("Yes")
                .positiveColor(ContextCompat.getColor(context, R.color.accent))
                .negativeText("Later")
                .negativeColor(ContextCompat.getColor(context, R.color.accent_light))
                .onPositive((dialog, which) -> {
                    openApp(context, CommonUtils.APP_GOOGLE_PLAY_SERVICES);
                })
                .onNegative((dialog, which) -> finish())
                .show();
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionManager.onRequestPermissionsResult(this, permissionListener, requestCode, permissions, grantResults);
    }

    @Override public void onBackPressed() {
        dismissSnackbar();
        super.onBackPressed();
    }

    @Override protected void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (compositeDisposable != null) compositeDisposable.clear();
        super.onDestroy();
    }

    public interface ToolbarActionClickListener {
        void onToolbarActionClicked();
    }
    public static void openApp(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        try {
            Intent i = manager.getLaunchIntentForPackage(packageName);
            if (i == null) {
                throw new PackageManager.NameNotFoundException();
            }
            i.addCategory(Intent.CATEGORY_LAUNCHER);
            context.startActivity(i);
        } catch (PackageManager.NameNotFoundException e) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                        ("market://details?id=" + packageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play" +
                        ".google.com/store/apps/details?id=" + packageName)));
            }
        }
    }

}
