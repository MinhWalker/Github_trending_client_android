package com.example.githubtrendingclient;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.githubtrendingclient.Models.Res.RepositoryData;
import com.example.githubtrendingclient.Networks.APIUtils;
import com.example.githubtrendingclient.Networks.DataClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TrendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TrendingFragment extends Fragment {

//    final static String token = "Bearer " + "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJVc2VySWQiOiIyMDI2MzdlMC0yOGI0LTExZWItOTAxYy1hY2RlNDgwMDExMjIiLCJSb2xlIjoiTUVNQkVSIiwiZXhwIjoxNjA1Nzc0MzM2fQ.9D18Kmlats0nQZfSxm4dzMzZGZLjV_3Ou3-HvYIlutw";

    private ListView listView;
    private RepoAdapter adapter;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public TrendingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TrendingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TrendingFragment newInstance(String param1, String param2) {
        TrendingFragment fragment = new TrendingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_trending, container, false);
    }

    @Override
    public void onViewCreated(@NonNull final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences pref = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
        final String token = "Bearer " + pref.getString("token", null);

        final DataClient dataClient = APIUtils.getData();
        final Call<RepositoryData> callback = dataClient.getRepositoryTrending(token);
        callback.enqueue(new Callback<RepositoryData>() {
            @Override
            public void onResponse(Call<RepositoryData> call, Response<RepositoryData> response) {
                listView = view.findViewById(R.id.listView);

                if(response.isSuccessful()) {
                    adapter = new RepoAdapter(getActivity(), R.layout.repos_view, response.body().getData());
                    listView.setAdapter(adapter);
                    Toast.makeText(getActivity(), "Success!", Toast.LENGTH_SHORT).show();
                } else {
                    SharedPreferences pref = getActivity().getSharedPreferences("token", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    Toast.makeText(getActivity(), "Fail!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RepositoryData> call, Throwable t) {
                Toast.makeText(getActivity(), "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
            }
        });
    }

}