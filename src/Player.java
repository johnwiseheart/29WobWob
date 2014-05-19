
public class Player extends Character {

    public Player(int x, int y) {
        super(x, y);
        xV = 0;
        yV = 0;
    }
    
    public void setV(int xV, int yV) {
        this.xV = xV;
        this.yV = yV;
    }
    
    public int nextPositionX() {
        return x + xV;
    }
    
    public int nextPositionY() {
        return y + yV;
    }
    
    public void move() {
        this.x += xV;
        this.y += yV;
    }
    
    private int xV;
    private int yV;
}
