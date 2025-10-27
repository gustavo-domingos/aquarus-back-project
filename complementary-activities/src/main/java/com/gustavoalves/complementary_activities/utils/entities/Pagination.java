package com.gustavoalves.complementary_activities.utils.entities;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class Pagination <T> {
    public List<T> items;
    public int count;
    public int page;
}
