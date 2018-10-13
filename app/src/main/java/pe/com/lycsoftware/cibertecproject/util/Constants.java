package pe.com.lycsoftware.cibertecproject.util;

import java.text.SimpleDateFormat;

public class Constants {
    public static final String url = "https://api.backendless.com/A00B9903-9649-7B95-FF46-8B93A2471400/27A21F20-610B-B538-FF48-56A725911700/data/";

    public static final int USER_REQUEST_CODE = 10;
    public static final int USEREDIT_REQUEST_CODE = 11;
    public static final int TASKEDIT_REQUEST_CODE = 12;
    public static final int TASKCREATE_REQUEST_CODE = 13;
    public static final int NOTIFICATIONCREATE_REQUEST_CODE = 14;
    public static final int NOTIFICATIONEDIT_REQUEST_CODE = 15;
    public static final String USER_PARAM = "user_param";
    public static final String TASK_PARAM = "task_param";
    public static final String NOTIFICATION_PARAM = "notification_param";
    public static final String NOTIFICATIONTIME_PARAM = "notificationtime_param";
    public static final String EMPTY_NOTIFICATION = "Añadir recordatorio";

    public static final String MODE_EDIT = "edit";
    public static final String MODE_VIEW = "view";

    public enum NOTIFICATION {
        NONE(-1, "Ninguno"),
        EXACTLY(0, "Hora exacta"),
        MINUTES_01(1, "1 min. antes"),
        MINUTES_10(10, "10 min. antes"),
        MINUTES_30(30, "30 min. antes"),
        HOUR_01(60, "1 hora antes"),
        HOUR_06(360, "6 horas antes"),
        DAY_01(1440, "1 día antes");

        private int time;
        private String desc;

        NOTIFICATION(final int time,
                     final String desc) {
            this.time = time;
            this.desc = desc;
        }

        public int getTime() {
            return this.time;
        }

        public String getDesc() {
            return this.desc;
        }
    }

    public static SimpleDateFormat getDateTimeFormatter() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //dateFormat = new SimpleDateFormat("dd/MMM/yyyy h:mm a");
    }
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }
}
