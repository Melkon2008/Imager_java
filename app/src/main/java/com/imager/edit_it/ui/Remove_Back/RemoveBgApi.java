package com.imager.edit_it.ui.Remove_Back;

import okhttp3.*;
import java.io.File;
import java.io.IOException;

public class RemoveBgApi {
    private static final String API_URL = "https://api.remove.bg/v1.0/removebg";
    private static final String API_KEY = "gXjmt2Xn1wFLxqjtfh4s3XYB";

    public interface RemoveBgCallback {
        void onSuccess(byte[] image);
        void onFailure(String errorMessage);
    }

    public void removeBackground(String imagePath, RemoveBgCallback callback) {
        OkHttpClient client = new OkHttpClient();
        File imageFile = new File(imagePath);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image_file", imageFile.getName(), RequestBody.create(imageFile, MediaType.parse("image/*")))
                .addFormDataPart("size", "auto")
                .build();

        Request request = new Request.Builder()
                .url(API_URL)
                .addHeader("X-Api-Key", API_KEY)
                .post(requestBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    callback.onFailure(response.message());
                    return;
                }

                byte[] imageBytes = response.body().bytes();
                callback.onSuccess(imageBytes);
            }
        });
    }
}
