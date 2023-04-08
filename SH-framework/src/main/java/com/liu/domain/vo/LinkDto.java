package com.liu.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LinkDto {
    private Long id;

    private String name;

    private String logo;

    private String description;
    //网站地址
    private String address;
    private String status;
}
