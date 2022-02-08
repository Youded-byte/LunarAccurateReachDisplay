# Lunar Client Accurate Reach Display

This agent removes the cap of 3.0 reach in the reach display mod included in lunar client. This cap is unnecesarry and even misleading, as on rare occasions a hit can be landed at a slightly larger distance.
This agent also removes the rounding of reach if the decimal ends with a zero, e.g. 0.00 being rounded to 0, as it can be unwanted for the size of the box to change frequently.

## Usage of this mod

This agent can be used easily by using [lunar-client-qt](https://github.com/Youded-byte/lunar-client-qt).

## Options
You can pass the option/argument "round" in order for the agent **not** to change the rounding system.