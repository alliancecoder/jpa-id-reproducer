package alliancecoder.sequences.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
public class EntityUsingLongTest {

    private EntityUsingLong cut;
    
    @Test
    public void can_create_valid_entity() {
        this.cut = new EntityUsingLong();
        cut.setOtherUniqueItem(42L);
        cut.setNonUniqueText("TEST CONTENT");
        Assertions.assertTrue(cut.isValid());
    }
    
    @Test
    public void zero_or_nagative_other_is_invalid_entity() {
        this.cut = new EntityUsingLong();
        cut.setOtherUniqueItem(0L);
        cut.setNonUniqueText("TEST CONTENT");
        Assertions.assertFalse(cut.isValid());
    }
    
    @Test
    public void missing_text_is_invalid_entity() {
        this.cut = new EntityUsingLong();
        cut.setOtherUniqueItem(42L);
        // missing
        Assertions.assertFalse(cut.isValid());
        // empty
        cut.setNonUniqueText("");
        Assertions.assertFalse(cut.isValid());
        // blank
        cut.setNonUniqueText("     ");
        Assertions.assertFalse(cut.isValid());
    }

}