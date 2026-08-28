package com.zero.zClanSystem;

import com.zero.zClanSystem.clan.ClanManager;
import com.zero.zClanSystem.commands.ClanCommand;
import com.zero.zClanSystem.commands.ClanTabCompleter;
import com.zero.zClanSystem.files.FileManager;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUIBlockListener;
import com.zero.zClanSystem.gui.main.ClanMainMenuGUIClickListener;
import com.zero.zClanSystem.gui.main.Invites.ClanInvitesMenuGUIBlockListener;
import com.zero.zClanSystem.gui.main.Invites.ClanInvitesMenuGUIClickListener;
import com.zero.zClanSystem.gui.main.Invites.Search.ClanInvitesMenuSearchListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.MyClanInvitesGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.MyClanInvitesGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanInvites.Search.MyClanInvitesSearchListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.MyClanMembersGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.MyClanMembersGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMembers.Search.MyClanMembersSearchListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanMenuGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanHome.MyClanHomeGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanHome.MyClanHomeGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanName.ChangeClanNameInputListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.ChangeClanTag.ChangeClanTagInputListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.DisbandClan.MyClanSettingsDisbandClanGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.DisbandClan.MyClanSettingsDisbandClanGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.MyClanMembersSettingsOptionsGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.MyClanMembersSettingsOptionsGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.TransferOwnership.MyClanMembersSettingsOptionsTransferOwnershipGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MemberOptions.TransferOwnership.MyClanMembersSettingsOptionsTransferOwnershipGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MyClanMembersSettingsGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.MyClanMembersSettingsGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.Members.Search.MyClanMembersSettingsSearchListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanSettings.MyClanSettingsGUIClickListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanStats.MyClanStatsGUIBlockListener;
import com.zero.zClanSystem.gui.main.MyClan.MyClanStats.MyClanStatsGUIClickListener;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.Search.TopClansAllClansRankListSearchListener;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.TopClansAllClansRankListGUIBlockListener;
import com.zero.zClanSystem.gui.main.TopClans.AllClansRanklist.TopClansAllClansRankListGUIClickListener;
import com.zero.zClanSystem.gui.main.TopClans.ClanStats.TopClansStatsGUIBlockListener;
import com.zero.zClanSystem.gui.main.TopClans.ClanStats.TopClansStatsGUIClickListener;
import com.zero.zClanSystem.gui.main.TopClans.TopClansListGUIBlockListener;
import com.zero.zClanSystem.gui.main.TopClans.TopClansListGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.AdminMenuGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.AdminMenuGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.AdminClanModifyMenuGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.AdminClanModifyMenuGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.ModifyColor.ClanModifyColorGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanModify.ModifyColor.ClanModifyColorGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanSelectModifyGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.ClanSelectModifyGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.ClanSelect.Search.ClanSelectModifySearchListener;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.AdminForceDisbandGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.AdminForceDisbandGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.Apply.AdminForceDisbandApplyGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.ForceDisband.Apply.AdminForceDisbandApplyGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.KickPlayer.AdminKickPlayerGUIBlockListener;
import com.zero.zClanSystem.gui.main.admin.KickPlayer.AdminKickPlayerGUIClickListener;
import com.zero.zClanSystem.gui.main.admin.KickPlayer.Search.AdminKickPlayerSearchListener;
import com.zero.zClanSystem.gui.main.create.CreateChatInputListener;
import com.zero.zClanSystem.gui.main.create.CreateClanGUIBlockListener;
import com.zero.zClanSystem.gui.main.create.CreateClanGUIClickListener;
import com.zero.zClanSystem.listeners.ClanDamageListener;
import com.zero.zClanSystem.placeholders.ClanPlaceholder;
import com.zero.zClanSystem.listeners.PlayerDeathListener;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public class zClanSystem extends JavaPlugin {

    private static zClanSystem instance;

    public static zClanSystem getInstance() {
        return instance;
    }

    private FileManager fileManager;
    private ClanManager clanManager;

    public ClanManager getClanManager() {
        return clanManager;
    }

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        fileManager = new FileManager(this);
        clanManager = new ClanManager(fileManager, this);

        clanManager.startInviteExpirationTask();

        getCommand("clan").setExecutor(new ClanCommand(clanManager));
        getCommand("clan").setTabCompleter(new ClanTabCompleter(clanManager));

        register(new ClanMainMenuGUIClickListener(clanManager));
        register(new ClanMainMenuGUIBlockListener());

        register(new CreateClanGUIClickListener(clanManager));
        register(new CreateClanGUIBlockListener());
        register(new CreateChatInputListener(clanManager));

        register(new TopClansListGUIClickListener(clanManager));
        register(new TopClansListGUIBlockListener());

        register(new TopClansAllClansRankListGUIClickListener(clanManager));
        register(new TopClansAllClansRankListGUIBlockListener());
        register(new TopClansAllClansRankListSearchListener(clanManager));

        register(new TopClansStatsGUIClickListener(clanManager));
        register(new TopClansStatsGUIBlockListener());

        register(new MyClanMenuGUIClickListener(clanManager));
        register(new MyClanMenuGUIBlockListener());

        register(new MyClanMembersGUIBlockListener());
        register(new MyClanMembersGUIClickListener(clanManager));
        register(new MyClanMembersSearchListener(clanManager));

        register(new MyClanStatsGUIClickListener(clanManager));
        register(new MyClanStatsGUIBlockListener());

        register(new MyClanSettingsGUIClickListener(clanManager));
        register(new MyClanSettingsGUIBlockListener());
        register(new ChangeClanNameInputListener(clanManager));
        register(new ChangeClanTagInputListener(clanManager));

        register(new MyClanHomeGUIBlockListener());
        register(new MyClanHomeGUIClickListener(clanManager));

        register(new MyClanMembersSettingsGUIBlockListener());
        register(new MyClanMembersSettingsGUIClickListener(clanManager));
        register(new MyClanMembersSettingsSearchListener(clanManager));

        register(new MyClanMembersSettingsOptionsGUIBlockListener());
        register(new MyClanMembersSettingsOptionsGUIClickListener(clanManager));

        register(new MyClanSettingsDisbandClanGUIBlockListener());
        register(new MyClanSettingsDisbandClanGUIClickListener(clanManager));

        register(new MyClanMembersSettingsOptionsTransferOwnershipGUIBlockListener());
        register(new MyClanMembersSettingsOptionsTransferOwnershipGUIClickListener(clanManager));

        register(new MyClanInvitesGUIClickListener(clanManager));
        register(new MyClanInvitesGUIBlockListener());
        register(new MyClanInvitesSearchListener(clanManager));

        register(new ClanInvitesMenuGUIClickListener(clanManager));
        register(new ClanInvitesMenuGUIBlockListener());
        register(new ClanInvitesMenuSearchListener(clanManager));

        register(new AdminMenuGUIClickListener(clanManager));
        register(new AdminMenuGUIBlockListener());

        register(new AdminForceDisbandGUIClickListener(clanManager));
        register(new AdminForceDisbandGUIBlockListener());

        register(new AdminKickPlayerGUIClickListener(clanManager));
        register(new AdminKickPlayerGUIBlockListener());
        register(new AdminKickPlayerSearchListener(clanManager));

        register(new AdminForceDisbandApplyGUIClickListener(clanManager));
        register(new AdminForceDisbandApplyGUIBlockListener());

        register(new ClanSelectModifyGUIClickListener(clanManager));
        register(new ClanSelectModifyGUIBlockListener());
        register(new ClanSelectModifySearchListener(clanManager));

        register(new AdminClanModifyMenuGUIClickListener(clanManager));
        register(new AdminClanModifyMenuGUIBlockListener());

        register(new ClanModifyColorGUIClickListener(clanManager));
        register(new ClanModifyColorGUIBlockListener());

        register(new ClanDamageListener(clanManager));
        register(new PlayerDeathListener(clanManager));

        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            new ClanPlaceholder(clanManager).register();
        }
    }

    @Override
    public void onDisable() {
        if (clanManager != null) {
            clanManager.saveClans();
        }
    }

    private void register(Listener listener) {
        getServer().getPluginManager().registerEvents(listener, this);
    }
}
