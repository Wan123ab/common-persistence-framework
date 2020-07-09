package cn.wq.persistence.jpa_plus.controller;

import cn.wq.persistence.jpa_plus.model.BaseEntity;
import cn.wq.persistence.jpa_plus.model.User;
import cn.wq.persistence.jpa_plus.service.BaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.support.DomainClassConverter;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author 万强
 * @version 1.0
 * @date 2020/7/8 15:35
 * @desc controller基类
 */
public abstract class BaseController<T extends BaseEntity, S extends BaseService<T, Long>> {
    
    @Autowired
    protected S service;

    @Autowired
    protected HttpServletRequest request;

    @Autowired
    protected HttpServletResponse response;

    private final Logger log = LoggerFactory.getLogger(getClass());

    /**
     * 获取当前登录用户
     * @return
     */
    protected User getCurrentUser(){
        return null;
    }

    /**
     * 在方法中注入实体对象，直接通过id查询
     * @param t 要查询的实体对象
     * @return 实体对象
     * @see EnableSpringDataWebSupport
     * @see DomainClassConverter
     */
    @GetMapping("/{id}")
    protected T queryById(@PathVariable("id") T t){
        return t;
    }

    /**
     * 分页查询
     * @param pageable 分页参数
     * @return page
     * @see EnableSpringDataWebSupport
     * @see org.springframework.data.web.PageableHandlerMethodArgumentResolver
     */
    @GetMapping(path = "/page")
    protected Page<T> findAllByPage(Pageable pageable){
        return service.findAllByPage(pageable);
    }

    /**
     * 排序查询
     * @param sort 排序参数
     * @return HttpEntity
     * @see EnableSpringDataWebSupport
     * @see org.springframework.data.web.SortHandlerMethodArgumentResolver
     */
    @GetMapping(path = "/sort")
    protected HttpEntity<List<T>> findAllBySort(Sort sort){
        return new HttpEntity<>(service.findAllBySort(sort));
    }

}
 
