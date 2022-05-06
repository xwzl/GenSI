package com.roy.gensi.genapp.application;

import com.roy.gensi.genapp.domain.hisrequest.service.GsRequestDomainService;
import com.roy.gensi.genapp.domain.sysManage.entity.SysManage;
import com.roy.gensi.genapp.domain.sysManage.service.SysManageDomainService;
import com.roy.gensi.util.GsConstants;
import com.roy.gensi.config.GsLogConfig;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/5
 * @description: 用户接口层
 **/

@RestController
@RequestMapping("/sysManage")
public class SysManageController {

    private Logger logger = Logger.getLogger(SysManageController.class);
    @Autowired
    GsRequestDomainService gsRequestDomainService;
    @Autowired
    SysManageDomainService sysManageDomainService;
    @Autowired
    GsLogConfig gsLogConfig;

    @RequestMapping(value = "/newSys", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object newSys(@RequestBody SysManage gsSys) throws Exception {
        logger.info("SysManageAction.newSys: gsmanage=> " + gsSys);
        gsSys.generateKey();
        int opRes = sysManageDomainService.saveSys(gsSys);
        Map<String, Object> res = new HashMap<String, Object>();
        if (opRes > 0) {
            res.put("code", 1);
            res.put("desc", "外围系统创建成功");
        } else {
            res.put("code", 0);
            res.put("desc", "外围系统创建失败。");
        }
        return res;
    }

    @RequestMapping(value = "/deleteSys", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object deleteSys(String sysId) throws Exception {
        logger.info("SysManageAction.deleteSys: sysid=> " + sysId);
        int opRes = sysManageDomainService.deleteSysById(sysId);
        return opRes;
    }

    @RequestMapping(value = "/querySys", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object querySys() throws Exception {
        logger.info("SysManageAction.querySys: ");
        List<SysManage> res = sysManageDomainService.queryAllSys();
        return res;
    }

    @RequestMapping(value = "/passKey", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object passKey(String passKey) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        if (passKey.equals(GsConstants.PASSKEY)) {
            res.put("result", "0");
        } else {
            res.put("result", "1");
        }
        return res;
    }

    @RequestMapping(value = "/getHistory", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object getHistory(HttpServletRequest request,String transId,String serviceCode) throws Exception {
        logger.info("查询历史请求记录：transId => "+transId+";serviceCode => "+serviceCode);
        Map<String, Object> res = new HashMap<String, Object>();
        Map<String, Object> paras = new HashMap<String, Object>();
        paras.put("transId", transId);
        paras.put("serviceCode", serviceCode);
        res.put("code", "1");
        res.put("desc", "获取数据成功！");
        res.put("data",gsRequestDomainService.queryGsRequest(transId,serviceCode));
        return res;
    }

    /**
     * 获取指定目录下transId文件列表
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTransIdLogFiles", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object getTransIdLogFiles(HttpServletRequest request) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            //transId命名的日志文件存放的根目录
            String transIdLogFilesRootPath = gsLogConfig.getTransLogDir();
            String transId = request.getParameter("transId");
            //存储返回结果
            List<Map<String, String>> dataList = new ArrayList<>();
            List<File> files = (List<File>) FileUtils.listFiles(new File(transIdLogFilesRootPath), new String[]{GsConstants.LOG_SUFFIX}, false);
            if (files != null && !files.isEmpty()) {
                //按文件最后修改时间倒序排序
                Collections.sort(files, (file1, file2) ->{
                    return (int) (file2.lastModified() - file1.lastModified());
                });
                //如果transId不为空，只展现该transId的文件。 否则，返回最近的100个日志文件
                Predicate<File> logFileFilter = (f)->{
                    return f.getName().contains(transId.trim());
                };
                if(StringUtils.isNotEmpty(transId)) {
                    files = files.stream().filter(logFileFilter).collect(Collectors.toList());
                }else if(files.size()>GsConstants.LOG_FILE_MAX_SIZE) {
                    files = files.subList(0, GsConstants.LOG_FILE_MAX_SIZE);
                }
                for (File file : files) {
                    Map<String, String> data = new HashMap<>();
                    data.put("transIdLogFileName", file.getName());
                    data.put("transIdLogFilePath", file.getAbsolutePath());
                    dataList.add(data);
                }
            }
            res.put("code", "1");
            res.put("desc", "获取数据成功！");
            res.put("data", dataList);
            res.put("logPath", transIdLogFilesRootPath);
        } catch (Exception ex) {
            res.put("code", "0");
            res.put("desc", "获取数据失败！");
            logger.error(ex.getMessage(), ex);
        }
        return res;
    }

    /**
     * 获取指定transId文件的内容
     *
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/getTransIdLogFileContent", produces = "application/json;charset=UTF-8", method = {RequestMethod.POST})
    public Object getTransIdLogFileContent(HttpServletRequest request,String transIdLogFilePath,String transIdLogFileName) throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        try {
            //transId命名的日志文件存放的根目录
            String content = FileUtils.readFileToString(new File(transIdLogFilePath), "utf-8");
            Map<String, String> data = new HashMap<>();
            data.put("transIdLogFilePath", transIdLogFilePath);
            data.put("transIdLogFileName", transIdLogFileName);
            data.put("transIdLogFileContent", content);
            res.put("code", "1");
            res.put("desc", "获取数据成功！");
            res.put("data", data);
        } catch (Exception ex) {
            res.put("code", "0");
            res.put("desc", "获取数据失败！");
            logger.error(ex.getMessage(), ex);
        }
        return res;
    }
}
