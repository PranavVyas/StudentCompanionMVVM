package com.vyas.pranav.studentcompanion.ui.activities;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.checkbox.MaterialCheckBox;
import com.google.firebase.messaging.FirebaseMessaging;
import com.orhanobut.logger.Logger;
import com.vyas.pranav.studentcompanion.R;
import com.vyas.pranav.studentcompanion.data.SharedPreferencesUtils;
import com.vyas.pranav.studentcompanion.repositories.SharedPreferencesRepository;
import com.vyas.pranav.studentcompanion.utils.AppExecutors;
import com.vyas.pranav.studentcompanion.utils.ConverterUtils;
import com.vyas.pranav.studentcompanion.utils.GlideApp;
import com.vyas.pranav.studentcompanion.viewmodels.NotificationPrefernceViewModel;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationPreferenceActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_notification_preference)
    Toolbar toolbar;
    @BindView(R.id.checkbox_notifications_event)
    MaterialCheckBox checkBoxEvent;
    @BindView(R.id.checkbox_notifications_new_item)
    MaterialCheckBox checkBoxNewItem;
    @BindView(R.id.placeholder_notification_preference_no_connection)
    ConstraintLayout placeHOlderConnection;
    @BindView(R.id.image_placeholder_notification_preference)
    ImageView imagePlaceHolder;
    @BindView(R.id.scroll_notification_preference)
    ScrollView scrollContainer;

    private NotificationPrefernceViewModel notificationsViewModel;
    private SharedPreferencesUtils sharedPreferencesUtils;
    private FirebaseMessaging instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferencesRepository.setUserTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_preference);
        ButterKnife.bind(this);
        notificationsViewModel = ViewModelProviders.of(this).get(NotificationPrefernceViewModel.class);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        placeHOlderConnection.setVisibility(View.GONE);
        sharedPreferencesUtils = notificationsViewModel.getPrefenceUtils();
        initViews();
        instance = notificationsViewModel.getInstance();
        checkBoxEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferencesUtils.setNotificationForEvent(b);
                refreshEventSubscription();
            }
        });

        checkBoxNewItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                sharedPreferencesUtils.setNotificationForNewItemInShop(b);
                refreshNewItemSubscription();
            }
        });
    }

    private void initViews() {
        GlideApp.with(this)
                .load(R.drawable.image_no_connection_placeholder)
                .into(imagePlaceHolder);
        checkBoxNewItem.setChecked(sharedPreferencesUtils.isEventNotificationEnabed());
        checkBoxNewItem.setChecked(sharedPreferencesUtils.isNewItemShopNotificationEnabled());
    }

    void refreshEventSubscription() {
        if (sharedPreferencesUtils.isEventNotificationEnabed()) {
            instance.subscribeToTopic("events").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(NotificationPreferenceActivity.this, "Subscribed to Event!", Toast.LENGTH_SHORT).show();
                    } else {
                        checkBoxEvent.setChecked(false);
                        Toast.makeText(NotificationPreferenceActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            instance.unsubscribeFromTopic("events").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(NotificationPreferenceActivity.this, "Unsubscribed to Event!", Toast.LENGTH_SHORT).show();
                    } else {
                        checkBoxEvent.setChecked(true);
                        Toast.makeText(NotificationPreferenceActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            })
            ;
        }
    }

    void refreshNewItemSubscription() {
        if (sharedPreferencesUtils.isNewItemShopNotificationEnabled()) {
            instance.subscribeToTopic("sell_item").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(NotificationPreferenceActivity.this, "Subscribed to sell item!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(NotificationPreferenceActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        checkBoxNewItem.setChecked(false);
                    }
                }
            });
        } else {
            instance.unsubscribeFromTopic("sell_item").addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(NotificationPreferenceActivity.this, "Unsubscribed to sell item!", Toast.LENGTH_SHORT).show();
                    } else {
                        checkBoxNewItem.setChecked(true);
                        Toast.makeText(NotificationPreferenceActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    @OnClick(R.id.btn_placeholder_notification_preference_no_connection_retry)
    void retryClicked() {
        AppExecutors.getInstance().networkIO().execute(new Runnable() {
            @Override
            public void run() {
                showPlaceHOlder(!ConverterUtils.hasInternetAccess(NotificationPreferenceActivity.this));
                Logger.d("Internet Connection is " + ConverterUtils.hasInternetAccess(NotificationPreferenceActivity.this));
            }
        });
    }

    private void showPlaceHOlder(boolean isShown) {
        AppExecutors.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                if (isShown) {
                    placeHOlderConnection.setVisibility(View.VISIBLE);
                    scrollContainer.setVisibility(View.GONE);
                } else {
                    placeHOlderConnection.setVisibility(View.GONE);
                    scrollContainer.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                retryClicked();
            }
        }, TimeUnit.SECONDS.toMillis(2));
    }

}
