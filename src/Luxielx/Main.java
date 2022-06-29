package Luxielx;

import me.drawethree.ultraprisoncore.libs.worldguardwrapper.WorldGuardWrapper;
import me.drawethree.ultraprisoncore.libs.worldguardwrapper.region.IWrappedRegion;
import me.jet315.prisonmines.JetsPrisonMines;
import me.jet315.prisonmines.JetsPrisonMinesAPI;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Set;
import java.util.stream.Collectors;

import static me.drawethree.ultraprisoncore.utils.RegionUtils.__pmob_$3aALYAu3YOdDgDJbfYGOgiJf9J_0SevW$_a;

public class Main extends JavaPlugin {
    static JetsPrisonMinesAPI jets;
    static Plugin upc;
    FileConfiguration config;

    public static IWrappedRegion getMineRegionWithHighestPriority(Location paramLocation) {
        Set set = WorldGuardWrapper.getInstance().getRegions(paramLocation).stream().filter(paramIWrappedRegion -> paramIWrappedRegion.getId().startsWith(__pmob_$3aALYAu3YOdDgDJbfYGOgiJf9J_0SevW$_a[0])).collect(Collectors.toSet());
        IWrappedRegion iWrappedRegion = null;
        for (Object iWrappedRegion3 : set) {
            if (iWrappedRegion3 instanceof IWrappedRegion) {
                IWrappedRegion iWrappedRegion1 = (IWrappedRegion) iWrappedRegion3;
                if (iWrappedRegion == null || iWrappedRegion1.getPriority() > iWrappedRegion.getPriority())
                    iWrappedRegion = iWrappedRegion1;
            }
        }
        return iWrappedRegion;
    }

    @Override
    public void onEnable() {
        config = getConfig();
        config.options().copyDefaults(true);
        if (Bukkit.getPluginManager().getPlugin("UltraPrisonCore") == null) {
            this.getLogger().warning("Unable to hook into UltraPrisonCore! Disabling...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;


        }
        if (Bukkit.getPluginManager().getPlugin("JetsPrisonMines") == null) {
            this.getLogger().warning("Unable to hook into JetsPrisonMines! Disabling...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;

        }
        this.jets = ((JetsPrisonMines) Bukkit.getPluginManager().getPlugin("JetsPrisonMines")).getAPI();
        this.upc = Bukkit.getPluginManager().getPlugin("UltraPrisonCore");
        new Laser().register();
        new Lightning().register();


    }

    @Override
    public void onDisable() {

    }
}
