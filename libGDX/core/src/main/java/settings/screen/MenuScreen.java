package settings.screen;

public class MenuScreen implements Screen {

    private String title = "Smiley Quest";

    public MenuScreen() {}

    public void handleInput() {
        System.out.println("Entrée dans le menu : gérer les inputs ici.");
    }

    @Override
    public void show() {
        System.out.println("Affichage du Menu principal : " + title);
    }

    @Override
    public void update(float delta) {
        handleInput();
    }

    @Override
    public void render(float delta) {
        System.out.println("Rendu du MenuScreen...");
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        System.out.println("Fermeture du menu principal.");
    }

    @Override
    public void dispose() {
        System.out.println("Nettoyage du MenuScreen.");
    }
}
