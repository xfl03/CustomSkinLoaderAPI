package customskinloader.modfix.headcrumbs;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.InsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.VarInsnNode;

import customskinloader.modfix.ModFixTransformer;

public class HeadCrumbsTransformer {

	/*
       className : ganymedes01.headcrumbs.network.packet.TextureSendPacket
       methodName : handleClientSide
       desc : (Lnet/minecraft/world/World;Lnet/minecraft/entity/player/EntityPlayer;)V
    */
	public static class handleClientSideTransformer implements ModFixTransformer.IMethodTransformer{
		@Override
		public void transform(MethodNode mn) {
			mn.instructions.clear();
			mn.instructions.add(new VarInsnNode(Opcodes.ALOAD, 0));
			mn.instructions.add(new FieldInsnNode(Opcodes.GETFIELD, "ganymedes01/headcrumbs/network/packet/TextureSendPacket", "profile", "Lcom/mojang/authlib/GameProfile;"));
			mn.instructions.add(new MethodInsnNode(Opcodes.INVOKESTATIC,
	                "customskinloader/modfix/headcrumbs/HeadCrumbsFix", "handleClientSide",
	                "(Lcom/mojang/authlib/GameProfile;)V", false));
            mn.instructions.add(new InsnNode(Opcodes.RETURN));
		}
	}
}
