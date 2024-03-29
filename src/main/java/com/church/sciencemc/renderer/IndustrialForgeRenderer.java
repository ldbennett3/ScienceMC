package com.church.sciencemc.renderer;

import net.minecraft.client.renderer.Tessellator;

import org.lwjgl.opengl.GL11;

import com.church.sciencemc.ScienceMCMod;
import com.church.sciencemc.blocks.IndustrialForge;
import com.church.sciencemc.models.IndustrialForgeModel;
import com.church.sciencemc.tileentities.TileEntityIndustrialForge;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class IndustrialForgeRenderer extends TileEntitySpecialRenderer {

	// The model of your block
	private final IndustrialForgeModel model;
	private IndustrialForge block;
	private TileEntityIndustrialForge tileEnt;
	public static final ResourceLocation textureLocationOff = new ResourceLocation("sciencemc",
			"textures/blocks/industrialForgeOff.png");
	public static final ResourceLocation textureLocationOn = new ResourceLocation("sciencemc",
			"textures/blocks/industrialForgeOn.png");

	public IndustrialForgeRenderer() {
		this.model = new IndustrialForgeModel();
	}

	private void adjustRotatePivotViaMeta(World world, int x, int y, int z) {
		int meta = world.getBlockMetadata(x, y, z);
		GL11.glPushMatrix();
		GL11.glRotatef(meta * (-90), 0.0F, 0.0F, 1.0F);
		GL11.glPopMatrix();
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double x, double y, double z, float scale) {
		// The PushMatrix tells the renderer to "start" doing something.
		GL11.glPushMatrix();
		// This is setting the initial location.
		GL11.glTranslatef((float) x + 0.5F, (float) y + 1.5F, (float) z + 0.5F);

		bindTexture(textureLocationOff);

		// This rotation part is very important! Without it, your model will
		// render upside-down! And for some reason you DO need PushMatrix again!
		GL11.glPushMatrix();
		GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);

		int rotation = 0;
		switch (te.getBlockMetadata() % 4) {
		case 0:
			rotation = 270;
			break;
		case 1:
			rotation = 0;
			break;
		case 2:
			rotation = 90;
			break;
		case 3:
			rotation = 180;
			break;
		}
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);

		// A reference to your Model file. Again, very important.
		// this.model.render(null, 0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		this.model.render();
		// Tell it to stop rendering for both the PushMatrix's
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}

	// Set the lighting stuff, so it changes it's brightness properly.
	private void adjustLightFixture(World world, int i, int j, int k, Block block) {
		Tessellator tess = Tessellator.instance;
		float brightness = block.getLightOpacity(world, i, j, k);
		int skyLight = world.getLightBrightnessForSkyBlocks(i, j, k, 0);
		int modulousModifier = skyLight % 65536;
		int divModifier = skyLight / 65536;
		tess.setColorOpaque_F(brightness, brightness, brightness);
		OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float) modulousModifier, divModifier);
	}

}
