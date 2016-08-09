package com.weapes.ntpaprseng.crawler.store;

import java.util.List;

/**
 * Created by lawrence on 16/8/9.
 */
public class Paper implements Storable {

    private List<String> authors;

    private String title;
    private String sourceTitle;
    private String ISSN;
    private String eISSN;
    private String DOI;

    private int volum;
    private int issue;
    private int pageBegin;
    private int pageEnd;

    public Paper(final List<String> authors,
                 final String title,
                 final String sourceTitle,
                 final String ISSN,
                 final String eISSN,
                 final String DOI,
                 final int volum,
                 final int issue,
                 final int pageBegin,
                 final int pageEnd) {
        this.authors = authors;
        this.title = title;
        this.sourceTitle = sourceTitle;
        this.ISSN = ISSN;
        this.eISSN = eISSN;
        this.DOI = DOI;
        this.volum = volum;
        this.issue = issue;
        this.pageBegin = pageBegin;
        this.pageEnd = pageEnd;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(final List<String> authors) {
        this.authors = authors;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getSourceTitle() {
        return sourceTitle;
    }

    public void setSourceTitle(final String sourceTitle) {
        this.sourceTitle = sourceTitle;
    }

    public String getISSN() {
        return ISSN;
    }

    public void setISSN(final String ISSN) {
        this.ISSN = ISSN;
    }

    public String geteISSN() {
        return eISSN;
    }

    public void seteISSN(final String eISSN) {
        this.eISSN = eISSN;
    }

    public String getDOI() {
        return DOI;
    }

    public void setDOI(final String DOI) {
        this.DOI = DOI;
    }

    public int getVolum() {
        return volum;
    }

    public void setVolum(final int volum) {
        this.volum = volum;
    }

    public int getIssue() {
        return issue;
    }

    public void setIssue(final int issue) {
        this.issue = issue;
    }

    public int getPageBegin() {
        return pageBegin;
    }

    public void setPageBegin(final int pageBegin) {
        this.pageBegin = pageBegin;
    }

    public int getPageEnd() {
        return pageEnd;
    }

    public void setPageEnd(final int pageEnd) {
        this.pageEnd = pageEnd;
    }

    @Override
    public boolean store() {
        return false;
    }
}
