package codechicken.wirelessredstone.manager;

import java.util.HashSet;

import codechicken.wirelessredstone.device.Remote;
import codechicken.wirelessredstone.device.Sniffer;
import codechicken.wirelessredstone.entity.EntityREP;

import codechicken.wirelessredstone.util.WirelessMapNodeStorage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public abstract class RedstoneEtherAddons
{    
    static class AddonPlayerInfo
    {
        Remote remote;
        Sniffer sniffer;
        EntityREP activeREP;
        int REPThrowTimeout;
        WirelessMapNodeStorage mapNodes = new WirelessMapNodeStorage();
        HashSet<Integer> triangSet = new HashSet<>();
        HashSet<Integer> mapInfoSet = new HashSet<>();
    }
    
    private static RedstoneEtherClientAddons clientManager;
    private static RedstoneEtherServerAddons serverManager;
    
    public static RedstoneEtherAddons get(boolean remote)
    {
        return remote ? clientManager : serverManager;
    }

    public static RedstoneEtherServerAddons server()
    {
        return serverManager;
    }

    @SideOnly(Side.CLIENT)
    public static RedstoneEtherClientAddons client()
    {
        if (clientManager == null) {
            loadClientManager();
        }
        return clientManager;
    }

    public static void loadServerWorld()
    {
        if(serverManager == null)
            serverManager = new RedstoneEtherServerAddons();
    }

    public static void unloadServer()
    {
        serverManager = null;
    }
    
    public static void loadClientManager()
    {
        clientManager = new RedstoneEtherClientAddons();
    }

    public abstract void invalidateREP(EntityPlayer player);

    public abstract boolean isRemoteOn(EntityPlayer player, int freq);

    public abstract void activateRemote(World world, EntityPlayer entityplayer);

    public abstract boolean deactivateRemote(World world, EntityPlayer player);
    
    public static String localizeWirelessItem(String shortName, int freq)
    {
        if(freq <= 0 || freq > RedstoneEther.numfreqs || RedstoneEther.client() == null)
            return shortName;

        String freqname = RedstoneEther.get(true).getFreqName(freq);
        if(freqname != null && !freqname.equals(""))
            return freqname;
        if(RedstoneEther.get(true).getFreqColourId(freq) == -1)
            return shortName + ' ' + freq;
        
        return RedstoneEther.get(true).getFreqColourName(freq, false) + 
            ' ' + shortName + ' ' + freq;
    }
}
