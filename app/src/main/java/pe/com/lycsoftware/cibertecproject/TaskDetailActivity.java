package pe.com.lycsoftware.cibertecproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.Networking;
import pe.com.lycsoftware.cibertecproject.util.NotificationAdapter;

public class TaskDetailActivity extends AppCompatActivity implements NotificationAdapter.OnNotificationListener {

    private static final String TAG = "TaskDetailActivity";
    private EditText txtName, txtTimeStart, txtTimeFinish;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Task task;
    private boolean editMode;
    private CustomDateTimePicker custom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        task = getIntent().getParcelableExtra(Constants.TASK_PARAM);

        setToolbarProperties();

        txtName = findViewById(R.id.txtName);
        txtTimeStart = findViewById(R.id.txtTimeStart);
        txtTimeStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtTimeStart);
            }
        });
        txtTimeFinish = findViewById(R.id.txtTimeFinish);
        txtTimeFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog(txtTimeFinish);
            }
        });

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
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (editMode) {
            menu.findItem(R.id.action_edit).setVisible(false);
            menu.findItem(R.id.action_delete).setVisible(false);
            menu.findItem(R.id.action_cancel).setVisible(true);
            menu.findItem(R.id.action_save).setVisible(true);
        } else {
            menu.findItem(R.id.action_edit).setVisible(true);
            menu.findItem(R.id.action_delete).setVisible(true);
            menu.findItem(R.id.action_cancel).setVisible(false);
            menu.findItem(R.id.action_save).setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.action_edit:
                item.setVisible(false);
                activateEditMode();
                loadNotifications();
                /*Intent intent = new Intent(this, NotificationActivity.class);
                intent.putExtra(Constants.TASK_PARAM, task);
                startActivityForResult(intent, Constants.TASKEDIT_REQUEST_CODE);*/
                return true;
            case R.id.action_delete:
                return true;
            case R.id.action_save:
                return true;
            case R.id.action_cancel:
                activateViewMode();
                loadData();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void activateEditMode() {
        editMode = true;
        txtName.setEnabled(true);
        txtTimeStart.setEnabled(true);
        txtTimeFinish.setEnabled(true);
    }

    private void activateViewMode() {
        editMode = false;
        txtName.setEnabled(false);
        txtTimeStart.setEnabled(false);
        txtTimeFinish.setEnabled(false);
    }

    private void showCustomDateTimePicker(final EditText editText) {
        custom = new CustomDateTimePicker(this,
            new CustomDateTimePicker.ICustomDateTimeListener() {

                @Override
                public void onSet(Dialog dialog, Calendar calendarSelected,
                                  Date dateSelected, int year, String monthFullName,
                                  String monthShortName, int monthNumber, int date,
                                  String weekDayFullName, String weekDayShortName,
                                  int hour24, int hour12, int min, int sec,
                                  String AM_PM) {
                    editText.setText("");
                    editText.setText(year
                            + "-" + (monthNumber + 1) + "-" + calendarSelected.get(Calendar.DAY_OF_MONTH)
                            + " " + hour24 + ":" + min
                            + ":" + sec);
                }

                @Override
                public void onCancel() {
                }
            });
        /**
         * Pass Directly current time format it will return AM and PM if you set
         * false
         */
        custom.set24HourFormat(true);
        /**
         * Pass Directly current data and time to show when it pop up
         */
        custom.setDate(Calendar.getInstance());
        custom.showDialog();
    }

    private void showDatePickerDialog(final EditText editText) {
        final Calendar date = Calendar.getInstance();
        datePicker(editText,date);
    }

    private void datePicker(final EditText editText, final Calendar calendar){
        final Calendar cur = Calendar.getInstance();
        try {
            cur.setTime(Constants.getDateFormatter().parse(editText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
            new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year,int monthOfYear, int dayOfMonth) {
                    //dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                    //*************Call Time Picker Here ********************
                    tiemPicker(editText, calendar);
                }
            }, cur.get(Calendar.YEAR), cur.get(Calendar.MONTH), cur.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void tiemPicker(final EditText editText, final Calendar calendar){
        final Calendar cur = Calendar.getInstance();
        try {
            cur.setTime(Constants.getDateFormatter().parse(editText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Launch Time Picker Dialog
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
            new TimePickerDialog.OnTimeSetListener() {

                @Override
                public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    //mHour = hourOfDay;
                    //mMinute = minute;

                    editText.setText(Constants.getDateFormatter().format(calendar.getTime()));
                }
            }, cur.get(Calendar.HOUR_OF_DAY), cur.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    private void loadData() {
        txtName.setText(task.getName());
        txtTimeStart.setText(Constants.getDateFormatter().format(task.getTaskTimeStart().toDate()));
        txtTimeFinish.setText(Constants.getDateFormatter().format(task.getTaskTimeFinish().toDate()));

        loadNotifications();
    }

    private void loadNotifications() {
        Networking.getNotifications4Task(task.getObjectId(),
            new Networking.NetworkingCallback<List<Notification>>() {
                @Override
                public void onResponse(List<Notification> response) {
                    NotificationAdapter lst = new NotificationAdapter(response,
                            TaskDetailActivity.this, editMode);
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

    @Override
    public void onNotificationClick(Notification notification) {

    }

    @Override
    public void onNotificationAddClick() {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivityForResult(intent, Constants.NOTIFICATION_REQUEST_CODE);
    }
}
