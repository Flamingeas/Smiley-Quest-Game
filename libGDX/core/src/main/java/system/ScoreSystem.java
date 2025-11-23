package system;

public class ScoreSystem {

    private int currentScore = 0;

    public ScoreSystem() {}

    /**
     * Appelé par GameWorld quand un événement survient.
     */
    public void onEvent(GameEvent e) {
        switch (e.getType()) {

            case "ITEM_COLLECTED" -> {
                int value = (int) e.getData();
                addScore(value);
            }

            case "PLAYER_HIT" -> {
                addScore(-30);
            }

            case "CHEST_FOUND" -> {
                addScore(100);
            }

            case "ENEMY_KILLED" -> {
                addScore(20);
            }

            case "PLAYER_DEAD" -> {
            }

            case "WEAPON_EQUIPPED" -> {
            }
        }
    }

    /** Ajoute un score, positif ou négatif */
    public int addScore(int points) {
        currentScore += points;
        System.out.println("[Score] " + (points >= 0 ? "+" : "") + points + " → total = " + currentScore);
        return currentScore;
    }

    public int getScore() {
        return currentScore;
    }

    public void reset() {
        currentScore = 0;
    }
}