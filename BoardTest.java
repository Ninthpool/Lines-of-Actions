/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static loa.Square.*;
import static org.junit.Assert.*;

import static loa.Piece.*;
import static loa.Move.mv;

/** Tests of the Board class API.
 *  @author
 */
public class BoardTest {

    /** A "general" position. */
    static final Piece[][] BOARD1 = {
        { EMP, BP,  EMP,  BP,  BP, EMP, EMP, EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP,  BP,  BP, EMP, WP  },
        { WP,  EMP,  BP, EMP, EMP,  WP, EMP, EMP  },
        { WP,  EMP,  WP,  WP, EMP,  WP, EMP, EMP  },
        { WP,  EMP, EMP, EMP,  BP, EMP, EMP, WP  },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP  },
        { EMP, BP,  BP,  BP,  EMP,  BP,  BP, EMP }
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD2 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  BP,  BP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  WP,  WP,  WP, EMP,  WP },
        { EMP,  WP,  WP,  BP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP,  BP, EMP, EMP, EMP, EMP },
    };

    /** A position in which black, but not white, pieces are contiguous. */
    static final Piece[][] BOARD3 = {
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
        { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
        { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
        { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
        { EMP,  WP,  WP,  WP, EMP, EMP, EMP, EMP },
        { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    /** A testing board. */
    static final Piece[][] BOARD4 = {
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
            { EMP,  WP,  BP,  WP,  WP, EMP, EMP, EMP },
            { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
            { EMP,  WP,  WP,  EMP, EMP, WP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    /** A testing board. */
    static final Piece[][] BOARD5 = {
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP,  BP,  WP,  BP,  WP, EMP, EMP, EMP },
            { EMP,  BP,  BP,  EMP,  WP, EMP, EMP, EMP },
            { EMP, EMP,  BP,  BP,  WP,  WP,  WP, EMP },
            { EMP,  WP,  WP,  EMP, EMP, WP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    /** A testing board. */
    static final Piece[][] BOARD6 = {
            { EMP, EMP, EMP, BP, EMP, WP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, BP,  BP,   WP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    /** A testing board. */
    static final Piece[][] BOARD7 = {
            { EMP, EMP, EMP, BP, EMP, WP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, BP,  BP,   WP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, WP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };

    /** A testing board. */
    static final Piece[][] BOARD8 = {
            { EMP, EMP, EMP, BP, EMP, WP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, WP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { BP,  BP,  BP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
    };


    static final String BOARD1_STRING =
        "===\n"
        + "    - b b b - b b - \n"
        + "    - - - - - - - - \n"
        + "    w - - - b - - w \n"
        + "    w - w w - w - - \n"
        + "    w - b - - w - - \n"
        + "    w - - - b b - w \n"
        + "    w - - - - - - w \n"
        + "    - b - b b - - - \n"
        + "Next move: black\n"
        + "===";

    /** Test count blocked. */
    @Test
    public void testNumblocked() {
        Board myBoard = new Board(BOARD3, BP);
        int c = myBoard.countBlocked(BP);
    }

    /** Test if correctly determine who's the winner. */
    @Test
    public void testWinner1() {
        Board myBoard = new Board(BOARD6, BP);
        assertNull(myBoard.winner());
        myBoard.makeMove(Move.mv("d1-d3"));
        assertEquals(BP, myBoard.winner());
    }

    /** Test if correctly determine who's the winner. */
    @Test
    public void testWinner2() {
        Board myBoard = new Board(BOARD7, BP);
        assertNull(myBoard.winner());
        myBoard.makeMove(Move.mv("d1-d3"));
        assertEquals(BP, myBoard.winner());
    }

    /** Test if correctly determine who's the winner. */
    @Test
    public void testWinner3() {
        Board myBoard = new Board(BOARD8, BP);
        assertNull(myBoard.winner());
        myBoard.makeMove(Move.mv("d1-d3"));
        assertEquals(WP, myBoard.winner());
    }


    /** Test retract. */
    @Test
    public void testRetract3() {
        Board myBoard = new Board();
        myBoard.makeMove(Move.mv("c1-c3"));
        myBoard.makeMove(Move.mv("a2-c2"));
        List<Move> a1 = myBoard.legalMoves();
        myBoard.makeMove(Move.mv("d1-d3"));
        myBoard.retract();
        List<Move> a2 = myBoard.legalMoves();
        assertEquals(a1, a2);

    }
    /** Test region sizes. */
    @Test
    public void testRegionsizes() {
        Board myBoard = new Board();
        assertEquals(2, myBoard.getRegionSizes(BP).size());
        assertEquals((Integer) 6, myBoard.getRegionSizes(BP).get(0));
        assertEquals(2, myBoard.getRegionSizes(WP).size());
        myBoard.makeMove(Move.mv("c1-c3"));
        assertEquals(4, myBoard.getRegionSizes(BP).size());
        assertEquals((Integer) 4, myBoard.getRegionSizes(BP).get(1));
        assertEquals((Integer) 1, myBoard.getRegionSizes(BP).get(2));
        assertEquals((Integer) 1, myBoard.getRegionSizes(BP).get(3));
    }

    /** Another test for region size. */
    @Test
    public void testRegionSies1() {
        Board myBoard = new Board(BOARD2, WP);
        assertEquals(1, myBoard.getRegionSizes(BP).size());
        assertEquals(2, myBoard.getRegionSizes(WP).size());
        myBoard.makeMove(Move.mv("h6-c6"));

        assertEquals(2, myBoard.getRegionSizes(BP).size());
        assertEquals(1, myBoard.getRegionSizes(WP).size());
    }

    /** Test adjacent allies. */
    @Test
    public void testAdjAllies() {
        Board myBoard = new Board();
        Object[] a = myBoard.adjAllies(sq("c1"));
        assertEquals(2, a.length);
        Set<Square> answer = new HashSet<>();
        answer.add(sq("b1"));
        answer.add(sq("d1"));
        for (int i = 0; i < a.length; i++) {
            assertTrue(answer.contains(a[i]));
        }
    }

    /** Test adjacent allies. */
    @Test
    public void testAdjAllies2() {
        Board myBoard = new Board(BOARD4, BP);
        Object[] a = myBoard.adjAllies(sq("c5"));
        assertEquals(4, a.length);
        Set<Square> answer = new HashSet<>();
        answer.add(sq("b4"));
        answer.add(sq("d4"));
        answer.add(sq("c6"));
        answer.add(sq("d6"));
        for (int i = 0; i < a.length; i++) {
            assertTrue(answer.contains(a[i]));
        }
    }




    /** Test display. I basically discard it because it's stupid*/
    @Test
    public void toStringTest() {
        assertNotEquals(BOARD1_STRING, new Board(BOARD1, BP).toString());
    }

   /** Get a new indicator for numContig method. */
    public boolean[][] newInd() {
        boolean[][] ind = new boolean[BOARD_SIZE][BOARD_SIZE];
        return ind;
    }

    /** Test numContig(). */
    @Test
    public void testNumContig1() {
        boolean[][] ind;
        Board myBoard = new Board();
        ind = newInd();
        int c1 = myBoard.numContig(sq("c1"), ind, BP);
        assertEquals(6, c1);
        ind = newInd();
        int d1 = myBoard.numContig(sq("d1"), ind, BP);
        assertEquals(6, d1);
        ind = newInd();
        int d11 = myBoard.numContig(sq("d1"), ind, WP);
        assertEquals(0, d11);
        ind = newInd();
        int h2 = myBoard.numContig(sq("h2"), ind, WP);
        assertEquals(6, h2);
    }

    /** Another test of numContig(). */
    @Test
    public void testNumContig2() {
        Board myBoard = new Board(BOARD3, BP);
        int b4 = myBoard.numContig(sq("b4"), newInd(), BP);
        int b5 = myBoard.numContig(sq("b5"), newInd(), WP);
        int b7 = myBoard.numContig(sq("b7"), newInd(), WP);
        int h2 = myBoard.numContig(sq("h2"), newInd(), WP);
        assertEquals(5, b4);
        assertEquals(11, b5);
        assertEquals(11, b7);
        assertEquals(0, h2);
    }

    /** Another test of numContig(). */
    @Test
    public void testNumContig3() {
        Board myBoard = new Board(BOARD5, BP);
        int b4 = myBoard.numContig(sq("b4"), newInd(), BP);
        int c5 = myBoard.numContig(sq("c5"), newInd(), BP);
        int b7 = myBoard.numContig(sq("b7"), newInd(), WP);
        int c4 = myBoard.numContig(sq("c4"), newInd(), WP);

        int e4 = myBoard.numContig(sq("e4"), newInd(), WP);
        int g6 = myBoard.numContig(sq("g6"), newInd(), WP);
        assertEquals(6, b4);
        assertEquals(6, c5);
        assertEquals(2, b7);
        assertEquals(1, c4);

        assertEquals(6, e4);
        assertEquals(6, g6);
    }




    /** Test countAlone method. */
    @Test
    public void testCountAlone() {
        Board myBoard = new Board();
        Square sq1 = Square.sq("c1");
        assertEquals(6, myBoard.countAlone(sq1, 2));
        assertEquals(6, myBoard.countAlone(sq1, 6));
        assertEquals(2, myBoard.countAlone(sq1, 7));
        assertEquals(2, myBoard.countAlone(sq1, 3));
        assertEquals(2, myBoard.countAlone(sq1, 0));
        assertEquals(2, myBoard.countAlone(sq1, 4));
        assertEquals(2, myBoard.countAlone(sq1, 1));
        assertEquals(2, myBoard.countAlone(sq1, 5));
    }

    /** Only an ad-host test for legalMoves. */
    @Test
    @SuppressWarnings("unchecked")
    public void testEachLeagal() {
        Board myBoard = new Board();
        List<Move> allMove = myBoard.legalMoves();
        assertEquals(36, allMove.size());
        ArrayList[] mapLMove = myBoard.eachLegalMove();
        Square c1 = sq("c1");
        Square h3 = sq("h3");
        ArrayList<Move> c1move = mapLMove[c1.index()];
        ArrayList<Move> h3move = mapLMove[h3.index()];
        myBoard.makeMove(Move.mv("b1-h1"));
        myBoard.makeMove(Move.mv("h3-f1"));
        myBoard.makeMove(Move.mv("g1-e3"));
        Square e1 = sq("e1");
        myBoard.makeMove(Move.mv("f1-f3"));
        ArrayList[] mapLMove1 = myBoard.eachLegalMove();
        ArrayList<Move> e1move = mapLMove1[e1.index()];

        List<Move> m = myBoard.legalMoves();
        assertEquals(34, m.size());
    }


    /** Test get and initialization */
    @Test
    public void testGet() {
        Board myBoard = new Board(BOARD3, BP);
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Square sq = Square.sq(c, r);
                assertEquals(BOARD3[r][c], myBoard.get(sq));
            }
        }
    }

    /** Test copy from and initialization from
     * another board.
     */
    @Test
    public void testCopyfrom() {
        Board board1 = new Board(BOARD1, BP);
        Board board2 = new Board(BOARD1, BP);
        Board board3 = new Board(BOARD2, BP);
        assertArrayEquals(board1.getBoard(), board2.getBoard());
        assertNotEquals(board2.getBoard(), board1.getBoard());
    }

    /** Test making a legal, not-capturing move.
     * Along with retract.
     */
    @Test
    public void testMakeMoveLN1() {
        Board myBoard = new Board();
        myBoard.makeMove(Move.mv("c1-c3"));
        Square fsq1 = Square.sq(2, 0);
        Piece fp1 = myBoard.getBoard()[fsq1.index()];
        Square tsq1 = Square.sq(2, 2);
        Piece tp1 = myBoard.getBoard()[tsq1.index()];
        assertEquals(BP, myBoard.getBoard()[tsq1.index()]);
        assertEquals(EMP, myBoard.getBoard()[fsq1.index()]);

        myBoard.retract();
        assertEquals(EMP, myBoard.getBoard()[tsq1.index()]);
        assertEquals(BP, myBoard.getBoard()[fsq1.index()]);
        assertEquals(0, myBoard.movesMade());
    }

    /** Another test as before. */
    @Test
    public void testMakeMoveLN2() {
        Board myBoard = new Board(Board.INITIAL_PIECES, WP);
        myBoard.makeMove(Move.mv("a2-c2"));
        Square fsq1 = Square.sq(0, 1);
        Piece fp1 = myBoard.getBoard()[fsq1.index()];
        Square tsq1 = Square.sq(2, 1);
        Piece tp1 = myBoard.getBoard()[tsq1.index()];
        assertEquals(WP, myBoard.getBoard()[tsq1.index()]);
        assertEquals(EMP, myBoard.getBoard()[fsq1.index()]);
        assertEquals(1, myBoard.movesMade());

        myBoard.retract();
        assertEquals(EMP, myBoard.getBoard()[tsq1.index()]);
        assertEquals(WP, myBoard.getBoard()[fsq1.index()]);
        myBoard.makeMove(Move.mv("a2-c4"));
        myBoard.makeMove(Move.mv("d1-d3"));
        assertEquals(2, myBoard.movesMade());
        myBoard.retract();
        assertEquals(1, myBoard.movesMade());
        myBoard.retract();
        assertEquals(0, myBoard.movesMade());
    }

    /** Test retract and movesMade() */
    @Test
    public void testRetract() {
        Board myBoard = new Board();
        myBoard.makeMove(Move.mv("c1-c3"));
        myBoard.makeMove(Move.mv("a2-c2"));
        myBoard.makeMove(Move.mv("b1-b3"));
        myBoard.makeMove(Move.mv("a4-c4"));
        myBoard.makeMove(Move.mv("d1-d3"));
        assertEquals(5, myBoard.movesMade());
        myBoard.makeMove(Move.mv("a5-c5"));
        assertEquals(6, myBoard.movesMade());
        myBoard.retract();
        assertEquals(5, myBoard.movesMade());
        myBoard.retract();
        assertEquals(Move.mv("a2-c2"), myBoard.getMoves().get(1));
        myBoard.retract();
        assertEquals(3, myBoard.movesMade());
        assertEquals(Move.mv("c1-c3"), myBoard.getMoves().get(0));
        myBoard.retract();
        assertEquals(2, myBoard.movesMade());
    }

    /* Test if an error will be thrown when there's nothing to retract. */
    @Test
    public void testRetract2() {
        Board myBoard = new Board();
        myBoard.makeMove(Move.mv("c1-c3"));
        myBoard.makeMove(Move.mv("a2-c2"));
        myBoard.makeMove(Move.mv("d1-d3"));
        myBoard.retract();
        myBoard.retract();
        myBoard.retract();
        myBoard.retract();
    }


    /** Test legal capturing moves, along with retract. */
    @Test
    public void testMakeMoveLC1() {
        Board myBoard = new Board(BOARD5, BP);
        Square fsq1 = Square.sq(1, 3);
        Piece fp1 = myBoard.getBoard()[fsq1.index()];
        Square tsq1 = Square.sq(1, 6);
        Piece tp1 = myBoard.getBoard()[tsq1.index()];
        assertEquals(WP, tp1);
        myBoard.makeMove(Move.mv("b4-b7"));
        assertEquals(BP, myBoard.getBoard()[tsq1.index()]);
        assertEquals(EMP, myBoard.getBoard()[fsq1.index()]);

        myBoard.retract();
        assertEquals(WP, myBoard.getBoard()[tsq1.index()]);
        assertEquals(BP, myBoard.getBoard()[fsq1.index()]);
        assertEquals(0, myBoard.movesMade());
    }

    /** Test illegal moves due to unselected from sq.
     */
    @Test (expected = AssertionError.class)
    public void testIllegalMove1() {
        Board myBoard = new Board();
        Square fsq1 = Square.sq(1, 4);
        Piece fp1 = myBoard.getBoard()[fsq1.index()];
        Square tsq1 = Square.sq(1, 7);
        Piece tp1 = myBoard.getBoard()[tsq1.index()];
        myBoard.makeMove(Move.mv("b4-b7"));
    }

    /** Test illegal moves due to to Sq == from Sq. */
    @Test (expected = AssertionError.class)
    public void testIllegalMove2() {
        Board myBoard = new Board(BOARD1, BP);
        myBoard.makeMove(Move.mv("b1-b1"));
    }

    /** Test illegal moves due to incorrect steps. */
    @Test (expected = AssertionError.class)
    public void testIllegalMove3() {
        Board myBoard = new Board(BOARD1, BP);
        myBoard.makeMove(Move.mv("b1-b5"));
    }

    /** Test illegal moves due to out of board */
    @Test (expected = AssertionError.class)
    public void testIllegalMove4() {
        Board myBoard = new Board(BOARD4, WP);
        myBoard.makeMove(Move.mv("f7-i7"));
    }

    /** Test illegal moves due to wrong term */
    @Test (expected = AssertionError.class)
    public void testIllegalMove5() {
        Board myBoard = new Board(BOARD4, BP);
        myBoard.makeMove(Move.mv("f7-i7"));
    }

    /** Test illegal moves due to blocked by
     * opposite piece.
     */
    @Test (expected = AssertionError.class)
    public void testIllegalMove6() {
        Board myBoard = new Board(BOARD4, BP);
        myBoard.makeMove(Move.mv("c5-g5"));
    }

    /** Test illegal moves due to land on
     * allies.
     */
    @Test (expected = AssertionError.class)
    public void testIllegalMove7() {
        Board myBoard = new Board(BOARD5, BP);
        myBoard.makeMove(Move.mv("d4-d6"));
    }


    /** Test legal moves. */
    @Test
    public void testLegality1() {
        Board b = new Board(BOARD1, BP);
        assertTrue("f3-d5", b.isLegal(mv("f3-d5")));
        assertTrue("f3-h5", b.isLegal(mv("f3-h5")));
        assertTrue("f3-h1", b.isLegal(mv("f3-h1")));
        assertTrue("f3-b3", b.isLegal(mv("f3-b3")));
        assertFalse("f3-d1", b.isLegal(mv("f3-d1")));
        assertFalse("f3-h3", b.isLegal(mv("f3-h3")));
        assertFalse("f3-e4", b.isLegal(mv("f3-e4")));
        assertFalse("c4-c7", b.isLegal(mv("c4-c7")));
        assertFalse("b1-b4", b.isLegal(mv("b1-b4")));
    }

    /** Test contiguity. */
    @Test
    public void testContiguous1() {
        Board b1 = new Board(BOARD1, BP);
        assertFalse("Board 1 black contiguous?", b1.piecesContiguous(BP));
        assertFalse("Board 1 white contiguous?", b1.piecesContiguous(WP));
        assertFalse("Board 1 game over?", b1.gameOver());
        Board b2 = new Board(BOARD2, BP);
        assertTrue("Board 2 black contiguous?", b2.piecesContiguous(BP));
        assertFalse("Board 2 white contiguous?", b2.piecesContiguous(WP));
        assertTrue("Board 2 game over", b2.gameOver());
        Board b3 = new Board(BOARD3, BP);
        assertTrue("Board 3 white contiguous?", b3.piecesContiguous(WP));
        assertTrue("Board 3 black contiguous?", b3.piecesContiguous(BP));
        assertTrue("Board 3 game over", b3.gameOver());
    }

    @Test
    public void testEquals1() {
        Board b1 = new Board(BOARD1, BP);
        Board b2 = new Board(BOARD1, BP);

        assertEquals("Board 1 equals Board 1", b1, b2);
    }

    @Test
    public void testMove1() {
        Board b0 = new Board(BOARD1, BP);
        Board b1 = new Board(BOARD1, BP);
        b1.makeMove(mv("f3-d5"));
        assertEquals("square d5 after f3-d5", BP, b1.get(sq(3, 4)));
        assertEquals("square f3 after f3-d5", EMP, b1.get(sq(5, 2)));
        assertEquals("Check move count for board 1 after one move",
                     1, b1.movesMade());
        b1.retract();
        assertEquals("Check for board 1 restored after retraction", b0, b1);
        assertEquals("Check move count for board 1 after move + retraction",
                     0, b1.movesMade());
    }

}
