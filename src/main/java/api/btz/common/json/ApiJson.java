package api.btz.common.json;

import java.io.Serializable;

/**
 * Created by User on 2017/7/20.
 */
public class ApiJson implements Serializable {

    /**
     * 成功
     */
    public final static Integer SUCCESS = 1;

    /**
     *错误
     */
    public final static Integer FAIL = 0;


    public final static String MSG_SUCCESS = "成功！";

    public final static String MSG_FAIL = "失败！";

    private Integer success = SUCCESS;

    private String msg = MSG_SUCCESS;

    private Object content;

    public Integer getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }
}
