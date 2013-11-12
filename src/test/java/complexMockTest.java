import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

public class complexMockTest {

    public static final String EXPECTATION = "EXPECTATION";
    public static final String NORMAL_MATCHED_ARG = "NORMAL_MATCHED_ARG";
    public static final String ERROR_MATCHED_ARG = "arg";

    @Test
    public void should_mock_multiple_behaviour_for_same_method() throws Exception {
        final Host host = new Host(createMultipleBehaviorStubClient(ERROR_MATCHED_ARG));
        assertThat(host.run(ERROR_MATCHED_ARG), is(EXPECTATION));
    }

    private Client createMultipleBehaviorStubClient(String arg) {
        final Client mock = mock(Client.class);
        
        doThrow(new IllegalArgumentException()).when(mock).run(arg);
        
        when(mock.run(NORMAL_MATCHED_ARG)).thenReturn(EXPECTATION);
        
        return mock;
    }

    static class Host {
        private Client client;

        public Host(Client client) {
            this.client = client;
        }

        public String run(String arg){
            try {
                return client.run(arg);
            } catch (IllegalArgumentException e) {
                return client.run(NORMAL_MATCHED_ARG);
            }
        }
    }

    static class Client {

        public String run(String arg) throws IllegalArgumentException {
            return arg;
        }
    }

}
