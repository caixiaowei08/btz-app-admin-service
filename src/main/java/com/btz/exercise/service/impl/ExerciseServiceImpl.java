package com.btz.exercise.service.impl;

import com.btz.exercise.service.ExerciseService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/12.
 */
@Service("exerciseService")
@Transactional
public class ExerciseServiceImpl extends BaseServiceImpl implements ExerciseService {
}
