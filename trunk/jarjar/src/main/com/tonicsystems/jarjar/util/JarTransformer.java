/*
  Jar Jar Links - A utility to repackage and embed Java libraries
  Copyright (C) 2004  Tonic Systems, Inc.

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; see the file COPYING.  if not, write to
  the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
  Boston, MA 02111-1307 USA
*/

package com.tonicsystems.jarjar.util;

import java.io.*;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

abstract public class JarTransformer implements JarProcessor
{
    public boolean process(EntryStruct struct) throws IOException {
        if (struct.name.endsWith(".class")) {
            ClassReader reader;
            try {
                reader = new ClassReader(struct.in);
            } catch (Exception e) {
                return true; // TODO?
            }
            GetNameClassWriter w = new GetNameClassWriter(ClassWriter.COMPUTE_MAXS);
            reader.accept(transform(w), 0);
            struct.in = new ByteArrayInputStream(w.toByteArray());
            struct.name = pathFromName(w.getClassName());
        }
        return true;
    }

    abstract protected ClassVisitor transform(ClassVisitor v);

    private static String pathFromName(String className) {
        return className.replace('.', '/') + ".class";
    }
}
