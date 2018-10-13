package pe.com.lycsoftware.cibertecproject.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.R;
import pe.com.lycsoftware.cibertecproject.model.Notification;

public class NotificationAdapter
        extends RecyclerView.Adapter<NotificationAdapter.ViewHolder>
{

    private static final String TAG = "NotificationAdapter";
    private final List<Notification> lstNotifications;
    private final OnNotificationListener listener;
    private TextView txtNotificationAdd;
    private String uiMode;

    public NotificationAdapter(final List<Notification> items, final OnNotificationListener listener,
                               final String uiMode)
    {
        this.lstNotifications = items;
        this.listener = listener;
        this.uiMode = uiMode;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_list_content, parent, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Log.d(TAG, "onBindViewHolder: position = " + lstNotifications.get(position).getNotificationDate());
        holder.bind(lstNotifications.get(position), listener, position);
    }

    @Override
    public int getItemCount()
    {
        return lstNotifications.size();
    }

    class ViewHolder
            extends RecyclerView.ViewHolder
    {
        private TextView txtNotificationTime;
        private TextView txtNotificationTimeAdd;
        private Notification notificationItem;

        ViewHolder(View view)
        {
            super(view);
            txtNotificationTime = view.findViewById(R.id.txtNotificationTime);
            txtNotificationTimeAdd = view.findViewById(R.id.txtNotificationTimeAdd);
        }

        public void bind(final Notification notification, final OnNotificationListener listener, final int position)
        {
            notificationItem = notification;
            /*txtNotificationTime.setText(Constants.getDateTimeFormatter()
                    .format(notification.getNotificationDate().toDate()));*/

            if (notification.getDescription().equals(Constants.EMPTY_NOTIFICATION)) {
                txtNotificationTime.setText(Constants.EMPTY_NOTIFICATION);
            } else {
                txtNotificationTime.setText(Constants.NOTIFICATION.valueOf(notification.getDescription()).getDesc());
            }
            txtNotificationTime.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    listener.onNotificationClick(notification, position);
                }
            });
            if (position == 0) {
                if (Constants.MODE_CREATE.equals(uiMode)) {
                    txtNotificationTimeAdd.setVisibility(View.VISIBLE);
                }
                txtNotificationTimeAdd.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        listener.onNotificationAddClick();
                    }
                });
                txtNotificationAdd = txtNotificationTimeAdd;
            }
        }

        public Notification getNotificationItem()
        {
            return notificationItem;
        }
    }

    public TextView getTxtNotificationAdd()
    {
        return this.txtNotificationAdd;
    }

    public interface OnNotificationListener
    {
        void onNotificationClick(Notification notification, int position);

        void onNotificationAddClick();
    }
}
