package com.javaxyq.data;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class SceneTeleporter implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;
    private int startId;
    private int endId;
    private String startPoint;
    private String endPoint;
    private String description;

    public SceneTeleporter(Integer id) {
        this.id = id;
    }

    public SceneTeleporter(Integer id, int startId, int endId, String startPoint, String endPoint) {
        this.id = id;
        this.startId = startId;
        this.endId = endId;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
    }
}
