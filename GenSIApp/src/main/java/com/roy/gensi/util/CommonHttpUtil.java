package com.roy.gensi.util;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.springframework.http.HttpStatus;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author ：楼兰
 * @date ：Created in 2021/5/6
 * @description:
 **/

public class CommonHttpUtil {
    private static Logger logger = Logger.getLogger(CommonHttpUtil.class);
    private static AtomicInteger sequenceNo = new AtomicInteger(10000);

    public static void randomTimerSleep(long minMillis, long maxMillis) {
        long value = 0;
        Random objRandom = null;
        Calendar nowCalendar = null;

        try {
            // 工作时间进行休眠
            nowCalendar = Calendar.getInstance();
            if (nowCalendar.get(Calendar.HOUR_OF_DAY) > 7 && nowCalendar.get(Calendar.HOUR_OF_DAY) < 20) {
                objRandom = new Random();
                value = objRandom.nextLong();
                value = value % maxMillis;
                if (value < minMillis)
                    value = minMillis;
            } else {
                value = 50;
            }

            // Thread.sleep(value);
            TimeUnit.MILLISECONDS.sleep(value);
        } catch (InterruptedException ignore) {
        }

        return;
    }

    public static String formatDate() {
        String dateString = "";
        Date nowDate = null;
        SimpleDateFormat sdfDate = null;

        sdfDate = new SimpleDateFormat("yyyyMMdd");
        nowDate = new Date();
        dateString = sdfDate.format(nowDate);

        return dateString;
    }

