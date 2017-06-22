package com.btz.exercise.utils;

import com.btz.exercise.entity.ExerciseEntity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.framework.core.common.system.BusinessException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/12.
 */
public class PoiExcelExerciseUtils {

    public static List<ExerciseEntity> readXls(File file) throws IOException, BusinessException {

        FileInputStream excelFile = new FileInputStream(file);
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook(excelFile);
        ExerciseEntity exerciseEntity = null;
        List<ExerciseEntity> exerciseEntityList = new ArrayList<ExerciseEntity>();
        for (int numSheet = 0; numSheet < hssfWorkbook.getNumberOfSheets(); numSheet++) {
            HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(numSheet);
            if (hssfSheet == null) {
                continue;
            }
            for (int rowNum = 1; rowNum <= hssfSheet.getLastRowNum(); rowNum++) {
                HSSFRow hssfRow = hssfSheet.getRow(rowNum);
                if (hssfRow != null) {
                    exerciseEntity = new ExerciseEntity();
                    HSSFCell subCourseId = hssfRow.getCell(0);
                    HSSFCell chapterId = hssfRow.getCell(2);
                    HSSFCell moduleType = hssfRow.getCell(4);
                    HSSFCell type = hssfRow.getCell(6);
                    HSSFCell title = hssfRow.getCell(8);
                    HSSFCell content = hssfRow.getCell(9);
                    HSSFCell answer = hssfRow.getCell(10);
                    HSSFCell answerKey = hssfRow.getCell(11);
                    HSSFCell orderNo = hssfRow.getCell(12);
                    try {
                        exerciseEntity.setSubCourseId(Integer.parseInt(subCourseId.toString()));
                        exerciseEntity.setChapterId(Integer.parseInt(chapterId.toString()));
                        exerciseEntity.setType(Integer.parseInt(type.toString()));
                        exerciseEntity.setTitle(title == null ? "":title.toString());
                        exerciseEntity.setModuleType(Integer.parseInt(moduleType.toString()));
                        exerciseEntity.setContent(content == null ? "" : content.toString());
                        exerciseEntity.setAnswer(answer == null ? "" : answer.toString());
                        exerciseEntity.setAnswerKey(answerKey == null ? "" : answerKey.toString());
                        exerciseEntity.setOrderNo(Integer.parseInt(orderNo.toString()));
                        exerciseEntityList.add(exerciseEntity);
                    } catch (Exception e) {
                        throw new BusinessException("数据解析错误,第" + rowNum + "行,请检查！");
                    }
                }
            }
        }
        return exerciseEntityList;
    }

}
