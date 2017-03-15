# Android-CBlock
#### Android application to block annoying incoming calls

  * ![link](../../../img/blob/master/cblock/main_screen.png) ![link](../../../img/blob/master/cblock/add_phone.png)

#### Add phone in block list in 3 clicks:

1. Click floating button `add`
2. Select phones from incoming calls list and click `done`
3. Click switch `Block Service`

When Service enabled, notification appears

#### Used libraries
 * [RxJava](https://github.com/ReactiveX/RxJava), [RxAndroid](https://github.com/ReactiveX/RxAndroid) - the core of [MVVM](https://en.wikipedia.org/wiki/Model%E2%80%93view%E2%80%93viewmodel) architecture
 * [Dagger](https://google.github.io/dagger/) - dependency injection framework
 * [SQLBrite](https://github.com/square/sqlbrite) - wrapper around ContentResolver, to get incoming calls list
 * [GreenDao](http://greenrobot.org/greendao/) - ORM, to save blocked phones in local DB
 * [AutoValue](https://github.com/google/auto/tree/master/value) - easy generate Data classes
 * [ButterKnife](http://jakewharton.github.io/butterknife/) - elegant way of view binding
