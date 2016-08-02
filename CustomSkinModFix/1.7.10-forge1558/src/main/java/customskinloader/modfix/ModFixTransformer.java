package customskinloader.modfix;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import customskinloader.modfix.headcrumbs.HeadCrumbsTransformer;
import net.minecraft.launchwrapper.IClassTransformer;

/*
 * Some code is from RecursiveG/UniSkinMod
 * Source: https://github.com/RecursiveG/UniSkinMod/blob/1.9.4/src/main/java/org/devinprogress/uniskinmod/coremod/BaseAsmTransformer.java
 * License: GPLv2
 * */
public class ModFixTransformer implements IClassTransformer {

    public interface IMethodTransformer {
        void transform(MethodNode mn);
    }
    
    private Map<String, Map<String, IMethodTransformer>> map;
	public ModFixTransformer(){
		FMLRelaunchLog.info("[ModFixTransformer] Loading");
		map = new HashMap<String, Map<String, IMethodTransformer>>();
		hookMethod("ganymedes01.headcrumbs.network.packet.TextureSendPacket",
				"handleClientSide",
				"(Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)V",
				new HeadCrumbsTransformer.handleClientSideTransformer());
	}
	protected void hookMethod(String className, String methodName, String desc, IMethodTransformer targetTransformer) {
		FMLRelaunchLog.info("[ModFixTransformer] REGISTERED");
        if (!map.containsKey(className))
            map.put(className, new HashMap<String, IMethodTransformer>());
        map.get(className).put(methodName+desc, targetTransformer);
    }
	
	@Override
	public byte[] transform(String className, String transformedName, byte[] bytes) {
		if (!map.containsKey(className)) return bytes;
		//FMLRelaunchLog.info("[ModFixTransformer] !!! "+className);
        Map<String, IMethodTransformer> transMap = map.get(className);
        ClassReader cr = new ClassReader(bytes);
        ClassNode cn = new ClassNode();
        cr.accept(cn, 0);

        List<MethodNode> ml = new ArrayList<MethodNode>();
        ml.addAll(cn.methods);
        for (MethodNode mn : ml) {
            String methodName = mn.name;
            String methodDesc = mn.desc;
            //FMLRelaunchLog.info("[ModFixTransformer] ! "+methodName+" "+methodDesc);
            if (transMap.containsKey(methodName + methodDesc)) {
                try {
                    FMLRelaunchLog.info("[ModFixTransformer] Transforming method %s in class %s", methodName + methodDesc,  className);
                    transMap.get(methodName + methodDesc).transform(mn);
                    FMLRelaunchLog.info("[ModFixTransformer] Successfully transformed method %s in class %s", methodName + methodDesc, className);
                } catch (Exception e) {
                    FMLRelaunchLog.warning("[ModFixTransformer] An error happened when transforming method %s in class %s. The whole class was not modified.", methodName + methodDesc,  className);
                    e.printStackTrace();
                    return bytes;
                }
            }
        }

        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES | ClassWriter.COMPUTE_MAXS);
        cn.accept(cw);
        return cw.toByteArray();
	}
}
