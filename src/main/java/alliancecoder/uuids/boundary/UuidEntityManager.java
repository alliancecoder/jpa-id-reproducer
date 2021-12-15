package alliancecoder.uuids.boundary;

import java.util.List;
import java.util.UUID;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.transaction.Transactional;

import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import alliancecoder.globals.OperationResult;
import alliancecoder.uuids.entity.EntityUsingUuid;

@RequestScoped
public class UuidEntityManager {
    
    private static final Logger LOGGER = Logger.getLogger(UuidEntityManager.class.getName());

    @Inject
    EntityManager em;

    @Transactional
    public EntityUsingUuid save(EntityUsingUuid uuidEntity) {
        try {
            if (uuidEntity.getUuidAsId() == null) {
                uuidEntity.setUuidAsId(UUID.randomUUID());
                this.em.persist(uuidEntity);
                return uuidEntity;
            }
            return this.em.merge(uuidEntity);            
        } catch (Exception ex) {
            LOGGER.log(Level.DEBUG, ex.toString(), ex);
            return null;
        }
    }

    public EntityUsingUuid findById(UUID uuid) {
        try {
        return this.em.find(EntityUsingUuid.class, uuid);
        } catch (Exception ex) {
            LOGGER.log(Level.DEBUG, ex.toString(), ex);
            return null;
        }
    }

    public List<EntityUsingUuid> getAll() {
        try {
            return this.em.createNamedQuery(EntityUsingUuid.getAll, EntityUsingUuid.class).getResultList();
        } catch (Exception e) {
            LOGGER.log(Level.DEBUG, e.toString(), e);
            return null;
        }
    }

	@Transactional
	public OperationResult delete(UUID uuid) {
		try {
			EntityUsingUuid reference = this.em.getReference(EntityUsingUuid.class, uuid);
			this.em.remove(reference);
			return OperationResult.SUCCESS;
		} catch (PersistenceException pEx) {
			LOGGER.log(Level.DEBUG, pEx.toString(), pEx);
			return OperationResult.FAILURE;
		}
	}
}