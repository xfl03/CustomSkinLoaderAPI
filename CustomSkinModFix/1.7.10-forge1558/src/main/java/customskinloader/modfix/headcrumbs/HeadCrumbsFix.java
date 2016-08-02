package customskinloader.modfix.headcrumbs;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import customskinloader.CustomSkinLoader;
import ganymedes01.headcrumbs.Headcrumbs;
import ganymedes01.headcrumbs.utils.TextureUtils;
import ganymedes01.headcrumbs.utils.helpers.EtFuturumHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.resources.SkinManager.SkinAvailableCallback;
import net.minecraft.util.ResourceLocation;

public class HeadCrumbsFix {
	private static final LoadingCache<GameProfile, Map<Type, MinecraftProfileTexture>> skinCacheLoader;
	
	static{
		skinCacheLoader = CacheBuilder.newBuilder().expireAfterAccess(15L, TimeUnit.SECONDS).<GameProfile, Map<Type, MinecraftProfileTexture>>build(new CacheLoader<GameProfile, Map<Type, MinecraftProfileTexture>>()
        {
            public Map<Type, MinecraftProfileTexture> load(GameProfile p_load_1_) throws Exception
            {
                return Minecraft.getMinecraft().func_152347_ac().getTextures(p_load_1_, false);
            }
        });
	}
	
	@SuppressWarnings("unchecked")
	public static void handleClientSide(GameProfile profile){
		//FMLRelaunchLog.info("[HeadCrumbFix] 2333");
		Map<Type, MinecraftProfileTexture> map=skinCacheLoader.getUnchecked(profile);
		if((map==null||map.isEmpty())&&profile.getName()!=null)
			map=CustomSkinLoader.loadProfile(profile);
		if(map==null||map.isEmpty())
			return;
		SkinManager skinManager = Minecraft.getMinecraft().func_152342_ad();
		
		for (Type type : Type.values())
			if (map.containsKey(type))
				skinManager.func_152789_a(map.get(type), type, getCallback(profile,type));
	}
	public static SkinAvailableCallback getCallback(final GameProfile profile,Type type) {
		if (Headcrumbs.use18PlayerModel)
			return EtFuturumHelper.getSkinDownloadCallback(profile.getName());
		else
			return new SkinAvailableCallback() {
				@Override
				public void func_152121_a(Type texType, ResourceLocation texture) {
					TextureUtils.textures.get(texType).put(profile.getName(), texture);
				}
			};
	}
}
