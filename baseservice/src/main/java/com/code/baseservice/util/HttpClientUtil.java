package com.code.baseservice.util;


import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.CharsetUtils;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
public class HttpClientUtil {
	private static final String DEFAULT_CHARSET = "utf-8";
	public static final String STATUS_NOT200 = "returnNot200";
	public static final String STATUS_CONNECTEXCEPTION = "connectExceptionOccurs";
	
    public static String doGet(String url, Map<String, Object> param) {

          // 创建Httpclient对象
          CloseableHttpClient httpclient = HttpClients.createDefault();
          String resultString = "";
          CloseableHttpResponse response = null;
          try {
              // 创建uri
              URIBuilder builder = new URIBuilder(url);
              if (param != null) {
                  for (String key : param.keySet()) {
                      builder.addParameter(key, String.valueOf(param.get(key)));
                      }
                  }
              URI uri = builder.build();
              // 创建http GET请求
              HttpGet httpGet = new HttpGet(uri);
              // 执行请求
              response = httpclient.execute(httpGet);
              // 判断返回状态是否为200
              if (response.getStatusLine().getStatusCode() == 200) {
                  resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
                  }
              } catch (Exception e) {
                  e.printStackTrace();
              } finally {
                  try {
                      if (response != null) {
                          response.close();
                      }
                      httpclient.close();
                      } catch (IOException e) {
                          e.printStackTrace();
                      }
              }
          return resultString;
          }

    public static String doGet(String url) {
          return doGet(url, null);
          }

    public static String doPost(String url, Map<String, String> param) {
        if (!url.startsWith("http")) {
            url = "http://".concat(url);
        }
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList, "UTF-8");
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }
    
    
    
    public static String doPost2(String url, Map<String, Object> param) {
        if (!url.startsWith("http")) {
            url = "http://".concat(url);
        }
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, (String)param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }
    
    public static String doPost(String url) {
            return doPost(url, null);
        }

    public static String doPostJson(String url, String json) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return resultString;
    }

    public static CloseableHttpResponse doPostResponse(String url, Map<String, Object> param) {
        // 创建Httpclient对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, (String) param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList);
                httpPost.setEntity(entity);
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return response;
    }


    /**
     *
     * @param url
     * @param string
     * @return
     */
    public static String doPostString(String url, String string) {
    	log.info("开始doPostString:url={}，string={} ",url,string);
        
        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String ret = null; 
        try {
            HttpPost httpPost = new HttpPost(url);
            HttpEntity entityReq = new StringEntity(string,
        	        CharsetUtils.get("utf-8"));
            httpPost.setEntity(entityReq);
            httpPost.setHeader("Content-Type", "text/xml;charset=UTF-8");  
            Long s = System.currentTimeMillis();
            response = httpclient.execute(httpPost);
//            System.out.println( "<<<" + response.getStatusLine());
            if (response.getStatusLine().getStatusCode() == 200) {
	            HttpEntity entity = response.getEntity();
	            ret = EntityUtils.toString(entity);
	            EntityUtils.consume(entity);
	            ret = new String(ret.getBytes("iso8859-1"), "utf-8");
            } else {
            	ret = STATUS_NOT200;
            }
        } catch (Exception e) {
           log.error("Exception={}",e);
            ret = STATUS_CONNECTEXCEPTION;
        } finally {
            close(response, httpclient);
        }

        return ret;
    } 
    /**
     * close <code>CloseableHttpResponse</code> and <code>CloseableHttpClient</code>
     * 
     * @param response <code>CloseableHttpResponse</code>
     * @param httpclient <code>CloseableHttpClient</code>
     */
    private static void close(CloseableHttpResponse response,
            CloseableHttpClient httpclient) {
        if (response != null) {
            try {
                response.close();
                response = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (httpclient != null) {
            try {
                httpclient.close();
                httpclient = null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
