package me.youded.LunarAccurateReachDisplay;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.tree.*;

import org.objectweb.asm.Opcodes;
import org.objectweb.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
    private Boolean foundfunction = false;
    
    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (classfileBuffer == null || classfileBuffer.length == 0) {
            return new byte[0];
        }

        if(!className.startsWith("lunar/")){
            return classfileBuffer;
        }

        ClassReader cr = new ClassReader(classfileBuffer);
        if( cr.getInterfaces().length == 0 && cr.getSuperName().startsWith("lunar/") ) {
            ClassNode cn = new ClassNode();

            cr.accept(cn, 0);
            
            for (MethodNode method : cn.methods) {
                if (method.name.equals("text")) {
                    if(!this.foundfunction){
                        for(AbstractInsnNode insn : method.instructions) {
                            if(insn.getOpcode() == Opcodes.LDC && (double)((LdcInsnNode)insn).cst == 3.0D) {
                                method.instructions.set(insn, new LdcInsnNode(300.0D));
                                this.foundfunction = true;
                                ClassWriter cw = new ClassWriter(cr, 0);
                                cn.accept(cw);
                                return cw.toByteArray();
                            }
                        }
                    }
                }
            }
        }
        return classfileBuffer;
    }
}
