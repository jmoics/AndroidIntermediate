package pe.com.lycsoftware.cibertecproject.service;

import java.util.List;

import pe.com.lycsoftware.cibertecproject.model.Task;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TaskService {
    @GET("TASK")
    Call<List<Task>> getTasks();

    @GET("TASK/{id}")
    Call<List<Task>> getTasks4Id(@Path("id") Integer id);
}
