package com.itbjx.oauth2.uaa.domain;

import lombok.Data;


@Data
public class PermissionDto {

    private String id;
    private String code;
    private String description;
    private String url;
}
