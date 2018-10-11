package pe.com.lycsoftware.cibertecproject.restService;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.User;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NotificationService {
    @GET("notification")
    Call<List<Notification>> getNotification4Task(@Query("where") String taskObjectId);
}
