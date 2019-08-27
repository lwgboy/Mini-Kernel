#!/bin/bash
lsusb -v | grep -E "Bus|iSerial|^$" | grep -E "Device|iSerial|^$"
