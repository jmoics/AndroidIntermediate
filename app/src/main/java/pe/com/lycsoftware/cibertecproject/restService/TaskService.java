package pe.com.lycsoftware.cibertecproject.restService;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Task;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface TaskService {
    @GET("TASK")
    Call<List<Task>> getTasks();

    @GET("TASK/{objectId}")
    Call<List<Task>> getTasks4Id(@Path("objectId") Integer objectId);

    @POST("TASK")
    Call<Task> createTask(@Body Task task);//el body convierte Noticia en JSon

    @PUT("TASK/{objectId}")
    Call<Task> updateTask(@Path("objectId") String objectId, @Body Task task);
}