    /**
     * 输出指定格式的当前时间的字符串表示形式
     *
     * @param formatPattern
     * @return
     */
    public static String formatDateNow(String formatPattern) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(formatPattern);
        return simpleDateFormat.format(new Date());
    }

    public static String formatYYMMDDHHMMSSDate() {
        String dateString = "";
        Date nowDate = null;
        SimpleDateFormat sdfDate = null;

        sdfDate = new SimpleDateFormat("yyMMddHHmmss");
        nowDate = new Date();
        dateString = sdfDate.format(nowDate);

        return dateString;
    }

    public static String generateSequence() {
        String seqNo = "";
        Date nowDate = null;
        SimpleDateFormat sdfDate = null;

        sdfDate = new SimpleDateFormat("yyMMddHHmmss");
        nowDate = new Date();
        seqNo = sdfDate.format(nowDate) + sequenceNo.incrementAndGet();
        seqNo = Long.toHexString(Long.parseLong(seqNo));

        return seqNo;
    }

    public static String sendHttpBodyRequest(String requestUrl, String requestJson) {
        int statusCode = 0;
        HttpPost httpPost = null;
        StringEntity reqEntity = null;
        HttpEntity responseEntity = null;
        CloseableHttpClient httpclient = null;
        CloseableHttpResponse httpResponse = null;
        String resultData = "";
        logger.info("---requestUrl---" + requestUrl);
        logger.info("---requestJson---" + requestJson);

        try {
            httpclient = HttpClients.createDefault();

            reqEntity = new StringEntity(requestJson, Consts.UTF_8);
            reqEntity.setContentType("application/json");
            httpPost = new HttpPost(requestUrl);
            httpPost.setEntity(reqEntity);

            long lStartTime = 0, lCostTime = 0;
            lStartTime = System.currentTimeMillis();
            httpResponse = httpclient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            lCostTime = System.currentTimeMillis() - lStartTime;
            logger.info("sendHttpBodyRequest - requestTime[" + lCostTime + "] statusCode = " + statusCode);

            if (statusCode == HttpStatus.OK.value()) {
                responseEntity = httpResponse.getEntity();
                resultData = EntityUtils.toString(responseEntity, "utf-8");
                logger.info(resultData);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            try {
                if (httpclient != null){
                    httpclient.close();
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            httpclient = null;
        }

        return resultData;
    }

    public static String sendHttpParameterRequest(String requestUrl, String reqParameter, String requestJson) {
        int statusCode = 0;
        HttpPost httpPost = null;
        HttpEntity responseEntity = null;
        CloseableHttpClient httpclient = null;
        List<NameValuePair> formParams = null;
        UrlEncodedFormEntity formEntity = null;
        CloseableHttpResponse httpResponse = null;
        String resultData = "";
        logger.info("---requestUrl---" + requestUrl);

        try {
            httpclient = HttpClients.createDefault();

            formParams = new ArrayList<NameValuePair>();
            formParams.add(new BasicNameValuePair(reqParameter, requestJson));
            formEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

            httpPost = new HttpPost(requestUrl);
            // httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(formEntity);
            logger.info("---attribute---" + reqParameter + " => " + requestJson);
            long lStartTime = 0, lCostTime = 0;
            lStartTime = System.currentTimeMillis();
            httpResponse = httpclient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            lCostTime = System.currentTimeMillis() - lStartTime;
            logger.info("requestTime[" + lCostTime + "] statusCode = " + statusCode);

            if (statusCode == HttpStatus.OK.value()) {
                responseEntity = httpResponse.getEntity();
                resultData = EntityUtils.toString(responseEntity, "utf-8");
                logger.info("responseEntity-resultData:" + resultData);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            try {
                if (httpclient != null) {
                    httpclient.close();
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            httpclient = null;
        }

        return resultData;
    }

    public static String sendHttpParameterRequest(String requestUrl, Map<String, Object> params) {
        int statusCode = 0;
        HttpPost httpPost = null;
        HttpEntity responseEntity = null;
        CloseableHttpClient httpclient = null;
        List<NameValuePair> formParams = null;
        UrlEncodedFormEntity formEntity = null;
        CloseableHttpResponse httpResponse = null;
        String resultData = "";
        logger.info("---requestUrl---" + requestUrl);

        try {
            httpclient = HttpClients.createDefault();
            formParams = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                formParams.add(new BasicNameValuePair(key, params.get(key).toString()));
                logger.info("---attribute---" + key + " => " + params.get(key));
            }

            formEntity = new UrlEncodedFormEntity(formParams, Consts.UTF_8);

            httpPost = new HttpPost(requestUrl);
            // httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
            httpPost.setEntity(formEntity);

            long lStartTime = 0, lCostTime = 0;
            lStartTime = System.currentTimeMillis();
            httpResponse = httpclient.execute(httpPost);
            statusCode = httpResponse.getStatusLine().getStatusCode();
            lCostTime = System.currentTimeMillis() - lStartTime;
            logger.info("requestTime[" + lCostTime + "] statusCode = " + statusCode);

            if (statusCode == HttpStatus.OK.value()) {
                responseEntity = httpResponse.getEntity();
                resultData = EntityUtils.toString(responseEntity, "utf-8");
                logger.info("responseEntity-resultData:" + resultData);
            }
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        } finally {
            try {
                if (httpclient != null){
                    httpclient.close();
                }
            } catch (Exception e) {
                logger.warn(e.getMessage(), e);
            }
            httpclient = null;
        }

        return resultData;
    }

    public static void inputstreamToFile(InputStream ins, File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            logger.warn(e.getMessage(), e);
        }
    }

    public String inputStreamToString(InputStream is) {
        String result = null;
        try {
            ByteArrayOutputStream outputstream = new ByteArrayOutputStream();
            byte[] str_b = new byte[1024];
            int i = -1;
            while ((i = is.read(str_b)) > 0) {
                outputstream.write(str_b, 0, i);
            }
            result = outputstream.toString();

        } catch (IOException e) {

            logger.warn(e.getMessage(), e);
        }
        return result;
    }

//    public static void main(String[] args) {
//        try {
//            // CommonUtils.sendHttpRequest("http://192.168.52.213:8080/portal/doCredit/data", "{}");
//            // String requestUrl = "http://218.76.63.90:8010/portal/doCredit/data";
//            String requestUrl = "http://192.168.52.105:8080/fyd/doCredit/data";
//            // String requestJson = FileUtils.readFileToString(new File("D:/Project/rsp_json.txt"));
//
//            com.gensi.manage.utils.CommonUtils.sendHttpParameterRequest(requestUrl, "creditData", "testInfo");
//        } catch (Exception e) {
//            logger.warn(e.getMessage(), e);
//        }
//    }
}
