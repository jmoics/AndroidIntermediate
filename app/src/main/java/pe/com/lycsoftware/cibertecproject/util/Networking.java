package pe.com.lycsoftware.cibertecproject.util;

import android.util.Log;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.restService.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Networking {
    private static final String TAG = "Networking";
    private static Retrofit retrofit;
    private static UserService userService;

    private static Retrofit getRetrofit() {
        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    Log.d("OkHttp", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor);

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.url)
                    .addConverterFactory(GsonConverterFactory.create()) //usa convertor de json a java
                    .client(okHttpBuilder.build())
                    .build();
        }
        return retrofit;
    }

    public static UserService getUserService() {
        if (userService == null) {
            userService = getRetrofit().create(UserService.class);
        }
        return userService;
    }

    public static void getUser4Email(String email, final NetworkingCallback<List<User>> callback) {
        getUserService().getUser4Email(email).enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                callback.onResponse(users);
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public static void updateUser(User user, final NetworkingCallback<User> callback) {
        getUserService().updateUser(user.getObjectId(), user).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface NetworkingCallback<T> {
        void onResponse(T response);
        void onError(Throwable throwable);
    }
}
