package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.solvd.recommendation.model.User;

@Mapper
public interface IUserMapper extends IMapper<User> {
}
