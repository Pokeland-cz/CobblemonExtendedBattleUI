package com.cobblemonextendedbattleui

import net.minecraft.text.Text
import net.minecraft.text.TranslatableTextContent

object BattleMessageInterceptor {

    private val BOOST_KEYS = mapOf(
        "cobblemon.battle.boost.slight" to 1,
        "cobblemon.battle.boost.sharp" to 2,
        "cobblemon.battle.boost.severe" to 3
    )

    private val UNBOOST_KEYS = mapOf(
        "cobblemon.battle.unboost.slight" to -1,
        "cobblemon.battle.unboost.sharp" to -2,
        "cobblemon.battle.unboost.severe" to -3
    )

    private val WEATHER_START_KEYS = mapOf(
        "cobblemon.battle.weather.raindance.start" to BattleStateTracker.Weather.RAIN,
        "cobblemon.battle.weather.sunnyday.start" to BattleStateTracker.Weather.SUN,
        "cobblemon.battle.weather.sandstorm.start" to BattleStateTracker.Weather.SANDSTORM,
        "cobblemon.battle.weather.hail.start" to BattleStateTracker.Weather.HAIL,
        "cobblemon.battle.weather.snow.start" to BattleStateTracker.Weather.SNOW
    )

    private val WEATHER_END_KEYS = setOf(
        "cobblemon.battle.weather.raindance.end",
        "cobblemon.battle.weather.sunnyday.end",
        "cobblemon.battle.weather.sandstorm.end",
        "cobblemon.battle.weather.hail.end",
        "cobblemon.battle.weather.snow.end"
    )

    private val TERRAIN_START_KEYS = mapOf(
        "cobblemon.battle.fieldstart.electricterrain" to BattleStateTracker.Terrain.ELECTRIC,
        "cobblemon.battle.fieldstart.grassyterrain" to BattleStateTracker.Terrain.GRASSY,
        "cobblemon.battle.fieldstart.mistyterrain" to BattleStateTracker.Terrain.MISTY,
        "cobblemon.battle.fieldstart.psychicterrain" to BattleStateTracker.Terrain.PSYCHIC
    )

    private val TERRAIN_END_KEYS = setOf(
        "cobblemon.battle.fieldend.electricterrain",
        "cobblemon.battle.fieldend.grassyterrain",
        "cobblemon.battle.fieldend.mistyterrain",
        "cobblemon.battle.fieldend.psychicterrain"
    )

    private val FIELD_START_KEYS = mapOf(
        "cobblemon.battle.fieldstart.trickroom" to BattleStateTracker.FieldCondition.TRICK_ROOM,
        "cobblemon.battle.fieldstart.gravity" to BattleStateTracker.FieldCondition.GRAVITY,
        "cobblemon.battle.fieldstart.magicroom" to BattleStateTracker.FieldCondition.MAGIC_ROOM,
        "cobblemon.battle.fieldstart.wonderroom" to BattleStateTracker.FieldCondition.WONDER_ROOM
    )

    private val FIELD_END_KEYS = mapOf(
        "cobblemon.battle.fieldend.trickroom" to BattleStateTracker.FieldCondition.TRICK_ROOM,
        "cobblemon.battle.fieldend.gravity" to BattleStateTracker.FieldCondition.GRAVITY,
        "cobblemon.battle.fieldend.magicroom" to BattleStateTracker.FieldCondition.MAGIC_ROOM,
        "cobblemon.battle.fieldend.wonderroom" to BattleStateTracker.FieldCondition.WONDER_ROOM
    )

    // 1.6.1: Generic keys — side determined by args[0] ("your team" = ally)
    private val SIDE_START_KEYS = mapOf(
        "cobblemon.battle.sidestart.reflect" to BattleStateTracker.SideCondition.REFLECT,
        "cobblemon.battle.sidestart.lightscreen" to BattleStateTracker.SideCondition.LIGHT_SCREEN,
        "cobblemon.battle.sidestart.auroraveil" to BattleStateTracker.SideCondition.AURORA_VEIL,
        "cobblemon.battle.sidestart.tailwind" to BattleStateTracker.SideCondition.TAILWIND,
        "cobblemon.battle.sidestart.safeguard" to BattleStateTracker.SideCondition.SAFEGUARD,
        "cobblemon.battle.sidestart.luckychant" to BattleStateTracker.SideCondition.LUCKY_CHANT,
        "cobblemon.battle.sidestart.mist" to BattleStateTracker.SideCondition.MIST,
        "cobblemon.battle.sidestart.stealthrock" to BattleStateTracker.SideCondition.STEALTH_ROCK,
        "cobblemon.battle.sidestart.spikes" to BattleStateTracker.SideCondition.SPIKES,
        "cobblemon.battle.sidestart.toxicspikes" to BattleStateTracker.SideCondition.TOXIC_SPIKES,
        "cobblemon.battle.sidestart.stickyweb" to BattleStateTracker.SideCondition.STICKY_WEB
    )

