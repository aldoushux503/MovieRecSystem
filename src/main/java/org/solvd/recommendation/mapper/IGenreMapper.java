package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.solvd.recommendation.model.Genre;

@Mapper
public interface IGenreMapper extends IMapper<Genre> {
}
