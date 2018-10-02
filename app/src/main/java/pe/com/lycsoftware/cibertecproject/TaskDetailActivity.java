package pe.com.lycsoftware.cibertecproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.Networking;

public class TaskDetailActivity extends AppCompatActivity {

    private static final String TAG = "TaskDetailActivity";
    private EditText txtName, txtTimeStart, txtTimeFinish;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        task = getIntent().getParcelableExtra(Constants.TASK_PARAM);

        setToolbarProperties();

        txtName = findViewById(R.id.txtName);
        txtTimeStart = findViewById(R.id.txtTimeStart);
        txtTimeFinish = findViewById(R.id.txtTimeFinish);

        recyclerView = findViewById(R.id.taskNotificationList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        loadData();
    }

    private void setToolbarProperties() {
        Toolbar main_toolbar = findViewById(R.id.toolbarTask);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_edit:
                Intent intent = new Intent(this, TaskDetailModActivity.class);
                intent.putExtra(Constants.TASK_PARAM, task);
                startActivityForResult(intent, Constants.TASKEDIT_REQUEST_CODE);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        txtName.setText(task.getName());
        txtTimeStart.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(task.getTaskTimeStart().toDate()));
        txtTimeFinish.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                .format(task.getTaskTimeFinish().toDate()));

        Networking.getNotifications4Task(task.getObjectId(),
                new Networking.NetworkingCallback<List<Notification>>() {
            @Override
            public void onResponse(List<Notification> response) {
                TaskDetailActivity.SimpleItemRecyclerViewAdapter lst =
                        new TaskDetailActivity.SimpleItemRecyclerViewAdapter(response);
                recyclerView.setAdapter(lst);
                Log.d(TAG, "onResponse: notification correctly loaded size = "
                        + response.size());
            }

            @Override
            public void onError(Throwable throwable) {
                Log.d(TAG, "onError: Error loading");
            }
        });
    }

    class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<TaskDetailActivity.ViewHolder> {

        private final List<Notification> mValues;

        SimpleItemRecyclerViewAdapter(List<Notification> items) {
            mValues = items;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.notification_list_content, parent, false);
            return new TaskDetailActivity.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Log.d(TAG, "onBindViewHolder: position = " + mValues.get(position).getNotificationDate());
            holder.notificationItem = mValues.get(position);
            holder.txtNotificationTime.setText(new SimpleDateFormat("dd/MM/yyyy HH:mm")
                    .format(mValues.get(position).getNotificationDate().toDate()));
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final View mView;
        final TextView txtNotificationTime;
        Notification notificationItem;

        ViewHolder(View view) {
            super(view);
            mView = view;
            txtNotificationTime = view.findViewById(R.id.txtNotificationTime);
        }

        public Notification getNotificationItem() {
            return notificationItem;
        }
    }
}
