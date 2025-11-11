package settings.screen;

public class SettingsScreen implements Screen {

    private float volume;

    public SettingsScreen() {
        this.volume = 0.5f;
    }

    public void adjustVolume(float value) {
        this.volume = Math.max(0f, Math.min(1f, value)); // borné entre 0 et 1
        System.out.println("Volume ajusté à " + (volume * 100) + "%");
    }

    @Override
    public void show() {
        System.out.println("Affichage du menu des paramètres");
    }

    @Override
    public void update(float delta) {
    }

    @Override
    public void render(float delta) {
        System.out.println("Rendu du SettingsScreen");
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        System.out.println("Fermeture de l'écran des paramètres");
    }

    @Override
    public void dispose() {
        System.out.println("Nettoyage du SettingsScreen");
    }
    public float getVolume() {
        return volume;
    }
}
