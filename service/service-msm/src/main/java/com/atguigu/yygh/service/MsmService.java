package com.atguigu.yygh.service;

/**
 * @author Guanghao Wei
 * @create 2023-03-10 15:19
 */
public interface MsmService {
    Boolean send(String phone, String code);
}
