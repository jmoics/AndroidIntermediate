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
    public static final String MODE_EDIT = "mode_edit";
    public static final String MODE_CREATE = "mode_create";
    public static final String MODE_VIEW = "mode_view";

    public static final String CHANNEL_ID = "pe.com.lycsoftware.cibertecproject";
    public static final String CHANNEL_NAME = "Notificaciones de Alarma";

    public enum NOTIFICATION {
        NONE(-1, "Ninguno", ""),
        EXACTLY(0, "Hora exacta", "Ahora inicia la tarea programada"),
        MINUTES_01(1, "1 min. antes", "En 1 minuto inicia la tarea programada"),
        MINUTES_02(2, "2 min. antes", "En 2 minuto inicia la tarea programada"),
        MINUTES_10(10, "10 min. antes", "En 10 minutos inicia la tarea programada"),
        MINUTES_30(30, "30 min. antes", "En 30 minutos inicia la tarea programada"),
        HOUR_01(60, "1 hora antes", "En 1 hora inicia la tarea programada"),
        HOUR_06(360, "6 horas antes", "En 6 horas inicia la tarea programada"),
        DAY_01(1440, "1 día antes", "En 1 día inicia la tarea programada");

        private int time;
        private String desc;
        private String message;

        NOTIFICATION(final int time,
                     final String desc,
                     final String message) {
            this.time = time;
            this.desc = desc;
            this.message = message;
        }

        public int getTime() {
            return this.time;
        }

        public String getDesc() {
            return this.desc;
        }

        public String getMessage()
        {
            return message;
        }
    }

    public static SimpleDateFormat getDateTimeFormatter() {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm");
        //dateFormat = new SimpleDateFormat("dd/MMM/yyyy h:mm a");
    }
    public static SimpleDateFormat getDateFormatter() {
        return new SimpleDateFormat("dd/MM/yyyy");
    }

    public static SimpleDateFormat getTimeFormatter() {
        return new SimpleDateFormat("HH:mm");
    }
}
