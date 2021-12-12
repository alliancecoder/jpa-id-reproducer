package alliancecoder.sequences.boundary;

import java.util.List;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import alliancecoder.globals.OperationResult;
import alliancecoder.sequences.entity.EntityUsingLong;

@RequestScoped
public class LongEntityManager {
    
    private static final Logger LOGGER = Logger.getLogger(LongEntityManager.class.getName());

    @Inject
    EntityManager em;

    @Transactional
    public EntityUsingLong save(EntityUsingLong longEntity) {
        return this.em.merge(longEntity);
    }

    public EntityUsingLong findById(Long longAsId) {
        return this.em.find(EntityUsingLong.class, longAsId);
    }

    public List<EntityUsingLong> getAll() {
        try {
            return this.em.createNamedQuery(EntityUsingLong.getAll, EntityUsingLong.class).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.DEBUG, e.toString(), e);
            return null;
        }
    }

	@Transactional
	public OperationResult delete(Long longAsId) {
		try {
			EntityUsingLong reference = this.em.getReference(EntityUsingLong.class, longAsId);
			this.em.remove(this.em.merge(reference));
			return OperationResult.SUCCESS;
		} catch (PersistenceException pEx) {
			LOGGER.log(Level.DEBUG, pEx.toString(), pEx);
			return OperationResult.FAILURE;
		}
	}
}