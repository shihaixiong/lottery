package com.maxsix.bingo.vo;

import java.io.Serializable;

/**
 * Created by shihaixiong on 2016/6/2.
 */
public class Lottery implements Serializable{
    private Integer id;
    private String name;
    private Double ratio;
    private Integer unit;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getRatio() {
        return ratio;
    }

    public void setRatio(Double ratio) {
        this.ratio = ratio;
    }

    public Integer getUnit() {
        return unit;
    }

    public void setUnit(Integer unit) {
        this.unit = unit;
    }
}
