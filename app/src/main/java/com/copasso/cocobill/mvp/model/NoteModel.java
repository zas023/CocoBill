package com.copasso.cocobill.mvp.model;

public interface NoteModel {

    void getNote(int id);

    void addSort(int userid, String sortName, String sortImg, boolean income);

    void addPay(int userid, String payName, String payImg, String payNum);

    void onUnsubscribe();
}
