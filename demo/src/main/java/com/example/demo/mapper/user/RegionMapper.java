package com.example.demo.mapper.user;

import com.example.demo.entity.user.Region;
import org.apache.ibatis.annotations.Mapper;

/**
  * @author mc
  * Create date 2019-03-05 15:20:29
  * Version 1.0
  * Description 持久层
  */
@Mapper
public interface RegionMapper extends tk.mybatis.mapper.common.Mapper<Region> {

    int insertSelectiveCustom(Region region);
}
