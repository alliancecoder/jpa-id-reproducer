package alliancecoder.uuids.entity;

import java.io.Serializable;
import java.util.UUID;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import alliancecoder.validations.ValidEntity;

@Entity
@Table(name = "entities_using_uuid", indexes = {
    @Index(name = "uk_unique_other_by_uuid", columnList = "other_unique_item", unique = true)
})
@NamedQueries({
    @NamedQuery(name = EntityUsingUuid.getAll, query = "SELECT u FROM EntityUsingUuid u")
})
public class EntityUsingUuid implements Serializable, ValidEntity {

	private static final long serialVersionUID = 1L;

    private static final String PREFIX = "uuids.entity.";
    public static final String getAll = PREFIX + "getAll";

    // Performance is better with binary - but has issues
    // how do we manually insert data
    // presentation should utilize "friendly output"
	@Column(name = "uuid_as_id")
	@Id
	//@Type(type = "uuid-char")
	protected UUID uuidAsId;

    @Column(name = "other_unique_item")
    private Long otherUniqueItem;

    @Column(name = "non_unique_text", nullable = false)
    private String nonUniqueText;

    public EntityUsingUuid() {
        this.uuidAsId = UUID.randomUUID();
    }

    public EntityUsingUuid(Long otherUniqueItem, String nonUniqueText) {
        this.uuidAsId = UUID.randomUUID();
        this.otherUniqueItem = otherUniqueItem;
        this.nonUniqueText = nonUniqueText;
    }

    public UUID getUuidAsId() {
        return uuidAsId;
    }

    public void setUuidAsId(UUID uuidAsId) {
        this.uuidAsId = uuidAsId;
    }

    public Long getOtherUniqueItem() {
        return otherUniqueItem;
    }

    public void setOtherUniqueItem(Long otherUniqueItem) {
        this.otherUniqueItem = otherUniqueItem;
    }

    public String getNonUniqueText() {
        return nonUniqueText;
    }

    public void setNonUniqueText(String nonUniqueText) {
        this.nonUniqueText = nonUniqueText;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((uuidAsId == null) ? 0 : uuidAsId.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EntityUsingUuid other = (EntityUsingUuid) obj;
        if (uuidAsId == null) {
            if (other.uuidAsId != null)
                return false;
        } else if (!uuidAsId.equals(other.uuidAsId))
            return false;
        return true;
    }

    @Override
    @Transient
    @JsonbTransient
    public boolean isValid() {
		// Implement business rules for entity validation
        // otherUniqueItem must be GT Zero
        if (this.otherUniqueItem <= 0L) return false;
        // nonUniqueText is required
        if (this.nonUniqueText == null || this.nonUniqueText.isEmpty() || this.nonUniqueText.isBlank()) return false;
        return true;
    }
    
}