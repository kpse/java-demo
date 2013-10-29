import static com.google.common.collect.Iterables.elementsEqual;
import static com.google.common.collect.Lists.transform;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.junit.Assert.assertThat;

import java.util.Arrays;
import java.util.List;

import com.google.common.base.Equivalence;
import com.google.common.base.Function;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.junit.Test;

public class newMatcherTest {
    @Test
    public void should_compare_events() throws Exception {
        final Event event = new Event(1, 1, 1);
        final Event event2 = new Event(2, 2, 2);

        EventStore eventStore = new EventStore(event, event2);

        assertThat(eventStore.allHappenedEvents(), hasItem(event));
        assertThat(eventStore.allHappenedEvents(), contains(event, event2));
        assertThat(eventStore.allHappenedEvents(), not(contains(event2, event)));

        assertThat(eventStore.allHappenedEvents(), containsEvents(event, event2));
        assertThat(eventStore.allHappenedEvents(), containsEvents(new Event(1, 1, 2), event2));
        assertThat(eventStore.allHappenedEvents(), containsEvents(new Event(1, 1, 3), new Event(2, 2, 3)));

        assertThat(eventStore.allHappenedEvents(), not(containsEvents(new Event(2, 2, 3), new Event(1, 1, 3))));
        assertThat(eventStore.allHappenedEvents(), not(containsEvents(new Event(2, 1, 1), new Event(2, 2, 3))));
    }

    private Matcher<? super List<Event>> containsEvents(final Event... events) {
        return new BaseMatcher<List<Event>>() {

            @Override
            public boolean matches(Object item) {
                final Equivalence equivalence = new Equivalence<Event>() {
                    @Override
                    protected boolean doEquivalent(Event a, Event b) {
                        return a.a == b.a && a.b == b.b;
                    }

                    @Override
                    protected int doHash(Event o) {
                        int result = o.a;
                        result = 31 * result + o.b;
                        return result;
                    }
                };

                final List expectation = transform((List<Event>) item, wrapEqual(equivalence));
                final List actual = transform(Arrays.asList(events), wrapEqual(equivalence));
                return elementsEqual(expectation, actual);
            }

            private Function<Event, Equivalence.Wrapper<Event>> wrapEqual(final Equivalence equivalence) {
                return new Function<Event, Equivalence.Wrapper<Event>>(){

                    @Override
                    public Equivalence.Wrapper<Event> apply(Event event) {
                        return equivalence.wrap(event);
                    }
                };
            }

            @Override
            public void describeTo(Description description) {

            }
        };
    }

    private class Event {
        int a;
        int b;
        int c;

        private Event(int a, int b, int c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }

        private Event() {
            this(0, 0, 0);
        }

//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            Event event = (Event) o;
//
//            if (a != event.a) return false;
//            if (b != event.b) return false;
//
//            return true;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = a;
//            result = 31 * result + b;
//            return result;
//        }
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
