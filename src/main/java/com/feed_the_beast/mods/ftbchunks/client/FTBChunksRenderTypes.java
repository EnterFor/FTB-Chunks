package com.feed_the_beast.mods.ftbchunks.client;

import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.OptionalDouble;

/**
 * @author LatvianModder
 */
public class FTBChunksRenderTypes extends RenderState
{
	public static final ResourceLocation WAYPOINT_BEAM = new ResourceLocation("ftbchunks:textures/waypoint_beam.png");

	public static final RenderType WAYPOINTS_DEPTH = RenderType.create("ftbchunks_waypoints_depth", DefaultVertexFormats.POSITION_COLOR_TEX, GL11.GL_QUADS, 256, RenderType.State.builder()
			.setLineState(new LineState(OptionalDouble.empty()))
			.setLayeringState(NO_LAYERING)
			.setTextureState(new TextureState(WAYPOINT_BEAM, true, false))
			.setTransparencyState(TRANSLUCENT_TRANSPARENCY)
			.setAlphaState(DEFAULT_ALPHA)
			.setWriteMaskState(COLOR_WRITE)
			.setCullState(CULL)
			.setShadeModelState(RenderState.SMOOTH_SHADE)
			.createCompositeState(false));

	private FTBChunksRenderTypes(String s, Runnable r0, Runnable r1)
	{
		super(s, r0, r1);
	}
}