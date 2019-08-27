package com.gcloud.boot.config;

import com.gcloud.core.error.ErrorCodes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/*
 * @Desccription 错误码初始化，读取所有jar包中的错误码文件，把错误码和错误信息对应放到内存map中
 * @Date 2018/2/6
 * @Created by yaowj
 */
@Configuration
public class ErrorCodeConfig {

    @Bean
    public ErrorCodes errorCodes(){

        ErrorCodes errorCodes = new ErrorCodes();
        errorCodes.init();

        return errorCodes;
    }

}
