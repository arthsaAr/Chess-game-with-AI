package Chess;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * This is a AI-controlled chess player
 * <p>
 * The AIPlayer automatically selects moves using a simple random or heuristic
 * strategy
 * depending on its difficulty level
 * </p>
 *
 * @author Group3
 * @version 1.0
 *
 */
public class AIPlayer extends Player {
    private int difficultyLevel; // need to implement the levels as well
    private Random random;

    /**
     * Creates a new AIPlayer with name, color, and difficulty level
     *
     * @param name            the name of the AI player
     * @param color           the color of the pieces controlled by this player
     *                        ("White" or "Black")
     * @param difficultyLevel the AI's difficulty level
     */
    public AIPlayer(String name, String color, int difficultyLevel) {
        super(name, color);
        this.difficultyLevel = difficultyLevel;
        this.random = new Random();
    }

    /**
     * Determines the next move for this AI player on the board
     * 
     * @param board the current state of the chessboard
     * @return the {@link Move} chosen by the AI (currently {@code null})
     */
    @Override
    public Move makeMove(Board board) {
        if (checkKingCheck(board, this.getColor())) {
            Move kingMove = getValidKingMoveInCheck(board);
            if (kingMove != null) {
                return kingMove;
            }
            return null;
        }
        List<Move> allValidMoves = generateValidMoves(board);

        if (allValidMoves.isEmpty()) {
            return null;
        }
        switch (difficultyLevel) {
            case 1:
                return veryBeginnerPlay(board, allValidMoves);
            case 2:
                return generateRandomMove(board, allValidMoves);
            case 3:
                return CaptureAnyPiece(board , allValidMoves);
            case 4:
                return captureHighValuePiece(board, allValidMoves);
            case 5:
                return avoidMoveToLoosePiece(board, allValidMoves);
            case 6:
                return populateBoardCenter(board, allValidMoves);
            case 7:
                return lookOneStepForward(board, allValidMoves);
            case 8:
                return OneStepAheadDefensivePlay(board, allValidMoves);
            case 9:
                return checkGivingMoves(board, allValidMoves);
            case 10:
                return HardestAIMove(board, allValidMoves);
            default:
                return generateRandomMove(board, allValidMoves);
        }
    }

    /**
     * @param board the current state of the chessboard
     * @return a list of all valid {@link Move} objects available to this player;
     *         empty list if no valid moves exist
     */

    public List<Move> generateValidMoves(Board board) { // refactoring from makeMove function into a seperate function
                                                        // for generating valid moves
        List<Piece> boardPieces = board.getAllPieces(this.getColor());
        List<Move> currentValidMoves = new ArrayList<>();

        for (int i = 0; i < boardPieces.size(); i++) {
            Piece piece = boardPieces.get(i);
            List<Coordinate> legalMoves = piece.getLegalMoves(board);

            for (int j = 0; j < legalMoves.size(); j++) {
                Coordinate to = legalMoves.get(j);
                Piece main = board.getPieceAt(to.getRow(), to.getCol());

                if (main == null || !piece.isSameColor(main)) {
                    Board testBoard = board.copy();
                    testBoard.movePiece(piece.getPosition().getRow(), 
                            piece.getPosition().getCol(),
                            to.getRow(), 
                            to.getCol(), 
                            null, false);

                    if(!checkKingCheck(testBoard, this.getColor())){
                        Move move = new Move(piece.getPosition(), to, piece, main, legalMoves, null);
                        currentValidMoves.add(move);
                    }
                }
            }
        }
        return currentValidMoves;
    }

    // here multiple checks are implemented, firstly find the piece that is giving
    // check, and try to capture it if possible, if not check if any other piece can
    // eat it,
    // if not than find if any piece can block the check and if not then we find the
    // valid available move for king

