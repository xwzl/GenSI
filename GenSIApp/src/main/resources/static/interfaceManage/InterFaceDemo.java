import com.alibaba.fastjson.JSONObject;

public class InterFaceDemo 
{
	public static void main(String[] args) throws Exception{
		FtClient ftClient = new FtClient();
		//一、基础参数部分
		//服务提供地址
		ftClient.setEndPoint("http://localhost:2222/bigdata/crawlerInterfaceV2");
		//以下三个根据服务处理时长灵活配置。不设也行， 有默认值。
		ftClient.setConnectionRequestTimeout(1000);
		ftClient.setConnectTimeout(30000);
		ftClient.setSocketTimeout(30000);
		//向第三方系统分配
		ftClient.setPublicKey("MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjQTWfGXgF4sxr0PAXa7j8GlgzoReplMTBIcFQYyFubCFDJFQMAMnCuk0Y5DOVcabSdqcO6UkjLROWv+F5xnAy5PReQ19a0ZM+z/j8qen4zuwvDDg0XuLONy7nKnV7O6QQ+EeiZX0IvmHJDnPQd89HKi5qn8rrHT25WKPuWaoXLI1gWiGx3MJxSMyXvqm9ToNoWVYGYgkDxtiX80yWx6+hGOa+d6hfq6oj6JtUIT9tTgitcKNBB36ut0KgSJraySPXhLzbdc0H4b3bwaOZEHERPUntsOTIQKqD4K7Y409LwSAkZL7OA02tWFV2dwTqzgN1Q3IT83qS9lJREQJHaaLmwIDAQAB");
		String ftRes = "";
		//二、header参数部分
		ReqHeader reqHeader = new ReqHeader();
		//向第三方系统分配
		reqHeader.setSysId("myTest");
		//用户名，密码，暂时无验证方案，预备与公司单点登录体系结合验证。预留。
		reqHeader.setSysUser("testUser");
		reqHeader.setSysPwd("testPwd");
		//唯一的请求ID。 重复的transId将直接返回第一次请求的结果。
		reqHeader.setTransId("200706011649500001");
		//服务功能码
		reqHeader.setServiceCode("search_mobile_mark");
		ftClient.setReportHeader(reqHeader);
		
		//三、业务参数部分
		JSONObject reportbody = new JSONObject();
		reportbody.put("mobile", "073188048501");
		ftClient.setReportBody(reportbody.toJSONString());
		
		ftRes = ftClient.postRequest();
		//同步返回请求接收结果。具体的业务结果将向sysId配置的返回路径进行推送。
		System.out.println(ftRes);
	}
}
