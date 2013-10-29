import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class newMatcherTest {
    @Test
    public void should_compare_events() throws Exception {
        final Event event = new Event();
        final Event event2 = new Event();

        EventStore eventStore = new EventStore(event, event2);

        assertThat(eventStore.allHappenedEvents(), hasItem(event));
        assertThat(eventStore.allHappenedEvents(), contains(event, event2));

    }

    private class Event {
    }

    private class EventStore {
        private final List<Event> events;

        public EventStore(Event... events) {
            this.events = Arrays.asList(events);

        }

        public List<Event> allHappenedEvents() {
            return events;
        }
    }
}
