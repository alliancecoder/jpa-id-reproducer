package alliancecoder.flyway.control;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import org.flywaydb.core.Flyway;
import org.jboss.logging.Logger;
import org.jboss.logging.Logger.Level;

import alliancecoder.globals.OperationResult;

@ApplicationScoped
public class MigrationService {

    private static final Logger LOGGER = Logger.getLogger(MigrationService.class.getName());

    @Inject
    Flyway flyway;

    public String checkMigrationVersion() {
        // This will print the latest version
        return flyway.info().current().getVersion().toString();
    }

    public OperationResult resetSampleData() {
        try {
            flyway.clean();
            flyway.migrate();
            return OperationResult.SUCCESS;
        } catch (Exception ex) {
            LOGGER.log(Level.DEBUG, ex.getMessage(), ex);
            return OperationResult.FAILURE;
        }
    }
}