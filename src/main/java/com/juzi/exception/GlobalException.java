package com.juzi.exception;

import com.juzi.result.CodeMsg;

/**
 * @author: juzi
 * @date: 2019/4/7
 * @time: 22:16
 */
public class GlobalException extends RuntimeException{


    private CodeMsg cm;

    public GlobalException(CodeMsg cm){
        super(cm.toString());
        this.cm = cm;
    }

    public CodeMsg getCm() {
        return cm;
    }
}
