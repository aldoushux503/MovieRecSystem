package org.solvd.recommendation.dao.mybatis;


import org.solvd.recommendation.dao.IInteractionDAO;
import org.solvd.recommendation.mapper.IInteractionMapper;
import org.solvd.recommendation.model.Interaction;

public class InteractionMyBatisDAO extends AbstractMyBatisDAO<Interaction, Long, IInteractionMapper> implements IInteractionDAO {

    public InteractionMyBatisDAO() {
        super(Interaction.class);
    }

    @Override
    protected Class<IInteractionMapper> getMapperClass() {
        return IInteractionMapper.class;
    }

    @Override
    protected Long getEntityId(Interaction entity) {
        return entity.getInteractionId();
    }
}