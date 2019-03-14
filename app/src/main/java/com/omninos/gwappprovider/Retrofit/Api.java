package com.omninos.gwappprovider.Retrofit;

import com.omninos.gwappprovider.directionApi.DirectionPojo;
import com.omninos.gwappprovider.modelClasses.CheckEmailPhoneModel;
import com.omninos.gwappprovider.modelClasses.CheckSocialLoginModel;
import com.omninos.gwappprovider.modelClasses.GetProviderProfile;
import com.omninos.gwappprovider.modelClasses.ProviderLoginModel;
import com.omninos.gwappprovider.modelClasses.ResendOtp;
import com.omninos.gwappprovider.modelClasses.ServiceModel;
import com.omninos.gwappprovider.modelClasses.SocialLoginModel;
import com.omninos.gwappprovider.modelClasses.UpdateProviderProfile;
import com.omninos.gwappprovider.modelClasses.UserRegisterLoginModel;
import com.omninos.gwappprovider.modelClasses.WorkListModel;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;

public interface Api {

    @GET("serviceList")
    Call<ServiceModel> serviceList();

    @Multipart
    @POST("providerRegister")
    Call<UserRegisterLoginModel> userRegister(@Part("username") RequestBody username,
                                              @Part("email") RequestBody email,
                                              @Part("phone") RequestBody phone,
                                              @Part("password") RequestBody password,
                                              @Part("serviceType") RequestBody serviceType,
                                              @Part("workQualification") RequestBody workQualification,
                                              @Part("serviceId") RequestBody serviceId,
                                              @Part("subServiceId") RequestBody subServiceId,
                                              @Part("device_type") RequestBody device_type,
                                              @Part("reg_id") RequestBody reg_id,
                                              @Part MultipartBody.Part driverLicence,
                                              @Part MultipartBody.Part insurance,
                                              @Part MultipartBody.Part image,
                                              @Part("latitude") RequestBody latitude,
                                              @Part("longitude") RequestBody longitude,
                                              @Part("perHourChargers") RequestBody perHourChargers);

    @FormUrlEncoded
    @POST("providerMatchVerificationToken")
    Call<Map> matchtoken(@Field("id") String id,
                         @Field("token") String token);

    @FormUrlEncoded
    @POST("providerResendVerificationToken")
    Call<ResendOtp> resend(@Field("id") String id);

    @FormUrlEncoded
    @POST("providerLogin")
    Call<ProviderLoginModel> login(@Field("email") String email,
                                   @Field("password") String password,
                                   @Field("reg_id") String reg_id,
                                   @Field("device_type") String device_type,
                                   @Field("latitude") String latitude,
                                   @Field("longitude") String longitude);

    @FormUrlEncoded
    @POST("checkEmail")
    Call<CheckEmailPhoneModel> checkEmail(@Field("email") String email);

    @FormUrlEncoded
    @POST("checkPhoneNumber")
    Call<CheckEmailPhoneModel> checkPhone(@Field("phone") String phone);

    @FormUrlEncoded
    @POST("providerProfile")
    Call<GetProviderProfile> getProfile(@Field("providerId") String providerId);

    @Multipart
    @POST("updateProviderProfile")
    Call<UpdateProviderProfile> updateProfile(@Part("providerId") RequestBody providerId,
                                              @Part("name") RequestBody name,
                                              @Part("idNumber") RequestBody idNumber,
                                              @Part("expiryDate") RequestBody expiryDate,
                                              @Part("phone") RequestBody phone,
                                              @Part MultipartBody.Part image);


    @GET("maps/api/directions/json?")
    Call<DirectionPojo> getPolyLine(@QueryMap Map<String, String> data);


    @FormUrlEncoded
    @POST("providerServiceRejectAccpetAndBid")
    Call<Map> SendAcceptRejectOrBidServices(@Field("type") String type,
                                            @Field("rejectReason") String rejectReason,
                                            @Field("bookingServiceId") String bookingServiceId);

    @FormUrlEncoded
    @POST("providerCheckSocialId")
    Call<CheckSocialLoginModel> CheckSocialID(@Field("social_id") String social_id);


    @Multipart
    @POST("providerSocialLogin")
    Call<SocialLoginModel> SocialLogin(@Part("name") RequestBody name,
                                       @Part("email") RequestBody email,
                                       @Part("image") RequestBody image,
                                       @Part("phone") RequestBody phone,
                                       @Part("serviceType") RequestBody serviceType,
                                       @Part("workQualification") RequestBody workQualification,
                                       @Part("latitude") RequestBody latitude,
                                       @Part("longitude") RequestBody longitude,
                                       @Part("serviceId") RequestBody serviceId,
                                       @Part("subServiceId") RequestBody subServiceId,
                                       @Part("social_id") RequestBody social_id,
                                       @Part("device_type") RequestBody device_type,
                                       @Part("reg_id") RequestBody reg_id,
                                       @Part("login_type") RequestBody login_type,
                                       @Part MultipartBody.Part driverLicence,
                                       @Part MultipartBody.Part insurance);

    @FormUrlEncoded
    @POST("providerWorkListNotitication")
    Call<WorkListModel> workList(@Field("providerId") String providerId);
}
