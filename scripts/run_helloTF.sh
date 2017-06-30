#!/bin/bash
~/spark/bin/spark-submit --class 'com.createksolutions.tfSpark.HelloTF' --master local[*]  target/scala-2.11/tfSpark-assembly-1.0.jar
