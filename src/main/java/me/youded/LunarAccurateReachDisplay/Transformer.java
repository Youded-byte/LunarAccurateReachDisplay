package me.youded.LunarAccurateReachDisplay;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.Arrays;

public class Transformer implements ClassFileTransformer {
    private boolean round = false;

    Transformer(String args){
        if(args != null && !args.isBlank() && args.equals("round"))
            this.round = true;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
            ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classfileBuffer == null || classfileBuffer.length == 0) {
            return new byte[0];
        }

        if (!className.startsWith("lunar/")) {
            return classfileBuffer;
        }

        ClassReader cr = new ClassReader(classfileBuffer);
        if (cr.getInterfaces().length == 0 && cr.getSuperName().startsWith("lunar/")) {
            ClassNode cn = new ClassNode();

            cr.accept(cn, 0);
            Boolean hasString = false;

            for (MethodNode method : cn.methods) {
                Boolean foundstring = Arrays.stream(method.instructions.toArray())
                        .filter(LdcInsnNode.class::isInstance)
                        .map(LdcInsnNode.class::cast)
                        .map(inst -> inst.cst)
                        .anyMatch("[1.3 blocks]"::equals);

                if (foundstring)
                    hasString = true;
            }
            if (hasString) {
                for (MethodNode method : cn.methods) {
                    if (method.name.equals("<clinit>") && method.desc.equals("()V") && !this.round)
                        for (AbstractInsnNode insn : method.instructions) {
                            if (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.getClass() == String.class
                                    && ((String) ((LdcInsnNode) insn).cst).equals("#.##")) {
                                method.instructions.set(insn, new LdcInsnNode("0.00"));
                            }
                        }

                    for (AbstractInsnNode insn : method.instructions) {
                        if (insn.getOpcode() == Opcodes.LDC && ((LdcInsnNode) insn).cst.getClass() == Double.class
                                && (Double) ((LdcInsnNode) insn).cst == 3.0D) {
                            method.instructions.set(insn, new LdcInsnNode(300.00D));
                        }
                    }
                }
                ClassWriter cw = new ClassWriter(cr, 0);
                cn.accept(cw);
                return cw.toByteArray();
            }
        }
        return classfileBuffer;
    }
}
