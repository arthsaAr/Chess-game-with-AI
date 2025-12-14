package edu.kingsu.SoftwareEngineering.Chess;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import edu.kingsu.SoftwareEngineering.Chess.innerclasses.InnerBoardFactory;
public class AIPlayerTest {

    private AIPlayer aiPlayer;
    private InnerBoardFactory test;
    @Before 
    public void setUp() {
        aiPlayer = new AIPlayer("AI", "White", 2);
        test = new InnerBoardFactory();
    }

    @After 
    public void destroy() {
        aiPlayer = null;
    }

    // ================= BASIC CONSTRUCTOR TEST =================

    @Test 
    public void testAIPlayerInitialization(){
        assertNotNull(aiPlayer);
        assertEquals("AI", aiPlayer.getName());
        assertEquals("White", aiPlayer.getColor());
        assertEquals(2, aiPlayer.getDifficultyLevel());
    }

    // ================= GENERATE VALID MOVES =================

    @Test 
    public void testGenerateValidMoveReturnsList(){
        Board board = new Board(null);
        List<Move> moves = aiPlayer.generateValidMoves(board);

        assertNotNull("generateValidMoves should not return null", moves);
        // We DO NOT force >0 because empty boards are valid
    }

    // ================= MAKE MOVE SAFETY =================

    @Test 
    public void testMakeMoveDoesNotCrash(){
        Board board = new Board(null);
        Move move = aiPlayer.makeMove(board);

        // Move may be null — this is VALID in your AI
        if (move != null) {
            assertNotNull(move.getFrom());
            assertNotNull(move.getTo());
        }
    }

    // ================= RANDOM MOVE =================

    @Test
    public void testGenerateRandomMove(){
        List<Move> moves = createDummyMoves();
        Board board = new Board(null);
        Move move = aiPlayer.generateRandomMove(board, moves);

        assertNotNull(move);
        assertTrue(moves.contains(move));
    }

    // ================= CAPTURE ANY PIECE =================

    @Test
    public void testCaptureAnyPiece(){
        List<Move> moves = createDummyMoves();
        Board board = new Board(null);
        Move move = aiPlayer.CaptureAnyPiece(board, moves);

        assertNotNull(move);
        assertNotNull("This move must capture a piece", move.getCapturedPiece());
    }

    // ================= CAPTURE HIGH VALUE =================

    @Test
    public void testCaptureHighValuePiece(){
        List<Move> moves = createHighValueMoves();
        Board board = new Board(null);
        Move move = aiPlayer.captureHighValuePiece(board, moves);

        assertNotNull(move);
        assertTrue(move.getCapturedPiece() instanceof Queen);
    }

    // ================= VERY BEGINNER PLAY =================

    @Test
    public void testVeryBeginnerPlay(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.veryBeginnerPlay(board, moves);

        assertNotNull(move);
        assertTrue(moves.contains(move));
    }

    // ================= AVOID LOST PIECE SAFETY =================

    @Test
    public void testAvoidMoveToLoosePiece(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.avoidMoveToLoosePiece(board, moves);

        assertNotNull(move);
    }

    // ================= POPULATE CENTER =================

    @Test
    public void testPopulateBoardCenter(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.populateBoardCenter(board, moves);

        assertNotNull(move);
    }

    // ================= LOOK ONE STEP FORWARD =================

    @Test
    public void testLookOneStepForward(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.lookOneStepForward(board, moves);

        assertNotNull(move);
    }

    // ================= DEFENSIVE PLAY =================

    @Test
    public void testOneStepAheadDefensivePlay(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.OneStepAheadDefensivePlay(board, moves);

        assertNotNull(move);
    }

    // ================= CHECK GIVING =================

    @Test
    public void testCheckGivingMoves(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.checkGivingMoves(board, moves);

        assertNotNull(move);
    }

    // ================= HARDEST AI MOVE =================

    @Test
    public void testHardestAIMove(){
        Board board = new Board(null);
        List<Move> moves = createDummyMoves();
        Move move = aiPlayer.HardestAIMove(board, moves);

        assertNotNull(move);
    }

    // ================= HELPER DATA =================

