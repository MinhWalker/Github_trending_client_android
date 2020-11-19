package com.example.githubtrendingclient.Networks;


import com.example.githubtrendingclient.Models.Req.BookmarkReq;
import com.example.githubtrendingclient.Models.Req.UserSigninReq;
import com.example.githubtrendingclient.Models.Req.UserSignupReq;
import com.example.githubtrendingclient.Models.Req.UserUpdate;
import com.example.githubtrendingclient.Models.Res.RepositoryData;
import com.example.githubtrendingclient.Models.Res.UserData;
import com.example.githubtrendingclient.Models.Res.UserProfileData;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface DataClient {

    // User API
    @POST("user/sign-in")
    Call<UserData> signin(@Body UserSigninReq userSigninReq);

    @POST("user/sign-up")
    Call<UserData> signup(@Body UserSignupReq userSignupReq);

    // Profile API
    @GET("user/profile")
    Call<UserProfileData> getUserProfile(@Header("Authorization") String token);

    @PUT("user/profile/update")
    Call<UserProfileData> updateUserProfile(@Header("Authorization") String token, @Body UserUpdate userUpdate);

    // Github repo
    @GET("github/trending")
    Call<RepositoryData> getRepositoryTrending(@Header("Authorization") String token);

    // Bookmark
    @GET("bookmark/list")
    Call<RepositoryData> getListBookmarks(@Header("Authorization") String token);

    @POST("bookmark/add")
    Call<RepositoryData> addBookmark(@Header("Authorization") String token, @Body BookmarkReq bookmarkReq);

    @HTTP(method = "DELETE", path = "bookmark/delete", hasBody = true)
    Call<RepositoryData> deleteBookmark(@Header("Authorization") String token, @Body BookmarkReq bookmarkReq);
}
