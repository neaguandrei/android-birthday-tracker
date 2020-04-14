package com.aneagu.birthdaytracker.data.models;

import java.util.Date;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor(staticName = "of")
public class Birthday {

    private String name;

    private Date date;

    private String daysLeft;

    private String pictureUrl;

    private String token;}
