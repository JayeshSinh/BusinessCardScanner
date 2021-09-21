package com.business.card.scanner.maker.database.dao;

import com.business.card.scanner.maker.model.Group_tab;
import com.business.card.scanner.maker.model.Group_tb;

import java.util.List;

public interface Group_Dao {
    void deleteGroup(Group_tb group_tb);

    List<Group_tab> get_AllGroup();

    void insertGroup(Group_tb group_tb);

    void updateGroup(Group_tb group_tb);
}
