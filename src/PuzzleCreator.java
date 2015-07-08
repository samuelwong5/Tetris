import java.util.Stack;

public class PuzzleCreator {

	/* NOTE: AUTO-FORMATTING IN ECLIPSE WILL 
	 * DESTROY THE DIAGRAMS BELOW.
	 * 
	 * I : XXXX
	 * 
	 * J : 00X 
	 *     XXX
	 * 
	 * L : X00 
	 *     XXX
	 * 
	 * O : XX0 
	 *     XX0
	 * 
	 * S : 0XX 
	 *     XX0
	 * 
	 * T : 0X0 
	 *     XXX
	 * 
	 * Z : XX0 
	 *     0XX
	 */
	
	private static Direction[] dir = { Direction.UP, Direction.DOWN,
			Direction.LEFT, Direction.RIGHT };
	private static int[][] tetrisBlocks = new int[7][];
	private static Stack<String> history;
	private static Stack<Integer> rotations;
	private static Stack<Integer> block;
	private static Stack<Integer> xCoor;
	private static Stack<Integer> yCoor;

	public static void main(String[] args) {
		int[][] pattern = { { 0, 1, 1, 1, 0 }, { 1, 1, 0, 1, 1 },
				{ 1, 0, 0, 0, 1 }, { 1, 1, 0, 1, 1 }, { 0, 1, 1, 1, 0 } };
		int[][] currentPattern = null;
		int[] i = { 3, 3, 3 };
		int[] j = { 1, 3, 3 };
		int[] l = { 3, 3, 0 };
		int[] o = { 1, 3, 0 };
		int[] s = { 3, 0, 3 };
		// int[] t = {3,0,1,3}; // Hardest to fit
		int[] z = { 3, 1, 3 };
		PuzzleCreator.history = new Stack<String>();
		PuzzleCreator.rotations = new Stack<Integer>();
		PuzzleCreator.block = new Stack<Integer>();
		PuzzleCreator.xCoor = new Stack<Integer>();
		PuzzleCreator.yCoor = new Stack<Integer>();
		PuzzleCreator.tetrisBlocks = new int[][] { i, j, l, o, s, z };
		currentPattern = fit(pattern);
		if (currentPattern != null) {
			int xx = 1;
			int[][] current = cloneMatrix(pattern);
			while (PuzzleCreator.block.size() > 0) {
				int[] b = tetrisBlocks[block.pop()].clone();
				int r = rotations.pop();
				for (int a = 0; a < b.length; a++) {
					for (int c = 0; c < r; c++) {
						b[a] = rotate(b[a]);
					}
				}
				int x = xCoor.pop();
				int y = yCoor.pop();
				Direction d;
				current[y][x] = xx;
				d = dir[b[0]];
				x += d.getX();
				y += d.getY();

				current[y][x] = xx;
				d = dir[b[1]];
				x += d.getX();
				y += d.getY();

				current[y][x] = xx;
				d = dir[b[2]];
				x += d.getX();
				y += d.getY();

				current[y][x] = xx;
				xx++;
			}
			printMatrix(current);
		} else {
			System.out.println("Fail");
		}
	}

	private static int[][] fit(int[][] currPattern) {
		int[][] hold;
		for (int i = 0; i < currPattern.length; i++) {
			for (int j = 0; j < currPattern[i].length; j++) {
				if (currPattern[i][j] != 0) {
					for (int l = 0; l < PuzzleCreator.tetrisBlocks.length; l++) {
						int[] block = PuzzleCreator.tetrisBlocks[l].clone();
						for (int k = 0; k < 3; k++) {
							hold = place(currPattern, block, j, i);
							if (hold != null) {
								PuzzleCreator.history.push("Added block: " + l
										+ " (rot: " + k + " | " + block[0]
										+ "," + block[1] + "," + block[2]
										+ ") at (" + i + "," + j + ")");
								xCoor.push(j);
								yCoor.push(i);
								PuzzleCreator.block.push(l);
								rotations.push(k);
								int sum = 0;
								for (int a = 0; a < hold.length; a++) {
									for (int b = 0; b < hold[a].length; b++) {
										sum += hold[a][b];
									}
								}
								if (sum == 0) {
									return hold;
								}
								hold = fit(hold);
								if (hold != null) {
									return hold;
								} else {
									PuzzleCreator.history.pop();
									xCoor.pop();
									yCoor.pop();
									PuzzleCreator.block.pop();
									rotations.pop();
								}
							}
							for (int z = 0; z < 3; z++) {
								block[z] = rotate(block[z]);
							}

						}
					}
				}
			}
		}
		return null;
	}

	private static int rotate(int x) {
		switch (x) {
		case 0:
			return 3;
		case 1:
			return 2;
		case 2:
			return 0;
		case 3:
			return 1;
		default:
			return -1;
		}
	}

	private static int[][] place(int[][] currPattern, int[] block, int xC,
			int yC) {
		int[][] current = cloneMatrix(currPattern);
		int x = xC;
		int y = yC;
		Direction d;
		if (y >= 0 && x >= 0 && y < currPattern.length
				&& x < currPattern.length && current[y][x] == 1) {
			current[y][x] = 0;
			d = dir[block[0]];
			x += d.getX();
			y += d.getY();
			if (y >= 0 && x >= 0 && y < currPattern.length
					&& x < currPattern.length && current[y][x] == 1) {
				current[y][x] = 0;
				d = dir[block[1]];
				x += d.getX();
				y += d.getY();
				if (y >= 0 && x >= 0 && y < currPattern.length
						&& x < currPattern.length && current[y][x] == 1) {
					current[y][x] = 0;
					d = dir[block[2]];
					x += d.getX();
					y += d.getY();
					if (y >= 0 && x >= 0 && y < currPattern.length
							&& x < currPattern.length && current[y][x] == 1) {
						current[y][x] = 0;
						// System.out.println("OLD: ");
						// PuzzleCreator.printMatrix(currPattern);
						// System.out.println("NEW: ");
						// PuzzleCreator.printMatrix(current);
						return current;
					} else {
						return null;
					}
				} else {
					return null;
				}
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private static int[][] cloneMatrix(int[][] matrix) {
		int[][] clone = new int[matrix.length][];
		for (int i = 0; i < matrix.length; i++) {
			clone[i] = new int[matrix[i].length];
			for (int j = 0; j < matrix[i].length; j++) {
				clone[i][j] = matrix[i][j];
			}
		}
		return clone;
	}

	private static void printMatrix(int[][] matrix) {
		for (int i = 0; i < matrix.length; i++) {
			for (int j = 0; j < matrix[i].length; j++) {
				System.out.print(matrix[i][j] + " ");
			}
			System.out.println(" ");
		}
		System.out.println(" ");
	}

	public enum Direction {
		UP(0, 1), DOWN(0, -1), LEFT(-1, 0), RIGHT(1, 0);

		int x;
		int y;

		private Direction(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}
}
