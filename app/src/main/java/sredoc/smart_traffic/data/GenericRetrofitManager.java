package sredoc.smart_traffic.data;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class GenericRetrofitManager {

    private static ApiHelper apiHelper;
    private static final long TIMEOUT = 60L;

    private GenericRetrofitManager(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(generateGson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(generateOkHttpClient())
                .build();
        apiHelper = retrofit.create(ApiHelper.class);
    }

    /**
     * Generate gson instance
     *
     * @return gson
     */
    private Gson generateGson() {
        return new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return false;
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .setLenient()
                .create();
    }

    /**
     * Generate gson instance
     *
     * @return gson
     */
    private OkHttpClient generateOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        builder.interceptors().add(logging);
        return builder.build();
    }

    public static ApiHelper getApiService(String baseUrl) {
        new GenericRetrofitManager(baseUrl);
        return apiHelper;
    }
}
