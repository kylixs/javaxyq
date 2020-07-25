package com.javaxyq.data;

import lombok.Data;

@Data
public class SceneTeleporter {
    private Integer id;
    private int startId;
    private int endId;
    private String startPoint;
    private String endPoint;
    private String description;
}
