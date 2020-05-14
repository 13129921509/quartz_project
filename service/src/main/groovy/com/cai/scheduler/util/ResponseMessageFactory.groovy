package com.cai.scheduler.util

class ResponseMessageFactory {
    static ResponseMessage success() {
        return new ResponseMessage(ResponseMessage.SUCCESS_STATUS, null, true)
    }

    static ResponseMessage success(String msg, Object data) {
        return new ResponseMessage(ResponseMessage.SUCCESS_STATUS, msg, data, true)
    }

    static ResponseMessage success(String msg) {
        return new ResponseMessage(ResponseMessage.SUCCESS_STATUS, msg, true)
    }

    static ResponseMessage error(String msg) {
        return new ResponseMessage(ResponseMessage.ERROR_STATUS, msg, false)
    }
}