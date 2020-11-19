package com.example.githubtrendingclient.Models.Req;

public class BookmarkReq {
    private String repo;

    public BookmarkReq() {
    }

    public BookmarkReq(String repo) {
        this.repo = repo;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }
}
