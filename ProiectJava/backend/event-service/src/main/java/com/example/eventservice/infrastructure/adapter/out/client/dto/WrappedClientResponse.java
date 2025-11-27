package com.example.eventservice.infrastructure.adapter.out.client.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WrappedClientResponse {

    private List<ClientWrapper> content;
    private int currentPage;
    private int totalItems;
    private int totalPages;

    public List<ClientWrapper> getContent() { return content; }
    public void setContent(List<ClientWrapper> content) { this.content = content; }

    public int getCurrentPage() { return currentPage; }
    public void setCurrentPage(int currentPage) { this.currentPage = currentPage; }

    public int getTotalItems() { return totalItems; }
    public void setTotalItems(int totalItems) { this.totalItems = totalItems; }

    public int getTotalPages() { return totalPages; }
    public void setTotalPages(int totalPages) { this.totalPages = totalPages; }
}
