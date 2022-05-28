package com.willfp.eco.internal.gui.slot

import com.willfp.eco.core.gui.slot.Slot
import com.willfp.eco.core.gui.slot.SlotBuilder
import com.willfp.eco.core.gui.slot.functional.SlotHandler
import com.willfp.eco.core.gui.slot.functional.SlotProvider
import com.willfp.eco.core.gui.slot.functional.SlotUpdater
import com.willfp.eco.core.items.TestableItem
import org.bukkit.entity.Player
import java.util.function.Function

class EcoSlotBuilder(private val provider: SlotProvider) : SlotBuilder {
    private var captive = false
    private var captiveDefault: ((Player) -> TestableItem?)? = null
    private var updater: SlotUpdater = SlotUpdater { player, menu, _ -> provider.provide(player, menu) }

    private var onLeftClick =
        SlotHandler { _, _, _ -> run { } }
    private var onRightClick =
        SlotHandler { _, _, _ -> run { } }
    private var onShiftLeftClick =
        SlotHandler { _, _, _ -> run { } }
    private var onShiftRightClick =
        SlotHandler { _, _, _ -> run { } }
    private var onMiddleClick =
        SlotHandler { _, _, _ -> run { } }

    override fun onLeftClick(action: SlotHandler): SlotBuilder {
        onLeftClick = action
        return this
    }

    override fun onRightClick(action: SlotHandler): SlotBuilder {
        onRightClick = action
        return this
    }

    override fun onShiftLeftClick(action: SlotHandler): SlotBuilder {
        onShiftLeftClick = action
        return this
    }

    override fun onShiftRightClick(action: SlotHandler): SlotBuilder {
        onShiftRightClick = action
        return this
    }

    override fun onMiddleClick(action: SlotHandler): SlotBuilder {
        onMiddleClick = action
        return this
    }

    override fun setCaptive(provider: Function<Player, TestableItem>?): SlotBuilder {
        captive = true
        if (provider != null) {
            captiveDefault = { provider.apply(it) }
        }
        return this
    }

    override fun setUpdater(updater: SlotUpdater): SlotBuilder {
        this.updater = updater
        return this
    }

    override fun build(): Slot {
        return if (captive) {
            EcoCaptiveSlot(provider, captiveDefault)
        } else {
            EcoSlot(provider, onLeftClick, onRightClick, onShiftLeftClick, onShiftRightClick, onMiddleClick, updater)
        }
    }
}