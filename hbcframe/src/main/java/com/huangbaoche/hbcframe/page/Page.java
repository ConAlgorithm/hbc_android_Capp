package com.huangbaoche.hbcframe.page;

/**
 * Created by ZHZEPHI on 2015/10/16.
 */
public class Page {

    public enum pageType{FIRST,NEXT}

    private int pageSize = 10;
    private int pageIndex = -pageSize;

    public int initFirstPage() {
        return pageIndex = -pageSize;
    }

    public int getNextPage() {
        return pageIndex+pageSize;
    }

    public void setPageIndex() {
        pageIndex+=pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}
