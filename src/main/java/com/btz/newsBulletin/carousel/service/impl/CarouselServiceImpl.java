package com.btz.newsBulletin.carousel.service.impl;

import com.btz.newsBulletin.carousel.service.CarouselService;
import org.framework.core.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by User on 2017/6/2.
 */
@Service("carouselService")
@Transactional
public class CarouselServiceImpl extends BaseServiceImpl implements CarouselService {
}
