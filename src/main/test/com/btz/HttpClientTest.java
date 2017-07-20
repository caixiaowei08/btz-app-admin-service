package com.btz;

import api.btz.function.user.vo.ApiUserVo;
import api.btz.function.user.vo.CourseAuthorityVo;
import com.alibaba.fastjson.JSON;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.framework.core.utils.DateUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by User on 2017/7/20.
 */
public class HttpClientTest {
    public static void main(String[] args) {
        String returnValue = "这是默认返回值，接口调用失败";
        String url = "http://localhost:8080/api/userController.do?doAddOrModifyCourseAuthorityByUserId&token=17cd3b8d8fd04c3aa236bed310c05faf";
        ApiUserVo apiUserVo = new ApiUserVo();
        apiUserVo.setUserId("caixiaowei08");
       // apiUserVo.setUserName("蔡晓伟");
        //apiUserVo.setUserPwd("123456");
        //apiUserVo.setPhone("13162302663");
       // apiUserVo.setArea("上海");
        List<CourseAuthorityVo> courseAuthorityVoList = new ArrayList<CourseAuthorityVo>();
        for (int i = 10001; i < 10004; i++) {
            CourseAuthorityVo courseAuthorityVo = new CourseAuthorityVo();
            courseAuthorityVo.setSubCourseId(i);
            courseAuthorityVo.setStartTime(DateUtils.addDay(new Date(),10));
            courseAuthorityVo.setEndTime(DateUtils.addDay(new Date(),20));
            courseAuthorityVoList.add(courseAuthorityVo);
        }
        apiUserVo.setAuthority(courseAuthorityVoList);
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            String json = JSON.toJSONString(apiUserVo);
            System.out.println(json);
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            //httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，
            System.out.println(returnValue);
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                httpClient.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}
