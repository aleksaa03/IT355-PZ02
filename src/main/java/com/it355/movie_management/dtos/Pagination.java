package com.it355.movie_management.dtos;

public class Pagination {
    private int page = 1;
    private int pageSize = 10;
    private String sortExp = "id";
    private String sortOrd = "DESC";

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortExp() {
        return sortExp;
    }

    public void setSortExp(String sortExp) {
        this.sortExp = sortExp;
    }

    public String getSortOrd() {
        return sortOrd;
    }

    public void setSortOrd(String sortOrd) {
        this.sortOrd = sortOrd;
    }
}