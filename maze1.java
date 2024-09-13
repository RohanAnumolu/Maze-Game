import java.util.*;
import java.io.*;
import java.lang.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import javax.imageio.ImageIO;
//import BufferedImage;
import javax.swing.*;  //we def need this


public class maze1 extends JPanel implements KeyListener{

	JFrame frame;
	int x, y, monX = 100, monY = 100;
	int dim = 15;
	int count = 0;
	String [][] maze;
	int dir = 1; //1 is north, 2 is west, 3 is south, 4 is east
	Font font = new Font("Ink Free", Font.BOLD, 40);
	Boolean draw3d = false, gameOver = false;
	ArrayList<Wall> walls = new ArrayList<Wall>();
	int pole = 50;
	BufferedImage rightWalk, rightStand, enemyWalk, enemyStand, enemy3D;

	public maze1()
	{
		frame = new JFrame();
		frame.add(this);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1200,800);
		x = 100;
		y = 100;

		setMaze();

		frame.addKeyListener(this);

		try
		{
			rightWalk = ImageIO.read(new File("rightWalk.png"));
			rightStand = ImageIO.read(new File("rightStand.png"));
			enemyWalk = ImageIO.read(new File("angWalk.jpg"));
			enemyStand = ImageIO.read(new File("angStand.png"));
			enemy3D = ImageIO.read(new File("sheepy.jpg"));
		}catch(IOException e)
		{

		}

		rightWalk=resize(rightWalk, dim, dim);
		rightStand =resize(rightStand, dim, dim);
		enemyWalk = resize(enemyWalk, dim, dim);
		enemyStand = resize(enemyStand, dim, dim);



