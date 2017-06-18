package app.btz.common.ajax;

import java.io.Serializable;

/**
 * Created by User on 2017/6/17.
 */
public class AppAjax implements Serializable {

    /**
     * 成功
     */
    public final static Integer SUCCESS = 1;

    /**
     *错误
     */
    public final static Integer FAIL = 0;

    private Integer returnCode = SUCCESS;

    private String msg = "返回成功";

    private Object content;

    public Integer getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
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
