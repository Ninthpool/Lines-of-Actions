package loa;

import org.junit.Test;

import static org.junit.Assert.*;

public class SquareTest {
    @Test
    public void testAlongLine() {
        assertTrue(Square.sq(1, 1).sameLine(Square.sq(1, 1), 1));
        assertTrue(Square.sq(1, 1).sameLine(Square.sq(1, 1), 2));
        assertTrue(Square.sq(1, 1).sameLine(Square.sq(2, 1), 2));
        assertTrue(Square.sq(1, 1).sameLine(Square.sq(2, 2), 1));
        assertTrue(Square.sq(1, 1).sameLine(Square.sq(0, 0), 5));
        assertTrue(Square.sq(1, 1).sameLine(Square.sq(0, 2), 7));
        assertTrue(Square.sq(3, 5).sameLine(Square.sq(3, 0), 4));
        assertTrue(Square.sq(3, 5).sameLine(Square.sq(0, 5), 6));
        assertTrue(Square.sq(3, 5).sameLine(Square.sq(0, 5), 2));
        assertTrue(Square.sq(3, 5).sameLine(Square.sq(3, 5), 2));

        assertFalse(Square.sq(3, 5).sameLine(Square.sq(3, 0), 5));
        assertFalse(Square.sq(1, 1).sameLine(Square.sq(0, 6), 4));
        assertFalse(Square.sq(1, 1).sameLine(Square.sq(4, 2), 3));
        assertFalse(Square.sq(3, 5).sameLine(Square.sq(4, 0), 0));
    }

}
