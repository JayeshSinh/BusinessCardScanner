package com.business.card.scanner.maker.database.dao;

import com.business.card.scanner.maker.model.Business_Card;

import java.util.List;

public interface Database_Dao {
    void delete(Business_Card business_Card);

    List<Business_Card> getAll_Card() throws Throwable;

    List<Business_Card> getFavData() throws Throwable;

    List<Business_Card> getFromGroup(String str) throws Throwable;

    Business_Card getOnecard(String str);

    void insert(Business_Card business_Card);

    void removeGroup(String str);

    void setFav(String str, String str2);

    void update(Business_Card business_Card);

    void updateGroup(String str, String str2);
}
