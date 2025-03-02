package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.solvd.recommendation.model.Person;

@Mapper
public interface IPersonMapper extends IMapper<Person> {
}
