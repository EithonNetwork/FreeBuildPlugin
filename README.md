# FreeBuild

A FreeBuild plugin for Minecraft; Like gamemode survival, but you are not attacked by mobs (and can't attack them) and you don't get damaged

## Prerequisits

* None

## Functionality

The plugin provides administrative commands for going in and out of FreeBuild mode.

### Administrative commands

* on: Go into FreeBuild mode
* off: Leave FreeBuild mode

### What is FreeBuild mode?

When the user changes to FreeBuild mode, the following applies:

* The user can't be attacked by monsters
* The user can't attack monsters
* The user can't be damaged

## Release history

### 2.3.2 (2015-04-15)

* BUG: Forgot to activate cooldown after ON/OFF

### 2.3.1 (2015-04-15)

* NEW: Added a permission to avoid cool down period
* BUG: Could not use ON/OFF

### 2.3 (2015-04-14)

* NEW: Added a configurable cooldown period. 

### 2.2.1 (2015-04-14)

* BUG: Should not prohibits fly in the non-free builder worlds. 

### 2.2 (2015-04-13)

* NEW: Limit freebuild to configured worlds.
* NEW: Permission freebuild.inallworlds means the player can use the freebuild command in all worlds
* BUG: Syntax error in config.yml

### 2.1 (2015-04-13)

* NEW: Permission freebuild.canfly means the player can fly in survival mode

### 2.0 (2015-04-12)

* NEW: Release

### 1.6 (2015-04-09)

* NEW: Survival mode players can't fly

### 1.5 (2015-04-09)

* NEW: Freebuilders can't splash other persons and other persons can't splash Freebuilders.

### 1.4 (2015-04-04)

* NEW: Now uses EithonLibraryPlugin.

### 1.3.1 (2015-04-03)

* CHANGE: New plugin tools

### 1.2 (2015-01-06)

* CHANGE: Now uses PluginTools.

### 1.1.1 (2015-01-03)

* BUG: Conflicted with the JumpPadPlugin, so loading plugin failed

### 1.1 (2014-12-27)

* NEW: FreeBuild player can't be damaged

### 1.0 (2014-12-27)

* NEW: First working version.
