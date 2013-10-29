import static com.google.common.base.Optional.of;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import com.google.common.base.Optional;

public class ReplaceAllTest {
	@Test
	public void should_replace() throws Exception {
		assertThat(blackOut("<Password>password</Password><1Password>password</1Password>"),
                is("<Password>********</Password><1Password>password</1Password>"));
	}

	private String blackOut(String s) {
		return s.replaceAll("(<Password>)[^<]+(</Password>)", "$1********$2");
	}

	@Test
	public void should_be_true_if_not_null() throws Exception {
		Optional<IdentityMergeData> dataOptional = of(new IdentityMergeData());
		assertThat(dataOptional.or(IdentityMergeData.NEVER_MERGE).shouldMerge(), is(true));
	}

    @Test
    public void should_be_false_if_null() throws Exception {
        Optional<IdentityMergeData> dataOptional = Optional.fromNullable(null);
        assertThat(dataOptional.or(IdentityMergeData.NEVER_MERGE).shouldMerge(), is(false));
    }

	private static class IdentityMergeData {
		public static final IdentityMergeData NEVER_MERGE = new IdentityMergeData() {

			@Override
			public boolean shouldMerge() {
				return false;
			}
		};

		public boolean shouldMerge() {
			return true;
		}

	}

	@Test(expected = BException.class)
	public void should_Name() throws Exception {
        final B mock = mock(B.class);
        doThrow(new BException()).when(mock).run();
        A a = new A(mock);
        a.run();

	}

    private class A {
        private final B b;

        public A(B b ) {
            this.b = b;
        }

        public void run() throws BException {
            b.run();

        }
    }

    private class B {
        public void run() throws BException{
        }
    }
    public class BException extends Exception {
    }
}
