package com.sapient.learning.domain;

import java.util.List;

public class Data {

    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                ", data='" + data + '\'' +
                '}';
    }
}
