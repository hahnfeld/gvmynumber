# GVMyNumber Design Philosophy #

## History ##

GVMyNumber is my third Google Voice app.

My first two apps never saw the light of day, but worked fine for personal use.

The first one simply triggered a callback using Google's web interface.  This was fairly easy to implement, but probably required a bit of screen-scraping, which Google discourages.  Plus, there are already apps out there that do this quite well.

The advantage to triggering a callback is that it can be used with Gizmo5 for SIP  (VOIP) callbacks.  This is fantastic for international travel when internet is available, but not mobile phone service.  It also lets you make calls from airplanes.  :)

My second app was similar to GVMyNumber, but more full-featured.  Instead of tying in with Google Voice, it was its own standalone dialer.  Because of this, it could do things like call out only during certain times (ie. non-night/weekend).  It was a bit heavier than I wanted for my own personal use, and the menus were a bit confusing.

## GVMyNumber ##

So, enter GVMyNumber.  This is a result of studying a lot of Android Source code and, well, figuring out how the native Google Voice app (probably) does its thing.  Obviously, the official Google Voice app is closed source, so I couldn't/didn't do any reverse engineering.  There are some pretty clear open source additions to the Android code on 2.x that seem to be built specifically for Google Voice, and those are where I focused my attention.

So, how does the official Google Voice app do its thing?  Easy!  It uses a standard broadcast receiver to intercept outgoing calls, pulls an access number down from the internet, spawns a new call intent, and sets a couple of new extras: com.android.phone.extra.GATEWAY\_PROVIDER\_PACKAGE and com.android.phone.extra.GATEWAY\_URI.  These are used to specify a "gateway", of which, nearest I can tell, Google Voice is the only one.

GATEWAY\_PROVIDER\_PACKAGE specifies where information about the gateway can be pulled.  It pulls information like the app name and icon, so it can display it in a little popup window when you place a call.  GATEWAY\_URI is where the magic happens.  This is an alternate URI that's used to dial the phone, instead of the user-specified phone number.  It can be set to anything, and the oficial app uses it to dial its random access number.

There are a few caveats.  First of all, GATEWAY\_URI must be a proper URI, and characters like "#" must be properly encoded or they'll be read as part of the URI.  Second, when a call is placed, all of the broadcast receivers will be called.  Assuming you don't return anything, the next one in line will be called.  If you start a new intent, everything starts over.  Each time your receiver is called, it has to know how to act.  This is a great place to spawn infinite loops if you're not careful.

So, GVMyNumber simply subscribes to outgoing calls, checks for those triggered by Google Voice (using GATEWAY\_PROVIDER\_PACKAGE), and re-sets GATEWAY\_URI to the google voice number configured instead of the random access number.  All-in-all, it's like 20 lines of code, and no hacking required.  I'm just using the standard Open Source android API to do everything.

So, there you have it!  A nice simple dialer, integrated with Google's fantastic Google Voice app for Android.

## Philosophy ##

Simple.  Free.  Open Source.  What else can I say?

There are a lot of big and/or expensive apps out there.  I hope this app encourages other people to develop free and open source applications for Android.  Or, heck, steal this code and make something even cooler out of it.  Just respect the GPL and respect Google -- they're the ones who made all this possible.