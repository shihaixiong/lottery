package com.maxsix.bingo.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by tls on 2015/8/11.
 */
public class GridList<T> implements Serializable {
    private long count;
    private List<T> results;
    private String next;
    private String previous;
    private Extend extend;
    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public String getPrevious() {
        return previous;
    }

    public void setPrevious(String previous) {
        this.previous = previous;
    }

    public List<T> getResults() {
        return results;
    }

    public void setResults(List<T> results) {
        this.results = results;
    }

    public Extend getExtend() {
        return extend;
    }

    public void setExtend(Extend extend) {
        this.extend = extend;
    }
}
