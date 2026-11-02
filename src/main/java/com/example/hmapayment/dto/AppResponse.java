package com.example.hmapayment.dto;

import java.util.List;

public class AppResponse<T> {
    private T data;
    private String message;
    private boolean status;
    private List<String> errors;
    private List<T> dataList;

    public AppResponse() {}

    public void setData(T data) {
        this.data = data;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Object getData() {
        return data;
    }

    public String getMessage() {
        return message;
    }

    public boolean isStatus() {
        return status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public List<T> getDataList() {
        return dataList;
    }
}

