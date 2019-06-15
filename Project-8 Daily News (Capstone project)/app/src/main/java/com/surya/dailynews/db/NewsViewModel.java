package com.surya.dailynews.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;
import java.util.List;

public class NewsViewModel extends AndroidViewModel {
    public NewsRepos newsRepositary;
    public LiveData<List<News>> getAllFav;
    public String newsid;

    public NewsViewModel(@NonNull Application application) {
        super(application);
        newsRepositary=new NewsRepos(application);
        getAllFav=newsRepositary.getGetallnews();
    }
    public String readid(String id){

        newsid=newsRepositary.readid(id);
        return newsid;
    }
    public void addnews(News news){
        newsRepositary.addfav(news);
    }
    public void removenews(News news){
        newsRepositary.removefav(news);
    }
    public LiveData<List<News>> getGetAllNews(){
        return getAllFav;
    }
}
