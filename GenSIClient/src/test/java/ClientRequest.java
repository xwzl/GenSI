import com.alibaba.fastjson.JSONObject;
import com.ftoul.ftoulClient.FtClient;
import com.ftoul.ftoulClient.ReqHeader;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 **/

public class ClientRequest {

    public static void main(String[] args) throws Exception {
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", "127.0.0.1");
        System.setProperty("http.proxyPort", "8888");

        FtClient ftClient = new FtClient();
        //一、基础参数部分 所有接口都相同
        //服务提供地址
        ftClient.setEndPoint("http://localhost:8888/genSI/gsServiceAsync");
//        ftClient.setEndPoint("http://localhost:8888/genSI/gsServiceSync");
        //以下三个根据服务处理时长灵活配置。不设也行， 有默认值。
        ftClient.setConnectionRequestTimeout(1000);
        ftClient.setConnectTimeout(30000);
        ftClient.setSocketTimeout(30000);
        //向 GenSI申请，由GenSI分配
//        ftClient.setPublicKey("22MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAv07sqgj5F+72UPN2Pv+vu4ln9e2wN3Mlg1gOJdDYElT02dH/MpYM5KYEUF9Fsf6CVwD4tGMRrrrcS3y0vPlqz9v/O8QGoZwzGY+1MlIWddK+RwcY/i/KAwqMLLRiXXcSvERnPCSn1Uuar4GtjNO8cit9MtezgThccWfmb+ghrjkN/fxJB9rDjKYBFo7nRY07SpUsU/eaGZuxYXQcH4HEhaUORDaiLmwmCLD6mgmY27gDRProCWhv2ybiAdD5aJidn+j9s7L6oTbD4lIL4RqsXMkM5WpSB4cXwkr+QCTr8hLp68+wjz7pc+CmF9j1IC//dK8vfjmgy3DrEVnGFiVxrwIDAQAB");
        ftClient.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAkx1f+wiuPSSYLPgMl9fym+pLKT58/HignCWAorW7YfkMShxSJslGGzR9a4SorR6DEPnEwobqpsuK8VBI0GSd2QotKS9MNomQIQ1YIjTJ7aJfG0TGsVQIS37EFDLoK/aXn9tO/ZSYMa/IVn5KqKcHVpIM6Z9ioAF/GYz+jlt52rufrbjpxH9k7AEI1iHQ72Y0YxKLUz9TqTfM4afjEWGiLpr/4n27bREd+D8R3/rmGCeEkL6gTyiu8kPqeb6zRbkkRENOJmR2TlgDMvG9Wlsn51wXIiidlJLI8g+8IfJYSPbFcOMxjKDfTyeY2y70Ez3LbPqPXIePujv+u7nzKROSSwIDAQAB");
        String ftRes = "";
        //二、header参数部分 同一个应用都相同
        ReqHeader reqHeader = new ReqHeader();
        //向第三方系统分配
        reqHeader.setSysId("MyTestSys");
        //用户名，密码，暂时无验证方案。预留。
        reqHeader.setSysUser("testUser");
        reqHeader.setSysPwd("testPwd");
        //唯一的请求ID。 重复的transId将直接返回第一次请求的结果。
        reqHeader.setTransId("202106241000131");
        //服务功能码-查询手机号码标注
       reqHeader.setServiceCode("search_mobile_tag");
        // reqHeader.setServiceCode("ECHO");
//        reqHeader.setServiceCode("search_mobile_area");
        ftClient.setReportHeader(reqHeader);

        //三、业务参数部分 由提供的接口文档确定
        JSONObject reportbody = new JSONObject();
        reportbody.put("mobile", "15280975199");
        ftClient.setReportBody(reportbody.toJSONString());

        ftRes = ftClient.postRequest();
        //同步返回请求接收结果。具体的业务结果将向sysId配置的返回路径进行推送。
        System.out.println(ftRes);
    }
}
