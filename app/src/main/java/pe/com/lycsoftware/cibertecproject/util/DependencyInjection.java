package pe.com.lycsoftware.cibertecproject.util;

import com.google.gson.GsonBuilder;

import org.joda.time.DateTime;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import pe.com.lycsoftware.cibertecproject.model.Notification;
import pe.com.lycsoftware.cibertecproject.restService.NotificationService;
import pe.com.lycsoftware.cibertecproject.restService.TaskService;
import pe.com.lycsoftware.cibertecproject.restService.UserService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DependencyInjection
{
    private static Retrofit retrofit;

    public static TaskService getTaskService() {
        return getRetrofit().create(TaskService.class);
    }

    public static NotificationService getNotificationService() {
        return getRetrofit().create(NotificationService.class);
    }

    public static UserService getUserService() {
        return getRetrofit().create(UserService.class);
    }

    public static Retrofit getRetrofit()
    {

        if (retrofit == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger()
            {
                @Override
                public void log(String message)
                {
                    Logger.d("OkHttp", message);
                }
            });
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder okHttpBuilder = new OkHttpClient.Builder().addInterceptor(loggingInterceptor);

            GsonBuilder gson = new GsonBuilder();
            gson.registerTypeAdapter(DateTime.class, new DateTimeTypeConverter());

            retrofit = new Retrofit.Builder().baseUrl(Constants.url).addConverterFactory(
                    GsonConverterFactory.create(gson.create())) //usa convertor de json a java
                                             .client(okHttpBuilder.build()).build();
        }
        return retrofit;
    }

    public static Networking getNetworking(){
        return new Networking(getTaskService(), getNotificationService(), getUserService());
    }
}
