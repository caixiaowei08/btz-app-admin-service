package com.btz.video.recorded.service.impl;

import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.exercise.entity.ExerciseEntity;
import com.btz.exercise.vo.ChapterVo;
import com.btz.exercise.vo.SubCourseVo;
import com.btz.module.entity.ModuleEntity;
import com.btz.poi.pojo.ExerciseExcelPojo;
import com.btz.utils.BelongToEnum;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
import com.btz.video.recorded.pojo.ChapterPojo;
import com.btz.video.recorded.pojo.ClassPojo;
import com.btz.video.recorded.pojo.CoursePojo;
import com.btz.video.recorded.pojo.YearPojo;
import com.btz.video.recorded.service.CourseRecordedVideoService;
import com.btz.video.recorded.vo.RecordedVideoPojo;
import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.framework.core.common.system.BusinessException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/7/14.
 */
@Service("courseRecordedVideoService")
@Transactional
public class CourseRecordedVideoServiceImpl extends BaseServiceImpl implements CourseRecordedVideoService {

    @Autowired
    private ChapterService chapterService;

    public CoursePojo getExcelTemplet(SubCourseEntity subCourseEntity) {
        DetachedCriteria chapterEntityDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterEntityDetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
        chapterEntityDetachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.RECORDED_VIDEO.getIndex()));
        chapterEntityDetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterEntityDetachedCriteria);
        //课程信息

        CoursePojo coursePojo = new CoursePojo();
        coursePojo.setId(subCourseEntity.getId());
        coursePojo.setName(subCourseEntity.getSubName());
        //处理第一级 A
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterA : chapterEntityList) {
                if (chapterA.getLevel().equals(ConstantChapterLevel.ONE)) {
                    YearPojo yearPojo = new YearPojo();
                    yearPojo.setId(chapterA.getId());
                    yearPojo.setName(chapterA.getChapterName());
                    coursePojo.getYears().add(yearPojo);
                    for (ChapterEntity chapterB : chapterEntityList) {
                        if (chapterB.getLevel().equals(ConstantChapterLevel.TWO)
                                && chapterB.getFid().equals(yearPojo.getId())) {
                            ClassPojo classPojo = new ClassPojo();
                            classPojo.setId(chapterB.getId());
                            classPojo.setName(chapterB.getChapterName());
                            yearPojo.getClasses().add(classPojo);
                            for (ChapterEntity chapterC : chapterEntityList) {
                                if (chapterC.getLevel().equals(ConstantChapterLevel.THREE)
                                        && chapterC.getFid().equals(classPojo.getId())) {
                                    ChapterPojo chapterPojo = new ChapterPojo();
                                    chapterPojo.setId(chapterC.getId());
                                    chapterPojo.setName(chapterC.getChapterName());
                                    classPojo.getChapters().add(chapterPojo);
                                }
                            }
                        }
                    }
                }
            }
        }
        return coursePojo;
    }

    public List<CourseRecordedVideoEntity> readXlsxToCourseRecordedVideoEntity(File file) throws IOException, BusinessException {
        FileInputStream excelXlsxFile = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelXlsxFile);
        List<CourseRecordedVideoEntity> courseRecordedVideoEntityList = new ArrayList<CourseRecordedVideoEntity>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            ChapterEntity chapterEntityClass = null;
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    XSSFCell chapterName = xssfRow.getCell(0);
                    XSSFCell title = xssfRow.getCell(1);
                    XSSFCell videoUrl = xssfRow.getCell(2);
                    XSSFCell lectureUrl = xssfRow.getCell(3);
                    XSSFCell orderNo = xssfRow.getCell(4);
                    XSSFCell classId = xssfRow.getCell(7);

                    if (
                            (chapterName == null || StringUtils.isEmpty(chapterName.getStringCellValue().trim())) &&
                                    (title == null || StringUtils.isEmpty(title.getStringCellValue().trim())) &&
                                    (videoUrl == null || StringUtils.isEmpty(videoUrl.getStringCellValue().trim())) &&
                                    (lectureUrl == null || StringUtils.isEmpty(lectureUrl.getStringCellValue().trim())) &&
                                    (orderNo == null)
                            ) {
                        continue;
                    }

                    if (classId != null) {
                        try {
                            Integer classIdValue = new Double(classId.getNumericCellValue()).intValue();
                            chapterEntityClass = chapterService.get(ChapterEntity.class, classIdValue);
                            if (chapterEntityClass == null) {
                                throw new BusinessException("第" + rowNum + "班次不存在，请核实！");
                            }
                            continue;
                        } catch (Exception e) {
                            throw new BusinessException("第" + rowNum + "班次编号有误，请核实！");
                        }
                    }

                    ChapterEntity chapterEntity = null;
                    if (chapterName != null) {

                        try {
                            String chapterNameValue = chapterName.getStringCellValue().trim();
                            if (StringUtils.isEmpty(chapterNameValue)) {
                                throw new BusinessException("第" + rowNum + "班次编号有误，请核实！");
                            }
                            DetachedCriteria chapterEntityDetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
                            chapterEntityDetachedCriteria.add(Restrictions.eq("courseId", chapterEntityClass.getCourseId()));
                            chapterEntityDetachedCriteria.add(Restrictions.eq("fid", chapterEntityClass.getId()));
                            chapterEntityDetachedCriteria.add(Restrictions.eq("chapterName", chapterNameValue));
                            chapterEntityDetachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.RECORDED_VIDEO.getIndex()));
                            List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterEntityDetachedCriteria);
                            if (CollectionUtils.isEmpty(chapterEntityList)) {
                                throw new BusinessException("第" + rowNum + "未查询到该班次下，该章节的信息，请核实！");
                            }

                            if (chapterEntityList.size() > 1) {
                                throw new BusinessException("第" + rowNum + "该班次下，有同名的章节，请核实！");
                            }

                            chapterEntity = chapterEntityList.get(0);
                        } catch (Exception e) {
                            throw new BusinessException("第" + rowNum + "章节名称有误，请核实！");
                        }
                    }

                    if (chapterEntity == null) {
                        throw new BusinessException("第" + rowNum + "未能获取章节信息，请核实！");
                    }

                    CourseRecordedVideoEntity courseRecordedVideoEntity = new CourseRecordedVideoEntity();
                    courseRecordedVideoEntity.setSubCourseId(chapterEntity.getCourseId());
                    courseRecordedVideoEntity.setChapterId(chapterEntity.getId());
                    courseRecordedVideoEntity.setModuleId(chapterEntity.getModuleId());
                    courseRecordedVideoEntity.setModuleType(chapterEntity.getModuleType());

                    if (title == null) {
                        throw new BusinessException("第" + rowNum + "行视频名称为空，请核实！");
                    }
                    try {
                        String titleValue = title.getStringCellValue();
                        if (StringUtils.isEmpty(titleValue)) {
                            throw new BusinessException("第" + rowNum + "行视频名称为空，请核实！");
                        }
                        courseRecordedVideoEntity.setTitle(titleValue);
                    } catch (Exception e) {
                        throw new BusinessException("第" + rowNum + "行视频名称格式有误，请核实！");
                    }

                    if (videoUrl == null) {
                        throw new BusinessException("第" + rowNum + "行视频播放链接为空，请核实！");
                    }
                    try {
                        String videoUrlValue = videoUrl.getStringCellValue();
                        if (StringUtils.isEmpty(videoUrlValue)) {
                            throw new BusinessException("第" + rowNum + "行频播放链接为空，请核实！");
                        }
                        courseRecordedVideoEntity.setVideoUrl(videoUrlValue);
                    } catch (Exception e) {
                        throw new BusinessException("第" + rowNum + "行频播放链接有误，请核实！");
                    }

                    if (lectureUrl == null) {
                        throw new BusinessException("第" + rowNum + "行视频讲义链接为空，请核实！");
                    }
                    try {
                        String lectureUrlValue = videoUrl.getStringCellValue();
                        if (StringUtils.isEmpty(lectureUrlValue)) {
                            throw new BusinessException("第" + rowNum + "行视频讲义链接为空，请核实！");
                        }
                        courseRecordedVideoEntity.setLectureUrl(lectureUrlValue);
                    } catch (Exception e) {
                        throw new BusinessException("第" + rowNum + "行视频讲义链接有误，请核实！");
                    }

                    if (orderNo == null) {
                        throw new BusinessException("第" + rowNum + "行显示顺序错误，请核实！");
                    }
                    try {
                        Integer orderNoValue = new Double(orderNo.getNumericCellValue()).intValue();
                        courseRecordedVideoEntity.setOrderNo(orderNoValue);
                    } catch (Exception e) {
                        throw new BusinessException("第" + rowNum + "行显示顺序错误，请核实！");
                    }
                    courseRecordedVideoEntityList.add(courseRecordedVideoEntity);
                }
            }
        }
        return courseRecordedVideoEntityList;
    }
}
