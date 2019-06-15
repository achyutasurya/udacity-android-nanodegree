package com.surya.popularmovies;

public class Movie {


    private String releaseDate;
    private String posterPath;

    public Movie(String releaseDate, String posterPath, String overview, String name, String id, int popularity, int voteCount, Double rating, String pos) {
        this.releaseDate = releaseDate;
        this.posterPath = posterPath;
        this.overview = overview;
        this.name = name;
        this.id = id;
        this.popularity = popularity;
        this.voteCount = voteCount;
        this.rating = rating;
        this.pos = pos;
    }

    private String overview, name;
    private String id;
    private String pos;
    private int popularity;
    private int voteCount;
    private Double rating;

    public String getPos() {
        return pos;
    }

    public void setPos(String pos) {
        this.pos = pos;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }
}