    /**
     * @param board the current state of the chessboard
     * @return a valid {@link Move} that escapes check, or {@code null} if no
     *         escape is possible (checkmate)
     */
    private Move getValidKingMoveInCheck(Board board) {
        Piece king = board.getAllPieces(this.getColor()).stream().filter(p -> p instanceof King).findFirst()
                .orElse(null);
        if (king == null) {
            return null;
        }

        //Our priority order is;
        //first eat with any available piece
        //if eating is not possible, consider blocking the check,
        //if not possible, than move the king/or eat with the king

        Coordinate curKingPosition = king.getPosition();
        String opponentColor = switchColor(this.getColor());

        List<Piece> opponentPieces = board.getAllPieces(opponentColor);

        // firstly getting all the opponent pieces that is attacking the king(giving
        // check)
        List<Piece> attackingPiece = new ArrayList<>();
        for (int i = 0; i < opponentPieces.size(); i++) {
            Piece curPiece = opponentPieces.get(i);
            List<Coordinate> curMove = curPiece.getLegalMoves(board);
            for (int j = 0; j < curMove.size(); j++) {
                if (curMove.get(j).equals(curKingPosition)) {
                    attackingPiece.add(curPiece);
                    break;
                }
            }
        }

        //this condition was found when playing, if there are multiple pieces giving check than ai was stuck in moving the piece instead of moving the kign
        if(attackingPiece.size()>1){
            return safeMoveforKing(board, king, curKingPosition);
        }

        if(attackingPiece.size() == 1){
            Piece attacker = attackingPiece.get(0);
            Coordinate attackerPosition = attacker.getPosition();

        //try to eat the piece with our piece if possible
            Move capturingMove = tryCaptureAttackerWithPiece(board, attacker, attackerPosition);
            if(capturingMove != null){
                return capturingMove;
            }

        // now if none of above move for capturing is possible, we will try to block the
        // check with other piece
        // blocking is only possible in these cases, as for other cases king must move,
        // or piece is captured
            Move blockingMove = tryBlockingTheCheck(board, attacker, attackerPosition, curKingPosition);
            if(blockingMove != null){
                return blockingMove;
            }

        //now, here if eating with other piece is not possible AND, eating with king is not possible, AND blocking the path is also not possible
        //than only option left is to move king to safe place
        //if eating from other piece is not possible, eat with king(if safe)
        //similar to our eating with other piece logic
            Move captureWithKing = tryCaptureWithKing(board, king, curKingPosition, attacker, attackerPosition);
            if (captureWithKing != null) {
                return captureWithKing;
            }

            return safeMoveforKing(board, king, curKingPosition);
        }
        return null;
    }

    private Move tryCaptureWithKing(Board board, Piece king, Coordinate curKingPosition, Piece attacker, Coordinate attackerPosition){
        List<Coordinate> kingMoves = king.getLegalMoves(board);
        for(int j = 0; j < kingMoves.size(); j++){
            Coordinate move = kingMoves.get(j);
            
            if(move.equals(attackerPosition)){
                // Test if king can safely capture
                Board testBoard = board.copy();
                testBoard.movePiece(curKingPosition.getRow(), 
                                    curKingPosition.getCol(),
                                    attackerPosition.getRow(), 
                                    attackerPosition.getCol(), null, false);
                
                // Make sure king is not in check after capturing
                if(!checkKingCheck(testBoard, this.getColor())){
                    Move kingCaptureMove = new Move(curKingPosition, attackerPosition, king, attacker, kingMoves, null);
                    return kingCaptureMove;
                }
            }
        }
        return null;
    }

