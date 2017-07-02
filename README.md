# BABBQastPresentation
Code for a presentation made at the Big Android BBQ Amsterdam 2015

This project was written as a test project for my presentation about the latest (mid 2015) ChromeCast API's made available for Android. 
It uses the then new API's to create an Android app which casts the contents of a CastPresentation View to the remote ChromeCast display.
As you will see, it allows any content to be cast to the remote display; the view doesn't even have to be visible on the Android device 
casting the content (although it could be mirrored there).

This was originally based on code I had written pre-CastPresentation class; the original used the undocumented Presentation class to cast 
it's contents to the ChromeCast using the Presentation class. It was the only way to do this and didn't even bneed a developer enabled 
ChromeCast to function. 
This code does need a dev enabled Cast. Please supply your own ChromeCast dev id in the LandscapeActivity.java file.

The code is primarily meant to show how to use the CastPresentation class. It updates the remote view using a really nasty updating 
mechanism at different FPS's, but it does show what it can do. Improvements would be using a surfaceview with better update 
mechanisms (dirty rectangles etc).