    private val SIDE_END_KEYS = mapOf(
        "cobblemon.battle.sideend.reflect" to BattleStateTracker.SideCondition.REFLECT,
        "cobblemon.battle.sideend.lightscreen" to BattleStateTracker.SideCondition.LIGHT_SCREEN,
        "cobblemon.battle.sideend.auroraveil" to BattleStateTracker.SideCondition.AURORA_VEIL,
        "cobblemon.battle.sideend.tailwind" to BattleStateTracker.SideCondition.TAILWIND,
        "cobblemon.battle.sideend.safeguard" to BattleStateTracker.SideCondition.SAFEGUARD,
        "cobblemon.battle.sideend.luckychant" to BattleStateTracker.SideCondition.LUCKY_CHANT,
        "cobblemon.battle.sideend.mist" to BattleStateTracker.SideCondition.MIST,
        "cobblemon.battle.sideend.stealthrock" to BattleStateTracker.SideCondition.STEALTH_ROCK,
        "cobblemon.battle.sideend.spikes" to BattleStateTracker.SideCondition.SPIKES,
        "cobblemon.battle.sideend.toxicspikes" to BattleStateTracker.SideCondition.TOXIC_SPIKES,
        "cobblemon.battle.sideend.stickyweb" to BattleStateTracker.SideCondition.STICKY_WEB
    )

    private const val TURN_KEY = "cobblemon.battle.turn"
    private const val FAINT_KEY = "cobblemon.battle.faint"
    private const val SWITCH_KEY = "cobblemon.battle.switch"
    private const val DRAG_KEY = "cobblemon.battle.drag"

    fun processMessages(messages: List<Text>) {
        for (message in messages) {
            processComponent(message)
        }
    }

    private fun processComponent(text: Text) {
        val content = text.content
        if (content is TranslatableTextContent) {
            val key = content.key
            val args = content.args

            // Optional debug logging
            // CobblemonExtendedBattleUI.LOGGER.info("Battle key: $key | args: ${args.joinToString { it.toString() }}")

            when {
                key == TURN_KEY -> {
                    extractTurn(args)
                    return
                }

                BOOST_KEYS.containsKey(key) -> {
                    val stages = BOOST_KEYS[key]!!
                    extractStatChange(args, stages)
                    return
                }

                UNBOOST_KEYS.containsKey(key) -> {
                    val stages = UNBOOST_KEYS[key]!!
                    extractStatChange(args, stages)
                    return
                }

                // Z-Move boosted stat (e.g. "cobblemon.battle.boost.sharp.zeffect")
                key.startsWith("cobblemon.battle.boost.") && key.endsWith(".zeffect") -> {
                    val baseKey = key.removeSuffix(".zeffect")
                    BOOST_KEYS[baseKey]?.let { stages ->
                        extractStatChange(args, stages)
                        return
                    }
                }

                WEATHER_START_KEYS.containsKey(key) -> {
                    BattleStateTracker.setWeather(WEATHER_START_KEYS[key]!!)
                    return
                }

                key in WEATHER_END_KEYS -> {
                    BattleStateTracker.clearWeather()
                    return
                }

                TERRAIN_START_KEYS.containsKey(key) -> {
                    BattleStateTracker.setTerrain(TERRAIN_START_KEYS[key]!!)
                    return
                }

                key in TERRAIN_END_KEYS -> {
                    BattleStateTracker.clearTerrain()
                    return
                }

                FIELD_START_KEYS.containsKey(key) -> {
                    BattleStateTracker.setFieldCondition(FIELD_START_KEYS[key]!!)
                    return
                }

                FIELD_END_KEYS.containsKey(key) -> {
                    BattleStateTracker.clearFieldCondition(FIELD_END_KEYS[key]!!)
                    return
                }

                SIDE_START_KEYS.containsKey(key) -> {
                    val condition = SIDE_START_KEYS[key]!!
                    val isAlly = isAllySide(args)
                    BattleStateTracker.setSideCondition(isAlly, condition)
                    return
                }

                SIDE_END_KEYS.containsKey(key) -> {
                    val condition = SIDE_END_KEYS[key]!!
                    val isAlly = isAllySide(args)
                    BattleStateTracker.clearSideCondition(isAlly, condition)
                    return
                }

                key == FAINT_KEY -> {
                    clearPokemonStats(args)
                    return
                }

                key == SWITCH_KEY || key == DRAG_KEY -> {
                    clearPokemonStats(args)
                    return
                }
            }
        }

        // Recurse into siblings (e.g. styled text)
        for (sibling in text.siblings) {
            processComponent(sibling)
        }
    }

    private fun isAllySide(args: Array<out Any>): Boolean {
        if (args.isEmpty()) return true // default to ally if unknown
        val first = args[0]
        val text = when (first) {
            is Text -> first.string
            else -> first.toString()
        }.lowercase()
        return text.contains("your") || text.contains("ally") || text.contains("team") && !text.contains("opposing") && !text.contains("foe")
    }

    private fun extractTurn(args: Array<out Any>) {
        if (args.isEmpty()) return
        val turnText = args[0].let {
            when (it) {
                is Text -> it.string
                else -> it.toString()
            }
        }
        turnText.toIntOrNull()?.let { BattleStateTracker.setTurn(it) }
    }

    private fun extractStatChange(args: Array<out Any>, stages: Int) {
        if (args.size < 2) return

        val pokemonName = args[0].let {
            when (it) {
                is Text -> it.string
                else -> it.toString()
            }
        }

        val statName = args[1].let {
            when (it) {
                is Text -> it.string
                else -> it.toString()
            }
        }

        BattleStateTracker.applyStatChange(pokemonName, statName, stages)
    }

    private fun clearPokemonStats(args: Array<out Any>) {
        if (args.isEmpty()) return
        val pokemonName = args[0].let {
            when (it) {
                is Text -> it.string
                else -> it.toString()
            }
        }
        BattleStateTracker.clearPokemonStatsByName(pokemonName)
    }
}