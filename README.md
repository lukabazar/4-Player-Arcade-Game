# CS351 Project 5
Donkey Kong Jr.

## Description
DonkeyKongJr.java is the client class of this program and launches a window with the instructions and the 
game window.
Server.java is the server class and is used to connect the 4 clients.

## Usage
To start the server: <br>
``
java -jar Server.jar <portNumber>
``

To start the clients: <br>
``
java -jar DonkeyKongJr.java <hostname> <portNumber>
``

Upon starting the client the instructions below are given in an additional window, this lays out the controls and 
the goals for the game.

    Instructions
    Jump to grab onto vines/chains, Junior climbs up very slow on one vine/chain but by reaching for
    another he climbs much faster, however holding onto only one vine/chain makes sliding down faster.
    The player with the flashing graphic overhead is your player!

    Game Modes
    Game B: A custom level for this project, Junior only has one life, drop on other players to take out fellow players,
    fruit respawns, and enemies spawn faster as the game progresses.
    Survive as long as possible, out last the other players, collect fruit, and drop 
    that fruit on enemies to get a high score before the enemies overwhelm Junior! 

    Controls
    Left/Right: Move right and left on the ground, or reach/grab a vine/chain
    Up/Down: Climb up and down the vines/chains
    Space: Jump
    Enter: Start the game with the highlighted selection on title screen  

## Contributions
Original project code: Luka Bazar <br>
Networking:<br>
Player collision:<br>
Multiplayer scoring:

## Credits
The backgrounds, the fruit sprites, and the Donkey Kong Jr. sprites are from 
https://www.spriters-resource.com/nes/donkeykongjr/ and edited by Luka Bazar (creating transparent backgrounds and 
other) The flashing current player graphic is also from this; edited by Roger Davis.
<br>
<br>
The title screen was from https://tcrf.net/File:DKJrNES-Title.png and also edited by Luka Bazar.

 
