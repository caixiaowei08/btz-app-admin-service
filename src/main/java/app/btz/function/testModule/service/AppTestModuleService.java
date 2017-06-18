package app.btz.function.testModule.service;

import app.btz.function.testModule.vo.ExerciseVo;
import app.btz.function.testModule.vo.ListInfoVo;
import com.btz.module.entity.ModuleEntity;

import java.util.List;

/**
 * Created by User on 2017/6/18.
 */
public interface AppTestModuleService {

    public List<ListInfoVo> getListInfoVoByModuleEntity(ModuleEntity moduleEntity);

    public List<ExerciseVo>  getExerciseVoListByListInfoVo(List<ListInfoVo> listInfoVoList);


}
