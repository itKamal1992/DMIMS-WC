package com.dmims.dmims.remote;

import com.dmims.dmims.model.MyResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Belal on 10/5/2017.
 */

public interface Api {

    //the base URL for our API
    //make sure you are not using localhost
    //find the ip usinc ipconfig command
    //String BASE_URL = "http://avbrh.gearhostpreview.com/imageupload/";
    String BASE_URL = "http://dmimsdu.in/web/imageupload/";

    //this is our multipart request
    //we have two parameters on is name and other one is description
    @Multipart
    @POST("Api.php?apicall=upload")
    Call<MyResponse> uploadImage2(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);

    @Multipart
    @POST("Api.php?apicall=upload_greivanceimage")
    Call<MyResponse> uploadGreivanceImage(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);

    @Multipart
    @POST("Api.php?apicall=upload_greivanceimage")
    Call<MyResponse> uploadImage3(@Part("image\"; filename=\"myfile.jpg\" ") RequestBody file, @Part("desc") RequestBody desc);

    //imageupload/upload_greivanceimage.php


}
