package pe.com.lycsoftware.cibertecproject.restService;

import java.util.List;
import pe.com.lycsoftware.cibertecproject.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface UserService {
    @GET("users")
    Call<List<User>> getUsers();

    @GET("users/{objectId}")
    Call<User> getUser4Id(@Path("objectId") Integer objectId);

    @GET("users")
    Call<List<User>> getUser4Email(@Query("where=email") String email);

    @PUT("users/{objectId}")
    Call<User> updateUser(@Path("objectId") String objectId, @Body User user);
}