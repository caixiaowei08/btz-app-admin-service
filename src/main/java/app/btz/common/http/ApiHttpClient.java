package app.btz.common.http;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by User on 2017/8/22.
 */
public class ApiHttpClient {

    public static String doGet(String URL) {
        String result = null;
        HttpGet httpRequst = new HttpGet(URL);
        try {
            HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequst);
            if (httpResponse.getStatusLine().getStatusCode() == 200) {
                HttpEntity httpEntity = httpResponse.getEntity();
                result = EntityUtils.toString(httpEntity);
                result.replaceAll("\r", "");
            } else {
                httpRequst.abort();
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            result = e.getMessage().toString();
        } catch (IOException e) {
            e.printStackTrace();
            result = e.getMessage().toString();
        }
        return result;
    }

    public static String doPost(String URL,List<BasicNameValuePair> parms) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        String returnValue = null;
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(URL);
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setEntity(new UrlEncodedFormEntity(parms));
            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return returnValue;
    }

}
