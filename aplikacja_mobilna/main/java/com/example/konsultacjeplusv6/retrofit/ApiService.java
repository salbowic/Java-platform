package com.example.konsultacjeplusv6.retrofit;

import com.example.konsultacjeplusv6.model.Forum;
import com.example.konsultacjeplusv6.model.Konsultacja;
import com.example.konsultacjeplusv6.model.LoginResponse;
import com.example.konsultacjeplusv6.model.Prowadzacy;
import com.example.konsultacjeplusv6.model.Przedmiot;
import com.example.konsultacjeplusv6.model.Realizacja;
import com.example.konsultacjeplusv6.model.Specjalizacja;
import com.example.konsultacjeplusv6.model.Wiadomosc;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @POST("login/mobile")
    Call<LoginResponse> loginMobile(@Body Map<String, String> credentials);

    @GET("konsultacje/mobile")
    Call<List<Konsultacja>> getKonsultacje();

    @GET("konsultacje/mobileStudent/{nrStudenta}")
    Call<List<Konsultacja>> getKonsultacjeByNrStudenta(@Path("nrStudenta") int nrStudenta);

    @GET("konsultacje/mobileProwadzacy/{nrProwadzacego}")
    Call<List<Konsultacja>> getKonsultacjeByNrProwadzacego(@Path("nrProwadzacego") int nrProwadzacego);

    @POST("/konsultacja/save")
    Call<Konsultacja> save(@Body Konsultacja konsultacja);

    @POST("/konsultacja/mobile/update")
    Call<Konsultacja> updateKonsultacja(@Body Konsultacja konsultacja);

    @POST("konsultacje/signup")
    Call<Void> signUpForKonsultacja(@Query("nr_konsultacji") int nrKonsultacji, @Query("nr_studenta") int nrStudenta);

    @GET("konsultacje/checkSignUp")
    Call<Boolean> checkStudentKonsultacja(@Query("nr_konsultacji") int nrKonsultacji, @Query("nr_studenta") int nrStudenta);

    @POST("konsultacje/mobile/signOut")
    Call<Void> signOutFromKonsultacja(@Query("nr_konsultacji") int nrKonsultacji, @Query("nr_studenta") int nrStudenta);

    @PUT("konsultacje/mobile/updatePytanie/{nr_konsultacji}")
    Call<Void> updateQuestion(@Path("nr_konsultacji") int nrKonsultacji, @Query("pytanie") String pytanie);

    @GET("/fora/mobile")
    Call<List<Forum>> listAllFora();

    @GET("/specjalizacje/{nrSpecjalizacji}")
    Call<Specjalizacja> getSpecjalizacjaData(@Path("nrSpecjalizacji") int nrSpecjalizacji);

    @GET("/realizacje/{nrRealizacji}")
    Call<Realizacja> getRealizacjaData(@Path("nrRealizacji") int nrRealizacji);

    @GET("/przedmioty/{nrPrzedmiotu}")
    Call<Przedmiot> getPrzedmiotData(@Path("nrPrzedmiotu") int nrPrzedmiotu);

    @GET("forums/{forumId}/messages")
    Call<List<Wiadomosc>> getMessagesForForum(@Path("forumId") int forumId);

    @FormUrlEncoded
    @POST("forums/{forumId}/messages")
    Call<Void> addNewMessage(@Path("forumId") int forumId,
                             @Field("nadawca") String nadawca,
                             @Field("messageText") String messageText,
                             @Field("timestamp") String timestamp,
                             @Field("nrStudenta") Integer nrStudenta,
                             @Field("nrProwadzacego") Integer nrProwadzacego); //@Query bez FormUrl?

    @GET("student/{nrStudenta}/image")
    Call<ResponseBody> getStudentImage(@Path("nrStudenta") int studentId);

    @GET("prowadzacy/{nrProwadzacego}/image")
    Call<ResponseBody> getProwadzacyImage(@Path("nrProwadzacego") int prowadzacyId);

    @GET("prowadzacy/mobile/{nrProwadzacego}")
    Call<Prowadzacy> getProwadzacyById(@Path("nrProwadzacego") int nrProwadzacego);

    @POST("update_student_data")
    Call<Void> updateStudent(@Query("nr_studenta") int nr_studenta, @Body Map<String, String> data);

    @POST("update_prowadzacy_data")
    Call<Void> updateProwadzacy(@Query("nr_prowadzacego") int nr_prowadzacego, @Body Map<String, String> data);

}

