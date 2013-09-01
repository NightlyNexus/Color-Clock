Color Clock
===========

An Android TextClock widget with a user-defined color.

Get Color Clock on Google Play: https://play.google.com/store/apps/details?id=com.brianco.colorclock&hl=en

Copyright (C) 2013 Eric Cochran <ericphysics@gatech.edu>

Color Clock is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

Color Clock is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

For a copy of the GNU General Public License, see 
<http://www.gnu.org/licenses/>.



Libraries
=========

android-ColorPickerPreference (net.margaritov.preference.colorpicker): https://github.com/attenzione/android-ColorPickerPreference under Apache License v2

Change at line 124 of ColorPickerDialog.java:
```java
//mOldColor.setOnClickListener(this);
View.OnClickListener ohBoy = new View.OnClickListener(){
	@Override
	public void onClick(View v) {
		if (mListener != null) {
			mListener.onColorChanged(mOldColor.getColor());
		}
		dismiss();
	}
};
//JIGGA
mOldColor.setOnClickListener(ohBoy);
```



Images
======

ic_launcher.png under Creative Commons

ic_drawer.png under Creative Commons
