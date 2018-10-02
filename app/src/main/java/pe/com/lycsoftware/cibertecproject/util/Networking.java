package pe.com.lycsoftware.cibertecproject.util;

import android.util.Log;

import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.model.Task;
import pe.com.lycsoftware.cibertecproject.model.User;
import pe.com.lycsoftware.cibertecproject.restService.NotificationService;
import pe.com.lycsoftware.cibertecproject.restService.TaskService;
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
    private static TaskService taskService;
    private static NotificationService notificationService;

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

            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());

            retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.url)
                    .addConverterFactory(GsonConverterFactory.create(gson.create())) //usa convertor de json a java
                    .client(okHttpBuilder.build())
                    .build();
        }
        return retrofit;
    }

    private static UserService getUserService() {
        if (userService == null) {
            userService = getRetrofit().create(UserService.class);
        }
        return userService;
    }

    private static TaskService getTaskService() {
        if (taskService == null) {
            taskService = getRetrofit().create(TaskService.class);
        }
        return taskService;
    }

    private static NotificationService getNotificationService() {
        if (notificationService == null) {
            notificationService = getRetrofit().create(NotificationService.class);
        }
        return notificationService;
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

    public static void getTasks(final NetworkingCallback<List<Task>> callback) {
        getTaskService().getTasks().enqueue(new Callback<List<Task>>() {
            @Override
            public void onResponse(Call<List<Task>> call, Response<List<Task>> response) {
                List<Task> taskList = response.body();
                callback.onResponse(taskList);
            }

            @Override
            public void onFailure(Call<List<Task>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public static void createTask(Task task, final NetworkingCallback<Task> callback) {
        getTaskService().createTask(task).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public static void updateTask(Task task, final NetworkingCallback<Task> callback) {
        getTaskService().updateTask(task.getObjectId(), task).enqueue(new Callback<Task>() {
            @Override
            public void onResponse(Call<Task> call, Response<Task> response) {
                callback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<Task> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public static void getNotifications4Task(String taskObjectId, final NetworkingCallback<List<Notification>> callback) {
        getNotificationService().getNotification4Task(taskObjectId).enqueue(new Callback<List<Notification>>() {
            @Override
            public void onResponse(Call<List<Notification>> call, Response<List<Notification>> response) {
                List<Notification> notifications = response.body();
                callback.onResponse(notifications);
            }

            @Override
            public void onFailure(Call<List<Notification>> call, Throwable t) {
                callback.onError(t);
            }
        });
    }

    public interface NetworkingCallback<T> {
        void onResponse(T response);
        void onError(Throwable throwable);
    }
}