    private List<Move> createDummyMoves() {
        List<Move> moves = new ArrayList<Move>();

        Piece whitePawn = new Pawn("White", new Coordinate(1, 1));
        Piece blackPawn = new Pawn("Black", new Coordinate(2, 2));

        Move captureMove = new Move(
                new Coordinate(1, 1),
                new Coordinate(2, 2),
                whitePawn,
                blackPawn,
                new ArrayList<Coordinate>(),
                null);

        Move normalMove = new Move(
                new Coordinate(1, 1),
                new Coordinate(1, 2),
                whitePawn,
                null,
                new ArrayList<Coordinate>(),
                null);

        moves.add(captureMove);
        moves.add(normalMove);
        return moves;
    }

    private List<Move> createHighValueMoves() {
        List<Move> moves = new ArrayList<Move>();

        Piece whitePawn = new Pawn("White", new Coordinate(1, 1));
        Piece queen = new Queen("Black", new Coordinate(3, 3));
        Piece pawn = new Pawn("Black", new Coordinate(2, 2));

        moves.add(new Move(
                new Coordinate(1, 1),
                pawn.getPosition(),
                whitePawn,
                pawn,
                new ArrayList<Coordinate>(),
                null));

        moves.add(new Move(
                new Coordinate(1, 1),
                queen.getPosition(),
                whitePawn,
                queen,
                new ArrayList<Coordinate>(),
                null));

        return moves;
    }

    @Test
    public void testMakeMoveWhenKingInCheck() {
        Board board = InnerBoardFactory.kingInCheckScenario();
        AIPlayer ai = new AIPlayer("AI", "White", 6);

        Move move = ai.makeMove(board);

        assertNotNull(move);
        assertTrue(move.getMovedPiece() instanceof King ||
                move.getCapturedPiece() != null);
    }

        @Test
        public void testAllDifficultyLevels() {
            Board board = InnerBoardFactory.simpleBoardWithCapture();

            for (int level = 1; level <= 10; level++) {
                AIPlayer ai = new AIPlayer("AI", "White", level);
                Move move = ai.makeMove(board);
                assertNotNull("Difficulty " + level + " must return a move", move);
            }
        }

        // Minimal TestBoardFactory to satisfy tests that reference it; these return simple boards
                // and keep compilation passing. Adjust to construct specific board scenarios if needed.
                

        @Test
        public void testDoubleCheckForcesKingMove() {
            Board board = InnerBoardFactory.doubleCheckScenario();
            AIPlayer ai = new AIPlayer("AI", "White", 9);

            Move move = ai.makeMove(board);
            assertNotNull(move);
            assertTrue(move.getMovedPiece() instanceof King);
        }
    
        @Test
        public void testIsPieceLostTruePath() {
            Board board = new Board(null);

            AIPlayer ai = new AIPlayer("AI","White",5);
            List<Move> moves = createDummyMoves(); 

            Move riskyMove = moves.get(0);
            Move result = ai.avoidMoveToLoosePiece(board, moves);

            assertNotNull(result);
        }


        @Test
        public void testCenterMoveBonusMax() {
            AIPlayer ai = new AIPlayer("AI","White",6);
            Coordinate c = new Coordinate(3,3);

            Board board = new Board(null);
            Piece pawn = new Pawn("White", new Coordinate(2,3));

            List<Coordinate> coords = new ArrayList<>();
            coords.add(c);

            Move move = new Move(pawn.getPosition(), c, pawn, null, coords, null);

            List<Move> list = new ArrayList<>();
            list.add(move);

            Move result = ai.populateBoardCenter(board, list);

            assertEquals(c, result.getTo());
        }

        @Test
        public void testBoardEvaluationMath() {
            Board board = InnerBoardFactory.materialComparisonBoard();
            AIPlayer ai = new AIPlayer("AI","White",8);

            Move move = ai.lookOneStepForward(board, ai.generateValidMoves(board));

            assertNotNull(move);
        }

        @Test
        public void testSwitchColorWhiteToBlack() throws Exception {
            AIPlayer ai = new AIPlayer("AI","White",2);

            Method m = AIPlayer.class.getDeclaredMethod("switchColor", String.class);
            m.setAccessible(true);

            assertEquals("Black", m.invoke(ai, "White"));
            assertEquals("White", m.invoke(ai, "Black"));
        }

        @Test
        public void testHardestMoveWorstPath() {
            Board board = InnerBoardFactory.deepTacticScenario();
            AIPlayer ai = new AIPlayer("AI","White",10);

            Move move = ai.HardestAIMove(board, ai.generateValidMoves(board));
            assertNotNull(move);
        }

                
        }
