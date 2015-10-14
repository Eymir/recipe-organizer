This recipe organizer is intended to be management software for cooking recipes. The focus is on customized organization and usability. It is written in Java with Eclipse RCP as the framework and SWT UI.

I started the project with the intent to put a UI for recipes on a kitchen appliance that had touch screen, text-to-speech support, and voice/foot activation (for when the hands are covered in ingredients). The first version of the project I intend to make an editor and viewer using Eclipse RCP and some sort of data storage. A later version would work on an embedded device running something like Windows CE for use in a kitchen appliance (e.g., front of the fridge, in a device similar to a portable auto navigator).

I really couldn't care less how the data is stored, but it needs to be light enough to load on a WinCE-type device, a single archive would be nice, easily merged with other installations of the application, and compatible with a range of different interfaces. If it can be done on a single codebase then that's great, and the Eclipse platform (which I'm familiar with more than others) makes strides to reuse code for desktop, web, and embedded applications.

## Contributing ##
I knew there were a lot of recipe management programs out there, but didn't realize there were already so many already hosted on google code. If any of those projects are still alive and the manager finds this one, feel free to contact me to see if we can merge our resources.

Also, if you check out the documentation and like where this thing is headed, let me know and give me some motivation to keep working on it.