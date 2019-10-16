package com.piyush.pictattendance.utils;

public class Resource<T> {

    public T data;
    public Status status;
    public String message;

    private Resource(T data, Status status) {
        this.data = data;
        this.status = status;
    }

    private Resource(Status status, String message)
    {

        this.status = status;
        this.message = message;
    }

    public static <T>Resource <T>loading (T data)
    {
        return new Resource<>(data , Status.LOADING);
    }

    public static <T>Resource <T>success(T data)
    {
        return new Resource<>(data, Status.SUCCESS);
    }
    public static <T>Resource <T>loadedFromDb(T data)
    {
        return new Resource<>(data, Status.LOADED_FROM_DB);
    }

    public static <T>Resource <T>error(String message, T data)
    {
        return new Resource<>( Status.ERROR, message);
    }


}
