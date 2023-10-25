import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	static final int WIDTH = 900;
	static final int HEIGHT = 600;
	static final int UNIT_SIZE = 20;
	static final int NUMBER_OF_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);
	final int x[] = new int[NUMBER_OF_UNITS];
	final int y[] = new int[NUMBER_OF_UNITS];
	int length = 5;
	int foodEaten;
	int foodX;
	int foodY;
	char direction = 'D';
	boolean running = false;
	Random random;
	Timer timer = null;
	JButton restartButton;
	JButton easyButton;
	JButton mediumButton;
	JButton hardButton;

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
		this.setBackground(Color.decode("#28282B"));
		this.setLayout(null);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		restartButton = new JButton("Restart");
		restartButton.setFocusable(false);
		restartButton.setVisible(false);
		this.add(restartButton);
		easyButton = new JButton("Easy");
		mediumButton = new JButton("Medium");
		hardButton = new JButton("Hard");
		easyButton.setBounds(275, 320, 100, 30);
		easyButton.setBackground(Color.decode("#61B653"));
		easyButton.setForeground(Color.WHITE);
		this.add(easyButton);
		mediumButton.setBounds(400, 320, 100, 30);
		mediumButton.setBackground(Color.decode("#F8A01A"));
		mediumButton.setForeground(Color.WHITE);
		this.add(mediumButton);
		hardButton.setBounds(525, 320, 100, 30);
		hardButton.setBackground(Color.decode("#EB5352"));
		hardButton.setForeground(Color.WHITE);
		this.add(hardButton);

		restartButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				restartGame();
			}
		});

		easyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDifficulty(100);
			}
		});

		mediumButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDifficulty(70);
			}
		});

		hardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setDifficulty(40);
			}
		});
		running = true;
	}

	public void setDifficulty(int delay) {
		timer = new Timer(delay, this);
		easyButton.setVisible(false);
		mediumButton.setVisible(false);
		hardButton.setVisible(false);
		play();
	}

	public void play() {
		addFood();
		timer.start();
	}

	public void restartGame() {
		closeFrame();
		new GameFrame();
	}

	public void closeFrame() {
		Window window = SwingUtilities.windowForComponent(this);
		if (window instanceof JFrame) {
			((JFrame) window).dispose();
		}
	}

	@Override
	public void paintComponent(Graphics graphics) {
		super.paintComponent(graphics);
		draw(graphics);
	}

	public void move() {
		for (int i = length; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}
		if (direction == 'L') {
			x[0] = x[0] - UNIT_SIZE;
		} else if (direction == 'R') {
			x[0] = x[0] + UNIT_SIZE;
		} else if (direction == 'U') {
			y[0] = y[0] - UNIT_SIZE;
		} else {
			y[0] = y[0] + UNIT_SIZE;
		}
	}

	public void checkFood() {
		if (x[0] == foodX && y[0] == foodY) {
			length++;
			foodEaten++;
			addFood();
		}
	}

	public void draw(Graphics graphics) {
		if (running && timer!=null) {
			graphics.setColor(new Color(240, 70, 25));
			graphics.fillOval(foodX, foodY, UNIT_SIZE, UNIT_SIZE);
			graphics.setColor(new Color(255, 255, 200));
			graphics.fillRect(x[0], y[0], UNIT_SIZE, UNIT_SIZE);

			for (int i = 1; i < length; i++) {
				graphics.setColor(new Color(81, 240, 68));
				graphics.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
			}
			graphics.setColor(Color.white);
			graphics.setFont(new Font("Segoe UI", Font.BOLD, 20));
			FontMetrics metrics = getFontMetrics(graphics.getFont());
			graphics.drawString("Score : " + foodEaten, (WIDTH - metrics.stringWidth("Score : " + foodEaten)) / 2,
					graphics.getFont().getSize());
		} else if(running){
			gameStart(graphics);
		} else {
			gameOver(graphics);
		}
	}

	public void addFood() {
		int randomX, randomY;
		do {
			randomX = random.nextInt((WIDTH / UNIT_SIZE)) * UNIT_SIZE;
			randomY = random.nextInt((HEIGHT / UNIT_SIZE)) * UNIT_SIZE;
		} while (isFoodOnSnake(randomX, randomY));
		foodX = randomX;
		foodY = randomY;
	}

	private boolean isFoodOnSnake(int testX, int testY) {
		for (int i = 0; i < length; i++) {
			if (testX == x[i] && testY == y[i]) {
				return true;
			}
		}
		return false;
	}

	public void checkHit() {
		for (int i = length; i > 0; i--) {
			if (x[0] == x[i] && y[0] == y[i]) {
				running = false;
			}
		}
		if (x[0] < 0 || x[0] >= WIDTH || y[0] < 0 || y[0] >= HEIGHT) {
			running = false;
		}
		if (!running) {
			timer.stop();
		}
	}

	public void gameStart(Graphics graphics) {
		graphics.setColor(Color.CYAN);
		graphics.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Start Game", (WIDTH - metrics.stringWidth("Start Game")) / 2, HEIGHT / 2-40);

		graphics.setColor(Color.GREEN);
		graphics.setFont(new Font("Segoe UI", Font.BOLD, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Pick your level of difficulty", (WIDTH - metrics.stringWidth("Pick your level of difficulty")) / 2, HEIGHT/2+10 );

		graphics.setColor(Color.white);
		graphics.setFont(new Font("Segoe UI", Font.BOLD, 22));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Instructions", (WIDTH - metrics.stringWidth("Instructions")) / 2, HEIGHT / 2+105);
		graphics.setColor(Color.white);
		graphics.setFont(new Font("Segoe UI", Font.PLAIN, 17));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("> Use Arrow keys to change the direction.", (WIDTH - metrics.stringWidth("> Use Arrow keys to change the direction.")) / 2, HEIGHT / 2+130);
		graphics.drawString("> Use 'P' to pause the game.", (WIDTH - metrics.stringWidth("> Use 'P' to pause the game.")) / 2, HEIGHT / 2+150);
		graphics.drawString("> Use 'R' to resume the game.", (WIDTH - metrics.stringWidth("> Use 'R' to resume the game.")) / 2, HEIGHT / 2+170);
		graphics.drawString("> Use 'Enter' to restart the game.", (WIDTH - metrics.stringWidth("> Use 'Enter' to restart the game.")) / 2, HEIGHT / 2+190);
	}

	public void gameOver(Graphics graphics) {
		graphics.setColor(Color.red);
		graphics.setFont(new Font("Franklin Gothic Medium", Font.BOLD, 75));
		FontMetrics metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Game Over!", (WIDTH - metrics.stringWidth("Game Over!")) / 2, HEIGHT / 2-40);

		graphics.setColor(Color.white);
		graphics.setFont(new Font("Segoe UI", Font.BOLD, 30));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Score : " + foodEaten, (WIDTH - metrics.stringWidth("Score : " + foodEaten)) / 2, HEIGHT / 2);

		graphics.setColor(Color.GREEN);
		graphics.setFont(new Font("Segoe UI", Font.BOLD, 25));
		metrics = getFontMetrics(graphics.getFont());
		graphics.drawString("Press Enter to Restart", (WIDTH - metrics.stringWidth("Press Enter to Restart")) / 2, HEIGHT / 2+40);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (running) {
			move();
			checkFood();
			checkHit();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {

				case KeyEvent.VK_LEFT:
					if (direction != 'R') {
						direction = 'L';
					}
					break;

				case KeyEvent.VK_RIGHT:
					if (direction != 'L') {
						direction = 'R';
					}
					break;

				case KeyEvent.VK_UP:
					if (direction != 'D') {
						direction = 'U';
					}
					break;

				case KeyEvent.VK_DOWN:
					if (direction != 'U') {
						direction = 'D';
					}
					break;

				case KeyEvent.VK_ENTER:
					if (!running) {
						restartButton.doClick();
					}
					break;

			}
		}
	}
}