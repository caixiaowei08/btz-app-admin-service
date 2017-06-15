package com.btz.exercise.utils;

import com.btz.exercise.entity.ExerciseEntity;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by User on 2017/6/12.
 */
public class PoiExcelExerciseUtils {

    public static List<ExerciseEntity> readXls(File file) throws IOException {

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
                    HSSFCell title = hssfRow.getCell(0);
                    HSSFCell content = hssfRow.getCell(1);
                    HSSFCell answer = hssfRow.getCell(2);
                    HSSFCell answerKey = hssfRow.getCell(3);
                    HSSFCell orderNo = hssfRow.getCell(4);
                    exerciseEntity.setTitle(title.toString());
                    exerciseEntity.setContent(content.toString());
                    exerciseEntity.setAnswer(answer.toString());
                    exerciseEntity.setAnswerKey(answerKey.toString());
                    exerciseEntity.setOrderNo(Integer.parseInt(orderNo.toString()));
                    exerciseEntityList.add(exerciseEntity);
                }
            }
        }
        return exerciseEntityList;
    }

}
