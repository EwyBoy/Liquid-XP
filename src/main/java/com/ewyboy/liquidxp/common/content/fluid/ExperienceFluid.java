package com.ewyboy.liquidxp.common.content.fluid;

import com.ewyboy.bibliotheca.common.content.fluid.BaseFluid;
import com.ewyboy.bibliotheca.common.loaders.ContentLoader;
import com.ewyboy.liquidxp.common.content.item.ExperienceBucket;
import net.minecraft.item.BucketItem;

public abstract class ExperienceFluid extends BaseFluid implements ContentLoader.IHasCustomBucket {

    protected ExperienceFluid(Properties properties) {
        super(properties);
    }

    private static final ExperienceBucket bucket = new ExperienceBucket();

    public static ExperienceBucket getBucket() {
        return bucket;
    }

    @Override
    public BucketItem getCustomBucketItem() {
        return bucket;
    }

}
