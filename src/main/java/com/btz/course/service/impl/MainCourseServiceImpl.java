package com.btz.course.service.impl;

import com.btz.course.service.MainCourseService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/4.
 */
@Service("mainCourseService")
@Transactional
public class MainCourseServiceImpl extends BaseServiceImpl implements MainCourseService{
}
