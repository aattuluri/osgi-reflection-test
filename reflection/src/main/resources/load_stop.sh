#!/bin/bash
ps aux | grep -ie curl | awk '{print $2}' | xargs kill -9 
