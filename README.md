# Image or any File Upload to Server using Retrofit in Android App

We'll design this type of sample Android App using [Retrofit](https://github.com/square/retrofit) Android Library:

<img src="https://raw.githubusercontent.com/hasancse91/Android-File-Upload-To-Server/master/Data/image-upload-to-server-android-retrofit.gif" width="250" height="444" />

Create instance of `Retrofit` class (You can check basic Retrofit implementation from [here](https://github.com/hasancse91/retrofit-implementation))
```java
public class RetrofitApiClient {

    private static final String BASE_URL = "http://yourdomainname.com"; //I used IP of my local machine

    private static Retrofit retrofit = null;

    private static Gson gson = new GsonBuilder()
            .setLenient()
            .create();

    private RetrofitApiClient() {} // So that nobody can create an object with constructor

    public static synchronized Retrofit getClient() {
        if (retrofit==null) {

            int timeOut = 5 * 60;
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(timeOut, TimeUnit.SECONDS)
                    .writeTimeout(timeOut, TimeUnit.SECONDS)
                    .readTimeout(timeOut, TimeUnit.SECONDS)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
```

The `Interface` class is given below:
```java
public interface ApiInterface {

    @Multipart
    @POST("file_upload_api/upload.php")
    Call<ResponseModel> fileUpload(
            @Part("sender_information") RequestBody description,
            @Part MultipartBody.Part file);
}
```

File upload method is here:
```java
public static void fileUpload(String filePath, ImageSenderInfo imageSenderInfo) {

    ApiInterface apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
    Logger.addLogAdapter(new AndroidLogAdapter());

    File file = new File(filePath);
    //create RequestBody instance from file
    RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);

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

            if(responseModel != null){
                EventBus.getDefault().post(new EventModel("response", responseModel.getMessage()));
                Logger.d("Response code " + response.code() +
                        " Response Message: " + responseModel.getMessage());
            } else
                EventBus.getDefault().post(new EventModel("response", "ResponseModel is NULL"));
        }

        @Override
        public void onFailure(@NonNull Call<ResponseModel> call, @NonNull Throwable t) {
            Logger.d("Exception: " + t);
            EventBus.getDefault().post(new EventModel("response", t.getMessage()));
        }
    });
}
```
Here I used [EventBus](https://github.com/greenrobot/EventBus) Library to notify my UI from a different class. The simple implementation of `EventBus` is given [here](https://github.com/hasancse91/EventBus-Android-Tutorial).

I used my local machine as a server (localhost). To do so, I created a folder `file_upload_api` in my `www>html` folder (for Linux). Inside this folder I created a folder `files`. This folder will contain my uploaded images. Then put a `PHP` script as a sibling of `files` folder. Here I mention the `upload.php` code:

```php
<?php
    $target_dir = "files/"; //folder name where your files will be stored. create this folder inside "file_upload_api" folder
    $target_file = $target_dir . basename($_FILES["file"]["name"]);

    $response = array('success' => false, 'message' => 'Sorry, there was an error uploading your file.');

    $data = $_POST['sender_information'];
    $json_data = json_decode($data , true);
    $sender_name = $json_data['sender_name'];
    $sender_age = $json_data['sender_age'];


    if (move_uploaded_file($_FILES["file"]["tmp_name"], $target_file))
        $response = array('success' => true, 'message' => 'Hello '.$sender_name.'! You are '.$sender_age.' years old. Your image is uploaded successfully!');

    echo json_encode($response);
?>
```
### Disclaimer
This `PHP` script cannot handle a large file. If you upload a tiny image it'll work fine. But for any large image you'll get error message. To upload large image file please search on Google.
