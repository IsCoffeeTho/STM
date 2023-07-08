> *Latest: `v1.1.0`*

# STM - Serve The Masses

Serve The Masses (STM) is a *Spigot* multiplayer plugin made as a way to enhance the multiplayer experience.

- Changelog [`view`](./Changelog.md)
- Commands [`view`](#commands)

## Commands
### Utility
- `/home` Teleports the player to their home bed
- `/heal <player>` Sets the players health to max

### Warps
- `/warp <name>` Warps the player to a named location.
- `/setwarp <name>` ***operator only*** Creates a warp location at the player.
- `/delwarp <name>` ***operator only*** Deletes a warp location.

### Goto
- `/goto <player>` Will send request to teleport to a player.
  **Note:** The player will get sent a messsage they can click to accept and deny the message.
- `/acceptgoto <player>` Accept a players request to teleport to you.
- `/denygoto <player>` Deny a players request to teleport to you.
