package com.example;

import net.thauvin.erik.isgd.Isgd;

public final class IsgdSample {
    public static void main(final String[] args) {
        if (args.length > 0) {
            for (final String arg : args) {
                if (arg.contains("is.gd")) {
                    System.out.println(arg + " <-- " + Isgd.lookup(arg));
                } else {
                    System.out.println(arg + " --> " + Isgd.shorten(arg));
                }
            }
        } else {
            System.err.println("Try specifying one or more URLs as arguments.");
        }
        System.exit(0);
    }
}
