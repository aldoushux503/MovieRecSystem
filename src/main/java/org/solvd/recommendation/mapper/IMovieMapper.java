package org.solvd.recommendation.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.solvd.recommendation.model.Movie;

@Mapper
public interface IMovieMapper extends IMapper<Movie> {
}
