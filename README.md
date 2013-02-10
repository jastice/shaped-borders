Shaped Borders
==============

Shaped Borders is a world border plugin based on (WorldBorder)[http://dev.bukkit.org/server-mods/worldborder/], redesigned to support additional border shapes, and easily add new ones. Currently supported border shapes are rectangular, circle, and composite. Composite shapes allow you to add multiple border shapes of any type to a world to create a single more complex border.

*Warning: ALPHA version* - not intended for production use.

Goals
-----
* support full feature set from WorldBorder (currently not fully implemented)
* maintain WorldBorder performance
* flexible border shape setup
* easily add new border shapes

Features
--------
* setup up one border per world
* setup border in rectangular, circle and composite shapes
* allow border bypass for players via permissions

Configuration
-------------
Configuration is currently only possible via the plugin config file. A default configuration is written to the plugin directory after the first start.

Example configuration file:

    # how many world ticks to wait between checking player locations
    timerTicks: 5
    # how far to knock a player back into the border when caught outside
    knockback: 3
    # show an effect when knocking player back
    whooshEffect: true

    # world border setup
    borders:
      # the main world is a small rectangle
      world:
        ==: rectangle
        xMin: -50.0
        xMax: 50.0
        zMin: -100.0
        zMax: 200.0
      # the nether is limited by a circle with the redius of 1200
      world_nether:
        ==: circle
        xCenter: 0
        yCenter: 0
        radius: 1200

      # a rectangular polygon for world "creative"
      creative:
        ==: composite
        borders:
          - 
            ==: rectangle
            xMin: 0
            xMax: 500
            zMin: -1000
            zMax: 1000
          -
            ==: rectangle
            xMin: 500
            xMax: 1500
            zMin: 500
            zMax: 2000



Permissions
-----------
* `shapedborders.admin` Allow shaped borders commands. Default: op only
* `shapedborders.bypass` Allow bypassing of borders. Default: false
