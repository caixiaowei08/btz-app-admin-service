package com.btz.newsBulletin.message.service.impl;

import com.btz.newsBulletin.message.service.MessageService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/3.
 */
@Service("messageService")
@Transactional
public class MessageServiceImpl extends BaseServiceImpl implements MessageService {
}
