package com.btz.video.live.service.impl;

import com.btz.course.ConstantChapterLevel;
import com.btz.course.entity.ChapterEntity;
import com.btz.course.entity.SubCourseEntity;
import com.btz.course.service.ChapterService;
import com.btz.exercise.vo.SubCourseVo;
import com.btz.utils.BelongToEnum;
import com.btz.video.live.entity.CourseLiveVideoEntity;
import com.btz.video.live.service.CourseLiveVideoService;
import com.btz.video.live.vo.LiveVideoPojo;
import com.btz.video.recorded.entity.CourseRecordedVideoEntity;
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
@Service("courseLiveVideoService")
@Transactional
public class CourseLiveVideoServiceImpl extends BaseServiceImpl implements CourseLiveVideoService {

    @Autowired
    private ChapterService chapterService;

    public List<LiveVideoPojo> getExcelTemplet(SubCourseEntity subCourseEntity) {
        SubCourseVo subCourseVo = new SubCourseVo();
        subCourseVo.setSubCourseId(subCourseEntity.getId());
        subCourseVo.setSubName(subCourseEntity.getSubName());
        DetachedCriteria chapterEntityADetachedCriteria = DetachedCriteria.forClass(ChapterEntity.class);
        chapterEntityADetachedCriteria.add(Restrictions.eq("courseId", subCourseEntity.getId()));
        chapterEntityADetachedCriteria.add(Restrictions.eq("level", ConstantChapterLevel.ONE));
        chapterEntityADetachedCriteria.add(Restrictions.eq("moduleType", BelongToEnum.LIVE_VIDEO.getIndex()));
        chapterEntityADetachedCriteria.addOrder(Order.asc("orderNo"));
        List<ChapterEntity> chapterEntityList = chapterService.getListByCriteriaQuery(chapterEntityADetachedCriteria);
        List<LiveVideoPojo> liveVideoPojoList = new ArrayList<LiveVideoPojo>();
        if (CollectionUtils.isNotEmpty(chapterEntityList)) {
            for (ChapterEntity chapterEntity : chapterEntityList) {
                LiveVideoPojo liveVideoPojo = new LiveVideoPojo();
                liveVideoPojo.setSubCourseId(chapterEntity.getCourseId());
                liveVideoPojo.setSubCourseName(subCourseEntity.getSubName());
                liveVideoPojo.setChapterId(chapterEntity.getId());
                liveVideoPojo.setChapterName(chapterEntity.getChapterName());
                liveVideoPojo.setModuleId(chapterEntity.getModuleId());
                liveVideoPojo.setModuleType(chapterEntity.getModuleType());
                liveVideoPojoList.add(liveVideoPojo);
            }
        }
        return liveVideoPojoList;
    }

    public List<CourseLiveVideoEntity> readXlsxToCourseLiveVideoEntity(File file) throws IOException, BusinessException {
        FileInputStream excelXlsxFile = new FileInputStream(file);
        XSSFWorkbook xssfWorkbook = new XSSFWorkbook(excelXlsxFile);
        CourseLiveVideoEntity courseLiveVideoEntity = null;
        List<CourseLiveVideoEntity> courseLiveVideoEntityList = new ArrayList<CourseLiveVideoEntity>();
        for (int numSheet = 0; numSheet < xssfWorkbook.getNumberOfSheets(); numSheet++) {
            XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(numSheet);
            if (xssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= xssfSheet.getLastRowNum(); rowNum++) {
                XSSFRow xssfRow = xssfSheet.getRow(rowNum);
                if (xssfRow != null) {
                    XSSFCell subCourseId = xssfRow.getCell(0);
                    XSSFCell chapterId = xssfRow.getCell(2);
                    XSSFCell moduleType = xssfRow.getCell(4);
                    XSSFCell title = xssfRow.getCell(6);
                    XSSFCell videoUrl = xssfRow.getCell(7);
                    XSSFCell lectureUrl = xssfRow.getCell(8);
                    XSSFCell orderNo = xssfRow.getCell(9);
                    courseLiveVideoEntity = new CourseLiveVideoEntity();
                    if (subCourseId == null) {
                        throw new BusinessException("第" + rowNum + "行课程ID错误，请核实！");
                    }
                    try {
                        Integer subCourseIdValue  = new Double(subCourseId.getNumericCellValue()).intValue();
                        courseLiveVideoEntity.setSubCourseId(subCourseIdValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行课程ID错误，请核实！");
                    }
                    if (chapterId == null) {
                        throw new BusinessException("第" + rowNum + "行章节ID错误，请核实！");
                    }
                    try {
                        Integer chapterIdValue  = new Double(chapterId.getNumericCellValue()).intValue();
                        courseLiveVideoEntity.setChapterId(chapterIdValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行章节ID错误，请核实！");
                    }
                    //moduleType
                    if (moduleType == null) {
                        throw new BusinessException("第" + rowNum + "行模块类型ID错误，请核实！");
                    }
                    try {
                        Integer moduleTypeValue  = new Double(moduleType.getNumericCellValue()).intValue();
                        courseLiveVideoEntity.setModuleType(moduleTypeValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行模块类型ID错误，请核实！");
                    }
                    if(StringUtils.hasText(title.getStringCellValue())){
                        courseLiveVideoEntity.setTitle(title.getStringCellValue());
                    }else{
                        throw new BusinessException("第" + rowNum + "行视频标题不能为空，请核实！");
                    }
                    if(StringUtils.hasText(videoUrl.getStringCellValue())){
                        courseLiveVideoEntity.setVideoUrl(videoUrl.getStringCellValue());
                    }else{
                        throw new BusinessException("第" + rowNum + "行视频链接地址不能为空，请核实！");
                    }
                    if (orderNo == null) {
                        throw new BusinessException("第" + rowNum + "行显示顺序错误，请核实！");
                    }
                    try {
                        Integer orderNoValue  = new Double(orderNo.getNumericCellValue()).intValue();
                        courseLiveVideoEntity.setOrderNo(orderNoValue);
                    }catch (Exception e){
                        throw new BusinessException("第" + rowNum + "行显示顺序错误，请核实！");
                    }
                    courseLiveVideoEntityList.add(courseLiveVideoEntity);
                }
            }
        }
        return courseLiveVideoEntityList;
    }
}
