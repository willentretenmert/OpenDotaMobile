package com.example.apiapplication.data

import com.google.gson.annotations.SerializedName

data class MatchStats(
    @field:SerializedName("barracks_status_dire") val barracks_status_dire: Int,

    @field:SerializedName("barracks_status_radiant") val barracks_status_radiant: Int,

    @field:SerializedName("chat") val chat: Any,

    @field:SerializedName("cluster") val cluster: Int,

    @field:SerializedName("cosmetics") val cosmetics: Any,

    @field:SerializedName("dire_score") val dire_score: Int,

    @field:SerializedName("dire_team_id") val dire_team_id: Any,

    @field:SerializedName("draft_timings") val draft_timings: Any,

    @field:SerializedName("duration") val duration: Int,

    @field:SerializedName("engine") val engine: Int,

    @field:SerializedName("first_blood_time") val first_blood_time: Int,

    @field:SerializedName("game_mode") val game_mode: Int,

    @field:SerializedName("human_players") val human_players: Int,

    @field:SerializedName("leagueid") val leagueid: Int,

    @field:SerializedName("lobby_type") val lobby_type: Int,

    @field:SerializedName("match_id") val match_id: Long,

    @field:SerializedName("match_seq_num") val match_seq_num: Long,

    @field:SerializedName("negative_votes") val negative_votes: Int,

    @field:SerializedName("objectives") val objectives: Any,

    @field:SerializedName("patch") val patch: Int,

    @field:SerializedName("picks_bans") val picks_bans: List<PicksBan>,

    @field:SerializedName("players") val players: List<Player>,

    @field:SerializedName("positive_votes") val positive_votes: Int,

    @field:SerializedName("radiant_gold_adv") val radiant_gold_adv: Any,

    @field:SerializedName("radiant_score") val radiant_score: Int,

    @field:SerializedName("radiant_team_id") val radiant_team_id: Any,

    @field:SerializedName("radiant_win") val radiant_win: Boolean,

    @field:SerializedName("radiant_xp_adv") val radiant_xp_adv: Any,

    @field:SerializedName("region") val region: Int,

    @field:SerializedName("replay_salt") val replay_salt: Int,

    @field:SerializedName("replay_url") val replay_url: String,

    @field:SerializedName("series_id") val series_id: Int,

    @field:SerializedName("series_type") val series_type: Int,

    @field:SerializedName("skill") val skill: Any,

    @field:SerializedName("start_time") val start_time: Long,

    @field:SerializedName("teamfights") val teamfights: Any,

    @field:SerializedName("tower_status_dire") val tower_status_dire: Int,

    @field:SerializedName("tower_status_radiant") val tower_status_radiant: Int,

    @field:SerializedName("version") val version: Any
) {
    data class PicksBan(
        @field:SerializedName("hero_id") val hero_id: Int,

        @field:SerializedName("is_pick") val is_pick: Boolean,

        @field:SerializedName("order") val order: Int,

        @field:SerializedName("team") val team: Int
    )

    data class Player(
        @field:SerializedName("abandons") val abandons: Int,

        @field:SerializedName("ability_targets") val ability_targets: Any,

        @field:SerializedName("ability_upgrades_arr") val ability_upgrades_arr: List<Int>,

        @field:SerializedName("ability_uses") val ability_uses: Any,

        @field:SerializedName("account_id") val account_id: Int,

        @field:SerializedName("actions") val actions: Any,

        @field:SerializedName("additional_units") val additional_units: Any,

        @field:SerializedName("assists") val assists: Int,

        @field:SerializedName("backpack_0") val backpack_0: Int,

        @field:SerializedName("backpack_1") val backpack_1: Int,

        @field:SerializedName("backpack_2") val backpack_2: Int,

        @field:SerializedName("backpack_3") val backpack_3: Int,

        @field:SerializedName("buyback_log") val buyback_log: Any,

        @field:SerializedName("camps_stacked") val camps_stacked: Any,

        @field:SerializedName("cluster") val cluster: Int,

        @field:SerializedName("connection_log") val connection_log: Any,

        @field:SerializedName("cosmetics") val cosmetics: List<Any>,

        @field:SerializedName("creeps_stacked") val creeps_stacked: Any,

        @field:SerializedName("damage") val damage: Any,

        @field:SerializedName("damage_inflictor") val damage_inflictor: Any,

        @field:SerializedName("damage_inflictor_received") val damage_inflictor_received: Any,

        @field:SerializedName("damage_taken") val damage_taken: Any,

        @field:SerializedName("damage_targets") val damage_targets: Any,

        @field:SerializedName("deaths") val deaths: Int,

        @field:SerializedName("denies") val denies: Int,

        @field:SerializedName("dn_t") val dn_t: Any,

        @field:SerializedName("duration") val duration: Int,

        @field:SerializedName("firstblood_claimed") val firstblood_claimed: Any,

        @field:SerializedName("game_mode") val game_mode: Int,

        @field:SerializedName("gold") val gold: Int,

        @field:SerializedName("gold_per_min") val gold_per_min: Int,

        @field:SerializedName("gold_reasons") val gold_reasons: Any,

        @field:SerializedName("gold_spent") val gold_spent: Int,

        @field:SerializedName("gold_t") val gold_t: Any,

        @field:SerializedName("hero_damage") val hero_damage: Int,

        @field:SerializedName("hero_healing") val hero_healing: Int,

        @field:SerializedName("hero_hits") val hero_hits: Any,

        @field:SerializedName("hero_id") val hero_id: Int,

        @field:SerializedName("isRadiant") val isRadiant: Boolean,

        @field:SerializedName("is_contributor") val is_contributor: Boolean,

        @field:SerializedName("is_subscriber") val is_subscriber: Boolean,

        @field:SerializedName("item_0") val item_0: Int,

        @field:SerializedName("item_1") val item_1: Int,

        @field:SerializedName("item_2") val item_2: Int,

        @field:SerializedName("item_3") val item_3: Int,

        @field:SerializedName("item_4") val item_4: Int,

        @field:SerializedName("item_5") val item_5: Int,

        @field:SerializedName("item_neutral") val item_neutral: Int,

        @field:SerializedName("item_uses") val item_uses: Any,

        @field:SerializedName("kda") val kda: Int,

        @field:SerializedName("kill_streaks") val kill_streaks: Any,

        @field:SerializedName("killed") val killed: Any,

        @field:SerializedName("killed_by") val killed_by: Any,

        @field:SerializedName("kills") val kills: Int,

        @field:SerializedName("kills_log") val kills_log: Any,

        @field:SerializedName("kills_per_min") val kills_per_min: Double,

        @field:SerializedName("lane_pos") val lane_pos: Any,

        @field:SerializedName("last_hits") val last_hits: Int,

        @field:SerializedName("last_login") val last_login: String,

        @field:SerializedName("leaver_status") val leaver_status: Int,

        @field:SerializedName("level") val level: Int,

        @field:SerializedName("lh_t") val lh_t: Any,

        @field:SerializedName("life_state") val life_state: Any,

        @field:SerializedName("lobby_type") val lobby_type: Int,

        @field:SerializedName("lose") val lose: Int,

        @field:SerializedName("match_id") val match_id: Long,

        @field:SerializedName("max_hero_hit") val max_hero_hit: Any,

        @field:SerializedName("multi_kills") val multi_kills: Any,

        @field:SerializedName("name") val name: Any,

        @field:SerializedName("net_worth") val net_worth: Int,

        @field:SerializedName("obs") val obs: Any,

        @field:SerializedName("obs_left_log") val obs_left_log: Any,

        @field:SerializedName("obs_log") val obs_log: Any,

        @field:SerializedName("obs_placed") val obs_placed: Any,

        @field:SerializedName("party_id") val party_id: Int,

        @field:SerializedName("party_size") val party_size: Int,

        @field:SerializedName("patch") val patch: Int,

        @field:SerializedName("performance_others") val performance_others: Any,

        @field:SerializedName("personaname") val personaname: String,

        @field:SerializedName("pings") val pings: Any,

        @field:SerializedName("player_slot") val player_slot: Int,

        @field:SerializedName("pred_vict") val pred_vict: Any,

        @field:SerializedName("purchase") val purchase: Any,

        @field:SerializedName("purchase_log") val purchase_log: List<Any>,

        @field:SerializedName("randomed") val randomed: Any,

        @field:SerializedName("repicked") val repicked: Any,

        @field:SerializedName("roshans_killed") val roshans_killed: Any,

        @field:SerializedName("rune_pickups") val rune_pickups: Any,

        @field:SerializedName("runes") val runes: Any,

        @field:SerializedName("runes_log") val runes_log: Any,

        @field:SerializedName("sen") val sen: Any,

        @field:SerializedName("sen_left_log") val sen_left_log: Any,

        @field:SerializedName("sen_log") val sen_log: Any,

        @field:SerializedName("sen_placed") val sen_placed: Any,

        @field:SerializedName("solo_competitive_rank") val solo_competitive_rank: Int,

        @field:SerializedName("stuns") val stuns: Any,

        @field:SerializedName("team_id") val team_id: Int,

        @field:SerializedName("team_name") val team_name: Any,

        @field:SerializedName("team_tag") val team_tag: Any,

        @field:SerializedName("throw") val `throw`: Any,

        @field:SerializedName("times") val times: Any,

        @field:SerializedName("tower_damage") val tower_damage: Int,

        @field:SerializedName("towers_killed") val towers_killed: Any,

        @field:SerializedName("win") val win: Int,

        @field:SerializedName("xp_per_min") val xp_per_min: Int,

        @field:SerializedName("xp_reasons") val xp_reasons: Any,

        @field:SerializedName("xp_t") val xp_t: Any
    )
}


