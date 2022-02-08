package me.youded.LunarAccurateReachDisplay;

import java.lang.instrument.Instrumentation;

public class Agent {
    public static void premain(String args, Instrumentation inst){
        inst.addTransformer(new Transformer(args), true);
    }
}