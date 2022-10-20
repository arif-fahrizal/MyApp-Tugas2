package com.example.myapp_tugas2.Model;

import java.util.List;

public class ResponseModel {
    private int code;
    private String message;
    private List<DataModel> data;

    public int getCode() {
        return code;
    }

    public void setKode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataModel> getData() {
        return data;
    }

    public void setData(List<DataModel> data) {
        this.data = data;
    }
}
