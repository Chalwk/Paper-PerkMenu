PerkMenu is a sophisticated permission-based perk management system, featuring a fully customizable GUI that allows
players to view and interact with their available perks organized by categories.

![Paper](https://img.shields.io/badge/Paper-1.21.4-green?logo=paper&logoColor=white)
[![MIT License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![Latest Release](https://img.shields.io/github/v/release/Chalwk/Paper-PerkMenu?sort=semver)](https://github.com/Chalwk/Paper-PerkMenu/releases/latest)

## Features

- **Multi-Category System**: Organize perks into categories like Survival, Kits, McMMO, Jobs, and Cosmetics
- **Permission-Based Access**: Each perk and category can have individual permission requirements
- **Fully Customizable GUI**: Configurable rows, layouts, colors, and navigation
- **Paginated Display**: Automatic pagination for categories with many perks
- **Interactive Perks**: Click on perks to activate commands, messages, or sounds
- **Visual Polish**: Clean interface with hover descriptions and color-coded categories
- **Admin Controls**: Reload configuration and manage permissions
- **Empty Slot Management**: Fill empty slots with decorative glass panes

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
3. **Perk Display**: Perks appear only if you have the required permission
4. **Navigation Controls**:
    - **Previous/Next Arrows**: Navigate between pages of perks
    - **Back Button**: Return to category selection
    - **Hover Descriptions**: View detailed perk information by hovering

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
    cost: 0  # Cost to activate (future feature)

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
- Click "Back" to return to categories

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

## License

Licensed under the [MIT License](LICENSE).