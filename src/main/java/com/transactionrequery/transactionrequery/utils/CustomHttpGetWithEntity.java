package com.transactionrequery.transactionrequery.utils;

import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import java.net.URI;

public class CustomHttpGetWithEntity extends HttpEntityEnclosingRequestBase {

    public final static String GET_METHOD = "GET";

    public CustomHttpGetWithEntity(final URI uri) {
        super();
        setURI(uri);
    }

    public CustomHttpGetWithEntity(final String uri) {
        super();
        setURI(URI.create(uri));
    }

    @Override
    public String getMethod() {
        return GET_METHOD;
    }
}
