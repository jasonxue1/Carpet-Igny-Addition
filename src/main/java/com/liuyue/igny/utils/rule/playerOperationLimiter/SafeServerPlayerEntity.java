package com.liuyue.igny.utils.rule.playerOperationLimiter;

public interface SafeServerPlayerEntity {
    int igny$getBreakCountPerTick();
    int igny$getPlaceCountPerTick();
    void igny$addBreakCountPerTick();
    void igny$addPlaceCountPerTick();
}