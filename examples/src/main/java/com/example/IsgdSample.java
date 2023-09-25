package com.example;

import net.thauvin.erik.isgd.Config;
import net.thauvin.erik.isgd.Format;
import net.thauvin.erik.isgd.Isgd;
import net.thauvin.erik.isgd.IsgdException;

public final class IsgdSample {
    public static void main(final String[] args) {
        if (args.length > 0) {
            for (final String arg : args) {
                try {
                    if (arg.contains("is.gd")) {
                        System.out.println(arg + " <-- " + Isgd.lookup(arg));
                        System.out.print(Isgd.lookup(new Config.Builder().shorturl(arg).format(Format.WEB).build()));
                    } else {
                        System.out.println(arg + " --> " + Isgd.shorten(arg));
                    }
                } catch (IsgdException e) {
                    System.out.println(e.getMessage());
                }
            }
        } else {
            System.err.println("Try specifying one or more URLs as arguments.");
        }
        System.exit(0);
    }
}
