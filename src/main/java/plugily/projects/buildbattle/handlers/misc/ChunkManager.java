/*
 *
 * BuildBattle - Ultimate building competition minigame
 * Copyright (C) 2022 Plugily Projects - maintained by Tigerpanzer_02 and contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package plugily.projects.buildbattle.handlers.misc;

import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.ViaAPI;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import plugily.projects.minigamesbox.classic.utils.version.PacketUtils;
import plugily.projects.minigamesbox.classic.utils.version.ServerVersion;
import protocolsupport.api.ProtocolSupportAPI;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Tigerpanzer_02
 * <p>
 * Created at 19.06.2022
 */
public class ChunkManager {

  private static Class<?> packetPlayOutMapChunk, chunkClass;
  private static Constructor<?> mapChunkConstructor;

  static {
    if (ServerVersion.Version.isCurrentLower(ServerVersion.Version.v1_18)) {
      packetPlayOutMapChunk = PacketUtils.classByName("net.minecraft.network.protocol.game", "PacketPlayOutMapChunk");
      chunkClass = PacketUtils.classByName("net.minecraft.world.level.chunk", "Chunk");

      if (ServerVersion.Version.isCurrentEqualOrHigher(ServerVersion.Version.v1_17)) {
        try {
          mapChunkConstructor = packetPlayOutMapChunk.getConstructor(PacketUtils.classByName("net.minecraft.world.level.chunk", "LevelChunk"));
        } catch (NoSuchMethodException e) {
          try {
            mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass);
          } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
  }

  public static void sendMapChunk(Player player, Chunk chunk) {
    /*int version = -1;
    if(Bukkit.getPluginManager().isPluginEnabled("ProtocolSupport")) {
      try {
        version = ProtocolSupportAPI.getProtocolVersion(player).getId();
      } catch (Exception ignored) {
        ignored.printStackTrace();
        //Can't interact with protocol api
      }
    }
    if(version==-1) {
      if(Bukkit.getPluginManager().isPluginEnabled("ViaVersion")) {
        try {
          version = Via.getAPI().getPlayerVersion(player); // Get the protocol version
        } catch(Exception ignored) {
          ignored.printStackTrace();
        }
      }
    }
    System.out.println(version);
    System.out.println(Arrays.toString(Bukkit.getPluginManager().getPlugins()));
    if(version==-1) {
      return;
    }
    if (version < 757) {
      packetPlayOutMapChunk = PacketUtils.classByName("net.minecraft.network.protocol.game", "PacketPlayOutMapChunk");
      chunkClass = PacketUtils.classByName("net.minecraft.world.level.chunk", "Chunk");

      if (version <= 755) {
        try {
          mapChunkConstructor = packetPlayOutMapChunk.getConstructor(PacketUtils.classByName("net.minecraft.world.level.chunk", "LevelChunk"));
        } catch (NoSuchMethodException e) {
          try {
            mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass);
          } catch (NoSuchMethodException ex) {
            throw new RuntimeException(ex);
          }
        }
      }
    }
    if(version >= 757) {
      return;
    }
    try {
      Method chunkHandleMethod = chunk.getClass().getMethod("getHandle");
      if (version >= 755) {
        PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk)));
        return;
      }

      if (version >= 735) {
        if (mapChunkConstructor == null)
          mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass, int.class, boolean.class);

        PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk), 65535, false));
        return;
      }

      if (version <= 210) {
        if (mapChunkConstructor == null)
          mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass, boolean.class, int.class);

        PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk), true, 65535));
        return;
      }

      if (mapChunkConstructor == null)
        mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass, int.class);

      PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk), 65535));
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException(exception);
    }*/


    if (ServerVersion.Version.isCurrentEqualOrHigher(ServerVersion.Version.v1_18)) {
      return; // Should just use World#refreshChunk instead
    }

    try {
      Method chunkHandleMethod = chunk.getClass().getMethod("getHandle");
      if (ServerVersion.Version.isCurrentEqualOrHigher(ServerVersion.Version.v1_17)) {
        PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk)));
        return;
      }

      if (ServerVersion.Version.isCurrentEqualOrHigher(ServerVersion.Version.v1_16)) {
        if (mapChunkConstructor == null)
          mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass, int.class, boolean.class);

        PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk), 65535, false));
        return;
      }

      if (ServerVersion.Version.isCurrentEqualOrLower(ServerVersion.Version.v1_10)) {
        if (mapChunkConstructor == null)
          mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass, boolean.class, int.class);

        PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk), true, 65535));
        return;
      }

      if (mapChunkConstructor == null)
        mapChunkConstructor = packetPlayOutMapChunk.getConstructor(chunkClass, int.class);

      PacketUtils.sendPacket(player, mapChunkConstructor.newInstance(chunkHandleMethod.invoke(chunk), 65535));
    } catch (ReflectiveOperationException exception) {
      throw new RuntimeException(exception);
    }
  }

}
