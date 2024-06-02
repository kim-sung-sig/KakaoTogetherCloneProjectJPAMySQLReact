package com.example.kakao.global.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class RsData<T> {
    private String msg;
    private T data;

    public static <T> RsData<T> of(String msg, T data){
        return new RsData<>(msg, data);
    }

    public static <T> RsData<T> of(String msg){
        return of(msg, null);
    }

    public static <T> RsData<T> of(Boolean msg, T data){
        String msgstr = null;
        if(msg != null) msgstr = msg.toString();

        return new RsData<>(msgstr, data);
    }

    public static <T> RsData<T> of(Boolean msg){
        return of(msg, null);
    }

}
