package app.btz.function.testModule.vo;

import java.io.Serializable;

/**
 * Created by User on 2017/6/18.
 */
public class ModuleTestRequestVo implements Serializable{

    /**
     * 课程id
     */
    private Integer subCourseId;

    /**
     * 模块类型
     */
    private Integer moduleType;

    public Integer getSubCourseId() {
        return subCourseId;
    }

    public void setSubCourseId(Integer subCourseId) {
        this.subCourseId = subCourseId;
    }

    public Integer getModuleType() {
        return moduleType;
    }

    public void setModuleType(Integer moduleType) {
        this.moduleType = moduleType;
    }
}
