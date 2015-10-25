package com.xiaocoder.android.fw.general.util;

import com.xiaocoder.android.fw.general.application.XCConfig;
import com.xiaocoder.android.fw.general.io.XCIO;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Deprecated
public class UtilHttpOrigin {

    /**
     * 如果getResponseCode==HTTP_OK,则返回一个连接成功的HttpURLConnection对象
     * 该con可以直接获取如inputstream,contentLength等 如果getResponseCode!=HTTP_OK,则返回null
     * 注:该con只能获取一次inputstream,如果向获取多次,则要调用多次requestByGet方法
     */
    public static HttpURLConnection requestByGet(String urlStr,
                                                 HashMap<String, Object> requestParams) throws Exception {
        HttpURLConnection conn = null;
        if (requestParams == null || requestParams.isEmpty()) {
            conn = (HttpURLConnection) new URL(urlStr).openConnection();// 一定得先获取连接,再设置参数
        } else {
            StringBuilder sb = new StringBuilder(urlStr);
            sb.append("?");
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                value = URLEncoder.encode(value, XCConfig.ENCODING_UTF8);
                sb.append(key).append("=").append(value).append("&");
            }
            sb.deleteCharAt(sb.length() - 1);
            conn = (HttpURLConnection) new URL(sb.toString()).openConnection();
        }
        conn.setRequestMethod(XCConfig.GET);
        conn.setConnectTimeout(10000);
        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
            return conn;
        }
        return null;
    }

    public static HttpURLConnection requestByPost(String urlStr,
                                                  HashMap<String, Object> requestParams) throws Exception {
        // Content-Type: application/x-www-form-urlencoded
        // Content-Length: 24
        // account=android&pwd=1234
        HttpURLConnection conn = null;
        StringBuilder content = new StringBuilder("");

        if (requestParams != null && !requestParams.isEmpty()) {
            for (Map.Entry<String, Object> entry : requestParams.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue().toString();
                value = URLEncoder.encode(value,XCConfig.ENCODING_UTF8);
                content.append(key).append("=").append(value).append("&");
            }
            content.deleteCharAt(content.length() - 1);
        }
        conn = (HttpURLConnection) new URL(urlStr).openConnection();
        conn.setRequestMethod(XCConfig.POST);
        conn.setConnectTimeout(10000);
        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        conn.setRequestProperty("Content-Length", content.toString().length() + "");
        conn.setDoOutput(true);// 允许对外输出
        OutputStream os = conn.getOutputStream();
        os.write(content.toString().getBytes(), 0, content.toString().getBytes().length);
        os.flush();
        if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
            return conn;
        }
        return null;
    }

    public static InputStream httpClientByGet(String url) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient(); // 相当于浏览器
        HttpGet get = new HttpGet(url); // 指定请求方式
        HttpResponse response = client.execute(get); // 发送请求
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) { // 判断请求是否成功
            InputStream in = response.getEntity().getContent();
            return in;
        }
        return null;
    }

    /**
     * 参数param: List<NameValuePair> param = new ArrayList<NameValuePair>();
     * <p/>
     * 构建请求体数据 param.add(new BasicNameValuePair("username", "123"));
     * <p/>
     * NameValuePair的接口,BasicNameValuePair是实现类
     */
    public static InputStream httpClientByPost(String url,
                                               List<NameValuePair> param) throws ClientProtocolException, IOException {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        // StringEntity entity = new StringEntity(xml, "<xml>");
        UrlEncodedFormEntity form = new UrlEncodedFormEntity(param, XCConfig.ENCODING_UTF8);
        post.setEntity(form);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream in = response.getEntity().getContent();
            return in;
        }
        return null;
    }

    public static String httpClientUpload(String url, String[] names, File file) throws ClientProtocolException, IOException {

        DefaultHttpClient client = new DefaultHttpClient();// 拿到浏览器
        HttpPost post = new HttpPost(url); // 指定请求方式
        MultipartEntity form = new MultipartEntity();// 生成表单
        form.addPart("filename", new StringBody(names[0], Charset.forName(XCConfig.ENCODING_UTF8)));// 文件名
        form.addPart("filedes", new StringBody(names[1], Charset.forName(XCConfig.ENCODING_UTF8))); // 文件描述
        form.addPart("formfile", new FileBody(file)); // 文件
        post.setEntity(form);
        HttpResponse response = client.execute(post);
        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
            InputStream in = response.getEntity().getContent();
            return new String(XCIO.toBytesByInputStream(in), XCConfig.ENCODING_UTF8);
        }
        return null;
    }
}
/*
 * //非第三方框架 做腾讯一键分享时的混合表单的例子
 * 
 * form.addPart("oauth_consumer_key", new StringBody("801534601",
 * Charset.forName("UTF-8")));
 * 
 * form.addPart("access_token", new
 * StringBody(sp.getString("access_token","ea280f0faa2b7a63ed45ceadec54c50f"),
 * Charset.forName("UTF-8")));
 * 
 * form.addPart("openid", new StringBody(sp.getString("openid",
 * "F67B0768090D5D090455697C2D91ECCD"), Charset.forName("UTF-8")));
 * 
 * form.addPart("oauth_version", new StringBody("2.a",
 * Charset.forName("UTF-8")));
 * 
 * form.addPart("scope", new StringBody( "all", Charset.forName("UTF-8")));
 * 
 * // ========私有参数
 * 
 * // format 是返回数据的格式（json或xml）
 * 
 * // content 是 微博内容（若在此处@好友，需正确填写好友的微博账号，而非昵称），不超过140字
 * 
 * // clientip 是 用户ip（必须正确填写用户侧真实ip，不能为内网ip及以127或255开头的ip，以分析用户所在地）
 * 
 * // longitude 否 经度，为实数，如113.421234（最多支持10位有效数字，可以填空）
 * 
 * // latitude 否 纬度，为实数，如22.354231（最多支持10位有效数字，可以填空）
 * 
 * // pic 是 文件域表单名。本字段不要放在签名的参数中，不然请求时会出现签名错误，图片大小限制在4M。
 * 
 * // syncflag 否 微博同步到空间分享标记（可选，0-同步，1-不同步，默认为0），目前仅支持OAuth1.0鉴权方式
 * 
 * // compatibleflag 否容错标志，支持按位操作，默认为0
 * 
 * form.addPart("format", new StringBody("json", Charset.forName("UTF-8")));
 * 
 * form.addPart("content", new StringBody("android_test_weibo",
 * Charset.forName("UTF-8")));
 * 
 * form.addPart("clientip", new StringBody("192.168.0.1",
 * Charset.forName("UTF-8")));
 * 
 * form.addPart("longitude", new StringBody( "101.23",
 * Charset.forName("UTF-8")));
 * 
 * form.addPart("latitude", new StringBody( "60.0", Charset.forName("UTF-8")));
 * 
 * //文件
 * 
 * //form.addPart("formfile",new FileBody(new
 * File("data/data/"+getPackageName()+"/2013-02-12_01.png")));
 * 
 * form.addPart("pic",new FileBody(new
 * File(Environment.getExternalStorageDirectory()+"/1picture/01.png")));
 */
