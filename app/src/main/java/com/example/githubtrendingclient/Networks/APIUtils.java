package com.example.githubtrendingclient.Networks;

public class APIUtils {

    // http://35.194.254.235:3000 Server
    // http://192.168.20.100:3000 Local
    // http://10.22.164.216:3000  fpt

//    public static final String Base_url = "http://172.20.10.10:3000/";
    public static final String Base_url = "http://35.194.254.235:3000/";

    public static DataClient getData() {
        return RetrofitClient.getClient(Base_url).create(DataClient.class);
    }

}
