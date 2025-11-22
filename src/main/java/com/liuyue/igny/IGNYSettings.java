package com.liuyue.igny;


import carpet.api.settings.Rule;

import static com.liuyue.igny.utils.IGNYRuleCategory.*;


public class IGNYSettings
{
    @Rule(
            categories = {IGNY,SURVIVAL,FEATURE}
    )
    public static boolean WardenNeverDig = false;
    @Rule(
            categories = {IGNY,SURVIVAL,FEATURE}
    )
    public static boolean PlayerLevitationFreeShulkerBullet = false;
    @Rule(
            categories = {IGNY,SURVIVAL,FEATURE}
    )
    public static boolean PlayerMiningFatigueFreeGuardian = false;
    @Rule(
            categories = {IGNY,FEATURE},
            options = {"true", "false"}
    )
    public static boolean ShowRuleChangeHistory = false;
    @Rule(
            categories = {IGNY,SURVIVAL,FEATURE},
            options = {"true", "false"}
    )
    public static boolean FakePlayerCanPush = true;
    @Rule(
            categories = {IGNY,SURVIVAL,FEATURE},
            options = {"true", "false","wet_sponge"}
    )
    public static String SpongeCanAbsorbLava = "false";
}
