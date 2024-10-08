import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Pipeline {

    @Test
    public void equal() {
        int expected = 2;
        int actual = 2;
        assertEquals(expected, actual);

    }

    @Test
    public void wrongEqual() {
        int expected = 3;
        int actual = 2;
        assertEquals(expected, actual);

    }
}
