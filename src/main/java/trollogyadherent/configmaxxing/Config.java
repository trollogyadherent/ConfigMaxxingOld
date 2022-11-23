package trollogyadherent.configmaxxing;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import trollogyadherent.configmaxxing.configpickers.mob.MobEntryPoint;
import trollogyadherent.configmaxxing.configpickers.potion.PotionEntryPoint;
import trollogyadherent.configmaxxing.configpickers.potionslim.PotionSlimEntryPoint;
import trollogyadherent.configmaxxing.util.Util;

import java.io.File;

public class Config {
    public static Configuration config = new Configuration(ConfigMaxxing.confFile);
    static boolean loaded = false;
    
    private static class Defaults {
        public static final boolean debug = false;

        public static final String singlePotion = "potion.moveSpeed";
        public static final String[] potionArray = {};
        public static final String[] potionArrayMaxLen = {};

        /* Fixed size lists have to be initialized with elements otherwise it NPE's. It's a Forge limitation I don't care enough about to fix. */
        public static final String[] potionArrayFixedSize = {"potion.moveSpeed", "potion.moveSlowdown", "potion.digSpeed", "potion.digSlowDown", "potion.damageBoost"};

        public static final String singleMob = "Wolf";
        public static final String[] mobArray = {};
        public static final String[] mobArrayMaxLen = {};
        public static final String[] mobArrayFixedSize = {"EntityHorse", "Ozelot", "EnderDragon", "Enderman", "Zombie"};
    }

    public static class Categories {
        public static final String general = "general";
        public static final String debug = "debug";
    }

    public static String singlePotion = Defaults.singlePotion;
    public static String[] potionArray = Defaults.potionArray;
    public static String[] potionArrayMaxLen = Defaults.potionArrayMaxLen;
    public static String[] potionArrayFixedSize = Defaults.potionArrayFixedSize;

    public static String singleMob = Defaults.singleMob;
    public static String[] mobArray = Defaults.mobArray;
    public static String[] mobArrayMaxLen = Defaults.mobArrayMaxLen;
    public static String[] mobArrayFixedSize = Defaults.mobArrayFixedSize;

    public static String singlePotionButSlimBecauseGTMEGA = Defaults.singlePotion;

    public static boolean debug;

    public static void synchronizeConfigurationClient(File configFile, boolean force, boolean load) {
        if (!loaded || force) {
            if (load) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();


        }
        if (config.hasChanged()) {
            config.save();
        }
    }

    public static void synchronizeConfigurationServer(File configFile, boolean force) {
        if (!loaded || force) {
            if (loaded) {
                config.load();
            }
            loaded = true;

            synchronizeConfigurationCommon();
        }
        if(config.hasChanged()) {
            config.save();
        }
    }

    public static void synchronizeConfigurationCommon() {
        /* debug */
        Property debugProperty = config.get(Categories.debug, "debug", Defaults.debug, "Enable/disable debug mode");
        debug = debugProperty.getBoolean();

        /* general */
        Property singlePotionProperty = config.get(Categories.general, "singlePotion", Defaults.singlePotion, "Example single potion.");
        if (!Util.isServer()) {
            singlePotionProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        singlePotion = singlePotionProperty.getString();

        Property potionArrayProperty = config.get(Categories.general, "potionArray", Defaults.potionArray, "Example list of potions.", false, 5, null);
        if (!Util.isServer()) {
            potionArrayProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        potionArray = potionArrayProperty.getStringList();

        Property potionArrayMaxLenProperty = config.get(Categories.general, "potionArrayMaxLen", Defaults.potionArrayMaxLen, "Example list of potions with a maximum size.", false, 5, null);
        if (!Util.isServer()) {
            potionArrayMaxLenProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        potionArrayMaxLen = potionArrayMaxLenProperty.getStringList();

        Property potionArrayFixedSizeProperty = config.get(Categories.general, "potionArrayFixedSize", Defaults.potionArrayFixedSize, "Example list of potions with a fixed size.", true, 5, null);
        if (!Util.isServer()) {
            potionArrayFixedSizeProperty.setConfigEntryClass(PotionEntryPoint.class);
        }
        potionArrayFixedSize = potionArrayFixedSizeProperty.getStringList();


        Property singleMobProperty = config.get(Categories.general, "singleMob", Defaults.singleMob, "Example single mob.");
        if (!Util.isServer()) {
            singleMobProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        singleMob = singleMobProperty.getString();

        Property mobArrayProperty = config.get(Categories.general, "mobArray", Defaults.mobArray, "Example list of mobs.", false, 5, null);
        if (!Util.isServer()) {
            mobArrayProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobArray = mobArrayProperty.getStringList();

        Property mobArrayMaxLenProperty = config.get(Categories.general, "mobArrayMaxLen", Defaults.mobArrayMaxLen, "Example list of mobs with a maximum size.", false, 5, null);
        if (!Util.isServer()) {
            mobArrayMaxLenProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobArrayMaxLen = mobArrayMaxLenProperty.getStringList();

        Property mobArrayFixedSizeProperty = config.get(Categories.general, "mobArrayFixedSize", Defaults.mobArrayFixedSize, "Example list of mobs with a fixed size.", true, 5, null);
        if (!Util.isServer()) {
            mobArrayFixedSizeProperty.setConfigEntryClass(MobEntryPoint.class);
        }
        mobArrayFixedSize = mobArrayFixedSizeProperty.getStringList();


        Property singlePotionSlimProperty = config.get(Categories.general, "slimExample for skill issue", Defaults.singlePotion, "This is to demonstrate slimmer selection list entries. For the MEGA modpack people. Houston, if you want smaller entries, just make them yourself");
        if (!Util.isServer()) {
            /* If you don't like dependencies, just do this. lol. */
            /* What's the worst thing that could happen anyway. The list gui will just be a plain forge one. */
            try {
                Class entry = Class.forName("trollogyadherent.configmaxxing.configpickers.potionslim.PotionSlimEntryPoint");
                singlePotionSlimProperty.setConfigEntryClass(entry);
            } catch (ClassNotFoundException e) {
                ConfigMaxxing.error(":(");
            }
        }
        singlePotionButSlimBecauseGTMEGA = singlePotionSlimProperty.getString();
    }
}