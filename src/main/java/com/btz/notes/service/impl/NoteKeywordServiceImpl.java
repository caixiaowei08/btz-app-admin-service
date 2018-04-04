package com.btz.notes.service.impl;

import com.btz.notes.service.NoteKeywordService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/9/1.
 */
@Service("noteKeywordService")
@Transactional
public class NoteKeywordServiceImpl extends BaseServiceImpl implements NoteKeywordService{
}
