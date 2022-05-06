package com.roy.gensi.util;

import org.apache.log4j.FileAppender;
import org.apache.log4j.PatternLayout;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description:
 **/

public class GsConstants {

    //系统管理页面的访问密钥
    public static final String PASSKEY = "gensi";
    //请求级别日志文件后缀
    public static final String LOG_SUFFIX = "gsTransLog";
    //日志查询页面最大的展现文件输。
    public static final int LOG_FILE_MAX_SIZE = 100;

    private static FileAppender fa;

    public static FileAppender getTransLogAppender() {
        if(null == fa) {
            FileAppender logfa = new FileAppender();
            PatternLayout layout = new PatternLayout();
            String conversionPattern = "%d %-5p %c [%L] - %m%n";
            layout.setConversionPattern(conversionPattern);
            logfa.setLayout(layout);//输出形式
            logfa.setName("fa");//Appender别名
            fa = logfa;
        }
        return fa;
    }
}
