package com.example.ssk.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.ssk.entity.List;
import com.example.ssk.mapper.ListMapper;
import com.example.ssk.service.IListService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author baomidou
 * @since 2023-03-07
 */
@Service
public class ListServiceImpl extends ServiceImpl<ListMapper, List> implements IListService {
}
