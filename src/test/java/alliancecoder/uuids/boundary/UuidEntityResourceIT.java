package alliancecoder.uuids.boundary;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;

import java.util.UUID;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import alliancecoder.uuids.entity.EntityUsingUuid;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestInstance(Lifecycle.PER_CLASS)
@TestHTTPEndpoint(UuidEntityResource.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@QuarkusTestResource(H2DatabaseTestResource.class)
public class UuidEntityResourceIT {

    private EntityUsingUuid validEntity;
    private EntityUsingUuid invalidEntity;
    private EntityUsingUuid duplicateEntity;

    // No good Hibernate / DB Generation model so the client should handle IDs
    private UUID insertedId = UUID.randomUUID();

    @BeforeAll
    public void init() {
        this.invalidEntity = new EntityUsingUuid();
        invalidEntity.setOtherUniqueItem(0L);
        invalidEntity.setNonUniqueText("");
        this.validEntity = new EntityUsingUuid();
        validEntity.setUuidAsId(insertedId);
        validEntity.setOtherUniqueItem(5L);
        validEntity.setNonUniqueText("FIFTH RECORD (TEST ENTRY)");
        this.duplicateEntity = new EntityUsingUuid();
        duplicateEntity.setOtherUniqueItem(5L);
        duplicateEntity.setNonUniqueText("SIXTH RECORD (UNADDED ENTRY)");
    }

	@Test
    @Order(1)
    public void invalid_post_returns_400() {
        given().contentType(ContentType.JSON).body(invalidEntity).when().post().then().statusCode(is(400))
        .body(is("The Entity is not properly formatted. Please refer to the API documentation."));
    }

    @Test
    @Order(2)
    public void valid_post_returns_201_with_uri() {
        given().contentType(ContentType.JSON).body(validEntity).when().post().then().statusCode(is(201))
                .header("Location", containsString("/uuids/" + insertedId));
    }

    @Test
    @Order(3)
    public void duplicate_post_returns_409() {
        given().contentType(ContentType.JSON).body(duplicateEntity).when().post().then().statusCode(is(409))
        .body(is("An Entity with this unique data already exists."));
    }

    @Test
    @Order(4)
    public void get_with_valid_id_returns_200_with_json_payload() {
        given().when().get("/" + insertedId).then().statusCode(is(200)).body("nonUniqueText", is(validEntity.getNonUniqueText()));
    }

    @Test
    @Order(5)
    public void get_with_invalid_id_returns_404() {
        given().when().get("/42").then().statusCode(is(404));
    }

    @Test
    @Order(6)
    public void get_returns_200_with_records() {
        given().when().get().then().statusCode(is(200)).body("size()", is(5));
    }

    @Test
    @Order(7)
    public void invalid_put_returns_400() {
        EntityUsingUuid invalidEdit = this.validEntity;
        invalidEdit.setUuidAsId(insertedId);
        invalidEdit.setOtherUniqueItem(-42L);
        given().contentType(ContentType.JSON).body(invalidEdit).when().put("/" + insertedId).then().statusCode(is(400))
                .body(is("The Entity is not properly formatted. Please refer to the API documentation."));
    }

    @Test
    @Order(8)
    public void put_with_valid_entity_returns_200_with_json_payload() {
        EntityUsingUuid validEdit = this.validEntity;
        validEdit.setUuidAsId(insertedId);
        validEdit.setOtherUniqueItem(152L);
        validEdit.setNonUniqueText("FIFTH RECORD (TEST ENTRY) - MODIFIED");
        given().contentType(ContentType.JSON).body(validEdit).when().put("/" + insertedId).then().statusCode(is(200))
                .body("nonUniqueText", is(validEdit.getNonUniqueText()));

        // verify a get returns the modified record
        given().when().get("/" + insertedId).then().statusCode(is(200)).body("nonUniqueText", is(validEdit.getNonUniqueText()));
    }

    @Test
    @Order(9)
    public void valid_delete_returns_200() {
        given().when().delete("/" + insertedId).then().statusCode(is(200));
    }

    @Test
    @Order(10)
    public void invalid_delete_returns_204() {
        UUID invalidId = UUID.randomUUID();
        given().when().delete("/" + invalidId).then().statusCode(is(204));
    }
    
}