package alliancecoder.sequences.entity;

import java.beans.Transient;
import java.io.Serializable;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import alliancecoder.validations.ValidEntity;

@Entity
@Table(name = "entities_using_long", indexes = {
    @Index(name = "uk_unique_other_by_long", columnList = "other_unique_item", unique = true)
})
@NamedQueries({
    @NamedQuery(name = EntityUsingLong.getAll, query = "SELECT l FROM EntityUsingLong l")
})
public class EntityUsingLong implements Serializable, ValidEntity {

	private static final long serialVersionUID = 1L;

    private static final String PREFIX = "sequences.entity.";
    public static final String getAll = PREFIX + "getAll";

	@Column(name = "long_as_id")
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "long_integer_seq")
    @SequenceGenerator(
           name = "sequence-corrected-definition",
           sequenceName = "long_integer_seq",
           initialValue = 54,
           allocationSize = 50
   )
	protected Long longAsId;

    @Column(name = "other_unique_item")
    private Long otherUniqueItem;

    @Column(name = "non_unique_text", nullable = false)
    private String nonUniqueText;

    public Long getLongAsId() {
        return longAsId;
    }

    public void setLongAsId(Long longAsId) {
        this.longAsId = longAsId;
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
        result = prime * result + ((longAsId == null) ? 0 : longAsId.hashCode());
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
        EntityUsingLong other = (EntityUsingLong) obj;
        if (longAsId == null) {
            if (other.longAsId != null)
                return false;
        } else if (!longAsId.equals(other.longAsId))
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