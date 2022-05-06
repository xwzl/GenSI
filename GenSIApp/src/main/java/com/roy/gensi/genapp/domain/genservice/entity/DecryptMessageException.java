package com.roy.gensi.genapp.domain.genservice.entity;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class DecryptMessageException extends RuntimeException{
    public DecryptMessageException() {
    }

    public DecryptMessageException(String message) {
        super(message);
    }
}
