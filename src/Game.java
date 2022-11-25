import java.util.Stack;

public class Game {
    Board b;
    Stack<String> moveHistory = new Stack<String>();
    Side currentTurn;

    public Game(){
        
    	
    	Board board = new Board();
    	this.b = board;
    	this.currentTurn = Side.WHITE; 
 
    }

    public static String getName() {
        return "Queen's Game-bit";
    }
    public boolean canMove(int x, int y, int destX, int destY, Side s){
       
    	
    	if (x > 7 || y > 7 || destX > 7 || destY > 7 || x < 0 || y < 0 || destX < 0|| destY < 0) {
    		return false;
    	} else if (this.b.get(x, y) == null || this.b.get(x, y).isCaptured() == true) {
    		return false;
    	} else if (this.b.get(x, y).getSide() != this.currentTurn) {
    		return false;
    	} else if (this.b.get(x,y).canMove(destX, destY) == false) { 
    		return false;
    	} else if (this.b.get(destX, destY) != null && this.b.get(destX, destY).getSide() == this.currentTurn) {
    		return false;
    	} else if (!(this.b.get(x, y) instanceof Knight) && isVisible(x, y, destX, destY) == false) {
    		return false;
    	} else if ((this.b.get(x, y) instanceof Pawn) && (x == destX) && (this.b.get(destX, destY) != null)) {
    		return false;    	
    	} else {
    		return true;
    	}
    }

    
    private boolean isVisible(int x, int y, int destX, int destY) {
        int diffX = destX - x;
        int diffY = destY - y;

        boolean validCheck = (diffX == 0 || diffY == 0) || (Math.abs(diffX) == Math.abs(diffY));
        if (!validCheck) {
            try {
                throw new Exception("The 'path' between the squares (" + x + ", " + y + ") and (" + destX + ", " + destY +") is undefined");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //Diagonal
        if (Math.abs(diffX) == Math.abs(diffY) && Math.abs(diffX) > 1) {
            //Determine direction of move
            int dirX = diffX > 0 ? 1 : -1;
            int dirY = diffY > 0 ? 1 : -1;
            for (int i = x + dirX, j = y + dirY; i != destX && j != destY; i += dirX, j += dirY) {
                if (b.get(i, j) != null) {
                    return false;
                }
            }
        }

        //Linear
        if ((diffX == 0 && Math.abs(diffY) > 1) || (diffY == 0 && Math.abs(diffX) > 1)) {
            if (diffX == 0) {
                int dirY = diffY > 0 ? 1 : -1;
                for (int j = y + dirY; j != destY; j += dirY) {
                    if (b.get(x, j) != null) {
                        return false;
                    }
                }
            } else {
                int dirX = diffX > 0 ? 1 : -1;
                for (int i = x + dirX; i != destX; i += dirX) {
                    if (b.get(i, y) != null) {
                        return false;
                    }
                }
            }
        }

        return true;
    }

    private void appendCheckToHistory(Side side){
        moveHistory.push(side.toString()+ " is in check");
    }

    private void appendWinToHistory(Side side){
        moveHistory.push(side.toString()+ " has won");
    }

    private void appendMoveToHistory(int x, int y, int destX, int destY, Side side){
        Piece toMove = b.get(x,y);
        Piece toCapture = b.get(destX, destY);
        if(toCapture == null){
            moveHistory.push(side.toString() + toMove.getSymbol() + " at "+ x + ", " + y + " to " + destX + ", " + destY);
        }else{
            moveHistory.push(side.toString() + toMove.getSymbol() + " at " + x + ", " + y + " captures " + toCapture.getSymbol() + " at " + destX + ", " + destY);
        }
    }



    
    public boolean move(int x, int y, int destX, int destY){
        
    	if (canMove(x, y, destX, destY, this.currentTurn)){
    		
    		King king = b.getKing(Side.negate(this.currentTurn));
    		appendMoveToHistory(x, y, destX, destY, this.currentTurn);
    		this.b.get(x, y).move(destX, destY);
    		//TO DO: Append Capture History if Piece is Captured!!
    		
    		if (king.isCaptured()) {
    			appendWinToHistory(this.currentTurn);
    		}
    		
    		if (isInCheck(Side.WHITE)) {
    			appendCheckToHistory(Side.WHITE);
    		} else if (isInCheck(Side.BLACK)) {
    			appendCheckToHistory(Side.BLACK);
    		}
    		

    		 this.currentTurn = Side.negate(this.currentTurn);
    		 return true;
    	} else {
    		return false;
    	}
    	
       
    }

    /**
     * Return true if the King of Side side can be captured by any of
     * the opponent's pieces.
     *
     */
    public boolean isInCheck(Side side) {
    	
    	King king = this.b.getKing(side);
    
    	if (king == null) {
    		return false;
    	}
    	
    	for (Piece piece : b.getPieces(Side.negate(side))) {
    		
    		if ((piece instanceof Knight) && piece.canMove(king.x, king.y)) {
    			return true;
    		} else if (piece.canMove(king.x, king.y) && isVisible(piece.x, piece.y, king.x, king.y)) {
    			return true;
    		}
    	}
        return false;
    }

    /**
     * Ensures that the game is in the exact same state as a new game
     */
    public void reset(){
        while(!moveHistory.empty()){
            System.out.println(moveHistory.pop());
        }
        b.fillBoard();
        currentTurn = Side.WHITE;
    }


    public static void main(String[] args){
        Board b = new Board();
        System.out.println(b);
    }

    /**
     * Return an array of Strings containing every successful move made during the game,
     * and every time a move resulted in check.
     */
    public String[] moveHistory(){
        String[] out = new String[moveHistory.size()];
        for(int i = 0; i < moveHistory.size(); i++){
            out[i] = moveHistory.get(i);
        }
        return out;
    }

    public Side getCurrentTurn(){
        return currentTurn;
    }
}
