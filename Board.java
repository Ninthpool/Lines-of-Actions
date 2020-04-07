/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.ArrayList;
import java.util.Stack;
import java.util.List;
import java.util.Formatter;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;
import java.util.Collections;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Square.*;

/** Represents the state of a game of Lines of Action.
 *  @author Heming Wu
 */
class Board {

    /** Default number of moves for each side that results in a draw. */
    static final int DEFAULT_MOVE_LIMIT = 60;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /** A Board whose initial contents are taken from INITIALCONTENTS
     *  and in which the player playing TURN is to move. The resulting
     *  Board has
     *        get(col, row) == INITIALCONTENTS[row][col]
     *  Assumes that PLAYER is not null and INITIALCONTENTS is 8x8.
     *
     *  CAUTION: The natural written notation for arrays initializers puts
     *  the BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        this(INITIAL_PIECES, BP);
    }

    /** A Board whose initial contents and state are copied from
     *  BOARD. */
    Board(Board board) {
        this();
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Piece p = contents[r][c];
                Square s = Square.sq(c, r);
                for (int i = 0; i < _board.length; i++) {
                    _board[s.index()] = p;
                }
            }
        }
        _winnerKnown = false;
        _turn = side;
        _moveLimit = DEFAULT_MOVE_LIMIT;
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set my state to a copy of BOARD.
     * Use deep copy, don't shallow copy it.
     * */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        for (int i = 0; i < _board.length; i++) {
            _board[i] = board._board[i];
        }
        _winnerKnown = board._winnerKnown;
        _turn = board._turn;
        _moveLimit = board._moveLimit;
    }

    /** Return the contents of the square at SQ. */
    Piece get(Square sq) {
        return _board[sq.index()];
    }

    /** Set the square at SQ to V and set the side that is to move next
     *  to NEXT, if NEXT is not null. */
    void set(Square sq, Piece v, Piece next) {
        if (next != null) {
            _turn = next;
        }
        _board[sq.index()] = v;
    }

    /** Set the square at SQ to V, without modifying the side that
     *  moves next. */
    void set(Square sq, Piece v) {
        set(sq, v, null);
    }

    /** Set limit on number of moves by each side that results in a tie to
     *  LIMIT, where 2 * LIMIT > movesMade(). */
    void setMoveLimit(int limit) {
        if (2 * limit <= movesMade()) {
            throw new IllegalArgumentException("move limit too small");
        }
        _moveLimit = 2 * limit;
    }

    /** Copy a board.
     * @param b copy from this board
     * @return a new board
     */
    Piece[] copyBoard(Piece[] b) {
        Piece[] newBoard = new Piece[b.length];
        for (int r = 0; r < BOARD_SIZE; r++) {
            for (int c = 0; c < BOARD_SIZE; c++) {
                Square s = Square.sq(c, r);
                for (int i = 0; i < _board.length; i++) {
                    newBoard[s.index()] = b[s.index()];
                }
            }
        }
        return newBoard;
    }

    /** Assuming isLegal(MOVE), make MOVE. This function assumes that
     *  MOVE.isCapture() will return false.  If it saves the move for
     *  later retraction, makeMove itself uses MOVE.captureMove() to produce
     *  the capturing move. */
    void makeMove(Move move) {
        assert isLegal(move);
        try {
            _subsetsInitialized = false;
            _snapShot.push(copyBoard(_board));
            frSq = move.getFrom();
            frP = _board[frSq.index()];
            toSq = move.getTo();
            toP = _board[toSq.index()];
            capOrNot = (toP != EMP) && (toP == frP.opposite());
            if (capOrNot) {
                move = move.captureMove();
            }
            _mymoves.push(move);
            _moves.add(move);
            _board[toSq.index()] = frP;
            _board[frSq.index()] = EMP;
            _turn = frP.opposite();
        } catch (AssertionError er) {
            Utils.error("Illegal Move");
        }
    }

    /** Undo moves. */
    void undo() {
        try {
            Piece[] prev = _snapShot.pop();
            _board = prev;
            _winnerKnown = false;
            _moves.remove(_mymoves.pop());
            _turn = _turn.opposite();
            _subsetsInitialized = false;
        } catch (AssertionError er) {
            Utils.error("No moves to undo");
        }
    }

    /** Retract (unmake) one move, returning to the state immediately before
     *  that move.  Requires that movesMade () > 0. */
    void retract() {
        try {
            assert movesMade() > 0;
            _board[frSq.index()] = frP;
            _board[toSq.index()] = toP;
            _moves.remove(_mymoves.pop());
            _turn = frP;
            _subsetsInitialized = false;
        } catch (AssertionError er) {
            Utils.error("No moves to retract");
        }
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff FROM - TO is a legal move for the player currently on
     *  move. */
    boolean isLegal(Square from, Square to) {
        Piece fp = _board[from.index()];
        Piece tp = _board[to.index()];
        int dis = from.distance(to);
        int dir = from.direction(to);
        if (fp == tp
            || fp == EMP
            || fp != _turn
            || !from.isValidMove(to)
            || blocked(from, to)
            || countAlone(from, dir) != dis) {
            return false;
        }
        return true;
    }


    /** Count the number of pieces alone the line of direction from sq.
     * @param sq The square it's counting from
     * @param dir direction.
     * @return number of squares alone the line of direction.
     * */
    int countAlone(Square sq, int dir) {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square canSq = Square.sq(i, j);
                if (sq.sameLine(canSq, dir)) {
                    if (_board[canSq.index()] != EMP) {
                        count += 1;
                    }
                }
            }
        }
        return count;
    }

    /** Return true iff MOVE is legal for the player currently on move.
     *  The isCapture() property is ignored. */
    boolean isLegal(Move move) {
        if (move == null) {
            return false;
        }
        return isLegal(move.getFrom(), move.getTo());
    }

    /** Return a sequence of all _turn's legal moves from this position. */
    @SuppressWarnings("unchecked")
    List<Move> legalMoves() {
        _allLegal.clear();
        _eachLegal = new ArrayList[BOARD_SIZE  * BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (_board[Square.sq(i, j).index()] == _turn) {
                    Square sq = Square.sq(i, j);
                    _eachLegal[sq.index()] = new ArrayList<Move>();
                    for (int dir = 0; dir < 8; dir++) {
                        for (int step = 1; step < BOARD_SIZE; step++) {
                            Square canSq = sq.moveDest(dir, step);
                            Move m = Move.mv(sq, canSq);
                            if (canSq != null
                                    && isLegal(m)) {
                                _allLegal.add(m);
                                _eachLegal[sq.index()].add(m);
                            }
                        }
                    }
                }
            }
        }
        return _allLegal;
    }

    /** Map of each leagal moves.
     * @return each legal move.
     * */
    public ArrayList<Move>[] eachLegalMove() {
        legalMoves();
        return _eachLegal;
    }

    /** Return true iff the game is over (either player has all his
     *  pieces continguous or there is a tie). */
    boolean gameOver() {
        return winner() != null;
    }

    /** Return true iff SIDE's pieces are contiguous. */
    boolean piecesContiguous(Piece side) {
        return getRegionSizes(side).size() == 1;
    }

    /** Return the winning side, if any.  If the game is not over, result is
     *  null.  If the game has ended in a tie, returns EMP. */
    Piece winner() {
        if (!_winnerKnown) {
            if (movesMade() >= _moveLimit) {
                _winner = EMP;
                _winnerKnown = true;
            }
            if (getRegionSizes(_turn.opposite()).size() == 1) {
                _winner = _turn.opposite();
                _winnerKnown = true;
            } else if (getRegionSizes(_turn).size() == 1) {
                _winner = _turn;
                _winnerKnown = true;
            }
        }
        return _winner;
    }

    /** Return the total number of moves that have been made (and not
     *  retracted).  Each valid call to makeMove with a normal move increases
     *  this number by 1. */
    int movesMade() {
        return _moves.size();
    }

    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        return Arrays.deepEquals(_board, b._board) && _turn == b._turn;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(_board) * 2 + _turn.hashCode();
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = BOARD_SIZE - 1; r >= 0; r -= 1) {
            out.format("    ");
            for (int c = 0; c < BOARD_SIZE; c += 1) {
                out.format("%s ", get(sq(c, r)).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    /** Return true if a move from FROM to TO is blocked by an opposing
     *  piece or by a friendly piece on the target square. */
    private boolean blocked(Square from, Square to) {
        Piece fp = _board[from.index()];
        Piece tp = _board[to.index()];
        int dir = from.direction(to);
        if ((fp == tp) && (fp != EMP)) {
            return true;
        }
        for (int i = 1; i < from.distance(to); i++) {
            Square s = from.moveDest(dir, i);
            if (s != null && _board[s.index()] == fp.opposite()) {
                return true;
            }
        }
        return false;
    }

    /** Return how many moves are blocked for p in current state.
     * @param p piece of interest
     * @return number of moves blocked.
     * */
    int countBlocked(Piece p) {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square sq = sq(i, j);
                if (_board[sq.index()] == p) {
                    for (int dir = 0; dir < 8; dir++) {
                        int step = countAlone(sq, dir);
                        Square to = sq.moveDest(dir, step);
                        if (to != null) {
                            if (blocked(sq, to)) {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        return count;
    }

    /** Count number of piece p in current board.
     * @param p piece of interest.
     * @return how many pieces are there for p.
     */
    int countPiece(Piece p) {
        int count = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square sq = sq(i, j);
                for (int k = 0; k < BOARD_SIZE; k++) {
                    if (_board[sq.index()] == p) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /** Return the size of the as-yet unvisited cluster of squares
     *  containing P at and adjacent to SQ.  VISITED indicates squares that
     *  have already been processed or are in different clusters.  Update
     *  VISITED to reflect squares counted. */
    int numContig(Square sq, boolean[][] visited, Piece p) {
        if (_board[sq.index()] == p.opposite()
            || _board[sq.index()] == EMP
            || visited[sq.col()][sq.row()]) {
            return 0;
        }
        int count = 1;
        visited[sq.col()][sq.row()] = true;
        Square[] adj = sq.adjacent();
        for (Square adSq : adj) {
            count = count + numContig(adSq, visited, p);
        }
        return count;
    }

    /** Get a new indicator for numContig method.
     * @return a new indicator.
     * */
    private boolean[][] newInd() {
        return new boolean[BOARD_SIZE][BOARD_SIZE];
    }

    /** Set the values of _whiteRegionSizes and _blackRegionSizes. */
    private void computeRegions() {
        if (_subsetsInitialized) {
            return;
        }
        _whiteRegionSizes.clear();
        _blackRegionSizes.clear();
        _hatedList.clear();
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Square sq = Square.sq(i, j);
                Piece p = _board[sq.index()];
                if (p == EMP) {
                    continue;
                }
                boolean[][] ind = newInd();
                int regSize = numContig(sq, ind, p);
                if (p == BP && !_hatedList.contains(sq)) {
                    _blackRegionSizes.add(regSize);
                }
                if (p == WP && !_hatedList.contains(sq)) {
                    _whiteRegionSizes.add(regSize);
                }
                for (int c = 0; c < ind.length; c++) {
                    for (int r = 0; r < ind.length; r++) {
                        if (ind[c][r]) {
                            _hatedList.add(sq(c, r));
                        }
                    }
                }
            }
        }
        Collections.sort(_whiteRegionSizes, Collections.reverseOrder());
        Collections.sort(_blackRegionSizes, Collections.reverseOrder());
        _subsetsInitialized = true;
    }


    /** Return adjacent allies of sq. If it's EMP, return an empty array.
     * @param  sq sqaure of interest.
     * @return An array of square on which lands the same piece on sq.
     */
    Object[] adjAllies(Square sq) {
        Square[] adj = sq.adjacent();
        ArrayList<Square> result = new ArrayList<>();
        if (_board[sq.index()] == EMP) {
            return result.toArray();
        }
        for (int i = 0; i < adj.length; i++) {
            Square can = adj[i];
            if (_board[can.index()] == _board[sq.index()]) {
                result.add(can);
            }
        }
        return result.toArray();
    }


    /** Return the sizes of all the regions in the current union-find
     *  structure for side S. */
    List<Integer> getRegionSizes(Piece s) {
        computeRegions();
        if (s == WP) {
            return _whiteRegionSizes;
        } else {
            return _blackRegionSizes;
        }
    }

    /** Snapshot of board states. */
    private Stack<Piece[]> _snapShot = new Stack<>();


    /** Get the board.
     * @return board
     * */
    public Piece[] getBoard() {
        return _board;
    }

    /** Get the moves.
     * @return _moves
     * */
    public ArrayList<Move> getMoves() {
        return _moves;
    }

    /** Get map of legal moves for each square that
     * has a piece on it.
     * @return _eachLegal
     */
    public ArrayList<Move>[] getEachLegal() {
        return _eachLegal;
    }


    /** Return true if it's a capture move. */
    private Boolean capOrNot;

    /** get toSq.
     * @return toSq
     */
    public Square getToSq() {
        return toSq;
    }

    /** To Square when making a move. */
    private Square toSq;

    /** Get to piece.
     * @return toP
     * */
    public Piece getToP() {
        return toP;
    }

    /** The piece at ToSq (could be EMP).
     * */
    private Piece toP;

    /** Get from square.
     * @return frSq
     * */
    public Square getFrSq() {
        return frSq;
    }

    /** From Square when making a move. */
    private Square frSq;

    /** Get frp.
     * @return frP
     * */
    public Piece getFrP() {
        return frP;
    }

    /** The piece at frSq (could be EMP). */
    private Piece frP;

    /** All legal moves in this board for current turn. */
    private List<Move> _allLegal = new ArrayList<>();


    /** All legal moves for each square position. If there aren't
     * any legal moves from that square, the corresponding ArrayList
     * is empty (not null).
     * Square S is at _eachLegal[S.index()]. */
    @SuppressWarnings("unchecked")
    private ArrayList<Move>[] _eachLegal
            = new ArrayList[BOARD_SIZE  * BOARD_SIZE];

    /** Set of squares that I don't want to compute region on.
     *  Just a helper global variable for computing regions.
     */
    private Set<Square> _hatedList = new HashSet<>();



    /** The standard initial configuration for Lines of Action (bottom row
     *  first). */
    static final Piece[][] INITIAL_PIECES = {
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { WP,  EMP, EMP, EMP, EMP, EMP, EMP, WP  },
        { EMP, BP,  BP,  BP,  BP,  BP,  BP,  EMP }
    };

    /** Current contents of the board.  Square S is at _board[S.index()]. */
    private Piece[] _board = new Piece[BOARD_SIZE  * BOARD_SIZE];

    /** List of all unretracted moves on this board, in order. */
    private ArrayList<Move> _moves = new ArrayList<>();
    /** Also a stack of all unretracted moves, but it's easier to retract
     * multiple moves. */
    private final Stack<Move> _mymoves = new Stack<>();
    /** Current side on move. */
    private Piece _turn;
    /** Limit on number of moves before tie is declared.  */
    private int _moveLimit;
    /** True iff the value of _winner is known to be valid. */
    private boolean _winnerKnown;
    /** Cached value of the winner (BP, WP, EMP (for tie), or null (game still
     *  in progress).  Use only if _winnerKnown. */
    private Piece _winner;

    /** True iff subsets computation is up-to-date. */
    private boolean _subsetsInitialized;

    /** List of the sizes of continguous clusters of pieces, by color. */
    private final ArrayList<Integer>
        _whiteRegionSizes = new ArrayList<>(),
        _blackRegionSizes = new ArrayList<>();
}
