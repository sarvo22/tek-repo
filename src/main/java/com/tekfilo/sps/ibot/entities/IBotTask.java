package com.tekfilo.sps.ibot.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Data
@Getter
@Setter
public class IBotTask implements Serializable {

    private String botUniqueId;
    private Integer botId;
    private String botName;
    private String botCode;
    private String botDescription;
    private String cronExpression;
    private Integer enabled;
    private Integer locked;
    private Integer companyId;
    private List<ApiConfigEntity> apiConfigEntityList;
    private boolean apiService;

}
