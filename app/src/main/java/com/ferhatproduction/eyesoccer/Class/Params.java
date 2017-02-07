package com.ferhatproduction.eyesoccer.Class;

/**
 * Created by leo on 1/28/17.
 */

public class Params {
    /*** real ***/
    public static final String BASE_URL = "http://103.229.72.232:8080/WebApi/v1";
//    public static final String AUTH_TOKEN = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0aW5nQGdtYWlsLmNvbSIsImF1ZGllbmNlIjoibW9iaWxlIiwiY3JlYXRlZCI6MTQ4NjIxMzA1MTExNiwiZXhwIjoxNDg2ODE3ODUxfQ.b19CAp9lPuUkEwhp6o7rSuk4nSTBbf9hwHQ--Oau6c6LP7FAOhRNkug6rDQv09sjcjro52309oOY_1bRaNLTXw";

    /*** mock ***/
//    public static final String BASE_URL = "https://private-e0fe4d-eyesoccer.apiary-mock.com";
    public static final String AUTH_TOKEN = "Basic ZXllc29jY2VyOmV5ZXNvY2NlcjIwMTc=";

    public static final String URL_NEWS = BASE_URL+"/news";
    public static final String URL_WATCH = BASE_URL+"/videos";
    public static final String URL_EVENTS = BASE_URL+"/events";

    //http://private-68f902-eyesoccer.apiary-mock.com/news
    //http://eyesoccer-didikh.rhcloud.com/WebApi/v1/secure/news

    //https://private-e0fe4d-eyesoccer.apiary-mock.com/
    //Basic ZXllc29jY2VyOmV5ZXNvY2NlcjIwMTc=

    public static final int TYPE_NEWS = 1;
    public static final int TYPE_WATCH = 2;
}
