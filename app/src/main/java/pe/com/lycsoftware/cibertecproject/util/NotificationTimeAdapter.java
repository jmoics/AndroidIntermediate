package pe.com.lycsoftware.cibertecproject.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pe.com.lycsoftware.cibertecproject.R;
import pe.com.lycsoftware.cibertecproject.model.Notification;

public class NotificationTimeAdapter extends RecyclerView.Adapter<NotificationTimeAdapter.ViewHolder> {

    private static final String TAG = "NotificationAdapter";
    private final List<Constants.NOTIFICATION> lstNotifications;
    private OnNotificationTimeListener listener;

    public NotificationTimeAdapter(OnNotificationTimeListener listener) {
        this.lstNotifications = Arrays.asList(Constants.NOTIFICATION.values());
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_list_content, parent, false);
        return new NotificationTimeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: position = " + lstNotifications.get(position).getDesc());
        holder.bind(lstNotifications.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return lstNotifications.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNotificationTime;
        private Constants.NOTIFICATION notificationTime;

        ViewHolder(View view) {
            super(view);
            txtNotificationTime = view.findViewById(R.id.txtNotificationTime);
        }

        public void bind(final Constants.NOTIFICATION notificationTime,
                         final OnNotificationTimeListener listener) {
            this.notificationTime = notificationTime;
            this.txtNotificationTime.setText(notificationTime.getDesc());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onNotificationTimeClick(notificationTime);
                }
            });
        }

        public Constants.NOTIFICATION getNotificationTime() {
            return notificationTime;
        }
    }

    public interface OnNotificationTimeListener {
        void onNotificationTimeClick(Constants.NOTIFICATION notificationTime);
    }
}
