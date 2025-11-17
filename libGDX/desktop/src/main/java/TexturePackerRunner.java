import com.badlogic.gdx.tools.texturepacker.TexturePacker;

public class TexturePackerRunner {
    public static void main(String[] args) throws Exception {

        TexturePacker.Settings settings = new TexturePacker.Settings();

        settings.maxWidth = 4096;   // ← accepte les grandes images
        settings.maxHeight = 4096;
        settings.paddingX = 2;
        settings.paddingY = 2;
        settings.duplicatePadding = true;
        settings.useIndexes = false;

        System.out.println("📦 Packing atlas...");

        TexturePacker.process(
                settings,
                "../core/src/main/resources/intro",   // input
                "../core/src/main/resources/intro",   // output
                "intro"                                // nom
        );

        System.out.println("✔ Atlas généré !");
    }
}
