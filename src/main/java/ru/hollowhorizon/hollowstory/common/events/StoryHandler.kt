package ru.hollowhorizon.hollowstory.common.events

import net.minecraftforge.event.entity.player.PlayerEvent
import ru.hollowhorizon.hc.api.utils.HollowConfig
import ru.hollowhorizon.hc.common.capabilities.HollowCapabilityV2
import ru.hollowhorizon.hollowstory.common.capabilities.StoryTeamCapability
import ru.hollowhorizon.hollowstory.common.files.HollowStoryDirHelper
import ru.hollowhorizon.hollowstory.common.hollowscript.story.StoryExecutorThread
import ru.hollowhorizon.hollowstory.story.StoryTeam
import ru.hollowhorizon.hollowstory.story.StoryVariables

object StoryHandler {
    @JvmField
    @HollowConfig("only_one_team", description = "All players will be in one team")
    var shouldAddToHostTeam = true

    @JvmStatic
    fun onPlayerJoin(event: PlayerEvent.PlayerLoggedInEvent) {
//        event.player.server?.overworld()?.getCapability(HollowCapabilityV2.get<StoryTeamCapability>())?.ifPresent { teamCap ->
//            val team = teamCap.getTeam(event.player)
//
//            HollowStoryDirHelper.getAllStoryEvents().forEach { script ->
//                try {
//                    StoryExecutorThread(team, StoryVariables(), script).start()
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
//        }
        val team = StoryTeam()
        team.add(event.player)
        HollowStoryDirHelper.getAllStoryEvents().forEach { script ->
            try {
                StoryExecutorThread(team, StoryVariables(), script).start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @JvmStatic
    fun onPlayerClone(event: PlayerEvent.Clone) {
        event.player.server?.overworld()?.getCapability(HollowCapabilityV2.get<StoryTeamCapability>())?.ifPresent { teamCap ->
            val team = teamCap.getTeam(event.original)

            team.updatePlayer(event.player)
        }
    }
}