public enum LevelType {

    LEVEL_1(3, "assets/wall1.png", LevelBoards.board1),
    LEVEL_2(2, "assets/wall2.png", LevelBoards.board2),
    LEVEL_3(1, "assets/wall3.png", LevelBoards.board3);

    private final int lives;
    private final String wallAsset;
    private final String[] board;

    LevelType(int lives, String wallAsset, String[] board) {
        this.lives = lives;
        this.wallAsset = wallAsset;
        this.board = board;
    }

    public int getLives() { return lives; }
    public String getWallAsset() { return wallAsset; }
    public String[] getBoard() { return board; }

}
