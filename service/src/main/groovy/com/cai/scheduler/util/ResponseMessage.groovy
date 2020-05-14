package com.cai.scheduler.util

class ResponseMessage<T> {
    String status
    String message
    T data
    boolean isSuccess
    public static String ERROR_STATUS = "error"
    public static String SUCCESS_STATUS = "success"

    ResponseMessage(String status, String message, T data, boolean isSuccess) {
        this.status = status
        this.message = message
        this.data = data
        this.isSuccess = isSuccess
    }


    ResponseMessage(String status, String message, boolean isSuccess) {
        this.status = status
        this.message = message
        this.isSuccess = isSuccess
    }


    ResponseMessage(String status, T data, boolean isSuccess) {
        this.status = status
        this.data = data
        this.isSuccess = isSuccess
    }


    ResponseMessage(String status) {
        this.status = status
    }
}