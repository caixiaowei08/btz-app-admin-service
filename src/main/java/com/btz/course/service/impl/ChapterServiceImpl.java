package com.btz.course.service.impl;

import com.btz.course.service.ChapterService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/8.
 */
@Service("chapterService")
@Transactional
public class ChapterServiceImpl extends BaseServiceImpl implements ChapterService {
}
