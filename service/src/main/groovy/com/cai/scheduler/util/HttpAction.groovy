package com.cai.scheduler.util

import org.springframework.http.HttpMethod

interface HttpAction {

    def <T> T execute(String url, HttpMethod method, Object request, Class < T > response)
}