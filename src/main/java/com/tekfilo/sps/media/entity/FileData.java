package com.tekfilo.sps.media.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class FileData {

    private String filename;
    private String fileOriginalName;
    private String url;
    private Long size;
    private String created;
    private String lastModified;
    private String lastAccessed;

    private String sizeText;
    private String type;
}
