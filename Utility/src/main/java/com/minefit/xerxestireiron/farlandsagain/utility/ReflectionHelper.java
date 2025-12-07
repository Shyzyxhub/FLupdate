package com.minefit.xerxestireiron.farlandsagain.utility;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import sun.misc.Unsafe;

/**
 * Helper pour manipuler les champs privés et finals via reflection.
 * Compatible Paper 1.21.4 / Java 17+.
 */
@SuppressWarnings("restriction")
public class ReflectionHelper {

    /**
     * Récupère un Field depuis la classe ou ses superclasses.
     *
     * @param baseClass classe de départ
     * @param fieldName nom du champ
     * @param declared true si getDeclaredField, false si getField
     * @return le Field demandé
     * @throws NoSuchFieldException si non trouvé
     */
    public static Field getField(Class<?> baseClass, String fieldName, boolean declared) throws NoSuchFieldException {
        Field field;

        try {
            if (declared) {
                field = baseClass.getDeclaredField(fieldName);
            } else {
                field = baseClass.getField(fieldName);
            }
        } catch (NoSuchFieldException e) {
            Class<?> superClass = baseClass.getSuperclass();
            if (superClass != null) {
                field = getField(superClass, fieldName, declared);
            } else {
                throw e;
            }
        }

        return field;
    }

    /**
     * Modifie la valeur d'un champ, même s'il est private/final.
     *
     * @param field    champ à modifier
     * @param instance instance cible (ou null si static)
     * @param obj      nouvelle valeur
     * @throws Throwable si impossible
     */
    public static void fieldSetter(Field field, Object instance, Object obj) throws Throwable {
        try {
            // Première approche classique
            field.setAccessible(true);
            Field modifiers = Field.class.getDeclaredField("modifiers"); // attention Java 12+
            modifiers.setAccessible(true);
            modifiers.setInt(field, field.getModifiers() & ~Modifier.FINAL);
            field.set(instance, obj);

        } catch (Exception e) {
            try {
                // Fallback via MethodHandles
                Lookup lookup = MethodHandles.lookup();
                MethodHandle handle = lookup.unreflectSetter(field);
                handle.invoke(instance, obj);

            } catch (Throwable t) {
                try {
                    // Dernier recours via Unsafe
                    Field theUnsafe = Unsafe.class.getDeclaredField("theUnsafe");
                    theUnsafe.setAccessible(true);
                    Unsafe unsafe = (Unsafe) theUnsafe.get(null);
                    long offset;

                    if (Modifier.isStatic(field.getModifiers())) {
                        offset = unsafe.staticFieldOffset(field);
                    } else {
                        offset = unsafe.objectFieldOffset(field);
                    }

                    unsafe.putObject(instance, offset, obj);

                } catch (Throwable t1) {
                    throw t1;
                }
            }
        }
    }
}