    private Move tryBlockingTheCheck(Board board, Piece attacker, Coordinate attackerPosition, Coordinate curKingPosition){
        if ((attacker instanceof Rook) || (attacker instanceof Bishop) || (attacker instanceof Queen)) {
            List<Coordinate> betweenKingAndAttacker = new ArrayList<>();
            int r1 = attackerPosition.getRow();
            int c1 = attackerPosition.getCol();

            int r2 = curKingPosition.getRow();
            int c2 = curKingPosition.getCol();
            betweenKingAndAttacker = getSquaresBetween(r1, r2, c1, c2);

            if (!betweenKingAndAttacker.isEmpty()) {
                List<Piece> myPieces = board.getAllPieces(this.getColor());
                for (int j = 0; j < myPieces.size(); j++) {
                    Piece curPiece = myPieces.get(j);
                    if (curPiece instanceof King) {
                        continue;
                    }
                    List<Coordinate> pieceMoves = curPiece.getLegalMoves(board);

                    for (int k = 0; k < pieceMoves.size(); k++) {
                        Coordinate pieceMove = pieceMoves.get(k);
                        // checking if a move is one of the path squares w hich can block
                        boolean isSquareInBetween = false;
                        for (int l = 0; l < betweenKingAndAttacker.size(); l++) {
                            if (betweenKingAndAttacker.get(l).equals(pieceMove)) {
                                isSquareInBetween = true;
                                break;
                            }
                        }

                        // is that specific square can block then we try to move into that specific
                        // square and check if it doesnot lead to king be in check
                        if (isSquareInBetween) {
                            Board testBoard = board.copy();
                            Piece capturedPieceBefore = board.getPieceAt(pieceMove.getRow(), pieceMove.getCol());

                            testBoard.movePiece(curPiece.getPosition().getRow(), 
                                                curPiece.getPosition().getCol(),
                                                pieceMove.getRow(), 
                                                pieceMove.getCol(), null, false);

                            if (!checkKingCheck(testBoard, this.getColor())) {
                                return new Move(curPiece.getPosition(), pieceMove, curPiece,
                                        capturedPieceBefore, pieceMoves, null);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }
    //REFACTORING
    private Move tryCaptureAttackerWithPiece(Board board, Piece attacker, Coordinate attackerPosition){
       //try to capture with any available piece
        List<Piece> myPieces = board.getAllPieces(this.getColor());
        for(int j=0; j<myPieces.size(); j++){
            Piece myPiece = myPieces.get(j);
            if(myPiece instanceof King){
                continue;
            }

            List<Coordinate> legalMoves = myPiece.getLegalMoves(board);
            for(int k=0; k<legalMoves.size(); k++){
                Coordinate move = legalMoves.get(k);
                if(move.equals(attackerPosition)){
                    Board testBoard = board.copy();
                    testBoard.movePiece(myPiece.getPosition().getRow(), 
                                        myPiece.getPosition().getCol(),
                                        attackerPosition.getRow(), 
                                        attackerPosition.getCol(), null, false);
                boolean stillInCheck = checkKingCheck(testBoard, this.getColor());
                
                    if(!stillInCheck){
                        Move captureMove = new Move(myPiece.getPosition(), move, myPiece, attacker, legalMoves, null);
                        return captureMove;
                    }
                }
            }
        }
        return null;
    }

    private Move safeMoveforKing(Board board, Piece king, Coordinate curKingPosition ){
        //implementing manual calculation(hard coded)
        int[][] kingDirections = { { 1, 0 }, { -1, 0 }, { 0, 1 }, { 0, -1 }, { 1, 1 }, { 1, -1 }, { -1, 1 }, { -1, -1 } };

        for(int i=0; i<kingDirections.length; i++){
            int newRow = curKingPosition.getRow() + kingDirections[i][0];
            int newCol = curKingPosition.getCol() + kingDirections[i][1];
    

            // Check board boundaries manually(similar method as in King class getlegalmoves()
            if (newRow < 0 || newRow >= 8 || newCol < 0 || newCol >= 8) {
                continue;
            }

            Piece targetPiece = board.getPieceAt(newRow, newCol);
            
            // Skip if square has friendly piece
            if (targetPiece != null) {
                continue;
            }

            Coordinate newKingPosition = new Coordinate(newRow, newCol);
            if(isSquareDanger(board, newKingPosition, this.getColor())){
                continue;
            }
            Coordinate safeMove = new Coordinate(newRow, newCol);
            Move possibleMove = new Move(curKingPosition, safeMove, king, null, new ArrayList<>(), null);
            return possibleMove;
            
        }
        System.out.println("=== NO SAFE MOVES FOUND ===");
        return null;
    }

    private boolean isSquareDanger(Board board, Coordinate square, String color){
        String opponentColor = switchColor(color);
        List<Piece> opponentPieces = board.getAllPieces(opponentColor);

        for(int i=0; i<opponentPieces.size(); i++){
            Piece curPiece = opponentPieces.get(i);
            if(curPiece.getPosition() == null){
                continue;
            }

            List<Coordinate> opponentMoves = curPiece.getLegalMoves(board);
            for(int j=0; j<opponentMoves.size(); j++){
                Coordinate curMove = opponentMoves.get(j);
                if(curMove.equals(square)){
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Move generating methods based on the ai difficulty levels
     */

    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available to this AI player
     * @return a {@link Move} that is likely to lose material, or a random move
     *         if no losing moves are found
     */

    // this easy method will do bad moves, like give out pieces easily Level 1
    public Move veryBeginnerPlay(Board board, List<Move> moves) {
        List<Move> badMoves = new ArrayList<>();
        String opponentColor = switchColor(this.getColor());

        for (int i = 0; i < moves.size(); i++) {
            Board testBoard = board.copy();
            Move curMove = moves.get(i);
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            //adding extra check if that move leaves king with check
            if(checkKingCheck(testBoard, this.getColor())){
                continue;
            }

            boolean isCaptured = false;
            List<Piece> opponentPieces = testBoard.getAllPieces(opponentColor);
            for (int j = 0; j < opponentPieces.size(); j++) {
                Piece curPiece = opponentPieces.get(j);
                List<Coordinate> curPieceLegalMoves = curPiece.getLegalMoves(testBoard);
                for (int k = 0; k < curPieceLegalMoves.size(); k++) {
                    Coordinate currMove = curPieceLegalMoves.get(k);
                    if (currMove.getRow() == curMove.getTo().getRow()
                            && currMove.getCol() == curMove.getTo().getCol()) {
                        isCaptured = true;
                        break;
                    }
                }
                if(isCaptured){
                    break;
                }
            }
            if (isCaptured) {
                badMoves.add(curMove);
            }
        }
        if (!badMoves.isEmpty()) {
            return badMoves.get(0);
        }
        return moves.get(random.nextInt(moves.size())); // taking a random move from move slist if there is no possible
                                                        // move to loose.
    }

    // ai difficulty level 2 where ai just moves randomly
    /**
     * @param moves the list of all valid moves available
     * @return a randomly selected {@link Move} from the available moves
     */
    public Move generateRandomMove(Board board, List<Move> moves) {
        List<Move> finalAIMove = new ArrayList<>();
        for(int i=0; i<moves.size(); i++){
            Board testBoard = board.copy();
            Move curMove = moves.get(i);
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            //adding extra check if that move leaves king with check
            if(!checkKingCheck(testBoard, this.getColor())){
                finalAIMove.add(curMove);
            }
        }
        if(!finalAIMove.isEmpty()){
            return finalAIMove.get(random.nextInt(finalAIMove.size()));
        }
        return null;
    }

    // ai difficulty level 3 where ai just captures any available piece
    /**
     * @param moves the list of all valid moves available
     * @return a {@link Move} that captures a piece if available, otherwise a random
     *         move
     */
    public Move CaptureAnyPiece(Board board,List<Move> moves) {
        // firstly scanning all the possible moves, and filtering our only moves which
        // actually attack the enemy
        List<Move> capturable = moves.stream().filter(m -> m.getCapturedPiece() != null).collect(Collectors.toList());

        //adding extra check if that move leaves king with check
        List<Move> safeMove = new ArrayList<>();
        for(int i=0; i<capturable.size(); i++){
            Board testBoard = board.copy();
            Move curMove = capturable.get(i);
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            if(!checkKingCheck(testBoard, this.getColor())){
                safeMove.add(curMove);
            }
        }

        if (!capturable.isEmpty()) { // choosing any capturable move first
            return safeMove.get(random.nextInt(capturable.size()));
        }

        return generateRandomMove(board, moves);
    }

    // ai difficulty level 4. where ai takes a highvalue piece(assigned value by
    // ourself according to piece)
    /**
     * @param moves the list of all valid moves available
     * @return a {@link Move} that captures the highest-value piece, or a random
     *         move if no captures are possible
     */
    public Move captureHighValuePiece(Board board, List<Move> moves) {
        // firstly scanning all the possible moves, and filtering our only moves which
        // actually attack the enemy
        List<Move> capturable = moves.stream().filter(m -> m.getCapturedPiece() != null).collect(Collectors.toList());
        if (!capturable.isEmpty()) { // choosing any capturable move first
            Move currentBestMove = null;
            int bestValue = -1;
            for (int i = 0; i < capturable.size(); i++) {
                Move move = capturable.get(i);
                
                //EXTRA TEST
                Board testBoard = board.copy();
                testBoard.movePiece(move.getFrom().getRow(), move.getFrom().getCol(), move.getTo().getRow(),
                        move.getTo().getCol(), null, false);

                if(checkKingCheck(testBoard, this.getColor())){
                    continue;   //directly skipping move if illegal
                }

                int value = getpieceValue(move.getCapturedPiece());
                if (value > bestValue) {
                    bestValue = value;
                    currentBestMove = move;
                }
            }
            if(currentBestMove != null){
                return currentBestMove;
            }
        }
        return generateRandomMove(board, moves);
    }

    // ai difficulty level 5. where ai makes move without loosing any piece(also
    // makes sure it still has previour levels features)
    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available
     * @return a {@link Move} that avoids losing material while attempting captures
     */
    public Move avoidMoveToLoosePiece(Board board, List<Move> moves) {
        // checking the move from our level 3 difficulty, is it's safe
        Move secondBestMove = captureHighValuePiece(board, moves);
        if (secondBestMove != null && !isPieceLost(board, secondBestMove)) {
            return secondBestMove;
        }

        List<Move> safeMoves = new ArrayList<>();
        for (int i = 0; i < moves.size(); i++) {
            Move curMove = moves.get(i);

            //EXTRA TEST
            Board testBoard = board.copy();
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            if(checkKingCheck(testBoard, this.getColor())){
                continue;   //directly skipping move if illegal
            }

            if (!isPieceLost(board, curMove)) {
                safeMoves.add(curMove);
            }
        }

        if (!safeMoves.isEmpty()) {
            // taking any sagemove which captures if it exists(and after capture it's not
            // lost as well)
            Move capturable = CaptureAnyPiece(board, safeMoves);
            return capturable;
        }

        if (secondBestMove != null) {
            return secondBestMove; // if no safe move is found returning highvalue capture anyway
        }

        return generateRandomMove(board, safeMoves); // returning random when not found anything

    }

    // ai difficulty level 6, here the ai will follow level4, and if the level 4
    // doesnot captures anything, than prefer to move into center for better
    // advantages
    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available
     * @return a {@link Move} that balances captures with center control
     */
    public Move populateBoardCenter(Board board, List<Move> moves) {
        Move secondBestMove = avoidMoveToLoosePiece(board, moves);

        // if we have a high posiiton value captures than directly returning the move
        // which means even though we have a safe move, but if its not a capturing one
        // than we will try to move pieces to center
        if (secondBestMove != null && secondBestMove.getCapturedPiece() != null) {
            return secondBestMove; // capturing is best first instead of always moving to center
        }

        Move bestMove = null;
        int bestScore = -1;
        for (int i = 0; i < moves.size(); i++) {
            int curScore = 0;
            Move curMove = moves.get(i);

            //EXTRA TEST
            Board testBoard = board.copy();
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            if(checkKingCheck(testBoard, this.getColor())){
                continue;   //directly skipping move if illegal
            }

            if (curMove.getCapturedPiece() != null) {
                curScore = curScore + getpieceValue(curMove.getCapturedPiece());
            }
            curScore = curScore + centerMoveBonus(curMove.getTo()) * 3;

            if (!isPieceLost(board, curMove)) {
                curScore = curScore + 5; // extra points if piece is not lost after move
            }
            curScore = curScore + random.nextInt(3); // for making more random/ trying not to get same scores
            if (curScore > bestScore) {
                bestScore = curScore;
                bestMove = curMove;
            }
        }
        if (bestMove != null) {
            return bestMove;
        } else {
            return secondBestMove;
        }
    }

    // ai difficulty level 7, this one analyzes the scores of the board when a
    // specific piece is captured and returns the best move that has highest score
    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available
     * @return a {@link Move} with the best one-move-ahead evaluation
     */
    public Move lookOneStepForward(Board board, List<Move> moves) {
        Move bestMove = null;
        int bestScore = -1;
        for (int i = 0; i < moves.size(); i++) {
            Move curMove = moves.get(i);

            //EXTRA TEST
            Board testBoard = board.copy();
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            if(checkKingCheck(testBoard, this.getColor())){
                continue;   //directly skipping move if illegal
            }

            Board analysisBoard = board.copy();
            analysisBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);
            int curScore = analyzeBoard(analysisBoard, this.getColor());

            curScore = curScore + centerMoveBonus(curMove.getTo()) * 2; // considering moves that goes to center with a
                                                                        // bit more priority

            if (!isPieceLost(board, curMove)) { // if the move doesnot loose ai piece, than more priority should be gven
                curScore = curScore + 3;
            }

            if (curScore > bestScore) {
                bestScore = curScore;
                bestMove = curMove;
            }
        }

        if (bestMove != null) {
            return bestMove;
        } else {
            return populateBoardCenter(board, moves); // considering lev 5
        }
    }

    // ai level 8:

    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available
     * @return a {@link Move} that minimizes opponent's counterplay opportunities
     */
    public Move OneStepAheadDefensivePlay(Board board, List<Move> moves) {
        Move bestMove = null;
        int bestScore = -1;
        String opponentColor = switchColor(color);

        for (int i = 0; i < moves.size(); i++) {
            Move curMove = moves.get(i);
            Board analysisBoard = board.copy();
            analysisBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            //additional check before moving forward:
            if (checkKingCheck(analysisBoard, this.getColor())) {
                continue; // Skip !!
            }

            // finding opponent best capture after our move
            int oppBestScore = 0;
            List<Piece> oppPieces = analysisBoard.getAllPieces(opponentColor);
            for (int j = 0; j < oppPieces.size(); j++) {
                Piece curPiece = oppPieces.get(j);
                Piece capturesIs = analysisBoard.getPieceAt(curPiece.getRow(), curPiece.getCol());
                if (capturesIs != null && !curPiece.isSameColor(capturesIs)) {
                    int value = getpieceValue(capturesIs);
                    if (value > oppBestScore) {
                        oppBestScore = value;
                    }
                }
            }

            int aiScore = analyzeBoard(analysisBoard, this.getColor());
            int curScore = aiScore - oppBestScore * 10;

            curScore = curScore + centerMoveBonus(curMove.getTo()) * 2;
            if (!isPieceLost(board, curMove)) {
                curScore = curScore + 3;
            }

            if (curScore > bestScore) {
                bestScore = curScore;
                bestMove = curMove;
            }
        }

        if (bestMove != null) {
            return bestMove;
        } else {
            return lookOneStepForward(board, moves);
        }
    }

    // level 9

    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available
     * @return a {@link Move} that gives check if possible, otherwise a defensive
     *         move
     */
    public Move checkGivingMoves(Board board, List<Move> moves) {
        List<Move> checkMoves = new ArrayList<>();
        String opponentColor = switchColor(this.getColor());

        for (int i = 0; i < moves.size(); i++) {
            Board testBoard = board.copy();
            Move curMove = moves.get(i);
            testBoard.movePiece(curMove.getFrom().getRow(), curMove.getFrom().getCol(), curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);
            
            //additional check before adding the move to the list
            if (checkKingCheck(testBoard, this.getColor())) {
                continue;
            }

            if (checkKingCheck(testBoard, opponentColor)) {
                checkMoves.add(curMove);
            }
        }

        if (!checkMoves.isEmpty()) {
            return checkMoves.get(random.nextInt(checkMoves.size()));
        }
        return OneStepAheadDefensivePlay(board, moves);
    }

    // level 10

    /**
     * @param board the current state of the chessboard
     * @param moves the list of all valid moves available
     * @return a {@link Move} that maximizes the minimum guaranteed advantage
     */
    public Move HardestAIMove(Board board, List<Move> moves) {
        // There are still some points where the AI takes the pieces without thinking
        // about next step/what will happen to those pieces.
        // this logic is already implemented but still it generates such moves
        // sometimes...
        Move bestMovePossible = null;
        int bestScore = -10000;  //decreasing from -1 to -1000
        String opponentColor = switchColor(this.getColor());

        for (int i = 0; i < moves.size(); i++) {
            Board testBoard = board.copy();
            Move curMove = moves.get(i);
            testBoard.movePiece(
                    curMove.getFrom().getRow(),
                    curMove.getFrom().getCol(),
                    curMove.getTo().getRow(),
                    curMove.getTo().getCol(), null, false);

            //additional check for keeping our kign safe!
            if (checkKingCheck(testBoard, this.getColor())) {
                continue; // Skip illegal that leaves king in check
            }

            int worstScore = 10000;
            // for each opponen piece we need to see which moves capture our/ai pieces
            List<Piece> opponentPieces = testBoard.getAllPieces(opponentColor);
            // finding our the worst result of any move for ai player when opponent plays
            // their next move

            // if opponent/has no moves(this condition will not be true most of the time),
            // getting the score directly
            if (opponentPieces.isEmpty()) {
                worstScore = boardScore(testBoard, this.getColor());
            } else {
                // recopying our copied testboard that has the state after the ai moved in it,
                // checking for opponent move again
                for (int k = 0; k < opponentPieces.size(); k++) {
                    Piece curPiece = opponentPieces.get(k);
                    List<Coordinate> curPieceMoves = curPiece.getLegalMoves(testBoard);
                    for (int j = 0; j < curPieceMoves.size(); j++) {
                        Piece capturedPiece = testBoard.getPieceAt(curPieceMoves.get(j).getRow(),
                                curPieceMoves.get(j).getCol());

                        if (capturedPiece == null || !capturedPiece.isSameColor(curPiece)) {
                            Board boardAfterOpponentMove = testBoard.copy();
                            Piece curNewPiece = boardAfterOpponentMove.getPieceAt(curPiece.getPosition().getRow(),
                                    curPiece.getPosition().getCol());
                            if (curNewPiece != null) {
                                boardAfterOpponentMove.movePiece(
                                        curNewPiece.getPosition().getRow(),
                                        curNewPiece.getPosition().getCol(),
                                        curPieceMoves.get(j).getRow(),
                                        curPieceMoves.get(j).getCol(),
                                        null, false);

                                int score = boardScore(boardAfterOpponentMove, this.getColor());
                                score = score + centerMoveBonus(curPieceMoves.get(j));
                                if (isPieceLost(testBoard, curMove)) {
                                    score = score - 5; // reducing score if piece is lost after move
                                }
                                if (score < worstScore) {
                                    worstScore = score;
                                }
                            }
                        }
                    }
                }
            }
            // after each move changing the score after opponent move
            if (worstScore > bestScore) {
                bestScore = worstScore;
                bestMovePossible = curMove;
            }
        }
        if (bestMovePossible == null) {
            return checkGivingMoves(board, moves);
        }

        return bestMovePossible;
    }

    // setting values to out pieces, for deciding which one to take when multiple
    // captures are available
    /**
     * @param piece the chess piece to evaluate
     * @return the numerical value of the piece, or 0 if piece is null
     */
    private int getpieceValue(Piece piece) {
        if (piece == null) {
            return 0;
        }
        if (piece instanceof King) {
            return 100;
        }
        if (piece instanceof Queen) {
            return 50;
        }
        if (piece instanceof Rook) {
            return 30;
        }
        if (piece instanceof Bishop) {
            return 25;
        }
        if (piece instanceof Knight) {
            return 25;
        }
        if (piece instanceof Pawn) {
            return 1;
        }
        return 0;
    }

    /**
     * @param board the board state to evaluate
     * @param color the color to evaluate from ("White" or "Black")
     * @return the evaluation score; positive favors the AI, negative favors
     *         opponent
     */
    private int boardScore(Board board, String color) {
        int score = 0;
        List<Piece> allPieces = board.getAllPieces(color);
        for (int i = 0; i < allPieces.size(); i++) {
            score = score + getpieceValue(allPieces.get(i));
        }

        List<Piece> opponentPiece = board.getAllPieces(switchColor(this.getColor()));
        for (int i = 0; i < opponentPiece.size(); i++) {
            score = score - getpieceValue(opponentPiece.get(i));
        }

        return score; // considering more values as advantage and more score in current baord
    }

    // function which checks if the move, looses piece after it's done

    /**
     * @param board the current board state
     * @param move  the move to evaluate
     * @return {@code true} if the piece would be capturable after the move,
     *         {@code false} otherwise
     */
    private boolean isPieceLost(Board board, Move move) {
        Board analysisBoard = board.copy();
        analysisBoard.movePiece(move.getFrom().getRow(), move.getFrom().getCol(), move.getTo().getRow(),
                move.getTo().getCol(), null, false);

        Piece movedPiece = analysisBoard.getPieceAt(move.getTo().getRow(), move.getTo().getCol());
        if (movedPiece == null) {
            return false;
        }

        // after the piece is moved, checking if it can be captured by opponenet
        String opponentColor = switchColor(this.getColor());
        List<Piece> opponentPieces = analysisBoard.getAllPieces(opponentColor);
        for (int i = 0; i < opponentPieces.size(); i++) {
            Piece curPiece = opponentPieces.get(i);
            List<Coordinate> allLegalMoves = curPiece.getLegalMoves(analysisBoard);
            for (int j = 0; j < allLegalMoves.size(); j++) {
                Coordinate c = allLegalMoves.get(j);
                if (c.getRow() == move.getTo().getRow() && c.getCol() == move.getTo().getCol()) {
                    return true;
                }
            }
        }
        return false;
    }

    // helper function to switch color, mainly used to analyze the captures from
    // opponent's perspective.
    /**
     * @param color the current color ("White" or "Black")
     * @return the opposite color
     */
    private String switchColor(String color) {
        String changed;
        if (color.equals("White")) {
            changed = "Black";
        } else {
            changed = "White";
        }
        return changed;
    }

    // handling points for center squares, and asigning higher points for it
    /**
     * @param to the destination coordinate of a move
     * @return the center control bonus value (0, 2, or 4)
     */
    private int centerMoveBonus(Coordinate to) {
        int curRow = to.getRow();
        int curCol = to.getCol();
        // we make the center pieces as strongest i.e. the center 3*3 grid will be
        // strong so need high bonus for that
        if ((curRow >= 3 && curRow <= 4) && (curCol >= 3 && curCol <= 4)) {
            return 4;
        }
        if ((curRow >= 2 && curRow <= 5) && (curCol >= 2 && curCol <= 5)) {
            return 2;
        }
        return 0;
    }

    // checking the board based on what pieces are available on board for both side

    /**
     * @param board the board state to analyze
     * @param color the color to analyze from ("White" or "Black")
     * @return the material balance score; positive indicates advantage for
     *         specified color
     */
    private int analyzeBoard(Board board, String color) {
        int sum = 0;
        List<Piece> piece = board.getAllPieces(color);
        for (int i = 0; i < piece.size(); i++) {
            sum = sum + getpieceValue(piece.get(i));
        }

        piece = board.getAllPieces(switchColor(color));
        for (int i = 0; i < piece.size(); i++) {
            sum = sum - getpieceValue(piece.get(i));
        }
        // AI piece value - opponent piece value = finding the best move that results in
        // higher value

        return sum;
    }

    /**
     * @param board the board state to check
     * @param color the color of the king to check ("White" or "Black")
     * @return {@code true} if the king is in check, {@code false} otherwise
     */
    private boolean checkKingCheck(Board board, String color) {
        Piece king = board.getAllPieces(color).stream().filter(p -> p instanceof King).findFirst().orElse(null);

        if (king == null) {
            return false;
        }

        Coordinate kingPosition = king.getPosition();
        String opponentColor = switchColor(color);

        List<Piece> opponentPieces = board.getAllPieces(opponentColor);
        for (int i = 0; i < opponentPieces.size(); i++) {
            Piece curPiece = opponentPieces.get(i);
            if(curPiece.getPosition() == null) {
                continue;
            }

            List<Coordinate> curMove = curPiece.getLegalMoves(board);
            for (int j = 0; j < curMove.size(); j++) {
                if (curMove.get(j).equals(kingPosition)) {
                    System.out.println("    King at " + kingPosition + " is attacked by " + 
                                   curPiece.getClass().getSimpleName() + " at " + curPiece.getPosition());
                
                    return true;
                }
            }
        }
        return false;
    }

    // implemented this helper function to get the squares between king and the
    // attacking piece.
    /**
     * @param r1 the row coordinate of the first position
     * @param r2 the row coordinate of the second position
     * @param c1 the column coordinate of the first position
     * @param c2 the column coordinate of the second position
     * @return a list of {@link Coordinate} objects representing squares between
     *         the two positions; empty list if positions are adjacent or not
     *         aligned
     */
    private List<Coordinate> getSquaresBetween(int r1, int r2, int c1, int c2) {
        List<Coordinate> squaresBetween = new ArrayList<>();
        
        // if both the king and attacking piece is in same row
        if (r1 == r2) {
            int left = c1;
            int right = c2;
            if (c2 < c1) {
                left = c2;
                right = c1;
            }

            int c = left + 1;
            while (c < right) {
                squaresBetween.add(new Coordinate(r1, c));
                c = c + 1;
            }
            return squaresBetween;
        }

        // if they are in same column
        if (c1 == c2) {
            int top = r1;
            int bottom = r2;
            if (r2 < r1) {
                top = r2;
                bottom = r1;
            }

            int r = top + 1;
            while (r < bottom) {
                squaresBetween.add(new Coordinate(r, c1));
                r = r + 1;
            }
            return squaresBetween;
        }

        // diagonally attacking by attacker
        int diagonalr = r2 - r1;
        int diagonalc = c2 - c1;

        if ((diagonalc == diagonalr) || (diagonalr == -diagonalc)) {
            int stepR = 1;
            int stepC = 1;

            if (r2 < r1) {
                stepR = -1;
            }
            if (c2 < c1) {
                stepC = -1;
            }

            int r = r1 + stepR;
            int c = c1 + stepC;

            while (r != r2 && c != c2) {
                squaresBetween.add(new Coordinate(r, c));
                r = r + stepR;
                c = c + stepC;
            }
            return squaresBetween;
        }
        return squaresBetween;
    }

    /**
     * Gets the difficulty level of this AI player.
     * 
     * <p>
     * Difficulty levels range from 1 (beginner) to 10 (expert), where:
     * <ul>
     *   <li>1: Makes intentionally weak moves</li>
     *   <li>2-3: Random/simple capture strategies</li>
     *   <li>4-6: Basic evaluation and center control</li>
     *   <li>7-9: Look-ahead and defensive play</li>
     *   <li>10: Advanced minimax-style evaluation</li>
     * </ul>
     * </p>
     *
     * @return the difficulty level (1-10)
     */
    public int getDifficultyLevel() {
        return difficultyLevel;
    }

}