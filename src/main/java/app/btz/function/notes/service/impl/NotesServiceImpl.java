package app.btz.function.notes.service.impl;

import app.btz.function.notes.service.NotesService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/7/22.
 */
@Service("notesService")
@Transactional
public class NotesServiceImpl extends BaseServiceImpl implements NotesService {
}
