package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品业务层
 */
@Service
@Slf4j
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

    /**
     * 保存菜品和对应的口味数据
     * @param dishDTO
     */
    @Transactional   //保持事务一致性
    public void saveWithFlavor(DishDTO dishDTO){
        Dish dish = new Dish();
        BeanUtils.copyProperties(dishDTO,dish); //对象属性拷贝
        //向菜品表插入一条数据
        dishMapper.insert(dish);

        //获取insert语句生成的主键值，即菜品id
        Long dishId = dish.getId();
        //向口味表中插入n条数据
        List<DishFlavor> flavors = dishDTO.getFlavors();  //菜品信息中提取出口味信息 list内包括 dishId,name,value
        if(flavors != null && flavors.size() > 0 ){
            flavors.forEach(dishFlavor -> {
                dishFlavor.setDishId(dishId);   //遍历循环插入菜品id
            });
            //向口味表中插入n条数据 (batch 批量插入)
            dishFlavorMapper.insertBatch(flavors);

        }
    }
}
