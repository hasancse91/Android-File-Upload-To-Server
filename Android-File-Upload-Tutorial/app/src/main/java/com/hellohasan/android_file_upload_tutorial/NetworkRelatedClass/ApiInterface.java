package com.hellohasan.android_file_upload_tutorial.NetworkRelatedClass;

import com.hellohasan.android_file_upload_tutorial.ModelClass.ResponseModel;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;


public interface ApiInterface {

    @Multipart
    @POST("file_upload_api/upload.php")
    Call<ResponseModel> fileUpload(
            @Part("sender_information") RequestBody description,
            @Part MultipartBody.Part file);

}
