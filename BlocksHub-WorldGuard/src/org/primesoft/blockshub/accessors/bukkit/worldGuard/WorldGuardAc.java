/*
 * BlocksHub a library plugin providing easy access to block loggers 
 * and block access controllers.
 * Copyright (c) 2013, SBPrime <https://github.com/SBPrime/>
 * Copyright (c) BlocksHub contributors
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted free of charge provided that the following 
 * conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer. 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution,
 * 3. Redistributions of source code, with or without modification, in any form 
 *    other then free of charge is not allowed,
 * 4. Redistributions in binary form in any form other then free of charge is 
 *    not allowed.
 * 5. Any derived work based on or containing parts of this software must reproduce 
 *    the above copyright notice, this list of conditions and the following 
 *    disclaimer in the documentation and/or other materials provided with the 
 *    derived work.
 * 6. The original author of the software is allowed to change the license 
 *    terms or the entire license of the software as he sees fit.
 * 7. The original author of the software is allowed to sublicense the software 
 *    or its parts using any license terms he sees fit.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.primesoft.blockshub.accessors.bukkit.worldGuard;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.primesoft.blockshub.api.BlockData;
import org.primesoft.blockshub.api.IAccessController;
import org.primesoft.blockshub.api.ILog;
import org.primesoft.blockshub.api.IPlayer;
import org.primesoft.blockshub.api.IWorld;
import org.primesoft.blockshub.api.Vector;
import org.primesoft.blockshub.platform.bukkit.BukkitBaseEntity;
import org.primesoft.blockshub.platform.bukkit.BukkitPlayer;
import org.primesoft.blockshub.platform.bukkit.BukkitWorld;

/**
 *
 * @author SBPrime
 */
public class WorldGuardAc extends BukkitBaseEntity implements IAccessController {

    static IAccessController create(ILog logger, Object plugin) {
        if (!(plugin instanceof WorldGuardPlugin)) {
            logger.log("plugin not found.");
            return null;
        }

        return new WorldGuardAc((WorldGuardPlugin) plugin);
    }
    
    private final WorldGuardPlugin m_worldGuard;

    private WorldGuardAc(WorldGuardPlugin plugin) {
        super(plugin);
        
        m_worldGuard = plugin;
    }

    @Override
    public boolean hasAccess(IPlayer player, IWorld world, Vector location) {
        BukkitPlayer bukkitPlayer = BukkitPlayer.getPlayer(player);
        Player bPlayer = bukkitPlayer != null ? bukkitPlayer.getPlayer() : null;
        if (bPlayer == null) {
            return true;
        }

        if (!(world instanceof BukkitWorld)) {
            return true;
        }

        Location l = new Location(((BukkitWorld) world).getWorld(), location.getX(), location.getY(), location.getZ());
        
        return m_worldGuard.canBuild(bPlayer, l);
    }

    @Override
    public boolean canPlace(IPlayer player, IWorld world, Vector location,
            BlockData blockOld, BlockData blockNew) {
        return hasAccess(player, world, location);
    }
}
