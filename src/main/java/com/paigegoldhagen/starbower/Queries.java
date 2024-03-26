package com.paigegoldhagen.starbower;

import org.apache.commons.lang3.tuple.Pair;

import java.util.List;

/**
 * Setting and getting SQL query strings.
 */
public class Queries {
    private final List<Pair<String, String>> QueryList;

    public Queries(List<Pair<String, String>> queryList) {
        this.QueryList = queryList;
    }

    public String getQueryString(String queryName) {
        String queryString = null;

        for (Pair<String, String> queryPair : QueryList) {
            if (queryPair.getKey().equals(queryName)) {
                queryString = queryPair.getValue();
                break;
            }
        }
        return queryString;
    }
}