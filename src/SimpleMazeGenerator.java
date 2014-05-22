import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;


public class SimpleMazeGenerator implements MazeGenerator {
    // TODO: This code sucks so much ass.
    
    public CellType[][] generateMaze(int width, int height, int numEnemy) {
        CellType[][] grid = new CellType[width][height];
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if ((x%2==1 && y%2==0) || x%2==0) {
                    grid[x][y] = CellType.WALL;
                } else {
                    grid[x][y] = CellType.DOT;
                }
            }
        }
        
        boolean[][] partOfMaze = new boolean[width][height];
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                partOfMaze[x][y] = false;
            }
        }
        Random rand = new Random();
        LinkedList<Point> walls = new LinkedList<Point>();
        
        // Start at the exit.
        partOfMaze[width/2][1] = true;
        
        walls.add(new Point(width/2, 2));
        walls.add(new Point(width/2+1, 1));
        walls.add(new Point(width/2-1, 1));
        
        while (!walls.isEmpty()) {
            Point wall = walls.get(rand.nextInt(walls.size()));
            int x = (int)wall.getX();
            int y = (int)wall.getY();
            if (x>=width-1 || x<1 || y>=height-1 || y<1) {
                walls.remove(wall);
                continue;
            }
            if (y%2==1) {
                // Vertical wall.
                if (partOfMaze[x-1][y] && partOfMaze[x+1][y]) {
                    walls.remove(wall);
                    // Connect back sometimes.
                    if (rand.nextInt(15) == 0) {
                        grid[x][y] = CellType.DOT;
                    }
                } else {
                    grid[x][y] = CellType.DOT;
                    if (partOfMaze[x-1][y]) {
                        partOfMaze[x+1][y] = true;
                        x = x+1;
                    } else if (partOfMaze[x+1][y]) {
                        partOfMaze[x-1][y] = true;
                        x = x-1;
                    }
                    walls.add(new Point(x+1, y));
                    walls.add(new Point(x, y+1));
                    walls.add(new Point(x-1, y));
                    walls.add(new Point(x, y-1));
                }
            } else {
                // Horizontal wall.
                if (partOfMaze[x][y-1] && partOfMaze[x][y+1]) {
                    walls.remove(wall);
                    // Connect back sometimes.
                    if (rand.nextInt(15) == 0) {
                        grid[x][y] = CellType.DOT;
                    }
                } else {
                    grid[x][y] = CellType.DOT;
                    if (partOfMaze[x][y-1]) {
                        partOfMaze[x][y+1] = true;
                        y = y+1;
                    } else if (partOfMaze[x][y+1]) {
                        partOfMaze[x][y-1] = true;
                        y = y-1;
                    }
                    walls.add(new Point(x+1, y));
                    walls.add(new Point(x, y+1));
                    walls.add(new Point(x-1, y));
                    walls.add(new Point(x, y-1));
                }
            }
        }

        // Assign Walls IT MAKES MY EYES BLEED!!!!
        for (int x=1; x<width - 1; x++) {
            for (int y=1; y<height - 1; y++) {
            	if (grid[x][y] != CellType.DOT) {
            		if (grid[x - 1][y] != CellType.DOT && grid[x + 1][y] != CellType.DOT) {
            			if (grid[x][y - 1] != CellType.DOT && grid[x][y + 1] != CellType.DOT) {
            				grid[x][y] = CellType.WALL_CROSS;
            			} else if (grid[x][y + 1] != CellType.DOT) {
            				grid[x][y] = CellType.WALL_T_S;
            			} else if (grid[x][y - 1] != CellType.DOT) {
            				grid[x][y] = CellType.WALL_T_N;
            			} else {
            				grid[x][y] = CellType.WALL_HOR;
            			}
            		} else if (grid[x][y - 1] != CellType.DOT && grid[x][y + 1] != CellType.DOT) {
            			if (grid[x + 1][y] != CellType.DOT) {
            				grid[x][y] = CellType.WALL_T_E;
            			} else if (grid[x - 1][y] != CellType.DOT) {
            				grid[x][y] = CellType.WALL_T_W;
            			} else {
            				grid[x][y] = CellType.WALL_VERT;
            			}
            		} else if (grid[x][y - 1] != CellType.DOT && grid[x - 1][y] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_CORN_SE;
            		} else if (grid[x][y - 1] != CellType.DOT && grid[x + 1][y] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_CORN_SW;
            		} else if (grid[x][y + 1] != CellType.DOT && grid[x - 1][y] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_CORN_NE;
            		} else if (grid[x][y + 1] != CellType.DOT && grid[x + 1][y] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_CORN_NW;
            		} else if (grid[x][y + 1] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_END_N;
            		} else if (grid[x][y - 1] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_END_S;
            		} else if (grid[x + 1][y] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_END_W;
            		} else if (grid[x - 1][y] != CellType.DOT) {
            			grid[x][y] = CellType.WALL_END_E;
            		} else {
            			grid[x][y] = CellType.WALL_BLOCK;
            		}
            	}
            }
        }
        
        for (int x=0; x < width; x++) {
        	if (grid[x][1] != CellType.DOT) {
        		grid[x][0] = CellType.WALL_T_S;
        	} else {
        		grid[x][0] = CellType.WALL_HOR;
        	}
        }    
        for (int x=0; x < width; x++) {
        	if (grid[x][height - 2] != CellType.DOT) {
        		grid[x][height - 1] = CellType.WALL_T_N;
        	} else {
        		grid[x][height - 1] = CellType.WALL_HOR;
        	}
        }
        for (int y=0; y < height; y++) {
        	if (grid[1][y] != CellType.DOT) {
        		grid[0][y] = CellType.WALL_T_E;
        	} else {
        		grid[0][y] = CellType.WALL_VERT;
        	}
        }
        for (int y=0; y < height; y++) {
        	if (grid[width - 2][y] != CellType.DOT) {
        		grid[width - 1][y] = CellType.WALL_T_W;
        	} else {
        		grid[width - 1][y] = CellType.WALL_VERT;
        	}
        }
        // Corner walls.
        grid[0][0] = CellType.WALL_CORN_NW;
        grid[0][height - 1] = CellType.WALL_CORN_SW;
        grid[width - 1][0] = CellType.WALL_CORN_NE;
        grid[width - 1][height - 1] = CellType.WALL_CORN_SE;
        
        // Walls around the door.
        // TODO: not actually correct if walls connect to these.
        grid[width/2+1][0] = CellType.WALL_END_W;
        grid[width/2-1][0] = CellType.WALL_END_E;
        
        
        // TODO: Actually add keys properly.
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
            	if(grid[x][y] == CellType.DOT) {
            		Random randomGenerator = new Random();
            		int randomInt = randomGenerator.nextInt(200);
            		if(randomInt==1 || randomInt==50)
            			grid[x][y] = CellType.KEY;
            	}
            }
        }
        
        grid[width/2][0] = CellType.DOOR;
        
        return grid;
    }

}
