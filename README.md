<img src="./assets/essentialsx-gui.png" alt="Logo" style="max-width:60%;">

# EssentialsX-GUI

EssentialsX-GUI is an **unofficial** EssentialsX addon that adds some GUIs for Essentials features, like homes, kits, warps, whois, etc..

[![Modrinth Downloads](https://img.shields.io/modrinth/dt/essentialsx-gui?style=for-the-badge&logo=modrinth&color=24b473)](https://modrinth.com/plugin/essentialsx-gui)
[![Spigot Downloads](https://img.shields.io/spiget/downloads/127805?style=for-the-badge&logo=spigotmc&color=fba905)](https://www.spigotmc.org/resources/127805/)
[![GitHub Download](https://img.shields.io/github/downloads/SniperTVmc/EssentialsX-GUI/total?style=for-the-badge&logo=github&color=dddddd)](https://github.com/SniperTVmc/EssentialsX-GUI/releases/latest)

[![License](https://img.shields.io/badge/license-GNU%20GPL%20v3-blue?style=for-the-badge)](https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/LICENSE)
[![CodeFactor](https://www.codefactor.io/repository/github/snipertvmc/essentialsx-gui/badge?style=for-the-badge)](https://www.codefactor.io/repository/github/snipertvmc/essentialsx-gui)
[![Issues](https://img.shields.io/github/issues/SniperTVmc/EssentialsX-GUI?style=for-the-badge)](https://github.com/SniperTVmc/EssentialsX-GUI/issues)

## ✨ | Features

### General features:
- Customizable permissions.
- Database support: SQLite, MySQL, MariaDB (preferred over MySQL)
- All messages are customizable.
- Lightweight and high-performance.
- Clean and structured code.
- Update checker.
- Cross-server support: Java Edition <!-- and Bedrock Edition. *(Requires GeyserMC plugin)* -->
- Compatible with most server types: Spigot, Paper, Purpur, Leaf, etc.

### Homes:
<details><summary>Click to view all homes features.</summary>
<ul>
  <li>Customize the appearance of your home in the homes' GUI.<ul>
      <li>Customizable home display name.</li>
      <li>Customizable home display icon.</li>
    </ul></li>
  <li>Chose the way you want to customize home item.<ul>
      <li>Using the chat.</li>
      <li>Using an anvil.</li>
      <li>Using a GUI. <em>(Icons only)</em></li>
      <li>Using item in hand. <em>(Icons only)</em></li>
    </ul></li>
  <li>Unique item for bed home.<ul>
      <li>Possibility to customize the item according to the home's world.</li>
    </ul></li>
  <li>Per-player home item editor, with preview home item.</li>
  <li>Infinite number of homes supported. <em>(Integrated pagination system)</em></li>
  <li>Play custom sounds when an action is performed.</li>
  <li>Home creation and deletion in a GUI.</li>
  <li>Search for home by name.</li>
</ul>

> **Supported EssentialsX features (2/2):**
> - Essentials Homes
> - Home limit per player.
</details>

### Kits:

<details><summary>Click to view all kits features.</summary>
<ul>
  <li>Customize the appearance of your kits in the kits' GUI.<ul>
      <li>Customizable kit display name.</li>
      <li>Customizable kit display icon.</li>
    </ul></li>
  <li>Choose the way you want to customize kit item.<ul>
      <li>Using the chat.</li>
      <li>Using an anvil.</li>
      <li>Using a GUI. <em>(Icons only)</em></li>
      <li>Using item in hand. <em>(Icons only)</em></li>
    </ul></li>
  <li>Admin view to manage kits.<ul>
      <li>Choose a player and give him a kit.</li>
      <li>Per-kit content editor.</li>
      <li>Kit creation and deletion in a GUI.</li>
    </ul></li>
  <li>Player view to see owned kits.<ul>
      <li>Give kit to player by clicking on it.</li>
      <li>Kits preview in a GUI.</li>
    </ul></li>
  <li>Infinite number of kits supported. <em>(Integrated pagination system)</em></li>
  <li>Play custom sounds when an action is performed.</li>
  <li>Search for kit by name.</li>
</ul>

> **Supported EssentialsX features (3/3):**
> - Essentials Kits
> - Cooldown for each kit.
> - Permission for each kit.
</details>

### Warps:

<details><summary>Click to view all warps features.</summary>
<ul>
  <li>Customize the appearance of your warps in the warps' GUI.<ul>
      <li>Customizable warp display name.</li>
      <li>Customizable warp display icon.</li>
    </ul></li>
  <li>Choose the way you want to customize warp item.<ul>
      <li>Using the chat.</li>
      <li>Using an anvil.</li>
      <li>Using a GUI. <em>(Icons only)</em></li>
      <li>Using item in hand. <em>(Icons only)</em></li>
    </ul></li>
  <li>Admin view to manage warps.<ul>
      <li>Choose a player and teleport him to the warp.</li>
      <li>Warp creation and deletion in a GUI.</li>
    </ul></li>
  <li>Player view to see warps with access.<ul>
      <li>Teleport player to the warp by clicking on it.</li>
      <li>Warps preview in a GUI.</li>
    </ul></li>
  <li>Infinite number of warps supported. <em>(Integrated pagination system)</em></li>
  <li>Play custom sounds when an action is performed.</li>
  <li>Search for warp by name.</li>
</ul>

> **Supported EssentialsX features (2/2):**
> - Essentials Warps.
> - Permission for each warp.
</details>

### /whois command:
<details><summary>Click to view all /whois features.</summary>
<ul>
  <li>Display player's information in a GUI.</li>
  <li>Player choice GUI when no player is specified.</li>
  <li>Play custom sounds when an action is performed.</li>
  <li>Players data divided into categories for better readability.<ul>
      <li><strong>Player identification :</strong> <code>Name</code>, <code>UUID</code>, <code>IP Address</code>, <code>Playtime</code></li>
      <li><strong>Player statistics :</strong> <code>Health</code>, <code>Hunger</code>, <code>Experience</code>, <code>Level</code></li>
      <li><strong>Player world :</strong> <code>World</code>, <code>Coordinates (X, Y, Z, Yaw, Pitch)</code></li>
      <li><strong>Player server data :</strong> <code>Gamemode</code>, <code>Flying status</code>, <code>Operator status</code>, <code>Vanished status</code>, <code>AFK status</code>, <code>Nickname</code></li>
      <li><strong>Player punishments :</strong> <code>Jail status</code>, <code>Mute status</code>, <code>Ban status</code></li>
    </ul></li>
</ul>

> **Supported EssentialsX features (1/1):**
> - Show player's IP address only with permission.
</details>

### Punishment commands:
- 🚧 ~~Available in an upcoming update.~~

## 👀 | Preview / Screenshots

### __1. Home creation__
<details>
  <br>
  <img src=https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/assets/preview/home-creation.gif?raw=true alt="Home creation">
  <br>
</details>

### __2. Home search__
<details>
  <br>
  <img src=https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/assets/preview/home-search.gif?raw=true alt="Home creation">
  <br>
</details>

### __3. Bed home item__
<details>
  <br>
  <img src=https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/assets/preview/bed-home-item.gif?raw=true alt="Home creation">
  <br>
</details>

### __4. Kit creation__
<details>
  <br>
  <img src=https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/assets/preview/kit-creation.gif?raw=true alt="Home creation">
  <br>
</details>

### __5. Home preview__
<details>
  <br>
  <img src=https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/assets/preview/kit-preview.gif?raw=true alt="Home creation">
  <br>
</details>

### __6. Kit content editor__
<details>
  <br>
  <img src=https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/assets/preview/kit-content-editor.gif?raw=true alt="Home creation">
  <br>
</details>

## 🔨 | How to install EssentialsX-GUI ?

Requirements:

- Minecraft Server: **Java Edition** <!-- Bedrock Edition is partially supported. *(Requires GeyserMC plugin)* -->
- Supported Versions: **1.8.8 to 26.2+**
- Java Version: **21+**
- EssentialsX Version: **2.21.2+**

> For more help, please visit the [wiki](https://sniper-tvmc.gitbook.io/essentialsx-gui/installation/prerequisites).

## 📢 | Servers using EssentialsX-GUI

Are you using our plugin and would like to add your server to the list?
Join [Discord Server](https://discord.gg/fSzK79TAYf).

## ❓ | Support

For support, join [Discord Server](https://discord.gg/fSzK79TAYf).

## 👥 | Contributors

- **Sniper_TVmc** — Creator, lead developer and project manager.

## 📝 | License

This project is licensed under the
[GNU General Public License v3.0](https://github.com/SniperTVmc/EssentialsX-GUI/blob/Main/LICENSE).<br>
In the same way as the plugin, EssentialsX is licensed under the
[GPL-3.0](https://github.com/EssentialsX/Essentials/blob/2.x/LICENSE).

## ⚠️ | Disclaimer

**EssentialsX-GUI** is an **independent**, **unofficial addon** developed by [Sniper_TVmc](https://github.com/SniperTVmc),
designed to provide graphical interfaces for some EssentialsX commands and features.<br>
This project is **not affiliated, associated, authorized, endorsed by, or in any way officially connected**
with EssentialsX or any of its contributors.<br>
All references to EssentialsX are for compatibility and integration purposes only.

Consequently, EssentialsX-GUI is not responsible for any issues or bugs that may arise from the use of EssentialsX.<br>
And EssentialsX is not responsible for any issues or bugs that may arise from the use of EssentialsX-GUI.