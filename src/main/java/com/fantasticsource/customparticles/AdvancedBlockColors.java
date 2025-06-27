package com.fantasticsource.customparticles;

import com.fantasticsource.tools.datastructures.Color;
import com.fantasticsource.tools.datastructures.ColorImmutable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;

import java.util.LinkedHashMap;

public class AdvancedBlockColors
{
    protected static final LinkedHashMap<IBlockState, Color> TEXTURE_COLORS = new LinkedHashMap<>();


    public static Color getBlockColor(BlockPos blockPos, IBlockState state)
    {
        Color color = getTextureColor(state);


        Minecraft minecraft = Minecraft.getMinecraft();
        int tint = minecraft.getBlockColors().colorMultiplier(state, minecraft.world, blockPos, 0);
        if (tint != -1)
        {
            Color texColor = color;
            color = new Color(tint, true);
            color.setRF(color.rf() * texColor.rf());
            color.setGF(color.gf() * texColor.gf());
            color.setBF(color.bf() * texColor.bf());
        }


        return color;
    }


    public static ColorImmutable getTextureColor(IBlockState state)
    {
        return (ColorImmutable) TEXTURE_COLORS.computeIfAbsent(state, o -> getTextureColorInternal(state, true));
    }

    public static Color getTextureColorInternal(IBlockState state)
    {
        return getTextureColorInternal(state, false);
    }

    public static Color getTextureColorInternal(IBlockState state, boolean makeImmutable)
    {
        Minecraft minecraft = Minecraft.getMinecraft();
        TextureAtlasSprite sprite = minecraft.getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
        int w = sprite.getIconWidth(), h = sprite.getIconHeight();
        int c, frames = sprite.getFrameCount();
        float r = 0, g = 0, b = 0, a, divisor = 0;
        BlockRenderLayer renderLayer = state.getBlock().getBlockLayer();
        for (int i = 0; i < frames; i++)
        {
            int[][] rgbaData = sprite.getFrameTextureData(i);
            for (int x = 0; x < w; x++)
            {
                for (int y = 0; y < h; y++)
                {
                    c = rgbaData[0][y * w + x];
                    a = renderLayer == BlockRenderLayer.SOLID ? 1 : (c >>> 24) / 255f;
                    if ((renderLayer == BlockRenderLayer.CUTOUT || renderLayer == BlockRenderLayer.CUTOUT_MIPPED) && a <= 0.5) continue; //Cutout threshold: 127 / 255 <-> 128 / 255


                    r += (float) ((c >> 16) & 255) * a;
                    g += (float) ((c >> 8) & 255) * a;
                    b += (float) (c & 255) * a;
                    divisor += a;
                }
            }
        }
        divisor *= 255;
        return makeImmutable ? new ColorImmutable(r / divisor, g / divisor, b / divisor, 1f) : new Color(r / divisor, g / divisor, b / divisor, 1f);
    }
}
