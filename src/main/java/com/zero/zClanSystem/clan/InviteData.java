package com.zero.zClanSystem.clan;

public class InviteData {

    private final String clanTag;
    private final long expiresAt;

    public InviteData(String clanTag, long expiresAt) {
        this.clanTag = clanTag;
        this.expiresAt = expiresAt;
    }

    public String getClanTag() {
        return clanTag;
    }

    public boolean isExpired() {
        return System.currentTimeMillis() > expiresAt;
    }

    public long getExpiresAt() {
        return expiresAt;
    }
}