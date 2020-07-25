package com.javaxyq.action;

import com.javaxyq.core.ApplicationHelper;
import com.javaxyq.core.DataManager;
import com.javaxyq.event.ItemEvent;
import com.javaxyq.event.ItemListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class WeaponItemHandler implements ItemListener {

    private DataManager dataManager;

    public WeaponItemHandler() {
        dataManager = ApplicationHelper.getApplication().getDataManager();
    }

    @Override
    public void onItemUsed(ItemEvent evt) {
        log.warn("todo ...");
    }
}