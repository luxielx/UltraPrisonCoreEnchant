package Luxielx;

import me.drawethree.ultraprisoncore.UltraPrisonCore;
import me.drawethree.ultraprisoncore.enchants.UltraPrisonEnchants;
import me.drawethree.ultraprisoncore.enchants.enchants.UltraPrisonEnchantment;
import me.drawethree.ultraprisoncore.enchants.enchants.implementations.LuckyBoosterEnchant;
import me.drawethree.ultraprisoncore.libs.worldguardwrapper.region.IWrappedRegion;
import me.drawethree.ultraprisoncore.mines.model.mine.Mine;
import me.drawethree.ultraprisoncore.multipliers.enums.MultiplierType;
import me.drawethree.ultraprisoncore.utils.RegionUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Lightning extends UltraPrisonEnchantment {

    private final double chance;


    public Lightning() {
        super(UltraPrisonEnchants.getInstance(), 51);
        this.chance = this.plugin.getConfig().get().getDouble("enchants." + id + ".Chance");
    }

    @Override
    public String getAuthor() {
        return "Luxielx";
    }

    @Override
    public void onEquip(Player player, ItemStack itemStack, int i) {
    }

    @Override
    public void onUnequip(Player player, ItemStack itemStack, int i) {

    }

    @Override
    public void onBlockBreak(BlockBreakEvent e, int level) {
        if (this.chance * level < ThreadLocalRandom.current().nextDouble(100)) return;
        Block b = e.getBlock();
        Location loc = b.getLocation();
        Player p = e.getPlayer();
        double extra = 0;
        boolean autosell = false;
        if (UltraPrisonCore.getInstance().getAutoSell().hasAutoSellEnabled(p)) {
            autosell = true;
        }
        IWrappedRegion iWrappedRegion = RegionUtils.getMineRegionWithHighestPriority(loc);
        ArrayList<Block> blist = new ArrayList<>();
        p.getWorld().strikeLightningEffect(b.getLocation());
        if (iWrappedRegion != null) {
            for (int y = (int) b.getLocation().getY(); y >= 0; y--) {
                for (int i = -3; i <= 2; i++) {
                    for (int z = -3; z <= 2; z++) {
                        Block bb = b.getLocation().clone().add(i, -y, z).getBlock();
                        if (Main.jets.getMinesByBlock(bb).size() >= 0 && bb.getType() != Material.AIR && RegionUtils.getMineRegionWithHighestPriority(bb.getLocation()) != null) {
                            blist.add(bb);
                            if (autosell) {
                                extra += UltraPrisonCore.getInstance().getAutoSell().getPriceForBrokenBlock(iWrappedRegion.getId(), bb);
                            } else {
                                p.getInventory().addItem(new ItemStack[]{new ItemStack(bb.getType(), 1)});
                            }
                        }
                    }
                }
            }
        }


        boolean bool = LuckyBoosterEnchant.hasLuckyBoosterRunning(e.getPlayer());
        double d2 = this.plugin.isMultipliersModule() ? (bool ? (this.plugin.getCore().getMultipliers().getApi().getTotalToDeposit(p, extra, MultiplierType.SELL) * 2.0D) : this.plugin.getCore().getMultipliers().getApi().getTotalToDeposit(p, extra, MultiplierType.SELL)) : (bool ? (extra * 2.0D) : extra);
        if (this.plugin.isMinesModule()) {
            Mine mine = this.plugin.getCore().getMines().getApi().getMineAtLocation(e.getBlock().getLocation());
            if (mine != null)
                mine.handleBlockBreak(blist);
        }
        Main.jets.blockBreak(blist);
        if (this.plugin.isAutoSellModule())
            this.plugin.getCore().getAutoSell().addToCurrentEarnings(p, d2);
        this.plugin.getEnchantsManager().addBlocksBrokenToItem(p, blist.size());
        this.plugin.getCore().getTokens().handleBlockBreak(p, blist, true);
        for (Block block1 : blist)
            this.plugin.getCore().getNmsProvider().setBlockInNativeDataPalette(block1.getWorld(), block1.getX(), block1.getY(), block1.getZ(), 0, (byte) 0, true);

    }

    @Override
    public void reload() {

    }

}
