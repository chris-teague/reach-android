# Android Turbolinks Project

## Quick Setup
1. Check out this project
2. Open with Android Studio as an existing project
3. Let Gradle Sync run
4. Install ALL suggested dependencies (build tools etc)
5. ![wait](https://i.imgur.com/4FOhVsg.png)
6. Done!

Combine this project with `https://github.com/reinteractive/turbolinks_rails_template`

## Android AVD
You will need to download and install an Android Virtual Device. You can find this in the top menu bar (icons) called **AVD Manger**.
You should already have one, but if you want to test on older Android versions you can add more.

## Tips
* Always install the "Google Api's" versions of the SDK platforms.
* SDK Manager has a full list of extra goodies to install
* Always use the V4 and V7 support libs for the most backwards compatibility.
* Only use the **x86** device images for the Emulators, ARM will run too slow.
* Want to get all fancy and run some C in your app? Android NDK is your friend.

### Annoying facts about Android development
* Support libs don't automatically fix everything.
* Samsung sometimes override default Google callbacks in their own systems, breaking builds for their devices only.
