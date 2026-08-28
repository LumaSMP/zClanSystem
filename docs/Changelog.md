# Changelog ClanSystem

### 5.1.5-alpha build #1
 - added GUI
 - added MainGUI
 - added AdminGUI
 - added createClanGUI
 - added forceDisbandGUI
 - added forceDisbandApplyGUI
 - improved config

### 5.1.5-alpha+build3
 - improved BlockListener

### 5.1.5-alpha+build4
 - improved BlockListener

### 5.1.5-alpha+build5-----[success]
 - final BlockListener on all GUI's

### 5.1.6-alpha+build1
 - tried to fix ClanForceDisbandGUI bug

### 5.1.6-alpha+build2
 - tried to fix ClanForceDisbandGUI bug

### 5.1.6-alpha+build3
 - tried to fix ClanForceDisbandGUI bug
 - reworked BlockListener logic

### 5.1.6-alpha+build4
 - tried to fix ClanForceDisbandGUI bug
 - reworked ClickListener logic 

### 5.1.6-alpha+build5
 - tried to fix ClanForceDisbandGUI bug
 - added ForceDisband notify for clan members in ClanManager

### 5.1.6-alpha+build5
 - reworked all ClickListener logic
 - reworked all BLockListener logic

### 5.1.6-alpha+build6-----[success]
 - fixed GUIUtils bug (had to rework ALL FCKING GUI's)

### 5.1.6-alpha+build9
 - added createClan cost (MONEY)
 - added createClan cost (ITEM)
 - added Payment button in create Clan
 - reworked config
 - added Vault as soft dependency

### 5.1.6-alpha+build10
 - removed Vault due to errors and outdated API's

### 5.1.6-alpha+build11
- fixed Payment button in createClanGUI

### 5.1.6-alpha+build12-----[success]
- reworked clanCreateGUI

### 5.1.6-alpha+build13-----[success]
- User feedback in GUI

### 5.1.6-alpha+build14
- first KickPlayerGUI

### 5.1.6-alpha+build15
- forgot GUI entrance in AdminMenuGUIClickListener

### 5.1.6-alpha+build16-----[success]
- replaced GRAY_PANES with RED_PANES in ClanForceDisbandGUI
- set KickPlayerGUI mode to slot 51 (prev: 48)
- added Search button to KickPlayerGUI on slot 47

### 5.1.6-alpha+build17
- added logic behind KickPlayerGUI

### 5.1.6-alpha+build18
- added kickPlayer logic
- added kickPlayer in ClanManager

### 5.1.6-alpha+build19
- added Search function & logic

### 5.1.6-alpha+build20
- switched search logic to main thread

### 5.1.6-alpha+build21
- forgot listener import in main class

### 5.1.6-alpha+build22
- fixed search

### 5.1.6-alpha+build23-----[success]
- improved player selection in ClanKickPlayerGUI
- deleted mode: clan in ClanKickPlayerGUI
- deleted ClanKickPlayerClanGUI
- added reloadConfig and reloadClans button in AdminMenuGUI

### 5.1.6-alpha+build24
- added logic behind reloadConfig and reloadClans button
- added logic behind back button in AdminMenuGUI

### 5.1.6-alpha+build25
- improved reload logic

### 5.1.6-alpha+build26-----[success]
- fixed reload logic

### 5.1.6-alpha+build27-----[success]
- added file-version to config and clans.yml

### 5.1.6-alpha+build28
- added ClanSelect and ClanModify

### 5.1.6-alpha+build29
- added Listener in onEnable
- fixed listener bug

### 5.1.6-alpha+build30
- fixed back button in createGUI
- removed bottom row names in list GUI's

### 5.1.6-alpha+build31
- fixed player-head bug in ClanSelectModifyGUI

### 5.1.6-alpha+build32
- modifyClanMenu implemented

### 5.1.6-alpha+build34
- AdminClanModifyMenuGUI and logic implemented
- ClanModifyColorGUI framework added

### 5.1.6-alpha+build35
- added real ClanModifyColorGUI interface (no logic yet)

### 5.1.6-alpha+build36-----[success]
- improved ClanModifyColorGUI

### 5.1.6-alpha+build37
- added ClanModifyColorGUI logic

### 5.1.6-alpha+build38
- improved ClanModifyColorBlockListener

### 5.1.6-alpha+build39
- fixed ClanModifyColorBlockListener

### 5.1.6-alpha+build40
- fixed ClanModifyColorBlockListener

// prob start of doubled output bug

### 5.1.6-alpha+build41-----[success]
- fixed ClanModifyColorBlockListener final -> forgot register in main class

### 5.1.6-alpha+build42
- added first version of TopClansListGUI

### 5.1.6-alpha+build43
- forgot register and ClanMainMenuGUI open

### 5.1.6-alpha+build44----[success]
- fixed Info lore in TopClansListGUI
- fixed Back lore in TopClansListGUI
- added clanheads to clans

### 5.1.6-alpha+build45
- added first version of TopClansAllClansRankListGUI

### 5.1.6-alpha+build46-----[success]
- fixed pages and buttons in /AllClansRanklist
- fixed pages in /ClanSelect, /ForceDisband & /KickPlayer

### 5.1.6-alpha+build47-----[success]
- search function added to /AllClansRanklist

### 5.1.6-alpha+build48-----[success]
- added ClanStats GUI
- implemented ClanStatsGUI into /AllClansRanklist

### 5.1.6-alpha+build49----[success]
- ClanStatsGUI in TopClansStatsGUI

### 5.1.6-alpha+build50
- Fixed clanColor names

### 5.1.6-alpha+build51----[success]
- Fixed clanColor names
- Fixed doubled output Bug (doubled Listener registration)

### 5.1.6-alpha+build52----[success]
- added config scores (Config ver.2.0)
- renamed plugin: clanSystemPlugin -> zClanSystem
- added /MyClan
- added Invites (Clans ver.2.0)

### 5.1.6-alpha+build53-----[success]
- added MyClanStatsGUI

### 5.1.6-alpha+build54-----[success]
- added MyClanSettingsGUI

### 5.1.6-alpha+build55-----[success]
- added ChangeClanName and ChangeClanTag to /MyClanSettings/

### 5.1.6-alpha+build56
- added /clan sethome/home/delhome command to system: GUI
- added /ChangeClanHome

### 5.1.6-alpha+build57-----[success]
- reworked new commands

### 5.1.6-alpha+build58
- added MyClanMembersSettingsGUI in /Members

### 5.1.6-alpha+build59-----[success]
- fixed entrance to MyClanMembersSettingsGUI in /MyClanSettings

### 5.1.6-alpha+build60------[success]
- MyClanMembersSettingsGUI fixes
- MyClanMembersSettings /Search

### 5.1.6-alpha+build61-------[success]
- MyClanSettingsGUIClickListener

### 5.1.6-alpha+build62-------[success]
- LeaveButton in MyClanMenuGUI (only for !Owner)
- TransferOwnership and more in /MemberOptions

### 5.1.6-alpha+build63
- fixed disband bug -> forceDisband(clan) -> clanDisband(clan)

### 5.1.6-alpha+build64-----[success]
- fixed clanLoad() bug

### 5.1.6-alpha+build65
- added invites to myclan and to the main menu
- added invite logic

### 5.1.6-alpha+build66
- register added
- entrance added

### 5.1.6-alpha+build67
- fixed Back button in myClan->Invites
- added myClan->Members GUI and Search