		frame.setVisible(true);
	}

	public BufferedImage resize(BufferedImage image, int width, int height)
	{
		Image temp = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage scaledVersion = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gg = scaledVersion.createGraphics();
		gg.drawImage(temp, 0, 0, null);
		gg.dispose();
		return scaledVersion;
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		g.setColor(Color.BLACK);
		g.fillRect(0,0,frame.getWidth(), frame.getHeight());

		Graphics2D g2 = (Graphics2D)g;
		if(!gameOver)
		{

			if(!draw3d)
			{
				for(int r = 0; r < maze.length; r++)
				{
					for(int c = 0; c < maze[0].length; c++)
					{
						if(maze[r][c].equals("#"))
						{
							g.setColor(new Color(1, 121, 111));
							g.fillRect(c*dim+dim,r*dim+dim,dim,dim);
							g.setColor(Color.BLACK);
							g.drawRect(c*dim+dim,r*dim+dim,dim,dim);
						}

					}
				}


				g2.setStroke(new BasicStroke(2));
					//256 options from 0 to 255
				g.setColor(new Color(0, 125, 0));
				//g.fillOval(monX*dim+dim,monY*dim+dim,dim,dim);
				g.setColor(Color.CYAN);
				//g.drawOval(monX*dim+dim,monY*dim+dim,dim,dim);

				if(count%2 == 0)
				{
					g.drawImage(rightStand, x*dim+dim, y*dim+dim, this);
					g.drawImage(enemyStand, monX*dim+dim, monY*dim+dim, this);
				}
				else
				{
					g.drawImage(rightWalk, x*dim+dim, y*dim+dim, this);
					g.drawImage(enemyWalk, monX*dim+dim, monY*dim+dim, this);
				}

				//globql variable
				//Font font = new Font("Ink Free", Font.Bold, 40);
			}
			else
			{
				for(Wall wall: walls)
				{
					//g.setColor(wall.getColor());
					g2.setPaint(wall.getPaint());
					g2.fill(wall.getWall());
					g2.setColor(Color.BLACK);
					g2.draw(wall.getWall());
				}

				int playerX = x*dim+dim;
				int playerY = y*dim+dim;
				int monsterX = monX*dim+dim;
				int monsterY = monY*dim+dim;

				int wow = 150;
				int waw = 150;
				enemy3D = resize(enemy3D, wow, waw);
				if(playerX == monsterX)
				{
					if((playerY - monsterY == 45 && (maze[y+1][x].equals(" ") || maze[y+2][x].equals(" "))) || (monsterY - playerY == 45 && (maze[y-1][x].equals(" ") || maze[y-2][x].equals(" "))))
					{
						g2.drawImage(enemy3D, 325, 325, this);
					}
					else if((playerY - monsterY == 30 && maze[y+1][x].equals(" ")) || (monsterY - playerY == 30 && maze[y-1][x].equals(" ")))
					{
						wow+=100;
						waw+=100;
						enemy3D = resize(enemy3D, wow, waw);
						g2.drawImage(enemy3D, 250, 250, this);
					}
					else if(playerY - monsterY == 15 || monsterY - playerY == 15)
					{
						wow+=200;
						waw+=200;
						enemy3D = resize(enemy3D, wow, waw);
						g2.drawImage(enemy3D, 225, 225, this);
					}
				}
				if(playerY == monsterY)
				{
					if((playerX - monsterX == 45 && (maze[y][x-1].equals(" ") || maze[y][x-2].equals(" "))) || (monsterX - playerX == 45 && (maze[y][x+1].equals(" ") || maze[y][x+2].equals(" "))))
					{
						g2.drawImage(enemy3D, 325, 325, this);

					}
					else if((playerX - monsterX == 30 && maze[y][x-1].equals(" ")) || (monsterX - playerX == 30 && maze[y][x+1].equals(" ")))
					{
						wow+=100;
						waw+=100;
						enemy3D = resize(enemy3D, wow, waw);
						g2.drawImage(enemy3D, 250, 250, this);
					}
					else if(playerX - monsterX == 15 || monsterX - playerX == 15)
					{
						wow+=200;
						waw+=200;
						enemy3D = resize(enemy3D, wow, waw);
						g2.drawImage(enemy3D, 225, 225, this);

					}
				}
			}

			g.setColor(Color.CYAN);
			g.setFont(font);

			switch(dir)
			{
				case 1: g.drawString("N", 1000, 100);
					break;
				case 2: g.drawString("W", 1000, 100);
					break;
				case 3: g.drawString("S", 1000, 100);
					break;
				case 4: g.drawString("E", 1000, 100);
					break;
			}
		}
		else
		{
			g.setColor(new Color(1, 121, 111));
			g.setFont(font);
			g.drawString("GAME OVER", 450, 375);

		}

	}


	public void setMaze()
	{

		maze = new String[41][];
		File file = new File("maze1.txt");
		try
		{
			BufferedReader input = new BufferedReader(new FileReader(file));
			String st;
			int row = 0;
			while((st=input.readLine())!= null)
			{
				String[] line = st.split("");
				maze[row] = line;
				if(st.indexOf("N") >= 0)
				{
					x = st.indexOf("N");
					dir = 1;
					y = row;
					maze[row][x] = " ";

				}
				if(st.indexOf("W") >= 0)
				{
					x = st.indexOf("W");
					dir = 2;
					y = row;
					maze[row][x] = " ";

				}
				if(st.indexOf("S") >= 0)
				{
					x = st.indexOf("S");
					dir = 3;
					y = row;
					maze[row][x] = " ";
				}
				if(st.indexOf("E") >= 0)
				{
					x = st.indexOf("E");
					dir = 4;
					y = row;
					maze[row][x] = " ";

				}
				if(st.indexOf("M") >= 0)
				{
					monX = st.indexOf("M");
					monY = row;
					maze[row][monX] = " ";
				}
				row++;
			}



		}
		catch(IOException e)
		{
			System.err.println("File Does Not Exist");
		}

	}

	public void monster(int plaX, int plaY)
	{
		int playerX = plaX*dim+dim;
		int playerY = plaY*dim+dim;
		int monsterX = monX*dim+dim;
		int monsterY = monY*dim+dim;

		if(((playerY+15) == monsterY && monsterX == playerX) || ((playerY-15) == monsterY && playerX == monsterX) || ((playerX+15) == monsterX && playerY == monsterY) || ((playerX-15) == monsterX && playerY == monsterY))
			gameOver = true;

		else if(playerX == monsterX && playerY == monsterY)
		{
			System.out.println("FINISHED: player x: " + playerX + ", player y: " + playerY + ", monster x: " + monsterX + ", monster y: " + monsterY);
			gameOver = true;
		}

		else if(playerX > monsterX && playerY == monsterY) // pkayer is to the right same Y
		{
			if(maze[monY][monX+1].equals(" "))
				monX++;
		}
		else if(monsterX > playerX && monsterY == playerY) // player is to the left same Y
		{
			if(maze[monY][monX-1].equals(" "))
				monX--;
		}
		else if(playerY > monsterY && playerX == monsterX) // player is under same X
		{
			if(maze[monY+1][monX].equals(" "))
				monY++;
		}
		else if(monsterY > playerY && playerX == monsterX) // player is on top same X
		{
			if(maze[monY-1][monX].equals(" "))
				monY--;
		}
		else if(playerX > monsterX && playerY > monsterY) // player is to the right and under
		{
			if(maze[monY][monX+1].equals(" ") && maze[monY+1][monX].equals("#"))
				monX++;
			else if(maze[monY+1][monX].equals(" ") && maze[monY][monX+1].equals("#"))
				monY++;
			else if(maze[monY+1][monX].equals(" ") && maze[monY][monX+1].equals(" "))
			{
				if((playerX - monsterX) > (playerY - monsterY))
					monX++;
				else
					monY++;
			}
		}
		else if(playerX > monsterX && playerY < monsterY) // player is to the right and on top
		{
			if(maze[monY][monX+1].equals(" ") && maze[monY-1][monX].equals("#"))
				monX++;
			else if(maze[monY-1][monX].equals(" ") && maze[monY][monX+1].equals("#"))
				monY--;
			else if(maze[monY-1][monX].equals(" ") && maze[monY][monX+1].equals(" "))
			{
				if((playerX - monsterX) > (monsterY - playerY))
					monX++;
				else
					monY--;
			}
		}
		else if(playerX < monsterX && playerY < monsterY) // player is to the left and on top
		{
			System.out.println("RN PLAYER IS TOP LEFT::: player x: " + playerX + ", player y: " + playerY + ", monster x: " + monsterX + ", monster y: " + monsterY);
			if(maze[monY][monX-1].equals(" ") && maze[monY-1][monX].equals("#"))
				monX--;
			else if(maze[monY-1][monX].equals(" ") &&  maze[monY][monX-1].equals("#"))
				monY--;
			else if(maze[monY][monX-1].equals(" ") && maze[monY-1][monX].equals(" "))
			{
				if((monsterX - playerX) > (monsterY - playerY))
				{
					monX--;
				}
				else
				{
					monY--;
				}
			}
		}
		else if(playerX < monsterX && playerY > monsterY) // player is to the left and bottom
		{
			if(maze[monY][monX-1].equals(" ") && maze[monY+1][monX].equals("#"))
				monX--;
			else if(maze[monY+1][monX].equals(" ") && maze[monY][monX-1].equals("#"))
				monY++;
			else if(maze[monY+1][monX].equals(" ") && maze[monY][monX-1].equals(" "))
			{
				if((monsterX - playerX) > (playerY - monsterY))
					monX--;
				else
					monY++;
			}
		}

	}

	public void keyReleased(KeyEvent e)
	{
		System.out.println(e.getKeyCode());
		//37 = left, 38 = up, 39 = right, 40 = down

	}

	public void keyPressed(KeyEvent e)
	{
		count++;
		monster(x, y);

		if(e.getKeyCode() == 38)
		{
			if(dir == 1)
			{
				if(maze[y-1][x].equals(" "))
					y-=1;
			}
			else if(dir == 2)
			{
				if(maze[y][x-1].equals(" "))
					x-=1;
			}
			else if(dir == 3)
			{
				if(maze[y+1][x].equals(" "))
					y+=1;
			}
			else if(dir == 4)
			{
				if(maze[y][x+1].equals(" "))
					x+=1;
			}


		}
		if(e.getKeyCode() == 37)
		{

			dir++;
			if(dir>4)
			{
				dir = 1;
			}

		}
		if(e.getKeyCode() == 39)
		{
			dir--;
			System.out.println(dir);
			if(dir < 1)
			{
				dir = 4;
			}
		}

		if(e.getKeyCode() == 32)
		{
			draw3d = !draw3d;
		}


		if(draw3d)
		{

			walls = new ArrayList<Wall>();
			addLeftWithNoQuestions();
			addRightWithNoQuestions();
			addLeftPathwayRectangles();
			addRightPathwayRectangles();
			drawLeftWalls();
			drawRightWalls();
			drawCeilings();
			drawFloors();
			drawForward();

		}
		repaint();
	}

	public void addLeftWithNoQuestions()
	{

		int[] xxx;
		int[] yyy;

		for(int a = 0; a<5; a++)
		{

			try
			{
				xxx = new int[]{100+pole*a, 150+pole*a, 150+pole*a, 100+pole*a};
				yyy = new int[]{100+pole*a, 150+pole*a, 650-pole*a, 700-pole*a};
				walls.add(new Wall(xxx, yyy, 50, 255-a*pole, "wall"));
				//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));


			}catch(ArrayIndexOutOfBoundsException e)
			{


			}
		}

	}

	public void addRightWithNoQuestions()
	{
		int[] xxx;
		int[] yyy;

		for(int a = 0; a<5; a++)
		{

			try
			{
				xxx = new int[]{700-pole*a, 650-pole*a, 650-pole*a, 700-pole*a};
				yyy = new int[]{100+pole*a, 150+pole*a, 650-pole*a, 700-pole*a};
				walls.add(new Wall(xxx, yyy, 50, 255-a*pole, "wall"));
				 //walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));


			}catch(ArrayIndexOutOfBoundsException e)
			{


			}
		}
	}

	public void addLeftPathwayRectangles()
	{
		int[] xxx;
		int[] yyy;

		for(int a = 0; a < 5; a++)
		{
			xxx = new int[]{100+pole*a, 150+pole*a, 150+pole*a, 100+pole*a};
			yyy = new int[]{150+pole*a, 150+pole*a, 650-pole*a, 650-pole*a};

			walls.add(new Wall(xxx, yyy, 50, 205-a*pole, "path"));
			//walls.add(new Wall(xxx, yyy, 50, new Color(1, 121, 111)));


		}

	}

	public void addRightPathwayRectangles()
	{
		int[] xxx;
		int[] yyy;

		for(int a = 0; a < 5; a++)
		{
			xxx = new int[]{700-pole*a, 650-pole*a, 650-pole*a, 700-pole*a};
			yyy = new int[]{150+pole*a, 150+pole*a, 650-pole*a, 650-pole*a};

			walls.add(new Wall(xxx, yyy, 50, 205-a*pole, "path"));
			//walls.add(new Wall(xxx, yyy, 50, new Color(1, 121, 111)));


		}

	}

	public void drawLeftWalls()
	{
		int[] xx;// = {100, 150, 150, 100};
		int[] yy;// = {100, 150, 650, 700};

		for(int a=0; a<5; a++)
		{
			try
			{
								//A			B			C			D
				xx = new int[]{100+pole*a, 150+pole*a, 150+pole*a, 100+pole*a};
				yy = new int[]{100+pole*a, 150+pole*a, 650-pole*a, 700-pole*a};
				if(dir == 1 && maze[y-a][x-1].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 2 && maze[y+1][x-a].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 3 && maze[y+a][x+1].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 4 && maze[y-1][x+a].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}





			}catch(ArrayIndexOutOfBoundsException e)
			{


			}

		}
	}

	public void drawRightWalls()
	{
		int[] xx;// = {100, 150, 150, 100};
		int[] yy;// = {100, 150, 650, 700};

		for(int a=0; a<5; a++)
		{
			try
			{
								//A			B			C			D
				xx = new int[]{700-pole*a, 650-pole*a, 650-pole*a, 700-pole*a};
				yy = new int[]{100+pole*a, 150+pole*a, 650-pole*a, 700-pole*a};
				if(dir == 1 && maze[y-a][x+1].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 2 && maze[y-1][x-a].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 3 && maze[y+a][x-1].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 4 && maze[y+1][x+a].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "wallAcc"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}




			}catch(ArrayIndexOutOfBoundsException e)
			{


			}

		}
	}


	public void drawCeilings()
	{
		int[] xx;// = {100, 150, 150, 100};
		int[] yy;// = {100, 150, 650, 700};

		for(int a=0; a<5; a++)
		{
			try
			{					//A			B			C			D
				xx = new int[]{100+pole*a, 700-pole*a, 650-pole*a, 150+pole*a};
				yy = new int[]{100+pole*a, 100+pole*a, 150+pole*a, 150+pole*a};
				walls.add(new Wall(xx, yy, 50, 255-a*pole, "fc"));
				//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));



			}catch(ArrayIndexOutOfBoundsException e)
			{


			}

		}

	}

	public void drawFloors()
	{
		int[] xx;// = {100, 150, 150, 100};
		int[] yy;// = {100, 150, 650, 700};

		for(int a=0; a<5; a++)
		{
			try
			{					//A			B			C			D
				xx = new int[]{ 100+pole*a, 700-pole*a, 650-pole*a, 150+pole*a};
				yy = new int[]{ 700-pole*a, 700-pole*a, 650-pole*a, 650-pole*a};
				walls.add(new Wall(xx, yy, 50, 255-a*pole, "fc"));
				//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));


			}catch(ArrayIndexOutOfBoundsException e)
			{


			}

		}
	}


	public void drawForward()
	{
		int[] xx;
		int[] yy;

		for(int a = 5; a >= 0; a--)
		{
			try{
								//A			B			C			D
				xx = new int[]{150+pole*a, 650-pole*a, 650-pole*a, 150+pole*a};
				yy = new int[]{150+pole*a, 150+pole*a, 650-pole*a, 650-pole*a};
				if(dir == 1 && maze[y-a-1][x].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "front"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));

				}
				if(dir == 2 && maze[y][x-a-1].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "front"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 3 && maze[y+a+1][x].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "front"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}
				if(dir == 4 && maze[y][x+a+1].equals("#"))
				{
					walls.add(new Wall(xx, yy, 50, 255-a*pole, "front"));
					//walls.add(new Wall(xx, yy, 50, new Color(1, 121, 111)));
				}


			}catch(ArrayIndexOutOfBoundsException e)
			{


			}
		}
	}





	public void keyTyped(KeyEvent e)
	{


	}


	public class Wall
	{
		private int[] x;
		private int[] y;
		private int dist;
		private Color color;
		private int colorNum;
		private String shape;
		public Wall(int[] x, int[] y, int dist, Color color)
		{
			this.x=x;
			this.y=y;
			this.dist=dist;
			this.color=color;
		}


		public Wall(int[] x, int[] y, int dist, int colorNum, String shape)
		{
			this.x=x;
			this.y=y;
			this.dist=dist;
			this.colorNum=colorNum;
			this.shape = shape;
		}

		public Polygon getWall()
		{
			return new Polygon(x,y,x.length);
		}
		public Color getColor()
		{
			return color;
		}
		public GradientPaint getPaint()
		{
			if(shape.equals("wallAcc"))
			{
				if((colorNum-dist) > 0)
					return new GradientPaint(x[0], y[1], new Color(colorNum,colorNum,colorNum),x[1],y[1],new Color(colorNum-dist,colorNum-dist,colorNum-dist));
				else
					return new GradientPaint(x[0], y[1], new Color(colorNum,colorNum,colorNum),x[1],y[1],new Color(0, 0, 0));
			}
			else if(shape.equals("fc"))
			{
				if((colorNum-dist) > 0)
					return new GradientPaint(x[0], y[0], new Color(colorNum,colorNum,colorNum),x[0],y[3],new Color(colorNum-dist,colorNum-dist,colorNum-dist));
				else
					return new GradientPaint(x[0], y[0], new Color(colorNum,colorNum,colorNum),x[0],y[3],new Color(0, 0, 0));
			}
			else if(shape.equals("path"))
			{
				colorNum+=dist;
				if(colorNum>255)
					colorNum=255;
				if((colorNum-dist) > 0)
					return new GradientPaint(x[0], y[1], new Color(colorNum-20,colorNum-20,colorNum-20),x[1],y[1],new Color(colorNum-dist,colorNum-dist,colorNum-dist));
				else
					return new GradientPaint(x[0], y[1], new Color(colorNum-20,colorNum-20,colorNum-20),x[1],y[1],new Color(0, 0, 0));
			}
			else if(shape.equals("wall"))
			{
				if((colorNum-dist) > 0)
					return new GradientPaint(x[0], y[1], new Color(colorNum-40,colorNum-40,colorNum-40),x[1],y[1],new Color(colorNum-dist,colorNum-dist,colorNum-dist));
				else
					return new GradientPaint(x[0], y[1], new Color(colorNum-40,colorNum-40,colorNum-40),x[1],y[1],new Color(0, 0, 0));
			}
			colorNum-=dist;
			if(colorNum<0)
				colorNum=0;
			return new GradientPaint(x[0], y[1], new Color(colorNum,colorNum,colorNum),x[0],y[0],new Color(colorNum,colorNum,colorNum));

		}
	}
	public static void main(String[] args)
	{
		maze1 app = new maze1();
	}
}

