package app.btz.function.testModule.vo;

import java.io.Serializable;
import java.util.List;

/**
 * Created by User on 2017/6/17.
 */
public class ModuleVo<T extends Serializable,L extends Serializable> implements Serializable{

    /**
     * 模型版本号 提供题目上传校验
     */
    private Integer version;

    /**
     *题目内容列表
     */
    private List<T> exam;

    /**
     * 层级列表
     */
    private List<L> list;


    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public List<T> getExam() {
        return exam;
    }

    public void setExam(List<T> exam) {
        this.exam = exam;
    }

    public List<L> getList() {
        return list;
    }

    public void setList(List<L> list) {
        this.list = list;
    }
}
