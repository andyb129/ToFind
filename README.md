# ToFind

This is a demo project to try and render the design from the Uplabs project below into a working app (as close as possible without the design resources :-) )

[https://www.uplabs.com/posts/tofind-concept-controller](https://www.uplabs.com/posts/tofind-concept-controller)

<p>
<img src="https://github.com/andyb129/ToFind/blob/master/screenshots%2to_find_anim.gif" height="600" alt="ToFind"/>
</p>

## Usage

To get the **Google map** working in the app you will have to register to get a Google maps API key (instructions here > [https://developers.google.com/maps/documentation/android-api/signup](https://developers.google.com/maps/documentation/android-api/signup))  
and then place this key in your **gradle.properties** file on your local machine before you build in the following format.

TO_FIND_GOOGLE_MAP_API_KEY=Abc12shshshshshiqeuwwr


### TODO
1. Get material transition working between ViewPager & Fragment
2. Add map above pager count on main screen & move with viewpager
3. Round corners on carousel box

### Thanks

This project uses the following libraries, custom views & resources to help me build it

* CarouselEffect by [bhaveshjabuvani-credencys](https://github.com/bhaveshjabuvani-credencys) - [https://github.com/bhaveshjabuvani-credencys/CarouselEffect](https://github.com/bhaveshjabuvani-credencys/CarouselEffect)
* CircleImageView by [hdodenhof](https://github.com/hdodenhof) - [https://github.com/hdodenhof/CircleImageView](https://github.com/hdodenhof/CircleImageView)
* KenBurnsView by [flavienlaurent.com](https://flavienlaurent.com) - [http://flavienlaurent.com/blog/2013/11/20/making-your-action-bar-not-boring/](http://flavienlaurent.com/blog/2013/11/20/making-your-action-bar-not-boring/)
* TabLayoutWithArrow by [konstantin-loginov](https://stackoverflow.com/users/1658267/konstantin-loginov) - [https://stackoverflow.com/questions/34235068/custom-tab-indicatorwith-arrow-down-like-indicator](https://stackoverflow.com/questions/34235068/custom-tab-indicatorwith-arrow-down-like-indicator)
* TickerView by [robinhood](https://github.com/robinhood) - [https://github.com/robinhood/ticker](https://github.com/robinhood/ticker)

### Licence
```
Copyright (c) 2016 Andy Barber

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
