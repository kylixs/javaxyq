package com.javaxyq.tool;

import com.javaxyq.util.DBToolkit;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DBTool {

    public static void main(String[] args) {
        DBToolkit.setForceInit(true);
        DBToolkit.prepareDatabase();
    }
}
