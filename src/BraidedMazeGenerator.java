import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

/**
 * Generates a maze with a closed outer border, all spaces reachable by any
 * other space and no dead ends.
 */
public class BraidedMazeGenerator implements MazeGenerator {
    
    /**
     * Generates the maze. Width needs to equal 3 mod four so the door fits in 
     * the middle of the maze and all initial cells are correctly surrounded by
     * walls
     * @return a 2D array of CellTypes representing the maze
     */
    public CellType[][] generateMaze(int width, int height) {
        CellType[][] grid = new CellType[width][height];
        // Generate the starting point of the maze where certain cells are
        // walls and certain cells dots.
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
        // An array that stores whether a given cell is already mart of the
        // maze being generated
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
        
        // Chooses a random wall from walls and turns it into a dot unless
        // the cells on either side are already part of the maze.
        while (!walls.isEmpty()) {
            Point wall = walls.get(rand.nextInt(walls.size()));
            int x = (int)wall.getX();
            int y = (int)wall.getY();
            // Don't remove the border walls.
            if (x>=width-1 || x<1 || y>=height-1 || y<1) {
                walls.remove(wall);
                continue;
            }
            if (y%2==1) {
                // Vertical wall.
                if (partOfMaze[x-1][y] && partOfMaze[x+1][y]) {
                    walls.remove(wall);
                } else {
                    grid[x][y] = CellType.DOT;
                    if (partOfMaze[x-1][y]) {
                        partOfMaze[x+1][y] = true;
                        x += 1;
                    } else if (partOfMaze[x+1][y]) {
                        partOfMaze[x-1][y] = true;
                        x -= 1;
                    }
                    // Add the new dot's walls to walls.
                    walls.add(new Point(x+1, y));
                    walls.add(new Point(x, y+1));
                    walls.add(new Point(x-1, y));
                    walls.add(new Point(x, y-1));
                }
            } else {
                // Horizontal wall.
                if (partOfMaze[x][y-1] && partOfMaze[x][y+1]) {
                    walls.remove(wall);
                } else {
                    grid[x][y] = CellType.DOT;
                    if (partOfMaze[x][y-1]) {
                        partOfMaze[x][y+1] = true;
                        y = y+1;
                    } else if (partOfMaze[x][y+1]) {
                        partOfMaze[x][y-1] = true;
                        y = y-1;
                    }
                    // Add the new dot's walls to walls.
                    walls.add(new Point(x+1, y));
                    walls.add(new Point(x, y+1));
                    walls.add(new Point(x-1, y));
                    walls.add(new Point(x, y-1));
                }
            }
        }
        
        // Remove dead ends.
        for (int x = 1; x < width - 1; x++) {
        	for (int y = 1; y < height - 1; y++) {
        		if (grid[x][y] == CellType.DOT) {
        			// Get neighbour walls.
        			ArrayList<Vector> neighbours = new ArrayList<Vector>();
        			
        			int[] dirs = {-1, 0, 1};
        			
        			for (int dx : dirs) {
        				for (int dy : dirs) {
        					int manhattan = Math.abs(dx) + Math.abs(dy);
        					if (manhattan == 1) {
        						int x2 = x+dx;
        						int y2 = y+dy;
        						
        						if (grid[x2][y2] != CellType.DOT && 
        						    !(x2 == 0 || y2 == 0 || x2 == width-1
        						      || y2 == height-1)) {
        							neighbours.add(new Vector(x2, y2));
        						}
        					}
        				}
        			}
        			
        			// add outer walls if necessary
        			int outerWalls = 0;
        			
        			if (x == 1 || x == width-2) {
        				outerWalls++;
        			}
        			
        			if (y == 1 || y == height-2) {
        				outerWalls++;
        			}
        			
        			// Remove a random neighbour wall that isn't an outer wall.
        			Random r = new Random();
        			if (neighbours.size() + outerWalls >= 3) {
        				Vector n = neighbours.remove(r.nextInt(neighbours.size()));
        				grid[n.x()][n.y()] = CellType.DOT;
        			}
        		}
        	}
        }

        // Change walls into corresponding wall types depending on where walls
        // are around them.
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
        
        // Change the outer walls.
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
        
        // Change the corner walls.
        grid[0][0] = CellType.WALL_CORN_NW;
        grid[0][height - 1] = CellType.WALL_CORN_SW;
        grid[width - 1][0] = CellType.WALL_CORN_NE;
        grid[width - 1][height - 1] = CellType.WALL_CORN_SE;
        
        // Change the walls around the door.
        if (grid[width/2+1][1] != CellType.DOT) {
            grid[width/2+1][0] = CellType.WALL_CORN_NW;
        } else {
            grid[width/2+1][0] = CellType.WALL_END_W;
        }
        
        if (grid[width/2-1][1] != CellType.DOT) {
            grid[width/2-1][0] = CellType.WALL_CORN_NE;
        } else {
            grid[width/2-1][0] = CellType.WALL_END_E;
        }
        
        grid[width/2][0] = CellType.DOOR;
        
        return grid;
    }

}
