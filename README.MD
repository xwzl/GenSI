# 自行实现的开放平台Demo。
- 启动zookeeper，作为dubbo服务注册中心
- 启动mysql，存储数据。

# 工程结构：
- GenSiServer: 开放平台中心，负责应用信息维护、接口请求解密、日志整合以及请求转发。
- GenSIResource: 后台应用服务，负责通过Dubbo对外提供手机号码标注服务，简单的使用sogou的搜索结果模拟内部业务。
- GensIInterface: 前后台微服务调用的接口。
- GenSIClient: 模拟外部应用客户端。通过调用封装的客户端API，请求接口服务。

# 测试流程：
1. 依次启动 GenSIServer, GenSIResource, GenSIClient三个应用。
2. 启动完成后，访问GenSIServer的应用配置页面：http://localhost:8888/interfaceManage/index.html#/manage/interface，访问密钥gensi
3. 在应用配置页面配置应用信息sysId: MyTestSys, sysName:genSiClient，然后响应地址配置： http://localhost:8890/client/receiveMessage 其他的就不用配了。配置完成后会默认生成一对RSA密钥。
4. 在GenSIClient中执行test目录下的ClientTest的main方法，模拟向GenSIServer发起请求，需要按照注释修改参数。
5. 发起请求后，GenSIServer将对请求进行签名验证，通过后，检查是否有历史记录，有历史记录就直接推送历史结果。没有历史记录就将请求异步转发到GenSIResource。同时立即返回给GenSIClient请求接收成功的响应信息。
6. GenSIResource接收到请求后，将访问Sogou，获得手机号的标注信息或位置信息，返回给GenSIServer。GenSIServer收到异步结果后，会向GenSIClient主动推送响应结果。

# 关于应用配置中的请求参数：notifyParam
这个参数，如果配置了,例如aaa，GenSIServer将会向Client推送 ?aaa={xxxxx}的结果。  
而如果没有配置， GenSIServer会向Client推送?{xxx}的结果。
另外，给应用预留了一些参数，做其他检查用。

#  请求日志
请求日志以Log4j直接记录系统执行情况，并保存到本地。GenSIServer可以通过日志查询页面查询每个transId的详细请求日志。