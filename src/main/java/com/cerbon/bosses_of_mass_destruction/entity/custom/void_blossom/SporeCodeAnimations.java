package com.cerbon.bosses_of_mass_destruction.entity.custom.void_blossom;

import com.cerbon.bosses_of_mass_destruction.entity.util.ICodeAnimations;
import com.cerbon.bosses_of_mass_destruction.projectile.SporeBallProjectile;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SporeCodeAnimations implements ICodeAnimations<SporeBallProjectile> {

    public SporeCodeAnimations(){}

    @Override
    public void animate(SporeBallProjectile animatable, AnimationState<?> data, GeoModel<SporeBallProjectile> geoModel) {
        float pitch = animatable.impacted ? animatable.getXRot() : Mth.rotLerp(data.getPartialTick(), animatable.getXRot() - 5, animatable.getXRot());

        BakedGeoModel model = geoModel.getBakedModel(geoModel.getModelResource(animatable));
        model.getBone("root1").ifPresent(it -> it.setRotX((float) Math.toRadians(pitch)));
    }
}
