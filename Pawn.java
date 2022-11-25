
public class Pawn extends Piece {
	public Pawn(int x, int y, Side side, Board board) {
    	super(x, y, side, board);
    }
	
	 @Override
	 public boolean canMove(int destX, int destY) {
		 
	    	if ((this.x == destX && this.y + 1 == destY && this.getSide() == Side.BLACK) || (this.x == destX && this.y - 1 == destY && this.getSide() == Side.WHITE)) {
	    		return true;
	    		
	    	} else if (((this.getSide() == Side.BLACK && this.y== 1) || (this.getSide() == Side.WHITE && this.y == 6)) 
	    			&& ((this.x == destX && this.y + 2 == destY) || (this.x == destX && this.y - 2 == destY))) {
	    		return true;
	    	} else if ( (((this.x -1 == destX && this.y + 1 == destY) || (this.x + 1 == destX && this.y + 1 == destY)) && (this.getSide() == Side.BLACK) && (board.get(destX, destY) != null)) 
	    			|| (((this.x -1 == destX && this.y - 1 == destY) || (this.x + 1 == destX && this.y - 1 == destY)) && (board.get(destX, destY) != null) && (this.getSide() == Side.WHITE)) ) {
	    		return true;
	    	} else {
	    		return false;
	    	}
	 }
	 
	 @Override
	    public String getSymbol() {
	        return this.getSide() == Side.BLACK ? "♟" : "♙";
	    
	 }
}