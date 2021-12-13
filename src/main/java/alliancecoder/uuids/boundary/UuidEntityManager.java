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

    // @Transactional
    // public OperationResult buildSampleData() {
    //     try {
    //         EntityUsingUuid iti;
    //         iti = new EntityUsingUuid(1L, "FIRST RECORD");
    //         this.save(iti);
    //         iti = new EntityUsingUuid(2L, "SECOND RECORD");
    //         this.save(iti);
    //         iti = new EntityUsingUuid(3L, "THIRD RECORD");
    //         this.save(iti);
    //         iti = new EntityUsingUuid(4L, "FOURTH RECORD");
    //         this.save(iti);
    //         return OperationResult.SUCCESS;
    //     } catch (Exception ex) {
    //         LOGGER.log(Level.DEBUG, ex.toString(), ex);
    //         return OperationResult.FAILURE;
    //     }
    // }

    public EntityUsingUuid findById(String uuid) {
        try {
        UUID id = UUID.fromString(uuid);
        return this.em.find(EntityUsingUuid.class, id);
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
	public OperationResult delete(String uuid) {
        UUID id = UUID.fromString(uuid);
		try {
			EntityUsingUuid reference = this.em.getReference(EntityUsingUuid.class, id);
			this.em.remove(reference);
			return OperationResult.SUCCESS;
		} catch (PersistenceException pEx) {
			LOGGER.log(Level.DEBUG, pEx.toString(), pEx);
			return OperationResult.FAILURE;
		}
	}
}