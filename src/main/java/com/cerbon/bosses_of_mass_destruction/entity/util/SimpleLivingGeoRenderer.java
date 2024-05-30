package com.cerbon.bosses_of_mass_destruction.entity.util;

import com.cerbon.bosses_of_mass_destruction.client.render.*;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector4f;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.GeoModelProvider;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public class SimpleLivingGeoRenderer<T extends LivingEntity & IAnimatable> extends GeoEntityRenderer<T> {
    private final IRenderLight<T> brightness;
    private final IBoneLight iBoneLight;
    private final IRenderer<T> renderer;
    private final IRendererWithModel renderWithModel;
    private final IOverlayOverride overlayOverride;
    private final RenderType renderType;
    private final boolean deathRotation;

    private final GeoRenderer<T> renderHelper = new GeoRenderer<>(modelProvider, this::getTextureLocation, this);

    public SimpleLivingGeoRenderer(EntityRendererManager renderManager, AnimatedGeoModel<T> model, IRenderLight<T> brightness, IBoneLight iBoneLight, IRenderer<T> renderer, IRendererWithModel renderWithModel, IOverlayOverride overlayOverride, RenderType renderType, boolean deathRotation) {
        super(renderManager, model);
        this.brightness = brightness;
        this.iBoneLight = iBoneLight;
        this.renderer = renderer;
        this.renderWithModel = renderWithModel;
        this.overlayOverride = overlayOverride;
        this.deathRotation = deathRotation;
        this.renderType = renderType;
    }

    @Override
    public RenderType getRenderType(T animatable, float partialTick, MatrixStack poseStack, @Nullable IRenderTypeBuffer bufferSource, @Nullable IVertexBuilder buffer, int packedLight, ResourceLocation texture) {
        return renderType != null ? renderHelper.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture) : super.getRenderType(animatable, partialTick, poseStack, bufferSource, buffer, packedLight, texture);
    }

    @Override
    protected int getBlockLightLevel(@Nonnull T entity, @Nonnull BlockPos pos) {
        return brightness != null ? brightness.getBlockLight(entity, pos) : super.getBlockLightLevel(entity, pos);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        int packedLight1 = iBoneLight != null ? iBoneLight.getLightForBone(bone, packedLight) : packedLight;
        Vector4f color = new Vector4f(red, green, blue, alpha);
        Vector4f newColor = iBoneLight != null ? iBoneLight.getColorForBone(bone, color) :color;
        super.renderRecursively(
                bone,
                poseStack,
                buffer,
                packedLight1,
                packedOverlay,
                newColor.x(), newColor.y(), newColor.z(), newColor.w()
        );
    }

    @Override
    public void render(
            @Nonnull T entity,
            float entityYaw,
            float partialTick,
            @Nonnull MatrixStack poseStack,
            @Nonnull IRenderTypeBuffer bufferSource,
            int packedLight
    ) {
        if (renderer != null) renderer.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }

    @Override
    public void render(GeoModel model, T animatable, float partialTick, RenderType type, MatrixStack poseStack, @Nullable IRenderTypeBuffer bufferSource, @Nullable IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        int packetOverlay = overlayOverride != null ? overlayOverride.getOverlay() : packedOverlay;
        renderHelper.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packetOverlay, red, green, blue, alpha);
        if (renderWithModel != null) renderWithModel.render(model, partialTick, poseStack, bufferSource, packedLight, packetOverlay, red, green, blue, alpha);
    }

    @Override
    public int getPackedOverlay(LivingEntity entity, float u) {
        return overlayOverride != null ? overlayOverride.getOverlay() : super.getPackedOverlay(entity, u);
    }

//    @Override
//    public void renderCube(GeoCube cube, PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//        RenderUtils.moveToPivot(cube, poseStack);
//        RenderUtils.rotate(cube, poseStack);
//        RenderUtils.moveBackFromPivot(cube, poseStack);
//        Matrix3f matrix3f = poseStack.last().normal();
//        Matrix4f matrix4f = poseStack.last().pose();
//
//        for (GeoQuad quad : cube.quads){
//            if (quad == null) continue;
//
//            Vector3f normal = quad.normal.copy();
//            normal.transform(matrix3f);
//            for (GeoVertex vertex : quad.vertices){
//                Vector4f vector4f = new Vector4f(vertex.position.x(), vertex.position.y(), vertex.position.z(), 1.0f);
//                vector4f.transform(matrix4f);
//                buffer.vertex(
//                        vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha,
//                        vertex.textureU, vertex.textureV, packedOverlay, packedLight, normal.x(), normal.y(),
//                        normal.z());
//            }
//        }
//    }

    @Override
    protected float getDeathMaxRotation(T animatable) {
        return deathRotation ? 90f : 0f;
    }

    private static class GeoRenderer<T extends LivingEntity & IAnimatable> implements IGeoRenderer<T> {
        private final AnimatedGeoModel<T> geoModel;
        private final Function<T, ResourceLocation> textureLocation;
        private final SimpleLivingGeoRenderer<T> renderer;

        private IRenderTypeBuffer provider;

        public GeoRenderer(AnimatedGeoModel<T> geoModel, Function<T, ResourceLocation> textureLocation, SimpleLivingGeoRenderer<T> renderer) {
            this.geoModel = geoModel;
            this.textureLocation = textureLocation;
            this.renderer = renderer;
        }

        @Override
        public GeoModelProvider<T> getGeoModelProvider() {
            return geoModel;
        }

        @Override
        public void renderRecursively(GeoBone bone, MatrixStack poseStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
            renderer.renderRecursively(bone, poseStack, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        }

        @Override
        public void setCurrentRTB(IRenderTypeBuffer bufferSource) {
            provider = bufferSource;
        }

        @Override
        public IRenderTypeBuffer getCurrentRTB() {
            return provider;
        }

        @Override
        public ResourceLocation getTextureLocation(T t) {
            return textureLocation.apply(t);
        }
    }
}
