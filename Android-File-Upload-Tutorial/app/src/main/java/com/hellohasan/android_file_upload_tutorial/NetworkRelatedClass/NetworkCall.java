package com.hellohasan.android_file_upload_tutorial.NetworkRelatedClass;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hellohasan.android_file_upload_tutorial.ModelClass.ImageSenderInfo;
import com.hellohasan.android_file_upload_tutorial.ModelClass.ResponseModel;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NetworkCall {

    public static void fileUpload(Context context, String filePath, ImageSenderInfo imageSenderInfo) {

        ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        Logger.addLogAdapter(new AndroidLogAdapter());


        // create RequestBody instance from file
//        RequestBody requestFile = RequestBody.create(MediaType.parse(getContentResolver().getType(uri)), file);

        File file = new File(filePath);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
//        RequestBody requestFile = RequestBody.create(MediaType.parse(context.getContentResolver().getType(fileUri)), file);


        // MultipartBody.Part is used to send also the actual file name
        MultipartBody.Part body = MultipartBody.Part.createFormData("file", file.getName(), requestFile);


        Gson gson = new Gson();
        String patientData = gson.toJson(imageSenderInfo);

        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, patientData);

        // finally, execute the request
        Call<ResponseModel> call = apiInterface.fileUpload(description, body);
        call.enqueue(new Callback<ResponseModel>() {
            @Override
            public void onResponse(@NonNull Call<ResponseModel> call, @NonNull Response<ResponseModel> response) {
                Logger.d("Response: " + response);

                ResponseModel responseModel = response.body();

                if(response.code()==200 && responseModel != null)
                    Logger.d("Success. Response: " + responseModel.getMessage());
                else
                    Logger.d("Failed. Response code: " + response.code());
            }

            @Override
            public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
                Logger.d("Exception: " + t);

            }
        });
    }

}
