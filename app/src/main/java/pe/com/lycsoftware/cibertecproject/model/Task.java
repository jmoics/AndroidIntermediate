package pe.com.lycsoftware.cibertecproject.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.joda.time.DateTime;

public class Task implements Parcelable {
    @SerializedName("objectId")
    private String objectId;
    private String name;
    private boolean active;
    @SerializedName("task_date")
    private DateTime taskDate;
    @SerializedName("task_time_start")
    private DateTime taskTimeStart;
    @SerializedName("task_time_finish")
    private DateTime taskTimeFinish;
    private DateTime created;
    private DateTime updated;

    protected Task(Parcel in) {
        objectId = in.readString();
        name = in.readString();
        active = in.readByte() != 0;
        taskDate = new DateTime(in.readLong());
        taskTimeStart = new DateTime(in.readLong());
        taskTimeFinish = new DateTime(in.readLong());
        created = new DateTime(in.readLong());
        updated = new DateTime(in.readLong());
    }

    public static final Creator<Task> CREATOR = new Creator<Task>() {
        @Override
        public Task createFromParcel(Parcel in) {
            return new Task(in);
        }

        @Override
        public Task[] newArray(int size) {
            return new Task[size];
        }
    };

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public DateTime getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(DateTime taskDate) {
        this.taskDate = taskDate;
    }

    public DateTime getTaskTimeStart() {
        return taskTimeStart;
    }

    public void setTaskTimeStart(DateTime taskTimeStart) {
        this.taskTimeStart = taskTimeStart;
    }

    public DateTime getTaskTimeFinish() {
        return taskTimeFinish;
    }

    public void setTaskTimeFinish(DateTime taskTimeFinish) {
        this.taskTimeFinish = taskTimeFinish;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public DateTime getCreated() {
        return created;
    }

    public void setCreated(DateTime created) {
        this.created = created;
    }

    public DateTime getUpdated() {
        return updated;
    }

    public void setUpdated(DateTime updated) {
        this.updated = updated;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(objectId);
        dest.writeString(name);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeLong(taskDate.getMillis());
        dest.writeLong(taskTimeStart.getMillis());
        dest.writeLong(taskTimeFinish.getMillis());
        dest.writeLong(created.getMillis());
        dest.writeLong(updated.getMillis());
    }
}
