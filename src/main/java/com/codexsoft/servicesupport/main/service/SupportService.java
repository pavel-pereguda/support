package com.codexsoft.servicesupport.main.service;

public interface SupportService {

     void checkAsync(String msg) throws InterruptedException;

     void checkAsyncPooled(String msg) throws InterruptedException;
}
