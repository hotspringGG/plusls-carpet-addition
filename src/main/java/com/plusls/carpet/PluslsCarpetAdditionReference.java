/*
 * Copyright (c) Copyright 2020 - 2022 The Cat Town Craft and contributors.
 * This source code is subject to the terms of the GNU Lesser General Public
 * License, version 3. If a copy of the LGPL was not distributed with this
 * file, You can obtain one at: https://www.gnu.org/licenses/lgpl-3.0.txt
 */
package com.plusls.carpet;

import lombok.Getter;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.resources.ResourceLocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class PluslsCarpetAdditionReference {
    @Getter
    private static final String modIdentifier = "@MOD_IDENTIFIER@";
    @Getter
    private static final String modName = "@MOD_NAME@";
    @Getter
    private static final String modVersion = "@MOD_VERSION@";
    @Getter
    private static final Logger logger = LogManager.getLogger(modIdentifier);
    public static final boolean tisCarpetLoaded = FabricUtil.isModLoaded("carpet-tis-addition", ">=1.27.0");

    @Contract(value = "_ -> new", pure = true)
    public static @NotNull ResourceLocation identifier(String path) {
        try {
            // 尝试使用 fromNamespaceAndPath 方法
            Method fromNamespaceAndPath = ResourceLocation.class.getMethod("fromNamespaceAndPath", String.class, String.class);
            return (ResourceLocation) fromNamespaceAndPath.invoke(null, modIdentifier, path);
        } catch (NoSuchMethodException e) {
            // 如果没有 fromNamespaceAndPath 方法，使用反射调用构造函数
            try {
                Constructor<ResourceLocation> constructor = ResourceLocation.class.getDeclaredConstructor(String.class, String.class);
                constructor.setAccessible(true);  // 允许访问私有构造函数
                return constructor.newInstance(modIdentifier, path);
            } catch (Exception ex) {
                // 处理构造函数调用失败的情况
                throw new RuntimeException("Failed to create ResourceLocation", ex);
            }
        } catch (Exception ex) {
            // 处理反射调用失败的情况
            throw new RuntimeException("Failed to create ResourceLocation using reflection", ex);
        }
    }
}
