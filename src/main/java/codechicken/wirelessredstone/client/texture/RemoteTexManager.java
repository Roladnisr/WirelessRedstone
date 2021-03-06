package codechicken.wirelessredstone.client.texture;

import codechicken.lib.colour.Colour;
import codechicken.lib.colour.ColourARGB;
import codechicken.lib.texture.TextureDataHolder;
import codechicken.lib.texture.TextureSpecial;
import codechicken.lib.texture.TextureUtils;
import codechicken.lib.texture.TextureUtils.IIconRegister;
import codechicken.wirelessredstone.manager.RedstoneEther;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class RemoteTexManager implements IIconRegister {

    private static Colour texGrad[];
    private static Colour texOff[];
    private static Colour texOn[];
    private static int[] imageData = new int[256];
    private static TextureSpecial[] icons = new TextureSpecial[(RedstoneEther.numcolours + 1) * 2];

    public void registerIcons(TextureMap textureMap) {

        for (int i = 0; i < icons.length; i++) {
            icons[i] = TextureUtils.getTextureSpecial(textureMap, "wrcbe:remote_" + i);
        }

        texOn = TextureUtils.loadTextureColours(new ResourceLocation("wrcbe", "textures/items/remote_on.png"));
        texOff = TextureUtils.loadTextureColours(new ResourceLocation("wrcbe", "textures/items/remote_off.png"));
        texGrad = TextureUtils.loadTextureColours(new ResourceLocation("wrcbe", "textures/items/remote_grad.png"));

        for (int i = 0; i < RedstoneEther.numcolours; i++) {
            processTexture(RedstoneEther.colours[i], false, getIconIndex(i, false));
            processTexture(RedstoneEther.colours[i], true, getIconIndex(i, true));
        }
        processTexture(0xFFFFFFFF, false, getIconIndex(-1, false));
        processTexture(0xFFFFFFFF, true, getIconIndex(-1, true));
    }

    private static void processTexture(int colour, boolean on, int i) {
        mergeTexturesWithColour(new ColourARGB(colour), on);
        icons[i].addTexture(new TextureDataHolder(imageData, 16).copyData());
    }

    public static TextureAtlasSprite getIcon(int colourid, boolean on) {
        return icons[getIconIndex(colourid, on)];
    }

    public static int getIconIndex(int colourid, boolean on) {
        return colourid + 1 + (on ? RedstoneEther.numcolours + 1 : 0);
    }

    private static void mergeTexturesWithColour(Colour texcolour, boolean on) {
        for (int i = 0; i < 256; i++) {
            Colour colour;
            if (texGrad[i].a == 0) {
                colour = on ? texOn[i] : texOff[i];
            } else {
                colour = texGrad[i].copy().multiply(texcolour);
            }

            imageData[i] = colour.argb();
        }
    }
}
