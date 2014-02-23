sparse_visualizer
=====================

Audio reactive and performance processing sketch. Built to be performed visually in tandem with the Sparse song. This repo is actually two separate visualizers, built by Xiaohan Zhang (who created Reactive Superformula) and Meena Vempaty (who created [visualinstrument](http://www.multimediaorchestra.com/projects/visualinstrument).

##How to run?

Download the app for Windows (32bit, 64bit) or Mac and run the application. Move your mouse to change what the blob looks like. Press left-shift to toggle between Reactive Superformula and visualinstrument.

## How to build? 

Prerequisites for Superformula:

1. Processing
2. PeasyCam for Processing
3. oscP5 for Processing

Now, to run:

1. ```git clone https://github.com/Multimedia-Orchestra/sparse_visualizer && cd sparse_visualizer```
2. Open Superformula.pde in Processing

**Note**: To get audio reaction, you need to send OSC messages to Processing through an external program (most likely Max MSP). To help with this I've added a ```send_to_processing.maxpat``` Max Patch with 2 audio signal inlets that will automatically send audio data into Processing. Look in ```example.maxpat``` for example usage. 

## Help!

Email me at hellocharlien@hotmail.com if you run into any problems!
