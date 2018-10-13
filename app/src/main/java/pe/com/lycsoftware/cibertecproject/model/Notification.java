package pe.com.lycsoftware.cibertecproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Notification
        implements Parcelable
{
    @SerializedName("objectId")
    private String objectId;
    @SerializedName("task_objectId")
    private String taskObjectId;
    private boolean active;
    private String description;
    @SerializedName("notification_date")
    private DateTime notificationDate;
    private DateTime created;
    private DateTime updated;

    public Notification() {}

    protected Notification(Parcel in)
    {
        objectId = in.readString();
        taskObjectId = in.readString();
        active = in.readByte() != 0;
        description = in.readString();
        notificationDate = new DateTime(in.readLong());
        created = new DateTime(in.readLong());
        updated = new DateTime(in.readLong());
    }

    public static final Creator<Notification> CREATOR = new Creator<Notification>()
    {
        @Override
        public Notification createFromParcel(Parcel in)
        {
            return new Notification(in);
        }

        @Override
        public Notification[] newArray(int size)
        {
            return new Notification[size];
        }
    };

    public String getObjectId()
    {
        return objectId;
    }

    public void setObjectId(String objectId)
    {
        this.objectId = objectId;
    }

    public String getTaskObjectId()
    {
        return taskObjectId;
    }

    public void setTaskObjectId(String taskObjectId)
    {
        this.taskObjectId = taskObjectId;
    }

    public DateTime getNotificationDate()
    {
        return notificationDate;
    }

    public void setNotificationDate(DateTime notificationDate)
    {
        this.notificationDate = notificationDate;
    }

    public boolean isActive()
    {
        return active;
    }

    public void setActive(boolean active)
    {
        this.active = active;
    }

    public DateTime getCreated()
    {
        return created;
    }

    public void setCreated(DateTime created)
    {
        this.created = created;
    }

    public DateTime getUpdated()
    {
        return updated;
    }

    public void setUpdated(DateTime updated)
    {
        this.updated = updated;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeString(objectId);
        dest.writeString(taskObjectId);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeString(description);
        if (notificationDate != null) {
            dest.writeLong(notificationDate.getMillis());
        }
        if (created != null) {
            dest.writeLong(created.getMillis());
        }
        if (updated != null) {
            dest.writeLong(updated.getMillis());
        }
    }
}
