package com.btz.feedback.service.impl;

import com.btz.feedback.service.FeedbackService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/7/25.
 */
@Service("feedbackService")
@Transactional
public class FeedbackServiceImpl extends BaseServiceImpl implements FeedbackService {
}
