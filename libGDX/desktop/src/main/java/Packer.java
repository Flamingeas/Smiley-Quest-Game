import com.badlogic.gdx.tools.texturepacker.TexturePacker;

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
