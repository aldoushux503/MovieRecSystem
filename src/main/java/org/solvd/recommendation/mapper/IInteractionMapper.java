package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.solvd.recommendation.model.Interaction;

@Mapper
public interface IInteractionMapper extends IMapper<Interaction> {
}
