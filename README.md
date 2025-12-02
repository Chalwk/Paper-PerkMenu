PerkMenu is a sophisticated permission-based perk management system, featuring a fully customizable GUI that allows
players to view and interact with their available perks organized by categories.

![Paper](https://img.shields.io/badge/Paper-1.21.4-green?logo=paper&logoColor=white)
[![MIT License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Latest Release](https://img.shields.io/github/v/release/Chalwk/Paper-PerkMenu?sort=semver)](https://github.com/Chalwk/Paper-PerkMenu/releases/latest)

## Features

- **Multi-Category System**: Organize perks into categories like Survival, Kits, McMMO, Jobs, and Cosmetics
- **Advanced Permission System**: Support for both single permission and multiple permission requirements
- **Fully Customizable GUI**: Configurable rows, layouts, colors, and navigation
- **Paginated Display**: Automatic pagination for categories with many perks
- **Interactive Perks**: Click on perks to activate commands, messages, or sounds
- **Admin Controls**: Reload configuration and manage permissions
- **Back Button Navigation**: Fixed navigation for returning to category menu

## Recent Updates

### Version 1.0.0

- **Fixed Back Button**: Back button now correctly returns to categories menu
- **Multiple Permission Support**: Perks can now require multiple permissions (AND logic - player must have ALL
  specified permissions)
- **Enhanced Permission Display**: When a perk has multiple permissions, they are shown in the perk description

## Commands

### Main Commands

- `/perks` or `/perkmenu` - Open the perk menu to view available perks
- `/pm` - Short alias for the perk menu

### Admin Commands

- `/perks reload` - Reload the plugin configuration (requires `perkmenu.admin`)
- `/perks help` - Show command help

## Permissions

### Core Permissions

- `perkmenu.use` - Allows using the perk menu (default: true)
- `perkmenu.admin` - Access to admin management commands (default: op)

### Category Permissions

- `perkmenu.category.mcmmo` - Access to McMMO category (default: op)
- `perkmenu.category.jobs` - Access to Jobs category (default: op)
- `perkmenu.category.cosmetics` - Access to Cosmetics category (default: op)

### Perk Permissions (Examples)

- `perkmenu.perk.magnet` - Access to Magnet perk (default: op)
- `perkmenu.perk.autosmelt` - Access to Auto-Smelt perk (default: op)
- `perkmenu.perk.doubledrops` - Access to Double Drops perk (default: op)
- `perkmenu.perk.starterkit` - Access to Starter Kit perk (default: true)
- `perkmenu.perk.vipkit` - Access to VIP Kit perk (default: false)

### Wildcard Permissions

- `perkmenu.*` - All PerkMenu permissions

## GUI Navigation

The PerkMenu interface is designed for intuitive navigation:

1. **Category Selection**: First view shows available categories
2. **Category Click**: Click a category to view its perks
3. **Perk Display**: Perks appear only if you have the required permission(s)
4. **Navigation Controls**:
    - **Previous/Next Arrows**: Navigate between pages of perks
    - **Back Button**: Return to category selection (fixed in latest version)
    - **Hover Descriptions**: View detailed perk information by hovering
5. **Permission Indicators**: Perks requiring multiple permissions show their requirements in the description

## Configuration

The plugin generates a `config.yml` file with extensive customization options:

### GUI Settings

```yaml
gui:
  rows: 6  # Number of rows (1-6)
  title: "&6&lPerk Menu"
  fill_empty: "GRAY_STAINED_GLASS_PANE"  # Material for empty slots
  fill_empty_name: "&7"  # Name for empty slots
```

### Navigation Items

```yaml
navigation:
  previous:
    material: "ARROW"
    name: "&aPrevious Page"
    slot: 45
  next:
    material: "ARROW"
    name: "&aNext Page"
    slot: 53
  back:
    material: "BARRIER"
    name: "&cBack to Categories"
    slot: 49
```

### Category Configuration

```yaml
categories:
  survival:
    display_name: "&a&lSurvival"
    icon: "GRASS_BLOCK"
    description: "Survival-related perks"
    slot: -1  # -1 for automatic placement
    permission: ""  # Empty for no permission required
  kits:
    display_name: "&b&lKits"
    icon: "CHEST"
    description: "Kit-based perks"
    slot: -1
    permission: ""
  mcmmo:
    display_name: "&e&lMcMMO"
    icon: "DIAMOND_PICKAXE"
    description: "McMMO skill perks"
    slot: -1
    permission: "perkmenu.category.mcmmo"
```

### Perk Configuration

```yaml
perks:
  # Single permission
  magnet:
    category: "survival"
    permission: "perkmenu.perk.magnet"
    display_name: "&e&lMagnet"
    description: "Automatically picks up items around you"
    icon: "HOPPER"
    enabled: true
    action:
      type: "command"  # command, message, or sound
      value: "mag give {player}"
      sound: "BLOCK_NOTE_BLOCK_PLING"
    cost: 0

  # Multiple permissions (NEW FEATURE)
  some_perk:
    category: "some_category"
    permission:
      - some1.perm
      - some2.perm
      - some3.perm
    display_name: "&e&lSpecial Perk"
    description: "Requires multiple permissions"
    icon: "DIAMOND"
    enabled: true
    action:
      type: "command"
      value: "somecommand {player}"
      sound: "BLOCK_NOTE_BLOCK_PLING"
    cost: 0

  autosmelt:
    category: "survival"
    permission: "perkmenu.perk.autosmelt"
    display_name: "&6&lAuto-Smelt"
    description: "Automatically smelts ores when mined"
    icon: "FURNACE"
    enabled: true
    action:
      type: "message"
      value: "&aAuto-Smelt perk activated!"
      sound: "BLOCK_NOTE_BLOCK_PLING"
    cost: 100
```

## Action Types

Perks can perform different actions when clicked:

1. **Command**: Execute a console command
   ```yaml
   action:
     type: "command"
     value: "kit vip {player}"
   ```

2. **Message**: Send a message to the player
   ```yaml
   action:
     type: "message"
     value: "&aPerk activated!"
   ```

3. **Sound**: Play a sound effect
   ```yaml
   action:
     type: "sound"
     value: "ENTITY_PLAYER_LEVELUP"
   ```

## Multiple Permission Requirements

### New Feature: Multiple Permissions per Perk

Perks can now require multiple permissions using YAML list syntax:

```yaml
premium_perk:
  category: "survival"
  permission:
    - premium.rank
    - perk.premium.access
    - world.survival
  display_name: "&6&lPremium Feature"
  description: "Exclusive premium feature with multiple requirements"
  icon: "NETHER_STAR"
  enabled: true
  action:
    type: "command"
    value: "give {player} diamond 64"
```

### How It Works

1. **AND Logic**: Player must have **ALL** permissions listed to see and use the perk
2. **Visual Feedback**: Required permissions are displayed in the perk's lore
3. **Backward Compatibility**: Single string permissions still work as before
4. **Empty Permission**: If permission is empty string or omitted, no permission is required

### Example Use Cases

```yaml
# Perk requiring both a rank and a specific world
world_specific_perk:
  permission:
    - rank.vip
    - world.nether

# Perk requiring multiple skill permissions
advanced_perk:
  permission:
    - skill.mining.expert
    - skill.combat.master
    - level.50

# Perk requiring both plugin permissions
cross_plugin_perk:
  permission:
    - jobs.repairman
    - mcmmo.super_breaker
```

## Usage Examples

### Basic Usage

```bash
# Open the perk menu
/perks

# Navigate through categories
- Click "Survival" category
- View available survival perks
- Click "Magnet" to activate it
- Use arrows to navigate pages
- Click "Back" to return to categories (now fixed!)

# Alternative command
/pm
```

### Admin Usage

```bash
# Reload configuration after making changes
/perks reload

# View command help
/perks help
```

### Adding New Perks

To add a new perk, add it to your `config.yml`:

```yaml
my_new_perk:
  category: "survival"
  permission: "perkmenu.perk.mynewperk"
  display_name: "&d&lMy New Perk"
  description: "A brand new perk with custom features"
  icon: "NETHER_STAR"
  enabled: true
  action:
    type: "command"
    value: "mycommand {player}"
```

For perks requiring multiple permissions:

```yaml
my_multi_perk:
  category: "survival"
  permission:
    - permission.one
    - permission.two
    - permission.three
  display_name: "&d&lMulti-Permission Perk"
  description: "This perk requires three permissions"
  icon: "NETHER_STAR"
  enabled: true
  action:
    type: "command"
    value: "multicommand {player}"
```

## Building the Plugin

### Prerequisites

- Java 21 or higher
- Gradle 8.0 or higher

### Manual Build

1. Clone the repository
2. Navigate to the project directory
3. Run the following command:

```bash
./gradlew build
```

4. The compiled JAR will be available in `build/libs/PerkMenu-1.0.0.jar`

## Installation

1. Download the latest JAR file or build it yourself
2. Place the JAR file in your server's `plugins` folder
3. Restart or reload your server
4. Configure the plugin in `plugins/PerkMenu/config.yml`
5. Set up appropriate permissions for your players
6. Enjoy the perk management system!

## Customization

### Adding New Categories

Extend the `categories` section in `config.yml`:

```yaml
my_new_category:
  display_name: "&5&lMy Category"
  icon: "BEACON"
  description: "Custom category for special perks"
  slot: 22  # Specific slot number (0-53)
  permission: "perkmenu.category.mynewcategory"
```

### Custom Icons

Use any Minecraft material name for icons. Some popular choices:

- `DIAMOND` - Premium perks
- `EMERALD` - Economy perks
- `NETHER_STAR` - Special abilities
- `ENCHANTED_BOOK` - Skill perks
- `TOTEM_OF_UNDYING` - Survival perks
- `ENDER_CHEST` - Storage perks
- `EXPERIENCE_BOTTLE` - Experience perks

### Formatting Colors

Use Minecraft color codes (&) in display names and descriptions:

- `&0` - Black
- `&1` - Dark Blue
- `&2` - Dark Green
- `&3` - Dark Aqua
- `&4` - Dark Red
- `&5` - Dark Purple
- `&6` - Gold
- `&7` - Gray
- `&8` - Dark Gray
- `&9` - Blue
- `&a` - Green
- `&b` - Aqua
- `&c` - Red
- `&d` - Light Purple
- `&e` - Yellow
- `&f` - White
- `&l` - **Bold**
- `&n` - <u>Underline</u>
- `&o` - *Italic*
- `&k` - Obfuscated

## Troubleshooting

### Common Issues

1. **Back button not working**: Ensure you have the latest version (1.0.1+)
2. **Multiple permissions not working**: Check YAML syntax - permissions must be a list with `-` bullet points
3. **Perk not showing**: Verify the player has ALL required permissions for multi-permission perks
4. **GUI not opening**: Check console for errors and ensure `perkmenu.use` permission is set

### Debug Tips

- Use `/perks reload` after configuration changes
- Check server console for loading errors
- Verify YAML indentation is correct
- Test permissions individually before combining them

## License

Licensed under the [MIT License](LICENSE)