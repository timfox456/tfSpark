# tfSpark

Tensorflow and Spark: Better Together

## Introduction

Tensorflow and Spark: two powrerful tools.

Tensorflow and Spark have a lot in common. Both are designed to be distributed engines for processing data.

The open-source tensorflow is awesome for what it does.  On Google Infrastrucutre, there is the excellent
Cloud ML Engine, which allows parallel training or deployment of tensorflow models.  However, On-Premesis
or non-Google infrastructure are not able to use Cloud ML Engine as it is not open-source.


## Scala and Tensorflow on Spark

I've included a couple of scala examples:

  * HelloTF : Hello World kind of stuff
  * LabeledImage : From the TF Java documentation

## Why Scala?

Why am I using Scala and not PySpark?  Well, first Spark is native to Scala; Python only exists as (much
slower) interface.   I wanted to use native Scala.

Then why not Java?  Well, Scala is much better than Java as far as Spark goes.  One can easily mix and 
match java classes with the scala if you prefer ajva.

