package com.dahoraapps.snowmanfinder.dtos;

import com.dahoraapps.snowmanfinder.models.Snowman;

import java.util.List;

public class SnowmanDTO {

    private Integer count;
    private List<Snowman> results;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<Snowman> getResults() {
        return results;
    }

    public void setResults(List<Snowman> results) {
        this.results = results;
    }

}
