package com.gustavoalves.complementary_activities.utils.entities;

import lombok.Getter;

@Getter
public class Response<T> {
    final T entity;
    final DefaultError error;


    public Response(T entity) {
        this.entity = entity;
        this.error = null;
    }

    public Response(DefaultError error) {
        this.error = error;
        this.entity = null;
    }
}
