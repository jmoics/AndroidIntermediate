package pe.com.lycsoftware.cibertecproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.NotificationTimeAdapter;

public class NotificationActivity extends AppCompatActivity implements NotificationTimeAdapter.OnNotificationTimeListener {

    private RecyclerView notification_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        setToolbarProperties();

        notification_view = findViewById(R.id.notification_view);
        notification_view.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        notification_view.setItemAnimator(new DefaultItemAnimator());
        notification_view.setLayoutManager(new LinearLayoutManager(this));
        NotificationTimeAdapter notificationTimeAdapter = new NotificationTimeAdapter(this);
        notification_view.setAdapter(notificationTimeAdapter);
    }

    private void setToolbarProperties() {
        Toolbar main_toolbar = findViewById(R.id.toolbarNotification);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public void onNotificationTimeClick(Constants.NOTIFICATION notificationTime) {
        Intent intent = new Intent();
        intent.putExtra("notification_time", notificationTime);
        setResult(RESULT_OK, intent);
        finish();
    }
}
