package com.cai.scheduler.util

import org.springframework.beans.BeansException
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate

class HttpUtil implements ApplicationContextAware{
    ConfigurableApplicationContext cac
    private static RestTemplate restTemplate

    private static HttpExecute execute

    static <T> T postToEntity(String url, Class<T> responseType){
        HttpEntity request = execute.getHttpEntity(null, null)
        return execute.post(url, responseType, request)
    }

    @Override
    void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        cac = applicationContext as ConfigurableApplicationContext
        restTemplate = cac.getBean(RestTemplate)
        execute = new HttpExecute(restTemplate)
    }


}

class HttpExecute implements HttpAction{
    HttpHeaders httpHeaders = new HttpHeaders()
    RestTemplate restTemplate

    HttpExecute(RestTemplate restTemplate){
        this.restTemplate = restTemplate
    }

    void buildHeader(){
        httpHeaders.setContentType(MediaType.APPLICATION_JSON)
        httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED)
    }

    HttpEntity<MultiValueMap<String,String>> getHttpEntity(HttpHeaders headers, MultiValueMap<String,String> map){
        buildHeader()
        if (headers)
            httpHeaders.putAll(headers)
        return new HttpEntity<MultiValueMap<String, String>>(map, httpHeaders)
    }

    HttpEntity<MultiValueMap<String,String>> getHttpEntity(MultiValueMap<String,String> map){
        return getHttpEntity(httpHeaders, map)
    }

    HttpEntity<MultiValueMap<String,String>> getHttpEntity(){
        return getHttpEntity(null , null)
    }

    def <T> T get(String url, Class < T > response){
        return execute(url, HttpMethod.GET, null, response)
    }

    def <T> T post(String url, Class < T > response, Object request){
        return execute(url, HttpMethod.POST, request, response)
    }

    @Override
    def <T> T execute(String url, HttpMethod method, Object request, Class < T > response) {
        return doExecute(url, method, request, response)
    }

    private def <T> T doExecute(String url, HttpMethod method, Object request, Class < T > response) {
        switch (method){
            case HttpMethod.GET:
                return restTemplate.getForObject(url,response,null)
            case HttpMethod.POST:
                return restTemplate.postForObject(url, request, response, null)
            default:
                throw new Exception('该类型暂未实现')
        }
    }
}