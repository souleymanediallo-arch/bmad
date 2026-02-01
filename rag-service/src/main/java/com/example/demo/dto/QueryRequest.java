package com.example.demo.dto;

public class QueryRequest {
    private String query;
    private String sessionId;

    public QueryRequest() {}

    public QueryRequest(String query, String sessionId) {
        this.query = query;
        this.sessionId = sessionId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
}
