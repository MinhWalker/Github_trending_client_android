package com.example.githubtrendingclient;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.githubtrendingclient.Models.Req.BookmarkReq;
import com.example.githubtrendingclient.Models.Res.Repository;
import com.example.githubtrendingclient.Models.Res.RepositoryData;
import com.example.githubtrendingclient.Networks.APIUtils;
import com.example.githubtrendingclient.Networks.DataClient;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RepoAdapter extends BaseAdapter {

    private Activity activity;
    private int layout;
    private List<Repository> repositoryList;

    public RepoAdapter(Activity activity, int layout, List<Repository> repositoryList) {
        this.activity = activity;
        this.layout = layout;
        this.repositoryList = repositoryList;
    }

    @Override
    public int getCount() {
        return repositoryList.size();
    }

    @Override
    public Object getItem(int position) {
        return repositoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        TextView tvNameRepo;
        TextView tvDescriptionRepo;
        TextView tvLangRepo;
        TextView tvStartRepo;
        TextView tvForkRepo;
        final CheckBox cbBookmark;
        LinearLayout gallery;
        TextView tvStarsTodayRepo;

        if (view == null) {
            view = activity.getLayoutInflater().inflate(layout, null);
            tvNameRepo = view.findViewById(R.id.tvNameRepo);
            tvDescriptionRepo = view.findViewById(R.id.tvDescriptionRepo);
            tvLangRepo = view.findViewById(R.id.tvLangRepo);
            tvStartRepo = view.findViewById(R.id.tvStartRepo);
            tvForkRepo = view.findViewById(R.id.tvForkRepo);
            cbBookmark = view.findViewById(R.id.cbBookmark);
            gallery = view.findViewById(R.id.gallery);
            tvStarsTodayRepo = view.findViewById(R.id.tvStarsTodayRepo);
            view.setTag(R.string.name, tvNameRepo);
            view.setTag(R.string.description, tvDescriptionRepo);
            view.setTag(R.string.lang, tvLangRepo);
            view.setTag(R.string.stars, tvStartRepo);
            view.setTag(R.string.fork, tvForkRepo);
            view.setTag(R.string.bookmarked, cbBookmark);
            view.setTag(R.string.contributors, gallery);
            view.setTag(R.string.starsToday, tvStarsTodayRepo);
        } else {
            tvNameRepo = (TextView)view.getTag(R.string.name);
            tvDescriptionRepo = (TextView)view.getTag(R.string.description);
            tvLangRepo = (TextView)view.getTag(R.string.lang);
            tvStartRepo = (TextView)view.getTag(R.string.stars);
            tvForkRepo = (TextView)view.getTag(R.string.fork);
            cbBookmark = (CheckBox)view.getTag(R.string.bookmarked);
            gallery = (LinearLayout)view.getTag(R.string.contributors);
            tvStarsTodayRepo = (TextView)view.getTag(R.string.starsToday);
        }
        final Repository repository = repositoryList.get(position);
        tvNameRepo.setText(repository.getName());
        tvDescriptionRepo.setText(repository.getDescription());
        tvLangRepo.setText(repository.getLang());
        tvStartRepo.setText(repository.getStars());
        tvForkRepo.setText(repository.getFork());
        cbBookmark.setChecked(repository.getBookmarked());

        if (repository.getColor() == "") {
            tvLangRepo.setTextColor(Color.BLACK);
        } else {
            tvLangRepo.setTextColor(Color.parseColor(repository.getColor()));
        }

        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        for (int i = 0; i < repository.getContributors().size(); i++)  {
            View viewGallery = inflater.inflate(R.layout.item_layout, gallery, false);
            ImageView imageView = viewGallery.findViewById(R.id.igAvatar);
            if (repository.getContributors().get(i) == "") {
                Picasso.get().load("https://avatars0.githubusercontent.com/u/2436188?s=40&v=4").into(imageView);
            } else {
                Picasso.get().load(repository.getContributors().get(i)).into(imageView);
            }
            gallery.addView(viewGallery);
        }
        tvStarsTodayRepo.setText(repository.getStarsToday());

        SharedPreferences pref = activity.getSharedPreferences("token", Context.MODE_PRIVATE);
        final String token = "Bearer " + pref.getString("token", null);


        cbBookmark.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    BookmarkReq bookmarkReq = new BookmarkReq();
                    bookmarkReq.setRepo(repository.getName());

                    final DataClient dataClient = APIUtils.getData();
                    final Call<RepositoryData> callback = dataClient.addBookmark(token, bookmarkReq);
                    callback.enqueue(new Callback<RepositoryData>() {
                        @Override
                        public void onResponse(Call<RepositoryData> call, Response<RepositoryData> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(activity, "bookmark success!", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences pref = activity.getSharedPreferences("token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(activity, MainActivity.class);
                                activity.startActivity(intent);
                                Toast.makeText(activity, "Fail!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RepositoryData> call, Throwable t) {
                            Toast.makeText(activity, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    BookmarkReq bookmarkReq = new BookmarkReq();
                    bookmarkReq.setRepo(repository.getName());

                    final DataClient dataClient = APIUtils.getData();
                    final Call<RepositoryData> callback = dataClient.deleteBookmark(token, bookmarkReq);
                    callback.enqueue(new Callback<RepositoryData>() {
                        @Override
                        public void onResponse(Call<RepositoryData> call, Response<RepositoryData> response) {
                            if(response.isSuccessful()) {
                                Toast.makeText(activity, "Delete bookmark success!", Toast.LENGTH_SHORT).show();
                            } else {
                                SharedPreferences pref = activity.getSharedPreferences("token", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.apply();
                                Intent intent = new Intent(activity, MainActivity.class);
                                activity.startActivity(intent);
                                Toast.makeText(activity, "Fail!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<RepositoryData> call, Throwable t) {
                            Toast.makeText(activity, "network failure :( inform the user and possibly retry", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        return view;
    }
}
