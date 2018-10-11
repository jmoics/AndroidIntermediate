package pe.com.lycsoftware.cibertecproject.restService;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface NotificationService {
    @GET("notification")
    Call<List<Notification>> getNotification4Task(@Query("where") String taskObjectId);

    @POST("notification")
    Call<Notification> createNotification(@Body Notification notification);//el body convierte Noticia en JSon

    @PUT("notification/{objectId}")
    Call<Notification> updateNotification(@Path("objectId") String objectId, @Body Notification notification);

    @DELETE("notification/{objectId}")
    Call<Notification> deleteNotification(@Path("objectId")String objectId);
}
