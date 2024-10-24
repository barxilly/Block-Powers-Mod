package sbs.silly.powers;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(testmod.MODID)
public class testmod {

    public static final String MODID = "testmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public testmod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM COMMON SETUP");
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        LOGGER.info("HELLO from server starting");
    }

    // New method to give players powers when interacting with specific blocks
    @SubscribeEvent
    public void onPlayerInteract(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();
        BlockState state = event.getLevel().getBlockState(event.getPos());

        // Check if the player interacts with certain blocks to grant powers
        if (state.is(Blocks.GRASS_BLOCK) || state.is(Blocks.DIRT)) {
            var scaleAttribute = player.getAttribute(Attributes.SCALE);
            if (scaleAttribute != null) {
                var prevValue = scaleAttribute.getBaseValue();
                scaleAttribute.setBaseValue(prevValue + 0.025);
            }
        } else if (state.is(Blocks.OAK_LEAVES) || state.is(Blocks.BIRCH_LEAVES) || state.is(Blocks.SPRUCE_LEAVES)
                || state.is(Blocks.JUNGLE_LEAVES) || state.is(Blocks.ACACIA_LEAVES) || state.is(Blocks.DARK_OAK_LEAVES)) {
            var scaleAttribute = player.getAttribute(Attributes.SCALE);
            if (scaleAttribute != null) {
                var prevValue = scaleAttribute.getBaseValue();
                scaleAttribute.setBaseValue(Math.max(prevValue - 0.025, 0)); // Ensure it doesn't go below 0
            }
        } else if (state.is(Blocks.DIAMOND_BLOCK)) {
            player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 200, 1)); // Strength for 10 seconds
            LOGGER.info("Player given strength from interacting with Diamond Block!");
        } else if (state.is(Blocks.EMERALD_BLOCK)) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 200, 1)); // Speed for 10 seconds
            LOGGER.info("Player given speed from interacting with Emerald Block!");
        } else if (state.is(Blocks.GOLD_BLOCK)) {
            player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 200, 1)); // Regeneration for 10 seconds
            LOGGER.info("Player given regeneration from interacting with Gold Block!");
        } else if (state.is(Blocks.WATER)) {
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 100, 0)); // 100 ticks (5 seconds) of poison at level 0
            LOGGER.info("Player given poison from interacting with Water!");
        } else if (state.is(Blocks.STONE)) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0)); // Fire resistance for 10 seconds
            LOGGER.info("Player given fire resistance from interacting with Stone!");
        } else if (state.is(Blocks.LAVA)) {
            player.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 200, 0)); // Fire resistance for 10 seconds
            LOGGER.info("Player given fire resistance from interacting with Lava!");
        } else if (state.is(Blocks.COAL_ORE)) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0)); // Night vision for 10 seconds
            LOGGER.info("Player given night vision from interacting with Coal Ore!");
        } else if (state.is(Blocks.MAGMA_BLOCK)) {
            // Teleport the player to the Nether
            if (player instanceof ServerPlayer serverPlayer) {
                if (serverPlayer.getServer() != null && serverPlayer.getServer().getLevel(Level.NETHER) != null) {
                    serverPlayer.teleportTo(serverPlayer.getServer().getLevel(Level.NETHER), serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());
                }
            }
        } else if (state.is(Blocks.NETHERRACK)) {
            player.addEffect(new MobEffectInstance(MobEffects.DOLPHINS_GRACE, 200, 0)); // Dolphins grace for 10 seconds
            LOGGER.info("Player given dolphins grace from interacting with Netherrack!");
        } else if (state.is(Blocks.SAND)){
            player.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 200, 0)); // Levitation for 10 seconds
            LOGGER.info("Player given levitation from interacting with Sand!");
        }
    }

    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            LOGGER.info("HELLO FROM CLIENT SETUP");
            LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());
        }
    }
}
