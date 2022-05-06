package com.roy.gensi.config;

import com.roy.gensi.util.GsConstants;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/

@Component
@ConfigurationProperties(prefix = "gs.log")
public class GsLogConfig {

    private Logger fLogger = Logger.getLogger(this.getClass());

    private String transLogDir;

//	private String LOG_SUFFIX=".gsTransLog";

//	private FileAppender fa = null;

    public String getTransLogDir() {
        return transLogDir;
    }

    public void setTransLogDir(String transLogDir) {
        this.transLogDir = transLogDir;
    }

    public void addTransLogAppender(Logger logger,String transId){
        String targetFileName=  this.getTransLogDir()+"/"+transId+"."+ GsConstants.LOG_SUFFIX;
        fLogger.info("追加trans日志到"+targetFileName);
        //log4j Appender 可以重复进行add或者remove，因此不需要判断是否已经有fa
        if(null != logger && StringUtils.isNotEmpty(transId)){
            logger.removeAppender("fa");
            FileAppender fa = GsConstants.getTransLogAppender();
            fa.setFile(targetFileName);
            fa.activateOptions();//将配置生效
            logger.removeAllAppenders();
            logger.setAdditivity(true);

            logger.addAppender(fa);
        }
    }

    public void removeTransLogAppender(Logger logger){
        //log4j Appender 可以重复进行add或者remove，因此不需要判断是否已经有fa
        logger.removeAppender("fa");
    }

}
