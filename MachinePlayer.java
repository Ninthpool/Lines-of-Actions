/* Skeleton Copyright (C) 2015, 2020 Paul N. Hilfinger and the Regents of the
 * University of California.  All rights reserved. */
package loa;

import java.util.List;

import static loa.Piece.*;

/** An automated Player.
 *  @author Heming Wu
 */
class MachinePlayer extends Player {

    /** A position-score magnitude indicating a win (for white if positive,
     *  black if negative). */
    private static final int WINNING_VALUE = Integer.MAX_VALUE - 20;
    /** A magnitude greater than a normal value. */
    private static final int INFTY = Integer.MAX_VALUE;
    /** A bonus point to reward. */
    private static final int BONUS1 = 50;
    /** A bonus point to reward. */
    private static final int BONUS2 = 200;
    /** A new MachinePlayer with no piece or controller (intended to produce
     *  a template). */
    MachinePlayer() {
        this(null, null);
    }

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    @Override
    String getMove() {
        Move choice;

        assert side() == getGame().getBoard().turn();
        int depth;
        choice = searchForMove();
        getGame().reportMove(choice);
        return choice.toString();
    }

    @Override
    Player create(Piece piece, Game game) {
        return new MachinePlayer(piece, game);
    }

    @Override
    boolean isManual() {
        return false;
    }

    /** Return a move after searching the game tree to DEPTH>0 moves
     *  from the current position. Assumes the game is not over. */
    private Move searchForMove() {
        Board work = new Board(getBoard());
        int value;
        assert side() == work.turn();
        _foundMove = null;
        if (side() == WP) {
            value = findMove(work, chooseDepth(), true, 1, -INFTY, INFTY);
        } else {
            value = findMove(work, chooseDepth(), true, -1, -INFTY, INFTY);
        }
        return _foundMove;
    }

    /** Find a move from position BOARD and return its value, recording
     *  the move found in _foundMove iff SAVEMOVE. The move
     *  should have maximal value or have value > BETA if SENSE==1,
     *  and minimal value or value < ALPHA if SENSE==-1. Searches up to
     *  DEPTH levels.  Searching at level 0 simply returns a static estimate
     *  of the board value and does not set _foundMove. If the game is over
     *  on BOARD, does not set _foundMove. */
    private int findMove(Board board, int depth, boolean saveMove,
                         int sense, int alpha, int beta) {
        if (depth == 0 || board.gameOver()) {
            return heuristic(board);
        }
        if (sense == 1) {
            int maxBest = -INFTY;
            List<Move> allMove = board.legalMoves();
            for (int i = 0; i < allMove.size(); i++) {
                Move mv = allMove.get(i);
                Board cp = new Board();
                cp.copyFrom(board);
                cp.makeMove(mv);
                int eval = findMove(cp, depth - 1, false,
                        -1, alpha, beta);
                maxBest = Math.max(maxBest, eval);
                if (saveMove && eval == maxBest) {
                    _foundMove = mv;
                }
                alpha = Math.max(alpha, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return maxBest;
        } else {
            int minBest = INFTY;
            List<Move> allMove = board.legalMoves();
            for (int i = 0; i < allMove.size(); i++) {
                Move mv = allMove.get(i);
                Board cp = new Board();
                cp.copyFrom(board);
                cp.makeMove(mv);
                int eval = findMove(cp, depth - 1, false,
                        1, alpha, beta);
                minBest = Math.min(minBest, eval);
                if (saveMove && eval == minBest) {
                    _foundMove = mv;
                }
                beta = Math.min(beta, eval);
                if (beta <= alpha) {
                    break;
                }
            }
            return minBest;
        }
    }

    /** Return a search depth for the current position. */
    int chooseDepth() {
        return 2;
    }


    /** Evaluate the score of a current board state.
     * Higher score favors white piece.
     * @param b current board.
     * @return Score of the board.
     */
    private int heuristic(Board b) {
        if (b.winner() == EMP) {
            return 0;
        }
        if (b.getRegionSizes(WP).size() == 1) {
            return 1000;
        }
        if (b.getRegionSizes(BP).size() == 1) {
            int c = -1 * 1000;
            return c;
        }
        int score = 0;
        int numWhite = b.countPiece(WP);
        int numBlack = b.countPiece(BP);
        int blockWhiteMv = b.countBlocked(WP);
        int blockBlackMv = b.countBlocked(BP);
        if (blockBlackMv > blockWhiteMv) {
            score += BONUS1;
        }
        if ((numBlack - b.getRegionSizes(BP).get(0))
            > (b.getRegionSizes(WP).get(0) - numWhite)) {
            int d = 100;
            score += d;
        }
        if (b.getRegionSizes(BP).size() > b.getRegionSizes(WP).size()) {
            score += BONUS2;
        }
        return score;
    }


    /** Used to convey moves discovered by findMove. */
    private Move _foundMove;

}
