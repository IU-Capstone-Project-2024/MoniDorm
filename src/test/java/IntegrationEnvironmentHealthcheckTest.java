import org.junit.jupiter.api.Test;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class IntegrationEnvironmentHealthcheckTest extends IntegrationEnvironment {
    @Test
    void healthcheck() {
        assertThat(POSTGRES.isRunning()).isTrue();
    }
}
