package pe.com.lycsoftware.cibertecproject;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
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

import org.joda.time.DateTime;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.util.Constants;
import pe.com.lycsoftware.cibertecproject.util.Networking;
import pe.com.lycsoftware.cibertecproject.util.NotificationAdapter;

public class TaskDetailActivity
        extends AppCompatActivity
        implements NotificationAdapter.OnNotificationListener
{

    private static final String TAG = "TaskDetailActivity";
    private EditText txtName, txtTimeStart, txtTimeFinish;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Task task;
    private String uiMode;
    private CustomDateTimePicker custom;
    private NotificationAdapter notificationAdapter;
    private List<Notification> notificationList = new ArrayList<>();
    private List<Notification> notificationDeleteList = new ArrayList<>();
    private int notificationPosition;
    private boolean hasEmptyNotification;
    private Set<String> notificationSet = new HashSet<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");
        setContentView(R.layout.activity_task_detail);

        setToolbarProperties();

        txtName = findViewById(R.id.txtName);
        txtTimeStart = findViewById(R.id.txtTimeStart);
        txtTimeStart.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog(txtTimeStart);
            }
        });
        txtTimeFinish = findViewById(R.id.txtTimeFinish);
        txtTimeFinish.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDatePickerDialog(txtTimeFinish);
            }
        });

        task = getIntent().getParcelableExtra(Constants.TASK_PARAM);
        if (task == null) {
            task = new Task();
            activateCreateMode();
        } else {
            activateViewMode();
        }

        recyclerView = findViewById(R.id.taskNotificationList);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        notificationAdapter = new NotificationAdapter(notificationList, TaskDetailActivity.this, uiMode);
        recyclerView.setAdapter(notificationAdapter);

        showTaskData();
        loadNotifications();
    }

    private void setToolbarProperties()
    {
        Toolbar main_toolbar = findViewById(R.id.toolbarTask);
        setSupportActionBar(main_toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_profile, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        switch (uiMode) {
            case Constants.MODE_CREATE:
                menu.findItem(R.id.action_edit).setVisible(false);
                menu.findItem(R.id.action_delete).setVisible(false);
                menu.findItem(R.id.action_cancel).setVisible(false);
                menu.findItem(R.id.action_save).setVisible(true);
                break;
            case Constants.MODE_EDIT:
                menu.findItem(R.id.action_edit).setVisible(false);
                menu.findItem(R.id.action_delete).setVisible(false);
                menu.findItem(R.id.action_cancel).setVisible(true);
                menu.findItem(R.id.action_save).setVisible(true);
                break;
            case Constants.MODE_VIEW:
                menu.findItem(R.id.action_edit).setVisible(true);
                menu.findItem(R.id.action_delete).setVisible(true);
                menu.findItem(R.id.action_cancel).setVisible(false);
                menu.findItem(R.id.action_save).setVisible(false);
                break;
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        invalidateOptionsMenu();
        switch (item.getItemId()) {
            case R.id.action_edit:
                //item.setVisible(false);
                notificationAdapter.getTxtNotificationAdd().setVisibility(View.VISIBLE);
                activateEditMode();
                return true;
            case R.id.action_delete:
                deleteAll();
                return true;
            case R.id.action_save:
                saveTask();
                return true;
            case R.id.action_cancel:
                notificationAdapter.getTxtNotificationAdd().setVisibility(View.GONE);
                activateViewMode();
                showTaskData();
                loadNotifications();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showTaskData()
    {
        if (uiMode.equals(Constants.MODE_CREATE)) {
            txtTimeStart.setText(Constants.getDateTimeFormatter().format(new DateTime().toDate()));
            txtTimeFinish.setText(Constants.getDateTimeFormatter().format(new DateTime().toDate()));
        } else {
            txtName.setText(task.getName());
            txtTimeStart.setText(Constants.getDateTimeFormatter().format(task.getTaskTimeStart().toDate()));
            txtTimeFinish.setText(Constants.getDateTimeFormatter().format(task.getTaskTimeFinish().toDate()));
        }
        //loadNotifications();
    }

    private void loadNotifications()
    {
        if (task.getObjectId() != null) {
            Networking.getNotifications4Task(task.getObjectId(), new Networking.NetworkingCallback<List<Notification>>()
            {
                @Override
                public void onResponse(List<Notification> response)
                {
                    notificationList.clear();
                    if (response.isEmpty()) {
                        response.add(getEmptyNotification());
                        hasEmptyNotification = true;
                    }
                    notificationList.addAll(response);
                    for (Notification not : notificationList) {
                        notificationSet.add(not.getDescription());
                    }
                    notificationAdapter.notifyDataSetChanged();
                    Log.d(TAG, "onResponse: notification correctly loaded size = " + response.size());
                }

                @Override
                public void onError(Throwable throwable)
                {
                    Log.d(TAG, "onError: Error loading");
                }
            });
        } else {
            addEmptyNotifications();
        }
    }

    private void addEmptyNotifications() {
        hasEmptyNotification = true;
        notificationList.add(getEmptyNotification());
        notificationAdapter.notifyDataSetChanged();
    }

    private Notification getEmptyNotification()
    {
        Notification notification = new Notification();
        notification.setDescription(Constants.EMPTY_NOTIFICATION);
        notification.setActive(false);
        return notification;
    }

    private void saveTask()
    {
        task.setActive(true);
        task.setName(this.txtName.getText().toString());
        try {
            task.setTaskDate(new DateTime(
                    Constants.getDateFormatter().parse(this.txtTimeStart.getText().toString())
                             .getTime()));
            task.setTaskTimeStart(new DateTime(
                    Constants.getDateTimeFormatter().parse(this.txtTimeStart.getText().toString())
                             .getTime()));
            task.setTaskTimeFinish(new DateTime(
                    Constants.getDateTimeFormatter().parse(this.txtTimeFinish.getText().toString())
                             .getTime()));
        } catch (ParseException e) {
            Log.e(TAG, "saveTask: error parsing date in saving task", e);
        }
        if (task.getObjectId() != null) {
            Networking.updateTask(task, new Networking.NetworkingCallback<Task>()
            {
                @Override
                public void onResponse(Task response)
                {
                    saveNotifications();
                    unscheduleNotifications();
                    scheduleNotifications();
                    finish();
                }

                @Override
                public void onError(Throwable throwable)
                {
                    Log.e(TAG, "onError: error updating task", throwable);
                }
            });
        } else {
            Networking.createTask(task, new Networking.NetworkingCallback<Task>() {
                @Override
                public void onResponse(final Task response)
                {
                    task.setObjectId(response.getObjectId());
                    saveNotifications();
                    scheduleNotifications();
                    finish();
                }

                @Override
                public void onError(final Throwable throwable)
                {
                    Log.d(TAG, "onError: error creating task");
                }
            });
        }
    }

    private void saveNotifications()
    {
        if (!notificationDeleteList.isEmpty()) {
            for (final Notification notification : notificationDeleteList) {
                Networking.deleteNotification(notification,
                        new Networking.NetworkingCallback<Notification>()
                        {
                            @Override
                            public void onResponse(Notification response)
                            {
                                Log.d(TAG, "onResponse: notification " + notification
                                        .getDescription() + "deleted");
                            }

                            @Override
                            public void onError(Throwable throwable)
                            {
                                Log.e(TAG, "onError: error deleting notification", throwable);
                            }
                        });
            }
        }
        if (!notificationList.isEmpty()) {
            if (!hasEmptyNotification) {
                for (final Notification notification : notificationList) {
                    notification.setNotificationDate(task.getTaskTimeStart().minusMinutes(
                            Constants.NOTIFICATION.valueOf(notification.getDescription())
                                                  .getTime()));
                    if (notification.getObjectId() != null) {
                        Networking.updateNotification(notification,
                            new Networking.NetworkingCallback<Notification>()
                            {
                                @Override
                                public void onResponse(Notification response)
                                {
                                    Log.d(TAG, "onResponse: notification " + notification
                                            .getDescription() + "updated");
                                }

                                @Override
                                public void onError(Throwable throwable)
                                {
                                    Log.e(TAG, "onError: error updating notification",
                                            throwable);
                                }
                            });
                    } else {
                        notification.setTaskObjectId(task.getObjectId());
                        Networking.createNotification(notification,
                            new Networking.NetworkingCallback<Notification>()
                            {
                                @Override
                                public void onResponse(Notification response)
                                {
                                    Log.d(TAG, "onResponse: notification " + notification
                                            .getDescription() + " created");
                                }

                                @Override
                                public void onError(Throwable throwable)
                                {
                                    Log.e(TAG, "onError: error creating notification",
                                            throwable);
                                }
                            });
                    }
                }
            }
        }
    }

    private void scheduleNotifications() {
        //int count = 10;
        for (Notification notification : notificationList) {
            Log.d(TAG, "scheduleNotifications: Notification scheduled at "
                    + Constants.getDateTimeFormatter().format(notification.getNotificationDate().toDate()));
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            notificationIntent.putExtra(Constants.TASK_PARAM, task);
            notificationIntent.putExtra(Constants.NOTIFICATION_PARAM, notification);
            PendingIntent broadcast = PendingIntent.getBroadcast(this,
                    notification.getNotificationDate().getMillisOfDay(),
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            /*Calendar cal = Calendar.getInstance();
            cal.add(Calendar.SECOND, count);*/
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, notification.getNotificationDate().getMillis(), broadcast);
            //alarmManager.setExact(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), broadcast);
            //count = count + 20;
        }
    }

    private void unscheduleNotifications() {
        for (Notification notification : notificationDeleteList) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            Intent notificationIntent = new Intent(this, AlarmReceiver.class);
            notificationIntent.putExtra(Constants.TASK_PARAM, task);
            notificationIntent.putExtra(Constants.NOTIFICATION_PARAM, notification);
            PendingIntent broadcast = PendingIntent.getBroadcast(this,
                    notification.getNotificationDate().getMillisOfDay(),
                    notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            alarmManager.cancel(broadcast);
        }
    }

    private void deleteAll()
    {
        for (final Notification notification : notificationList) {
            Networking.deleteNotification(notification, new Networking.NetworkingCallback<Notification>() {
                @Override
                public void onResponse(final Notification response)
                {
                    Log.d(TAG, "onResponse: notification " + notification.getDescription() + " deleted");
                }

                @Override
                public void onError(final Throwable throwable)
                {
                    Log.e(TAG, "onError: deleting notification", throwable);
                }
            });
        }

        Networking.deleteTask(task, new Networking.NetworkingCallback<Task>() {
            @Override
            public void onResponse(final Task response)
            {
                Log.d(TAG, "onResponse: task " + task.getName() + " deleted");
                finish();
            }

            @Override
            public void onError(final Throwable throwable)
            {
                Log.e(TAG, "onError: deleting task", throwable);
            }
        });
    }

    private void activateCreateMode()
    {
        uiMode = Constants.MODE_CREATE;
        txtName.setEnabled(true);
        txtTimeStart.setEnabled(true);
        txtTimeFinish.setEnabled(true);
    }

    private void activateEditMode()
    {
        uiMode = Constants.MODE_EDIT;
        txtName.setEnabled(true);
        txtTimeStart.setEnabled(true);
        txtTimeFinish.setEnabled(true);
    }

    private void activateViewMode()
    {
        uiMode = Constants.MODE_VIEW;
        txtName.setEnabled(false);
        txtTimeStart.setEnabled(false);
        txtTimeFinish.setEnabled(false);
    }

    private void showCustomDateTimePicker(final EditText editText)
    {
        custom = new CustomDateTimePicker(this, new CustomDateTimePicker.ICustomDateTimeListener()
        {

            @Override
            public void onSet(Dialog dialog, Calendar calendarSelected, Date dateSelected, int year,
                              String monthFullName, String monthShortName, int monthNumber,
                              int date, String weekDayFullName, String weekDayShortName, int hour24,
                              int hour12, int min, int sec, String AM_PM)
            {
                editText.setText("");
                editText.setText(year + "-" + (monthNumber + 1) + "-" + calendarSelected
                        .get(Calendar.DAY_OF_MONTH) + " " + hour24 + ":" + min + ":" + sec);
            }

            @Override
            public void onCancel()
            {
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

    private void showDatePickerDialog(final EditText editText)
    {
        final Calendar date = Calendar.getInstance();
        datePicker(editText, date);
    }

    private void datePicker(final EditText editText, final Calendar calendar)
    {
        final Calendar cur = Calendar.getInstance();
        try {
            cur.setTime(Constants.getDateTimeFormatter().parse(editText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener()
                {

                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear,
                                          int dayOfMonth)
                    {
                        //dateTime = new DateTime(year, monthOfYear, dayOfMonth, 0, 0);
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        //date_time = dayOfMonth + "-" + (monthOfYear + 1) + "-" + year;
                        //*************Call Time Picker Here ********************
                        timePicker(editText, calendar);
                    }
                }, cur.get(Calendar.YEAR), cur.get(Calendar.MONTH), cur.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void timePicker(final EditText editText, final Calendar calendar)
    {
        final Calendar cur = Calendar.getInstance();
        try {
            cur.setTime(Constants.getDateTimeFormatter().parse(editText.getText().toString()));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // Launch Time Picker Dialog
        final TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener()
                {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute)
                    {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        //mHour = hourOfDay;
                        //mMinute = minute;

                        editText.setText(
                                Constants.getDateTimeFormatter().format(calendar.getTime()));
                    }
                }, cur.get(Calendar.HOUR_OF_DAY), cur.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.NOTIFICATIONCREATE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String notificationTime = data.getStringExtra(Constants.NOTIFICATIONTIME_PARAM);
                    Notification notification = new Notification();
                    //notification.setNotificationDate(task.getTaskDate().minusMinutes(Constants.NOTIFICATION.valueOf(notificationTime).getTime()));
                    notification.setDescription(notificationTime);
                    notification.setActive(true);
                    if (hasEmptyNotification) {
                        notificationList.clear();
                        hasEmptyNotification = false;
                    }
                    if (!notificationSet.contains(notificationTime)) {
                        notificationList.add(notification);
                        notificationSet.add(notificationTime);
                    }
                    notificationAdapter.notifyDataSetChanged();
                }
                break;
            case Constants.NOTIFICATIONEDIT_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    String notificationTime = data.getStringExtra(Constants.NOTIFICATIONTIME_PARAM);
                    Log.d(TAG, "onActivityResult: " + Constants.NOTIFICATIONEDIT_REQUEST_CODE
                            + ",  notificationTime: " + notificationTime);
                    Notification notification = data
                            .getParcelableExtra(Constants.NOTIFICATION_PARAM);
                    notification.setDescription(notificationTime);
                    //notification.setNotificationDate(task.getTaskDate().minusMinutes(Constants.NOTIFICATION.valueOf(notificationTime).getTime()));
                    if (!notificationTime.equals(Constants.NOTIFICATION.NONE.name())) {
                        if (!notificationSet.contains(notificationTime)) {
                            Notification notificationDelete = notificationList.remove(notificationPosition);
                            notification.setDescription(notificationTime);
                            notificationSet.remove(notificationDelete.getDescription());
                            notificationList.add(notificationPosition, notification);
                            notificationSet.add(notificationTime);
                        }
                    } else {
                        Notification notificationDelete = notificationList.remove(notificationPosition);
                        if (notificationDelete.getObjectId() != null) {
                            notificationDeleteList.add(notificationDelete);
                        }
                        notificationSet.remove(notificationDelete.getDescription());
                        if (notificationList.isEmpty()) {
                            addEmptyNotifications();
                        }
                    }
                    notificationAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onNotificationClick(Notification notification, int position)
    {
        Intent intent = new Intent(this, NotificationActivity.class);
        notificationPosition = position;
        intent.putExtra(Constants.NOTIFICATION_PARAM, notification);
        if (!Constants.EMPTY_NOTIFICATION.equals(notification.getDescription())
                && !uiMode.equals(Constants.MODE_VIEW)) {
            startActivityForResult(intent, Constants.NOTIFICATIONEDIT_REQUEST_CODE);
        }
    }

    @Override
    public void onNotificationAddClick()
    {
        Intent intent = new Intent(this, NotificationActivity.class);
        startActivityForResult(intent, Constants.NOTIFICATIONCREATE_REQUEST_CODE);
    }
}
