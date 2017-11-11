package com.btz;

import api.btz.function.user.json.ApiUserInfoJson;
import app.btz.common.constant.ApiURLConstant;
import app.btz.common.http.ApiHttpClient;
import app.btz.function.feedback.vo.FeedbackVo;
import app.btz.function.user.vo.AppUserEmailVo;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by User on 2017/7/20.
 */
public class HttpClientTest {
    public static void main(String[] args) {
        String returnValue = "这是默认返回值，接口调用失败";
        // String url = "http://114.55.110.110:8080/app/feedbackController.do?doAdd&token=f96474ae960b448194fc7c867b92b573";
        String url = ApiURLConstant.BTZ_UPDATE_PWD_EMIAL_CODE_URL;//"http://114.55.110.110:8080/app/feedbackController.do?doAdd&token=f96474ae960b448194fc7c867b92b573";
        //  FeedbackVo feedbackVo = new FeedbackVo();
//        feedbackVo.setEx erciseId(100005);
//        feedbackVo.setContent("问题很大！");
        /*NotesEntity notesEntity = new NotesEntity();
        notesEntity.setExerciseId(100005);
        notesEntity.setNotes("笔记测试笔记测试");*/
       /*ApiUserVo apiUserVo = new ApiUserVo();
        apiUserVo.setUserId("10058142921@qq.com");
        apiUserVo.setUserName("蔡晓伟");
        apiUserVo.setUserPwd("123456");
        apiUserVo.setState(1);
        apiUserVo.setPhone("13162302663");
        apiUserVo.setArea("上海");*/
       /* List<CourseAuthorityVo> courseAuthorityVoList = new ArrayList<CourseAuthorityVo>();
        for (int i = 10001; i < 10003; i++) {
            CourseAuthorityVo courseAuthorityVo = new CourseAuthorityVo();
            courseAuthorityVo.setSubCourseId(i);
            courseAuthorityVo.setStartTime(DateUtils.addDay(new Date(),10));
            courseAuthorityVo.setEndTime(DateUtils.addDay(new Date(),20));
            courseAuthorityVoList.add(courseAuthorityVo);
        }*/

        AppUserEmailVo appUserEmailVo = new AppUserEmailVo();
        appUserEmailVo.setCode("627615");
        appUserEmailVo.setUsername("1005814292");
        appUserEmailVo.setPassword("1q2w3e4r");
        appUserEmailVo.setToken(ApiURLConstant.BTZ_TOKEN);

        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            HttpPost httpPost = new HttpPost(url);
            String json = JSON.toJSONString(appUserEmailVo);
            System.out.println(json);
            StringEntity requestEntity = new StringEntity(json, "utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
            httpPost.setEntity(requestEntity);
            returnValue = httpClient.execute(httpPost, responseHandler); //调接口获取返回值时，
            System.out.println(returnValue);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void test01() {
        String url = "http://api.baitizhan.com/index.php?c=User&a=info&token=3ZI3Bt2CyMpimPzC&username=chuji";
        String result = ApiHttpClient.doGet(url);
        System.out.println("result:" + result);
        ApiUserInfoJson apiUserInfoJson = JSON.parseObject(result, ApiUserInfoJson.class);
    }
}
