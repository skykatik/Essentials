package essential;

import io.anuke.arc.*;
import io.anuke.arc.util.*;
import io.anuke.mindustry.*;
import io.anuke.mindustry.core.*;
import io.anuke.mindustry.core.GameState.*;
import io.anuke.mindustry.content.*;
import io.anuke.mindustry.entities.type.*;
import io.anuke.mindustry.game.EventType.*;
import io.anuke.mindustry.game.*;
import io.anuke.mindustry.gen.*;
import io.anuke.mindustry.plugin.Plugin;

import io.anuke.mindustry.net.Packets.KickReason;
import io.anuke.mindustry.maps.Map;
import io.anuke.mindustry.maps.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Essentials extends Plugin{
    public Essentials(){
        //startup
        Core.settings.getDataDirectory().child("motd.txt").writeString("");

        /*
        Events.on(PlayerJoin.class, player -> {
            String motd = Core.settings.getDataDirectory().child("motd.txt").readString();
            player.sendMessage("adfs");
        });
        */
        // error: non-static method sendMessage(String) cannot be referenced from a static context
    }

    @Override
    public void registerServerCommands(CommandHandler handler){
        
    }

    @Override
    public void registerClientCommands(CommandHandler handler){
        handler.<Player>register("motd", "Show server motd.", (args, player) -> {
            String motd = Core.settings.getDataDirectory().child("motd.txt").readString();
            player.sendMessage(motd);
        });

        handler.<Player>register("getpos", "Get your current position info", (args, player) -> {
            player.sendMessage("X: " + Math.round(player.x) + " Y: " + Math.round(player.y));
        });

        handler.<Player>register("info", "Show your information", (args, player) -> {
            player.sendMessage("[green]Name[]: " + player.name);
            player.sendMessage("[green]UUID[]: " + player.uuid);
            player.sendMessage("[green]Mobile[]: " + player.isMobile);
            player.sendMessage("[green]lastText[]: " + player.lastText);
        });

        // Teleport source from https://github.com/J-VdS/locationplugin
        handler.<Player>register("tp", "<player...>", "Teleport to other players", (args, player) -> {
            Player other = Vars.playerGroup.find(p->p.name.equalsIgnoreCase(args[0]));
            if(player.isAdmin == false){
                player.sendMessage("[green]Notice:[] You're not admin!");
            } else {
                if(other == null){
                    player.sendMessage("[scarlet]No player by that name found!");
                    return;
                }
                player.setNet(other.x, other.y);
            }
        });
        

        handler.<Player>register("kickall", "Kick all players", (args, player) -> {
            if(player.isAdmin == false){
                player.sendMessage("[green]Notice: [] You're not admin!");
            } else {
                // Call.onKick(other, KickReason.kick);
            }
        });

        handler.<Player>register("spawnmob", "Spawn mob", (args, player) -> {
            if(player.isAdmin == false){
                player.sendMessage("[green]Notice: [] You're not admin!");
            } else {
                player.sendMessage("source here");
            }
        });

        handler.<Player>register("tempban", "timer ban", (args, player) -> {
            if(player.isAdmin == false){
                player.sendMessage("[green]Notice: [] You're not admin!");
            } else {
                /*
                netServer.admins.banPlayerIP(other.con.address);
                netServer.kick(other.con.id, KickReason.banned);
                Core.settings.getDataDirectory().child("bans.txt").writeString(other.name+"/"+other.ip+"/"+time);
                String motd = Core.settings.getDataDirectory().child("motd.txt").readString();
                */
                // copied from ServerControl.java
            }
        });

        handler.<Player>register("me", "<text...>", "broadcast * message", (args, player) -> {
            Call.sendMessage("[orange]*[] " + player.name + "[orange][] : " + args[0]);
        });

        handler.<Player>register("difficulty", "<difficulty...>", "Set server difficulty", (args, player) -> {
            /*
            if(player.isAdmin == false){
                player.sendMessage("[green]Notice: [] You're not admin!");
            } else {
                try{
                    GameState.rules.waveSpacing = Difficulty.valueOf(args[0]).waveTime * 60 * 60 * 2;
                    player.sendMessage("Difficulty set to '"+args[0]+"'.");
                }catch(IllegalArgumentException e){
                    player.sendMessage("No difficulty with name '"+args[0]+"' found.");
                }
            }
            */
            // error: non-static method all() cannot be referenced from a static context
        });

        handler.<Player>register("effect", "<effect...>", "make effect", (args, player) -> {
            //source
        });

        handler.<Player>register("gamerule", "<gamerule...>", "Set gamerule", (args, player) -> {
            //source
        });

        handler.<Player>register("vote", "<vote...>", "Votemap", (args, player) -> {
            /*
            if(!Maps.all().isEmpty()){
                player.sendMessage("Maps:");
                for(Map m : Maps.all()){
                    player.sendMessage(m.name()+"/"+m.width+"x"+m.height);
                }
            }else{
                player.sendMessage("No maps found.");
            }
            */
            // error: non-static method all() cannot be referenced from a static context
        });

        handler.<Player>register("kill", "[player...]", "Kill player", (args, player) -> {
            /*
            player.onPlayerDeath(player);
            if(args[0] != null && player.isAdmin == false){
                player.sendMessage("[green]Notice:[] You're not admin!");
            } else {
                Player other = Vars.playerGroup.find(p->p.name.equalsIgnoreCase(args[0]));
                other.onPlayerDeath(other);
            }
            */
            // This command has all player kill bug
        });

        handler.<Player>register("save", "Map save", (args, player) -> {
            /*
            Core.app.post(() -> {
                int slot = Strings.parseInt(arg[0]);
                SaveIO.saveToSlot(slot);
                info("Saved to slot {0}.", slot);
            });
            */
            // copied from ServerControl.java
        });

        handler.<Player>register("time", "Show server time", (args, player) -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yy-M-d a h:m.ss");
            String nowString = now.format(dateTimeFormatter);
            player.sendMessage("[green]Server time[white]: "+nowString);
        });

        // PvP Team source from https://github.com/J-VdS/PVPPlugin
        // Full copied.
        handler.<Player>register("team", "<team...>", "Set team", (args, player) -> {
            //change team
            if (!Vars.state.rules.pvp){
                player.sendMessage("Only available in pvp.");
                return;
            }
            int index = player.getTeam().ordinal()+1;
            while (index != player.getTeam().ordinal()){
                if (index >= Team.all.length){
                    index = 0;
                }
                if (!Vars.state.teams.get(Team.all[index]).cores.isEmpty()){
                    player.setTeam(Team.all[index]);
                    break;
                }
                index++;
            }
            //kill player
            Call.onPlayerDeath(player);
        });

        handler.<Player>register("banlist", "Show banlist", (args, player) -> {
            /*
            for(PlayerInfo info : bans){
                info(" &ly {0} / Last known name: '{1}'", info.id, info.lastName);
            }
            for(String string : ipbans){
                PlayerInfo info = netServer.admins.findByIP(string);
                if(info != null){
                    info(" &lm '{0}' / Last known name: '{1}' / ID: '{2}'", string, info.lastName, info.id);
                }else{
                    info(" &lm '{0}' (No known name or info)", string);
                }
            }
            */
            //copied from ServerControl.java
        });
    }
}