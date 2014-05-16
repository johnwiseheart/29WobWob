import java.awt.Point;
import java.util.LinkedList;
import java.util.Random;


public class SimpleMazeGenerator implements MazeGenerator {
    // This code sucks so much ass.
    
    public int[][] generateMaze(int width, int height) {
        int[][] grid = new int[width][height];
        for (int x=0; x<width; x++) {
            for (int y=0; y<height; y++) {
                if ((x%2==1 && y%2==0) || x%2==0) {
                    grid[x][y] = Maze.WALL;
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
                        grid[x][y] = Maze.SPACE;
                    }
                } else {
                    grid[x][y] = Maze.SPACE;
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
                        grid[x][y] = Maze.SPACE;
                    }
                } else {
                    grid[x][y] = Maze.SPACE;
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
        return grid;
    }

}
