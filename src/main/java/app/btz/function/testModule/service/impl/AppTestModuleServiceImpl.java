package app.btz.function.testModule.service.impl;

import app.btz.common.ajax.AppAjax;
import app.btz.function.testModule.service.AppTestModuleService;
import app.btz.function.testModule.vo.ExerciseVo;
import app.btz.function.testModule.vo.ListInfoVo;
import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.service.ChapterService;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.service.ExerciseService;
import com.btz.module.entity.ModuleEntity;
import org.apache.commons.collections.CollectionUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/18.
 */
@Service("appTestModuleService")
@Transactional
public class AppTestModuleServiceImpl implements AppTestModuleService {

    @Autowired
    private ChapterService chapterService;

    @Autowired
    private ExerciseService exerciseService;


    public List<ExerciseVo> getExerciseVoListByListInfoVo(List<ListInfoVo> listInfoVoList) {
        List<ExerciseVo> exerciseVoList = new ArrayList<ExerciseVo>();
        if (CollectionUtils.isNotEmpty(listInfoVoList)) {
            for (ListInfoVo listInfoVoA : listInfoVoList) {
                listInfoVoA.setBeg(exerciseVoList.size());
                if (listInfoVoA.isLeaf()) {
                    findExerciseListByListInfoVo(exerciseVoList, listInfoVoA);
                } else {
                    List<ListInfoVo> listInfoVosB = listInfoVoA.getSub();
                    for (ListInfoVo listInfoVoB : listInfoVosB) {
                        listInfoVoB.setBeg(exerciseVoList.size());
                        if (listInfoVoB.isLeaf()) {
                            findExerciseListByListInfoVo(exerciseVoList, listInfoVoB);
                        } else {
                            List<ListInfoVo> listInfoVosC = listInfoVoB.getSub();
                            for (ListInfoVo listInfoVoC : listInfoVosC) {
                                listInfoVoC.setBeg(exerciseVoList.size());
                                findExerciseListByListInfoVo(exerciseVoList, listInfoVoC);
                                if (CollectionUtils.isNotEmpty(exerciseVoList)) {
                                    listInfoVoC.setAll(exerciseVoList.size() - listInfoVoC.getBeg());
                                }
                            }
                        }
                        if (CollectionUtils.isNotEmpty(exerciseVoList)) {
                            listInfoVoB.setAll(exerciseVoList.size() - listInfoVoB.getBeg());
                        }
                    }
                }
                if (CollectionUtils.isNotEmpty(exerciseVoList)) {
                    listInfoVoA.setAll(exerciseVoList.size() - listInfoVoA.getBeg());
                }

            }
        }
        return exerciseVoList;
    }

    private void findExerciseListByListInfoVo(List<ExerciseVo> exerciseVoList, ListInfoVo listInfoVo) {
        DetachedCriteria exerciseDetachedCriteria = DetachedCriteria.forClass(ExerciseEntity.class);
        exerciseDetachedCriteria.add(Restrictions.eq("chapterId", listInfoVo.getChapterId()));
        exerciseDetachedCriteria.addOrder(Order.asc("type"));
        exerciseDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ExerciseEntity> exerciseEntityListA = exerciseService.getListByCriteriaQuery(exerciseDetachedCriteria);
        if (CollectionUtils.isNotEmpty(exerciseEntityListA)) {
            for (ExerciseEntity exerciseEntity : exerciseEntityListA) {
                exerciseVoList.add(getExerciseVo(exerciseEntity));
            }
        }
    }

    private ExerciseVo getExerciseVo(ExerciseEntity exerciseEntity) {
        ExerciseVo exerciseVo = new ExerciseVo();
        exerciseVo.setId(exerciseEntity.getId());
        exerciseVo.setType(exerciseEntity.getType());
        exerciseVo.setTitle(exerciseEntity.getTitle());
        exerciseVo.setContent(exerciseEntity.getContent());
        exerciseVo.setAnswer(exerciseEntity.getAnswer());
        exerciseVo.setAnswerKey(exerciseEntity.getAnswerKey());
        return exerciseVo;
    }

    public List<ListInfoVo> getListInfoVoByModuleEntity(ModuleEntity moduleEntity) {
        DetachedCriteria chapterDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterDetachedCriteria.add(Restrictions.eq("courseId", moduleEntity.getSubCourseId()));
        chapterDetachedCriteria.add(Restrictions.eq("moduleId", moduleEntity.getId()));
        chapterDetachedCriteria.add(Restrictions.eq("moduleType", moduleEntity.getType()));
        chapterDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntities = chapterService.getListByCriteriaQuery(chapterDetachedCriteria);
        List<ListInfoVo> listInfoVoList = new ArrayList<ListInfoVo>();
        //层次整理
        if (CollectionUtils.isNotEmpty(chapterEntities)) {
            for (ChapterEntity chapterEntityOne : chapterEntities) {
                if (chapterEntityOne.getLevel().equals(ConstantChapterLevel.ONE)) {
                    ListInfoVo listInfoVoOne = new ListInfoVo();
                    listInfoVoOne.setChapterId(chapterEntityOne.getId());
                    listInfoVoOne.setTit(chapterEntityOne.getChapterName());
                    listInfoVoOne.setLevel(chapterEntityOne.getLevel());
                    listInfoVoList.add(listInfoVoOne);
                    List<ListInfoVo> listInfoVosTwo = new ArrayList<ListInfoVo>();
                    listInfoVoOne.setSub(listInfoVosTwo);
                    for (ChapterEntity chapterEntityTwo : chapterEntities) {
                        if (chapterEntityTwo.getLevel().equals(ConstantChapterLevel.TWO)
                                && chapterEntityTwo.getFid().equals(chapterEntityOne.getId())) {
                            ListInfoVo listInfoVoTwo = new ListInfoVo();
                            listInfoVoTwo.setChapterId(chapterEntityTwo.getId());
                            listInfoVoTwo.setTit(chapterEntityTwo.getChapterName());
                            listInfoVoTwo.setLevel(chapterEntityTwo.getLevel());
                            listInfoVosTwo.add(listInfoVoTwo);
                            List<ListInfoVo> listInfoVosThree = new ArrayList<ListInfoVo>();
                            listInfoVoTwo.setSub(listInfoVosThree);
                            for (ChapterEntity chapterEntityThree : chapterEntities) {
                                if (chapterEntityThree.getLevel().equals(ConstantChapterLevel.THREE)
                                        && chapterEntityThree.getFid().equals(chapterEntityTwo.getId())) {
                                    ListInfoVo listInfoVoThree = new ListInfoVo();
                                    listInfoVoThree.setChapterId(chapterEntityThree.getId());
                                    listInfoVoThree.setTit(chapterEntityThree.getChapterName());
                                    listInfoVoThree.setLevel(chapterEntityThree.getLevel());
                                    listInfoVosThree.add(listInfoVoThree);
                                }
                            }
                        }
                    }
                }
            }
        }
        return listInfoVoList;
    }
}
