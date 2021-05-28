package com.jason.mdtally.entity;


import org.litepal.crud.DataSupport;

import java.util.Date;

public class Bill extends DataSupport {
    private int id;
    private double money;
    private String category;
    private String account;
    private Date date;
}
