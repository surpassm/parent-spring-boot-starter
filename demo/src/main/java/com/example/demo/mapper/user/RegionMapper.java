package com.example.demo.mapper.user;

import com.example.demo.entity.user.Region;

/**
  * @author mc
  * Create date 2019-03-14 20:41:03
  * Version 1.0
  * Description 区域持久层
  */
public interface RegionMapper extends tk.mybatis.mapper.common.Mapper<Region> {

    int insertSelectiveCustom(Region region);
}
