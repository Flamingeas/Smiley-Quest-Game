import com.badlogic.gdx.tools.texturepacker.TexturePacker;

/**
 * Packs textures from the "intro" folder into a single atlas using libGDX TexturePacker.
 * Configures settings such as max size, aliasing, edge padding, and duplicate padding.
 * Outputs the atlas into the specified assets directory.
 */
public class Packer {
    public static void main(String[] args) throws Exception {

        System.out.println("Working dir = " + new java.io.File(".").getAbsolutePath());

        TexturePacker.Settings settings = new TexturePacker.Settings();
        settings.maxWidth = 4096;
        settings.maxHeight = 4096;
        settings.alias = false;
        settings.edgePadding = false;
        settings.duplicatePadding = false;

        TexturePacker.process(
                settings,
                "libGDX/core/assets/intro",
                "libGDX/core/assets/",
                "intro"
        );

        System.out.println("ATLAS GENERATED!");
    }
}
