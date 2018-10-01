package pe.com.lycsoftware.cibertecproject.restService;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Task;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskService {
    @GET("TASK")
    Call<List<Task>> getTasks();

    @GET("TASK/{objectId}")
    Call<List<Task>> getTasks4Id(@Path("objectId") Integer objectId);
}
